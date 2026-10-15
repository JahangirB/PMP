import Foundation

/** Where you stood at the end of a week (Monday to Sunday). The current week's is updated as you go. */
struct WeeklyReport: Equatable {
    var weekStart: Millis
    var updatedAt: Millis
    var readiness: Int
    var accuracy: Int
    var tasksTouched: Int
    var mastered: Int
    var minutes: Int
    var days: Int
    var answers: Int
    var mocks: Int
    var mockAvg: Int?
    var fitness: Int?
    var stamina: Int?
    var composure: Int?
    var consistency: Int?
    var wpm: Int?
    var longest: Int
    var freezePct: Int?
    var worry: Int?
    var sureWrong: Int? = nil
    var luckyGuesses: Int? = nil
    var causes: [RootCause: Int] = [:]

    static func == (a: WeeklyReport, b: WeeklyReport) -> Bool {
        var x = a, y = b; x.updatedAt = 0; y.updatedAt = 0
        return x.weekStart == y.weekStart && x.readiness == y.readiness && x.accuracy == y.accuracy && x.tasksTouched == y.tasksTouched &&
            x.mastered == y.mastered && x.minutes == y.minutes && x.days == y.days && x.answers == y.answers && x.mocks == y.mocks &&
            x.mockAvg == y.mockAvg && x.fitness == y.fitness && x.stamina == y.stamina && x.composure == y.composure &&
            x.consistency == y.consistency && x.wpm == y.wpm && x.longest == y.longest && x.freezePct == y.freezePct &&
            x.worry == y.worry && x.sureWrong == y.sureWrong && x.luckyGuesses == y.luckyGuesses && x.causes == y.causes
    }

    func toJson() -> [String: Any] {
        var o: [String: Any] = ["w": weekStart, "u": updatedAt, "r": readiness, "acc": accuracy, "tt": tasksTouched, "ma": mastered,
                                "min": minutes, "d": days, "n": answers, "mk": mocks, "lg": longest]
        if let v = mockAvg { o["mka"] = v }; if let v = fitness { o["f"] = v }; if let v = stamina { o["st"] = v }
        if let v = composure { o["co"] = v }; if let v = consistency { o["cn"] = v }; if let v = wpm { o["wpm"] = v }
        if let v = freezePct { o["fr"] = v }; if let v = worry { o["wo"] = v }
        if let v = sureWrong { o["sw"] = v }; if let v = luckyGuesses { o["lk"] = v }
        if !causes.isEmpty { o["cz"] = Dictionary(uniqueKeysWithValues: causes.map { ($0.key.rawValue, $0.value) }) }
        return o
    }

    static func fromJson(_ o: JObj) -> WeeklyReport {
        func i(_ k: String) -> Int? { o.has(k) ? o.optInt(k) : nil }
        var causes: [RootCause: Int] = [:]
        if let c = o.obj("cz") { for rc in RootCause.allCases where c.has(rc.rawValue) { causes[rc] = c.optInt(rc.rawValue) } }
        return WeeklyReport(weekStart: o.optLong("w"), updatedAt: o.optLong("u"), readiness: o.optInt("r"), accuracy: o.optInt("acc"),
                            tasksTouched: o.optInt("tt"), mastered: o.optInt("ma"), minutes: o.optInt("min"), days: o.optInt("d"),
                            answers: o.optInt("n"), mocks: o.optInt("mk"), mockAvg: i("mka"), fitness: i("f"), stamina: i("st"),
                            composure: i("co"), consistency: i("cn"), wpm: i("wpm"), longest: o.optInt("lg"), freezePct: i("fr"),
                            worry: i("wo"), sureWrong: i("sw"), luckyGuesses: i("lk"), causes: causes)
    }
}

enum WeeklyReports {
    /** Monday 00:00 of the week containing t, local time. */
    static func weekStart(_ t: Millis) -> Millis {
        var cal = Calendar.current
        cal.firstWeekday = 2
        var d = Time.startOfDay(t)
        while cal.component(.weekday, from: Time.date(d)) != 2 { d = Time.prevDay(d) }
        return d
    }

    /** This week so far, from the same figures the rest of the app shows. */
    static func build(now: Millis, readiness: ReadinessReport, tasksTouched: Int, mastered: Int, attempts: [Attempt],
                      results: [SessionResult], mocks: [MockResult], fitness: FitnessReport, tags: [MistakeTag] = []) -> WeeklyReport {
        let from = weekStart(now)
        let week = attempts.filter { $0.at >= from && $0.source != Readiness.SOURCE_LESSON }
        let sessionMin = results.filter { $0.at >= from }.reduce(0) { $0 + $1.minutes }
        let answerMin = Int((Double(week.reduce(0) { $0 + $1.ms }) / 60_000).rounded())
        let days = Set(week.map { Time.startOfDay($0.at) } + results.filter { $0.at >= from }.map { Time.startOfDay($0.at) }).count
        let wm = mocks.filter { $0.at >= from }
        let acc = readiness.components.first { $0.id == "accuracy" }
        func part(_ id: String) -> Int? { fitness.part(id).score.map { Int(($0 * 100).rounded()) } }
        let asked = week.contains { $0.confidence > 0 }
        return WeeklyReport(
            weekStart: from, updatedAt: now, readiness: readiness.score,
            accuracy: Int(acc?.figure.replacingOccurrences(of: "%", with: "") ?? "") ?? 0,
            tasksTouched: tasksTouched, mastered: mastered, minutes: max(sessionMin, answerMin), days: days, answers: week.count,
            mocks: wm.count, mockAvg: wm.isEmpty ? nil : Int((wm.map(\.pct).reduce(0, +) / Double(wm.count) * 100).rounded()),
            fitness: fitness.score, stamina: part("stamina"), composure: part("composure"), consistency: part("consistency"),
            wpm: fitness.wpm.map { Int($0.rounded()) }, longest: fitness.longest, freezePct: fitness.freezeRate.map { Int(($0 * 100).rounded()) },
            worry: fitness.worry,
            sureWrong: asked ? week.filter(\.sureWrong).count : nil,
            luckyGuesses: asked ? week.filter(\.luckyGuess).count : nil,
            causes: Dictionary(grouping: tags.filter { $0.at >= from }, by: \.cause).mapValues(\.count))
    }

    /** One line of a comparison: this week's value, the week before's, and which way is better. */
    struct Row: Identifiable {
        var id: String { label }
        let label: String; let value: Int?; let previous: Int?; let unit: String; var higherIsBetter: Bool = true
        var delta: Int? { value != nil && previous != nil ? value! - previous! : nil }
        /** +1 better, −1 worse, 0 same or not comparable. */
        var direction: Int { delta.map { d in d == 0 ? 0 : ((d > 0) == higherIsBetter ? 1 : -1) } ?? 0 }
    }

    struct Section: Identifiable { var id: String { title }; let title: String; let rows: [Row] }

    static func compare(_ r: WeeklyReport, _ p: WeeklyReport?) -> [Section] {
        [
            Section(title: "Knowledge", rows: [
                Row(label: "Readiness", value: r.readiness, previous: p?.readiness, unit: "/100"),
                Row(label: "Accuracy", value: r.accuracy, previous: p?.accuracy, unit: "%"),
                Row(label: "Tasks practised", value: r.tasksTouched, previous: p?.tasksTouched, unit: "/26"),
                Row(label: "Tasks mastered", value: r.mastered, previous: p?.mastered, unit: ""),
                Row(label: "Mock average", value: r.mockAvg, previous: p?.mockAvg, unit: "%"),
            ]),
            Section(title: "Exam fitness", rows: [
                Row(label: "Exam fitness", value: r.fitness, previous: p?.fitness, unit: "/100"),
                Row(label: "Stamina", value: r.stamina, previous: p?.stamina, unit: "/100"),
                Row(label: "Composure", value: r.composure, previous: p?.composure, unit: "/100"),
                Row(label: "Consistency", value: r.consistency, previous: p?.consistency, unit: "/100"),
                Row(label: "Reading pace", value: r.wpm, previous: p?.wpm, unit: " wpm"),
                Row(label: "Longest sitting", value: r.longest, previous: p?.longest, unit: " q"),
                Row(label: "Freezes", value: r.freezePct, previous: p?.freezePct, unit: "%", higherIsBetter: false),
                Row(label: "Exam worry", value: r.worry, previous: p?.worry, unit: "/100", higherIsBetter: false),
            ]),
            Section(title: "Effort", rows: [
                Row(label: "Minutes studied", value: r.minutes, previous: p?.minutes, unit: " min"),
                Row(label: "Study days", value: r.days, previous: p?.days, unit: "/7"),
                Row(label: "Questions answered", value: r.answers, previous: p?.answers, unit: ""),
                Row(label: "Mocks sat", value: r.mocks, previous: p?.mocks, unit: ""),
            ]),
            Section(title: "Confidence", rows: [
                Row(label: "Sure but wrong", value: r.sureWrong, previous: p?.sureWrong, unit: "", higherIsBetter: false),
                Row(label: "Right by guessing", value: r.luckyGuesses, previous: p?.luckyGuesses, unit: "", higherIsBetter: false),
            ]),
            // Only causes tagged in one of the two weeks; a cause not tagged in a week counts as none.
            Section(title: "Mistake causes", rows: RootCause.allCases.filter { (r.causes[$0] ?? 0) + (p?.causes[$0] ?? 0) > 0 }.map { c in
                Row(label: c.title, value: r.causes[c] ?? 0, previous: p.map { $0.causes[c] ?? 0 }, unit: "", higherIsBetter: false)
            }),
        ].filter { $0.rows.contains { $0.value != nil } }
    }
}
