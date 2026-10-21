import XCTest
@testable import PMPPrep

/** Before you start reaches the iPhone with its own wording (index.html §05.13, WelcomeTest.kt). */
final class WelcomeTests: XCTestCase {
    func testSixScreensWithThisPhonesWording() throws {
        let repo = ContentRepository(bundle: .main)
        try repo.load()
        let w = try XCTUnwrap(repo.extras.welcome)
        XCTAssertEqual(w.warn, "Please read this carefully before you start")
        XCTAssertEqual(w.sections.map(\.main), [true, false, false, false, false])
        XCTAssertEqual(w.sections[0].h, "Start with a fixed exam date")
        let all = w.sections.flatMap(\.items) + [w.again]
        XCTAssertTrue(all.allSatisfy { !$0.isEmpty })
        XCTAssertTrue(all.contains { $0.hasPrefix("Your progress is saved on this iPhone") })
        XCTAssertTrue(w.again.contains("Menu → Before you start"))
        XCTAssertFalse(all.contains { $0.contains("browser") || $0.contains("this phone") || $0.contains("Settings and About") })
    }
}
