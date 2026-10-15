import SwiftUI

/** A clay surface: soft tinted shadow, pastel fill with a light top-left sheen, and a light rim. */
struct ClaySurface: ViewModifier {
    var fill: Color = Palette.surface
    var border: Color? = nil
    var radius: CGFloat = clayRadius
    @Environment(\.colorScheme) private var scheme
    func body(content: Content) -> some View {
        let shape = RoundedRectangle(cornerRadius: radius, style: .continuous)
        content
            .background(
                shape.fill(fill)
                    .overlay(shape.fill(LinearGradient(colors: [.white.opacity(scheme == .dark ? 0.05 : 0.45), .clear], startPoint: .topLeading, endPoint: .center)))
                    .overlay(shape.strokeBorder(border ?? Palette.rim, lineWidth: 2))
            )
            .clipShape(shape)
            .shadow(color: Palette.shade.opacity(scheme == .dark ? 0.45 : 0.14), radius: 10, x: 0, y: 5)
    }
}

extension View {
    func clay(_ fill: Color = Palette.surface, border: Color? = nil, radius: CGFloat = clayRadius) -> some View {
        modifier(ClaySurface(fill: fill, border: border, radius: radius))
    }
}

/** A clay card. Tappable cards lift a little while pressed. */
struct PmpCard<Content: View>: View {
    var container: Color? = nil
    var border: Color? = nil
    var onTap: (() -> Void)? = nil
    @ViewBuilder var content: () -> Content

    var body: some View {
        let inner = VStack(alignment: .leading, spacing: 4) { content() }
            .frame(maxWidth: .infinity, alignment: .leading)
            .padding(.horizontal, 20).padding(.vertical, 18)
            .foregroundStyle(Palette.onSurface)
            .clay(container ?? Palette.surface, border: border)
        if let onTap {
            Button(action: onTap) { inner }.buttonStyle(LiftStyle())
        } else {
            inner
        }
    }
}

/** Lifts and dims slightly while pressed: the clay "press". */
struct LiftStyle: ButtonStyle {
    func makeBody(configuration: Configuration) -> some View {
        configuration.label
            .offset(y: configuration.isPressed ? -2 : 0)
            .brightness(configuration.isPressed ? 0.02 : 0)
            .animation(.easeOut(duration: 0.26), value: configuration.isPressed)
    }
}

/** The filled clay button. */
struct ClayButtonStyle: ButtonStyle {
    var fill: Color = Palette.primary
    var text: Color = Palette.onPrimary
    @Environment(\.isEnabled) private var enabled
    func makeBody(configuration: Configuration) -> some View {
        configuration.label
            .font(.nunito(15, .bold))
            .multilineTextAlignment(.center)
            .padding(.horizontal, 20).padding(.vertical, 12)
            .frame(minHeight: 44)
            .foregroundStyle(enabled ? text : Palette.onSurface.opacity(0.4))
            .background(Capsule().fill(enabled ? fill : Palette.onSurface.opacity(0.12)))
            .shadow(color: enabled ? fill.opacity(configuration.isPressed ? 0.45 : 0.25) : .clear, radius: configuration.isPressed ? 10 : 4, y: 2)
            .offset(y: configuration.isPressed ? -2 : 0)
            .animation(.easeOut(duration: 0.26), value: configuration.isPressed)
    }
}

/** The outlined clay button. */
struct ClayOutlinedStyle: ButtonStyle {
    @Environment(\.isEnabled) private var enabled
    func makeBody(configuration: Configuration) -> some View {
        configuration.label
            .font(.nunito(15, .semibold))
            .multilineTextAlignment(.center)
            .padding(.horizontal, 18).padding(.vertical, 11)
            .frame(minHeight: 44)
            .foregroundStyle(enabled ? Palette.primary : Palette.onSurface.opacity(0.4))
            .background(Capsule().fill(configuration.isPressed ? Palette.primary.opacity(0.08) : .clear))
            .overlay(Capsule().strokeBorder(Palette.outline.opacity(enabled ? 1 : 0.4), lineWidth: 2))
            .offset(y: configuration.isPressed ? -2 : 0)
            .animation(.easeOut(duration: 0.26), value: configuration.isPressed)
    }
}

/** A plain text button in the primary colour. */
struct TextLinkStyle: ButtonStyle {
    var color: Color = Palette.primary
    func makeBody(configuration: Configuration) -> some View {
        configuration.label.font(.nunito(15, .semibold)).foregroundStyle(color).opacity(configuration.isPressed ? 0.6 : 1)
            .padding(.vertical, 8).contentShape(Rectangle())
    }
}

extension View {
    func primaryButton(fill: Color = Palette.primary) -> some View { buttonStyle(ClayButtonStyle(fill: fill)) }
    func outlinedButton() -> some View { buttonStyle(ClayOutlinedStyle()) }
    func textButton(_ color: Color = Palette.primary) -> some View { buttonStyle(TextLinkStyle(color: color)) }
}

struct Pill: View {
    let text: String
    let color: Color
    var filled = false
    var body: some View {
        Text(text).font(.nunito(11, .bold)).lineLimit(1)
            .foregroundStyle(filled ? Palette.onStatus : color)
            .padding(.horizontal, 8).padding(.vertical, 3)
            .background(RoundedRectangle(cornerRadius: 12).fill(filled ? color : color.opacity(0.12)))
            .overlay(RoundedRectangle(cornerRadius: 12).strokeBorder(color.opacity(0.45), lineWidth: 1))
    }
}

struct DomainChip: View {
    let domain: Domain
    var weight: Double? = nil
    var body: some View { Pill(text: weight.map { "\(domain.short) (\(pct($0)))" } ?? domain.short, color: Palette.domain(domain)) }
}

struct SectionLabel: View {
    let text: String
    init(_ text: String) { self.text = text }
    var body: some View {
        Text(text.uppercased()).font(.nunito(12, .bold)).tracking(1).foregroundStyle(Palette.onSurfaceVariant)
    }
}

struct ProgressRow: View {
    let label: String
    let value: Double
    let right: String
    var color: Color = Palette.primary
    var body: some View {
        VStack(spacing: 4) {
            HStack {
                Text(label).font(.nunito(12, .semibold)).lineLimit(1).foregroundStyle(Palette.onSurface)
                Spacer()
                Text(right).font(.system(size: 12, weight: .bold, design: .monospaced)).foregroundStyle(color)
            }
            Bar(value: value, color: color)
        }
    }
}

/** A thin rounded progress bar. */
struct Bar: View {
    let value: Double
    var color: Color = Palette.primary
    var track: Color = Palette.surfaceVariant
    var height: CGFloat = 6
    var body: some View {
        GeometryReader { g in
            ZStack(alignment: .leading) {
                Capsule().fill(track)
                Capsule().fill(color).frame(width: g.size.width * min(max(value, 0), 1))
            }
        }
        .frame(height: height)
        .accessibilityElement().accessibilityValue(pct(value))
    }
}

struct PacingBar: View {
    let secondsSpent: Int64
    let targetSeconds: Int
    var body: some View {
        let ratio = Double(secondsSpent) / Double(max(targetSeconds, 1))
        let color = ratio <= 0.75 ? Palette.success : (ratio <= 1 ? Palette.warning : Palette.error)
        VStack(spacing: 4) {
            HStack(spacing: 4) {
                Image(systemName: "speedometer").font(.caption).foregroundStyle(color)
                Text("Pace: \(secondsSpent)s / \(targetSeconds)s target").font(.system(size: 12, weight: .medium, design: .monospaced)).foregroundStyle(color)
                Spacer()
                if ratio > 1 { Text("Over target").font(.nunito(11, .bold)).foregroundStyle(Palette.error) }
            }
            Bar(value: ratio / 1.5, color: color)
        }
    }
}

struct MindsetCallout: View {
    let mindset: String
    var body: some View {
        if !mindset.trimmingCharacters(in: .whitespaces).isEmpty {
            HStack(alignment: .top, spacing: 10) {
                Image(systemName: "lightbulb.fill").foregroundStyle(Palette.onTertiaryContainer)
                VStack(alignment: .leading, spacing: 2) {
                    Text("MINDSET").font(.nunito(11, .bold)).tracking(0.8).foregroundStyle(Palette.onTertiaryContainer)
                    Text(mindset).font(.nunito(13, .semibold)).italic().foregroundStyle(Palette.onTertiaryContainer)
                }
            }
            .padding(12)
            .frame(maxWidth: .infinity, alignment: .leading)
            .background(RoundedRectangle(cornerRadius: 12).fill(Palette.tertiaryContainer.opacity(0.7)))
            .overlay(RoundedRectangle(cornerRadius: 12).strokeBorder(Palette.tertiary.opacity(0.5), lineWidth: 1))
        }
    }
}

/** A labelled pop-up menu of choices. */
struct Selector<T: Hashable>: View {
    let label: String
    let options: [(T, String)]
    let selected: T
    let onSelect: (T) -> Void
    var body: some View {
        VStack(alignment: .leading, spacing: 4) {
            Text(label).font(.nunito(12, .semibold)).foregroundStyle(Palette.onSurfaceVariant)
            Menu {
                ForEach(options.indices, id: \.self) { i in
                    Button(options[i].1) { onSelect(options[i].0) }
                }
            } label: {
                HStack {
                    Text(options.first { $0.0 == selected }?.1 ?? "").font(.nunito(14)).foregroundStyle(Palette.onSurface).multilineTextAlignment(.leading)
                    Spacer()
                    Image(systemName: "chevron.up.chevron.down").font(.caption).foregroundStyle(Palette.onSurfaceVariant)
                }
                .padding(12)
                .frame(minHeight: 48)
                .background(RoundedRectangle(cornerRadius: 10).fill(Palette.surface))
                .overlay(RoundedRectangle(cornerRadius: 10).strokeBorder(Palette.outline, lineWidth: 1))
            }
        }
    }
}

struct BulletList: View {
    let items: [String]
    var color: Color = Palette.onSurface
    var bullet = "•"
    var body: some View {
        VStack(alignment: .leading, spacing: 4) {
            ForEach(items.indices, id: \.self) { i in
                HStack(alignment: .firstTextBaseline, spacing: 4) {
                    Text(bullet).font(.nunito(14)).foregroundStyle(color)
                    Text(items[i]).font(.nunito(14)).foregroundStyle(color).fixedSize(horizontal: false, vertical: true)
                }
            }
        }
    }
}

struct EmptyNote: View {
    let text: String
    var body: some View { Text(text).font(.nunito(14)).foregroundStyle(Palette.onSurfaceVariant).padding(.vertical, 24) }
}

/** A selectable chip, like Material's FilterChip. */
struct Chip: View {
    let label: String
    let selected: Bool
    let action: () -> Void
    var body: some View {
        Button(action: action) {
            HStack(spacing: 4) {
                if selected { Image(systemName: "checkmark").font(.caption2.weight(.bold)) }
                Text(label).font(.nunito(13, .semibold)).multilineTextAlignment(.leading)
            }
            .padding(.horizontal, 12).padding(.vertical, 8)
            .foregroundStyle(selected ? Palette.onSecondaryContainer : Palette.onSurfaceVariant)
            .background(RoundedRectangle(cornerRadius: 10).fill(selected ? Palette.secondaryContainer : .clear))
            .overlay(RoundedRectangle(cornerRadius: 10).strokeBorder(selected ? .clear : Palette.outline, lineWidth: 1))
        }
        .buttonStyle(.plain)
        .accessibilityAddTraits(selected ? .isSelected : [])
    }
}

/** Wraps its children onto as many rows as they need. */
struct FlowLayout: Layout {
    var spacing: CGFloat = 8
    func sizeThatFits(proposal: ProposedViewSize, subviews: Subviews, cache: inout ()) -> CGSize {
        let width = proposal.width ?? .infinity
        var x: CGFloat = 0, y: CGFloat = 0, row: CGFloat = 0, maxX: CGFloat = 0
        for s in subviews {
            let size = s.sizeThatFits(.unspecified)
            if x > 0 && x + size.width > width { x = 0; y += row + spacing; row = 0 }
            x += size.width + spacing
            maxX = max(maxX, x - spacing)
            row = max(row, size.height)
        }
        return CGSize(width: min(maxX, width), height: y + row)
    }
    func placeSubviews(in bounds: CGRect, proposal: ProposedViewSize, subviews: Subviews, cache: inout ()) {
        var x = bounds.minX, y = bounds.minY, row: CGFloat = 0
        for s in subviews {
            let size = s.sizeThatFits(.unspecified)
            if x > bounds.minX && x + size.width > bounds.maxX { x = bounds.minX; y += row + spacing; row = 0 }
            s.place(at: CGPoint(x: x, y: y), proposal: ProposedViewSize(size))
            x += size.width + spacing
            row = max(row, size.height)
        }
    }
}

/** The light markdown Gemini replies use: headings, lists, code, **bold**, *italic* and `code`. */
struct MarkdownText: View {
    let text: String
    var color: Color = Palette.onSurface

    private enum Block { case para(String), heading(String), item(String, String, Int), code(String) }

    private var blocks: [Block] {
        var out: [Block] = []
        var para: [String] = []
        func flush() { if !para.isEmpty { out.append(.para(para.joined(separator: " "))); para = [] } }
        let lines = text.replacingOccurrences(of: "\r", with: "").components(separatedBy: "\n")
        var i = 0
        while i < lines.count {
            let line = lines[i]
            let t = line.trimmingCharacters(in: .whitespaces)
            if t.hasPrefix("```") {
                flush()
                var code: [String] = []
                i += 1
                while i < lines.count && !lines[i].trimmingCharacters(in: .whitespaces).hasPrefix("```") { code.append(lines[i]); i += 1 }
                out.append(.code(code.joined(separator: "\n")))
            } else if t.isEmpty { flush() }
            else if t.hasPrefix("#") { flush(); out.append(.heading(t.trimmingCharacters(in: CharacterSet(charactersIn: "# ")))) }
            else if t.range(of: "^([-*•])\\s+", options: .regularExpression) != nil || t.range(of: "^\\d+[.)]\\s+", options: .regularExpression) != nil {
                flush()
                let marker = t.first!.isNumber ? String(t.prefix { $0 != " " }) : "•"
                let rest = t.drop { $0 != " " }.trimmingCharacters(in: .whitespaces)
                out.append(.item(marker, rest, line.count - line.drop { $0 == " " }.count))
            } else { para.append(t) }
            i += 1
        }
        flush()
        return out
    }

    private func inline(_ s: String) -> Text {
        if let a = try? AttributedString(markdown: s, options: .init(interpretedSyntax: .inlineOnlyPreservingWhitespace)) { return Text(a) }
        return Text(s)
    }

    var body: some View {
        VStack(alignment: .leading, spacing: 6) {
            ForEach(Array(blocks.enumerated()), id: \.offset) { _, b in
                switch b {
                case .para(let t): inline(t).font(.nunito(15)).foregroundStyle(color)
                case .heading(let t): inline(t).font(.nunito(16, .bold)).foregroundStyle(color)
                case .code(let t): Text(t).font(.system(size: 13, design: .monospaced)).foregroundStyle(color)
                    .padding(8).frame(maxWidth: .infinity, alignment: .leading).background(RoundedRectangle(cornerRadius: 8).fill(Palette.surfaceVariant))
                case .item(let m, let t, let indent):
                    HStack(alignment: .firstTextBaseline, spacing: 4) {
                        Text(m).font(.nunito(15)).foregroundStyle(Palette.primary).frame(width: m == "•" ? 14 : 24, alignment: .leading)
                        inline(t).font(.nunito(15)).foregroundStyle(color)
                    }.padding(.leading, CGFloat(min(indent, 8) * 4))
                }
            }
        }
        .textSelection(.enabled)
    }
}

/** A screen's title and its one-line explanation. */
struct ScreenTitle: View {
    let title: String
    var subtitle: String? = nil
    var body: some View {
        VStack(alignment: .leading, spacing: 4) {
            Text(title).font(.nunito(22, .heavy))
            if let subtitle { Text(subtitle).font(.nunito(13)).foregroundStyle(Palette.onSurfaceVariant) }
        }
        .frame(maxWidth: .infinity, alignment: .leading)
    }
}

/** A scrolling screen with the standard padding and spacing. */
struct ScreenScroll<Content: View>: View {
    var spacing: CGFloat = 12
    @ViewBuilder var content: () -> Content
    var body: some View {
        ScrollView {
            VStack(alignment: .leading, spacing: spacing) { content() }
                .padding(16)
                .frame(maxWidth: 720)
                .frame(maxWidth: .infinity)
        }
        .scrollDismissesKeyboard(.interactively)
    }
}

/** A bar pinned to the bottom of a screen, above the keyboard and the home indicator. */
struct BottomBar<Content: View>: View {
    @ViewBuilder var content: () -> Content
    var body: some View {
        VStack(spacing: 0) {
            Divider()
            HStack(spacing: 8) { content() }.padding(12).frame(maxWidth: 720)
        }
        .frame(maxWidth: .infinity)
        .background(Palette.surface.ignoresSafeArea(edges: .bottom))
    }
}
