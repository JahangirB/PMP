import XCTest
@testable import PMPPrep

/** Theory reaches the phone whole (index.html §05.12, TheoryTest.kt): every chapter, block and figure. */
final class TheoryTests: XCTestCase {
    func testEveryChapterArrives() throws {
        let repo = ContentRepository(bundle: .main)
        try repo.load()
        let t: Theory = repo.extras.theory
        XCTAssertEqual(t.parts.count, 6)
        XCTAssertEqual(t.planned, 26)
        XCTAssertEqual(t.chapters.count, t.planned)
        XCTAssertEqual([0, 1, 2, 24, 25].map { t.numberOf(t.chapters[$0]) }, [1, 2, 3, 25, 26])
        XCTAssertTrue(t.chapters.allSatisfy { !$0.sections.isEmpty && !$0.keyIdeas.isEmpty && $0.minutes > 0 })
        var figs = 0, kinds = Set<String>()
        for c in t.chapters { for s in c.sections { for b in s.blocks {
            if case .fig = b { figs += 1 }
            if case .box(let k, _) = b { kinds.insert(k) }
        } } }
        XCTAssertGreaterThanOrEqual(figs, 26)
        XCTAssertEqual(kinds, ["exam", "myth", "example"])
    }
}
