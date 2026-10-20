import Foundation

/** A published iPhone build, as the SideStore/AltStore source lists it. */
struct IosRelease: Equatable {
    let build: Int
    let version: String
    let ipaURL: String
    let bytes: Int64
    let date: String
}

/**
 * App updates on iPhone (AppUpdate.kt on Android). iOS lets no app install another build of itself,
 * so the check reads the source that .github/workflows/ios.yml publishes beside the IPA on every build
 * of the default branch, and the update itself goes through SideStore or AltStore, which re-sign it
 * with the person's Apple ID. CI sets CFBundleVersion to the run number and the version to 2.0.<run>.
 */
enum AppUpdate {
    static let source = "https://github.com/JahangirB/PMP/releases/download/ios/source.json"
    static let ipa = "https://github.com/JahangirB/PMP/releases/download/ios/PMP-Prep.ipa"
    static let checkEveryMs: Int64 = 24 * 60 * 60 * 1000

    static func parse(_ data: Data) -> IosRelease? {
        guard let o = (try? JSONSerialization.jsonObject(with: data)) as? [String: Any],
              let apps = o["apps"] as? [[String: Any]],
              let app = apps.first(where: { ($0["bundleIdentifier"] as? String) == "com.jahangirb.pmpprep" }),
              let v = (app["versions"] as? [[String: Any]])?.first,
              let version = v["version"] as? String,
              let url = v["downloadURL"] as? String, url.hasPrefix("https://") else { return nil }
        // buildVersion when the source has it, else the last part of 2.0.<run>.
        let build = Int(v["buildVersion"] as? String ?? "") ?? Int(version.split(separator: ".").last ?? "") ?? 0
        guard build > 0 else { return nil }
        return IosRelease(build: build, version: version, ipaURL: url, bytes: (v["size"] as? NSNumber)?.int64Value ?? 0, date: v["date"] as? String ?? "")
    }

    /** This install's build: CFBundleVersion (1 on builds from before the run number was set). */
    static var installedBuild: Int { Int(Bundle.main.object(forInfoDictionaryKey: "CFBundleVersion") as? String ?? "") ?? 0 }
    static var installedVersion: String { Bundle.main.object(forInfoDictionaryKey: "CFBundleShortVersionString") as? String ?? "" }

    static func isNewer(_ r: IosRelease, than build: Int) -> Bool { r.build > build }

    /** Deep links into the two stores: install this IPA, or add the source so updates show there. */
    static func installLink(_ store: String) -> URL? { URL(string: "\(store)://install?url=\(ipa)") }
    static func sourceLink(_ store: String) -> URL? { URL(string: "\(store)://source?url=\(source)") }

    static func fetchLatest() async throws -> IosRelease? {
        var req = URLRequest(url: URL(string: source)!, cachePolicy: .reloadIgnoringLocalCacheData, timeoutInterval: 20)
        req.setValue("PMP-Prep-iOS", forHTTPHeaderField: "User-Agent")
        let (data, resp) = try await URLSession.shared.data(for: req)
        let code = (resp as? HTTPURLResponse)?.statusCode ?? 0
        if code == 404 { return nil }
        guard (200..<300).contains(code) else { throw Gemini.GeminiError(message: "GitHub answered \(code). Try again later.", status: code) }
        return parse(data)
    }
}
