import Foundation

/*
 * Everything the app shows is parsed from the repository's own content
 * (index.html and packs/) into these types. Nothing is authored here.
 * Ported from the Android app's data/Models.kt.
 */

enum Domain: String, CaseIterable, Identifiable {
    case people, process, business
    var id: String { rawValue }
    var title: String { switch self { case .people: "People"; case .process: "Process"; case .business: "Business Environment" } }
    var short: String { switch self { case .people: "People"; case .process: "Process"; case .business: "Business" } }
    static func of(_ id: String?) -> Domain? { id.flatMap { Domain(rawValue: $0) } }
}

enum Approach: String, CaseIterable, Identifiable {
    case predictive, agile, hybrid
    var id: String { rawValue }
    var title: String { rawValue.prefix(1).uppercased() + rawValue.dropFirst() }
    static func of(_ id: String?) -> Approach? { id.flatMap { Approach(rawValue: $0) } }
}

/** The seven authored types. PULLDOWN renders as matching, GRAPHIC as hotspot. */
enum QType: String, CaseIterable, Identifiable {
    case single, multi, matching, pulldown, hotspot, graphic, fillblank
    var id: String { rawValue }
    var title: String {
        switch self {
        case .single: "Multiple choice"; case .multi: "Multiple response"; case .matching: "Matching"
        case .pulldown: "Drop-down"; case .hotspot: "Hotspot"; case .graphic: "Graphic"; case .fillblank: "Fill in the blank"
        }
    }
    var base: QType { switch self { case .pulldown: .matching; case .graphic: .hotspot; default: self } }
    static func of(_ id: String?) -> QType? { id.flatMap { QType(rawValue: $0) } }
}

struct EcoTask: Identifiable, Hashable {
    let id: String
    let domain: Domain
    let n: Int
    let label: String
    let summary: String
    var code: String {
        switch domain { case .people: "P\(n)"; case .process: "PR\(n)"; case .business: "B\(n)" }
    }
}

struct Option: Identifiable, Hashable {
    let id: String
    let text: String
    let correct: Bool
    let rationale: String
    /** matching / pulldown: the prompt this option answers, or nil for an unused distractor. */
    let match: String?
    let fixed: Bool
}

struct Prompt: Identifiable, Hashable { let id: String; let text: String }

struct Region: Identifiable, Hashable {
    let id: String; let n: Int; let label: String
    let x: Double; let y: Double; let w: Double; let h: Double
    let correct: Bool; let rationale: String
}

struct Graphic: Hashable { let viewBox: [Double]; let title: String; let caption: String; let flow: Bool }

struct Blank: Identifiable, Hashable {
    let id: String; let label: String; let numeric: Bool
    let expect: Double; let tol: Double; let unit: String
    let accept: [String]; let rationale: String
}

struct Question: Identifiable, Hashable {
    let id: String
    let type: QType
    let domain: Domain
    let ecoTask: String
    let approach: Approach?
    let difficulty: Int
    let tags: [String]
    let stem: String
    let explanation: String
    let mindset: String
    var options: [Option] = []
    var prompts: [Prompt] = []
    var regions: [Region] = []
    var graphic: Graphic? = nil
    var blanks: [Blank] = []
    /** Hotspot items may allow more than one region. */
    var multiSelect: Bool = false
    var packId: String = ""

    var base: QType { type.base }
    var correctCount: Int { base == .hotspot ? regions.filter(\.correct).count : options.filter(\.correct).count }

    static func == (a: Question, b: Question) -> Bool { a.id == b.id }
    func hash(into h: inout Hasher) { h.combine(id) }
}

struct QuestionPack: Identifiable {
    let id: String
    let name: String
    let file: String?
    let builtin: Bool
    var questions: [Question]
    var skippedDuplicates: Int = 0
    var invalid: Int = 0
    var errors: [String] = []
}

/* Situational trainer: every option is plausible, each carries a rank. */
enum Rank: String, CaseIterable {
    case best, acceptable, premature, wrong
    var title: String { rawValue.prefix(1).uppercased() + rawValue.dropFirst() }
    /** Credit for picking an option of this rank, from the shared rules. */
    var credit: Double { Rules.current.credit(self) }
    static func of(_ id: String?) -> Rank { id.flatMap { Rank(rawValue: $0) } ?? .wrong }
}

struct RankedOption: Identifiable, Hashable { let id: String; let text: String; let rank: Rank; let why: String }

struct Scenario: Identifiable, Hashable {
    let id: String
    let domain: Domain?
    let ecoTask: String
    let approach: Approach?
    let difficulty: Int
    let stem: String
    let options: [RankedOption]
    let mindset: String
    let explanation: String
}

struct Deck: Identifiable, Hashable { let id: String; let label: String; let ecoTask: String?; let summary: String }

struct Flashcard: Identifiable, Hashable {
    let id: String; let deck: String; let ecoTask: String?
    let front: String; let back: String; let note: String
}

struct GlossaryTerm: Hashable { let term: String; let group: String; let definition: String }
struct Abbreviation: Hashable { let abbr: String; let full: String; let group: String; let note: String }

struct FormulaItem: Identifiable, Hashable {
    let id: String; let name: String; let tag: String; let expr: String
    let `where`: String; let reads: String; let trap: String
}
struct FormulaGroup: Identifiable, Hashable { let id: String; let label: String; let blurb: String; let items: [FormulaItem] }

/* Learn: modules of lessons, each with sections, a worked example and checks. */
struct LessonModule: Identifiable, Hashable { let id: String; let order: Int; let prefix: String; let label: String; let domain: Domain?; let blurb: String }
struct LessonSection: Hashable { let heading: String; let body: String }

struct Lesson: Identifiable, Hashable {
    let id: String
    let module: String
    let order: Int
    let ecoTask: String
    let minutes: Int
    let title: String
    let objectives: [String]
    let examWeight: String
    let sections: [LessonSection]
    let lenses: [(String, String)]
    let worked: Scenario?
    let traps: [String]
    let checks: [Question]
    let terms: [String]
    let recap: [String]
    let mindset: String

    static func == (a: Lesson, b: Lesson) -> Bool { a.id == b.id }
    func hash(into h: inout Hasher) { h.combine(id) }
}

/* Reference library. */
struct RefGroup: Identifiable, Hashable { let id: String; let n: Int; let label: String; let count: Int; let blurb: String }
struct RefProcess: Identifiable, Hashable {
    let id: String; let n: String; let name: String; let area: String; let group: String
    let purpose: String; let inputs: [String]; let tools: [String]; let outputs: [String]; let key: String
}
struct RefPrinciple: Hashable { let n: Int; let label: String; let line: String; let body: String; let practice: [String]; let trap: String }
struct RefPerfDomain: Hashable { let n: Int; let label: String; let line: String; let body: String; let outcomes: [String]; let checks: [String] }
struct RefAgileItem: Hashable { let name: String; let body: String; let points: [String] }
struct RefAgileGroup: Hashable { let label: String; let blurb: String; let items: [RefAgileItem] }
struct RefTable: Hashable { let label: String; let blurb: String; let cols: [String]; let rows: [[String]]; let note: String }

struct Reference {
    let groups: [RefGroup]
    let areas: [RefGroup]
    let processes: [RefProcess]
    let principles: [RefPrinciple]
    let domains: [RefPerfDomain]
    let agile: [RefAgileGroup]
    let tables: [RefTable]
}

/* Settings the web app keeps in §01, carried over so both apps agree. */
struct ExamLayout {
    let totalItems: Int
    let minutes: Int
    let breakMinutes: Int
    let breakAfter: [Int]
    let paceTargetSec: Int
    /** Items in the case-study block that opens a full sitting (EXAM.CASE_BLOCK_ITEMS, an assumption PMI does not publish). */
    var caseBlockItems: Int = 40
}

/* Case studies (index.html §05.3 PMP.CASES): a background passage, small exhibit tables, and 8 to 12 ordinary items about it. */
struct CaseExhibit: Hashable {
    let id: String
    let label: String
    let title: String
    let cols: [String]
    let rows: [[String]]
    let note: String
}

struct CaseStudy: Identifiable, Hashable {
    let id: String
    let title: String
    let approach: Approach?
    let setting: String
    /** One string per paragraph. */
    let passage: [String]
    let exhibits: [CaseExhibit]
    /** In authored order: a case is read and answered front to back. */
    let questions: [Question]

    static func == (a: CaseStudy, b: CaseStudy) -> Bool { a.id == b.id }
    func hash(into h: inout Hasher) { h.combine(id) }
}

struct SrsConfig {
    let easeStart: Double, easeMin: Double, easeAgain: Double, easeHard: Double, easeEasy: Double
    let interval1: Int, interval2: Int, intervalEasy: Int, lapseInterval: Int
    let hardFactor: Double, easyBonus: Double, maxInterval: Int, newPerDay: Int, leechThreshold: Int
}

struct ReadinessConfig {
    let wAccuracy: Int, wCoverage: Int, wMocks: Int, wPace: Int, wHabit: Int
    let accuracyHalflifeDays: Double, accuracyPrior: Double, accuracyPriorN: Double, accuracyWindow: Int
    let accuracyMin: Double, accuracyMax: Double
    let coverageTargetPerTask: Int
    let mockWeights: [Double], mockMin: Double, mockMax: Double
    let mockStaleAfterDays: Double, mockStaleHalflife: Double, mockStaleFloor: Double
    let paceBand: [Double], paceSlowZero: Double, paceFastZero: Double, paceMinSample: Int
    let habitWindowDays: Int, habitTargetDays: Int
    let gateItems: Int, gateTasks: Int
}

struct Extras {
    let ecoLabel: String
    let domainWeights: [Domain: Double]
    let exam: ExamLayout
    let srs: SrsConfig
    let readiness: ReadinessConfig
    let practicePaceSec: Int
    let tasks: [EcoTask]
    let scenarios: [Scenario]
    let decks: [Deck]
    let flashcards: [Flashcard]
    let formulas: [FormulaGroup]
    let reference: Reference
    let glossary: [GlossaryTerm]
    let abbreviations: [Abbreviation]
    let modules: [LessonModule]
    let lessons: [Lesson]
    /** The shared rules (PMP.RULES, docs/progress-format.md §1); the documented defaults when the content has none. */
    var rules: Rules = Rules()
    /** The Google Apps Script the Cloud sync screen shows, with __SYNC_CODE__ still in it. */
    var syncScript: String = ""
    /** The prompt the Make more questions card copies (docs/question-generator-prompt.md). */
    var generatorPrompt: String = ""
    /** Case studies; empty until index.html ships some, and everything works without them. */
    var cases: [CaseStudy] = []
}

/** A mistake root cause as the shared rules describe it. label is first person, for the tag button. */
struct CauseRule: Hashable { let id: String; let short: String; let label: String; let hint: String }

/** One answer to "how sure are you?": v is Attempt.SURE, THINK or GUESS. */
struct ConfidenceRule: Hashable { let v: Int; let id: String; let label: String }

/** The four score bands PMI reports, from the best down. */
enum MockBand: Int, CaseIterable {
    case above, target, below, needs
    var title: String {
        switch self { case .above: "Above Target"; case .target: "Target"; case .below: "Below Target"; case .needs: "Needs Improvement" }
    }
}

/**
 * The rules all three apps share (PMP.RULES in index.html, copied into extras.json as "rules").
 * Every value has the documented default, so a content file without "rules" still behaves the same.
 * Rules.current is set when the content loads.
 */
struct Rules {
    var bandAbove: Double = 0.80
    var bandTarget: Double = 0.65
    var bandBelow: Double = 0.50
    var creditBest: Double = 1
    var creditAcceptable: Double = 0.5
    var creditPremature: Double = 0.25
    var creditWrong: Double = 0
    var masteryMinAnswers: Int = 10
    var masteryWindow: Int = 15
    var masteryAccuracy: Double = 0.75
    var masteryFadeDays: Int = 14
    var causes: [CauseRule] = Rules.defaultCauses
    var reentryDays: Int = 5
    var confidence: [ConfidenceRule] = Rules.defaultConfidence

    static var current = Rules()

    static let defaultCauses: [CauseRule] = [
        CauseRule(id: "KNOWLEDGE_GAP", short: "Knowledge gap", label: "I did not know this",
                  hint: "The material itself was missing. This is the cause that answers to study time."),
        CauseRule(id: "MISREAD_STEM", short: "Misread the question", label: "I misread it",
                  hint: "You knew the material and answered a question that was not quite the one asked: a missed FIRST, NEXT, NOT or BEST."),
        CauseRule(id: "WRONG_MINDSET", short: "Wrong mindset", label: "Wrong instinct",
                  hint: "A defensible action, out of order: escalating before gathering facts, or fixing before asking."),
        CauseRule(id: "TWO_PLAUSIBLE", short: "Two plausible answers", label: "Two answers both worked",
                  hint: "You narrowed it to two and chose wrong. The discriminator was there and was not visible to you."),
        CauseRule(id: "CALCULATION", short: "Calculation slip", label: "I slipped on the numbers",
                  hint: "Arithmetic or an inverted ratio."),
        CauseRule(id: "RUSHED", short: "Rushed", label: "I rushed",
                  hint: "You had it and moved before you had it. Usually a time well under your own median."),
    ]

    static let defaultConfidence: [ConfidenceRule] = [
        ConfidenceRule(v: 3, id: "sure", label: "Sure"),
        ConfidenceRule(v: 2, id: "think", label: "Think so"),
        ConfidenceRule(v: 1, id: "guess", label: "Guess"),
    ]

    func band(_ p: Double) -> MockBand {
        if p >= bandAbove { return .above }
        if p >= bandTarget { return .target }
        if p >= bandBelow { return .below }
        return .needs
    }

    func credit(_ r: Rank) -> Double {
        switch r { case .best: return creditBest; case .acceptable: return creditAcceptable; case .premature: return creditPremature; case .wrong: return creditWrong }
    }

    func cause(_ id: String) -> CauseRule? { causes.first { $0.id == id } }

    func confidenceLabel(_ v: Int) -> String {
        if let c = confidence.first(where: { $0.v == v }) { return c.label }
        switch v { case 3: return "Sure"; case 2: return "Think so"; case 1: return "Guess"; default: return "" }
    }
}
