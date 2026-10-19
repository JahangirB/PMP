import Foundation

/** One case in a sitting's case block, with the items taken from it (all of them unless the sitting is short). */
struct CasePick {
    let caseStudy: CaseStudy
    let questions: [Question]
}

/**
 * Where case studies go in a sitting, and which case a practice run uses. Mirrors Android's CasePlan.
 *
 * The real exam opens with a case-study block. index.html §01.2 assumes it is EXAM.CASE_BLOCK_ITEMS (40)
 * of the 180 items, and §10.03.1 layoutFor() scales it for a shorter sitting: max(1, round(40 × n / 180)).
 * The block is filled with whole cases, so every item can show its passage; it is never shuffled into the
 * rest of the paper, and a case's items stay in their authored order. With no cases the block is empty and
 * the paper is exactly what it was before cases existed.
 */
enum CasePlan {

    /** Case-block size for a sitting of n (index.html layoutFor): the full block at total or more, else scaled, at least 1, never more than n. */
    static func caseItemsFor(_ n: Int, total: Int = 180, caseBlock: Int = 40) -> Int {
        if n <= 0 || caseBlock <= 0 { return 0 }
        if n >= total { return min(caseBlock, n) }
        let raw: Double = Double(caseBlock) * Double(n) / Double(total)
        // JS Math.round: half rounds up.
        let rounded: Int = Int((raw + 0.5).rounded(.down))
        return min(max(rounded, 1), n)
    }

    /** How many of a case's items have been answered before. */
    static func seenCount(_ c: CaseStudy, seen: (String) -> Bool) -> Int {
        var k: Int = 0
        for q in c.questions where seen(q.id) { k += 1 }
        return k
    }

    /** Cases with the fewest items answered before first, ties in random order. */
    static func freshestFirst(_ cases: [CaseStudy], seen: (String) -> Bool) -> [CaseStudy] {
        let shuffled: [CaseStudy] = cases.filter { (c: CaseStudy) -> Bool in !c.questions.isEmpty }.shuffled()
        var keyed: [(Int, Int, CaseStudy)] = []
        for (i, c) in shuffled.enumerated() {
            let k: Int = seenCount(c, seen: seen)
            keyed.append((k, i, c))
        }
        keyed.sort { (a: (Int, Int, CaseStudy), b: (Int, Int, CaseStudy)) -> Bool in
            if a.0 != b.0 { return a.0 < b.0 }
            return a.1 < b.1
        }
        return keyed.map { (t: (Int, Int, CaseStudy)) -> CaseStudy in t.2 }
    }

    /**
     * The case block: whole cases, freshest first, each taken while it still fits in target. If not even
     * one case fits whole (a short sitting), the opening items of the freshest case, so the block still
     * reads as one case.
     */
    static func pickCases(_ cases: [CaseStudy], target: Int, seen: (String) -> Bool) -> [CasePick] {
        if target <= 0 { return [] }
        let order: [CaseStudy] = freshestFirst(cases, seen: seen)
        guard let first = order.first else { return [] }
        var out: [CasePick] = []
        var used: Int = 0
        for c in order where used + c.questions.count <= target {
            out.append(CasePick(caseStudy: c, questions: c.questions))
            used += c.questions.count
        }
        if out.isEmpty {
            out.append(CasePick(caseStudy: first, questions: Array(first.questions.prefix(target))))
        }
        return out
    }

    /** The block's items in paper order. */
    static func block(_ picks: [CasePick]) -> [Question] {
        var out: [Question] = []
        for p in picks { out.append(contentsOf: p.questions) }
        return out
    }

    /** The whole paper: the case block first, then the rest, with no item twice. */
    static func paper(_ caseBlock: [Question], rest: [Question]) -> [Question] {
        let ids: Set<String> = Set(caseBlock.map { (q: Question) -> String in q.id })
        let tail: [Question] = rest.filter { (q: Question) -> Bool in !ids.contains(q.id) }
        return caseBlock + tail
    }

    /** The case a "Case studies" practice run should use: the one with fewest items answered before. */
    static func nextCase(_ cases: [CaseStudy], seen: (String) -> Bool) -> CaseStudy? {
        freshestFirst(cases, seen: seen).first
    }

    /** "Question 3 of 10 in this case", or nil if qid is not one of the case's items. */
    static func position(_ c: CaseStudy, qid: String) -> String? {
        guard let i = c.questions.firstIndex(where: { (q: Question) -> Bool in q.id == qid }) else { return nil }
        return "Question \(i + 1) of \(c.questions.count) in this case"
    }
}
