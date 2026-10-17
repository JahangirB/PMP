package com.jahangirb.pmpprep.data

/*
 * Everything the app shows is parsed from the repository's own content
 * (index.html and packs/) into these classes. Nothing is authored here.
 */

enum class Domain(val id: String, val title: String, val short: String) {
    PEOPLE("people", "People", "People"),
    PROCESS("process", "Process", "Process"),
    BUSINESS("business", "Business Environment", "Business");

    companion object {
        fun of(id: String?): Domain? = entries.firstOrNull { it.id == id }
    }
}

enum class Approach(val id: String, val title: String) {
    PREDICTIVE("predictive", "Predictive"),
    AGILE("agile", "Agile"),
    HYBRID("hybrid", "Hybrid");

    companion object {
        fun of(id: String?): Approach? = entries.firstOrNull { it.id == id }
    }
}

/** The seven authored types. PULLDOWN renders as matching, GRAPHIC as hotspot. */
enum class QType(val id: String, val title: String) {
    SINGLE("single", "Multiple choice"),
    MULTI("multi", "Multiple response"),
    MATCHING("matching", "Matching"),
    PULLDOWN("pulldown", "Drop-down"),
    HOTSPOT("hotspot", "Hotspot"),
    GRAPHIC("graphic", "Graphic"),
    FILLBLANK("fillblank", "Fill in the blank");

    val base: QType
        get() = when (this) {
            PULLDOWN -> MATCHING
            GRAPHIC -> HOTSPOT
            else -> this
        }

    companion object {
        fun of(id: String?): QType? = entries.firstOrNull { it.id == id }
    }
}

data class EcoTask(val id: String, val domain: Domain, val n: Int, val label: String, val summary: String) {
    val code: String get() = when (domain) {
        Domain.PEOPLE -> "P$n"
        Domain.PROCESS -> "PR$n"
        Domain.BUSINESS -> "B$n"
    }
}

data class Option(
    val id: String,
    val text: String,
    val correct: Boolean,
    val rationale: String,
    /** matching / pulldown: the prompt this option answers, or null for an unused distractor. */
    val match: String?,
    val fixed: Boolean,
)

data class Prompt(val id: String, val text: String)

data class Region(
    val id: String, val n: Int, val label: String,
    val x: Float, val y: Float, val w: Float, val h: Float,
    val correct: Boolean, val rationale: String,
)

data class Graphic(val viewBox: List<Float>, val title: String, val caption: String, val flow: Boolean)

data class Blank(
    val id: String, val label: String, val numeric: Boolean,
    val expect: Double, val tol: Double, val unit: String,
    val accept: List<String>, val rationale: String,
)

data class Question(
    val id: String,
    val type: QType,
    val domain: Domain,
    val ecoTask: String,
    val approach: Approach?,
    val difficulty: Int,
    val tags: List<String>,
    val stem: String,
    val explanation: String,
    val mindset: String,
    val options: List<Option> = emptyList(),
    val prompts: List<Prompt> = emptyList(),
    val regions: List<Region> = emptyList(),
    val graphic: Graphic? = null,
    val blanks: List<Blank> = emptyList(),
    /** Hotspot items may allow more than one region. */
    val multiSelect: Boolean = false,
    val packId: String = "",
) {
    val base: QType get() = type.base
    val correctCount: Int get() = when (base) {
        QType.HOTSPOT -> regions.count { it.correct }
        else -> options.count { it.correct }
    }
}

data class QuestionPack(
    val id: String,
    val name: String,
    val file: String?,
    val builtin: Boolean,
    val questions: List<Question>,
    val skippedDuplicates: Int = 0,
    val invalid: Int = 0,
    val errors: List<String> = emptyList(),
)

/* Situational trainer: every option is plausible, each carries a rank. */
enum class Rank(val id: String, val title: String, val credit: Float) {
    BEST("best", "Best", 1f),
    ACCEPTABLE("acceptable", "Acceptable", 0.5f),
    PREMATURE("premature", "Premature", 0.25f),
    WRONG("wrong", "Wrong", 0f);

    companion object {
        fun of(id: String?): Rank = entries.firstOrNull { it.id == id } ?: WRONG
    }
}

data class RankedOption(val id: String, val text: String, val rank: Rank, val why: String)

data class Scenario(
    val id: String,
    val domain: Domain?,
    val ecoTask: String,
    val approach: Approach?,
    val difficulty: Int,
    val stem: String,
    val options: List<RankedOption>,
    val mindset: String,
    val explanation: String,
)

data class Deck(val id: String, val label: String, val ecoTask: String?, val summary: String)

data class Flashcard(
    val id: String, val deck: String, val ecoTask: String?,
    val front: String, val back: String, val note: String,
)

data class GlossaryTerm(val term: String, val group: String, val definition: String)

data class Abbreviation(val abbr: String, val full: String, val group: String, val note: String)

data class FormulaItem(
    val id: String, val name: String, val tag: String, val expr: String,
    val where: String, val reads: String, val trap: String,
)

data class FormulaGroup(val id: String, val label: String, val blurb: String, val items: List<FormulaItem>)

/* Learn: modules of lessons, each with sections, a worked example and checks. */
data class LessonModule(val id: String, val order: Int, val prefix: String, val label: String, val domain: Domain?, val blurb: String)

data class LessonSection(val heading: String, val body: String)

data class Lesson(
    val id: String,
    val module: String,
    val order: Int,
    val ecoTask: String,
    val minutes: Int,
    val title: String,
    val objectives: List<String>,
    val examWeight: String,
    val sections: List<LessonSection>,
    val lenses: Map<String, String>,
    val worked: Scenario?,
    val traps: List<String>,
    val checks: List<Question>,
    val terms: List<String>,
    val recap: List<String>,
    val mindset: String,
)

/* Reference library. */
data class RefGroup(val id: String, val n: Int, val label: String, val count: Int, val blurb: String)

data class RefProcess(
    val id: String, val n: String, val name: String, val area: String, val group: String,
    val purpose: String, val inputs: List<String>, val tools: List<String>, val outputs: List<String>, val key: String,
)

data class RefPrinciple(val n: Int, val label: String, val line: String, val body: String, val practice: List<String>, val trap: String)

data class RefPerfDomain(val n: Int, val label: String, val line: String, val body: String, val outcomes: List<String>, val checks: List<String>)

data class RefAgileItem(val name: String, val body: String, val points: List<String>)

data class RefAgileGroup(val label: String, val blurb: String, val items: List<RefAgileItem>)

data class RefTable(val label: String, val blurb: String, val cols: List<String>, val rows: List<List<String>>, val note: String)

data class Reference(
    val groups: List<RefGroup>,
    val areas: List<RefGroup>,
    val processes: List<RefProcess>,
    val principles: List<RefPrinciple>,
    val domains: List<RefPerfDomain>,
    val agile: List<RefAgileGroup>,
    val tables: List<RefTable>,
)

/* Settings the web app keeps in §01, carried over so both apps agree. */
data class ExamLayout(
    val totalItems: Int, val minutes: Int, val breakMinutes: Int,
    val breakAfter: List<Int>, val paceTargetSec: Int,
    /** Of [totalItems], the ones that count; the rest are unscored pretest items (EXAM.SCORED_ITEMS, 170 of 180). */
    val scoredItems: Int = 170,
    /** Items in the case-study block that opens a full sitting (EXAM.CASE_BLOCK_ITEMS, an assumption PMI does not publish). */
    val caseBlockItems: Int = 40,
)

/* Case studies (index.html §05.3 PMP.CASES): a background passage, small exhibit tables, and 8 to 12 ordinary items about it. */
data class CaseExhibit(val id: String, val label: String, val title: String, val cols: List<String>, val rows: List<List<String>>, val note: String)

data class CaseStudy(
    val id: String,
    val title: String,
    val approach: Approach?,
    val setting: String,
    /** One string per paragraph. */
    val passage: List<String>,
    val exhibits: List<CaseExhibit>,
    /** In authored order: a case is read and answered front to back. */
    val questions: List<Question>,
)

/**
 * The adaptive drill's weighting, PMP.PRACTICE.ADAPTIVE in index.html §01.7. [prior] and [priorN]
 * stop a task with two answers from reading as settled evidence; [exponent] sharpens the pull to
 * weak tasks; [unseenBoost] and [rightDamp] favour questions never seen and hold back ones right last time.
 */
data class AdaptiveConfig(
    val prior: Double = 0.5, val priorN: Double = 4.0, val exponent: Double = 1.5, val floor: Double = 0.05,
    val unseenBoost: Double = 1.4, val rightDamp: Double = 0.5,
)

data class SrsConfig(
    val easeStart: Double, val easeMin: Double, val easeAgain: Double, val easeHard: Double, val easeEasy: Double,
    val interval1: Int, val interval2: Int, val intervalEasy: Int, val lapseInterval: Int,
    val hardFactor: Double, val easyBonus: Double, val maxInterval: Int, val newPerDay: Int, val leechThreshold: Int,
)

data class ReadinessConfig(
    val wAccuracy: Int, val wCoverage: Int, val wMocks: Int, val wPace: Int, val wHabit: Int,
    val accuracyHalflifeDays: Double, val accuracyPrior: Double, val accuracyPriorN: Double, val accuracyWindow: Int,
    val accuracyMin: Double, val accuracyMax: Double,
    val coverageTargetPerTask: Int,
    val mockWeights: List<Double>, val mockMin: Double, val mockMax: Double,
    val mockStaleAfterDays: Double, val mockStaleHalflife: Double, val mockStaleFloor: Double,
    val paceBand: List<Double>, val paceSlowZero: Double, val paceFastZero: Double, val paceMinSample: Int,
    val habitWindowDays: Int, val habitTargetDays: Int,
    val gateItems: Int, val gateTasks: Int,
)

data class Extras(
    val ecoLabel: String,
    val domainWeights: Map<Domain, Double>,
    val exam: ExamLayout,
    val srs: SrsConfig,
    val readiness: ReadinessConfig,
    val practicePaceSec: Int,
    val tasks: List<EcoTask>,
    val scenarios: List<Scenario>,
    val decks: List<Deck>,
    val flashcards: List<Flashcard>,
    val formulas: List<FormulaGroup>,
    val reference: Reference,
    val glossary: List<GlossaryTerm>,
    val abbreviations: List<Abbreviation>,
    val modules: List<LessonModule>,
    val lessons: List<Lesson>,
    /** The rules every app shares (docs/progress-format.md §1). */
    val rules: Rules = Rules(),
    /** How the adaptive drill weights questions (§10.02.2). */
    val adaptive: AdaptiveConfig = AdaptiveConfig(),
    /** The Google Apps Script the Cloud sync screen shows, with __SYNC_CODE__ still in it. */
    val syncScript: String = "",
    /** Case studies; empty until index.html ships some, and everything works without them. */
    val cases: List<CaseStudy> = emptyList(),
)

/*
 * The shared rules, PMP.RULES in index.html, carried in extras.json as "rules". The web, Android
 * and iOS read them from one place so a mock band or the mastery bar can't drift between apps.
 * The defaults are the values in docs/progress-format.md §1, used when extras has no "rules".
 */

/** Mock score bands, overall and per domain. PMI reports these four but does not publish the cut scores. */
data class MockBands(val above: Double = 0.80, val target: Double = 0.65, val below: Double = 0.50) {
    fun label(p: Double): String = when {
        p >= above -> "Above Target"
        p >= target -> "Target"
        p >= below -> "Below Target"
        else -> "Needs Improvement"
    }
}

/**
 * A task is mastered with at least [minAnswers] answers (lesson answers, lucky guesses and hinted
 * answers left out) and [accuracy] over the last [window]. It fades after [fadeDays] without an answer.
 */
data class MasteryRule(val minAnswers: Int = 10, val window: Int = 15, val accuracy: Double = 0.75, val fadeDays: Int = 14)

/** How a mistake cause reads: [short] on chips and in reports, [label] (first person) on the tag button, [hint] under it. */
data class CauseText(val id: String, val short: String, val label: String, val hint: String)

/** One "how sure?" answer: [v] is what attempts store (3 sure, 2 think so, 1 guess). */
data class ConfidenceLevel(val v: Int, val id: String, val label: String)

data class Rules(
    val mockBands: MockBands = MockBands(),
    /** Scenario trainer credit per rank. */
    val scenarioCredit: Map<Rank, Double> = Rank.entries.associateWith { it.credit.toDouble() },
    val mastery: MasteryRule = MasteryRule(),
    /** In the order the apps show them. */
    val causes: List<CauseText> = RootCause.entries.map { CauseText(it.name, it.title, it.title, it.description) },
    /** Days away before the welcome-back warm-up. */
    val reentryDays: Int = 5,
    val confidence: List<ConfidenceLevel> = listOf(
        ConfidenceLevel(Attempt.SURE, "sure", "Sure"), ConfidenceLevel(Attempt.THINK, "think", "Think so"),
        ConfidenceLevel(Attempt.GUESS, "guess", "Guess"),
    ),
) {
    fun credit(r: Rank): Double = scenarioCredit[r] ?: r.credit.toDouble()

    fun cause(c: RootCause): CauseText = causes.firstOrNull { it.id == c.name } ?: CauseText(c.name, c.title, c.title, c.description)

    /** The causes as the enum, in the shared order; any the rules leave out follow at the end. */
    val causeOrder: List<RootCause>
        get() = causes.mapNotNull { c -> RootCause.entries.firstOrNull { it.name == c.id } }.distinct()
            .let { it + RootCause.entries.filter { e -> e !in it } }
}
