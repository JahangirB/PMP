import Foundation

/**
 * A thin, forgiving view of parsed JSON with the same "opt" semantics as Android's org.json,
 * so the content and backup files read exactly as they do on Android.
 */
struct JObj {
    let d: [String: Any]
    init(_ d: [String: Any]) { self.d = d }
    init?(_ any: Any?) { guard let d = any as? [String: Any] else { return nil }; self.d = d }
    static func parse(_ text: String) throws -> JObj {
        let any = try JSONSerialization.jsonObject(with: Data(text.utf8), options: [.fragmentsAllowed])
        guard let d = any as? [String: Any] else { throw JError.notObject }
        return JObj(d)
    }

    func has(_ k: String) -> Bool { d[k] != nil }
    func isNull(_ k: String) -> Bool { d[k] == nil || d[k] is NSNull }
    var keys: [String] { Array(d.keys) }

    func optString(_ k: String, _ def: String = "") -> String {
        guard let v = d[k], !(v is NSNull) else { return def }
        if let s = v as? String { return s }
        if let n = v as? NSNumber { return JObj.numberString(n) }
        return def
    }
    /** "" when missing or null. */
    func str(_ k: String) -> String { optString(k) }
    func strOrNil(_ k: String) -> String? { isNull(k) ? nil : optString(k) }

    func optInt(_ k: String, _ def: Int = 0) -> Int {
        if let n = d[k] as? NSNumber, !JObj.isBool(n) { return n.intValue }
        if let s = d[k] as? String, let v = Double(s) { return Int(v) }
        return def
    }
    func optLong(_ k: String, _ def: Int64 = 0) -> Int64 {
        if let n = d[k] as? NSNumber, !JObj.isBool(n) { return n.int64Value }
        if let s = d[k] as? String, let v = Double(s) { return Int64(v) }
        return def
    }
    func optDouble(_ k: String, _ def: Double = 0) -> Double {
        if let n = d[k] as? NSNumber, !JObj.isBool(n) { return n.doubleValue }
        if let s = d[k] as? String, let v = Double(s) { return v }
        return def
    }
    func optBool(_ k: String, _ def: Bool = false) -> Bool {
        if let n = d[k] as? NSNumber { return JObj.isBool(n) ? n.boolValue : def }
        if let s = d[k] as? String { return s.lowercased() == "true" ? true : (s.lowercased() == "false" ? false : def) }
        return def
    }
    func obj(_ k: String) -> JObj? { JObj(d[k]) }
    func arr(_ k: String) -> [Any]? { d[k] as? [Any] }
    func objects(_ k: String) -> [JObj] { (d[k] as? [Any] ?? []).compactMap { JObj($0) } }
    func strings(_ k: String) -> [String] {
        (d[k] as? [Any] ?? []).map { v in
            if let s = v as? String { return s }
            if let n = v as? NSNumber { return JObj.numberString(n) }
            return ""
        }
    }
    func doubles(_ k: String) -> [Double]? { (d[k] as? [Any])?.map { ($0 as? NSNumber)?.doubleValue ?? 0 } }
    func ints(_ k: String) -> [Int]? { (d[k] as? [Any])?.map { ($0 as? NSNumber)?.intValue ?? 0 } }

    static func isBool(_ n: NSNumber) -> Bool { CFGetTypeID(n) == CFBooleanGetTypeID() }
    static func numberString(_ n: NSNumber) -> String {
        if isBool(n) { return n.boolValue ? "true" : "false" }
        let v = n.doubleValue
        return v.rounded() == v && abs(v) < 1e15 ? String(Int64(v)) : String(v)
    }

    enum JError: LocalizedError {
        case notObject
        var errorDescription: String? { "The file is not a JSON object." }
    }
}

/** Writes JSON with stable key order, for files people may compare. */
enum JWrite {
    static func string(_ any: Any, pretty: Bool = false) -> String {
        var opts: JSONSerialization.WritingOptions = [.sortedKeys, .fragmentsAllowed]
        if pretty { opts.insert(.prettyPrinted) }
        guard let data = try? JSONSerialization.data(withJSONObject: any, options: opts) else { return "{}" }
        return String(decoding: data, as: UTF8.self)
    }
}
