import Foundation

/**
 * A Gemini reply written out word by word (index.html §12.2.2 typedText/tick, Typewriter.kt), though it
 * arrives whole: `next` is how far to show after `n` characters, `shown` the text so far with an open ** or ``` closed.
 */
enum Typewriter {
    static let frameNanos: UInt64 = 16_000_000

    static func step(_ text: String) -> Int { max(3, (text.count + 199) / 200) }

    static func next(_ text: String, _ n: Int) -> Int {
        let chars = Array(text)
        let from = n + step(text)
        if from >= chars.count { return chars.count }
        return (from..<chars.count).first { chars[$0].isWhitespace } ?? chars.count
    }

    static func shown(_ text: String, _ n: Int) -> String {
        var out = String(text.prefix(n))
        if out.components(separatedBy: "**").count % 2 == 0 { out += "**" }
        if out.components(separatedBy: "```").count % 2 == 0 { out += "\n```" }
        return out
    }
}
