import SwiftUI

/**
 * The case passage shown above each of a case's items: a "Case: <title>" header that folds the passage
 * and exhibits away. It opens expanded. Callers give it .id(case id), so the open or closed state is kept
 * while the reader moves between that case's items and starts fresh for the next case. Mirrors Android's CasePanel.
 */
struct CasePanel: View {
    let study: CaseStudy
    var position: String? = nil
    @State private var open: Bool = true

    var body: some View {
        PmpCard {
            Button(action: { open.toggle() }) {
                HStack(alignment: .center, spacing: 8) {
                    VStack(alignment: .leading, spacing: 2) {
                        Text("Case: " + study.title).font(.nunito(15, .bold))
                            .fixedSize(horizontal: false, vertical: true)
                        if let p = position {
                            Text(p).font(.nunito(11)).foregroundStyle(Palette.onSurfaceVariant)
                        }
                    }
                    Spacer(minLength: 0)
                    Image(systemName: open ? "chevron.up" : "chevron.down")
                        .foregroundStyle(Palette.onSurfaceVariant)
                }
                .contentShape(Rectangle())
            }
            .buttonStyle(.plain)
            .accessibilityLabel((open ? "Hide" : "Show") + " the case: " + study.title)
            if open {
                VStack(alignment: .leading, spacing: 8) {
                    ForEach(Array(study.passage.enumerated()), id: \.offset) { (pair: (offset: Int, element: String)) in
                        Text(pair.element).font(.nunito(14)).lineSpacing(3)
                            .fixedSize(horizontal: false, vertical: true)
                    }
                    ForEach(study.exhibits, id: \.id) { (x: CaseExhibit) in
                        CaseExhibitTable(exhibit: x)
                    }
                }
                .padding(.top, 8)
            }
        }
        .padding(.bottom, 12)
    }
}

private struct CaseExhibitTable: View {
    let exhibit: CaseExhibit

    private var heading: String {
        let parts: [String] = [exhibit.label, exhibit.title].filter { (s: String) -> Bool in !s.isEmpty }
        return parts.joined(separator: ": ")
    }

    private var cellCount: Int { max(exhibit.cols.count, 1) }

    private func cell(_ row: [String], _ i: Int) -> String {
        return i < row.count ? row[i] : ""
    }

    var body: some View {
        VStack(alignment: .leading, spacing: 4) {
            Text(heading).font(.nunito(13, .semibold))
                .fixedSize(horizontal: false, vertical: true)
            HStack(alignment: .top, spacing: 6) {
                ForEach(0..<cellCount, id: \.self) { (i: Int) in
                    Text(cell(exhibit.cols, i)).font(.nunito(12, .bold))
                        .frame(maxWidth: .infinity, alignment: .leading)
                        .fixedSize(horizontal: false, vertical: true)
                }
            }
            Divider()
            ForEach(Array(exhibit.rows.enumerated()), id: \.offset) { (pair: (offset: Int, element: [String])) in
                HStack(alignment: .top, spacing: 6) {
                    ForEach(0..<cellCount, id: \.self) { (i: Int) in
                        Text(cell(pair.element, i)).font(.nunito(12))
                            .frame(maxWidth: .infinity, alignment: .leading)
                            .fixedSize(horizontal: false, vertical: true)
                    }
                }
            }
            if !exhibit.note.isEmpty {
                Text(exhibit.note).font(.nunito(11)).foregroundStyle(Palette.onSurfaceVariant)
                    .fixedSize(horizontal: false, vertical: true)
            }
        }
        .padding(.top, 4)
    }
}
