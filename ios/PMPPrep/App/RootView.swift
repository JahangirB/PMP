import SwiftUI

/** The frame around every screen: the backdrop, the top bar, and the coach bubble. */
struct RootView: View {
    @EnvironmentObject private var m: AppModel
    @Environment(\.openURL) private var openURL

    /** The support reminder (docs/progress-format.md §4) is up. */
    private var supportShowing: Binding<Bool> {
        Binding<Bool>(
            get: { m.supportAsk != .nothing },
            set: { (on: Bool) in if !on { m.dismissSupportAsk() } }
        )
    }

    var body: some View {
        ZStack {
            LinearGradient(colors: Palette.backdrop, startPoint: .top, endPoint: .bottom).ignoresSafeArea()
            if let err = m.loadError {
                VStack(spacing: 12) {
                    Text("The app's content could not be loaded.").font(.nunito(18, .bold))
                    Text(err).font(.nunito(13)).foregroundStyle(Palette.onSurfaceVariant)
                }.padding(24)
            } else if !m.ready {
                ProgressView()
            } else {
                Shell()
            }
        }
        .foregroundStyle(Palette.onSurface)
        .tint(Palette.primary)
        .font(.nunito(15))
        .alert(Support.title, isPresented: supportShowing) {
            Button("Not now", role: .cancel) { m.dismissSupportAsk() }
            Button("Support the app") {
                if let url: URL = URL(string: Support.url) { openURL(url) }
                m.supportPressed()
            }
        } message: {
            Text(m.supportAsk == .first ? Support.firstBody : Support.alreadyBody)
        }
    }
}

private struct Shell: View {
    @EnvironmentObject private var m: AppModel

    private var examRunning: Bool { m.screen == .MOCK && m.mock != nil }
    private var fullScreen: Bool { examRunning || m.screen == .SESSION }
    /** The coach floats over every screen except a mock exam in progress (web §10.33 alike). */
    private var inMock: Bool {
        examRunning ||
        (m.screen == .SESSION && m.run.map { r in r.step < r.session.steps.count && r.session.steps[r.step].kind == .MOCK } == true)
    }

    var body: some View {
        VStack(spacing: 0) {
            if !fullScreen { TopBar() }
            ZStack(alignment: .bottomTrailing) {
                screen
                    .frame(maxWidth: .infinity, maxHeight: .infinity)
                    .id(m.screen)
                    .transition(.opacity.combined(with: .move(edge: .leading)).animation(.easeOut(duration: 0.35)))
                if !inMock { CoachBubble() }
            }
        }
        .environment(\.reading, m.reading)
    }

    @ViewBuilder private var screen: some View {
        switch m.screen {
        case .DASHBOARD: PathScreen()
        case .PRACTICE: PracticeScreen()
        case .MOCK: MockExamScreen()
        case .SITUATIONAL: SituationalScreen()
        case .LEARN: LearnScreen()
        case .FLASHCARDS: FlashcardsScreen()
        case .FORMULAS: FormulaLabScreen()
        case .REFERENCE: ReferenceScreen()
        case .ANALYTICS: AnalyticsScreen()
        case .MISTAKES: MistakeJournalScreen()
        case .PLANNER: PlannerScreen()
        case .DATA: DataScreen()
        case .COACH: CoachScreen()
        case .MENU: MenuScreen()
        case .GUIDE: GuideScreen()
        case .FITNESS: FitnessScreen()
        case .REPORTS: ReportsScreen()
        case .SESSION: SessionScreen()
        case .SETTINGS: StudySettingsScreen()
        case .BOOK: BookScreen()
        case .SEARCH: SearchScreen()
        case .SYNC: SyncScreen()
        case .SUPPORT: SupportScreen()
        case .UPDATES: UpdateScreen()
        case .THEORY: TheoryScreen()
        case .WELCOME: WelcomeScreen()
        }
    }
}

private struct TopBar: View {
    @EnvironmentObject private var m: AppModel
    var body: some View {
        HStack(spacing: 4) {
            if m.canGoBack {
                Button { m.back() } label: { Image(systemName: "chevron.left").font(.system(size: 18, weight: .semibold)).frame(width: 44, height: 44) }
                    .accessibilityLabel("Back")
            } else {
                Spacer().frame(width: 12)
            }
            Text(m.screen.title).font(.nunito(20, .heavy)).lineLimit(1)
            Spacer()
            if m.screen != .MENU {
                Button { m.navigate(.MENU) } label: { Image(systemName: "line.3.horizontal").font(.system(size: 18, weight: .semibold)).frame(width: 44, height: 44) }
                    .accessibilityLabel("Menu")
            }
        }
        .foregroundStyle(Palette.onSurface)
        .padding(.horizontal, 4)
        .padding(.top, 2)
    }
}

/** The coach, a draggable clay bubble carrying a count of what it has to say. Notes live inside it. */
private struct CoachBubble: View {
    @EnvironmentObject private var m: AppModel
    @State private var offset: CGSize = .zero
    @GestureState private var drag: CGSize = .zero

    var body: some View {
        let count = m.coach().report().messages.count
        Button {
            m.coachTab = 0
            m.navigate(.COACH)
        } label: {
            ZStack(alignment: .topTrailing) {
                Image(systemName: "sparkles").font(.system(size: 22, weight: .semibold))
                    .foregroundStyle(Palette.onPrimaryContainer)
                    .frame(width: 58, height: 58)
                    .clay(Palette.primaryContainer, radius: 29)
                if count > 0 {
                    Text("\(count)").font(.nunito(11, .bold)).foregroundStyle(Palette.onStatus)
                        .padding(.horizontal, 6).padding(.vertical, 2).background(Capsule().fill(Palette.error))
                }
            }
        }
        .buttonStyle(LiftStyle())
        .accessibilityLabel(count > 0 ? "Coach, \(count) messages" : "Coach")
        .offset(x: offset.width + drag.width, y: offset.height + drag.height)
        .gesture(DragGesture().updating($drag) { v, s, _ in s = v.translation }.onEnded { v in
            offset.width += v.translation.width; offset.height += v.translation.height
        })
        .padding(20)
    }
}

/** Reading comfort for questions and lessons. */
private struct ReadingKey: EnvironmentKey { static let defaultValue = ReadingPrefs() }
extension EnvironmentValues {
    var reading: ReadingPrefs {
        get { self[ReadingKey.self] }
        set { self[ReadingKey.self] = newValue }
    }
}
