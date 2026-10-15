import SwiftUI
import UIKit

/*
 * One running page of notes, like the web app's notes pane, without its AI tab: the AI lives in
 * the coach. Formatting is light markup the ribbon writes around the selection (**bold**, *italic*,
 * __underline__, ~~strike~~, ==highlight==, bullet and numbered lines). The editor styles it as you
 * type, keeping the markers visible but faint, so the text never shifts under the cursor.
 */

/** The markup: where each marked span is, in UTF-16 offsets (as UITextView and NSString count). */
private enum NotesMarkup {
    struct Span { let mark: String; let content: NSRange; let open: NSRange; let close: NSRange }

    static let marks = ["**", "~~", "__", "==", "*"]
    private static let expressions: [(String, NSRegularExpression)] = marks.compactMap { m in
        let q = NSRegularExpression.escapedPattern(for: m)
        return (try? NSRegularExpression(pattern: "\(q)(?=\\S)(.+?)(?<=\\S)\(q)")).map { (m, $0) }
    }

    static func spans(_ text: String) -> [Span] {
        let ns = text as NSString
        var taken = [Bool](repeating: false, count: ns.length)
        var out: [Span] = []
        for (m, re) in expressions {
            let len = (m as NSString).length
            for hit in re.matches(in: text, range: NSRange(location: 0, length: ns.length)) {
                let r = hit.range
                let end = r.location + r.length
                if (r.location..<end).contains(where: { taken[$0] }) { continue }
                if m == "*" && ((r.location > 0 && ns.character(at: r.location - 1) == 42) || (end < ns.length && ns.character(at: end) == 42)) { continue }
                out.append(Span(mark: m, content: NSRange(location: r.location + len, length: r.length - 2 * len),
                                open: NSRange(location: r.location, length: len), close: NSRange(location: end - len, length: len)))
                for i in r.location..<end { taken[i] = true }
            }
        }
        return out
    }

    static let highlight = UIColor(red: 1, green: 213 / 255, blue: 79 / 255, alpha: 0.4)

    static func uiFont(_ name: String, _ size: CGFloat) -> UIFont {
        UIFontMetrics(forTextStyle: .body).scaledFont(for: UIFont(name: name, size: size) ?? .systemFont(ofSize: size))
    }
    static var baseFont: UIFont { uiFont("NunitoExtraLight-Regular", 16) }
    static var boldFont: UIFont { uiFont("NunitoExtraLight-Bold", 16) }
    static var italicFont: UIFont {
        let f = baseFont
        return f.fontDescriptor.withSymbolicTraits(.traitItalic).map { UIFont(descriptor: $0, size: f.pointSize) } ?? UIFont.italicSystemFont(ofSize: f.pointSize)
    }

    /** Styles the editor's text in place: the markers stay, faint. */
    static func style(_ ts: NSTextStorage) {
        let full = NSRange(location: 0, length: ts.length)
        let faint = UIColor(Palette.onSurfaceVariant).withAlphaComponent(0.45)
        ts.beginEditing()
        ts.setAttributes([.font: baseFont, .foregroundColor: UIColor(Palette.onSurface)], range: full)
        for s in spans(ts.string) {
            switch s.mark {
            case "**": ts.addAttribute(.font, value: boldFont, range: s.content)
            case "*": ts.addAttribute(.font, value: italicFont, range: s.content)
            case "__": ts.addAttribute(.underlineStyle, value: NSUnderlineStyle.single.rawValue, range: s.content)
            case "~~": ts.addAttribute(.strikethroughStyle, value: NSUnderlineStyle.single.rawValue, range: s.content)
            default: ts.addAttribute(.backgroundColor, value: highlight, range: s.content)
            }
            ts.addAttribute(.foregroundColor, value: faint, range: s.open)
            ts.addAttribute(.foregroundColor, value: faint, range: s.close)
        }
        ts.endEditing()
    }

    /** The text as read: styled, with the markers taken out. */
    static func reading(_ text: String) -> AttributedString {
        var a = AttributedString(text)
        let found = spans(text)
        for s in found {
            guard let r = Range(s.content, in: a) else { continue }
            switch s.mark {
            case "**": a[r][AttributeScopes.SwiftUIAttributes.FontAttribute.self] = Font.nunito(16, .bold)
            case "*": a[r][AttributeScopes.SwiftUIAttributes.FontAttribute.self] = Font.nunito(16).italic()
            case "__": a[r][AttributeScopes.SwiftUIAttributes.UnderlineStyleAttribute.self] = Text.LineStyle.single
            case "~~": a[r][AttributeScopes.SwiftUIAttributes.StrikethroughStyleAttribute.self] = Text.LineStyle.single
            default: a[r][AttributeScopes.SwiftUIAttributes.BackgroundColorAttribute.self] = Color(uiColor: highlight)
            }
        }
        // Remove markers from the end backwards, so earlier offsets stay right.
        for m in found.flatMap({ [$0.open, $0.close] }).sorted(by: { $0.location > $1.location }) {
            if let r = Range(m, in: a) { a.removeSubrange(r) }
        }
        return a
    }

    private static func clamp(_ sel: NSRange, _ length: Int) -> (Int, Int) {
        let s = min(max(sel.location, 0), length)
        return (s, min(max(sel.location + sel.length, s), length))
    }

    /** Wraps the selection in [mark], or unwraps it if it already is. */
    static func wrap(_ t: String, _ sel: NSRange, _ mark: String) -> (String, NSRange) {
        let ns = t as NSString
        let (s, e) = clamp(sel, ns.length)
        let ml = (mark as NSString).length
        let inner = ns.substring(with: NSRange(location: s, length: e - s))
        if s >= ml && e + ml <= ns.length && ns.substring(with: NSRange(location: s - ml, length: ml)) == mark && ns.substring(with: NSRange(location: e, length: ml)) == mark {
            return (ns.substring(to: s - ml) + inner + ns.substring(from: e + ml), NSRange(location: s - ml, length: e - s))
        }
        return (ns.substring(to: s) + mark + inner + mark + ns.substring(from: e), NSRange(location: s + ml, length: e - s))
    }

    /** Bullets or numbers the selected lines, or takes them off if every line has one. */
    static func prefixLines(_ t: String, _ sel: NSRange, numbered: Bool) -> (String, NSRange) {
        let ns = t as NSString
        let (selMin, selMax) = clamp(sel, ns.length)
        var start = 0
        if selMin > 0 {
            let from = max(selMin - 1, 0)
            let nl = ns.range(of: "\n", options: .backwards, range: NSRange(location: 0, length: min(from + 1, ns.length)))
            start = nl.location == NSNotFound ? 0 : nl.location + 1
        }
        let after = ns.range(of: "\n", range: NSRange(location: selMax, length: ns.length - selMax))
        let endNl = after.location == NSNotFound ? ns.length : after.location
        let lines = ns.substring(with: NSRange(location: start, length: endNl - start)).components(separatedBy: "\n")
        let bullet = try! NSRegularExpression(pattern: "^(• |\\d+\\. )")
        func marked(_ l: String) -> Bool { bullet.firstMatch(in: l, range: NSRange(location: 0, length: (l as NSString).length)) != nil }
        func bare(_ l: String) -> String { bullet.stringByReplacingMatches(in: l, range: NSRange(location: 0, length: (l as NSString).length), withTemplate: "") }
        let allMarked = lines.allSatisfy(marked)
        let out = lines.enumerated().map { i, l in allMarked ? bare(l) : (numbered ? "\(i + 1). " : "• ") + bare(l) }.joined(separator: "\n")
        let nt = ns.substring(to: start) + out + ns.substring(from: endNl)
        return (nt, NSRange(location: start + (out as NSString).length, length: 0))
    }
}

/** A UITextView, so the ribbon can work on the selection and the markup can be styled as you type. */
private struct NotesEditor: UIViewRepresentable {
    @Binding var text: String
    @Binding var selection: NSRange

    func makeCoordinator() -> Coordinator { Coordinator(self) }

    func makeUIView(context: Context) -> UITextView {
        let v = UITextView()
        v.delegate = context.coordinator
        v.isScrollEnabled = false
        v.backgroundColor = .clear
        v.textContainerInset = UIEdgeInsets(top: 12, left: 8, bottom: 12, right: 8)
        v.adjustsFontForContentSizeCategory = true
        v.setContentCompressionResistancePriority(.defaultLow, for: .horizontal)
        v.accessibilityLabel = "Notes"
        context.coordinator.updating = true
        v.text = text
        NotesMarkup.style(v.textStorage)
        v.typingAttributes = [.font: NotesMarkup.baseFont, .foregroundColor: UIColor(Palette.onSurface)]
        context.coordinator.updating = false
        return v
    }

    func updateUIView(_ v: UITextView, context: Context) {
        context.coordinator.parent = self
        context.coordinator.updating = true
        defer { context.coordinator.updating = false }
        if v.text != text {
            v.text = text
            NotesMarkup.style(v.textStorage)
        }
        let length = (v.text as NSString).length
        if selection.location + selection.length <= length && v.selectedRange != selection { v.selectedRange = selection }
        v.typingAttributes = [.font: NotesMarkup.baseFont, .foregroundColor: UIColor(Palette.onSurface)]
    }

    func sizeThatFits(_ proposal: ProposedViewSize, uiView: UITextView, context: Context) -> CGSize? {
        let w = proposal.width ?? 320
        let fit = uiView.sizeThatFits(CGSize(width: w, height: .greatestFiniteMagnitude))
        return CGSize(width: w, height: max(320, fit.height))
    }

    final class Coordinator: NSObject, UITextViewDelegate {
        var parent: NotesEditor
        var updating = false
        init(_ parent: NotesEditor) { self.parent = parent }

        func textViewDidChange(_ v: UITextView) {
            if v.markedTextRange == nil {
                let sel = v.selectedRange
                NotesMarkup.style(v.textStorage)
                v.selectedRange = sel
                v.typingAttributes = [.font: NotesMarkup.baseFont, .foregroundColor: UIColor(Palette.onSurface)]
            }
            parent.text = v.text
            parent.selection = v.selectedRange
        }

        func textViewDidChangeSelection(_ v: UITextView) {
            if updating { return }
            DispatchQueue.main.async { [weak self, weak v] in
                guard let self, let v, self.parent.selection != v.selectedRange else { return }
                self.parent.selection = v.selectedRange
            }
        }
    }
}

/** The notes page: the editor with its ribbon, a reading view, and notes left on questions. */
struct NotesView: View {
    @EnvironmentObject private var m: AppModel
    @State private var text = ""
    @State private var selection = NSRange(location: 0, length: 0)
    @State private var loaded = false
    @State private var reading = false
    @State private var confirmClear = false

    var body: some View {
        ScreenScroll(spacing: 10) {
            Text("Formulas, mnemonics, traps you fell for, questions to come back to. Saved on this phone as you type.")
                .font(.nunito(13)).foregroundStyle(Palette.onSurfaceVariant).fixedSize(horizontal: false, vertical: true)
            HStack(spacing: 6) {
                Chip(label: "Edit", selected: !reading) { reading = false }
                Chip(label: "Read", selected: reading) { reading = true }
            }
            if !reading {
                ribbon
                NotesEditor(text: $text, selection: $selection)
                    .background(RoundedRectangle(cornerRadius: 12).fill(Palette.surface))
                    .overlay(RoundedRectangle(cornerRadius: 12).strokeBorder(Palette.outline, lineWidth: 1))
                    .overlay(alignment: .topLeading) {
                        if text.isEmpty {
                            Text("Select text and use the ribbon to format it.").font(.nunito(16)).foregroundStyle(Palette.onSurfaceVariant.opacity(0.7))
                                .padding(.horizontal, 13).padding(.vertical, 12).allowsHitTesting(false).accessibilityHidden(true)
                        }
                    }
            } else {
                PmpCard {
                    if text.trimmingCharacters(in: .whitespacesAndNewlines).isEmpty {
                        Text("No notes yet.").foregroundStyle(Palette.onSurfaceVariant)
                    } else {
                        Text(NotesMarkup.reading(text)).font(.nunito(16)).lineSpacing(6).textSelection(.enabled)
                    }
                }
            }
            actions
            questionNotes
            Spacer().frame(height: 16)
        }
        .onAppear {
            if !loaded { text = m.store.notepad; loaded = true }
        }
        .task(id: text) {
            // Saved shortly after typing stops, and when leaving the screen.
            try? await Task.sleep(nanoseconds: 700_000_000)
            if !Task.isCancelled && loaded { m.store.setNotepad(text) }
        }
        .onDisappear { if loaded { m.store.setNotepad(text) } }
        .alert("Clear your notes?", isPresented: $confirmClear) {
            Button("Clear", role: .destructive) { text = ""; selection = NSRange(location: 0, length: 0); m.store.setNotepad("") }
            Button("Cancel", role: .cancel) {}
        } message: {
            Text("This empties the notes page. Notes on questions are kept.")
        }
    }

    private func apply(_ r: (String, NSRange)) { text = r.0; selection = r.1 }

    private var ribbon: some View {
        ScrollView(.horizontal, showsIndicators: false) {
            HStack(spacing: 2) {
                ribbonButton(Text("B").bold(), "Bold") { apply(NotesMarkup.wrap(text, selection, "**")) }
                ribbonButton(Text("I").italic(), "Italic") { apply(NotesMarkup.wrap(text, selection, "*")) }
                ribbonButton(Text("U").underline(), "Underline") { apply(NotesMarkup.wrap(text, selection, "__")) }
                ribbonButton(Text("S").strikethrough(), "Strikethrough") { apply(NotesMarkup.wrap(text, selection, "~~")) }
                ribbonButton(Text("H").padding(.horizontal, 2).background(Color(uiColor: NotesMarkup.highlight)), "Highlight") { apply(NotesMarkup.wrap(text, selection, "==")) }
                ribbonButton(Image(systemName: "list.bullet"), "Bulleted list") { apply(NotesMarkup.prefixLines(text, selection, numbered: false)) }
                ribbonButton(Image(systemName: "list.number"), "Numbered list") { apply(NotesMarkup.prefixLines(text, selection, numbered: true)) }
            }
            .padding(4)
        }
        .background(RoundedRectangle(cornerRadius: 10).fill(Palette.surfaceVariant))
    }

    private func ribbonButton<L: View>(_ label: L, _ name: String, _ action: @escaping () -> Void) -> some View {
        Button(action: action) {
            label.font(.system(size: 17)).foregroundStyle(Palette.primary).frame(minWidth: 44, minHeight: 44)
        }
        .buttonStyle(.plain)
        .accessibilityLabel(name)
    }

    private var actions: some View {
        let blank = text.trimmingCharacters(in: .whitespacesAndNewlines).isEmpty
        let words = text.split(whereSeparator: { $0.isWhitespace }).count
        return HStack(spacing: 12) {
            Text("\(words) words").font(.nunito(12)).foregroundStyle(Palette.onSurfaceVariant)
            Spacer()
            Button("Copy") { UIPasteboard.general.string = text }.textButton().disabled(blank)
            Button("Ask coach") {
                m.store.setNotepad(text)
                m.coachDraft = "Here are my study notes. Check them for anything wrong or missing for the PMP exam, and suggest what to add:\n\n" + String(text.prefix(6000))
                m.coachTab = 1
                m.navigate(.COACH)
            }
            .textButton().disabled(blank)
            Button("Clear") { confirmClear = true }.textButton().disabled(blank)
        }
    }

    @ViewBuilder private var questionNotes: some View {
        let qNotes: [(Question, String)] = m.store.notes.keys.sorted().compactMap { id in
            guard let q = m.repo.byId[id], let n = m.store.notes[id] else { return nil }
            return (q, n)
        }
        if !qNotes.isEmpty {
            SectionLabel("Notes on questions (\(qNotes.count))").padding(.top, 6)
            ForEach(qNotes.indices, id: \.self) { i in
                let q = qNotes[i].0, n = qNotes[i].1
                PmpCard(onTap: { m.startPracticeWith([q], title: "Your noted question") }) {
                    Text(q.stem).font(.nunito(13)).lineLimit(2).foregroundStyle(Palette.onSurfaceVariant).multilineTextAlignment(.leading)
                    Text(n).font(.nunito(14)).multilineTextAlignment(.leading).padding(.top, 4)
                }
            }
        }
    }
}
