import Foundation

/**
 * Talks straight to Google's Generative Language API with the person's own AI Studio key:
 * no server in between. The key travels in a header, never in a URL, and only to Google.
 */
enum Gemini {
    static let API = "https://generativelanguage.googleapis.com/v1beta"

    /** status is Google's HTTP status, 0 when Google wasn't reached; keyProblem when the key itself was refused. */
    struct GeminiError: LocalizedError {
        let message: String; var status: Int = 0; var keyProblem: Bool = false
        var errorDescription: String? { message }
    }

    struct Model: Codable, Equatable { let name: String; let label: String; let rank: Double
        var stable: Bool { name.range(of: "(preview|exp)", options: [.regularExpression, .caseInsensitive]) == nil }
    }

    /* ── pure parts, unit-tested ── */

    static func flashVersion(_ name: String) -> Double {
        guard let r = name.range(of: "gemini-(\\d+(?:\\.\\d+)?)-flash", options: [.regularExpression, .caseInsensitive]) else { return 0 }
        let m = String(name[r]).lowercased().replacingOccurrences(of: "gemini-", with: "").replacingOccurrences(of: "-flash", with: "")
        return Double(m) ?? 0
    }

    /** Flash chat models, newest first; within a version the stable model leads, then lite, then previews. */
    static func parseModels(_ json: String) -> [Model] {
        guard let o = try? JObj.parse(json) else { return [] }
        let all = o.objects("models").filter { m in
            let name = m.optString("name")
            let gen = m.strings("supportedGenerationMethods").contains("generateContent")
            return gen && flashVersion(name) > 0 && name.range(of: "(tts|image|embedding|audio|live|thinking)", options: [.regularExpression, .caseInsensitive]) == nil
        }.map { m -> Model in
            let n = m.optString("name")
            let rank = flashVersion(n) * 100 - (n.range(of: "(preview|exp)", options: [.regularExpression, .caseInsensitive]) != nil ? 2 : 0) - (n.lowercased().contains("lite") ? 1 : 0)
            let label = m.optString("displayName")
            return Model(name: n, label: label.isEmpty ? n.replacingOccurrences(of: "models/", with: "") : label, rank: rank)
        }.sorted { $0.rank != $1.rank ? $0.rank > $1.rank : $0.name < $1.name }
        let modern = all.filter { flashVersion($0.name) >= 3 }
        return modern.isEmpty ? all : modern
    }

    /** The oldest Flash the coach will use, and the most models kept in the line-up. */
    static let BASE_VERSION = 3.5
    static let MAX_MODELS = 5

    /**
     * The coach's line-up, tried in this order: full Flash models (no Lite) from BASE_VERSION up,
     * newest first, at most MAX_MODELS. A BASE_VERSION model keeps its place while Google lists one.
     */
    static func lineup(_ models: [Model]) -> [Model] {
        let eligible = models.filter { flashVersion($0.name) >= BASE_VERSION - 1e-9 && !$0.name.lowercased().contains("lite") }
            .sorted { $0.rank != $1.rank ? $0.rank > $1.rank : $0.name < $1.name }
        if eligible.count <= MAX_MODELS { return eligible }
        let top = Array(eligible.prefix(MAX_MODELS))
        if top.contains(where: isBase) { return top }
        guard let base = eligible.first(where: isBase) else { return top }
        return Array(top.prefix(MAX_MODELS - 1)) + [base]
    }

    private static func isBase(_ m: Model) -> Bool { abs(flashVersion(m.name) - BASE_VERSION) < 1e-9 }

    /** Worth trying the next model down: not for a refused key or no connection. */
    static func tryNextModel(_ e: GeminiError) -> Bool { !e.keyProblem && e.status != 0 }

    static func requestBody(_ system: String, _ turns: [ChatTurn]) -> Data {
        let body: [String: Any] = [
            "systemInstruction": ["parts": [["text": system]]],
            "contents": turns.map { ["role": $0.role == "model" ? "model" : "user", "parts": [["text": $0.text]]] },
            "generationConfig": ["temperature": 0.6],
        ]
        return (try? JSONSerialization.data(withJSONObject: body)) ?? Data()
    }

    static func parseReply(_ json: String) throws -> String {
        let o = try JObj.parse(json)
        guard let cand = o.objects("candidates").first else {
            if let reason = o.obj("promptFeedback")?.strOrNil("blockReason") { throw GeminiError(message: "Google declined to answer this (\(reason)). Try rephrasing.") }
            throw GeminiError(message: "Google sent back an empty reply. Try again.")
        }
        let text = (cand.obj("content")?.objects("parts") ?? []).filter { !$0.optBool("thought") }.map { $0.optString("text") }.joined()
            .trimmingCharacters(in: .whitespacesAndNewlines)
        if text.isEmpty { throw GeminiError(message: "Google sent back an empty reply (\(cand.optString("finishReason", "no reason given"))). Try again.") }
        return text
    }

    /** Google's error, in words someone can act on. */
    static func explain(_ status: Int, _ body: String) -> String {
        let message = (try? JObj.parse(body))?.obj("error")?.optString("message") ?? ""
        if body.contains("API_KEY_INVALID") { return "Google says this key is not valid. Check you copied all of it, or create a new one in AI Studio." }
        switch status {
        case 429: return "Google is limiting requests on this key: its free quota or per-minute rate is used up. Wait a minute and try again; a daily limit resets the next day."
        case 403: return "This key is not allowed to use the Gemini API. In AI Studio, check the key's project has the Generative Language API enabled."
        case 404: return "That model is not available to this key."
        default: return "Google returned an error (\(status))" + (message.isEmpty ? "." : ": \(message)")
        }
    }

    /* ── network ── */

    private static func call(_ path: String, key: String, body: Data?) async throws -> String {
        guard let url = URL(string: API + path) else { throw GeminiError(message: "Bad request.") }
        var req = URLRequest(url: url, timeoutInterval: 90)
        req.httpMethod = body == nil ? "GET" : "POST"
        req.setValue(key, forHTTPHeaderField: "x-goog-api-key")
        if let body { req.httpBody = body; req.setValue("application/json; charset=utf-8", forHTTPHeaderField: "Content-Type") }
        let data: Data, resp: URLResponse
        do { (data, resp) = try await URLSession.shared.data(for: req) }
        catch { throw GeminiError(message: "Could not reach Google. Check your internet connection and try again.") }
        let status = (resp as? HTTPURLResponse)?.statusCode ?? 0
        let text = String(decoding: data, as: UTF8.self)
        if !(200...299).contains(status) {
            throw GeminiError(message: explain(status, text), status: status, keyProblem: text.contains("API_KEY_INVALID") || status == 401 || status == 403)
        }
        return text
    }

    /** Lists the Flash models this key may use. Doubles as the check that a pasted key works. */
    static func listModels(_ key: String) async throws -> [Model] {
        let models = parseModels(try await call("/models?pageSize=1000", key: key, body: nil))
        if models.isEmpty { throw GeminiError(message: "This key works, but Google lists no Flash chat models for it.") }
        return models
    }

    static func generate(_ key: String, _ model: String, _ system: String, _ turns: [ChatTurn]) async throws -> String {
        let path = "/" + (model.hasPrefix("/") ? String(model.dropFirst()) : model) + ":generateContent"
        return try parseReply(try await call(path, key: key, body: requestBody(system, turns)))
    }
}
