package com.jahangirb.pmpprep

import com.jahangirb.pmpprep.data.Approach
import com.jahangirb.pmpprep.data.Attempt
import com.jahangirb.pmpprep.data.ContentParser
import com.jahangirb.pmpprep.data.Domain
import com.jahangirb.pmpprep.data.QType
import com.jahangirb.pmpprep.data.Question
import com.jahangirb.pmpprep.data.QuestionPicker
import com.jahangirb.pmpprep.data.Readiness
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test
import java.io.File
import kotlin.random.Random

/** Fixes 1 and 3 from the assessment review: domain-weighted accuracy and the weak-approach tilt. */
class GapsTest {
    private val dir = File(System.getProperty("pmp.assets")!!)
    private val x = ContentParser.extras(File(dir, "content/extras.json").readText())
    private val pool: List<Question> = ContentParser.parsePack(File(dir, "content/core.json").readText(), "core", null, null, true).questions
    private val now = 1_800_000_000_000L

    private fun answers(d: Domain, n: Int, score: Double, approach: Approach? = null) = (0 until n).map { i ->
        val right = i < Math.round(n * score)
        Attempt("$d-$approach-$i", "t", d, approach, QType.SINGLE, if (right) 1.0 else 0.0, right, 60_000, now - i * 1000L, "practice")
    }

    @Test fun aWeakDomainPullsAccuracyDownAsTheExamWeighsIt() {
        val atts = answers(Domain.PEOPLE, 60, 0.9) + answers(Domain.PROCESS, 30, 0.5) + answers(Domain.BUSINESS, 60, 0.9)
        val overall = Readiness(x.readiness, x.exam, x.tasks).weightedAccuracy(atts, now).first
        val (weighted, _, byDomain) = Readiness(x.readiness, x.exam, x.tasks, x.domainWeights).weightedAccuracy(atts, now)
        assertTrue("Process at 50% (41% of the exam) shows: $weighted < $overall", weighted < overall - 0.03)
        assertEquals(3, byDomain.size)
    }

    @Test fun theWeakestApproachIsFoundOnlyWhenClearlyBehind() {
        val even = answers(Domain.PEOPLE, 20, 0.8, Approach.PREDICTIVE) + answers(Domain.PEOPLE, 20, 0.75, Approach.AGILE)
        assertNull(QuestionPicker.weakApproach(even))
        val agileWeak = answers(Domain.PEOPLE, 20, 0.9, Approach.PREDICTIVE) + answers(Domain.PEOPLE, 20, 0.4, Approach.AGILE)
        assertEquals(Approach.AGILE, QuestionPicker.weakApproach(agileWeak))
    }

    @Test fun practiceLeansTowardTheWeakApproachWithoutExcludingOthers() {
        val qs = pool.filter { it.approach != null }.take(60)
        val picked = QuestionPicker.order(qs, { null }, Approach.AGILE, 20, Random(1))
        val agile = picked.count { it.approach == Approach.AGILE }
        val share = qs.count { it.approach == Approach.AGILE }.toDouble() / qs.size
        assertTrue("agile gets at least its normal share and up to half ($agile of 20, normal ${"%.0f".format(share * 100)}%)", agile >= Math.round(share * 20) && agile <= 10 || share > 0.5)
        assertTrue("other approaches still appear", picked.any { it.approach != Approach.AGILE })
    }

    @Test fun unseenComeBeforeMissedBeforeRight() {
        val qs = pool.take(9)
        val last = mapOf(qs[0].id to true, qs[1].id to false, qs[2].id to true)
        val picked = QuestionPicker.order(qs, { last[it] }, null, 9, Random(2))
        assertTrue(picked.take(6).none { it.id in last })
        assertEquals(qs[1].id, picked[6].id)
    }
}
