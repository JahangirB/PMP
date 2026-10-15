import SwiftUI
import UIKit

/**
 * Question text with the exam's highlighter: long-press a word and drag to mark words like NEXT or
 * FIRST; long-press a marked word to clear its run. With the reading guide on, a soft band sits on
 * one line and a tap moves it.
 *
 * Each word is its own Text, laid out in rows, so a touch can be matched to a word. Word indices
 * count the whitespace-separated words of the whole text, as on Android, so saved marks agree.
 */
struct StemText: View {
    private let text: String
    private let baseSize: CGFloat
    private let highlights: Set<Int>?
    private let onHighlights: ((Set<Int>) -> Void)?

    init(_ text: String, baseSize: CGFloat, highlights: Set<Int>? = nil, onHighlights: ((Set<Int>) -> Void)? = nil) {
        self.text = text
        self.baseSize = baseSize
        self.highlights = highlights
        self.onHighlights = onHighlights
    }

    @Environment(\.reading) private var reading
    @State private var frames: [Int: CGRect] = [:]
    @State private var guideLine = 0
    @State private var preview: StemPreview? = nil
    @State private var dragStart = 0

    private static let space = "pmpStemText"
    private var marker: Color { Palette.warning.opacity(0.35) }

    private var marks: Set<Int> { highlights ?? [] }

    /** The marks as they'd be if the current press ended now. */
    private var shown: Set<Int> {
        guard let p = preview else { return marks }
        let r = Set(p.lower...p.upper)
        return p.adding ? marks.union(r) : marks.subtracting(r)
    }

    /** Paragraphs of (global word index, word). */
    private var paragraphs: [[StemWord]] {
        var out: [[StemWord]] = []
        var i = 0
        for line in text.components(separatedBy: "\n") {
            let ws = line.split(whereSeparator: { $0.isWhitespace }).map(String.init)
            if ws.isEmpty { continue }
            var para: [StemWord] = []
            for w in ws { para.append(StemWord(id: i, text: w)); i += 1 }
            out.append(para)
        }
        return out
    }

    /** Rows of text, top to bottom, from where the words landed. */
    private var lines: [StemLine] {
        var out: [StemLine] = []
        for r in frames.values.sorted(by: { $0.minY < $1.minY }) {
            if let l = out.last, abs(r.minY - l.minY) < 2 {
                out[out.count - 1].maxY = max(l.maxY, r.maxY)
            } else {
                out.append(StemLine(minY: r.minY, maxY: r.maxY))
            }
        }
        return out
    }

    var body: some View {
        let size = reading.size(baseSize)
        let gap = reading.spacing(baseSize)
        let on = shown
        VStack(alignment: .leading, spacing: gap + size * 0.45) {
            ForEach(Array(paragraphs.enumerated()), id: \.offset) { _, para in
                StemFlow(hSpacing: size * 0.27, vSpacing: gap) {
                    ForEach(para) { w in
                        Text(w.text)
                            .font(.nunito(size))
                            .foregroundStyle(Palette.onSurface)
                            .background(
                                RoundedRectangle(cornerRadius: 3, style: .continuous)
                                    .fill(on.contains(w.id) ? marker : Color.clear)
                                    .padding(.horizontal, -2)
                            )
                            .background(
                                GeometryReader { g in
                                    Color.clear.preference(key: StemFramesKey.self, value: [w.id: g.frame(in: .named(Self.space))])
                                }
                            )
                    }
                }
            }
        }
        .frame(maxWidth: .infinity, alignment: .leading)
        .background(guideBand)
        .overlay(touchLayer)
        .coordinateSpace(name: Self.space)
        .onPreferenceChange(StemFramesKey.self) { frames = $0 }
        .onChange(of: text) {
            guideLine = 0
            preview = nil
        }
        .accessibilityElement(children: .ignore)
        .accessibilityLabel(text)
    }

    @ViewBuilder private var guideBand: some View {
        if reading.guide {
            GeometryReader { g in
                let ls = lines
                if !ls.isEmpty {
                    let l = ls[min(max(guideLine, 0), ls.count - 1)]
                    let pad: CGFloat = 3
                    RoundedRectangle(cornerRadius: 6, style: .continuous)
                        .fill(Palette.primary.opacity(0.10))
                        .frame(width: g.size.width + 2 * pad, height: l.maxY - l.minY + pad)
                        .position(x: g.size.width / 2, y: (l.minY + l.maxY) / 2)
                        .animation(.easeOut(duration: 0.2), value: guideLine)
                }
            }
        }
    }

    @ViewBuilder private var touchLayer: some View {
        if onHighlights != nil || reading.guide {
            StemTouchView(longPress: onHighlights != nil, tap: reading.guide) { e in handle(e) }
                .accessibilityHidden(true)
        }
    }

    /** The word under (or nearest to) a point, in the text's coordinates. */
    private func wordAt(_ p: CGPoint) -> Int? {
        var best: Int? = nil
        var bestD = CGFloat.infinity
        for (i, r) in frames {
            let dx = max(r.minX - p.x, 0, p.x - r.maxX)
            let dy = max(r.minY - p.y, 0, p.y - r.maxY)
            let d = dy * 1000 + dx
            if d < bestD || (d == bestD && i < (best ?? Int.max)) { bestD = d; best = i }
        }
        return best
    }

    private func lineAt(_ y: CGFloat) -> Int? {
        let ls = lines
        if ls.isEmpty { return nil }
        var best = 0
        var bestD = CGFloat.infinity
        for (i, l) in ls.enumerated() {
            let d = max(l.minY - y, 0, y - l.maxY)
            if d < bestD { bestD = d; best = i }
        }
        return best
    }

    private func handle(_ e: StemTouch) {
        switch e {
        case .tap(let p):
            if reading.guide, let l = lineAt(p.y) { guideLine = l }
        case .began(let p):
            guard onHighlights != nil, let w = wordAt(p) else { return }
            UIImpactFeedbackGenerator(style: .medium).impactOccurred()
            dragStart = w
            if marks.contains(w) {
                // Clear the whole marked run this word belongs to.
                var a = w, b = w
                while marks.contains(a - 1) { a -= 1 }
                while marks.contains(b + 1) { b += 1 }
                preview = StemPreview(lower: a, upper: b, adding: false)
            } else {
                preview = StemPreview(lower: w, upper: w, adding: true)
            }
        case .changed(let p):
            guard let pv = preview, pv.adding, let w = wordAt(p) else { return }
            preview = StemPreview(lower: min(dragStart, w), upper: max(dragStart, w), adding: true)
        case .ended:
            guard preview != nil else { return }
            let result = shown
            preview = nil
            onHighlights?(result)
        case .cancelled:
            preview = nil
        }
    }
}

private struct StemWord: Identifiable { let id: Int; let text: String }
private struct StemLine { var minY: CGFloat; var maxY: CGFloat }
private struct StemPreview: Equatable { let lower: Int; let upper: Int; let adding: Bool }
private enum StemTouch { case began(CGPoint), changed(CGPoint), ended, cancelled, tap(CGPoint) }

private struct StemFramesKey: PreferenceKey {
    static let defaultValue: [Int: CGRect] = [:]
    static func reduce(value: inout [Int: CGRect], nextValue: () -> [Int: CGRect]) {
        value.merge(nextValue()) { _, new in new }
    }
}

/** Words in rows: a space's width between words, the reading line gap between rows. */
private struct StemFlow: Layout {
    var hSpacing: CGFloat
    var vSpacing: CGFloat

    private func measure(_ s: LayoutSubview, _ maxWidth: CGFloat) -> CGSize {
        let size = s.sizeThatFits(.unspecified)
        if size.width > maxWidth && maxWidth.isFinite {
            return s.sizeThatFits(ProposedViewSize(width: maxWidth, height: nil))
        }
        return size
    }

    func sizeThatFits(proposal: ProposedViewSize, subviews: Subviews, cache: inout ()) -> CGSize {
        let width = proposal.width ?? .infinity
        var x: CGFloat = 0, y: CGFloat = 0, row: CGFloat = 0, maxX: CGFloat = 0
        for s in subviews {
            let size = measure(s, width)
            if x > 0 && x + size.width > width { x = 0; y += row + vSpacing; row = 0 }
            x += size.width + hSpacing
            maxX = max(maxX, x - hSpacing)
            row = max(row, size.height)
        }
        return CGSize(width: width.isFinite ? width : maxX, height: y + row)
    }

    func placeSubviews(in bounds: CGRect, proposal: ProposedViewSize, subviews: Subviews, cache: inout ()) {
        var x = bounds.minX, y = bounds.minY, row: CGFloat = 0
        for s in subviews {
            let size = measure(s, bounds.width)
            if x > bounds.minX && x + size.width > bounds.maxX { x = bounds.minX; y += row + vSpacing; row = 0 }
            s.place(at: CGPoint(x: x, y: y), proposal: ProposedViewSize(size))
            x += size.width + hSpacing
            row = max(row, size.height)
        }
    }
}

/**
 * The touch layer over the text. UIKit's long-press recogniser gives the press point and then follows
 * the finger, and lets the surrounding scroll view scroll when the finger moves before the press is held.
 */
private struct StemTouchView: UIViewRepresentable {
    let longPress: Bool
    let tap: Bool
    let onEvent: (StemTouch) -> Void

    func makeCoordinator() -> StemTouchCoordinator { StemTouchCoordinator() }

    func makeUIView(context: Context) -> UIView {
        let v = UIView()
        v.backgroundColor = .clear
        v.isAccessibilityElement = false
        let c = context.coordinator
        let lp = UILongPressGestureRecognizer(target: c, action: #selector(StemTouchCoordinator.longPressed(_:)))
        lp.minimumPressDuration = 0.4
        let t = UITapGestureRecognizer(target: c, action: #selector(StemTouchCoordinator.tapped(_:)))
        v.addGestureRecognizer(lp)
        v.addGestureRecognizer(t)
        c.press = lp
        c.tap = t
        return v
    }

    func updateUIView(_ v: UIView, context: Context) {
        let c = context.coordinator
        c.onEvent = onEvent
        c.press?.isEnabled = longPress
        c.tap?.isEnabled = tap
    }
}

private final class StemTouchCoordinator: NSObject {
    var onEvent: (StemTouch) -> Void = { _ in }
    weak var press: UILongPressGestureRecognizer?
    weak var tap: UITapGestureRecognizer?

    @objc func longPressed(_ g: UILongPressGestureRecognizer) {
        let p = g.location(in: g.view)
        switch g.state {
        case .began: onEvent(.began(p))
        case .changed: onEvent(.changed(p))
        case .ended: onEvent(.ended)
        case .cancelled, .failed: onEvent(.cancelled)
        default: break
        }
    }

    @objc func tapped(_ g: UITapGestureRecognizer) {
        if g.state == .ended { onEvent(.tap(g.location(in: g.view))) }
    }
}
