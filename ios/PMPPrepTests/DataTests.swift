import XCTest
@testable import PMPPrep

/** The data layer, checked the way the Android app's unit tests check it. */
final class DataTests: XCTestCase {
    private let D = Time.dayMs
    /** A Wednesday noon. */
    private lazy var now: Millis = {
        var c = DateComponents(); c.year = 2027; c.month = 3; c.day = 10; c.hour = 12
        return Time.millis(Calendar.current.date(from: c)!)
    }()

    private func a(_ qid: String, _ at: Millis, _ right: Bool, confidence: Int = 0, source: String = "practice") -> Attempt {
        Attempt(qid: qid, ecoTask: "t1", domain: .people, approach: nil, type: .single, credit: right ? 1 : 0, correct: right,
                ms: 60_000, at: at, source: source, confidence: confidence)
    }

    /* ── content ── */

    func testContentLoadsFromTheAppBundle() throws {
        let repo = ContentRepository(bundle: .main)
        try repo.load()
        XCTAssertGreaterThan(repo.pool().count, 100, "questions")
        XCTAssertEqual(repo.extras.tasks.count, 26)
        XCTAssertFalse(repo.extras.lessons.isEmpty)
        XCTAssertFalse(repo.extras.flashcards.isEmpty)
        XCTAssertEqual(repo.extras.exam.totalItems, 180)
        for q in repo.pool().prefix(200) where q.base == .single {
            XCTAssertEqual(q.options.filter(\.correct).count, 1, q.id)
        }
    }

    func testPackParsingSkipsInvalidItems() throws {
        let text = """
        {"type":"pmp-pack","name":"Test","questions":[
          {"id":"a","type":"single","domain":"people","stem":"S","options":[{"id":"1","text":"x","correct":true},{"id":"2","text":"y"}]},
          {"id":"b","type":"single","domain":"nowhere","stem":"S"},
          {"id":"a","type":"single","domain":"people","stem":"S","options":[{"id":"1","text":"x","correct":true},{"id":"2","text":"y"}]}
        ]}
        """
        let p = try ContentParser.parsePack(text, id: "t", fallbackName: nil, file: nil, builtin: false)
        XCTAssertEqual(p.name, "Test")
        XCTAssertEqual(p.questions.map(\.id), ["a"])
        XCTAssertEqual(p.invalid, 2)
    }

    /* ── scoring ── */

    func testScoringRules() {
        let q = Question(id: "m", type: .multi, domain: .people, ecoTask: "", approach: nil, difficulty: 3, tags: [], stem: "", explanation: "", mindset: "",
                         options: [Option(id: "a", text: "", correct: true, rationale: "", match: nil, fixed: false),
                                   Option(id: "b", text: "", correct: true, rationale: "", match: nil, fixed: false),
                                   Option(id: "c", text: "", correct: false, rationale: "", match: nil, fixed: false)])
        XCTAssertEqual(Scoring.score(q, Answer(choices: ["a", "b"])).credit, 1)
        XCTAssertEqual(Scoring.score(q, Answer(choices: ["a"])).credit, 0.5)
        XCTAssertEqual(Scoring.score(q, Answer(choices: ["a", "b", "c"])).credit, 0.5)
        XCTAssertEqual(Scoring.score(q, Answer(choices: ["a", "c"])).credit, 0)
        XCTAssertEqual(Scoring.parseNumber("(1,200)"), -1200)
        XCTAssertEqual(Scoring.parseNumber("$3.5"), 3.5)
        XCTAssertNil(Scoring.parseNumber("abc"))
        XCTAssertEqual(Scoring.normalise("  Café  Latte! "), "cafe latte")
    }

    /* ── progress: the Android app's backup format ── */

    func testReadsAnAndroidBackupAndWritesTheSameFormat() throws {
        let android = """
        {"type":"pmp-android-progress","version":2,
         "attempts":[{"q":"q1","t":"t1","d":"people","ap":"agile","ty":"single","cr":1.0,"ok":true,"ms":61000,"at":1700000000000,"src":"practice","cf":3,"el":2}],
         "flags":["q1"],"notes":{"q1":"note"},"mistakes":{"q2":{"c":"MISREAD_STEM","n":"","at":1700000000000}},
         "cards":{"c1":[2.5,6,2,0,1700500000000,1700000000000,1690000000000]},
         "mocks":[{"at":1700000000000,"title":"Full","n":180,"right":130,"usedMs":13800000,"domains":{"people":[50,76]}}],
         "lessons":["l1"],"plan":{"examDate":1710000000000,"hours":12,"days":[2,4,6],"onboarded":true,"rest":false},
         "sessions":[{"k":"build:t1","kind":"BUILD","t":"t1","d":"people","s":0.8,"m":30,"at":1700000000000}],
         "notepad":"hello","mycards":[{"id":"my:q1","t":"t1","f":"front","b":"back","n":""}]}
        """
        let dir = FileManager.default.temporaryDirectory.appendingPathComponent(UUID().uuidString)
        let s = ProgressStore(dir: dir)
        try s.restore(android)
        XCTAssertEqual(s.attempts.first?.confidence, Attempt.SURE)
        XCTAssertEqual(s.attempts.first?.eliminated, 2)
        XCTAssertEqual(s.attempts.first?.approach, .agile)
        XCTAssertEqual(s.mistakeTags["q2"]?.cause, .MISREAD_STEM)
        XCTAssertEqual(s.cards["c1"]?.interval, 6)
        XCTAssertEqual(s.mocks.first?.domains[.people], [50, 76])
        XCTAssertEqual(s.plan.studyDays, [2, 4, 6])
        XCTAssertFalse(s.plan.restDays)
        XCTAssertEqual(s.sessionResults.first?.kind, .BUILD)
        XCTAssertEqual(s.myCards.first?.deck, ProgressStore.MY_DECK)
        // And back out again, readable by the same parser.
        let again = ProgressStore(dir: dir.appendingPathComponent("b"))
        try again.restore(JWrite.string(s.toJson()))
        XCTAssertEqual(again.attempts, s.attempts)
        XCTAssertEqual(again.notepad, "hello")
        XCTAssertThrowsError(try again.restore("{\"type\":\"something-else\"}"))
    }

    /* ── missed-question review ── */

    func testMissedQuestionComesBackOnTheLadder() {
        let miss = a("q", now, false)
        let s0 = QuestionReview.state([miss])!
        XCTAssertEqual(s0.step, 0)
        XCTAssertEqual(s0.due, Time.startOfDay(now + D))
        XCTAssertEqual(QuestionReview.state([miss, a("q", now + 3_600_000, true)])!.step, 0, "right the same day doesn't count")
        var h = [miss]
        for step in 1...3 {
            let t = QuestionReview.state(h)!.due + 10 * 3_600_000
            h.append(a("q", t, true))
            XCTAssertEqual(QuestionReview.state(h)!.step, step)
        }
        let t = QuestionReview.state(h)!.due + 3_600_000
        XCTAssertNil(QuestionReview.state(h + [a("q", t, true)]), "graduates after the 21-day review")
        XCTAssertEqual(QuestionReview.allowance([a("q1", now, true, source: QuestionReview.SOURCE)], now), 4)
    }

    /* ── ready to book ── */

    private func week(_ i: Int, _ readiness: Int, _ fitness: Int?) -> WeeklyReport {
        WeeklyReport(weekStart: WeeklyReports.weekStart(now) - Millis(5 - i) * 7 * D, updatedAt: now, readiness: readiness, accuracy: 70,
                     tasksTouched: 10, mastered: 2, minutes: 300, days: 5, answers: 100, mocks: 0, mockAvg: nil, fitness: fitness,
                     stamina: nil, composure: nil, consistency: nil, wpm: nil, longest: 0, freezePct: nil, worry: nil)
    }

    func testBookingProjection() {
        let two = [week(4, 60, 60), week(5, 65, 65)]
        let p = Booking.check(readiness: 65, fitness: 65, mocks: [], fullItems: 180, weekly: two, examDate: nil).projection!
        XCTAssertTrue(p.rough)
        XCTAssertEqual(p.latest, two.last!.weekStart + 14 * D)
        let flat = [week(4, 60, 60), week(5, 58, 66)]
        XCTAssertEqual(Booking.check(readiness: 58, fitness: 66, mocks: [], fullItems: 180, weekly: flat, examDate: nil).projection!.stalled, ["Readiness"])
        let mocks = [MockResult(at: now, title: "", n: 180, right: 126, usedMs: 0, domains: [:]), MockResult(at: now, title: "", n: 180, right: 120, usedMs: 0, domains: [:])]
        XCTAssertTrue(Booking.check(readiness: 80, fitness: 72, mocks: mocks, fullItems: 180, weekly: [], examDate: nil).ready)
    }

    /* ── rest days ── */

    func testOneMissedDayAWeekIsKept() {
        let today = Time.startOfDay(now)
        func day(_ n: Int) -> Millis { var d = today; for _ in 0..<abs(n) { d = n < 0 ? Time.prevDay(d) : Time.nextDay(d) }; return d }
        let studied = Set([-8, -7, -5, -3, -1, 0].map(day))
        let kept = Readiness.withRestDays(studied, today)
        XCTAssertTrue(kept.contains(day(-6)))
        XCTAssertFalse(kept.contains(day(-4)))
        XCTAssertTrue(kept.contains(day(-2)))
    }

    /* ── Gemini line-up ── */

    func testGeminiLineup() {
        func models(_ names: String...) -> [Gemini.Model] {
            Gemini.parseModels("{\"models\":[" + names.map { "{\"name\":\"models/\($0)\",\"supportedGenerationMethods\":[\"generateContent\"]}" }.joined(separator: ",") + "]}")
        }
        XCTAssertEqual(Gemini.lineup(models("gemini-3.5-flash", "gemini-3.5-flash-preview", "gemini-4-flash", "gemini-4-flash-lite", "gemini-3-flash")).map(\.name),
                       ["models/gemini-4-flash", "models/gemini-3.5-flash", "models/gemini-3.5-flash-preview"])
        let crowded = Gemini.lineup(models("gemini-3.5-flash", "gemini-4-flash", "gemini-4.5-flash", "gemini-5-flash", "gemini-5.5-flash", "gemini-6-flash"))
        XCTAssertEqual(crowded.count, 5)
        XCTAssertEqual(crowded.last?.name, "models/gemini-3.5-flash")
        XCTAssertTrue(Gemini.tryNextModel(Gemini.GeminiError(message: "", status: 429)))
        XCTAssertFalse(Gemini.tryNextModel(Gemini.GeminiError(message: "", status: 400, keyProblem: true)))
        XCTAssertFalse(Gemini.tryNextModel(Gemini.GeminiError(message: "", status: 0)))
    }

    /* ── widget ── */

    func testWidgetLevelsAndColours() {
        XCTAssertEqual(WidgetStats.heat(0).0, 214)
        XCTAssertEqual(WidgetStats.heat(1).1, 158)
        let s = WidgetStats(examDate: now + 90 * D, day: Time.startOfDay(now), todayMinutes: 30, targetMinutes: 60, readiness: 50, lessonsDone: 1, lessonsTotal: 4)
        XCTAssertEqual(s.daysLevel(now), 1)
        XCTAssertEqual(s.today(now), 0.5, accuracy: 1e-9)
        var r = s; r.readyState = WidgetStats.READY_PROJECTED; r.readyBy = now + 100 * D
        XCTAssertEqual(r.onTrack(), false)
    }

    /* ── the plan builds from real content ── */

    func testPlanBuildsFromRealContent() throws {
        let repo = ContentRepository(bundle: .main)
        try repo.load()
        let plan = PlanEngine.build(PlanInput(x: repo.extras, attempts: [], lessonsDone: [], results: [], mocks: [], diagnostic: nil,
                                              settings: PlanSettings(examDate: now + 60 * D, hoursPerWeek: 10, studyDays: [2, 3, 4, 5, 6]), now: now))
        XCTAssertFalse(plan.sessions.isEmpty)
        XCTAssertFalse(plan.phase.isEmpty)
        XCTAssertEqual(plan.daysLeft, 60)
    }
}
