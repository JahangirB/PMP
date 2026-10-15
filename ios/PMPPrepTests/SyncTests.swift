import XCTest
@testable import PMPPrep

/** Stands in for the Apps Script: keeps one document and a revision, and can be told to report conflicts. */
private final class FakeTransport: SyncTransport {
    var rev: Int = 0
    var doc: [String: Any]? = nil
    /** How many puts to refuse with a conflict, each time as if another device had just saved otherDoc. */
    var conflicts: Int = 0
    var otherDoc: [String: Any]? = nil
    var puts: Int = 0
    var ops: [String] = []

    func send(_ body: [String: Any]) async throws -> [String: Any] {
        let op: String = body["op"] as? String ?? ""
        ops.append(op)
        if (body["code"] as? String) != "the-code" { return ["ok": false, "error": "bad-code"] }
        switch op {
        case "ping":
            return ["ok": true, "rev": rev, "savedAt": 0]
        case "get":
            var r: [String: Any] = ["ok": true, "rev": rev]
            if let doc { r["doc"] = doc } else { r["doc"] = NSNull() }
            return r
        case "put":
            puts += 1
            if conflicts > 0 {
                conflicts -= 1
                rev += 1
                if let otherDoc { doc = otherDoc }
                var r: [String: Any] = ["ok": false, "conflict": true, "rev": rev]
                if let doc { r["doc"] = doc } else { r["doc"] = NSNull() }
                return r
            }
            if (body["baseRev"] as? Int) != rev {
                var r: [String: Any] = ["ok": false, "conflict": true, "rev": rev]
                if let doc { r["doc"] = doc } else { r["doc"] = NSNull() }
                return r
            }
            doc = body["doc"] as? [String: Any]
            rev += 1
            return ["ok": true, "rev": rev]
        default:
            return ["ok": false, "error": "bad-op"]
        }
    }
}

/** The shared progress format (version 3), the three-way merge and the sync client. */
final class SyncTests: XCTestCase {
    private let device: [String: Any] = ["app": "ios", "id": "test-device"]

    private func fixture(_ name: String) throws -> [String: Any] {
        let bundle = Bundle(for: SyncTests.self)
        let found: URL? = bundle.url(forResource: name, withExtension: "json", subdirectory: "sync")
            ?? bundle.url(forResource: name, withExtension: "json")
        let url = try XCTUnwrap(found, "\(name).json from tests/fixtures/sync is not in the test bundle")
        let text = try String(contentsOf: url, encoding: .utf8)
        return try JObj.parse(text).d
    }

    private func attempt(_ q: String, _ at: Int) -> [String: Any] {
        ["q": q, "t": "1.1", "d": "people", "ap": "agile", "ty": "single", "cr": 1, "ok": true, "ms": 60000, "at": at, "src": "practice"]
    }

    private func doc(_ attempts: [[String: Any]], savedAt: Int) -> [String: Any] {
        ["type": "pmp-progress", "version": 3, "savedAt": savedAt, "attempts": attempts]
    }

    private func qids(_ d: [String: Any]?) -> [String] {
        let list: [Any] = d?["attempts"] as? [Any] ?? []
        return list.compactMap { ($0 as? [String: Any])?["q"] as? String }
    }

    /* ── the merge ── */

    func testMergeMatchesTheSharedFixture() throws {
        let base = try fixture("base")
        let local = try fixture("local")
        let remote = try fixture("remote")
        let expected = try fixture("merged")
        let merged: [String: Any] = SyncMerge.merge(base: base, local: local, remote: remote, now: 5000, device: device)
        let got: String = SyncMerge.canonical(SyncMerge.normalise(merged))
        let want: String = SyncMerge.canonical(SyncMerge.normalise(expected))
        XCTAssertEqual(got, want)
        XCTAssertEqual(SyncMerge.number(merged["savedAt"]), 5000)
        XCTAssertEqual((merged["device"] as? [String: Any])?["id"] as? String, "test-device")
        XCTAssertEqual(merged["flags"] as? [String], ["q1", "q3", "q4"])
        XCTAssertNil((merged["notes"] as? [String: Any])?["q2"])
        XCTAssertNotNil(merged["future"])
    }

    func testMergeWithoutBaseUnitesAndCapsLogs() {
        var chatL: [[String: Any]] = []
        for i in 0..<70 { chatL.append(["r": "user", "t": "m\(i)", "at": 1000 + i]) }
        let local: [String: Any] = ["savedAt": 2, "chat": chatL, "flags": ["a"]]
        let remote: [String: Any] = ["savedAt": 1, "chat": [["r": "model", "t": "x", "at": 5000]], "flags": ["b"]]
        let merged: [String: Any] = SyncMerge.merge(base: nil, local: local, remote: remote, now: 1, device: device)
        let chat: [Any] = merged["chat"] as? [Any] ?? []
        XCTAssertEqual(chat.count, 60)
        XCTAssertEqual((chat.last as? [String: Any])?["t"] as? String, "x")
        XCTAssertEqual(merged["flags"] as? [String], ["a", "b"])
    }

    /* ── the sync client ── */

    func testSyncPutsTheMergeAndRetriesOnConflict() async throws {
        let t = FakeTransport()
        t.rev = 4
        t.doc = doc([attempt("a", 100)], savedAt: 10)
        t.conflicts = 2
        t.otherDoc = doc([attempt("a", 100), attempt("c", 300)], savedAt: 20)
        let local = doc([attempt("b", 200)], savedAt: 30)
        let r: SyncResult = try await CloudSync.sync(t, code: "the-code", base: nil, local: local, now: 99, device: device)
        XCTAssertTrue(r.uploaded)
        XCTAssertEqual(t.puts, 3)
        XCTAssertEqual(qids(r.doc), ["a", "b", "c"])
        XCTAssertEqual(qids(t.doc), ["a", "b", "c"])
        XCTAssertEqual(r.rev, t.rev)
    }

    func testSyncGivesUpAfterThreeRetries() async {
        let t = FakeTransport()
        t.conflicts = 100
        t.otherDoc = doc([attempt("c", 300)], savedAt: 20)
        do {
            _ = try await CloudSync.sync(t, code: "the-code", base: nil, local: doc([attempt("b", 200)], savedAt: 30), now: 99, device: device)
            XCTFail("expected the sync to give up")
        } catch {
            XCTAssertEqual(t.puts, 1 + CloudSync.maxRetries)
        }
    }

    func testSyncSendsNothingWhenTheCloudAlreadyHasIt() async throws {
        let t = FakeTransport()
        t.rev = 7
        let same = doc([attempt("a", 100)], savedAt: 10)
        t.doc = same
        let r: SyncResult = try await CloudSync.sync(t, code: "the-code", base: same, local: same, now: 99, device: device)
        XCTAssertFalse(r.uploaded)
        XCTAssertEqual(t.puts, 0)
        XCTAssertEqual(r.rev, 7)
    }

    func testFirstSyncToAnEmptyScriptUploads() async throws {
        let t = FakeTransport()
        let r: SyncResult = try await CloudSync.sync(t, code: "the-code", base: nil, local: doc([attempt("a", 100)], savedAt: 10), now: 99, device: device)
        XCTAssertTrue(r.uploaded)
        XCTAssertEqual(t.rev, 1)
        XCTAssertEqual(qids(t.doc), ["a"])
    }

    func testWrongCodeIsReported() async {
        let t = FakeTransport()
        do {
            _ = try await CloudSync.ping(t, code: "nope")
            XCTFail("expected bad-code")
        } catch {
            XCTAssertTrue(error.localizedDescription.contains("sync code"))
        }
    }

    func testSyncCodeIs32LettersAndDigits() {
        let a = CloudSync.newCode()
        XCTAssertEqual(a.count, 32)
        XCTAssertTrue(a.allSatisfy { $0.isASCII && ($0.isLetter || $0.isNumber) })
        XCTAssertNotEqual(a, CloudSync.newCode())
        XCTAssertEqual(CloudSync.script("const SYNC_CODE = '__SYNC_CODE__';", code: a), "const SYNC_CODE = '\(a)';")
    }

    /* ── the progress document, version 3 ── */

    func testProgressV3RoundTripKeepsWhatItDoesNotKnow() throws {
        let text = """
        {"type":"pmp-progress","version":3,"savedAt":1000,"device":{"app":"web","id":"w1"},
         "attempts":[{"q":"q1","t":"1.1","d":"people","ap":"agile","ty":"single","cr":1,"ok":true,"ms":61000,"at":1700000000000,"src":"practice","df":4,"h":2,"id":"row-1"}],
         "cards":{"c1":[2.5,6,2,0,1700500000000,1700000000000,1690000000000,1],"c2":[2.5,1,1,0,1,1,1]},
         "mistakes":{"q1":{"c":"tossup","n":"","at":5}},
         "web":{"settings":{"dailyTarget":30},"notepadHtml":"<b>x</b>"},"native":{"k":1},"future":{"x":[1,2]}}
        """
        let dir = FileManager.default.temporaryDirectory.appendingPathComponent(UUID().uuidString)
        let s = ProgressStore(dir: dir)
        try s.restore(text)
        let a0 = try XCTUnwrap(s.attempts.first)
        XCTAssertEqual(a0.hints, 2)
        XCTAssertEqual(a0.difficulty, 4)
        XCTAssertFalse(a0.isEvidence)
        let c1 = try XCTUnwrap(s.cards["c1"])
        XCTAssertTrue(c1.suspended)
        XCTAssertFalse(c1.isDue(Millis.max))
        XCTAssertEqual(s.cards["c2"]?.suspended, false)
        XCTAssertEqual(s.mistakeTags["q1"]?.cause, .TWO_PLAUSIBLE)

        let out: [String: Any] = s.toJson()
        let input: [String: Any] = try JObj.parse(text).d
        XCTAssertEqual(out["type"] as? String, "pmp-progress")
        XCTAssertEqual(SyncMerge.number(out["version"]), 3)
        XCTAssertEqual((out["device"] as? [String: Any])?["app"] as? String, "ios")
        XCTAssertEqual(SyncMerge.canonical(out["web"] ?? NSNull()), SyncMerge.canonical(input["web"] ?? NSNull()))
        XCTAssertEqual(SyncMerge.canonical(out["native"] ?? NSNull()), SyncMerge.canonical(input["native"] ?? NSNull()))
        XCTAssertEqual(SyncMerge.canonical(out["future"] ?? NSNull()), SyncMerge.canonical(input["future"] ?? NSNull()))
        let cards: [String: Any] = out["cards"] as? [String: Any] ?? [:]
        XCTAssertEqual((cards["c1"] as? [Any])?.count, 8)
        let first: [String: Any] = (out["attempts"] as? [[String: Any]])?.first ?? [:]
        XCTAssertEqual(SyncMerge.number(first["h"]), 2)
        XCTAssertEqual(first["id"] as? String, "row-1")
        XCTAssertThrowsError(try s.restore("{\"type\":\"pmp-progress\",\"schemaVersion\":2}"))
        XCTAssertThrowsError(try s.restore("{\"type\":\"pmp-progress\",\"version\":4}"))
    }

    /* ── the shared rules ── */

    func testRulesParseWithDefaults() throws {
        let none: Rules = ContentParser.rules(nil)
        XCTAssertEqual(none.bandTarget, 0.65)
        XCTAssertEqual(none.masteryMinAnswers, 10)
        XCTAssertEqual(none.masteryWindow, 15)
        XCTAssertEqual(none.reentryDays, 5)
        XCTAssertEqual(none.causes.map(\.id), RootCause.allCases.map(\.rawValue))
        let j = try JObj.parse("""
        {"mockBands":{"above":0.8,"target":0.7,"below":0.5},"scenarioCredit":{"best":1,"acceptable":0.6,"premature":0.2,"wrong":0},
         "mastery":{"minAnswers":12,"window":20,"accuracy":0.8,"fadeDays":10},"reentryDays":4,
         "causes":[{"id":"KNOWLEDGE_GAP","short":"Gap","label":"I didn't know","hint":"h"}],
         "confidence":[{"v":3,"id":"sure","label":"Certain"}]}
        """)
        let r: Rules = ContentParser.rules(j)
        XCTAssertEqual(r.band(0.69), .below)
        XCTAssertEqual(r.band(0.7), .target)
        XCTAssertEqual(r.credit(.acceptable), 0.6)
        XCTAssertEqual(r.masteryMinAnswers, 12)
        XCTAssertEqual(r.masteryFadeDays, 10)
        XCTAssertEqual(r.reentryDays, 4)
        XCTAssertEqual(r.cause("KNOWLEDGE_GAP")?.short, "Gap")
        XCTAssertEqual(r.confidenceLabel(3), "Certain")
        XCTAssertEqual(r.confidenceLabel(1), "Guess")
        XCTAssertEqual(RootCause.of("tossup"), .TWO_PLAUSIBLE)
        XCTAssertEqual(RootCause.of("mindset"), .WRONG_MINDSET)
    }
}
