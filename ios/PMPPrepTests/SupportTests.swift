import XCTest
@testable import PMPPrep

/** The support reminder (docs/progress-format.md §4) and its two shared slices. Mirrors Android's SupportTest. */
final class SupportTests: XCTestCase {
    private let utc: TimeZone = TimeZone(identifier: "UTC")!
    private let dayMs: Millis = 24 * 60 * 60 * 1000
    /** 2026-10-01, noon UTC. */
    private let t0: Millis = 1_790_856_000_000

    /** The app, opened once a day: records the date, asks, and marks the message shown when it is. */
    private final class Sim {
        var opens: [String] = []
        var support: SupportState = SupportState()
        let t0: Millis
        let dayMs: Millis
        let zone: TimeZone
        init(t0: Millis, dayMs: Millis, zone: TimeZone) {
            self.t0 = t0
            self.dayMs = dayMs
            self.zone = zone
        }
        func open(_ i: Int, busy: Bool = false) -> SupportDue {
            let now: Millis = t0 + Millis(i) * dayMs
            let today: String = Support.day(now, zone: zone)
            opens = Support.recordOpen(opens, today)
            let due: SupportDue = Support.due(opens, support, today: today, now: now, busy: busy, zone: zone)
            if due != .nothing { support = Support.markShown(support, now: now, today: today) }
            return due
        }
    }

    private func sim() -> Sim { Sim(t0: t0, dayMs: dayMs, zone: utc) }

    func testDayAndDates() {
        XCTAssertEqual(Support.day(t0, zone: utc), "2026-10-01")
        XCTAssertEqual(Support.addDays("2026-10-31", 1), "2026-11-01")
        XCTAssertEqual(Support.addDays("2026-03-01", -1), "2026-02-28")
    }

    func testNoMessageBeforeSevenDaysThenFirstOnDaySeven() {
        let s: Sim = sim()
        for i in 0..<6 { XCTAssertEqual(s.open(i), .nothing) }
        XCTAssertEqual(s.open(6), .first)
        XCTAssertEqual(s.support.anchor, "2026-10-08")
        XCTAssertEqual(s.support.shownAt, t0 + 6 * dayMs)
    }

    func testCountRestartsAfterShowing() {
        let s: Sim = sim()
        for i in 0...6 { _ = s.open(i) }
        for i in 7...12 { XCTAssertEqual(s.open(i), .nothing, "day \(i)") }
        XCTAssertEqual(s.open(13), .first)
    }

    func testAGapResetsTheStreak() {
        let s: Sim = sim()
        for i in 0...4 { _ = s.open(i) }
        // Day 5 missed.
        for i in 6...11 { XCTAssertEqual(s.open(i), .nothing, "day \(i)") }
        XCTAssertEqual(s.open(12), .first)
    }

    func testPressedIsQuietFor30DaysThenAlreadyOnceThenTheWeeklyCycle() {
        let s: Sim = sim()
        for i in 0...6 { _ = s.open(i) }
        s.support = Support.markPressed(s.support, now: t0 + 6 * dayMs + 1000)
        for i in 7...36 { XCTAssertEqual(s.open(i), .nothing, "day \(i)") }
        XCTAssertEqual(s.open(37), .already)
        for i in 38...43 { XCTAssertEqual(s.open(i), .nothing, "day \(i)") }
        XCTAssertEqual(s.open(44), .already)
    }

    func testBusyShowsNothingAndTheNextOpenDoes() {
        let s: Sim = sim()
        for i in 0...5 { _ = s.open(i) }
        XCTAssertEqual(s.open(6, busy: true), .nothing)
        XCTAssertEqual(s.support, SupportState())
        XCTAssertEqual(s.open(6), .first)
    }

    func testAtMostOnceADay() {
        let opens: [String] = (0...9).map { (i: Int) -> String in Support.day(t0 + Millis(i) * dayMs, zone: utc) }
        let now: Millis = t0 + 9 * dayMs
        let today: String = Support.day(now, zone: utc)
        let earlierToday: SupportState = SupportState(pressedAt: 0, shownAt: now - 3_600_000, anchor: "")
        let yesterday: SupportState = SupportState(pressedAt: 0, shownAt: now - dayMs, anchor: "")
        XCTAssertEqual(Support.due(opens, earlierToday, today: today, now: now, busy: false, zone: utc), .nothing)
        XCTAssertEqual(Support.due(opens, yesterday, today: today, now: now, busy: false, zone: utc), .first)
    }

    func testOpensKeepTheNewest60() {
        var opens: [String] = []
        for i in 0..<70 { opens = Support.recordOpen(opens, Support.day(t0 + Millis(i) * dayMs, zone: utc)) }
        XCTAssertEqual(opens.count, 60)
        XCTAssertEqual(opens.first, Support.day(t0 + 10 * dayMs, zone: utc))
        let last: String = opens.last ?? ""
        XCTAssertEqual(Support.recordOpen(opens, last), opens)
    }

    func testProgressDocumentRoundTrip() throws {
        let dir: URL = FileManager.default.temporaryDirectory.appendingPathComponent(UUID().uuidString)
        let a: ProgressStore = ProgressStore(dir: dir)
        a.recordOpen("2026-10-02")
        a.recordOpen("2026-10-01")
        a.setSupport(SupportState(pressedAt: 5, shownAt: 7, anchor: "2026-10-03"))
        let doc: [String: Any] = a.toJson()
        XCTAssertEqual(doc["opens"] as? [String], ["2026-10-01", "2026-10-02"])
        let sup: [String: Any] = doc["support"] as? [String: Any] ?? [:]
        XCTAssertEqual(sup["anchor"] as? String, "2026-10-03")
        let b: ProgressStore = ProgressStore(dir: dir.appendingPathComponent("b"))
        try b.restore(JWrite.string(doc))
        XCTAssertEqual(b.opens, ["2026-10-01", "2026-10-02"])
        XCTAssertEqual(b.support, SupportState(pressedAt: 5, shownAt: 7, anchor: "2026-10-03"))
        // Unset fields stay out of the document.
        let c: ProgressStore = ProgressStore(dir: dir.appendingPathComponent("c"))
        let empty: [String: Any] = c.toJson()["support"] as? [String: Any] ?? ["missing": true]
        XCTAssertTrue(empty.isEmpty)
    }

    func testMergeKeepsTheHigherSupportValueAndMergesOpensAsASet() {
        let base: [String: Any] = ["type": "pmp-progress", "version": 3, "savedAt": 1,
                                   "opens": ["2026-10-01", "2026-10-02"],
                                   "support": ["pressedAt": 100, "anchor": "2026-10-01"] as [String: Any]]
        let local: [String: Any] = ["type": "pmp-progress", "version": 3, "savedAt": 9,
                                    "opens": ["2026-10-02", "2026-10-03"],
                                    "support": ["pressedAt": 200, "shownAt": 50, "anchor": "2026-10-05"] as [String: Any]]
        let remote: [String: Any] = ["type": "pmp-progress", "version": 3, "savedAt": 5,
                                     "opens": ["2026-10-01", "2026-10-02", "2026-10-04"],
                                     "support": ["pressedAt": 300, "shownAt": 80, "anchor": "2026-10-04"] as [String: Any]]
        let m: [String: Any] = SyncMerge.merge(base: base, local: local, remote: remote, now: 10, device: ["app": "ios", "id": "t"])
        XCTAssertEqual(m["opens"] as? [String], ["2026-10-02", "2026-10-03", "2026-10-04"])
        let s: [String: Any] = m["support"] as? [String: Any] ?? [:]
        XCTAssertEqual(SyncMerge.number(s["pressedAt"]), 300)
        XCTAssertEqual(SyncMerge.number(s["shownAt"]), 80)
        XCTAssertEqual(s["anchor"] as? String, "2026-10-05")
    }
}
