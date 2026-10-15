import Foundation

/**
 * Spaced review for questions you got wrong. A miss comes back after 1 day; each later right answer
 * (on or after the day it was due, and not a guess) moves it to 3, 7 and 21 days, then it graduates.
 * A wrong answer starts it again. Worked out from the answer history, so nothing extra is stored.
 */
enum QuestionReview {
    static let STEP_DAYS: [Int64] = [1, 3, 7, 21]
    static let PER_DAY = 5
    static let SOURCE = "review"

    struct Item { let qid: String; let due: Millis; let step: Int }

    /** Where one question stands, from its answers oldest first; nil if never missed or graduated. */
    static func state(_ history: [Attempt]) -> Item? {
        let h = history.filter { $0.source != Readiness.SOURCE_LESSON }
        guard let miss = h.lastIndex(where: { !$0.correct }) else { return nil }
        var step = 0
        var due = dueAfter(h[miss].at, 0)
        for a in h[(miss + 1)...] where a.correct && !a.luckyGuess && Time.startOfDay(a.at) >= due {
            step += 1
            if step >= STEP_DAYS.count { return nil }
            due = dueAfter(a.at, step)
        }
        return Item(qid: h[miss].qid, due: due, step: step)
    }

    private static func dueAfter(_ at: Millis, _ step: Int) -> Millis {
        Time.startOfDay(Time.startOfDay(at) + STEP_DAYS[step] * Time.dayMs + Time.dayMs / 2)
    }

    /** Every missed question due by now, most overdue first. */
    static func due(_ attempts: [Attempt], _ now: Millis) -> [Item] {
        Dictionary(grouping: attempts, by: \.qid).values.compactMap { state($0.sorted { $0.at < $1.at }) }
            .filter { $0.due <= now }.sorted { $0.due < $1.due }
    }

    /** How many more may be reviewed today. */
    static func allowance(_ attempts: [Attempt], _ now: Millis) -> Int {
        let today = Time.startOfDay(now)
        let done = Set(attempts.filter { $0.source == SOURCE && $0.at >= today }.map(\.qid)).count
        return max(0, PER_DAY - done)
    }
}
