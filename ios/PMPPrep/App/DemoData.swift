import Foundation

/**
 * Sample progress for the CI screenshots only (launched with -uiDemo 1), so screens show a realistic
 * state rather than an empty one. Never used otherwise.
 */
@MainActor
enum DemoData {
    static func seed(_ m: AppModel) {
        let now = Time.now()
        let day = Time.dayMs
        var plan = m.store.plan
        plan.examDate = Time.startOfDay(now) + 60 * day
        plan.onboarded = true
        m.store.setPlan(plan)
        let qs = Array(m.pool().shuffled().prefix(160))
        for (i, q) in qs.enumerated() {
            let at = now - Millis(30 - i / 6) * day + Millis(i % 6) * 70_000
            let right = (i * 7) % 10 < 7
            m.store.record(Attempt(qid: q.id, ecoTask: q.ecoTask, domain: q.domain, approach: q.approach, type: q.type,
                                   credit: right ? 1 : 0, correct: right, ms: 60_000 + Int64((i * 3_137) % 40_000), at: at, source: "practice",
                                   firstTapMs: 30_000, idleMs: 5_000, words: 140, confidence: (i % 3) + 1))
        }
        m.store.recordMock(MockResult(at: now - 5 * day, title: "Full mock exam", n: 180, right: 121, usedMs: 13_200_000,
                                      domains: [.people: [44, 76], .process: [55, 81], .business: [22, 23]]))
        for l in m.repo.extras.lessons.prefix(8) { m.store.setLessonDone(l.id, true) }
        if let q = qs.first(where: { m.store.last[$0.id]?.correct == false }) { m.store.tagMistake(q.id, .MISREAD_STEM, "") }
    }
}
