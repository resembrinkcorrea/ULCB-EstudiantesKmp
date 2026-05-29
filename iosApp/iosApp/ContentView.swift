import UIKit
import SwiftUI
import ComposeApp

struct ComposeView: UIViewControllerRepresentable {
    func makeUIViewController(context: Context) -> UIViewController {
        MainViewControllerKt.MainViewController()
    }
    func updateUIViewController(_ uiViewController: UIViewController, context: Context) {}
}

struct ContentView: View {
    @Environment(\.scenePhase) var scenePhase
    @StateObject private var sessionManager = SessionManager()

    var body: some View {
        ComposeView()
            .ignoresSafeArea()
            .onAppear {
                sessionManager.startInactivityTimer()
            }
            .onDisappear {
                sessionManager.stopInactivityTimer()
            }
            .onChange(of: scenePhase) { newPhase in
                switch newPhase {
                case .active:
                    sessionManager.handleSceneDidBecomeActive()
                case .background:
                    sessionManager.handleSceneDidEnterBackground()
                default:
                    break
                }
            }
    }
}
