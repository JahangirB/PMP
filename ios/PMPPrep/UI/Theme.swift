import SwiftUI
import UIKit

/*
 * Sky Clay, as on Android: pastel sky blue and soft lilac, measured for contrast in both themes.
 * Every colour adapts to light and dark on its own, so screens never pick by theme.
 */
extension Color {
    init(hex: UInt32) {
        self.init(.sRGB, red: Double((hex >> 16) & 0xFF) / 255, green: Double((hex >> 8) & 0xFF) / 255, blue: Double(hex & 0xFF) / 255, opacity: 1)
    }
    /** One colour for light mode, another for dark. */
    init(light: UInt32, dark: UInt32) {
        self.init(UIColor { t in
            let h = t.userInterfaceStyle == .dark ? dark : light
            return UIColor(red: CGFloat((h >> 16) & 0xFF) / 255, green: CGFloat((h >> 8) & 0xFF) / 255, blue: CGFloat(h & 0xFF) / 255, alpha: 1)
        })
    }
}

enum Palette {
    static let primary = Color(light: 0x0B6684, dark: 0x4CC7F0)
    static let onPrimary = Color(light: 0xFFFFFF, dark: 0x0A1F29)
    static let primaryContainer = Color(light: 0xC8EDF9, dark: 0x185062)
    static let onPrimaryContainer = Color(light: 0x072F40, dark: 0xD1F0FA)
    static let secondary = Color(light: 0x395460, dark: 0xA6C1C9)
    static let secondaryContainer = Color(light: 0xDAEBF1, dark: 0x2E434C)
    static let onSecondaryContainer = Color(light: 0x12232B, dark: 0xEAF3F6)
    static let tertiary = Color(light: 0xA04A08, dark: 0xFBBF24)
    static let tertiaryContainer = Color(light: 0xFDE9D3, dark: 0x5A3A0C)
    static let onTertiaryContainer = Color(light: 0x5A2604, dark: 0xFEF3C7)
    static let background = Color(light: 0xE8F4F7, dark: 0x0D171C)
    static let surface = Color(light: 0xF7FBFD, dark: 0x152228)
    static let onSurface = Color(light: 0x12232B, dark: 0xEEF4F6)
    static let surfaceVariant = Color(light: 0xDEEDF2, dark: 0x223239)
    static let onSurfaceVariant = Color(light: 0x395460, dark: 0xABBEC4)
    static let outline = Color(light: 0xB3D1DB, dark: 0x3D535C)

    static let success = Color(light: 0x047857, dark: 0x34D399)
    static let warning = Color(light: 0xA04A08, dark: 0xFBBF24)
    static let error = Color(light: 0xB91C1C, dark: 0xF87171)
    static let info = Color(light: 0x1D4ED8, dark: 0x60A5FA)
    static let onStatus = Color(light: 0xFFFFFF, dark: 0x0A1F29)
    static let people = Color(light: 0x4F46E5, dark: 0xA5B4FC)
    static let process = Color(light: 0x0369A1, dark: 0x38BDF8)
    static let business = Color(light: 0xA04A08, dark: 0xFBBF24)

    /** The app's backdrop, top to bottom: sky to lilac. */
    static let backdrop = [Color(light: 0xDDF0F5, dark: 0x0D171C), Color(light: 0xE8F4F7, dark: 0x101B21), Color(light: 0xEEE9F6, dark: 0x141826)]
    /** The light rim and tinted shadow of a clay surface. */
    static let rim = Color(UIColor { $0.userInterfaceStyle == .dark ? UIColor(white: 1, alpha: 0.07) : UIColor(white: 1, alpha: 0.9) })
    static let shade = Color(UIColor { $0.userInterfaceStyle == .dark ? .black : UIColor(red: 11 / 255, green: 102 / 255, blue: 132 / 255, alpha: 1) })

    static func domain(_ d: Domain?) -> Color {
        switch d { case .people?: people; case .process?: process; case .business?: business; case nil: .gray }
    }
}

/** Nunito, bundled: rounded and friendly, readable at small sizes. Sizes follow Dynamic Type. */
extension Font {
    static func nunito(_ size: CGFloat, _ weight: Font.Weight = .regular) -> Font {
        let name: String
        switch weight {
        case .black, .heavy: name = "NunitoExtraLight-ExtraBold"
        case .bold: name = "NunitoExtraLight-Bold"
        case .semibold, .medium: name = "NunitoExtraLight-SemiBold"
        default: name = "NunitoExtraLight-Regular"
        }
        return .custom(name, size: size, relativeTo: size >= 20 ? .title2 : (size >= 15 ? .body : .footnote))
    }
}

let clayRadius: CGFloat = 32

func pct(_ v: Double) -> String { "\(Int((v * 100).rounded()))%" }

func clock(_ ms: Int64) -> String {
    let s = max(0, ms / 1000)
    let h = s / 3600, m = (s % 3600) / 60, ss = s % 60
    return h > 0 ? String(format: "%d:%02d:%02d", h, m, ss) : String(format: "%d:%02d", m, ss)
}
