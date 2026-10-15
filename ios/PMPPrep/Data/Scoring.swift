import Foundation

/**
 * One answer to one question. choices holds the picked option or region ids
 * (single, multi, hotspot); map holds prompt → option (matching, drop-down)
 * or blank → typed text (fill in the blank).
 */
struct Answer: Equatable {
    var choices: Set<String> = []
    var map: [String: String] = [:]
}

struct ScoreResult: Equatable {
    let credit: Double
    var correct: Bool { credit >= 0.9999 }
    var partial: Bool { credit > 0 && !correct }
}

/**
 * The web app's scoring rules (§10.04.2), unchanged: credit runs 0–1 and an
 * item is correct only at full credit. On multiple-response and hotspot items
 * a wrong pick cancels a right one, so selecting everything scores zero.
 */
enum Scoring {
    static func score(_ q: Question, _ a: Answer?) -> ScoreResult {
        let ans = a ?? Answer()
        switch q.base {
        case .single:
            let right = q.options.first(where: \.correct)?.id
            return ScoreResult(credit: right != nil && ans.choices.count == 1 && ans.choices.first == right ? 1 : 0)
        case .multi, .hotspot:
            let want: Set<String> = q.base == .multi ? Set(q.options.filter(\.correct).map(\.id)) : Set(q.regions.filter(\.correct).map(\.id))
            if want.isEmpty { return ScoreResult(credit: 0) }
            let hits = ans.choices.filter { want.contains($0) }.count
            let wrong = ans.choices.count - hits
            if hits == want.count && wrong == 0 { return ScoreResult(credit: 1) }
            return ScoreResult(credit: clamp(Double(hits - wrong) / Double(want.count)))
        case .matching:
            if q.prompts.isEmpty { return ScoreResult(credit: 0) }
            let hit = q.prompts.filter { p in matchFor(q, p.id).map { $0 == ans.map[p.id] } ?? false }.count
            return ScoreResult(credit: Double(hit) / Double(q.prompts.count))
        case .fillblank:
            if q.blanks.isEmpty { return ScoreResult(credit: 0) }
            let hit = q.blanks.filter { blankIsRight($0, ans.map[$0.id]) }.count
            return ScoreResult(credit: Double(hit) / Double(q.blanks.count))
        default:
            return ScoreResult(credit: 0)
        }
    }

    static func isAnswered(_ q: Question, _ a: Answer?) -> Bool {
        guard let a else { return false }
        switch q.base {
        case .single, .multi, .hotspot: return !a.choices.isEmpty
        case .matching: return q.prompts.allSatisfy { !(a.map[$0.id] ?? "").trimmingCharacters(in: .whitespaces).isEmpty }
        case .fillblank: return q.blanks.allSatisfy { !(a.map[$0.id] ?? "").trimmingCharacters(in: .whitespaces).isEmpty }
        default: return false
        }
    }

    /** The option that answers a matching prompt. */
    static func matchFor(_ q: Question, _ promptId: String) -> String? { q.options.first { $0.match == promptId }?.id }

    static func blankIsRight(_ b: Blank, _ raw: String?) -> Bool {
        guard let raw, !raw.trimmingCharacters(in: .whitespaces).isEmpty else { return false }
        if b.numeric {
            guard let v = parseNumber(raw) else { return false }
            return abs(v - b.expect) <= b.tol + 1e-9
        }
        let got = normalise(raw)
        return b.accept.contains { normalise($0) == got }
    }

    /** Accepts the things people type: thousands separators, currency signs, %, and (accounting) negatives. */
    static func parseNumber(_ s: String) -> Double? {
        var t = s.trimmingCharacters(in: .whitespaces)
        if t.isEmpty { return nil }
        if t.hasPrefix("(") && t.hasSuffix(")") { t = "-" + t.dropFirst().dropLast() }
        t = t.replacingOccurrences(of: "[,\\s]", with: "", options: .regularExpression)
            .replacingOccurrences(of: "[$£€%]", with: "", options: .regularExpression)
        guard t.range(of: "^[-+]?\\d*\\.?\\d+([eE][-+]?\\d+)?$", options: .regularExpression) != nil else { return nil }
        return Double(t)
    }

    static func normalise(_ s: String) -> String {
        let t = s.lowercased().folding(options: .diacriticInsensitive, locale: nil)
        return t.replacingOccurrences(of: "[^a-z0-9 ]+", with: " ", options: .regularExpression)
            .replacingOccurrences(of: "\\s+", with: " ", options: .regularExpression)
            .trimmingCharacters(in: .whitespaces)
    }

    /** A blank's expected answer, written the way a person would read it. */
    static func blankAnswer(_ b: Blank) -> String {
        if !b.numeric { return b.accept.first ?? "" }
        let n = formatNumber(b.expect)
        let withUnit: String
        if b.unit.range(of: "^[$£€]$", options: .regularExpression) != nil { withUnit = b.unit + n }
        else if b.unit.isEmpty { withUnit = n }
        else { withUnit = "\(n) \(b.unit)" }
        return b.tol > 0 ? "\(withUnit) (±\(formatNumber(b.tol)))" : withUnit
    }

    static func formatNumber(_ v: Double) -> String {
        let f = NumberFormatter()
        f.numberStyle = .decimal
        f.locale = Locale(identifier: "en_US")
        f.maximumFractionDigits = v.rounded() == v && abs(v) < 1e15 ? 0 : 4
        return f.string(from: NSNumber(value: v)) ?? String(v)
    }

    private static func clamp(_ v: Double) -> Double { max(0, min(1, v)) }
}
