import XCTest
@testable import PMPPrep

/** Study order (index.html §10.24, tests/24-lectures, CourseOrderTest.kt): by knowledge area, new lessons come in the classroom order. */
final class CourseOrderTests: XCTestCase {
    private let D: Millis = 86_400_000
    private let now: Millis = 1_803_978_000_000

    private func builds(_ x: Extras, byArea: Bool) -> [String] {
        let diag: DiagnosticResult = DiagnosticResult(at: 0, n: 25, right: 13, domains: [.people: [6, 10], .process: [5, 10], .business: [5, 9]])
        let settings: PlanSettings = PlanSettings(examDate: now + 90 * D, hoursPerWeek: 10, studyDays: [1, 2, 3, 4, 5, 6, 7], byArea: byArea)
        let plan: Plan = PlanEngine.build(PlanInput(x: x, attempts: [], lessonsDone: [], results: [], mocks: [], diagnostic: diag,
                                                    settings: settings, now: now))
        return plan.sessions.filter { $0.kind == .BUILD }.compactMap { $0.lessonId }
    }

    func testClassroomOrder() throws {
        let repo = ContentRepository(bundle: .main)
        try repo.load()
        let x: Extras = repo.extras
        let course: [String] = builds(x, byArea: true)
        let idx: [Int] = course.map { x.courseOrder.firstIndex(of: $0) ?? -1 }
        XCTAssertGreaterThanOrEqual(course.count, 10)
        XCTAssertEqual(idx, idx.sorted())
        XCTAssertNotEqual(builds(x, byArea: false), course)
        XCTAssertEqual(x.areaTours.count, 10)
        XCTAssertEqual(Set(x.courseOrder), Set(x.lessons.map { $0.id }))
    }

    /** A finished "lesson:" session counts its lesson as read even if the lesson set lost it (sync). */
    func testFinishedSessionsCountTheirLessons() throws {
        let repo = ContentRepository(bundle: .main)
        try repo.load()
        let x: Extras = repo.extras
        let f0: String = x.lessons.first { $0.module == "foundations" }!.id
        let diag: DiagnosticResult = DiagnosticResult(at: 0, n: 25, right: 13, domains: [.people: [6, 10], .process: [5, 10], .business: [5, 9]])
        let settings: PlanSettings = PlanSettings(examDate: now + 90 * D, hoursPerWeek: 10, studyDays: [1, 2, 3, 4, 5, 6, 7])
        func keys(_ results: [SessionResult]) -> [String] {
            PlanEngine.build(PlanInput(x: x, attempts: [], lessonsDone: [], results: results, mocks: [], diagnostic: diag,
                                       settings: settings, now: now)).sessions.map { $0.key }
        }
        XCTAssertTrue(keys([]).contains("lesson:\(f0)"))
        let done = SessionResult(key: "lesson:\(f0)", kind: .FOUNDATION, taskId: nil, domain: nil, score: 1, minutes: 20, at: now - D)
        XCTAssertFalse(keys([done]).contains("lesson:\(f0)"))
    }
}
