import XCTest
@testable import PMPPrep

/** Case studies and their place in a mock, checked the way Android's CasePlanTest checks them. */
final class CaseTests: XCTestCase {

    private func item(_ id: String) -> [String: Any] {
        let a: [String: Any] = ["id": "a", "text": "Right", "correct": true, "rationale": "r"]
        let b: [String: Any] = ["id": "b", "text": "Wrong", "correct": false, "rationale": "r"]
        return ["id": id, "type": "single", "domain": "process", "ecoTask": "process-1", "approach": "hybrid",
                "stem": "A stem long enough to be a stem for " + id + ".", "explanation": "e", "mindset": "m",
                "options": [a, b]]
    }

    private func caseJSON(_ id: String, _ n: Int) -> [String: Any] {
        var qs: [[String: Any]] = []
        for i in 1...n { qs.append(item("pmp-" + id + "-" + String(format: "%03d", i))) }
        let exhibit: [String: Any] = ["id": "ex1", "label": "Exhibit 1", "title": "T", "cols": ["A", "B"], "rows": [["1", "2"]]]
        return ["id": id, "title": "Case " + id, "approach": "hybrid", "setting": "s",
                "passage": ["First paragraph.", "Second paragraph."], "exhibits": [exhibit], "questions": qs]
    }

    private func cases(_ sizes: [Int]) -> [CaseStudy] {
        var list: [JObj] = []
        for (i, n) in sizes.enumerated() { list.append(JObj(caseJSON("c\(i + 1)", n))) }
        return ContentParser.cases(list)
    }

    func testParsesTheCaseShape() {
        let all: [CaseStudy] = cases([10])
        XCTAssertEqual(all.count, 1)
        let c: CaseStudy = all[0]
        XCTAssertEqual(c.passage, ["First paragraph.", "Second paragraph."])
        XCTAssertEqual(c.exhibits.first?.label, "Exhibit 1")
        XCTAssertEqual(c.exhibits.first?.rows ?? [], [["1", "2"]])
        XCTAssertEqual(c.questions.count, 10)
        XCTAssertEqual(c.questions.first?.packId, "case:c1")
    }

    func testEmptyCasesChangeNothing() {
        XCTAssertTrue(ContentParser.cases([]).isEmpty)
        XCTAssertTrue(CasePlan.pickCases([], target: 40, seen: { (_: String) -> Bool in false }).isEmpty)
        XCTAssertNil(CasePlan.nextCase([], seen: { (_: String) -> Bool in false }))
        let rest: [Question] = cases([5])[0].questions
        let paper: [Question] = CasePlan.paper([], rest: rest)
        XCTAssertEqual(paper.map { (q: Question) -> String in q.id }, rest.map { (q: Question) -> String in q.id })
    }

    func testBlockSizeFollowsTheWebLayout() {
        XCTAssertEqual(CasePlan.caseItemsFor(180, total: 180, caseBlock: 40), 40)
        XCTAssertEqual(CasePlan.caseItemsFor(90, total: 180, caseBlock: 40), 20)
        XCTAssertEqual(CasePlan.caseItemsFor(45, total: 180, caseBlock: 40), 10)
        XCTAssertEqual(CasePlan.caseItemsFor(20, total: 180, caseBlock: 40), 4)
        XCTAssertEqual(CasePlan.caseItemsFor(2, total: 180, caseBlock: 40), 1)
        XCTAssertEqual(CasePlan.caseItemsFor(0, total: 180, caseBlock: 40), 0)
    }

    /** A full mock: 40 case items from whole cases first, in authored order, then the rest with no repeats. */
    func testFullMockPlacesTheCaseBlockFirst() {
        let all: [CaseStudy] = cases([10, 10, 10, 10, 10])
        let target: Int = CasePlan.caseItemsFor(180, total: 180, caseBlock: 40)
        let picks: [CasePick] = CasePlan.pickCases(all, target: target, seen: { (_: String) -> Bool in false })
        XCTAssertEqual(picks.count, 4)
        for p in picks {
            XCTAssertEqual(p.questions.map { (q: Question) -> String in q.id }, p.caseStudy.questions.map { (q: Question) -> String in q.id })
        }
        let caseBlock: [Question] = CasePlan.block(picks)
        XCTAssertEqual(caseBlock.count, 40)
        var rest: [Question] = []
        for c in all { rest.append(contentsOf: c.questions) }
        rest.shuffle()
        let paper: [Question] = CasePlan.paper(caseBlock, rest: rest)
        XCTAssertEqual(paper.count, 50)
        XCTAssertEqual(Array(paper.prefix(40)).map { (q: Question) -> String in q.id }, caseBlock.map { (q: Question) -> String in q.id })
        XCTAssertEqual(Set(paper.map { (q: Question) -> String in q.id }).count, 50)
    }

    func testShortSittingTakesTheOpeningOfOneCase() {
        let all: [CaseStudy] = cases([10, 10])
        let picks: [CasePick] = CasePlan.pickCases(all, target: 4, seen: { (_: String) -> Bool in false })
        XCTAssertEqual(picks.count, 1)
        let p: CasePick = picks[0]
        XCTAssertEqual(p.questions.map { (q: Question) -> String in q.id },
                       Array(p.caseStudy.questions.prefix(4)).map { (q: Question) -> String in q.id })
    }

    func testCasesYouHaveSeenComeLast() {
        let all: [CaseStudy] = cases([10, 10, 10])
        var seenIds: Set<String> = Set(all[0].questions.map { (q: Question) -> String in q.id })
        for q in all[2].questions.prefix(3) { seenIds.insert(q.id) }
        let seen: (String) -> Bool = { (id: String) -> Bool in seenIds.contains(id) }
        for _ in 0..<10 {
            XCTAssertEqual(CasePlan.nextCase(all, seen: seen)?.id, "c2")
            XCTAssertEqual(CasePlan.pickCases(all, target: 10, seen: seen).first?.caseStudy.id, "c2")
        }
        XCTAssertEqual(CasePlan.position(all[1], qid: all[1].questions[2].id), "Question 3 of 10 in this case")
        XCTAssertNil(CasePlan.position(all[1], qid: "nope"))
    }
}
