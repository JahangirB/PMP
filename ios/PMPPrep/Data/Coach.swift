import Foundation

/** Where a coach line or step sends you. */
enum CoachAction: String {
    case PLANNER, DIAGNOSTIC, PRACTICE, TASK, MOCK, LEARN, CARDS, SCENARIOS, JOURNAL, FORMULAS, ANALYTICS
    var label: String {
        switch self {
        case .PLANNER: "Open the planner"; case .DIAGNOSTIC: "Take the diagnostic"; case .PRACTICE: "Practise"
        case .TASK: "Practise this task"; case .MOCK: "Open the mock exam"; case .LEARN: "Open the lesson"
        case .CARDS: "Review cards"; case .SCENARIOS: "Open scenarios"; case .JOURNAL: "Open the journal"
        case .FORMULAS: "Formula sheet"; case .ANALYTICS: "See analytics"
        }
    }
}

struct CoachMessage: Identifiable { let id: String; let text: String; let priority: Int; let cooldownDays: Int; var action: CoachAction? = nil; var arg: String? = nil }
struct CoachStep: Identifiable { var id: String { title }; let title: String; let why: String; let action: CoachAction; var arg: String? = nil; var done: Bool = false }

struct CoachReport {
    let stage: String
    let headline: String
    let verdict: String
    let strengths: [String]
    let improve: [String]
    let messages: [CoachMessage]
    let steps: [CoachStep]
}

/**
 * The coach. Everything it says offline is derived from the person's own history with
 * the rules the web app's coach uses, in the same voice: plain, second person, no guilt
 * and no pass prediction. snapshot() is the same picture written out for Gemini.
 */
struct Coach {
    let x: Extras
    let pool: [Question]
    let store: ProgressStore
    let readiness: Readiness
    private let day = Time.dayMs

    private func pct(_ v: Double) -> String { "\(Int((v * 100).rounded()))%" }
    private func plural(_ n: Int, _ w: String) -> String { n == 1 ? "1 \(w)" : "\(n) \(w)s" }

    func daysToExam(_ now: Millis) -> Int? {
        store.plan.examDate.map { Int((Double(Time.startOfDay($0) - Time.startOfDay(now)) / Double(day)).rounded()) }
    }

    private func lastActivity() -> Millis {
        [store.attempts.map(\.at).max(), store.mocks.map(\.at).max(), store.scenarioRuns.map(\.at).max(), store.cards.values.map(\.last).max()]
            .compactMap { $0 }.max() ?? 0
    }

    func stage(_ now: Millis) -> String {
        if let d = daysToExam(now), (0...7).contains(d) { return "examweek" }
        let last = lastActivity()
        if last > 0 && now - last >= Millis(Rules.current.reentryDays) * day { return "reentry" }
        return "normal"
    }

    private func leadCause() -> (RootCause, Int)? {
        let wrongNow = Set(store.last.values.filter { !$0.correct }.map(\.qid))
        let tags = Dictionary(grouping: store.mistakeTags.filter { wrongNow.contains($0.key) }.values, by: \.cause).mapValues(\.count)
        if tags.values.reduce(0, +) < 3 { return nil }
        return tags.max { $0.value < $1.value }.map { ($0.key, $0.value) }
    }

    func nextLesson() -> Lesson? {
        let order = Dictionary(x.modules.map { ($0.id, $0.order) }, uniquingKeysWith: { a, _ in a })
        return x.lessons.sorted { ((order[$0.module] ?? 99), $0.order) < ((order[$1.module] ?? 99), $1.order) }
            .first { !store.lessonsDone.contains($0.id) }
    }

    private func cardsDue(_ now: Millis) -> Int { x.flashcards.filter { c in store.cards[c.id].map { $0.isDue(now) } ?? false }.count }
    private func leeches() -> Int { store.cards.values.filter { $0.lapses >= x.srs.leechThreshold }.count }

    private func fitness(_ now: Millis) -> FitnessReport {
        ExamFitness.report(FitnessInput(attempts: store.attempts, sittings: store.sittings, checkIns: store.checkIns,
                                        results: store.sessionResults, studyDays: store.plan.studyDays, hoursPerWeek: store.plan.hoursPerWeek, now: now))
    }

    func report(_ now: Millis = Time.now()) -> CoachReport {
        let r = readiness.report(store, now)
        let st = stage(now)
        let days = daysToExam(now)
        let atts = store.attempts.filter { $0.source != Readiness.SOURCE_LESSON }
        let byTask = readiness.group(atts) { $0.ecoTask }
        let byDomain = readiness.group(atts) { $0.domain.rawValue }
        let (median, paceN) = readiness.medianPace(atts)
        let paceReady = paceN >= x.readiness.paceMinSample
        let untouched = x.tasks.filter { (byTask[$0.id]?.n ?? 0) == 0 }.count
        let wrongNow = store.last.values.filter { !$0.correct }
        let untagged = wrongNow.filter { store.mistakeTags[$0.qid] == nil }.count
        let lead = leadCause()
        let due = cardsDue(now)
        let diag = store.diagnostic
        let lastMockDays = store.mocks.map(\.at).max().map { Int((now - $0) / day) }
        let next = nextLesson()

        /* ── how you're doing ── */
        let headline: String
        if atts.isEmpty && diag == nil { headline = "Let's find your starting point" }
        else if r.score >= 75 { headline = "On course for the exam" }
        else if r.score >= 60 { headline = "Building well" }
        else if r.score >= 40 { headline = "Foundations forming" }
        else { headline = "Early days" }
        let domainLine = Domain.allCases.compactMap { d in byDomain[d.rawValue].flatMap { $0.n >= 5 ? "\(d.short) \(pct($0.accuracy))" : nil } }
        var verdict = "Readiness \(r.score) out of 100"
        verdict += r.provisional ? " (provisional: \(r.gates.joined(separator: "; "))). " : ". "
        if !domainLine.isEmpty { verdict += "Accuracy by domain: \(domainLine.joined(separator: ", ")). " }
        if let diag { verdict += "Your diagnostic scored \(pct(diag.pct)). " }
        if let m = store.mocks.max(by: { $0.at < $1.at }) { verdict += "Last mock \(pct(m.pct)). " }
        if let days, days >= 0 { verdict += "Exam in \(plural(days, "day"))." }
        verdict = verdict.trimmingCharacters(in: .whitespaces)

        var strengths: [String] = []
        for d in Domain.allCases { if let g = byDomain[d.rawValue], g.n >= 10, g.accuracy >= 0.75 { strengths.append("\(d.title): \(pct(g.accuracy)) over \(g.n) answers") } }
        for (t, g) in x.tasks.compactMap({ t in byTask[t.id].flatMap { $0.n >= 5 && $0.accuracy >= 0.8 ? (t, $0) : nil } }).sorted(by: { $0.1.accuracy > $1.1.accuracy }).prefix(3) {
            strengths.append("\(t.code) \(t.label): \(pct(g.accuracy))")
        }
        if paceReady, let median, median >= x.readiness.paceBand[0], median <= x.readiness.paceBand[1] { strengths.append("Pace: \(Int(median.rounded()))s a question, inside the healthy band") }

        let weakTasks = x.tasks.compactMap { t in byTask[t.id].flatMap { $0.n >= 3 && $0.accuracy < 0.65 ? (t, $0) : nil } }.sorted { $0.1.accuracy < $1.1.accuracy }
        var improve: [String] = []
        for (t, g) in weakTasks.prefix(3) { improve.append("\(t.code) \(t.label): \(pct(g.accuracy)) over \(g.n) answers") }
        if untouched > 0 && !atts.isEmpty { improve.append("\(plural(untouched, "ECO task")) not practised yet") }
        if let lead { improve.append("Most tagged misses are “\(lead.0.title)” (\(lead.1))") }
        if paceReady, let median, median > x.readiness.paceBand[1] { improve.append("Pace: median \(Int(median.rounded()))s against about \(x.exam.paceTargetSec)s on the day") }
        if paceReady, let median, median < x.readiness.paceBand[0] { improve.append("Pace: median \(Int(median.rounded()))s is fast enough to be costing marks") }
        if store.mocks.isEmpty && atts.count >= 100 { improve.append("No full mock exam yet") }
        if untagged >= 5 { improve.append("\(untagged) misses without a root cause") }

        /* ── messages ── */
        var msgs: [CoachMessage] = []
        if st == "reentry" { msgs.append(CoachMessage(id: "welcome_back", text: "Welcome back. Short session today: cards and ten mixed questions. Then see if the plan needs adjusting.", priority: 100, cooldownDays: 1, action: .PRACTICE)) }
        if st == "examweek", let days { msgs.append(CoachMessage(id: "taper", text: "Exam in \(plural(days, "day")). Nothing new now. Re-read your journal, skim the formula sheet, sleep.", priority: 100, cooldownDays: 1, action: .FORMULAS)) }
        if store.plan.examDate == nil { msgs.append(CoachMessage(id: "plan_offer", text: "There's no exam date yet. Setting one takes a minute and fits the work to the time you have.", priority: 75, cooldownDays: 2, action: .PLANNER)) }
        if diag == nil { msgs.append(CoachMessage(id: "diagnostic_first", text: "The plan is guessing where you're weak. A 25-question diagnostic, taken cold, lets it stop guessing.", priority: 70, cooldownDays: 3, action: .DIAGNOSTIC)) }
        if store.mocks.isEmpty && ((days != nil && days! <= 42) || atts.count >= 300) {
            msgs.append(CoachMessage(id: "first_mock_due", text: "Time for a first full mock. It's the only evidence you can hold accuracy for \(x.exam.minutes / 60) hours. Block out a weekend morning.", priority: 65, cooldownDays: 5, action: .MOCK))
        }
        switch lead?.0 {
        case .MISREAD_STEM?: msgs.append(CoachMessage(id: "cause_misread", text: "Most of your misses are reading, not knowledge. Find the qualifier (first, next, best, except) before looking at the options.", priority: 60, cooldownDays: 7))
        case .WRONG_MINDSET?: msgs.append(CoachMessage(id: "cause_mindset", text: "Most misses are the right move at the wrong moment. Two scenario sets this week will help more than more questions.", priority: 60, cooldownDays: 7, action: .SCENARIOS))
        case .KNOWLEDGE_GAP?: msgs.append(CoachMessage(id: "cause_knowledge", text: "Most misses are material you hadn't met. Lessons first, then the questions on them.", priority: 60, cooldownDays: 7, action: .LEARN, arg: next?.id))
        case .TWO_PLAUSIBLE?: msgs.append(CoachMessage(id: "cause_tossup", text: "Most of your misses are two-answer splits. The discriminator is usually in the stem's constraints rather than in the options, and the scenario trainer's ranked reveal is built to make that visible.", priority: 60, cooldownDays: 7, action: .SCENARIOS))
        case .CALCULATION?: msgs.append(CoachMessage(id: "cause_calc", text: "Most misses are arithmetic. Work the Formula Lab drills until the formulas are automatic.", priority: 60, cooldownDays: 7, action: .FORMULAS))
        case .RUSHED?: msgs.append(CoachMessage(id: "cause_rushed", text: "Most misses come from rushing. Read all four options before choosing, even when the first looks right.", priority: 60, cooldownDays: 7))
        case nil: break
        }
        if paceReady, let median, median < x.readiness.paceBand[0] { msgs.append(CoachMessage(id: "pace_fast", text: "Your median is \(Int(median.rounded()))s. Fast answers are missing marks, and you have the time to read the stem twice.", priority: 55, cooldownDays: 7)) }
        if paceReady, let median, median > x.readiness.paceBand[1] { msgs.append(CoachMessage(id: "pace_slow", text: "Your median is \(Int(median.rounded()))s against \(x.exam.paceTargetSec)s on the day. A timed drill twice a week closes it slowly, and that's normal.", priority: 55, cooldownDays: 7, action: .PRACTICE)) }
        let fit = fitness(now)
        if fit.momentumLow { msgs.append(CoachMessage(id: "momentum", text: "Your study time has dipped compared with recent weeks. That's normal over a long preparation. Try a lighter week of minimum days: 10 cards or 10 questions keep the habit going without pressure.", priority: 60, cooldownDays: 3)) }
        if let f = fit.finding, let n = fit.next { msgs.append(CoachMessage(id: "fitness", text: "Exam fitness: \(f) \(n)", priority: 45, cooldownDays: 5)) }
        if due >= 20 { msgs.append(CoachMessage(id: "cards_due", text: "\(due) cards are waiting. Ten minutes now saves twenty later in the week.", priority: 50, cooldownDays: 2, action: .CARDS)) }
        if atts.count >= 50 && untouched > 0 { msgs.append(CoachMessage(id: "coverage_gap", text: (untouched == 1 ? "One task hasn't" : "\(untouched) tasks haven't") + " been touched yet. Coverage is worth more readiness per minute than anything else right now.", priority: 50, cooldownDays: 5, action: .PRACTICE)) }
        if untagged >= 5 { msgs.append(CoachMessage(id: "journal_waiting", text: "\(untagged) misses haven't got a cause yet. Tagging them is the cheapest insight on the board.", priority: 45, cooldownDays: 3, action: .JOURNAL)) }
        if let lastMockDays, Double(lastMockDays) > x.readiness.mockStaleAfterDays { msgs.append(CoachMessage(id: "mock_stale", text: "Your last mock was \(lastMockDays) days ago, so it's counting for less. Another one will show where you are now.", priority: 45, cooldownDays: 10, action: .MOCK)) }
        let lc = leeches()
        if lc >= 1 { msgs.append(CoachMessage(id: "leech", text: (lc == 1 ? "One card keeps" : "\(lc) cards keep") + " slipping. Rewrite it in your own words or study the lesson behind it.", priority: 40, cooldownDays: 7, action: .CARDS)) }
        if r.provisional && !atts.isEmpty { msgs.append(CoachMessage(id: "provisional", text: "Your readiness score is provisional for now: \(r.gates.joined(separator: "; ")). It firms up on its own as you work.", priority: 30, cooldownDays: 7)) }
        let visible = msgs.filter { (store.coachSnoozed[$0.id] ?? 0) <= now }.sorted { $0.priority > $1.priority }

        /* ── today's session ── */
        let today = Time.startOfDay(now)
        let todayAtts = atts.filter { $0.at >= today }
        var steps: [CoachStep] = []
        if diag == nil { steps.append(CoachStep(title: "25-question diagnostic", why: "Taken cold, it shows where you stand in each domain so the plan can aim at the weakest.", action: .DIAGNOSTIC)) }
        if store.plan.examDate == nil { steps.append(CoachStep(title: "Set your exam date and weekly hours", why: "The plan is built around the time you really have.", action: .PLANNER)) }
        let reviewedToday = store.cards.values.filter { $0.last >= today }.count
        if due > 0 || reviewedToday > 0 { steps.append(CoachStep(title: due > 0 ? "Flashcards: \(due) due" : "Flashcards", why: "Reviews first. They hold up the accuracy your score is built on, and they make a good warm-up.", action: .CARDS, done: due == 0)) }
        if st == "examweek" {
            steps.append(CoachStep(title: "Skim the formula sheet", why: "Light work only: nothing new goes in now.", action: .FORMULAS))
            steps.append(CoachStep(title: "10 mixed questions", why: "Light mixed practice keeps you sharp without opening anything new.", action: .PRACTICE, done: todayAtts.count >= 10))
        } else if st == "reentry" {
            steps.append(CoachStep(title: "10 mixed questions", why: "Ten mixed questions show where things stand without asking much of you.", action: .PRACTICE, done: todayAtts.count >= 10))
        } else {
            if let next { steps.append(CoachStep(title: "Lesson \(next.id): \(next.title)", why: "Teaching comes before the questions on it, so lessons take the front of the day.", action: .LEARN, arg: next.id)) }
            // Coverage first, then the weakest task with evidence, then the diagnostic's weakest domain.
            var target = x.tasks.first { t in (byTask[t.id]?.n ?? 0) < x.readiness.coverageTargetPerTask && pool.contains { $0.ecoTask == t.id } } ?? weakTasks.first?.0
            if target == nil, let dg = diag, let d = Domain.allCases.min(by: { (dg.domainPct($0) ?? 1) < (dg.domainPct($1) ?? 1) }) {
                target = x.tasks.filter { $0.domain == d }.min { (byTask[$0.id]?.accuracy ?? 0) < (byTask[$1.id]?.accuracy ?? 0) }
            }
            if let target {
                let n = todayAtts.filter { $0.ecoTask == target.id }.count
                steps.append(CoachStep(title: "10 questions on \(target.label)",
                    why: (byTask[target.id]?.n ?? 0) < x.readiness.coverageTargetPerTask ? "Every task gets a block until it has enough answers to count." : "This is where your answers are weakest, so it gets the time.",
                    action: .TASK, arg: target.id, done: n >= 10))
            }
            if untagged >= 5 { steps.append(CoachStep(title: "Tag 5 misses in the journal", why: "Knowing why you missed is what turns practice into a plan.", action: .JOURNAL)) }
            if lead?.0 == .WRONG_MINDSET { steps.append(CoachStep(title: "5 situational scenarios", why: "Ordering the right moves is what situational items test, and it's the last skill to come good.", action: .SCENARIOS)) }
            if let days, (8...42).contains(days), lastMockDays == nil || lastMockDays! >= 7 { steps.append(CoachStep(title: "Full mock exam this week", why: "A full sitting is the only evidence you can hold accuracy for the whole exam.", action: .MOCK)) }
        }
        return CoachReport(stage: st, headline: headline, verdict: verdict, strengths: strengths, improve: improve, messages: visible, steps: steps)
    }

    /** The person's progress as plain text, sent with every message to Gemini so its answers rest on real figures. */
    func snapshot(_ now: Millis = Time.now()) -> String {
        let r = readiness.report(store, now)
        let atts = store.attempts.filter { $0.source != Readiness.SOURCE_LESSON }
        let byTask = readiness.group(atts) { $0.ecoTask }
        let byDomain = readiness.group(atts) { $0.domain.rawValue }
        let byApproach = readiness.group(atts) { $0.approach?.rawValue }
        let byType = readiness.group(atts) { $0.type.base.rawValue }
        let (median, paceN) = readiness.medianPace(atts)
        let streak = readiness.streak(store, now)
        let c = report(now)
        var lines: [String] = []
        let df = DateFormatter(); df.dateFormat = "yyyy-MM-dd"; df.locale = Locale(identifier: "en_US_POSIX")
        lines.append("Date: \(df.string(from: Time.date(now)))")
        lines.append("Exam: \(x.exam.totalItems) questions in \(x.exam.minutes) minutes. Domain weights: " + Domain.allCases.map { "\($0.title) \(pct(x.domainWeights[$0] ?? 0))" }.joined(separator: ", "))
        lines.append("Exam date: " + (store.plan.examDate != nil ? "in \(daysToExam(now) ?? 0) days" : "not set") + "; planned study \(store.plan.hoursPerWeek) h/week")
        lines.append("Readiness: \(r.score)/100" + (r.provisional ? " (provisional: \(r.gates.joined(separator: "; ")))" : ""))
        let fit = fitness(now)
        lines.append("Exam fitness (stamina, composure under the clock, consistency; behaviour-based, not a diagnosis): \(fit.score.map(String.init) ?? "not enough evidence yet")" +
            " — " + fit.parts.map { p in "\(p.label) \(p.score.map { String(Int($0 * 100)) } ?? "n/a")" }.joined(separator: ", ") +
            (fit.wpm.map { "; reading about \(Int($0)) words a minute (exam needs ~\(Int(ExamFitness.TARGET_WPM)))" } ?? "") +
            "; longest sitting \(fit.longest) questions" + (fit.worry.map { "; self-reported exam worry \($0)/100" } ?? "") +
            (fit.finding.map { ". Main finding: \($0)" } ?? ""))
        for comp in r.components { lines.append("  - \(comp.label) (\(comp.weight) pts): \(comp.figure), earning \(String(format: "%.1f", comp.points))") }
        if let d = store.diagnostic {
            lines.append("Diagnostic: \(d.right)/\(d.n) (\(pct(d.pct))); " + Domain.allCases.map { "\($0.short) " + (d.domainPct($0).map(pct) ?? "-") }.joined(separator: ", "))
        } else { lines.append("Diagnostic: not taken") }
        lines.append("Answers recorded: \(atts.count); distinct questions: \(store.last.count) of \(pool.count) in the bank; wrong last time: \(store.last.values.filter { !$0.correct }.count)")
        lines.append("Accuracy by domain: " + Domain.allCases.map { d in byDomain[d.rawValue].map { "\(d.short) \(pct($0.accuracy)) (n=\($0.n))" } ?? "\(d.short) none" }.joined(separator: ", "))
        lines.append("Accuracy by approach: " + Approach.allCases.map { a in byApproach[a.rawValue].map { "\(a.title) \(pct($0.accuracy)) (n=\($0.n))" } ?? "\(a.title) none" }.joined(separator: ", "))
        lines.append("Accuracy by question type: " + [QType.single, .multi, .matching, .hotspot, .fillblank].map { t in byType[t.rawValue].map { "\(t.title) \(pct($0.accuracy)) (n=\($0.n))" } ?? "\(t.title) none" }.joined(separator: ", "))
        lines.append("ECO tasks (code, label, accuracy, answers):")
        for t in x.tasks { lines.append("  \(t.code) \(t.label): " + (byTask[t.id].map { "\(pct($0.accuracy)), \($0.n)" } ?? "not practised")) }
        if store.mocks.isEmpty { lines.append("Mock exams: none") }
        else {
            lines.append("Mock exams (newest first):")
            for m in store.mocks.sorted(by: { $0.at > $1.at }).prefix(5) {
                lines.append("  \((now - m.at) / Time.dayMs) days ago: \(m.right)/\(m.n) (\(pct(m.pct))); " + Domain.allCases.map { d in m.domains[d].map { "\(d.short) \($0[0])/\($0[1])" } ?? "" }.joined(separator: ", "))
            }
        }
        let causes = Dictionary(grouping: store.mistakeTags.values, by: { $0.cause.title }).mapValues(\.count)
        lines.append("Tagged mistake causes: " + (causes.isEmpty ? "none" : causes.map { "\($0.key) \($0.value)" }.joined(separator: ", ")))
        lines.append("Pace: " + (median.map { "median \(Int($0.rounded()))s over \(paceN) timed answers" } ?? "no timed answers") + "; target about \(x.exam.paceTargetSec)s")
        lines.append("Flashcards: \(cardsDue(now)) due, \(x.flashcards.filter { store.cards[$0.id] == nil }.count) never studied, \(leeches()) leeches, of \(x.flashcards.count)")
        lines.append("Lessons done: \(store.lessonsDone.count) of \(x.lessons.count)" + (nextLesson().map { "; next: \($0.id) \($0.title)" } ?? ""))
        let sc = store.scenarioRuns
        lines.append("Situational scenarios: \(sc.count) answered" + (sc.isEmpty ? "" : ", average credit \(pct(sc.reduce(0) { $0 + $1.rank.credit } / Double(sc.count)))"))
        lines.append("Study days in the last \(streak.last14.count): \(streak.activeInWindow); current streak \(streak.current)")
        lines.append("App coach's suggestions today: " + c.steps.map { $0.title + ($0.done ? " (done)" : "") }.joined(separator: ", "))
        return lines.joined(separator: "\n") + "\n"
    }

    /** For explain-it-back: feedback on the learner's own explanation. */
    static let EXPLAIN_SYSTEM = "You are a PMP exam tutor (PMI's Project Management Professional certification). The learner has explained something " +
        "in their own words to check their understanding. Reply in plain text, under 150 words, no headings: first say specifically what they got right, " +
        "then what they missed or got wrong, then the key idea in one sentence. Be warm and direct. If their explanation is correct and complete, say so plainly. " +
        "Base your judgement on PMI's current exam content outline and the PMBOK Guide; if unsure, say so rather than guess."

    static let SYSTEM = "You are the study coach inside a PMP exam preparation app (PMI's Project Management Professional certification). " +
        "You can see the learner's real progress in the snapshot below; base anything you say about their progress on those figures, " +
        "quote the numbers, and never invent results. When asked how they are doing, give an honest overall picture, what is going well, " +
        "and the two or three changes that would help most, tied to specific ECO tasks, question types or habits. Do not predict whether they will pass. " +
        "When they ask a PMP question, answer accurately and concisely, explain the reasoning the exam rewards rather than just the answer, " +
        "and reason the way PMI expects: collaborative, proactive, following the agreed process, servant leadership, and fitted to whether the work " +
        "is predictive, agile or hybrid. Point them to parts of the app when useful: Practice, Mock Exam, Learn, Flashcards, Situational Trainer, " +
        "Formula Lab, Reference, Mistake Journal, Study Planner. Use short paragraphs and short bullet lists, plain English, no tables. " +
        "If you are unsure of something, say so."
}
