import SwiftUI

private let roman = ["", "I", "II", "III", "IV", "V", "VI", "VII", "VIII"]
private let boxLabel = ["exam": "On the exam", "myth": "Common misconception", "example": "The running case"]
private func r(_ n: Int) -> String { n < roman.count ? roman[n] : String(n) }

/** **bold** and *italic*, the inline markup the chapters carry. */
private func inline(_ s: String) -> AttributedString {
    (try? AttributedString(markdown: s, options: .init(interpretedSyntax: .inlineOnlyPreservingWhitespace))) ?? AttributedString(s)
}

/** The flask that marks a feature still being tested (index.html §10.37 .labs). */
struct LabsBadge: View {
    var body: some View {
        HStack(spacing: 4) {
            Image(systemName: "flask").font(.system(size: 11, weight: .semibold))
            Text("Labs").font(.nunito(11, .bold))
        }
        .foregroundStyle(Palette.warning)
        .padding(.horizontal, 8).padding(.vertical, 2)
        .background(Capsule().fill(Palette.warning.opacity(0.14)))
        .accessibilityElement(children: .ignore)
        .accessibilityLabel("Labs: still being tested")
    }
}

/** Menu → Theory: chapters on project management as a discipline (index.html §10.37, TheoryScreen.kt), in Labs. */
struct TheoryScreen: View {
    @EnvironmentObject private var m: AppModel

    var body: some View {
        let t = m.repo.extras.theory
        // Back from a chapter returns to the contents (AppModel.back).
        if let id = m.theoryChapter, let c = t.chapters.first(where: { $0.id == id }) {
            TheoryReader(t: t, c: c).id(c.id)
        } else {
            TheoryContents(t: t)
        }
    }
}

private struct TheoryContents: View {
    @EnvironmentObject private var m: AppModel
    let t: Theory

    var body: some View {
        let place = m.theoryPlace.flatMap { p in t.chapters.first { $0.id == p.ch } }
        ScreenScroll {
            HStack(alignment: .firstTextBaseline) {
                Text("Project management in depth").font(.nunito(22, .heavy)).fixedSize(horizontal: false, vertical: true)
                Spacer(minLength: 8)
                LabsBadge()
            }
            Text("Optional reading for people who want to understand the discipline itself: why the practices exist, where they came from, how they fit together and how experienced project managers think. It goes well past what the exam asks, and every chapter ends with what the exam does ask.")
                .font(.nunito(14)).fixedSize(horizontal: false, vertical: true)
            Text("\(t.chapters.count) of \(t.planned) chapters written · \(t.chapters.filter { m.theoryRead.contains($0.id) }.count) read")
                .font(.nunito(13)).foregroundStyle(Palette.onSurfaceVariant)
            if let place {
                Button { m.openTheory(place.id, resume: true) } label: { Text("Continue: \(place.title)").frame(maxWidth: .infinity) }
                    .primaryButton()
            }
            ForEach(t.parts, id: \.n) { p in
                let mine = t.chapters.filter { $0.part == p.n }
                let first = t.parts.filter { $0.n < p.n }.reduce(0) { $0 + $1.planned.count }
                PmpCard {
                    SectionLabel("Part \(r(p.n))")
                    Text(p.title).font(.nunito(18, .bold))
                    Text(p.blurb).font(.nunito(13)).foregroundStyle(Palette.onSurfaceVariant).fixedSize(horizontal: false, vertical: true)
                    ForEach(Array(p.planned.enumerated()), id: \.offset) { i, title in
                        let c: TheoryChapter? = i < mine.count ? mine[i] : nil
                        Button { if let c { m.openTheory(c.id) } } label: {
                            HStack(alignment: .top, spacing: 8) {
                                Text("\(first + i + 1).").font(.nunito(14)).foregroundStyle(Palette.onSurfaceVariant).frame(width: 26, alignment: .leading)
                                VStack(alignment: .leading, spacing: 2) {
                                    Text(title).font(.nunito(15, .semibold)).foregroundStyle(c == nil ? Palette.onSurfaceVariant : Palette.primary)
                                        .multilineTextAlignment(.leading)
                                    if let c { Text(c.summary).font(.nunito(12)).foregroundStyle(Palette.onSurfaceVariant).multilineTextAlignment(.leading) }
                                }
                                .frame(maxWidth: .infinity, alignment: .leading)
                                if c == nil { Text("Not written yet").font(.nunito(11)).foregroundStyle(Palette.onSurfaceVariant) }
                                else if let c, m.theoryRead.contains(c.id) { Image(systemName: "checkmark.circle.fill").foregroundStyle(Palette.success).accessibilityLabel("Read") }
                                else if let c { Text("\(c.minutes) min").font(.nunito(11)).foregroundStyle(Palette.onSurfaceVariant) }
                            }
                            .padding(.vertical, 6)
                            .contentShape(Rectangle())
                        }
                        .buttonStyle(.plain)
                        .disabled(c == nil)
                    }
                }
            }
            Spacer().frame(height: 16)
        }
    }
}

private struct TheoryReader: View {
    @EnvironmentObject private var m: AppModel
    let t: Theory
    let c: TheoryChapter
    @State private var current: String? = nil

    var body: some View {
        let idx = t.chapters.firstIndex { $0.id == c.id } ?? 0
        let prev = idx > 0 ? t.chapters[idx - 1] : nil
        let next = idx + 1 < t.chapters.count ? t.chapters[idx + 1] : nil
        let order = c.sections.map(\.id)
        let done = Double((current.flatMap { order.firstIndex(of: $0) } ?? -1) + 1) / Double(max(1, order.count))
        ScrollViewReader { proxy in
            VStack(spacing: 0) {
                ProgressView(value: done).tint(Palette.primary)
                HStack {
                    Button("All chapters") { m.theoryChapter = nil }.textButton()
                    Spacer()
                    LabsBadge()
                    Menu {
                        ForEach(c.sections) { s in Button(s.title) { withAnimation { proxy.scrollTo(s.id, anchor: .top) } } }
                        Button("Key ideas") { withAnimation { proxy.scrollTo("key", anchor: .top) } }
                    } label: { Label("Sections", systemImage: "list.bullet").font(.nunito(14, .semibold)) }
                }
                .padding(.horizontal, 16).padding(.vertical, 6)
                ScrollView {
                    LazyVStack(alignment: .leading, spacing: 18) {
                        VStack(alignment: .leading, spacing: 8) {
                            SectionLabel("Part \(r(c.part)) · Chapter \(t.numberOf(c)) · \(c.minutes) min read")
                            Text(c.title).font(.nunito(26, .heavy)).fixedSize(horizontal: false, vertical: true)
                            Text(inline(c.summary)).font(.nunito(16)).foregroundStyle(Palette.onSurfaceVariant).fixedSize(horizontal: false, vertical: true)
                            VStack(alignment: .leading, spacing: 4) {
                                SectionLabel("Why it matters")
                                Text(inline(c.why)).font(.nunito(16)).lineSpacing(5).fixedSize(horizontal: false, vertical: true)
                            }
                            .padding(14)
                            .background(RoundedRectangle(cornerRadius: 12).fill(Palette.primaryContainer.opacity(0.45)))
                        }
                        ForEach(c.sections) { s in
                            VStack(alignment: .leading, spacing: 12) {
                                Text(s.title).font(.nunito(20, .bold)).fixedSize(horizontal: false, vertical: true)
                                ForEach(Array(s.blocks.enumerated()), id: \.offset) { _, b in BlockView(b: b) }
                            }
                            .id(s.id)
                            .onAppear { current = s.id; m.saveTheoryPlace(c.id, s.id) }
                        }
                        PmpCard {
                            Text("Key ideas").font(.nunito(18, .bold))
                            Items(items: c.keyIdeas, numbered: false)
                        }
                        .id("key")
                        // Reaching the key ideas counts as reading the chapter.
                        .onAppear { if !m.theoryRead.contains(c.id) { m.setTheoryRead(c.id, true) } }
                        if !c.reading.isEmpty {
                            VStack(alignment: .leading, spacing: 8) {
                                Text("Further reading").font(.nunito(18, .bold))
                                Items(items: c.reading, numbered: false, size: 14)
                            }
                        }
                        VStack(alignment: .leading, spacing: 10) {
                            let read = m.theoryRead.contains(c.id)
                            Button(read ? "Read ✓" : "Mark as read") { m.setTheoryRead(c.id, !read) }.textButton()
                            if let next {
                                Button { m.openTheory(next.id) } label: { Text("Next: \(next.title)").frame(maxWidth: .infinity) }.primaryButton()
                            }
                            if let prev { Button("Previous: \(prev.title)") { m.openTheory(prev.id) }.textButton() }
                        }
                        Spacer().frame(height: 24)
                    }
                    .padding(.horizontal, 16)
                    .frame(maxWidth: 720)
                    .frame(maxWidth: .infinity)
                }
            }
            .onAppear {
                if let s = m.theoryStart { DispatchQueue.main.async { proxy.scrollTo(s, anchor: .top) } }
            }
        }
    }
}

private struct BlockView: View {
    let b: TheoryBlock
    var body: some View {
        switch b {
        case .para(let s):
            Text(inline(s)).font(.nunito(16)).lineSpacing(5).fixedSize(horizontal: false, vertical: true)
        case .heading(let s):
            Text(s).font(.nunito(16, .bold))
        case .bullets(let items):
            Items(items: items, numbered: false)
        case .numbered(let items):
            Items(items: items, numbered: true)
        case .fig(let f):
            FigureView(fig: f)
        case .box(let kind, let text):
            VStack(alignment: .leading, spacing: 4) {
                SectionLabel(boxLabel[kind] ?? kind)
                Text(inline(text)).font(.nunito(14)).lineSpacing(3).fixedSize(horizontal: false, vertical: true)
            }
            .frame(maxWidth: .infinity, alignment: .leading)
            .padding(14)
            .background(RoundedRectangle(cornerRadius: 12).fill(kind == "exam" ? Palette.primaryContainer.opacity(0.55)
                : kind == "myth" ? Palette.warning.opacity(0.12) : Palette.surface))
        }
    }
}

private struct Items: View {
    let items: [String]
    let numbered: Bool
    var size: CGFloat = 16
    var body: some View {
        VStack(alignment: .leading, spacing: 6) {
            ForEach(Array(items.enumerated()), id: \.offset) { i, s in
                HStack(alignment: .firstTextBaseline, spacing: 6) {
                    Text(numbered ? "\(i + 1)." : "•").font(.nunito(size)).frame(width: 22, alignment: .leading)
                    Text(inline(s)).font(.nunito(size)).lineSpacing(4).fixedSize(horizontal: false, vertical: true)
                }
            }
        }
    }
}
