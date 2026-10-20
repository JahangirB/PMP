import XCTest
@testable import PMPPrep

/** App updates on iPhone: the SideStore source CI publishes (ios.yml), against this build. */
final class AppUpdateTests: XCTestCase {
    private func source(_ version: [String: Any]) -> Data {
        try! JSONSerialization.data(withJSONObject: ["name": "PMP Prep", "apps": [["bundleIdentifier": "com.jahangirb.pmpprep", "versions": [version]]]])
    }

    func testReadsTheNewestVersion() {
        let r = AppUpdate.parse(source(["version": "2.0.57", "buildVersion": "57", "date": "2026-10-20T12:00:00Z",
                                        "downloadURL": "https://github.com/JahangirB/PMP/releases/download/ios/PMP-Prep.ipa", "size": 9_000_000]))
        XCTAssertEqual(r?.build, 57)
        XCTAssertEqual(r?.bytes, 9_000_000)
        // Without buildVersion, the last part of the version.
        XCTAssertEqual(AppUpdate.parse(source(["version": "2.0.58", "downloadURL": "https://x/y.ipa"]))?.build, 58)
        XCTAssertNil(AppUpdate.parse(source(["version": "2.0", "downloadURL": "http://insecure/y.ipa"])))
        XCTAssertNil(AppUpdate.parse(Data("nope".utf8)))
    }

    func testNewerAndLinks() {
        let r = IosRelease(build: 57, version: "2.0.57", ipaURL: "https://x/y.ipa", bytes: 0, date: "")
        XCTAssertTrue(AppUpdate.isNewer(r, than: 1))
        XCTAssertFalse(AppUpdate.isNewer(r, than: 57))
        XCTAssertEqual(AppUpdate.installLink("sidestore")?.absoluteString, "sidestore://install?url=" + AppUpdate.ipa)
        XCTAssertEqual(AppUpdate.sourceLink("altstore")?.absoluteString, "altstore://source?url=" + AppUpdate.source)
    }
}
