import SwiftUI

private struct SearchHit {
    let kind: String
    let title: String
    let snippet: String
    let detail: String?
    let go: (() -> Void)?
    var key: String { kind + title + snippet }
}

private let searchMax = 60

/** One search across lessons, glossary, abbreviations, formulas, your notes and the questions' explanations. */
struct SearchScreen: View {
    @EnvironmentObject private var m: AppModel
    @State private var query = ""
    @State private var open: String? = nil
    @FocusState private var focused: Bool

    var body: some View {
        let searching = query.trimmingCharacters(in: .whitespacesAndNewlines).count >= 2
        let hits = searching ? runSearch(m, query) : []
        ScrollView {
            LazyVStack(alignment: .leading, spacing: 10) {
                VStack(alignment: .leading, spacing: 6) {
                    HStack(spacing: 8) {
                        Image(systemName: "magnifyingglass").foregroundStyle(Palette.onSurfaceVariant)
                        TextField("Search lessons, terms, formulas, notes…", text: $query)
                            .font(.nunito(15))
                            .focused($focused)
                            .textInputAutocapitalization(.never)
                            .autocorrectionDisabled()
                            .submitLabel(.search)
                        if !query.isEmpty {
                            Button { query = "" } label: { Image(systemName: "xmark.circle.fill").foregroundStyle(Palette.onSurfaceVariant) }
                                .buttonStyle(.plain)
                                .accessibilityLabel("Clear search")
                        }
                    }
                    .padding(.horizontal, 12)
                    .frame(minHeight: 48)
                    .background(RoundedRectangle(cornerRadius: 12).fill(Palette.surface))
                    .overlay(RoundedRectangle(cornerRadius: 12).strokeBorder(Palette.outline, lineWidth: 1))
                    if searching {
                        Text(hits.isEmpty ? "Nothing found." : (hits.count >= searchMax ? "First \(searchMax) results." : "\(hits.count) found."))
                            .font(.nunito(12)).foregroundStyle(Palette.onSurfaceVariant)
                    }
                }
                .padding(.top, 8)
                ForEach(hits.indices, id: \.self) { i in
                    let h = hits[i]
                    let isOpen = open == h.key
                    PmpCard(onTap: {
                        if let go = h.go { go() } else { open = isOpen ? nil : h.key }
                    }) {
                        Pill(text: h.kind, color: Palette.primary)
                        Text(h.title).font(.nunito(15, .bold)).lineLimit(2).multilineTextAlignment(.leading).padding(.top, 4)
                        if isOpen, let d = h.detail {
                            Text(d).font(.nunito(14)).lineSpacing(4).multilineTextAlignment(.leading)
                        } else {
                            Text(h.snippet).font(.nunito(13)).lineSpacing(3).lineLimit(3).multilineTextAlignment(.leading)
                                .foregroundStyle(Palette.onSurfaceVariant)
                        }
                    }
                }
                Spacer().frame(height: 24)
            }
            .padding(.horizontal, 16)
            .frame(maxWidth: 720)
            .frame(maxWidth: .infinity)
        }
        .scrollDismissesKeyboard(.interactively)
        .onAppear { focused = true }
        .onChange(of: query) { _, _ in open = nil }
    }
}

@MainActor
private func runSearch(_ m: AppModel, _ query: String) -> [SearchHit] {
    let words = query.lowercased().split(whereSeparator: { $0.isWhitespace }).map(String.init)
    guard let first = words.first else { return [] }
    func matches(_ texts: String...) -> Bool {
        let t = texts.joined(separator: " ").lowercased()
        return words.allSatisfy { t.contains($0) }
    }
    func blank(_ s: String) -> Bool { s.trimmingCharacters(in: .whitespacesAndNewlines).isEmpty }
    func snip(_ text: String) -> String {
        let chars = Array(text)
        var i = 0
        if let r = text.range(of: first, options: .caseInsensitive) { i = text.distance(from: text.startIndex, to: r.lowerBound) }
        let from = max(0, min(i - 50, chars.count))
        let to = min(chars.count, from + 160)
        let piece = String(chars[from..<to]).replacingOccurrences(of: "\n", with: " ").trimmingCharacters(in: .whitespaces)
        return (from > 0 ? "…" : "") + piece + (from + 160 < chars.count ? "…" : "")
    }

    let x: Extras = m.repo.extras
    var out: [SearchHit] = []
    for l in x.lessons {
        let sectionText: String = l.sections.map { (sec) -> String in sec.heading + " " + sec.body }.joined(separator: " ")
        let body: String = [sectionText, l.recap.joined(separator: " "), l.traps.joined(separator: " ")].joined(separator: " ")
        if matches(l.title, body) {
            let id = l.id
            out.append(SearchHit(kind: "Lesson", title: l.title, snippet: snip(matches(l.title) ? l.objectives.joined(separator: " ") : body), detail: nil) {
                m.openLesson = id
                m.navigate(.LEARN)
            })
        }
    }
    for g in x.glossary where matches(g.term, g.definition) {
        out.append(SearchHit(kind: "Glossary", title: g.term, snippet: snip(g.definition), detail: g.definition, go: nil))
    }
    for a in x.abbreviations where matches(a.abbr, a.full, a.note) {
        out.append(SearchHit(kind: "Abbreviation", title: "\(a.abbr) · \(a.full)", snippet: snip(blank(a.note) ? a.full : a.note),
                             detail: blank(a.note) ? nil : a.note, go: nil))
    }
    for g in x.formulas {
        for f in g.items where matches(f.name, f.expr, f.reads, f.where, f.trap) {
            let detail = [f.expr, f.where, f.reads, blank(f.trap) ? "" : "Trap: \(f.trap)"].filter { !blank($0) }.joined(separator: "\n\n")
            out.append(SearchHit(kind: "Formula", title: "\(f.name): \(f.expr)", snippet: snip(blank(f.reads) ? f.where : f.reads), detail: detail, go: nil))
        }
    }
    let notepad = m.store.notepad
    if matches(notepad) {
        out.append(SearchHit(kind: "Your notes", title: "Notes page", snippet: snip(notepad), detail: nil) {
            m.coachTab = 2
            m.navigate(.COACH)
        })
    }
    for (id, note) in m.store.notes.sorted(by: { $0.key < $1.key }) {
        guard let q = m.repo.byId[id] else { continue }
        if matches(note, q.stem) {
            out.append(SearchHit(kind: "Your note on a question", title: note.components(separatedBy: "\n").first ?? note, snippet: snip(q.stem), detail: nil) {
                m.startPracticeWith([q], title: "From search")
            })
        }
    }
    for q in m.pool() {
        if out.count >= searchMax { break }
        if matches(q.stem, q.explanation, q.mindset) {
            out.append(SearchHit(kind: "Question", title: String(q.stem.prefix(120)), snippet: snip(blank(q.explanation) ? q.stem : q.explanation), detail: nil) {
                m.startPracticeWith([q], title: "From search")
            })
        }
    }
    return Array(out.prefix(searchMax))
}
