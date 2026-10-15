import SwiftUI
import UIKit

private let qvLetters = Array("ABCDEFGHIJKLMNOP")

private func qvCountWord(_ n: Int) -> String {
    switch n { case 2: return "two"; case 3: return "three"; case 4: return "four"; case 5: return "five"; default: return "\(n)" }
}

/** The domain, task and type, then the question text with its highlighter. */
struct QuestionHeader: View {
    let q: Question
    let taskLabel: String?
    let highlights: Set<Int>?
    let onHighlights: ((Set<Int>) -> Void)?

    init(q: Question, taskLabel: String?, highlights: Set<Int>? = nil, onHighlights: ((Set<Int>) -> Void)? = nil) {
        self.q = q
        self.taskLabel = taskLabel
        self.highlights = highlights
        self.onHighlights = onHighlights
    }

    var body: some View {
        VStack(alignment: .leading, spacing: 10) {
            HStack(spacing: 6) {
                DomainChip(domain: q.domain)
                if let taskLabel { Pill(text: taskLabel, color: Palette.onSurfaceVariant).layoutPriority(-1) }
                Pill(text: q.type.title, color: Palette.primary)
            }
            StemText(q.stem, baseSize: 16, highlights: highlights, onHighlights: onHighlights)
        }
        .frame(maxWidth: .infinity, alignment: .leading)
        .padding(.bottom, 12)
    }
}

/** The answer area of any question type. Set revealed to show what was right and why. */
struct QuestionBody: View {
    let q: Question
    let options: [Option]
    let answer: Answer?
    let revealed: Bool
    let struck: Set<String>
    let onStrike: ((String) -> Void)?
    let onAnswer: (Answer) -> Void

    init(q: Question, options: [Option], answer: Answer?, revealed: Bool, struck: Set<String> = [],
         onStrike: ((String) -> Void)? = nil, onAnswer: @escaping (Answer) -> Void) {
        self.q = q
        self.options = options
        self.answer = answer
        self.revealed = revealed
        self.struck = struck
        self.onStrike = onStrike
        self.onAnswer = onAnswer
    }

    var body: some View {
        let a = answer ?? Answer()
        switch q.base {
        case .single, .multi:
            QVChoices(q: q, options: options, a: a, revealed: revealed, struck: struck, onStrike: onStrike, onAnswer: onAnswer)
        case .matching:
            QVMatching(q: q, options: options, a: a, revealed: revealed, onAnswer: onAnswer)
        case .hotspot:
            QVHotspot(q: q, a: a, revealed: revealed, onAnswer: onAnswer)
        case .fillblank:
            QVBlanks(q: q, a: a, revealed: revealed, onAnswer: onAnswer)
        default:
            EmptyView()
        }
    }
}

private struct QVHint: View {
    let text: String
    var body: some View {
        Text(text).font(.nunito(13, .semibold)).foregroundStyle(Palette.onSurfaceVariant)
            .frame(maxWidth: .infinity, alignment: .leading)
            .padding(.bottom, 8)
    }
}

/* ── single and multiple response ───────────────────────────────────────── */

private struct QVChoices: View {
    let q: Question
    let options: [Option]
    let a: Answer
    let revealed: Bool
    let struck: Set<String>
    let onStrike: ((String) -> Void)?
    let onAnswer: (Answer) -> Void

    var body: some View {
        let multi = q.base == .multi
        VStack(alignment: .leading, spacing: 0) {
            if multi { QVHint(text: "Select \(qvCountWord(q.correctCount)).") }
            VStack(spacing: 10) {
                ForEach(Array(options.enumerated()), id: \.element.id) { i, o in
                    let sel = a.choices.contains(o.id)
                    QVChoiceCard(
                        letter: String(qvLetters[i % qvLetters.count]),
                        text: o.text,
                        selected: sel,
                        correct: o.correct,
                        square: multi,
                        revealed: revealed,
                        rationale: o.rationale,
                        missedNote: multi,
                        struck: struck.contains(o.id),
                        onLongPress: onStrike.map { f in { f(o.id) } }
                    ) {
                        if multi {
                            var n = a
                            if sel { n.choices.remove(o.id) } else { n.choices.insert(o.id) }
                            onAnswer(n)
                        } else {
                            onAnswer(Answer(choices: [o.id]))
                        }
                    }
                }
            }
        }
    }
}

/** One option. Long-press crosses it out (and back), as on the exam's strike-out tool. */
private struct QVChoiceCard: View {
    let letter: String
    let text: String
    let selected: Bool
    let correct: Bool
    let square: Bool
    let revealed: Bool
    let rationale: String
    var missedNote = false
    var struck = false
    var onLongPress: (() -> Void)? = nil
    let onTap: () -> Void

    @Environment(\.reading) private var reading

    private var border: Color {
        if revealed && correct { return Palette.success }
        if revealed && selected { return Palette.error }
        if selected { return Palette.primary }
        return Palette.outline.opacity(0.4)
    }

    private var fill: Color {
        if revealed && correct { return Palette.success.opacity(0.10) }
        if revealed && selected { return Palette.error.opacity(0.10) }
        if selected { return Palette.primaryContainer.opacity(0.45) }
        return Palette.surface
    }

    private var lead: String {
        if correct && selected { return "Correct. " }
        if correct { return missedNote ? "Correct, and you missed it. " : "This was the answer. " }
        if selected { return "Your choice. " }
        return ""
    }

    var body: some View {
        let shape = RoundedRectangle(cornerRadius: 12, style: .continuous)
        let badge = square ? AnyShape(RoundedRectangle(cornerRadius: 6, style: .continuous)) : AnyShape(Circle())
        VStack(alignment: .leading, spacing: 10) {
            HStack(alignment: .top, spacing: 12) {
                Text(letter)
                    .font(.nunito(13, .bold))
                    .foregroundStyle(selected ? Palette.onPrimary : Palette.onSurfaceVariant)
                    .frame(width: 28, height: 28)
                    .background(badge.fill(selected ? Palette.primary : Palette.surfaceVariant))
                Text(text)
                    .font(.nunito(reading.size(15)))
                    .lineSpacing(reading.spacing(15))
                    .strikethrough(struck)
                    .foregroundStyle(Palette.onSurface)
                    .frame(maxWidth: .infinity, alignment: .leading)
                    .fixedSize(horizontal: false, vertical: true)
            }
            .opacity(struck && !revealed ? 0.5 : 1)
            if revealed && !rationale.trimmingCharacters(in: .whitespacesAndNewlines).isEmpty {
                QVRationale(lead: lead, text: rationale, good: correct, bad: selected && !correct)
            }
        }
        .padding(14)
        .frame(maxWidth: .infinity, alignment: .leading)
        .background(shape.fill(fill))
        .overlay(shape.strokeBorder(border, lineWidth: selected || (revealed && correct) ? 2 : 1))
        .contentShape(shape)
        .animation(.easeOut(duration: 0.2), value: selected)
        .animation(.easeOut(duration: 0.2), value: revealed)
        .onTapGesture { if !revealed { onTap() } }
        .onLongPressGesture(minimumDuration: 0.45) {
            guard !revealed, let onLongPress else { return }
            UIImpactFeedbackGenerator(style: .medium).impactOccurred()
            onLongPress()
        }
        .accessibilityElement(children: .combine)
        .accessibilityAddTraits(.isButton)
        .accessibilityAddTraits(selected ? .isSelected : [])
        .accessibilityValue(struck ? "Crossed out" : "")
        .accessibilityAction { if !revealed { onTap() } }
        .accessibilityActions {
            if let onLongPress, !revealed {
                Button(struck ? "Restore option" : "Cross out option") { onLongPress() }
            }
        }
    }
}

/** The why of one option or region. */
private struct QVRationale: View {
    let lead: String
    let text: String
    let good: Bool
    let bad: Bool

    var body: some View {
        let tint = good ? Palette.success : (bad ? Palette.error : Palette.onSurfaceVariant)
        HStack(alignment: .top, spacing: 6) {
            Image(systemName: good ? "checkmark.circle.fill" : "xmark.circle.fill")
                .font(.system(size: 14))
                .foregroundStyle(tint)
                .padding(.top, 1)
                .accessibilityHidden(true)
            (Text(lead).font(.nunito(13, .bold)).foregroundColor(tint) + Text(text).font(.nunito(13)).foregroundColor(Palette.onSurfaceVariant))
                .lineSpacing(3)
                .frame(maxWidth: .infinity, alignment: .leading)
                .fixedSize(horizontal: false, vertical: true)
        }
        .padding(8)
        .frame(maxWidth: .infinity, alignment: .leading)
        .background(RoundedRectangle(cornerRadius: 8, style: .continuous).fill(good ? Palette.success.opacity(0.12) : Palette.surfaceVariant))
    }
}

/* ── matching and drop-down ─────────────────────────────────────────────── */

private struct QVPick: Identifiable { let id: String }

private struct QVMatching: View {
    let q: Question
    let options: [Option]
    let a: Answer
    let revealed: Bool
    let onAnswer: (Answer) -> Void
    @State private var picking: QVPick? = nil

    var body: some View {
        VStack(alignment: .leading, spacing: 0) {
            QVHint(text: q.type == .pulldown ? "Complete each sentence." : "Choose a match for each item.")
            VStack(spacing: 10) {
                ForEach(Array(q.prompts.enumerated()), id: \.element.id) { i, p in
                    prompt(i, p)
                }
                if revealed {
                    ForEach(options.filter { $0.match == nil }) { o in
                        PmpCard {
                            Text("Not used: \(o.text)").font(.nunito(14, .semibold))
                            QVRationale(lead: "", text: o.rationale, good: false, bad: false).padding(.top, 4)
                        }
                    }
                }
            }
        }
        .sheet(item: $picking) { target in
            QVPickSheet(options: options, chosen: a.map[target.id]) { id in
                var n = a
                n.map[target.id] = id
                onAnswer(n)
                picking = nil
            } onCancel: { picking = nil }
        }
    }

    @ViewBuilder private func prompt(_ i: Int, _ p: Prompt) -> some View {
        let chosen = options.first { $0.id == a.map[p.id] }
        let want = Scoring.matchFor(q, p.id)
        let ok = chosen != nil && chosen?.id == want
        PmpCard(container: !revealed ? nil : (ok ? Palette.success.opacity(0.08) : Palette.error.opacity(0.08)),
                border: !revealed ? nil : (ok ? Palette.success : Palette.error)) {
            Text("\(i + 1). \(p.text)").font(.nunito(15)).lineSpacing(4).fixedSize(horizontal: false, vertical: true)
                .padding(.bottom, 4)
            if !revealed {
                Button { picking = QVPick(id: p.id) } label: {
                    HStack {
                        Text(chosen?.text ?? "Choose…")
                            .font(.nunito(14, chosen == nil ? .semibold : .regular))
                            .foregroundStyle(chosen == nil ? Palette.primary : Palette.onSurface)
                            .multilineTextAlignment(.leading)
                            .frame(maxWidth: .infinity, alignment: .leading)
                        Image(systemName: "chevron.down").font(.caption).foregroundStyle(Palette.onSurfaceVariant)
                    }
                    .padding(.horizontal, 12).padding(.vertical, 10)
                    .frame(minHeight: 44)
                    .background(RoundedRectangle(cornerRadius: 10, style: .continuous).fill(Palette.surfaceVariant))
                    .overlay(RoundedRectangle(cornerRadius: 10, style: .continuous)
                        .strokeBorder(chosen == nil ? Palette.primary : Palette.outline, lineWidth: 1))
                    .contentShape(Rectangle())
                }
                .buttonStyle(.plain)
                .accessibilityLabel("Match for item \(i + 1)")
                .accessibilityValue(chosen?.text ?? "Not chosen")
            } else {
                Text((ok ? "✓ " : "✗ ") + "Your answer: " + (chosen?.text ?? "none"))
                    .font(.nunito(14, .semibold)).foregroundStyle(ok ? Palette.success : Palette.error)
                    .fixedSize(horizontal: false, vertical: true)
                let right = q.options.first { $0.id == want }
                if let right {
                    if !ok {
                        Text("Correct: \(right.text)").font(.nunito(14, .semibold)).foregroundStyle(Palette.success)
                            .fixedSize(horizontal: false, vertical: true)
                    }
                    QVRationale(lead: "", text: right.rationale, good: ok, bad: !ok).padding(.top, 4)
                }
            }
        }
    }
}

/** The list of choices for one matching item. */
private struct QVPickSheet: View {
    let options: [Option]
    let chosen: String?
    let onPick: (String) -> Void
    let onCancel: () -> Void

    var body: some View {
        NavigationStack {
            ScrollView {
                VStack(spacing: 8) {
                    ForEach(options) { o in
                        Button { onPick(o.id) } label: {
                            Text(o.text).font(.nunito(14)).foregroundStyle(Palette.onSurface)
                                .multilineTextAlignment(.leading)
                                .frame(maxWidth: .infinity, alignment: .leading)
                                .padding(12)
                                .background(RoundedRectangle(cornerRadius: 10, style: .continuous)
                                    .fill(chosen == o.id ? Palette.primaryContainer : Palette.surfaceVariant))
                                .contentShape(Rectangle())
                        }
                        .buttonStyle(.plain)
                        .accessibilityAddTraits(chosen == o.id ? .isSelected : [])
                    }
                }
                .padding(16)
            }
            .background(Palette.background.ignoresSafeArea())
            .navigationTitle("Choose")
            .navigationBarTitleDisplayMode(.inline)
            .toolbar {
                ToolbarItem(placement: .cancellationAction) { Button("Cancel", action: onCancel) }
            }
        }
        .presentationDetents([.medium, .large])
        .tint(Palette.primary)
    }
}

/* ── hotspot and graphic ────────────────────────────────────────────────── */

private struct QVHotspot: View {
    let q: Question
    let a: Answer
    let revealed: Bool
    let onAnswer: (Answer) -> Void

    private func toggle(_ id: String) {
        if q.multiSelect {
            var n = a
            if n.choices.contains(id) { n.choices.remove(id) } else { n.choices.insert(id) }
            onAnswer(n)
        } else {
            onAnswer(Answer(choices: [id]))
        }
    }

    var body: some View {
        VStack(alignment: .leading, spacing: 0) {
            if let g = q.graphic, !g.title.trimmingCharacters(in: .whitespaces).isEmpty { QVHint(text: g.title) }
            QVHint(text: q.multiSelect ? "Tap the \(qvCountWord(q.correctCount)) correct areas, in the diagram or the list."
                                       : "Tap one area, in the diagram or the list.")
            PmpCard {
                QVDiagram(q: q, selected: a.choices, revealed: revealed, onTap: revealed ? nil : { toggle($0) })
            }
            VStack(spacing: 8) {
                ForEach(q.regions) { r in
                    QVChoiceCard(letter: "\(r.n)", text: r.label, selected: a.choices.contains(r.id), correct: r.correct,
                                 square: q.multiSelect, revealed: revealed, rationale: r.rationale) { toggle(r.id) }
                }
            }
            .padding(.top, 10)
        }
    }
}

/**
 * Draws the regions in their viewBox coordinates with the left-to-right flow arrows the web app
 * draws. Labels are set at a fixed readable size rather than scaled with the diagram.
 */
private struct QVDiagram: View {
    let q: Question
    let selected: Set<String>
    let revealed: Bool
    let onTap: ((String) -> Void)?

    private var box: (x: Double, y: Double, w: Double, h: Double) {
        let vb = q.graphic?.viewBox ?? []
        if vb.count >= 4 && vb[2] > 0 && vb[3] > 0 { return (vb[0], vb[1], vb[2], vb[3]) }
        return (0, 0, 660, 130)
    }

    private func colors(_ r: Region) -> (fill: Color, stroke: Color, plain: Bool) {
        let sel = selected.contains(r.id)
        if revealed && r.correct { return (Palette.success.opacity(0.18), Palette.success, false) }
        if revealed && sel { return (Palette.error.opacity(0.18), Palette.error, false) }
        if sel { return (Palette.primary.opacity(0.16), Palette.primary, false) }
        return (Palette.surface.opacity(0.6), Palette.outline, true)
    }

    var body: some View {
        let vb = box
        let ratio = CGFloat(min(max(vb.w / vb.h, 0.5), 8))
        GeometryReader { g in
            let s = g.size.width / CGFloat(vb.w)
            ZStack(alignment: .topLeading) {
                Canvas { ctx, size in
                    draw(ctx, size: size, s: s, vb: vb)
                }
                ForEach(q.regions) { r in
                    let c = colors(r)
                    let w = CGFloat(r.w) * s
                    let h = CGFloat(r.h) * s
                    ZStack(alignment: .topLeading) {
                        Text(r.label)
                            .font(.nunito(11))
                            .foregroundStyle(Palette.onSurface)
                            .multilineTextAlignment(.center)
                            .minimumScaleFactor(0.7)
                            .padding(.horizontal, 3)
                            .padding(.top, 13)
                            .padding(.bottom, 2)
                            .frame(width: w, height: h)
                        Text("\(r.n)")
                            .font(.system(size: 12, weight: .bold))
                            .foregroundStyle(c.plain ? Palette.primary : c.stroke)
                            .padding(.leading, 4)
                            .padding(.top, 2)
                    }
                    .frame(width: w, height: h, alignment: .topLeading)
                    .clipped()
                    .offset(x: CGFloat(r.x - vb.x) * s, y: CGFloat(r.y - vb.y) * s)
                }
            }
            .frame(width: g.size.width, height: g.size.height, alignment: .topLeading)
            .contentShape(Rectangle())
            .gesture(SpatialTapGesture().onEnded { v in
                let p = v.location
                guard let onTap, s > 0 else { return }
                let x = Double(p.x / s) + vb.x
                let y = Double(p.y / s) + vb.y
                // The smallest region under the finger wins, so nested regions stay reachable.
                let hit = q.regions.filter { x >= $0.x && x <= $0.x + $0.w && y >= $0.y && y <= $0.y + $0.h }
                    .min { $0.w * $0.h < $1.w * $1.h }
                if let hit { onTap(hit.id) }
            })
        }
        .aspectRatio(ratio, contentMode: .fit)
        .frame(maxWidth: .infinity)
        .accessibilityElement(children: .ignore)
        .accessibilityLabel(q.graphic?.title.isEmpty == false ? q.graphic!.title : "Diagram")
        .accessibilityHint("The same areas are listed below the diagram.")
    }

    private func draw(_ ctx: GraphicsContext, size: CGSize, s: CGFloat, vb: (x: Double, y: Double, w: Double, h: Double)) {
        func sx(_ x: Double) -> CGFloat { CGFloat(x - vb.x) * s }
        func sy(_ y: Double) -> CGFloat { CGFloat(y - vb.y) * s }
        if q.regions.isEmpty {
            ctx.fill(Path(CGRect(origin: .zero, size: size)), with: .color(Palette.surfaceVariant))
            return
        }
        if q.graphic?.flow ?? true {
            let ord = q.regions.sorted { $0.x < $1.x }
            if ord.count > 1 {
                for i in 1..<ord.count {
                    let r1 = ord[i - 1], r2 = ord[i]
                    let y = sy(r1.y + r1.h / 2)
                    let x1 = sx(r1.x + r1.w), x2 = sx(r2.x)
                    if x2 - x1 < 6 * s { continue }
                    let head: CGFloat = 5
                    var line = Path()
                    line.move(to: CGPoint(x: x1, y: y))
                    line.addLine(to: CGPoint(x: x2 - head, y: y))
                    ctx.stroke(line, with: .color(Palette.onSurfaceVariant), lineWidth: 1.5)
                    var tip = Path()
                    tip.move(to: CGPoint(x: x2 - head, y: y - head * 0.6))
                    tip.addLine(to: CGPoint(x: x2, y: y))
                    tip.addLine(to: CGPoint(x: x2 - head, y: y + head * 0.6))
                    tip.closeSubpath()
                    ctx.fill(tip, with: .color(Palette.onSurfaceVariant))
                }
            }
        }
        for r in q.regions {
            let c = colors(r)
            let rect = CGRect(x: sx(r.x), y: sy(r.y), width: CGFloat(r.w) * s, height: CGFloat(r.h) * s)
            let path = Path(roundedRect: rect, cornerRadius: 5)
            ctx.fill(path, with: .color(c.fill))
            let strong = selected.contains(r.id) || (revealed && r.correct)
            ctx.stroke(path, with: .color(c.stroke), lineWidth: strong ? 2.5 : 1.2)
        }
    }
}

/* ── fill in the blank ──────────────────────────────────────────────────── */

private struct QVBlanks: View {
    let q: Question
    let a: Answer
    let revealed: Bool
    let onAnswer: (Answer) -> Void
    @FocusState private var focus: String?

    init(q: Question, a: Answer, revealed: Bool, onAnswer: @escaping (Answer) -> Void) {
        self.q = q
        self.a = a
        self.revealed = revealed
        self.onAnswer = onAnswer
    }

    var body: some View {
        VStack(spacing: 10) {
            ForEach(Array(q.blanks.enumerated()), id: \.element.id) { i, b in
                blank(i, b)
            }
        }
    }

    @ViewBuilder private func blank(_ i: Int, _ b: Blank) -> some View {
        let value = a.map[b.id] ?? ""
        let ok = Scoring.blankIsRight(b, value)
        let prefixUnit = b.unit.range(of: "^[$£€]$", options: .regularExpression) != nil
        let isLast = i >= q.blanks.count - 1
        PmpCard(container: !revealed ? nil : (ok ? Palette.success.opacity(0.08) : Palette.error.opacity(0.08)),
                border: !revealed ? nil : (ok ? Palette.success : Palette.error)) {
            Text(b.label).font(.nunito(14, .semibold)).fixedSize(horizontal: false, vertical: true)
                .padding(.bottom, 2)
            HStack(spacing: 6) {
                if prefixUnit { Text(b.unit).font(.nunito(16)).foregroundStyle(Palette.onSurfaceVariant) }
                TextField("", text: Binding(
                    get: { a.map[b.id] ?? "" },
                    set: { v in
                        var n = a
                        n.map[b.id] = v
                        onAnswer(n)
                    }
                ))
                .font(.nunito(16))
                // Numbers can be negative (variances), so the keyboard keeps a minus key and a return key.
                .keyboardType(b.numeric ? .numbersAndPunctuation : .default)
                .textInputAutocapitalization(.never)
                .autocorrectionDisabled()
                .submitLabel(isLast ? .done : .next)
                .focused($focus, equals: b.id)
                .onSubmit { focus = isLast ? nil : q.blanks[i + 1].id }
                .disabled(revealed)
                .accessibilityLabel(b.label)
                if !prefixUnit && !b.unit.isEmpty { Text(b.unit).font(.nunito(16)).foregroundStyle(Palette.onSurfaceVariant) }
            }
            .padding(.horizontal, 12)
            .frame(minHeight: 48)
            .background(RoundedRectangle(cornerRadius: 10, style: .continuous).fill(Palette.surface))
            .overlay(RoundedRectangle(cornerRadius: 10, style: .continuous)
                .strokeBorder(focus == b.id ? Palette.primary : Palette.outline, lineWidth: focus == b.id ? 2 : 1))
            .opacity(revealed ? 0.8 : 1)
            if revealed {
                Text(ok ? "✓ Correct" : "✗ Answer: \(Scoring.blankAnswer(b))")
                    .font(.nunito(14, .bold)).foregroundStyle(ok ? Palette.success : Palette.error)
                    .padding(.top, 6)
                QVRationale(lead: "", text: b.rationale, good: ok, bad: !ok).padding(.top, 4)
            }
        }
    }
}

/* ── after checking ─────────────────────────────────────────────────────── */

/** Right, partly right or not, then the why and the mindset. */
struct Feedback: View {
    let q: Question
    let result: ScoreResult
    let answered: Bool

    init(q: Question, result: ScoreResult, answered: Bool) {
        self.q = q
        self.result = result
        self.answered = answered
    }

    var body: some View {
        let color = result.correct ? Palette.success : (result.partial ? Palette.warning : Palette.error)
        let label: String = {
            if !answered { return "Not answered" }
            if result.correct { return "Correct" }
            if result.partial { return "Partly right (\(pct(result.credit)))" }
            return "Not quite"
        }()
        VStack(alignment: .leading, spacing: 10) {
            Text(label)
                .font(.nunito(17, .bold))
                .foregroundStyle(color)
                .padding(12)
                .frame(maxWidth: .infinity, alignment: .leading)
                .background(RoundedRectangle(cornerRadius: 12, style: .continuous).fill(color.opacity(0.12)))
                .overlay(RoundedRectangle(cornerRadius: 12, style: .continuous).strokeBorder(color, lineWidth: 1))
                .accessibilityAddTraits(.isHeader)
            if !q.explanation.trimmingCharacters(in: .whitespacesAndNewlines).isEmpty {
                PmpCard {
                    SectionLabel("Why")
                    Text(q.explanation).font(.nunito(15)).lineSpacing(5)
                        .fixedSize(horizontal: false, vertical: true)
                        .padding(.top, 2)
                }
            }
            if !q.mindset.trimmingCharacters(in: .whitespacesAndNewlines).isEmpty {
                MindsetCallout(mindset: q.mindset)
            }
        }
    }
}
