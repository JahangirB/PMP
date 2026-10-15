import Foundation

/**
 * Chooses the questions for a guided-path practice step: unseen first, then the ones you got
 * wrong last time, then the rest. If one exam approach is clearly your weakest, its questions
 * take every other place, so drills lean toward it without becoming a one-approach set.
 */
enum QuestionPicker {
    static let MIN_ANSWERS = 30
    static let MIN_PER_APPROACH = 10
    static let GAP = 0.10

    static func weakApproach(_ attempts: [Attempt]) -> Approach? {
        let atts = attempts.filter { $0.source != Readiness.SOURCE_LESSON && $0.approach != nil }
        if atts.count < MIN_ANSWERS { return nil }
        let overall = atts.reduce(0) { $0 + $1.credit } / Double(atts.count)
        let by = Dictionary(grouping: atts, by: { $0.approach! }).filter { $0.value.count >= MIN_PER_APPROACH }
            .mapValues { l in l.reduce(0) { $0 + $1.credit } / Double(l.count) }
        guard let worst = by.min(by: { $0.value < $1.value }), worst.value <= overall - GAP else { return nil }
        return worst.key
    }

    /** lastCorrect is how a question went the last time it was answered, or nil if never. */
    static func order(_ qs: [Question], lastCorrect: (String) -> Bool?, weak: Approach?, count: Int) -> [Question] {
        let unseen = qs.filter { lastCorrect($0.id) == nil }.shuffled()
        let missed = qs.filter { lastCorrect($0.id) == false }.shuffled()
        let rest = qs.filter { lastCorrect($0.id) == true }.shuffled()
        return Array((tilt(unseen, weak) + tilt(missed, weak) + tilt(rest, weak)).prefix(count))
    }

    private static func tilt(_ list: [Question], _ weak: Approach?) -> [Question] {
        guard let weak else { return list }
        var a = list.filter { $0.approach == weak }
        var b = list.filter { $0.approach != weak }
        var out: [Question] = []
        while !a.isEmpty || !b.isEmpty {
            if !a.isEmpty { out.append(a.removeFirst()) }
            if !b.isEmpty { out.append(b.removeFirst()) }
        }
        return out
    }
}
