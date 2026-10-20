import XCTest
@testable import PMPPrep

/** The chat's "/" commands (index.html §12.2.2, ChatCommandsTest.kt): built in, the person's own, and synced. */
final class ChatCommandsTests: XCTestCase {
    private let own: [ChatCommand] = [ChatCommand("agile", "Quiz me on agile.")]

    func testTheMenuFollowsTheTypedName() {
        XCTAssertEqual(ChatCommands.matching("/", own)?.map(\.name), ["progress", "improve", "plan", "weakest", "agile"])
        XCTAssertEqual(ChatCommands.matching("/PL", own)?.map(\.name), ["plan"])
        XCTAssertNil(ChatCommands.matching("/plan now", own))
        XCTAssertNil(ChatCommands.matching("hello", own))
    }

    func testATypedCommandBecomesItsMessage() {
        XCTAssertEqual(ChatCommands.expand("/agile", own), "Quiz me on agile.")
        XCTAssertEqual(ChatCommands.expand("/weakest in agile", own), ChatCommands.BUILT_IN[3].text + "\n\nin agile")
        XCTAssertEqual(ChatCommands.expand("/nothing", own), "/nothing")
    }

    func testNamesAreCheckedAndTheCommandsSync() throws {
        XCTAssertEqual(ChatCommands.check("plan", "x", own, old: nil).error, "/plan is already a command. Pick another name.")
        XCTAssertEqual(ChatCommands.check("/Agile", "x", own, old: "agile").name, "agile")
        XCTAssertNotNil(ChatCommands.check("two words", "x", own, old: nil).error)
        let dir: URL = FileManager.default.temporaryDirectory.appendingPathComponent(UUID().uuidString)
        let a: ProgressStore = ProgressStore(dir: dir)
        a.setChatCommands(own)
        let doc: [String: Any] = a.toJson()
        XCTAssertEqual((doc["commands"] as? [[String: Any]])?.first?["n"] as? String, "agile")
        let b: ProgressStore = ProgressStore(dir: dir.appendingPathComponent("b"))
        try b.restore(JWrite.string(doc))
        XCTAssertEqual(b.chatCommands, own)
        b.setChatCommands([])
        XCTAssertNil(b.toJson()["commands"])
    }
}
