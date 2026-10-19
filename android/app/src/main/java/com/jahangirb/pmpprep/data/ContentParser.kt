package com.jahangirb.pmpprep.data

import org.json.JSONArray
import org.json.JSONObject

/** Turns the JSON in assets (and imported packs) into model classes. Pure, so it is unit-tested on the JVM. */
object ContentParser {

    class PackException(message: String) : Exception(message)

    /* ── small JSON helpers ──────────────────────────────────────────────── */

    private fun JSONObject.str(key: String): String = if (isNull(key)) "" else optString(key, "")

    private fun JSONObject.strOrNull(key: String): String? = if (!has(key) || isNull(key)) null else optString(key)

    private fun JSONArray?.objects(): List<JSONObject> {
        if (this == null) return emptyList()
        return (0 until length()).mapNotNull { optJSONObject(it) }
    }

    private fun JSONArray?.strings(): List<String> {
        if (this == null) return emptyList()
        return (0 until length()).map { optString(it) }
    }

    private fun JSONObject.f(key: String): Float = optDouble(key, 0.0).toFloat()

    /* ── question packs ──────────────────────────────────────────────────── */

    /**
     * Parses a pmp-pack file (or a bare array of questions). Invalid items are
     * skipped and listed in [QuestionPack.errors]; ids repeated inside the file
     * are skipped too. Duplicates against other packs are handled by the caller.
     */
    fun parsePack(text: String, id: String, fallbackName: String?, file: String?, builtin: Boolean): QuestionPack {
        val trimmed = text.trim().removePrefix("﻿")
        val items: JSONArray
        var name = fallbackName
        try {
            if (trimmed.startsWith("[")) {
                items = JSONArray(trimmed)
            } else {
                val env = JSONObject(trimmed)
                val type = env.optString("type", "pmp-pack")
                if (type != "pmp-pack") throw PackException("This is not a question pack (its type is \"$type\").")
                if (env.optInt("schemaVersion", 2) > 2) throw PackException("This pack was made for a newer version of the app.")
                items = env.optJSONArray("questions") ?: throw PackException("The file has no \"questions\" list.")
                if (env.has("name")) name = env.optString("name")
            }
        } catch (e: PackException) {
            throw e
        } catch (e: Exception) {
            throw PackException("The file is not valid JSON: ${e.message}")
        }
        val out = ArrayList<Question>()
        val errors = ArrayList<String>()
        val seen = HashSet<String>()
        var invalid = 0
        for (i in 0 until items.length()) {
            val o = items.optJSONObject(i)
            var err = if (o == null) "not an object" else validate(o)
            if (err == null && !seen.add(o!!.optString("id"))) err = "id repeated inside this file"
            if (err != null) {
                invalid++
                if (errors.size < 20) {
                    val qid = o?.optString("id").orEmpty()
                    errors += "#${i + 1}${if (qid.isEmpty()) "" else " ($qid)"}: $err"
                }
                continue
            }
            out += question(o!!, id)
        }
        return QuestionPack(
            id = id,
            name = name ?: file?.removeSuffix(".json") ?: id,
            file = file,
            builtin = builtin,
            questions = out,
            invalid = invalid,
            errors = errors,
        )
    }

    /** Returns null when the item can be shown and scored, otherwise the reason it cannot. */
    fun validate(o: JSONObject): String? {
        if (o.optString("id").isBlank()) return "missing id"
        val type = QType.of(o.optString("type")) ?: return "unknown type \"${o.optString("type")}\""
        if (Domain.of(o.optString("domain")) == null) return "domain must be people, process or business"
        if (o.optString("stem").isBlank()) return "missing stem"
        when (type.base) {
            QType.SINGLE, QType.MULTI, QType.MATCHING -> {
                val opts = o.optJSONArray("options").objects()
                if (opts.size < 2) return "needs at least two options"
                opts.forEachIndexed { i, op ->
                    if (op.optString("id").isEmpty() || op.optString("text").isEmpty()) return "option ${i + 1} needs an id and text"
                }
                val correct = opts.count { it.optBoolean("correct") }
                if (type.base == QType.SINGLE && correct != 1) return "a single-answer item needs exactly one correct option"
                if (type.base == QType.MULTI && correct < 1) return "a multiple-response item needs correct options"
                if (type.base == QType.MATCHING) {
                    val prompts = o.optJSONArray("prompts").objects()
                    if (prompts.isEmpty()) return "matching needs prompts"
                    prompts.forEachIndexed { i, p ->
                        val pid = p.optString("id")
                        if (pid.isEmpty() || opts.none { it.strOrNull("match") == pid }) return "prompt ${i + 1} has no matching option"
                    }
                }
            }
            QType.HOTSPOT -> {
                val regions = o.optJSONArray("regions").objects()
                if (regions.isEmpty()) return "hotspot needs regions"
                regions.forEachIndexed { i, r ->
                    if (!r.has("x") || !r.has("y") || !r.has("w") || !r.has("h")) return "region ${i + 1} needs x, y, w and h"
                }
                if (regions.none { it.optBoolean("correct") }) return "hotspot needs a correct region"
            }
            QType.FILLBLANK -> {
                val blanks = o.optJSONArray("blanks").objects()
                if (blanks.isEmpty()) return "fill-in-the-blank needs blanks"
                blanks.forEachIndexed { i, b ->
                    if (b.optString("id").isEmpty()) return "blank ${i + 1} needs an id"
                    val ok = if (b.optBoolean("numeric")) b.has("expect") else b.optJSONArray("accept")?.length() ?: 0 > 0
                    if (!ok) return "blank ${i + 1} needs expect (numeric) or accept (text)"
                }
            }
            else -> {}
        }
        return null
    }

    fun question(o: JSONObject, packId: String, fallbackDomain: Domain = Domain.PROCESS): Question {
        val type = QType.of(o.optString("type")) ?: QType.SINGLE
        val g = o.optJSONObject("graphic")
        val regions = o.optJSONArray("regions").objects().mapIndexed { i, r ->
            Region(
                id = r.str("id"), n = r.optInt("n", i + 1), label = r.str("label"),
                x = r.f("x"), y = r.f("y"), w = r.f("w"), h = r.f("h"),
                correct = r.optBoolean("correct"), rationale = r.str("rationale"),
            )
        }
        return Question(
            id = o.str("id"),
            type = type,
            domain = Domain.of(o.optString("domain")) ?: fallbackDomain,
            ecoTask = o.str("ecoTask"),
            approach = Approach.of(o.optString("approach")),
            difficulty = o.optInt("difficulty", 3),
            tags = o.optJSONArray("tags").strings(),
            stem = o.str("stem"),
            explanation = o.str("explanation"),
            mindset = o.str("mindset"),
            options = o.optJSONArray("options").objects().map { op ->
                Option(
                    id = op.str("id"), text = op.str("text"), correct = op.optBoolean("correct"),
                    rationale = op.str("rationale"), match = op.strOrNull("match"), fixed = op.optBoolean("fixed"),
                )
            },
            prompts = o.optJSONArray("prompts").objects().map { Prompt(it.str("id"), it.str("text")) },
            regions = regions,
            graphic = g?.let {
                val vb = it.optString("viewBox", "0 0 660 130").trim().split(Regex("[\\s,]+")).mapNotNull { n -> n.toFloatOrNull() }
                Graphic(if (vb.size == 4) vb else listOf(0f, 0f, 660f, 130f), it.str("title"), it.str("caption"), it.optBoolean("flow", true))
            },
            blanks = o.optJSONArray("blanks").objects().map { b ->
                Blank(
                    id = b.str("id"), label = b.str("label"), numeric = b.optBoolean("numeric"),
                    expect = b.optDouble("expect", 0.0), tol = b.optDouble("tol", 0.0), unit = b.str("unit"),
                    accept = b.optJSONArray("accept").strings(), rationale = b.str("rationale"),
                )
            },
            multiSelect = type.base == QType.HOTSPOT && (o.optBoolean("multi") || regions.count { it.correct } > 1),
            packId = packId,
        )
    }

    /* ── everything else ─────────────────────────────────────────────────── */

    fun extras(text: String): Extras {
        val o = JSONObject(text)
        val eco = o.getJSONObject("eco")
        val weights = eco.optJSONObject("DOMAIN_WEIGHTS")
        val tasks = o.optJSONArray("tasks").objects().mapNotNull { t ->
            val d = Domain.of(t.optString("domain")) ?: return@mapNotNull null
            EcoTask(t.str("id"), d, t.optInt("n"), t.str("label"), t.str("summary"))
        }
        val taskDomain = tasks.associate { it.id to it.domain }

        val exam = o.getJSONObject("exam")
        val srs = o.getJSONObject("srs")
        val rd = o.getJSONObject("readiness")
        val rw = rd.getJSONObject("WEIGHTS")

        val modules = o.optJSONArray("modules").objects().map { m ->
            val d = Domain.of(m.strOrNull("domain"))
            LessonModule(m.str("id"), m.optInt("order"), m.str("prefix"), m.strOrNull("label") ?: d?.title ?: m.str("id"), d, m.str("blurb"))
        }
        val moduleDomain = modules.associate { it.id to it.domain }

        return Extras(
            ecoLabel = eco.str("LABEL"),
            domainWeights = Domain.entries.associateWith { weights?.optDouble(it.id, 0.0) ?: 0.0 },
            exam = ExamLayout(
                totalItems = exam.optInt("TOTAL_ITEMS", 180),
                minutes = exam.optInt("MINUTES", 230),
                breakMinutes = exam.optInt("BREAK_MINUTES", 10),
                breakAfter = listOf(exam.optInt("BREAK_1_AFTER", 60), exam.optInt("BREAK_2_AFTER", 120)),
                paceTargetSec = exam.optDouble("PACE_TARGET_SEC", 80.0).toInt(),
                scoredItems = exam.optInt("SCORED_ITEMS", 170),
                caseBlockItems = exam.optInt("CASE_BLOCK_ITEMS", 40),
            ),
            srs = SrsConfig(
                easeStart = srs.optDouble("EASE_START", 2.5), easeMin = srs.optDouble("EASE_MIN", 1.3),
                easeAgain = srs.optDouble("EASE_AGAIN", 0.2), easeHard = srs.optDouble("EASE_HARD", 0.15),
                easeEasy = srs.optDouble("EASE_EASY", 0.15), interval1 = srs.optInt("INTERVAL_1", 1),
                interval2 = srs.optInt("INTERVAL_2", 6), intervalEasy = srs.optInt("INTERVAL_EASY", 4),
                lapseInterval = srs.optInt("LAPSE_INTERVAL", 1), hardFactor = srs.optDouble("HARD_FACTOR", 1.2),
                easyBonus = srs.optDouble("EASY_BONUS", 1.3), maxInterval = srs.optInt("MAX_INTERVAL", 365),
                newPerDay = srs.optInt("NEW_PER_DAY", 20), leechThreshold = srs.optInt("LEECH_THRESHOLD", 6),
            ),
            readiness = ReadinessConfig(
                wAccuracy = rw.optInt("accuracy", 35), wCoverage = rw.optInt("coverage", 20), wMocks = rw.optInt("mocks", 25),
                wPace = rw.optInt("pace", 10), wHabit = rw.optInt("habit", 10),
                accuracyHalflifeDays = rd.optDouble("ACCURACY_HALFLIFE_DAYS", 21.0),
                accuracyPrior = rd.optDouble("ACCURACY_PRIOR", 0.5), accuracyPriorN = rd.optDouble("ACCURACY_PRIOR_N", 8.0),
                accuracyWindow = rd.optInt("ACCURACY_WINDOW", 400),
                accuracyMin = rd.optDouble("ACCURACY_MIN", 0.4), accuracyMax = rd.optDouble("ACCURACY_MAX", 0.85),
                coverageTargetPerTask = rd.optInt("COVERAGE_TARGET_PER_TASK", 5),
                mockWeights = rd.optJSONArray("MOCK_WEIGHTS")?.let { a -> (0 until a.length()).map { a.optDouble(it) } } ?: listOf(0.5, 0.3, 0.2),
                mockMin = rd.optDouble("MOCK_MIN", 0.45), mockMax = rd.optDouble("MOCK_MAX", 0.8),
                mockStaleAfterDays = rd.optDouble("MOCK_STALE_AFTER_DAYS", 45.0),
                mockStaleHalflife = rd.optDouble("MOCK_STALE_HALFLIFE", 90.0), mockStaleFloor = rd.optDouble("MOCK_STALE_FLOOR", 0.5),
                paceBand = rd.optJSONArray("PACE_BAND")?.let { a -> (0 until a.length()).map { a.optDouble(it) } } ?: listOf(45.0, 80.0),
                paceSlowZero = rd.optDouble("PACE_SLOW_ZERO", 130.0), paceFastZero = rd.optDouble("PACE_FAST_ZERO", 15.0),
                paceMinSample = rd.optInt("PACE_MIN_SAMPLE", 30),
                habitWindowDays = rd.optInt("HABIT_WINDOW_DAYS", 14), habitTargetDays = rd.optInt("HABIT_TARGET_DAYS", 10),
                gateItems = rd.optInt("GATE_ITEMS", 100), gateTasks = rd.optInt("GATE_TASKS", 15),
            ),
            practicePaceSec = o.optJSONObject("practice")?.optInt("PACE_TARGET_SEC", 76) ?: 76,
            tasks = tasks,
            scenarios = o.optJSONArray("scenarios").objects().map { scenario(it) },
            decks = o.optJSONArray("decks").objects().map { Deck(it.str("id"), it.str("label"), it.strOrNull("ecoTask"), it.str("summary")) },
            flashcards = o.optJSONArray("flashcards").objects().map {
                Flashcard(it.str("id"), it.str("deck"), it.strOrNull("ecoTask"), it.str("front"), it.str("back"), it.str("note"))
            },
            formulas = o.optJSONArray("formulas").objects().map { g ->
                FormulaGroup(g.str("id"), g.str("label"), g.str("blurb"), g.optJSONArray("items").objects().map { f ->
                    FormulaItem(f.str("id"), f.str("name"), f.str("tag"), f.str("expr"), f.str("where"), f.str("reads"), f.str("trap"),
                        f.optJSONObject("example")?.let { e -> FormulaExample(e.str("situation"), e.optJSONArray("steps").strings(), e.str("means")) })
                })
            },
            reference = reference(o.getJSONObject("reference")),
            glossary = o.optJSONArray("glossary").objects().map { GlossaryTerm(it.str("t"), it.str("g"), it.str("d")) },
            abbreviations = o.optJSONArray("abbreviations").objects().map {
                Abbreviation(it.str("abbr"), it.str("full"), it.str("group"), it.str("note"))
            },
            modules = modules,
            lessons = o.optJSONArray("lessons").objects().map { l ->
                val fallback = taskDomain[l.str("ecoTask")] ?: moduleDomain[l.str("module")] ?: Domain.PROCESS
                val lenses = l.optJSONObject("lenses")
                Lesson(
                    id = l.str("id"), module = l.str("module"), order = l.optInt("order"), ecoTask = l.str("ecoTask"),
                    minutes = l.optInt("minutes"), title = l.str("title"),
                    objectives = l.optJSONArray("objectives").strings(), examWeight = l.str("examWeight"),
                    sections = l.optJSONArray("sections").objects().map { LessonSection(it.str("heading"), it.str("body"), figure(it.optJSONObject("figure"))) },
                    lenses = lenses?.keys()?.asSequence()?.associateWith { lenses.optString(it) } ?: emptyMap(),
                    worked = l.optJSONObject("worked")?.let { w ->
                        w.put("id", l.str("id") + "-worked")
                        scenario(w)
                    },
                    traps = l.optJSONArray("traps").strings(),
                    checks = l.optJSONArray("checks").objects().filter { c ->
                        // Checks carry no domain of their own; give them the lesson's before validating.
                        if (!c.has("domain")) c.put("domain", (taskDomain[c.str("ecoTask")] ?: fallback).id)
                        validate(c) == null
                    }.map { question(it, "lesson:" + l.str("id"), fallback) },
                    terms = l.optJSONArray("terms").strings(),
                    recap = l.optJSONArray("recap").strings(),
                    mindset = l.str("mindset"),
                )
            },
            rules = rules(o.optJSONObject("rules")),
            adaptive = o.optJSONObject("practice")?.optJSONObject("ADAPTIVE").let { a ->
                val d = AdaptiveConfig()
                if (a == null) d else AdaptiveConfig(
                    a.optDouble("PRIOR", d.prior), a.optDouble("PRIOR_N", d.priorN), a.optDouble("EXPONENT", d.exponent),
                    a.optDouble("FLOOR", d.floor), a.optDouble("UNSEEN_BOOST", d.unseenBoost), a.optDouble("RIGHT_DAMP", d.rightDamp),
                )
            },
            syncScript = o.str("syncScript"),
            generatorPrompt = o.str("generatorPrompt"),
            cases = cases(o.optJSONArray("cases")),
            documents = documents(o.optJSONObject("documents")),
            areaTours = o.optJSONArray("areaTours").objects().map { t ->
                AreaTour(t.str("area"), t.str("intro"), t.optJSONArray("lessons").strings(), figure(t.optJSONObject("figure")))
            },
            courseOrder = o.optJSONArray("courseOrder").strings(),
        )
    }

    private fun JSONArray?.rows(): List<List<String>> {
        if (this == null) return emptyList()
        return (0 until length()).map { optJSONArray(it).strings() }
    }

    /** A lesson figure (index.html §10.31), or null when the section has none. */
    fun figure(o: JSONObject?): Figure? {
        if (o == null) return null
        val shapes = o.optJSONArray("shapes").objects().map { s ->
            val p = s.optJSONArray("p")
            FigShape(
                k = s.str("k"), x = s.f("x"), y = s.f("y"), w = s.f("w"), h = s.f("h"),
                t = s.str("t"), tone = s.strOrNull("tone") ?: "ink", fill = s.optBoolean("fill", true),
                p = if (p == null) emptyList() else (0 until p.length()).map { p.optDouble(it, 0.0).toFloat() },
                arrow = s.optBoolean("arrow"), dash = s.optBoolean("dash"),
                a = s.strOrNull("a") ?: "start", s = s.optDouble("s", 13.0).toFloat(), b = s.optBoolean("b"),
                r = s.optDouble("r", 5.0).toFloat(),
            )
        }
        return Figure(o.str("title"), o.str("caption"), o.str("alt"), o.f("w"), o.f("h"), shapes)
    }

    /** index.html PMP.DOCUMENTS: { project: { name, summary }, documents: [...] }. */
    fun documents(o: JSONObject?): SampleDocuments {
        if (o == null) return SampleDocuments("", "", emptyList())
        val p = o.optJSONObject("project")
        val list = o.optJSONArray("documents").objects().map { d ->
            val links = d.optJSONObject("links")
            SampleDocument(
                d.str("id"), d.str("title"), d.str("kind"),
                d.str("purpose"), d.str("owner"), d.str("when"), d.str("agile"), d.str("examTip"),
                d.optJSONArray("sections").objects().map { s -> DocSection(s.str("heading"), s.optJSONArray("rows").rows()) },
                d.optJSONObject("table")?.let { t -> DocTable(t.optJSONArray("cols").strings(), t.optJSONArray("rows").rows()) },
                links?.optJSONArray("lessons").strings(), links?.optJSONArray("processes").strings(), links?.optJSONArray("terms").strings(),
            )
        }
        return SampleDocuments(p?.str("name") ?: "", p?.str("summary") ?: "", list)
    }

    /**
     * PMP.CASES. Each item is validated like a pack item and parsed with pack id "case:<id>"; an invalid
     * item is dropped, and a case left with no items is dropped. A missing or empty array gives no cases.
     */
    fun cases(a: JSONArray?): List<CaseStudy> = a.objects().mapNotNull { c ->
        val id = c.str("id")
        if (id.isEmpty()) return@mapNotNull null
        val qs = c.optJSONArray("questions").objects().filter { validate(it) == null }.map { question(it, "case:$id") }
        if (qs.isEmpty()) return@mapNotNull null
        CaseStudy(
            id = id,
            title = c.str("title").ifEmpty { id },
            approach = Approach.of(c.optString("approach")),
            setting = c.str("setting"),
            passage = c.optJSONArray("passage")?.strings() ?: c.str("passage").split("\n\n").filter { it.isNotBlank() },
            exhibits = c.optJSONArray("exhibits").objects().map { x ->
                val rows = x.optJSONArray("rows")
                CaseExhibit(
                    x.str("id"), x.str("label"), x.str("title"), x.optJSONArray("cols").strings(),
                    if (rows == null) emptyList() else (0 until rows.length()).map { rows.optJSONArray(it).strings() },
                    x.str("note"),
                )
            },
            questions = qs,
        )
    }

    /** PMP.RULES (docs/progress-format.md §1). Anything missing keeps the documented default. */
    fun rules(r: JSONObject?): Rules {
        val d = Rules()
        if (r == null) return d
        val mb = r.optJSONObject("mockBands")
        val sc = r.optJSONObject("scenarioCredit")
        val m = r.optJSONObject("mastery")
        val causes = r.optJSONArray("causes").objects().mapNotNull { c ->
            val id = c.str("id")
            if (id.isEmpty()) return@mapNotNull null
            val fallback = d.causes.firstOrNull { it.id == id }
            val short = c.str("short").ifEmpty { fallback?.short ?: id }
            CauseText(id, short, c.str("label").ifEmpty { fallback?.label ?: short }, c.str("hint").ifEmpty { fallback?.hint.orEmpty() })
        }
        val confidence = r.optJSONArray("confidence").objects().mapNotNull { c ->
            val v = c.optInt("v")
            if (v <= 0) null else ConfidenceLevel(v, c.str("id"), c.str("label"))
        }
        return Rules(
            mockBands = if (mb == null) d.mockBands else MockBands(
                mb.optDouble("above", d.mockBands.above), mb.optDouble("target", d.mockBands.target), mb.optDouble("below", d.mockBands.below),
            ),
            scenarioCredit = Rank.entries.associateWith { rank -> sc?.optDouble(rank.id, d.credit(rank))?.takeIf { !it.isNaN() } ?: d.credit(rank) },
            mastery = if (m == null) d.mastery else MasteryRule(
                m.optInt("minAnswers", d.mastery.minAnswers), m.optInt("window", d.mastery.window),
                m.optDouble("accuracy", d.mastery.accuracy), m.optInt("fadeDays", d.mastery.fadeDays),
            ),
            causes = causes.ifEmpty { d.causes },
            reentryDays = r.optInt("reentryDays", d.reentryDays),
            confidence = confidence.ifEmpty { d.confidence },
        )
    }

    private fun scenario(s: JSONObject): Scenario {
        val ranks = s.optJSONArray("ranking").objects().associate { it.str("option") to (Rank.of(it.str("rank")) to it.str("why")) }
        return Scenario(
            id = s.str("id"),
            domain = Domain.of(s.optString("domain")),
            ecoTask = s.str("ecoTask"),
            approach = Approach.of(s.optString("approach")),
            difficulty = s.optInt("difficulty", 3),
            stem = s.str("stem"),
            options = s.optJSONArray("options").objects().map { op ->
                val r = ranks[op.str("id")]
                RankedOption(op.str("id"), op.str("text"), r?.first ?: Rank.WRONG, r?.second.orEmpty())
            },
            mindset = s.str("mindset"),
            explanation = s.str("explanation"),
        )
    }

    private fun reference(r: JSONObject): Reference {
        fun groups(key: String) = r.optJSONArray(key).objects().map {
            RefGroup(it.str("id"), it.optInt("n"), it.str("label"), it.optInt("count"), it.str("blurb"))
        }
        return Reference(
            groups = groups("groups"),
            areas = groups("areas"),
            processes = r.optJSONArray("processes").objects().map {
                RefProcess(
                    it.str("id"), it.str("n"), it.str("name"), it.str("area"), it.str("group"), it.str("purpose"),
                    it.optJSONArray("inputs").strings(), it.optJSONArray("tools").strings(), it.optJSONArray("outputs").strings(), it.str("key"),
                )
            },
            principles = r.optJSONArray("principles").objects().map {
                RefPrinciple(it.optInt("n"), it.str("label"), it.str("line"), it.str("body"), it.optJSONArray("practice").strings(), it.str("trap"))
            },
            domains = r.optJSONArray("domains").objects().map {
                RefPerfDomain(it.optInt("n"), it.str("label"), it.str("line"), it.str("body"), it.optJSONArray("outcomes").strings(), it.optJSONArray("checks").strings())
            },
            agile = r.optJSONArray("agile").objects().map { g ->
                RefAgileGroup(g.str("label"), g.str("blurb"), g.optJSONArray("items").objects().map {
                    RefAgileItem(it.str("name"), it.str("body"), it.optJSONArray("points").strings())
                })
            },
            tables = r.optJSONArray("tables").objects().map { t ->
                val rows = t.optJSONArray("rows")
                RefTable(
                    t.str("label"), t.str("blurb"), t.optJSONArray("cols").strings(),
                    if (rows == null) emptyList() else (0 until rows.length()).map { rows.optJSONArray(it).strings() },
                    t.str("note"),
                )
            },
        )
    }
}
