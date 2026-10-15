import WidgetKit
import SwiftUI

/*
 * The home-screen widget: days left, today's progress, readiness, syllabus, exam fitness and the
 * date your pace says you'll be ready. Each tile is tinted from red (needs work) through amber to
 * green; the countdown runs the other way, green far off and red in the final week; the ready-by
 * tile is green on or before the exam, red after. The app saves the figures in the shared App
 * Group; the widget only reads them, and redraws just after midnight when the day rolls over.
 */

struct Entry: TimelineEntry {
    let date: Date
    let stats: WidgetStats?
}

struct Provider: TimelineProvider {
    func placeholder(in context: Context) -> Entry { Entry(date: Date(), stats: sample) }
    func getSnapshot(in context: Context, completion: @escaping (Entry) -> Void) { completion(Entry(date: Date(), stats: WidgetStats.load() ?? sample)) }
    func getTimeline(in context: Context, completion: @escaping (Timeline<Entry>) -> Void) {
        let now = Date()
        let midnight = Calendar.current.date(byAdding: .minute, value: 1, to: Calendar.current.startOfDay(for: now.addingTimeInterval(86_400)))!
        completion(Timeline(entries: [Entry(date: now, stats: WidgetStats.load())], policy: .after(midnight)))
    }
    private var sample: WidgetStats {
        WidgetStats(examDate: Time.now() + 42 * Time.dayMs, day: Time.startOfDay(Time.now()), todayMinutes: 35, targetMinutes: 60,
                    readiness: 68, lessonsDone: 12, lessonsTotal: 52, fitness: 62, readyBy: Time.now() + 30 * Time.dayMs, readyState: WidgetStats.READY_PROJECTED)
    }
}

private struct Tile: Identifiable {
    let id: String
    let value: String
    let label: String
    /** 0..1 sets the tint and fills the bar; nil leaves the tile neutral. */
    let level: Double?
    let link: String
}

private func tiles(_ s: WidgetStats?, _ now: Millis) -> [Tile] {
    let days = s?.daysLeft(now)
    let today = s?.today(now) ?? 0
    let syllabus = s?.syllabus() ?? 0
    let fit = s?.fitness ?? -1
    let track = s?.onTrack()
    let df = DateFormatter(); df.setLocalizedDateFormatFromTemplate("d MMM")
    let readyText: String
    switch s?.readyState {
    case WidgetStats.READY_NOW?: readyText = "Now"
    case WidgetStats.READY_STALLED?: readyText = "Stalled"
    case WidgetStats.READY_PROJECTED?: readyText = df.string(from: Time.date(s!.readyBy))
    default: readyText = "–"
    }
    return [
        Tile(id: "days", value: days.map(String.init) ?? "–", label: "days left", level: s?.daysLevel(now), link: "days"),
        Tile(id: "today", value: "\(Int((today * 100).rounded()))%", label: "today", level: s == nil ? nil : today, link: "today"),
        Tile(id: "ready", value: s.map { "\($0.readiness)" } ?? "–", label: "ready", level: s.map { Double($0.readiness) / 100 }, link: "ready"),
        Tile(id: "syllabus", value: "\(Int((syllabus * 100).rounded()))%", label: "syllabus", level: s == nil ? nil : syllabus, link: "syllabus"),
        Tile(id: "fitness", value: fit < 0 ? "–" : "\(fit)", label: "fitness", level: fit < 0 ? nil : Double(fit) / 100, link: "fitness"),
        Tile(id: "book", value: readyText, label: "ready by", level: track.map { $0 ? 1 : 0 }, link: "book"),
    ]
}

private struct TileView: View {
    let t: Tile
    @Environment(\.colorScheme) private var scheme
    var body: some View {
        let dark = scheme == .dark
        let ink = dark ? Color(white: 0.96) : Color(red: 0.106, green: 0.106, blue: 0.122)
        let bg: Color = t.level.map { rgb(WidgetStats.tint($0, dark: dark)) } ?? (dark ? Color(red: 0.18, green: 0.26, blue: 0.30) : Color(red: 0.855, green: 0.92, blue: 0.945))
        Link(destination: URL(string: "pmpprep://open/\(t.link)")!) {
            VStack(spacing: 3) {
                Text(t.value).font(.system(size: 16, weight: .bold, design: .rounded)).foregroundStyle(ink).lineLimit(1).minimumScaleFactor(0.6)
                if let level = t.level {
                    GeometryReader { g in
                        ZStack(alignment: .leading) {
                            Capsule().fill(dark ? Color.black.opacity(0.35) : Color.white.opacity(0.7))
                            Capsule().fill(rgb(WidgetStats.heat(level))).frame(width: g.size.width * min(max(level, 0), 1))
                        }
                    }.frame(width: 36, height: 4)
                }
                Text(t.label).font(.system(size: 10, weight: .medium, design: .rounded)).foregroundStyle(ink.opacity(0.8)).lineLimit(1)
            }
            .frame(maxWidth: .infinity, maxHeight: .infinity)
            .background(RoundedRectangle(cornerRadius: 16, style: .continuous).fill(bg))
        }
    }
    private func rgb(_ c: (Double, Double, Double)) -> Color { Color(red: c.0 / 255, green: c.1 / 255, blue: c.2 / 255) }
}

struct PMPWidgetView: View {
    let entry: Entry
    @Environment(\.widgetFamily) private var family
    var body: some View {
        let all = tiles(entry.stats, Time.millis(entry.date))
        Group {
            if family == .systemSmall {
                VStack(spacing: 6) { TileView(t: all[0]); TileView(t: all[5]) }
            } else {
                Grid(horizontalSpacing: 6, verticalSpacing: 6) {
                    GridRow { TileView(t: all[0]); TileView(t: all[1]); TileView(t: all[2]) }
                    GridRow { TileView(t: all[3]); TileView(t: all[4]); TileView(t: all[5]) }
                }
            }
        }
        .containerBackground(for: .widget) {
            LinearGradient(colors: [Color(light: 0xDDF0F5, dark: 0x0D171C), Color(light: 0xEEE9F6, dark: 0x141826)], startPoint: .top, endPoint: .bottom)
        }
    }
}

@main
struct PMPPrepWidget: Widget {
    var body: some WidgetConfiguration {
        StaticConfiguration(kind: "PMPPrepWidget", provider: Provider()) { PMPWidgetView(entry: $0) }
            .configurationDisplayName("PMP Prep")
            .description("Days to the exam, progress today, readiness, syllabus, exam fitness and your projected ready date.")
            .supportedFamilies([.systemSmall, .systemMedium])
    }
}

extension Color {
    init(light: UInt32, dark: UInt32) {
        self.init(UIColor { t in
            let h = t.userInterfaceStyle == .dark ? dark : light
            return UIColor(red: CGFloat((h >> 16) & 0xFF) / 255, green: CGFloat((h >> 8) & 0xFF) / 255, blue: CGFloat(h & 0xFF) / 255, alpha: 1)
        })
    }
}
