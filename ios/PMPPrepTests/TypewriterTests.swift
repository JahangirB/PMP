import XCTest
@testable import PMPPrep

/** The reply written out word by word (index.html §12.2.2, TypewriterTest.kt). */
final class TypewriterTests: XCTestCase {
    func testWholeWordsAndClosedMarkup() {
        let text = "Your **weakest** area is scope. Review five questions today."
        let chars = Array(text)
        var n = 0
        var cuts: [Int] = []
        while n < chars.count { n = Typewriter.next(text, n); cuts.append(n) }
        XCTAssertEqual(cuts.last, chars.count)
        XCTAssertTrue(cuts.dropLast().allSatisfy { chars[$0].isWhitespace })
        XCTAssertEqual(Typewriter.shown(text, 9), "Your **we**")
        XCTAssertEqual(Typewriter.shown("a\n```\ncode", 10), "a\n```\ncode\n```")
    }
}
