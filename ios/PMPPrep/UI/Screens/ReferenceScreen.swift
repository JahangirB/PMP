import SwiftUI

/** ECO tasks, glossary, abbreviations, principles, performance domains, processes, agile and tables. */
struct ReferenceScreen: View {
    @State private var tab = 0
    private let tabs = ["ECO tasks", "Glossary", "Abbreviations", "Principles", "Perf. domains", "Processes", "Agile", "Tables", "Documents"]

    var body: some View {
        VStack(spacing: 0) {
            RefTabStrip(tabs: tabs, selected: $tab)
            SwiftUI.Group {
                switch tab {
                case 0: EcoTasks()
                case 1: Glossary()
                case 2: Abbreviations()
                case 3: Principles()
                case 4: PerfDomains()
                case 5: Processes()
                case 6: Agile()
                case 7: Tables()
                default: Documents()
                }
            }
            .frame(maxWidth: .infinity, maxHeight: .infinity)
        }
    }
}

/** A scrolling row of tabs with an underline under the selected one. */
private struct RefTabStrip: View {
    let tabs: [String]
    @Binding var selected: Int

    var body: some View {
        ScrollViewReader { proxy in
            ScrollView(.horizontal, showsIndicators: false) {
                HStack(spacing: 4) {
                    ForEach(tabs.indices, id: \.self) { i in
                        Button { selected = i } label: {
                            VStack(spacing: 6) {
                                Text(tabs[i]).font(.nunito(14, selected == i ? .bold : .semibold))
                                    .foregroundStyle(selected == i ? Palette.primary : Palette.onSurfaceVariant)
                                Capsule().fill(selected == i ? Palette.primary : .clear).frame(height: 3)
                            }
                            .padding(.horizontal, 10).padding(.top, 10)
                            .fixedSize()
                        }
                        .buttonStyle(.plain)
                        .id(i)
                        .accessibilityAddTraits(selected == i ? .isSelected : [])
                    }
                }
                .padding(.horizontal, 8)
            }
            .onChange(of: selected) { _, i in withAnimation { proxy.scrollTo(i, anchor: .center) } }
        }
        .overlay(alignment: .bottom) { Divider() }
    }
}

private struct RefSearchBox: View {
    @Binding var text: String
    let hint: String

    var body: some View {
        HStack(spacing: 8) {
            Image(systemName: "magnifyingglass").foregroundStyle(Palette.onSurfaceVariant)
            TextField(hint, text: $text)
                .font(.nunito(15))
                .textInputAutocapitalization(.never)
                .autocorrectionDisabled()
                .submitLabel(.search)
            if !text.isEmpty {
                Button { text = "" } label: { Image(systemName: "xmark.circle.fill").foregroundStyle(Palette.onSurfaceVariant) }
                    .buttonStyle(.plain)
                    .accessibilityLabel("Clear search")
            }
        }
        .padding(.horizontal, 12)
        .frame(minHeight: 48)
        .background(RoundedRectangle(cornerRadius: 12).fill(Palette.surface))
        .overlay(RoundedRectangle(cornerRadius: 12).strokeBorder(Palette.outline, lineWidth: 1))
        .padding(.horizontal, 16).padding(.vertical, 8)
    }
}

/** A lazily built list with the standard side padding. */
private struct RefList<Content: View>: View {
    var spacing: CGFloat = 10
    @ViewBuilder var content: () -> Content
    var body: some View {
        ScrollView {
            LazyVStack(alignment: .leading, spacing: spacing) {
                content()
                Spacer().frame(height: 16)
            }
            .padding(.horizontal, 16)
            .frame(maxWidth: 720)
            .frame(maxWidth: .infinity)
        }
        .scrollDismissesKeyboard(.interactively)
    }
}

private func refBlank(_ s: String) -> Bool { s.trimmingCharacters(in: .whitespacesAndNewlines).isEmpty }

private struct EcoTasks: View {
    @EnvironmentObject private var m: AppModel

    var body: some View {
        let x: Extras = m.repo.extras
        let pool = m.pool()
        let byTask = m.readiness.group(m.store.attempts) { $0.ecoTask }
        RefList {
            ForEach(Domain.allCases) { d in
                HStack { DomainChip(domain: d, weight: x.domainWeights[d]); Spacer() }.padding(.top, 8)
                ForEach(x.tasks.filter { $0.domain == d }) { t in
                    let g = byTask[t.id]
                    let n = pool.filter { $0.ecoTask == t.id }.count
                    PmpCard {
                        Text("\(t.code) · \(t.label)").font(.nunito(15, .bold))
                        Text(t.summary).font(.nunito(14)).lineSpacing(4)
                        HStack {
                            Text("\(n) questions" + (g.map { " · you: \(pct($0.accuracy)) over \($0.n)" } ?? " · not practised yet"))
                                .font(.nunito(12)).foregroundStyle(Palette.onSurfaceVariant)
                            Spacer()
                            Button("Practise") {
                                m.startPractice(PracticeFilter(task: t.id, count: 10), title: "Practice: \(t.label)")
                            }
                            .outlinedButton()
                            .disabled(n == 0)
                        }
                        .padding(.top, 6)
                    }
                }
            }
        }
    }
}

private struct Glossary: View {
    @EnvironmentObject private var m: AppModel
    @State private var q = ""

    var body: some View {
        let terms = m.repo.extras.glossary
        let n = q.trimmingCharacters(in: .whitespaces).lowercased()
        let shown = n.isEmpty ? terms
            : terms.filter { $0.term.lowercased().contains(n) } + terms.filter { !$0.term.lowercased().contains(n) && $0.definition.lowercased().contains(n) }
        VStack(spacing: 0) {
            RefSearchBox(text: $q, hint: "Search \(terms.count) terms")
            RefList(spacing: 8) {
                ForEach(Array(shown.enumerated()), id: \.offset) { _, t in
                    PmpCard {
                        HStack(alignment: .firstTextBaseline) {
                            Text(t.term).font(.nunito(15, .bold))
                            Spacer()
                            Text(t.group).font(.nunito(11)).foregroundStyle(Palette.primary)
                        }
                        Text(t.definition).font(.nunito(14)).lineSpacing(4).foregroundStyle(Palette.onSurfaceVariant)
                    }
                }
            }
        }
    }
}

private struct Abbreviations: View {
    @EnvironmentObject private var m: AppModel
    @State private var q = ""

    var body: some View {
        let all = m.repo.extras.abbreviations
        let n = q.trimmingCharacters(in: .whitespaces).lowercased()
        let shown = n.isEmpty ? all : all.filter { $0.abbr.lowercased().contains(n) || $0.full.lowercased().contains(n) }
        VStack(spacing: 0) {
            RefSearchBox(text: $q, hint: "Search \(all.count) abbreviations")
            RefList(spacing: 6) {
                ForEach(Array(shown.enumerated()), id: \.offset) { _, a in
                    HStack(alignment: .firstTextBaseline, spacing: 8) {
                        Text(a.abbr).font(.nunito(15, .heavy)).foregroundStyle(Palette.primary)
                            .frame(width: 76, alignment: .leading)
                        VStack(alignment: .leading, spacing: 2) {
                            Text(a.full).font(.nunito(14))
                            if !refBlank(a.note) {
                                Text(a.note).font(.nunito(12)).foregroundStyle(Palette.onSurfaceVariant)
                            }
                        }
                        Spacer(minLength: 0)
                    }
                    .padding(.vertical, 4)
                    .accessibilityElement(children: .combine)
                }
            }
        }
    }
}

private struct Principles: View {
    @EnvironmentObject private var m: AppModel

    var body: some View {
        RefList {
            SectionLabel("The 12 principles of project management").padding(.top, 8)
            ForEach(Array(m.repo.extras.reference.principles.enumerated()), id: \.offset) { _, p in
                PmpCard {
                    Text("\(p.n). \(p.label)").font(.nunito(16, .bold))
                    Text(p.line).font(.nunito(14, .semibold)).foregroundStyle(Palette.primary)
                    Text(p.body).font(.nunito(14)).lineSpacing(4).padding(.top, 6)
                    if !p.practice.isEmpty { BulletList(items: p.practice).padding(.top, 6) }
                    if !refBlank(p.trap) {
                        Text("Trap: \(p.trap)").font(.nunito(13)).foregroundStyle(Palette.warning).padding(.top, 6)
                    }
                }
            }
        }
    }
}

private struct PerfDomains: View {
    @EnvironmentObject private var m: AppModel

    var body: some View {
        RefList {
            SectionLabel("Project performance domains").padding(.top, 8)
            ForEach(Array(m.repo.extras.reference.domains.enumerated()), id: \.offset) { _, d in
                PmpCard {
                    Text("\(d.n). \(d.label)").font(.nunito(16, .bold))
                    Text(d.line).font(.nunito(14, .semibold)).foregroundStyle(Palette.primary)
                    Text(d.body).font(.nunito(14)).lineSpacing(4).padding(.top, 6)
                    if !d.outcomes.isEmpty {
                        SectionLabel("Outcomes").padding(.top, 6)
                        BulletList(items: d.outcomes)
                    }
                    if !d.checks.isEmpty {
                        SectionLabel("Ask yourself").padding(.top, 6)
                        BulletList(items: d.checks, bullet: "?")
                    }
                }
            }
        }
    }
}

private struct Processes: View {
    @EnvironmentObject private var m: AppModel
    @State private var area: String? = nil
    @State private var open: String? = nil

    var body: some View {
        let ref = m.repo.extras.reference
        let list = ref.processes.filter { area == nil || $0.area == area }
        VStack(spacing: 0) {
            ScrollView(.horizontal, showsIndicators: false) {
                HStack(spacing: 6) {
                    Chip(label: "All \(ref.processes.count)", selected: area == nil) { area = nil }
                    ForEach(ref.areas) { a in
                        Chip(label: a.label, selected: area == a.id) { area = a.id }
                    }
                }
                .padding(.horizontal, 12).padding(.vertical, 8)
            }
            RefList(spacing: 8) {
                ForEach(list) { p in
                    let group = ref.groups.first { $0.id == p.group }?.label ?? ""
                    let isOpen = open == p.id
                    PmpCard(onTap: { withAnimation(.easeOut(duration: 0.2)) { open = isOpen ? nil : p.id } }) {
                        Text("\(p.n) \(p.name)").font(.nunito(15, .bold)).multilineTextAlignment(.leading)
                        Text(group).font(.nunito(12)).foregroundStyle(Palette.primary)
                        Text(p.purpose).font(.nunito(14)).lineSpacing(4).multilineTextAlignment(.leading)
                        if isOpen {
                            SectionLabel("Inputs").padding(.top, 8)
                            BulletList(items: p.inputs)
                            SectionLabel("Tools and techniques").padding(.top, 8)
                            BulletList(items: p.tools)
                            SectionLabel("Outputs").padding(.top, 8)
                            BulletList(items: p.outputs)
                            if !refBlank(p.key) {
                                Text(p.key).font(.nunito(13, .semibold)).foregroundStyle(Palette.warning).padding(.top, 8)
                                    .multilineTextAlignment(.leading)
                            }
                        } else {
                            Text("Tap for inputs, tools and outputs").font(.nunito(11)).foregroundStyle(Palette.onSurfaceVariant)
                        }
                    }
                    .accessibilityHint(isOpen ? "Hides the details" : "Shows inputs, tools and outputs")
                }
            }
        }
    }
}

private struct Agile: View {
    @EnvironmentObject private var m: AppModel

    var body: some View {
        RefList {
            ForEach(Array(m.repo.extras.reference.agile.enumerated()), id: \.offset) { _, g in
                VStack(alignment: .leading, spacing: 2) {
                    SectionLabel(g.label)
                    Text(g.blurb).font(.nunito(13)).foregroundStyle(Palette.onSurfaceVariant)
                }
                .padding(.top, 8)
                ForEach(Array(g.items.enumerated()), id: \.offset) { _, item in
                    PmpCard {
                        Text(item.name).font(.nunito(15, .bold))
                        Text(item.body).font(.nunito(14)).lineSpacing(4)
                        if !item.points.isEmpty { BulletList(items: item.points).padding(.top, 6) }
                    }
                }
            }
        }
    }
}

/* Sample documents (L2): every one filled in for the same running project, so
   the charter, the registers and the change request tell one story. */
private struct Documents: View {
    @EnvironmentObject private var m: AppModel
    @State private var open: String? = nil

    var body: some View {
        let docs: SampleDocuments = m.repo.extras.documents
        RefList(spacing: 8) {
            SectionLabel("The running example: \(docs.projectName)").padding(.top, 8)
            Text(docs.projectSummary).font(.nunito(13)).foregroundStyle(Palette.onSurfaceVariant).lineSpacing(3)
            ForEach(docs.list) { d in
                let isOpen: Bool = open == d.id
                PmpCard(onTap: { withAnimation(.easeOut(duration: 0.2)) { open = isOpen ? nil : d.id } }) {
                    Text(d.title).font(.nunito(15, .bold)).multilineTextAlignment(.leading)
                    Text(d.kind.capitalized).font(.nunito(12)).foregroundStyle(Palette.primary)
                    Text(d.purpose).font(.nunito(14)).lineSpacing(4).multilineTextAlignment(.leading)
                    if isOpen {
                        DocumentBody(d: d)
                    } else {
                        Text("Tap to see it filled in").font(.nunito(11)).foregroundStyle(Palette.onSurfaceVariant)
                    }
                }
                .accessibilityHint(isOpen ? "Hides the sample" : "Shows the sample filled in")
            }
        }
    }
}

private struct DocumentBody: View {
    @EnvironmentObject private var m: AppModel
    let d: SampleDocument

    private func seeAlso() -> [String] {
        var out: [String] = []
        for id in d.lessons {
            if let l = m.repo.extras.lessons.first(where: { $0.id == id }) { out.append("Lesson \(l.id) · \(l.title)") }
        }
        for id in d.processes {
            if let p = m.repo.extras.reference.processes.first(where: { $0.id == id }) { out.append("Process \(p.n) \(p.name)") }
        }
        for t in d.terms { out.append("Glossary: \(t)") }
        return out
    }

    var body: some View {
        VStack(alignment: .leading, spacing: 4) {
            DocFact(label: "Who", text: d.owner)
            DocFact(label: "When", text: d.whenText)
            DocFact(label: "In agile", text: d.agile)
            DocFact(label: "On the exam", text: d.examTip)
            ForEach(d.sections.indices, id: \.self) { si in
                let s: DocSection = d.sections[si]
                if !refBlank(s.heading) { SectionLabel(s.heading).padding(.top, 8) }
                ForEach(s.rows.indices, id: \.self) { ri in
                    let r: [String] = s.rows[ri]
                    VStack(alignment: .leading, spacing: 1) {
                        Text(r.first ?? "").font(.nunito(13, .bold)).foregroundStyle(Palette.primary)
                        Text(r.count > 1 ? r[1] : "").font(.nunito(13)).lineSpacing(3)
                    }
                    .padding(.top, 4)
                }
            }
            if let t = d.table {
                // Rows read as stacked cards: a phone is too narrow for a wide register.
                ForEach(t.rows.indices, id: \.self) { ri in
                    let row: [String] = t.rows[ri]
                    VStack(alignment: .leading, spacing: 2) {
                        ForEach(row.indices, id: \.self) { i in
                            if i == 0 {
                                Text(row[i]).font(.nunito(14, .bold)).foregroundStyle(Palette.primary)
                            } else {
                                Text("\(i < t.cols.count ? t.cols[i] : ""): \(row[i])").font(.nunito(13)).lineSpacing(3)
                            }
                        }
                    }
                    .padding(.top, 8)
                }
            }
            let see: [String] = seeAlso()
            if !see.isEmpty {
                SectionLabel("See also").padding(.top, 8)
                BulletList(items: see)
            }
        }
        .padding(.top, 4)
    }
}

private struct DocFact: View {
    let label: String
    let text: String

    var body: some View {
        if !refBlank(text) {
            Text("\(label): \(text)").font(.nunito(13)).foregroundStyle(Palette.onSurfaceVariant).lineSpacing(3)
        }
    }
}

private struct Tables: View {
    @EnvironmentObject private var m: AppModel

    var body: some View {
        RefList {
            ForEach(Array(m.repo.extras.reference.tables.enumerated()), id: \.offset) { _, t in
                PmpCard {
                    Text(t.label).font(.nunito(16, .bold))
                    if !refBlank(t.blurb) { Text(t.blurb).font(.nunito(13)).foregroundStyle(Palette.onSurfaceVariant) }
                    // Rows read as stacked cards: a phone is too narrow for a four-column table.
                    ForEach(t.rows.indices, id: \.self) { r in
                        let row = t.rows[r]
                        VStack(alignment: .leading, spacing: 2) {
                            ForEach(row.indices, id: \.self) { i in
                                if i == 0 {
                                    Text(row[i]).font(.nunito(14, .bold)).foregroundStyle(Palette.primary)
                                } else {
                                    Text("\(i < t.cols.count ? t.cols[i] : ""): \(row[i])").font(.nunito(13)).lineSpacing(3)
                                }
                            }
                        }
                        .padding(.top, 8)
                    }
                    if !refBlank(t.note) {
                        Text(t.note).font(.nunito(12)).foregroundStyle(Palette.onSurfaceVariant).padding(.top, 8)
                    }
                }
            }
        }
    }
}
