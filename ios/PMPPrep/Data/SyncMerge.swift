import Foundation

/**
 * The three-way merge of progress documents (docs/progress-format.md §3), the same in every app.
 * It works on plain JSON dictionaries, so fields and keys this app doesn't know pass through.
 * Equality is canonical JSON: sorted keys, compared as text.
 */
enum SyncMerge {
    typealias Doc = [String: Any]

    /** A log slice: records are never deleted, only united by identity. */
    struct LogSpec {
        let key: String
        /** The fields that together identify a record. */
        let fields: [String]
        /** The field logs are sorted by: "at", or the week start for weekly reports. */
        let sortField: String
        let cap: Int
    }

    static let logs: [LogSpec] = [
        LogSpec(key: "attempts", fields: ["q", "at"], sortField: "at", cap: 20000),
        LogSpec(key: "scenarios", fields: ["id", "at"], sortField: "at", cap: Int.max),
        LogSpec(key: "mocks", fields: ["at", "title"], sortField: "at", cap: Int.max),
        LogSpec(key: "sessions", fields: ["k", "at"], sortField: "at", cap: Int.max),
        LogSpec(key: "diagnostics", fields: ["at"], sortField: "at", cap: Int.max),
        LogSpec(key: "chat", fields: ["at", "r"], sortField: "at", cap: 60),
        LogSpec(key: "sittings", fields: ["k", "at"], sortField: "at", cap: 2000),
        LogSpec(key: "checkins", fields: ["kind", "at"], sortField: "at", cap: Int.max),
        LogSpec(key: "weekly", fields: ["w"], sortField: "w", cap: 104),
        LogSpec(key: "drills", fields: ["at", "g"], sortField: "at", cap: 2000),
    ]
    static let setKeys: [String] = ["flags", "lessons", "opens"]
    /** Maps merged per key; how a key changed on both sides is settled. */
    static let mapKeys: [(String, Tie)] = [("notes", .savedAt), ("mistakes", .at), ("cards", .lastMs), ("snoozed", .savedAt), ("support", .higher)]
    /** App-only blocks: merged per key, never interpreted. */
    static let blockKeys: [String] = ["web", "native"]
    static let valueKeys: [String] = ["plan", "notepad"]
    static let headerKeys: Set<String> = ["type", "version", "savedAt", "device"]

    enum Tie { case savedAt, at, lastMs, higher }

    /** Every top-level key the merge treats specially; any other key is merged as a whole value. */
    static var knownKeys: Set<String> {
        var s = headerKeys
        for l in logs { s.insert(l.key) }
        for k in setKeys { s.insert(k) }
        for (k, _) in mapKeys { s.insert(k) }
        for k in blockKeys { s.insert(k) }
        for k in valueKeys { s.insert(k) }
        s.insert("mycards")
        return s
    }

    /**
     * Merges local and remote against base (the document as it was after this device's last sync;
     * nil or empty before the first). The result carries savedAt = now and this device.
     */
    static func merge(base: Doc?, local: Doc, remote: Doc, now: Millis, device: [String: Any]) -> Doc {
        let b: Doc = base ?? [:]
        let localLater: Bool = number(local["savedAt"]) >= number(remote["savedAt"])
        var out: Doc = [:]

        for spec in logs {
            let l: [Any] = local[spec.key] as? [Any] ?? []
            let r: [Any] = remote[spec.key] as? [Any] ?? []
            out[spec.key] = mergeLog(spec, l, r)
        }
        for k in setKeys {
            out[k] = mergeSet(b[k] as? [Any], local[k] as? [Any], remote[k] as? [Any])
        }
        for (k, tie) in mapKeys {
            out[k] = mergeMap(b[k] as? [String: Any], local[k] as? [String: Any], remote[k] as? [String: Any], tie: tie, localLater: localLater)
        }
        out["mycards"] = mergeById(b["mycards"] as? [Any], local["mycards"] as? [Any], remote["mycards"] as? [Any], localLater: localLater)
        for k in blockKeys {
            let bb: [String: Any]? = b[k] as? [String: Any]
            let lb: [String: Any]? = local[k] as? [String: Any]
            let rb: [String: Any]? = remote[k] as? [String: Any]
            if bb == nil && lb == nil && rb == nil { continue }
            out[k] = mergeMap(bb, lb, rb, tie: .savedAt, localLater: localLater)
        }

        // plan, notepad and every key no app has told this one about: three-way as a whole.
        let known = knownKeys
        var wholeKeys: Set<String> = Set(valueKeys)
        for k in b.keys where !known.contains(k) { wholeKeys.insert(k) }
        for k in local.keys where !known.contains(k) { wholeKeys.insert(k) }
        for k in remote.keys where !known.contains(k) { wholeKeys.insert(k) }
        for k in wholeKeys {
            if let v = mergeValue(b[k], local[k], remote[k], localLater: localLater) { out[k] = v }
        }

        out["type"] = ProgressStore.TYPE
        out["version"] = ProgressStore.VERSION
        out["savedAt"] = now
        out["device"] = device
        return out
    }

    /* ── the four kinds ─────────────────────────────────────────────────── */

    /** Union by identity, keeping the local record when both hold it; sorted, then capped to the newest. */
    static func mergeLog(_ spec: LogSpec, _ local: [Any], _ remote: [Any]) -> [Any] {
        var seen = Set<String>()
        var out: [[String: Any]] = []
        for item in local + remote {
            guard let o = item as? [String: Any] else { continue }
            if seen.insert(identity(spec, o)).inserted { out.append(o) }
        }
        let sorted: [[String: Any]] = sortLog(spec, out)
        if sorted.count > spec.cap { return Array(sorted.suffix(spec.cap)) }
        return sorted
    }

    static func identity(_ spec: LogSpec, _ o: [String: Any]) -> String {
        let parts: [String] = spec.fields.map { (f: String) -> String in text(o[f]) }
        return parts.joined(separator: "\u{1}")
    }

    static func sortLog(_ spec: LogSpec, _ items: [[String: Any]]) -> [[String: Any]] {
        let keyed: [(Double, String, [String: Any])] = items.map { (o: [String: Any]) -> (Double, String, [String: Any]) in
            (number(o[spec.sortField]), identity(spec, o), o)
        }
        let sorted = keyed.sorted { (a: (Double, String, [String: Any]), c: (Double, String, [String: Any])) -> Bool in
            if a.0 != c.0 { return a.0 < c.0 }
            return a.1 < c.1
        }
        return sorted.map { $0.2 }
    }

    /** Kept if kept on both sides; removed if removed on either; added if added on either. Sorted ascending. */
    static func mergeSet(_ base: [Any]?, _ local: [Any]?, _ remote: [Any]?) -> [String] {
        let b = Set(strings(base))
        let l = Set(strings(local))
        let r = Set(strings(remote))
        var out = Set<String>()
        for e in b.union(l).union(r) {
            if b.contains(e) {
                if l.contains(e) && r.contains(e) { out.insert(e) }
            } else if l.contains(e) || r.contains(e) {
                out.insert(e)
            }
        }
        return out.sorted()
    }

    /** Per key, three-way. A key changed on both sides is settled by tie. A missing value is a deletion. */
    static func mergeMap(_ base: [String: Any]?, _ local: [String: Any]?, _ remote: [String: Any]?, tie: Tie, localLater: Bool) -> [String: Any] {
        let b: [String: Any] = base ?? [:]
        let l: [String: Any] = local ?? [:]
        let r: [String: Any] = remote ?? [:]
        var keys = Set<String>(b.keys)
        keys.formUnion(l.keys)
        keys.formUnion(r.keys)
        var out: [String: Any] = [:]
        for k in keys {
            let bv: Any? = b[k]
            let lv: Any? = l[k]
            let rv: Any? = r[k]
            let lChanged = !same(lv, bv)
            let rChanged = !same(rv, bv)
            var pick: Any? = nil
            if !lChanged { pick = rv }
            else if !rChanged { pick = lv }
            else if same(lv, rv) { pick = lv }
            else { pick = settle(lv, rv, tie: tie, localLater: localLater) }
            if let pick { out[k] = pick }
        }
        return out
    }

    /** Both sides changed a key differently: cards keep the later review, mistakes the later tag, the rest the later copy. */
    static func settle(_ lv: Any?, _ rv: Any?, tie: Tie, localLater: Bool) -> Any? {
        if tie == .higher { return larger(lv, rv) }
        guard let lv, let rv else { return localLater ? lv : rv }
        switch tie {
        case .lastMs:
            let la: [Any] = lv as? [Any] ?? []
            let ra: [Any] = rv as? [Any] ?? []
            let l5: Double = la.count > 5 ? number(la[5]) : 0
            let r5: Double = ra.count > 5 ? number(ra[5]) : 0
            if l5 != r5 { return l5 > r5 ? lv : rv }
        case .at:
            let lAt: Double = number((lv as? [String: Any])?["at"])
            let rAt: Double = number((rv as? [String: Any])?["at"])
            if lAt != rAt { return lAt > rAt ? lv : rv }
        case .savedAt, .higher:
            break
        }
        return localLater ? lv : rv
    }

    /** support (§4): the higher value, numbers by value and the anchor date as text; an absent side loses. */
    static func larger(_ lv: Any?, _ rv: Any?) -> Any? {
        guard let lv else { return rv }
        guard let rv else { return lv }
        if let ln = lv as? NSNumber, let rn = rv as? NSNumber {
            return ln.doubleValue >= rn.doubleValue ? lv : rv
        }
        let ls: String = text(lv)
        let rs: String = text(rv)
        return ls >= rs ? lv : rv
    }

    /** mycards: a list keyed by id, merged as a map and written sorted by id. */
    static func mergeById(_ base: [Any]?, _ local: [Any]?, _ remote: [Any]?, localLater: Bool) -> [Any] {
        let merged = mergeMap(byId(base), byId(local), byId(remote), tie: .savedAt, localLater: localLater)
        let ids: [String] = merged.keys.sorted()
        return ids.compactMap { merged[$0] }
    }

    static func byId(_ list: [Any]?) -> [String: Any] {
        var out: [String: Any] = [:]
        for item in list ?? [] {
            guard let o = item as? [String: Any] else { continue }
            let id = text(o["id"])
            if !id.isEmpty && out[id] == nil { out[id] = o }
        }
        return out
    }

    /** Three-way as a whole: the side that changed; if both did, the later copy. nil means absent. */
    static func mergeValue(_ base: Any?, _ local: Any?, _ remote: Any?, localLater: Bool) -> Any? {
        let lChanged = !same(local, base)
        let rChanged = !same(remote, base)
        if !lChanged { return remote }
        if !rChanged { return local }
        if same(local, remote) { return local }
        return localLater ? local : remote
    }

    /* ── comparison ─────────────────────────────────────────────────────── */

    /** Canonical JSON: sorted keys, no spaces. */
    static func canonical(_ v: Any) -> String { JWrite.string(v) }

    static func same(_ a: Any?, _ b: Any?) -> Bool {
        guard let a else { return b == nil }
        guard let b else { return false }
        return canonical(a) == canonical(b)
    }

    /**
     * The document as the merge fixture compares it (tests/fixtures/sync/README.md): savedAt and device
     * dropped, a missing shared slice the same as an empty one, logs and sets in their sorted order.
     */
    static func normalise(_ doc: Doc) -> Doc {
        var o = doc
        o["savedAt"] = nil
        o["device"] = nil
        for spec in logs {
            let items: [[String: Any]] = (o[spec.key] as? [Any] ?? []).compactMap { $0 as? [String: Any] }
            o[spec.key] = sortLog(spec, items)
        }
        for k in setKeys { o[k] = strings(o[k] as? [Any]).sorted() }
        for (k, _) in mapKeys where o[k] == nil { o[k] = [String: Any]() }
        let cards: [String: Any] = byId(o["mycards"] as? [Any])
        o["mycards"] = cards.keys.sorted().compactMap { cards[$0] }
        if o["notepad"] == nil { o["notepad"] = "" }
        return o
    }

    /** True when the two documents hold different progress (savedAt and device aside). */
    static func differs(_ a: Doc, _ b: Doc) -> Bool { canonical(normalise(a)) != canonical(normalise(b)) }

    /* ── helpers ────────────────────────────────────────────────────────── */

    static func number(_ v: Any?) -> Double {
        if let n = v as? NSNumber { return n.doubleValue }
        if let s = v as? String, let d = Double(s) { return d }
        return 0
    }

    static func text(_ v: Any?) -> String {
        if let s = v as? String { return s }
        if let n = v as? NSNumber { return JObj.numberString(n) }
        return ""
    }

    static func strings(_ list: [Any]?) -> [String] {
        (list ?? []).map { (v: Any) -> String in text(v) }
    }
}
