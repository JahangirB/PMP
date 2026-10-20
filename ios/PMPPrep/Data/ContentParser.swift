import Foundation

struct PackError: LocalizedError {
    let message: String
    init(_ m: String) { message = m }
    var errorDescription: String? { message }
}

/** Turns the content JSON (and imported packs) into model types. Pure, so it is unit-tested. */
enum ContentParser {

    /* ── question packs ──────────────────────────────────────────────────── */

    /**
     * Parses a pmp-pack file (or a bare array of questions). Invalid items are
     * skipped and listed in errors; ids repeated inside the file are skipped too.
     * Duplicates against other packs are handled by the caller.
     */
    static func parsePack(_ text: String, id: String, fallbackName: String?, file: String?, builtin: Bool) throws -> QuestionPack {
        var trimmed = text.trimmingCharacters(in: .whitespacesAndNewlines)
        if trimmed.hasPrefix("\u{FEFF}") { trimmed.removeFirst() }
        var name = fallbackName
        let items: [Any]
        do {
            let any = try JSONSerialization.jsonObject(with: Data(trimmed.utf8))
            if let a = any as? [Any] { items = a }
            else if let o = JObj(any) {
                let type = o.optString("type", "pmp-pack")
                if type != "pmp-pack" { throw PackError("This is not a question pack (its type is \"\(type)\").") }
                if o.optInt("schemaVersion", 2) > 2 { throw PackError("This pack was made for a newer version of the app.") }
                guard let q = o.arr("questions") else { throw PackError("The file has no \"questions\" list.") }
                items = q
                if o.has("name") { name = o.optString("name") }
            } else { throw PackError("The file is not valid JSON.") }
        } catch let e as PackError { throw e }
        catch { throw PackError("The file is not valid JSON: \(error.localizedDescription)") }

        var out: [Question] = []
        var errors: [String] = []
        var seen = Set<String>()
        var invalid = 0
        for (i, item) in items.enumerated() {
            let o = JObj(item)
            var err = o == nil ? "not an object" : validate(o!)
            if err == nil, !seen.insert(o!.optString("id")).inserted { err = "id repeated inside this file" }
            if let err {
                invalid += 1
                if errors.count < 20 {
                    let qid = o?.optString("id") ?? ""
                    errors.append("#\(i + 1)\(qid.isEmpty ? "" : " (\(qid))"): \(err)")
                }
                continue
            }
            out.append(question(o!, packId: id))
        }
        return QuestionPack(id: id, name: name ?? file.map { $0.replacingOccurrences(of: ".json", with: "") } ?? id,
                            file: file, builtin: builtin, questions: out, invalid: invalid, errors: errors)
    }

    /** Returns nil when the item can be shown and scored, otherwise the reason it cannot. */
    static func validate(_ o: JObj) -> String? {
        if o.optString("id").trimmingCharacters(in: .whitespaces).isEmpty { return "missing id" }
        guard let type = QType.of(o.optString("type")) else { return "unknown type \"\(o.optString("type"))\"" }
        if Domain.of(o.optString("domain")) == nil { return "domain must be people, process or business" }
        if o.optString("stem").trimmingCharacters(in: .whitespaces).isEmpty { return "missing stem" }
        switch type.base {
        case .single, .multi, .matching:
            let opts = o.objects("options")
            if opts.count < 2 { return "needs at least two options" }
            for (i, op) in opts.enumerated() where op.optString("id").isEmpty || op.optString("text").isEmpty {
                return "option \(i + 1) needs an id and text"
            }
            let correct = opts.filter { $0.optBool("correct") }.count
            if type.base == .single && correct != 1 { return "a single-answer item needs exactly one correct option" }
            if type.base == .multi && correct < 1 { return "a multiple-response item needs correct options" }
            if type.base == .matching {
                let prompts = o.objects("prompts")
                if prompts.isEmpty { return "matching needs prompts" }
                for (i, p) in prompts.enumerated() {
                    let pid = p.optString("id")
                    if pid.isEmpty || !opts.contains(where: { $0.strOrNil("match") == pid }) { return "prompt \(i + 1) has no matching option" }
                }
            }
        case .hotspot:
            let regions = o.objects("regions")
            if regions.isEmpty { return "hotspot needs regions" }
            for (i, r) in regions.enumerated() where !(r.has("x") && r.has("y") && r.has("w") && r.has("h")) {
                return "region \(i + 1) needs x, y, w and h"
            }
            if !regions.contains(where: { $0.optBool("correct") }) { return "hotspot needs a correct region" }
        case .fillblank:
            let blanks = o.objects("blanks")
            if blanks.isEmpty { return "fill-in-the-blank needs blanks" }
            for (i, b) in blanks.enumerated() {
                if b.optString("id").isEmpty { return "blank \(i + 1) needs an id" }
                let ok = b.optBool("numeric") ? b.has("expect") : !(b.arr("accept") ?? []).isEmpty
                if !ok { return "blank \(i + 1) needs expect (numeric) or accept (text)" }
            }
        default: break
        }
        return nil
    }

    static func question(_ o: JObj, packId: String, fallbackDomain: Domain = .process) -> Question {
        let type = QType.of(o.optString("type")) ?? .single
        let regions = o.objects("regions").enumerated().map { i, r in
            Region(id: r.str("id"), n: r.optInt("n", i + 1), label: r.str("label"),
                   x: r.optDouble("x"), y: r.optDouble("y"), w: r.optDouble("w"), h: r.optDouble("h"),
                   correct: r.optBool("correct"), rationale: r.str("rationale"))
        }
        var q = Question(
            id: o.str("id"), type: type,
            domain: Domain.of(o.optString("domain")) ?? fallbackDomain,
            ecoTask: o.str("ecoTask"), approach: Approach.of(o.optString("approach")),
            difficulty: o.optInt("difficulty", 3), tags: o.strings("tags"),
            stem: o.str("stem"), explanation: o.str("explanation"), mindset: o.str("mindset"))
        q.options = o.objects("options").map { op in
            Option(id: op.str("id"), text: op.str("text"), correct: op.optBool("correct"),
                   rationale: op.str("rationale"), match: op.strOrNil("match"), fixed: op.optBool("fixed"))
        }
        q.prompts = o.objects("prompts").map { Prompt(id: $0.str("id"), text: $0.str("text")) }
        q.regions = regions
        if let g = o.obj("graphic") {
            let vb = g.optString("viewBox", "0 0 660 130").trimmingCharacters(in: .whitespaces)
                .components(separatedBy: CharacterSet(charactersIn: " ,\t\n")).compactMap { Double($0) }
            q.graphic = Graphic(viewBox: vb.count == 4 ? vb : [0, 0, 660, 130], title: g.str("title"), caption: g.str("caption"), flow: g.optBool("flow", true))
        }
        q.blanks = o.objects("blanks").map { b in
            Blank(id: b.str("id"), label: b.str("label"), numeric: b.optBool("numeric"),
                  expect: b.optDouble("expect"), tol: b.optDouble("tol"), unit: b.str("unit"),
                  accept: b.strings("accept"), rationale: b.str("rationale"))
        }
        q.multiSelect = type.base == .hotspot && (o.optBool("multi") || regions.filter(\.correct).count > 1)
        q.packId = packId
        return q
    }

    /* ── everything else ─────────────────────────────────────────────────── */

    static func extras(_ text: String) throws -> Extras {
        let o = try JObj.parse(text)
        let eco = o.obj("eco") ?? JObj([:])
        let weights = eco.obj("DOMAIN_WEIGHTS")
        let tasks = o.objects("tasks").compactMap { t -> EcoTask? in
            guard let d = Domain.of(t.optString("domain")) else { return nil }
            return EcoTask(id: t.str("id"), domain: d, n: t.optInt("n"), label: t.str("label"), summary: t.str("summary"))
        }
        let taskDomain = Dictionary(tasks.map { ($0.id, $0.domain) }, uniquingKeysWith: { a, _ in a })
        let exam = o.obj("exam") ?? JObj([:])
        let srs = o.obj("srs") ?? JObj([:])
        let rd = o.obj("readiness") ?? JObj([:])
        let rw = rd.obj("WEIGHTS") ?? JObj([:])

        let modules = o.objects("modules").map { m -> LessonModule in
            let d = Domain.of(m.strOrNil("domain"))
            return LessonModule(id: m.str("id"), order: m.optInt("order"), prefix: m.str("prefix"),
                                label: m.strOrNil("label") ?? d?.title ?? m.str("id"), domain: d, blurb: m.str("blurb"))
        }
        let moduleDomain = Dictionary(modules.map { ($0.id, $0.domain) }, uniquingKeysWith: { a, _ in a })

        let lessons = o.objects("lessons").map { l -> Lesson in
            let moduleD: Domain? = moduleDomain[l.str("module")] ?? nil
            let fallback: Domain = taskDomain[l.str("ecoTask")] ?? moduleD ?? .process
            let lensObj = l.obj("lenses")
            let order = ["predictive", "agile", "hybrid"]
            let lensKeys = (lensObj?.keys ?? []).sorted { (order.firstIndex(of: $0) ?? 9, $0) < (order.firstIndex(of: $1) ?? 9, $1) }
            var worked: Scenario? = nil
            if let w = l.obj("worked") {
                var d = w.d; d["id"] = l.str("id") + "-worked"
                worked = scenario(JObj(d))
            }
            let checks = l.objects("checks").compactMap { c -> Question? in
                // Checks carry no domain of their own; give them the lesson's before validating.
                var d = c.d
                if d["domain"] == nil { d["domain"] = (taskDomain[c.str("ecoTask")] ?? fallback).rawValue }
                let cj = JObj(d)
                return validate(cj) == nil ? question(cj, packId: "lesson:" + l.str("id"), fallbackDomain: fallback) : nil
            }
            return Lesson(
                id: l.str("id"), module: l.str("module"), order: l.optInt("order"), ecoTask: l.str("ecoTask"),
                minutes: l.optInt("minutes"), title: l.str("title"), objectives: l.strings("objectives"),
                examWeight: l.str("examWeight"),
                sections: l.objects("sections").map { LessonSection(heading: $0.str("heading"), body: $0.str("body"), figure: ContentParser.figure($0.obj("figure"))) },
                lenses: lensKeys.map { ($0, lensObj?.optString($0) ?? "") },
                worked: worked, traps: l.strings("traps"), checks: checks,
                terms: l.strings("terms"), recap: l.strings("recap"), mindset: l.str("mindset"))
        }

        return Extras(
            ecoLabel: eco.str("LABEL"),
            domainWeights: Dictionary(uniqueKeysWithValues: Domain.allCases.map { ($0, weights?.optDouble($0.rawValue) ?? 0) }),
            exam: ExamLayout(totalItems: exam.optInt("TOTAL_ITEMS", 180), minutes: exam.optInt("MINUTES", 230),
                             breakMinutes: exam.optInt("BREAK_MINUTES", 10),
                             breakAfter: [exam.optInt("BREAK_1_AFTER", 60), exam.optInt("BREAK_2_AFTER", 120)],
                             paceTargetSec: Int(exam.optDouble("PACE_TARGET_SEC", 80)),
                             caseBlockItems: exam.optInt("CASE_BLOCK_ITEMS", 40)),
            srs: SrsConfig(
                easeStart: srs.optDouble("EASE_START", 2.5), easeMin: srs.optDouble("EASE_MIN", 1.3),
                easeAgain: srs.optDouble("EASE_AGAIN", 0.2), easeHard: srs.optDouble("EASE_HARD", 0.15),
                easeEasy: srs.optDouble("EASE_EASY", 0.15), interval1: srs.optInt("INTERVAL_1", 1),
                interval2: srs.optInt("INTERVAL_2", 6), intervalEasy: srs.optInt("INTERVAL_EASY", 4),
                lapseInterval: srs.optInt("LAPSE_INTERVAL", 1), hardFactor: srs.optDouble("HARD_FACTOR", 1.2),
                easyBonus: srs.optDouble("EASY_BONUS", 1.3), maxInterval: srs.optInt("MAX_INTERVAL", 365),
                newPerDay: srs.optInt("NEW_PER_DAY", 20), leechThreshold: srs.optInt("LEECH_THRESHOLD", 6)),
            readiness: ReadinessConfig(
                wAccuracy: rw.optInt("accuracy", 35), wCoverage: rw.optInt("coverage", 20), wMocks: rw.optInt("mocks", 25),
                wPace: rw.optInt("pace", 10), wHabit: rw.optInt("habit", 10),
                accuracyHalflifeDays: rd.optDouble("ACCURACY_HALFLIFE_DAYS", 21),
                accuracyPrior: rd.optDouble("ACCURACY_PRIOR", 0.5), accuracyPriorN: rd.optDouble("ACCURACY_PRIOR_N", 8),
                accuracyWindow: rd.optInt("ACCURACY_WINDOW", 400),
                accuracyMin: rd.optDouble("ACCURACY_MIN", 0.4), accuracyMax: rd.optDouble("ACCURACY_MAX", 0.85),
                coverageTargetPerTask: rd.optInt("COVERAGE_TARGET_PER_TASK", 5),
                mockWeights: rd.doubles("MOCK_WEIGHTS") ?? [0.5, 0.3, 0.2],
                mockMin: rd.optDouble("MOCK_MIN", 0.45), mockMax: rd.optDouble("MOCK_MAX", 0.8),
                mockStaleAfterDays: rd.optDouble("MOCK_STALE_AFTER_DAYS", 45),
                mockStaleHalflife: rd.optDouble("MOCK_STALE_HALFLIFE", 90), mockStaleFloor: rd.optDouble("MOCK_STALE_FLOOR", 0.5),
                paceBand: rd.doubles("PACE_BAND") ?? [45, 80],
                paceSlowZero: rd.optDouble("PACE_SLOW_ZERO", 130), paceFastZero: rd.optDouble("PACE_FAST_ZERO", 15),
                paceMinSample: rd.optInt("PACE_MIN_SAMPLE", 30),
                habitWindowDays: rd.optInt("HABIT_WINDOW_DAYS", 14), habitTargetDays: rd.optInt("HABIT_TARGET_DAYS", 10),
                gateItems: rd.optInt("GATE_ITEMS", 100), gateTasks: rd.optInt("GATE_TASKS", 15)),
            practicePaceSec: o.obj("practice")?.optInt("PACE_TARGET_SEC", 76) ?? 76,
            tasks: tasks,
            scenarios: o.objects("scenarios").map { scenario($0) },
            decks: o.objects("decks").map { Deck(id: $0.str("id"), label: $0.str("label"), ecoTask: $0.strOrNil("ecoTask"), summary: $0.str("summary")) },
            flashcards: o.objects("flashcards").map {
                Flashcard(id: $0.str("id"), deck: $0.str("deck"), ecoTask: $0.strOrNil("ecoTask"), front: $0.str("front"), back: $0.str("back"), note: $0.str("note"))
            },
            formulas: o.objects("formulas").map { g in
                FormulaGroup(id: g.str("id"), label: g.str("label"), blurb: g.str("blurb"), items: g.objects("items").map { f in
                    FormulaItem(id: f.str("id"), name: f.str("name"), tag: f.str("tag"), expr: f.str("expr"), where: f.str("where"), reads: f.str("reads"), trap: f.str("trap"),
                                example: ContentParser.formulaExample(f.obj("example")))
                })
            },
            reference: reference(o.obj("reference") ?? JObj([:])),
            glossary: o.objects("glossary").map { GlossaryTerm(term: $0.str("t"), group: $0.str("g"), definition: $0.str("d")) },
            abbreviations: o.objects("abbreviations").map { Abbreviation(abbr: $0.str("abbr"), full: $0.str("full"), group: $0.str("group"), note: $0.str("note")) },
            modules: modules,
            lessons: lessons,
            rules: rules(o.obj("rules")),
            syncScript: o.str("syncScript"),
            generatorPrompt: o.str("generatorPrompt"),
            cases: cases(o.objects("cases")),
            documents: documents(o.obj("documents")),
            areaTours: o.objects("areaTours").map { t in
                AreaTour(area: t.str("area"), intro: t.str("intro"), lessons: t.strings("lessons"), figure: ContentParser.figure(t.obj("figure")))
            },
            courseOrder: o.strings("courseOrder"),
            theory: theory(o.obj("theory")))
    }

    /** index.html PMP.THEORY, as extract-content.mjs writes it (abbreviations spelled out). */
    static func theory(_ o: JObj?) -> Theory {
        guard let o = o else { return Theory() }
        let parts: [TheoryPart] = o.objects("parts").map { p in
            TheoryPart(n: p.optInt("n"), title: p.str("title"), blurb: p.str("blurb"), planned: p.strings("planned"))
        }
        let chapters: [TheoryChapter] = o.objects("chapters").map { c in
            let sections: [TheorySection] = c.objects("sections").map { s in
                let raw: [Any] = s.arr("blocks") ?? []
                let blocks: [TheoryBlock] = raw.compactMap { b -> TheoryBlock? in
                    if let t = b as? String { return .para(t) }
                    guard let j = JObj(b) else { return nil }
                    if j.has("ul") { return .bullets(j.strings("ul")) }
                    if j.has("ol") { return .numbered(j.strings("ol")) }
                    if j.has("h") { return .heading(j.str("h")) }
                    if j.has("box") { return .box(kind: j.str("box"), text: j.str("text")) }
                    if let f = ContentParser.figure(j.obj("fig")) { return .fig(f) }
                    return nil
                }
                return TheorySection(id: s.str("id"), title: s.str("title"), blocks: blocks)
            }
            return TheoryChapter(id: c.str("id"), part: c.optInt("part"), title: c.str("title"), summary: c.str("summary"), why: c.str("why"),
                                 sections: sections, keyIdeas: c.strings("keyIdeas"), reading: c.strings("reading"), minutes: max(1, c.optInt("minutes", 1)))
        }
        return Theory(parts: parts, chapters: chapters)
    }

    /** A lesson figure (index.html §10.31), or nil when the section has none. */
    static func figure(_ o: JObj?) -> Figure? {
        guard let o = o else { return nil }
        let shapes: [FigShape] = o.objects("shapes").map { s in
            var f = FigShape()
            f.k = s.str("k"); f.x = s.optDouble("x"); f.y = s.optDouble("y"); f.w = s.optDouble("w"); f.h = s.optDouble("h")
            f.t = s.str("t"); f.tone = s.strOrNil("tone") ?? "ink"; f.fill = s.optBool("fill", true)
            f.p = s.doubles("p") ?? []; f.arrow = s.optBool("arrow"); f.dash = s.optBool("dash")
            f.a = s.strOrNil("a") ?? "start"; f.s = s.optDouble("s", 13); f.b = s.optBool("b"); f.r = s.optDouble("r", 5)
            return f
        }
        return Figure(title: o.str("title"), caption: o.str("caption"), alt: o.str("alt"), w: o.optDouble("w"), h: o.optDouble("h"), shapes: shapes)
    }

    static func rows(_ list: Any?) -> [[String]] {
        let outer: [Any] = list as? [Any] ?? []
        return outer.map { r in
            let cells: [Any] = r as? [Any] ?? []
            return cells.map { c in c as? String ?? "" }
        }
    }

    /** index.html PMP.DOCUMENTS: { project: { name, summary }, documents: [...] }. */
    static func documents(_ o: JObj?) -> SampleDocuments {
        guard let o = o else { return SampleDocuments(projectName: "", projectSummary: "", list: []) }
        let p: JObj? = o.obj("project")
        let list: [SampleDocument] = o.objects("documents").map { d in
            let links: JObj = d.obj("links") ?? JObj([:])
            let sections: [DocSection] = d.objects("sections").map { s in
                DocSection(heading: s.str("heading"), rows: rows(s.d["rows"]))
            }
            var table: DocTable? = nil
            if let t = d.obj("table") {
                table = DocTable(cols: t.strings("cols"), rows: rows(t.d["rows"]))
            }
            return SampleDocument(id: d.str("id"), title: d.str("title"), kind: d.str("kind"),
                                  purpose: d.str("purpose"), owner: d.str("owner"), whenText: d.str("when"),
                                  agile: d.str("agile"), examTip: d.str("examTip"),
                                  sections: sections, table: table,
                                  lessons: links.strings("lessons"), processes: links.strings("processes"), terms: links.strings("terms"))
        }
        return SampleDocuments(projectName: p?.str("name") ?? "", projectSummary: p?.str("summary") ?? "", list: list)
    }

    /**
     * PMP.CASES. Each item is validated like a pack item and parsed with pack id "case:<id>"; an invalid
     * item is dropped, and a case left with no items is dropped. A missing or empty array gives no cases.
     */
    static func formulaExample(_ e: JObj?) -> FormulaExample? {
        guard let e = e else { return nil }
        let steps: [String] = e.strings("steps")
        return FormulaExample(situation: e.str("situation"), steps: steps, means: e.str("means"))
    }

    static func cases(_ list: [JObj]) -> [CaseStudy] {
        var out: [CaseStudy] = []
        for c in list {
            let id: String = c.str("id")
            if id.isEmpty { continue }
            var qs: [Question] = []
            for o in c.objects("questions") where validate(o) == nil {
                qs.append(question(o, packId: "case:" + id))
            }
            if qs.isEmpty { continue }
            var passage: [String] = c.strings("passage")
            if passage.isEmpty {
                let joined: String = c.str("passage")
                passage = joined.components(separatedBy: "\n\n").filter { (p: String) -> Bool in !p.trimmingCharacters(in: .whitespaces).isEmpty }
            }
            var exhibits: [CaseExhibit] = []
            for x in c.objects("exhibits") {
                let rawRows: [Any] = x.arr("rows") ?? []
                var rows: [[String]] = []
                for row in rawRows {
                    let cells: [Any] = (row as? [Any]) ?? []
                    rows.append(cells.map { (v: Any) -> String in (v as? String) ?? "" })
                }
                exhibits.append(CaseExhibit(id: x.str("id"), label: x.str("label"), title: x.str("title"),
                                            cols: x.strings("cols"), rows: rows, note: x.str("note")))
            }
            let title: String = c.str("title").isEmpty ? id : c.str("title")
            out.append(CaseStudy(id: id, title: title, approach: Approach.of(c.optString("approach")), setting: c.str("setting"),
                                 passage: passage, exhibits: exhibits, questions: qs))
        }
        return out
    }

    /** PMP.RULES (docs/progress-format.md §1). Anything missing keeps its documented default. */
    static func rules(_ j: JObj?) -> Rules {
        var r = Rules()
        guard let j else { return r }
        if let b = j.obj("mockBands") {
            r.bandAbove = b.optDouble("above", r.bandAbove)
            r.bandTarget = b.optDouble("target", r.bandTarget)
            r.bandBelow = b.optDouble("below", r.bandBelow)
        }
        if let c = j.obj("scenarioCredit") {
            r.creditBest = c.optDouble("best", r.creditBest)
            r.creditAcceptable = c.optDouble("acceptable", r.creditAcceptable)
            r.creditPremature = c.optDouble("premature", r.creditPremature)
            r.creditWrong = c.optDouble("wrong", r.creditWrong)
        }
        if let m = j.obj("mastery") {
            r.masteryMinAnswers = m.optInt("minAnswers", r.masteryMinAnswers)
            r.masteryWindow = m.optInt("window", r.masteryWindow)
            r.masteryAccuracy = m.optDouble("accuracy", r.masteryAccuracy)
            r.masteryFadeDays = m.optInt("fadeDays", r.masteryFadeDays)
        }
        let causes: [CauseRule] = j.objects("causes").compactMap { c -> CauseRule? in
            let id = c.str("id")
            if id.isEmpty { return nil }
            let fallback: CauseRule? = Rules.defaultCauses.first { $0.id == id }
            let short = c.str("short").isEmpty ? (fallback?.short ?? id) : c.str("short")
            let label = c.str("label").isEmpty ? (fallback?.label ?? short) : c.str("label")
            let hint = c.str("hint").isEmpty ? (fallback?.hint ?? "") : c.str("hint")
            return CauseRule(id: id, short: short, label: label, hint: hint)
        }
        if !causes.isEmpty { r.causes = causes }
        r.reentryDays = j.optInt("reentryDays", r.reentryDays)
        let conf: [ConfidenceRule] = j.objects("confidence").compactMap { c -> ConfidenceRule? in
            let v = c.optInt("v")
            if v <= 0 { return nil }
            return ConfidenceRule(v: v, id: c.str("id"), label: c.str("label"))
        }
        if !conf.isEmpty { r.confidence = conf }
        return r
    }

    private static func scenario(_ s: JObj) -> Scenario {
        var ranks: [String: (Rank, String)] = [:]
        for r in s.objects("ranking") { ranks[r.str("option")] = (Rank.of(r.str("rank")), r.str("why")) }
        return Scenario(
            id: s.str("id"), domain: Domain.of(s.optString("domain")), ecoTask: s.str("ecoTask"),
            approach: Approach.of(s.optString("approach")), difficulty: s.optInt("difficulty", 3), stem: s.str("stem"),
            options: s.objects("options").map { op in
                let r = ranks[op.str("id")]
                return RankedOption(id: op.str("id"), text: op.str("text"), rank: r?.0 ?? .wrong, why: r?.1 ?? "")
            },
            mindset: s.str("mindset"), explanation: s.str("explanation"))
    }

    private static func reference(_ r: JObj) -> Reference {
        func groups(_ key: String) -> [RefGroup] {
            r.objects(key).map { RefGroup(id: $0.str("id"), n: $0.optInt("n"), label: $0.str("label"), count: $0.optInt("count"), blurb: $0.str("blurb")) }
        }
        return Reference(
            groups: groups("groups"), areas: groups("areas"),
            processes: r.objects("processes").map {
                RefProcess(id: $0.str("id"), n: $0.str("n"), name: $0.str("name"), area: $0.str("area"), group: $0.str("group"),
                           purpose: $0.str("purpose"), inputs: $0.strings("inputs"), tools: $0.strings("tools"), outputs: $0.strings("outputs"), key: $0.str("key"))
            },
            principles: r.objects("principles").map {
                RefPrinciple(n: $0.optInt("n"), label: $0.str("label"), line: $0.str("line"), body: $0.str("body"), practice: $0.strings("practice"), trap: $0.str("trap"))
            },
            domains: r.objects("domains").map {
                RefPerfDomain(n: $0.optInt("n"), label: $0.str("label"), line: $0.str("line"), body: $0.str("body"), outcomes: $0.strings("outcomes"), checks: $0.strings("checks"))
            },
            agile: r.objects("agile").map { g in
                RefAgileGroup(label: g.str("label"), blurb: g.str("blurb"), items: g.objects("items").map {
                    RefAgileItem(name: $0.str("name"), body: $0.str("body"), points: $0.strings("points"))
                })
            },
            tables: r.objects("tables").map { t in
                RefTable(label: t.str("label"), blurb: t.str("blurb"), cols: t.strings("cols"),
                         rows: (t.arr("rows") ?? []).map { row in (row as? [Any] ?? []).map { ($0 as? String) ?? "" } },
                         note: t.str("note"))
            })
    }
}
