import Foundation
import Security

/** Carries one request to the sync script and brings back its JSON answer. Faked in the unit tests. */
protocol SyncTransport {
    func send(_ body: [String: Any]) async throws -> [String: Any]
}

/** A sync problem, worded for the Cloud sync screen. */
struct SyncError: LocalizedError {
    let message: String
    var errorDescription: String? { message }
}

/**
 * POSTs to the Apps Script web app (docs/progress-format.md §4). The body is JSON sent as
 * text/plain. Google answers with a 302 to a one-time URL that has to be fetched with GET;
 * URLSession turns a redirected POST into a GET for 302 anyway, and the redirect hook below
 * makes sure of it, with no body.
 */
final class HTTPSyncTransport: NSObject, SyncTransport, URLSessionTaskDelegate {
    let url: URL
    init(url: URL) { self.url = url }

    func send(_ body: [String: Any]) async throws -> [String: Any] {
        var req = URLRequest(url: url, cachePolicy: .reloadIgnoringLocalCacheData, timeoutInterval: 60)
        req.httpMethod = "POST"
        req.setValue("text/plain;charset=utf-8", forHTTPHeaderField: "Content-Type")
        req.httpBody = Data(JWrite.string(body).utf8)
        let result: (Data, URLResponse)
        do {
            result = try await URLSession.shared.data(for: req, delegate: self)
        } catch {
            throw SyncError(message: "Couldn't reach your sync script: \(error.localizedDescription)")
        }
        let data: Data = result.0
        let status: Int = (result.1 as? HTTPURLResponse)?.statusCode ?? 0
        if status != 200 {
            throw SyncError(message: "The script's URL answered with an error (HTTP \(status)). Check that you pasted the Web app URL, ending in /exec.")
        }
        let parsed: Any? = try? JSONSerialization.jsonObject(with: data)
        guard let o = parsed as? [String: Any] else {
            throw SyncError(message: "That URL didn't answer as the sync script. Check that you pasted the Web app URL (ending in /exec) and that Who has access is set to Anyone.")
        }
        return o
    }

    func urlSession(_ session: URLSession, task: URLSessionTask, willPerformHTTPRedirection response: HTTPURLResponse,
                    newRequest request: URLRequest) async -> URLRequest? {
        guard let next = request.url else { return request }
        var get = URLRequest(url: next, cachePolicy: .reloadIgnoringLocalCacheData, timeoutInterval: 60)
        get.httpMethod = "GET"
        return get
    }
}

struct SyncResult {
    /** The merged document: save it as local progress and as the new base. */
    let doc: [String: Any]
    let rev: Int
    /** False when the cloud copy already held everything, so nothing was sent. */
    let uploaded: Bool
}

/** The client side of cloud sync: ping, and the get–merge–put loop with conflict retries. */
enum CloudSync {
    static let maxRetries = 3

    /** Checks the URL and the code. Returns the cloud copy's revision. */
    static func ping(_ t: SyncTransport, code: String) async throws -> Int {
        let body: [String: Any] = ["code": code, "op": "ping"]
        let o = try await t.send(body)
        try check(o)
        return JObj(o).optInt("rev")
    }

    /**
     * 1. get, then merge base, local and remote. 2. If the merge differs from the remote copy, put it
     * with baseRev. 3. On a conflict, merge again with the returned copy and put again, at most 3 times.
     * The caller saves the result as local progress and as base.
     */
    static func sync(_ t: SyncTransport, code: String, base: [String: Any]?, local: [String: Any],
                     now: Millis, device: [String: Any]) async throws -> SyncResult {
        let getBody: [String: Any] = ["code": code, "op": "get"]
        let got = try await t.send(getBody)
        try check(got)
        var rev: Int = JObj(got).optInt("rev")
        var remote: [String: Any]? = got["doc"] as? [String: Any]
        var retries = 0
        while true {
            let merged: [String: Any] = SyncMerge.merge(base: base, local: local, remote: remote ?? [:], now: now, device: device)
            if let r = remote, !SyncMerge.differs(merged, r) {
                return SyncResult(doc: merged, rev: rev, uploaded: false)
            }
            let putBody: [String: Any] = ["code": code, "op": "put", "baseRev": rev, "doc": merged]
            let put = try await t.send(putBody)
            let pj = JObj(put)
            if pj.optBool("ok") { return SyncResult(doc: merged, rev: pj.optInt("rev"), uploaded: true) }
            if !pj.optBool("conflict") {
                try check(put)
                throw SyncError(message: "The script didn't save your progress.")
            }
            retries += 1
            if retries > maxRetries {
                throw SyncError(message: "Another device kept saving at the same moment. Try again in a minute.")
            }
            rev = pj.optInt("rev")
            remote = put["doc"] as? [String: Any]
        }
    }

    /** Throws the script's refusal, worded for people. */
    static func check(_ o: [String: Any]) throws {
        let j = JObj(o)
        if j.optBool("ok") { return }
        let error = j.optString("error")
        switch error {
        case "bad-code":
            throw SyncError(message: "The script refused the sync code. The code here must be exactly the SYNC_CODE in your script, and after changing the script you have to deploy a new version.")
        case "busy":
            throw SyncError(message: "The script was busy with another device. Try again in a moment.")
        case "bad-doc":
            throw SyncError(message: "The script refused the progress file as not a PMP Prep progress document.")
        case "bad-request", "bad-op":
            throw SyncError(message: "The script didn't understand the request. Copy the script from this screen again and deploy a new version.")
        default:
            throw SyncError(message: error.isEmpty ? "The script answered, but not as expected. Copy the script from this screen again and deploy a new version." : "The script said: \(error)")
        }
    }

    /** A random 32-character sync code (letters and digits) from the system's secure generator. */
    static func newCode() -> String {
        let alphabet: [Character] = Array("ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789")
        var out = ""
        while out.count < 32 {
            var bytes = [UInt8](repeating: 0, count: 48)
            let status = SecRandomCopyBytes(kSecRandomDefault, bytes.count, &bytes)
            if status != errSecSuccess { continue }
            for b in bytes where out.count < 32 {
                // 248 = 4 × 62: dropping the top bytes keeps every character equally likely.
                if b < 248 { out.append(alphabet[Int(b) % alphabet.count]) }
            }
        }
        return out
    }

    /** The script text with the code filled in. */
    static func script(_ template: String, code: String) -> String {
        template.replacingOccurrences(of: "__SYNC_CODE__", with: code)
    }
}

/** The document as it was after this device's last sync, the merge's base: Application Support/sync-base.json. */
enum SyncBase {
    static func file(_ dir: URL) -> URL { dir.appendingPathComponent("sync-base.json") }

    static func load(_ dir: URL) -> [String: Any]? {
        guard let text = try? String(contentsOf: file(dir), encoding: .utf8), let o = try? JObj.parse(text) else { return nil }
        return o.d
    }

    static func save(_ doc: [String: Any], _ dir: URL) {
        try? JWrite.string(doc).write(to: file(dir), atomically: true, encoding: .utf8)
    }

    static func delete(_ dir: URL) { try? FileManager.default.removeItem(at: file(dir)) }
}
