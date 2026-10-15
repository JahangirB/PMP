import Foundation

/**
 * All questions and study content. Built-in content ships in the app bundle (made at
 * build time from index.html and packs/); imported packs live as files in
 * Application Support/packs and load after the built-in ones. An id that is already
 * loaded is skipped, never overwritten, so answer history always points at the
 * question it was recorded against.
 */
final class ContentRepository {
    private(set) var extras: Extras!
    private(set) var packs: [QuestionPack] = []
    private(set) var byId: [String: Question] = [:]
    private var disabled = Set<String>()
    private let defaults = UserDefaults.standard
    private let bundle: Bundle

    init(bundle: Bundle = .main) { self.bundle = bundle }

    private var userDir: URL {
        let u = FileManager.default.urls(for: .applicationSupportDirectory, in: .userDomainMask)[0].appendingPathComponent("packs")
        try? FileManager.default.createDirectory(at: u, withIntermediateDirectories: true)
        return u
    }

    func load() throws {
        packs = []; byId = [:]
        extras = try ContentParser.extras(try resource("content/extras.json"))
        add(try ContentParser.parsePack(try resource("content/core.json"), id: "core", fallbackName: "Core pack (built in)", file: nil, builtin: true))
        let packDir = bundle.resourceURL?.appendingPathComponent("packs")
        let files = ((packDir.flatMap { try? FileManager.default.contentsOfDirectory(atPath: $0.path) }) ?? [])
            .filter { $0.hasSuffix(".json") && $0 != "index.json" }
            .sorted(by: ContentRepository.natural)
        for f in files {
            if let text = try? resource("packs/\(f)"), let p = try? ContentParser.parsePack(text, id: "asset:\(f)", fallbackName: nil, file: f, builtin: true) { add(p) }
        }
        let user = ((try? FileManager.default.contentsOfDirectory(atPath: userDir.path)) ?? []).filter { $0.hasSuffix(".json") }.sorted()
        for f in user {
            // A damaged imported file must not stop the app from opening.
            if let text = try? String(contentsOf: userDir.appendingPathComponent(f), encoding: .utf8),
               let p = try? ContentParser.parsePack(text, id: "user:\(f)", fallbackName: nil, file: f, builtin: false) { add(p) }
        }
        disabled = Set(defaults.stringArray(forKey: "disabledPacks") ?? [])
    }

    @discardableResult
    private func add(_ p: QuestionPack) -> QuestionPack {
        var pack = p
        let fresh = p.questions.filter { byId[$0.id] == nil }
        pack.questions = fresh
        pack.skippedDuplicates = p.questions.count - fresh.count
        for q in fresh { byId[q.id] = q }
        packs.append(pack)
        return pack
    }

    func isEnabled(_ p: QuestionPack) -> Bool { !disabled.contains(p.id) }

    func setEnabled(_ p: QuestionPack, _ on: Bool) {
        if on { disabled.remove(p.id) } else { disabled.insert(p.id) }
        defaults.set(Array(disabled), forKey: "disabledPacks")
    }

    /** Questions from every enabled pack. */
    func pool() -> [Question] { packs.filter { isEnabled($0) }.flatMap(\.questions) }

    /** Validates, stores and loads an imported pack, returning it as loaded. */
    func importPack(_ text: String, displayName: String) throws -> QuestionPack {
        var base = displayName.replacingOccurrences(of: ".json", with: "")
            .replacingOccurrences(of: "[^A-Za-z0-9._-]+", with: "-", options: .regularExpression)
            .trimmingCharacters(in: CharacterSet(charactersIn: "-"))
        if base.isEmpty { base = "pack" }
        let file = "\(base)-\(Time.now()).json"
        let parsed = try ContentParser.parsePack(text, id: "user:\(file)", fallbackName: base, file: file, builtin: false)
        if parsed.questions.isEmpty {
            throw PackError("No usable questions in this file." + (parsed.errors.isEmpty ? "" : "\n\n" + parsed.errors.joined(separator: "\n")))
        }
        if parsed.questions.allSatisfy({ byId[$0.id] != nil }) {
            throw PackError("All \(parsed.questions.count) questions in this file are already in the app.")
        }
        try text.write(to: userDir.appendingPathComponent(file), atomically: true, encoding: .utf8)
        return add(parsed)
    }

    func deletePack(_ p: QuestionPack) {
        guard !p.builtin, let f = p.file else { return }
        try? FileManager.default.removeItem(at: userDir.appendingPathComponent(f))
        packs.removeAll { $0.id == p.id }
        for q in p.questions { byId[q.id] = nil }
    }

    func task(_ id: String) -> EcoTask? { extras.tasks.first { $0.id == id } }

    private func resource(_ path: String) throws -> String {
        guard let url = bundle.resourceURL?.appendingPathComponent(path) else { throw PackError("Missing \(path)") }
        return try String(contentsOf: url, encoding: .utf8)
    }

    /** "people-2" before "people-10". */
    static func natural(_ a: String, _ b: String) -> Bool { a.localizedStandardCompare(b) == .orderedAscending }
}
