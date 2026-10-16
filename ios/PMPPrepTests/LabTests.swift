import XCTest
@testable import PMPPrep

/** The formula lab, the drill generators, hints, adaptive practice, pretest items, analytics and the drills log. */
final class LabTests: XCTestCase {

    private func attempt(_ qid: String, task: String, at: Millis, right: Bool, ms: Int64 = 60_000,
                         confidence: Int = 0, hints: Int = 0, source: String = "practice") -> Attempt {
        Attempt(qid: qid, ecoTask: task, domain: .people, approach: nil, type: .single, credit: right ? 1 : 0, correct: right,
                ms: ms, at: at, source: source, confidence: confidence, hints: hints)
    }

    private func option(_ id: String, _ correct: Bool, _ why: String = "") -> Option {
        Option(id: id, text: id, correct: correct, rationale: why, match: nil, fixed: false)
    }

    /* ── calculators ── */

    func testNetworkPassesMatchTheWebExample() {
        let list: [NetInput] = [
            NetInput(id: "A", dur: 3, preds: []), NetInput(id: "B", dur: 4, preds: ["A"]), NetInput(id: "C", dur: 2, preds: ["A"]),
            NetInput(id: "D", dur: 5, preds: ["B"]), NetInput(id: "E", dur: 3, preds: ["C"]), NetInput(id: "F", dur: 2, preds: ["D", "E"]),
        ]
        let r: NetResult = FormulaMath.network(list)
        XCTAssertTrue(r.ok)
        XCTAssertEqual(r.duration, 14)
        XCTAssertEqual(r.critical, ["A", "B", "D", "F"])
        XCTAssertEqual(r.activity("A")?.es, 1)
        XCTAssertEqual(r.activity("A")?.ef, 3)
        XCTAssertEqual(r.activity("C")?.tf, 4)
        XCTAssertEqual(r.activity("C")?.ff, 0)
        XCTAssertEqual(r.activity("E")?.ff, 4)
        XCTAssertEqual(r.columns.count, 4)
        XCTAssertFalse(r.multipleCritical)
        XCTAssertFalse(FormulaMath.network([NetInput(id: "A", dur: 1, preds: ["B"]), NetInput(id: "B", dur: 1, preds: ["A"])]).ok, "a loop")
        XCTAssertFalse(FormulaMath.network([NetInput(id: "A", dur: 1, preds: ["Z"])]).ok, "an unknown predecessor")
    }

    func testCashFlowTreeAndPayback() throws {
        let c: CashResult = FormulaMath.cash(400000, [120000, 160000, 180000, 150000], 0.10)
        let y1: Double = 120000.0 / 1.1
        let y2: Double = 160000.0 / 1.21
        let y3: Double = 180000.0 / 1.331
        let y4: Double = 150000.0 / 1.4641
        let expectedPv: Double = y1 + y2 + y3 + y4
        XCTAssertEqual(c.pvIn, expectedPv, accuracy: 0.01)
        XCTAssertEqual(c.npv, c.pvIn - 400000, accuracy: 0.001)
        XCTAssertEqual(c.payback ?? 0, 2 + 120000.0 / 180000.0, accuracy: 1e-9)
        let irr: Double = try XCTUnwrap(c.irr)
        XCTAssertEqual(FormulaMath.npvAt(400000, [120000, 160000, 180000, 150000], irr), 0, accuracy: 0.01)
        XCTAssertNil(FormulaMath.payback(500, [100, 100]))

        let t: [TreeBranch] = FormulaMath.tree([
            TreeBranchInput(name: "Build new", cost: 500000, outcomes: [TreeOutcome(p: 0.6, payoff: 1200000), TreeOutcome(p: 0.4, payoff: 400000)]),
            TreeBranchInput(name: "Upgrade", cost: 180000, outcomes: [TreeOutcome(p: 0.6, payoff: 700000), TreeOutcome(p: 0.4, payoff: 300000)]),
        ])
        XCTAssertEqual(t[0].emv, 880000, accuracy: 0.001)
        XCTAssertEqual(t[0].net, 380000, accuracy: 0.001)
        XCTAssertEqual(t[1].net, 360000, accuracy: 0.001)
        XCTAssertTrue(t[0].best)
        XCTAssertFalse(t[1].best)
    }

    /* ── drill generators ── */

    func testDrillNumberFormatting() {
        XCTAssertEqual(DrillFmt.fmt(-40000, 0), "-40,000")
        XCTAssertEqual(DrillFmt.fmt(0.875, 2), "0.88")
        XCTAssertEqual(DrillFmt.fmt(-0.001, 2), "0.00")
        XCTAssertEqual(DrillFmt.mn(745555.02), "745,555.02")
        XCTAssertEqual(DrillFmt.mn(1200000), "1,200,000")
        XCTAssertEqual(DrillFmt.fmt(.nan, 2), "—")
        XCTAssertEqual(DrillFmt.chain("CV", ["EV − AC", "1 - 2", "-1"]), "CV = EV − AC\n   = 1 - 2\n   = -1")
    }

    func testEveryGeneratorMarksItsOwnAnswerRight() {
        XCTAssertEqual(DrillGenerators.all.count, 18)
        XCTAssertEqual(Set(DrillGenerators.all.map { $0.id }).count, 18, "ids are unique")
        for g in DrillGenerators.all {
            for seed in UInt32(1)...UInt32(60) {
                let p: DrillProblem = DrillGenerators.generate(g, seed: seed &* 2654435761)
                XCTAssertEqual(p.gen, g.id)
                XCTAssertEqual(p.group, g.group, g.id)
                XCTAssertTrue(p.answer.isFinite, "\(g.id) seed \(seed)")
                XCTAssertFalse(p.steps.isEmpty, g.id)
                XCTAssertFalse(p.given.isEmpty, g.id)
                XCTAssertFalse(p.stem.isEmpty, g.id)
                let typed: String = DrillFmt.fmt(p.answer, p.dp)
                XCTAssertEqual(DrillGenerators.mark(p, typed).verdict, .right, "\(g.id) seed \(seed): \(typed)")
                let tol: Double = DrillGenerators.tolFor(p)
                for a in p.alts {
                    XCTAssertGreaterThan(abs(a.v - p.answer), tol, "\(g.id): an alt sits on the answer")
                    let m: DrillMark = DrillGenerators.mark(p, String(a.v))
                    XCTAssertEqual(m.verdict, .wrong, "\(g.id): \(a.why)")
                    XCTAssertNotNil(m.why)
                }
            }
        }
        XCTAssertEqual(DrillGenerators.groupOf("float"), "schedule")
        XCTAssertNil(DrillGenerators.groupOf("nope"))
        XCTAssertEqual(DrillGenerators.mark(DrillGenerators.generate(DrillGenerators.all[0], seed: 7), "abc").verdict, .unreadable)
    }

    func testSameSeedSameProblem() {
        let a: DrillProblem = DrillGenerators.newProblem(group: "finance", seed: 12345)
        let b: DrillProblem = DrillGenerators.newProblem(group: "finance", seed: 12345)
        XCTAssertEqual(a.stem, b.stem)
        XCTAssertEqual(a.answer, b.answer)
        XCTAssertEqual(DrillGenerators.groupOf(a.gen), "finance")
    }

    /* ── hint ladder ── */

    func testHintLadderRungs() {
        let q = Question(id: "q", type: .single, domain: .people, ecoTask: "t1", approach: .agile, difficulty: 3, tags: [], stem: "S",
                         explanation: "", mindset: "Servant leadership first.",
                         options: [option("a", true), option("b", false, "B is wrong because."), option("c", false, "C is wrong.")])
        let lesson = Lesson(id: "L1", module: "m", order: 1, ecoTask: "t1", minutes: 10, title: "T", objectives: [], examWeight: "",
                            sections: [], lenses: [("agile", "Agile lens text")], worked: nil, traps: ["Trap one"], checks: [],
                            terms: [], recap: [], mindset: "")
        let h1 = HintLadder.hint(q, rung: 1, lesson: lesson, options: q.options, chosen: [])
        XCTAssertTrue(h1.text.contains("agile"))
        XCTAssertEqual(h1.detail, "Agile lens text")
        XCTAssertEqual(HintLadder.hint(q, rung: 2, lesson: lesson, options: q.options, chosen: []).detail, "Servant leadership first.")
        let h3 = HintLadder.hint(q, rung: 3, lesson: lesson, options: q.options, chosen: ["b"])
        XCTAssertEqual(h3.strike, "c", "never the option already chosen, never the right one")
        XCTAssertEqual(h3.detail, "C is wrong.")
        let fill = Question(id: "f", type: .fillblank, domain: .people, ecoTask: "t1", approach: nil, difficulty: 3, tags: [], stem: "S",
                        explanation: "", mindset: "")
        let t3 = HintLadder.hint(fill, rung: 3, lesson: lesson, options: [], chosen: [])
        XCTAssertNil(t3.strike)
        XCTAssertEqual(t3.detail, "Trap one")
        XCTAssertNil(HintLadder.hint(fill, rung: 1, lesson: nil, options: [], chosen: []).detail)

        let modules = [LessonModule(id: "m", order: 1, prefix: "P", label: "People", domain: .people, blurb: ""),
                       LessonModule(id: "f", order: 0, prefix: "F", label: "Foundations", domain: nil, blurb: "")]
        let foundation = Lesson(id: "F1", module: "f", order: 0, ecoTask: "t1", minutes: 5, title: "F", objectives: [], examWeight: "",
                            sections: [], lenses: [], worked: nil, traps: [], checks: [], terms: [], recap: [], mindset: "")
        XCTAssertEqual(HintLadder.lessonFor("t1", lessons: [foundation, lesson], modules: modules)?.id, "L1")
    }

    func testHintedAnswersStayOutOfAccuracyAndAnalytics() {
        let atts: [Attempt] = [attempt("a", task: "t", at: 1, right: true, hints: 2), attempt("b", task: "t", at: 2, right: false)]
        XCTAssertEqual(AnalyticsData.evidence(atts).map { $0.qid }, ["b"])
        XCTAssertEqual(AdaptivePicker.taskIndex(atts)["t"]?.n, 1)
    }

    /* ── adaptive practice ── */

    func testAdaptiveWeights() {
        let unseenNoHistory: Double = AdaptivePicker.weight(nil, lastCorrect: nil)
        XCTAssertEqual(unseenNoHistory, (pow(0.5, 1.5) + 0.05) * 1.4, accuracy: 1e-9)
        let weak = AdaptivePicker.TaskStat(n: 10, credit: 2)
        let strong = AdaptivePicker.TaskStat(n: 10, credit: 9)
        XCTAssertGreaterThan(AdaptivePicker.weight(weak, lastCorrect: false), AdaptivePicker.weight(strong, lastCorrect: false))
        XCTAssertEqual(AdaptivePicker.weight(weak, lastCorrect: true), AdaptivePicker.weight(weak, lastCorrect: false) * 0.5, accuracy: 1e-12)

        let drawn: [String] = AdaptivePicker.sample(["zero", "a", "b"], [0, 1, 1], 2, { 0.5 })
        XCTAssertEqual(Set(drawn), ["a", "b"], "a zero weight is never drawn while a positive one remains")
        XCTAssertEqual(AdaptivePicker.sample(["x", "y"], [0, 0], 2, { 0.3 }).count, 2, "all zero falls back to a flat draw")

        // Over many draws, the weak task's questions dominate.
        var qs: [Question] = []
        for i in 0..<20 {
            let task: String = i < 10 ? "weak" : "strong"
            qs.append(Question(id: "\(task)\(i)", type: .single, domain: .people, ecoTask: task, approach: nil, difficulty: 3, tags: [],
                               stem: "", explanation: "", mindset: ""))
        }
        var history: [Attempt] = []
        for i in 0..<20 { history.append(attempt("h\(i)", task: "weak", at: Millis(i), right: i % 5 == 0)) }
        for i in 0..<20 { history.append(attempt("s\(i)", task: "strong", at: Millis(i), right: true)) }
        var weakCount: Int = 0
        for _ in 0..<50 {
            let pick: [Question] = AdaptivePicker.pick(qs, attempts: history, lastCorrect: { _ in nil }, count: 5)
            XCTAssertEqual(pick.count, 5)
            weakCount += pick.filter { $0.ecoTask == "weak" }.count
        }
        XCTAssertGreaterThan(weakCount, 175)
    }

    /* ── mock pretest items ── */

    func testPretestCount() {
        XCTAssertEqual(MockPlan.pretestCount(180, totalItems: 180), 10)
        XCTAssertEqual(MockPlan.pretestCount(90, totalItems: 180), 5)
        XCTAssertEqual(MockPlan.pretestCount(45, totalItems: 180), 2)
        XCTAssertEqual(MockPlan.pretestCount(20, totalItems: 180), 1)
        XCTAssertEqual(MockPlan.pretestCount(1, totalItems: 180), 0)
    }

    /* ── analytics ── */

    func testTrendCalibrationAndPace() {
        var atts: [Attempt] = []
        for i in 0..<60 { atts.append(attempt("q\(i)", task: "t", at: Millis(i), right: i % 2 == 0, ms: Int64(10_000 + i * 2_000), confidence: (i % 3) + 1)) }
        let t: [TrendPoint] = AnalyticsData.trend(atts)
        XCTAssertEqual(t.first?.i, 10)
        XCTAssertEqual(t.first?.full, false)
        XCTAssertEqual(t.first(where: { $0.full })?.i, 50)
        XCTAssertEqual(t.last?.v ?? 0, 0.5, accuracy: 1e-9)
        XCTAssertTrue(AnalyticsData.trend(Array(atts.prefix(9))).isEmpty)

        let c: Calibration = AnalyticsData.calibration(atts)
        XCTAssertEqual(c.total, 60)
        XCTAssertEqual(c.sure, 20)
        XCTAssertEqual(c.sureRight + c.unsureRight, 30)

        let bins: [PaceBin] = AnalyticsData.paceBins(AnalyticsData.paceSamples(atts))
        XCTAssertEqual(bins.count, 13)
        XCTAssertEqual(bins.reduce(0) { $0 + $1.n }, 60)
        XCTAssertNil(bins.last?.hi)
        XCTAssertEqual(AnalyticsData.median([1, 3, 2, 4]), 2.5)

        let task = EcoTask(id: "t", domain: .people, n: 1, label: "T", summary: "")
        let other = EcoTask(id: "u", domain: .people, n: 2, label: "U", summary: "")
        let heat: [HeatCell] = AnalyticsData.heat(atts + [attempt("x", task: "u", at: 99, right: true)], tasks: [task, other])
        XCTAssertFalse(heat[0].thin)
        XCTAssertTrue(heat[1].thin)
        XCTAssertEqual(heat[1].n, 1)
    }

    /* ── the drills log ── */

    func testDrillsRoundTripMergeAndGroups() throws {
        let dir = FileManager.default.temporaryDirectory.appendingPathComponent(UUID().uuidString)
        let s = ProgressStore(dir: dir)
        s.recordDrill(DrillRun(at: 1_700_000_000_000, generator: "npv", ok: true))
        s.recordDrill(DrillRun(at: 1_700_000_100_000, generator: "float", ok: false))
        let out: [String: Any] = s.toJson()
        let rows: [[String: Any]] = out["drills"] as? [[String: Any]] ?? []
        XCTAssertEqual(rows.count, 2)
        XCTAssertEqual(rows[0]["g"] as? String, "npv")
        let again = ProgressStore(dir: dir.appendingPathComponent("b"))
        try again.restore(JWrite.string(out))
        XCTAssertEqual(again.drills, s.drills)

        let local: [String: Any] = ["drills": [["at": 1, "g": "npv", "ok": true], ["at": 2, "g": "pta", "ok": false]]]
        let remote: [String: Any] = ["drills": [["at": 2, "g": "pta", "ok": true], ["at": 3, "g": "irr", "ok": true]]]
        let merged = SyncMerge.merge(base: nil, local: local, remote: remote, now: 10, device: ["app": "ios", "id": "t"])
        let list: [[String: Any]] = merged["drills"] as? [[String: Any]] ?? []
        XCTAssertEqual(list.map { SyncMerge.text($0["g"]) }, ["npv", "pta", "irr"])
        XCTAssertEqual(list[1]["ok"] as? Bool, false, "the local record wins")

        let groups: [DrillGroupStat] = AnalyticsData.drillGroups(s.drills + [DrillRun(at: 5, generator: "irr", ok: true)])
        XCTAssertEqual(groups.map { $0.group }, ["finance", "schedule"])
        XCTAssertEqual(groups[0].n, 2)
        XCTAssertEqual(groups[0].right, 2)

        for i in 0..<(ProgressStore.maxDrills + 5) { s.drills.append(DrillRun(at: Millis(i), generator: "npv", ok: true)) }
        s.recordDrill(DrillRun(at: 9, generator: "npv", ok: true))
        XCTAssertEqual(s.drills.count, ProgressStore.maxDrills)
    }
}
