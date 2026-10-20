import Foundation

/** One "/" command in the Gemini chat: typed as /name, it sends `text`. `label` is what the menu shows. */
struct ChatCommand: Equatable, Identifiable {
    var name: String
    var text: String
    var label: String
    var own: Bool
    var id: String { name }
    init(_ name: String, _ text: String, label: String? = nil, own: Bool = true) {
        self.name = name; self.text = text; self.label = label ?? text; self.own = own
    }
}

/**
 * The chat's commands (index.html §12.2.2 COMMANDS, ChatCommands.kt): four built in, then the person's own,
 * kept in the shared `commands` slice as { n, t } so they reach every app after a sync.
 */
enum ChatCommands {
    static let REVIEW = "How am I doing overall? Look at all my figures, tell me honestly where I stand, what is going well, and the three things I should change this week."
    static let BUILT_IN: [ChatCommand] = [
        ChatCommand("progress", REVIEW, label: "How am I doing?", own: false),
        ChatCommand("improve", "What are the three weakest areas in my figures, and exactly what should I do about each this week in this app?", label: "What should I improve?", own: false),
        ChatCommand("plan", "I have about an hour today. Using my figures, what should I do, in order, and why?", label: "Plan my day", own: false),
        ChatCommand("weakest", "Teach me the ECO task where my accuracy is lowest: what PMI expects, the common traps, and one example question with the reasoning.", label: "Explain my weakest task", own: false),
    ]

    private static func nameChars(_ s: Substring) -> Bool {
        s.unicodeScalars.allSatisfy { ("a"..."z").contains($0) || ("0"..."9").contains($0) || $0 == "-" }
    }
    static func isName(_ s: String) -> Bool { !s.isEmpty && s.count <= 24 && nameChars(Substring(s)) }

    static func parse(_ v: Any?) -> [ChatCommand] {
        guard let a = v as? [Any] else { return [] }
        return a.compactMap { x in
            guard let o = x as? [String: Any], let n = o["n"] as? String, let t = o["t"] as? String,
                  isName(n), !t.trimmingCharacters(in: .whitespacesAndNewlines).isEmpty else { return nil }
            return ChatCommand(n, t)
        }
    }

    static func json(_ list: [ChatCommand]) -> [[String: Any]] { list.map { ["n": $0.name, "t": $0.text] } }

    static func all(_ own: [ChatCommand]) -> [ChatCommand] { BUILT_IN + own }

    /** The commands the box asks for while it holds "/" plus a name being typed; nil when the menu is closed. */
    static func matching(_ input: String, _ own: [ChatCommand]) -> [ChatCommand]? {
        guard input.hasPrefix("/") else { return nil }
        let q = input.dropFirst().lowercased()
        guard nameChars(Substring(q)) else { return nil }
        return all(own).filter { $0.name.hasPrefix(q) }
    }

    /** "/name", alone or followed by more words, becomes that command's message. */
    static func expand(_ input: String, _ own: [ChatCommand]) -> String {
        guard input.hasPrefix("/") else { return input }
        let body = input.dropFirst()
        let name = body.prefix { !$0.isWhitespace }.lowercased()
        guard isName(name), let c = all(own).first(where: { $0.name == name }) else { return input }
        let rest = body.dropFirst(name.count).trimmingCharacters(in: .whitespacesAndNewlines)
        return rest.isEmpty ? c.text : c.text + "\n\n" + rest
    }

    /** The cleaned name, or the reason it can't be saved (replacing `old`, or new when nil). */
    static func check(_ name: String, _ text: String, _ own: [ChatCommand], old: String?) -> (name: String?, error: String?) {
        var n = name.trimmingCharacters(in: .whitespacesAndNewlines).lowercased()
        if n.hasPrefix("/") { n.removeFirst() }
        if !isName(n) { return (nil, "Use up to 24 letters, digits or hyphens for the name, with no spaces.") }
        if BUILT_IN.contains(where: { $0.name == n }) || own.contains(where: { $0.name == n && $0.name != old }) {
            return (nil, "/\(n) is already a command. Pick another name.")
        }
        if text.trimmingCharacters(in: .whitespacesAndNewlines).isEmpty { return (nil, "Write the message the command sends.") }
        return (n, nil)
    }

    /** `own` with `old` replaced by (or, when nil, followed by) the command name → text. */
    static func saved(_ own: [ChatCommand], old: String?, name: String, text: String) -> [ChatCommand] {
        let row = ChatCommand(name, text.trimmingCharacters(in: .whitespacesAndNewlines))
        guard let old else { return own + [row] }
        return own.map { $0.name == old ? row : $0 }
    }
}
