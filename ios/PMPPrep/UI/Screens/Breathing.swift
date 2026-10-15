import SwiftUI
import UIKit

private let breathingCycles = 12

/** Two minutes of paced breathing: in for 4 seconds, out for 6, twelve times. One tap skips it. */
struct BreathingReset: View {
    private let onDone: () -> Void
    @Environment(\.accessibilityReduceMotion) private var reduceMotion
    @State private var grow: CGFloat = 0.55
    @State private var phase = "Breathe in"
    @State private var left = breathingCycles
    @State private var finished = false

    init(onDone: @escaping () -> Void) { self.onDone = onDone }

    private func finish() {
        if finished { return }
        finished = true
        onDone()
    }

    var body: some View {
        VStack(spacing: 0) {
            Spacer(minLength: 0)
            Text("A two-minute reset").font(.nunito(22, .heavy)).multilineTextAlignment(.center)
            Text("Follow the circle. A longer breath out than in slows things down.")
                .font(.nunito(14)).foregroundStyle(Palette.onSurfaceVariant)
                .multilineTextAlignment(.center)
                .padding(.top, 4)
            ZStack {
                Circle()
                    .fill(Palette.primaryContainer)
                    .frame(width: 220, height: 220)
                    // With reduce motion on, the circle stays still and only the words change.
                    .scaleEffect(reduceMotion ? 1 : grow)
                    .opacity(reduceMotion ? (phase == "Breathe in" ? 1 : 0.7) : 1)
                    .accessibilityHidden(true)
                Text(phase)
                    .font(.nunito(20, .bold))
                    .foregroundStyle(Palette.onPrimaryContainer)
                    .accessibilityAddTraits(.updatesFrequently)
            }
            .frame(width: 220, height: 220)
            .padding(.top, 32)
            Text("\(left) breaths to go").font(.nunito(13)).foregroundStyle(Palette.onSurfaceVariant)
                .padding(.top, 24)
            Button("I'm ready, start now") { finish() }
                .textButton()
                .padding(.top, 12)
            Spacer(minLength: 0)
        }
        .padding(24)
        .frame(maxWidth: .infinity, maxHeight: .infinity)
        .task {
            for _ in 0..<breathingCycles {
                phase = "Breathe in"
                UIAccessibility.post(notification: .announcement, argument: phase)
                withAnimation(.easeInOut(duration: 4)) { grow = 1 }
                try? await Task.sleep(nanoseconds: 4_000_000_000)
                if Task.isCancelled { return }
                phase = "Breathe out"
                UIAccessibility.post(notification: .announcement, argument: phase)
                withAnimation(.easeInOut(duration: 6)) { grow = 0.55 }
                try? await Task.sleep(nanoseconds: 6_000_000_000)
                if Task.isCancelled { return }
                left -= 1
            }
            finish()
        }
    }
}
