import Foundation

enum Grade: CaseIterable {
    case again, hard, good, easy
    var title: String { switch self { case .again: "Again"; case .hard: "Hard"; case .good: "Good"; case .easy: "Easy" } }
}

/** SM-2 state for one flashcard. A card never studied has last == 0. */
struct CardState: Equatable {
    var ease: Double
    var interval: Int = 0
    var reps: Int = 0
    var lapses: Int = 0
    var due: Millis = 0
    var last: Millis = 0
    /** When the card was first studied; drives the new-cards-per-day cap. */
    var first: Millis = 0
    var isNew: Bool { last == 0 }
}

/** SM-2 exactly as the web app runs it (§10.05.1), with every number taken from its §01.4 settings. */
struct Srs {
    let c: SrsConfig

    func fresh() -> CardState { CardState(ease: c.easeStart) }
    func isLeech(_ s: CardState) -> Bool { s.lapses >= c.leechThreshold }

    func review(_ s: CardState, _ g: Grade, _ now: Millis) -> CardState {
        let failed = g == .again
        var e = s.ease
        switch g { case .again: e -= c.easeAgain; case .hard: e -= c.easeHard; case .easy: e += c.easeEasy; case .good: break }
        let ease = max(c.easeMin, (e * 100).rounded() / 100)
        let interval: Int
        if failed { interval = c.lapseInterval }
        else if s.reps == 0 { interval = g == .easy ? c.intervalEasy : c.interval1 }
        else if s.reps == 1 { interval = g == .easy ? Int((Double(c.interval2) * c.easyBonus).rounded()) : c.interval2 }
        else {
            let base = Double(max(1, s.interval))
            let days: Double
            switch g { case .hard: days = base * c.hardFactor; case .easy: days = base * ease * c.easyBonus; default: days = base * ease }
            interval = min(c.maxInterval, max(s.interval + 1, Int(days.rounded())))
        }
        return CardState(
            ease: ease, interval: interval,
            reps: failed ? 0 : s.reps + 1,
            lapses: failed ? s.lapses + 1 : s.lapses,
            due: Time.startOfDay(now) + Millis(interval) * Time.dayMs,
            last: now,
            first: s.first == 0 ? now : s.first)
    }

    /** The interval each grade would give, for the labels on the buttons. */
    func preview(_ s: CardState, _ now: Millis) -> [Grade: Int] {
        Dictionary(uniqueKeysWithValues: Grade.allCases.map { ($0, review(s, $0, now).interval) })
    }
}
