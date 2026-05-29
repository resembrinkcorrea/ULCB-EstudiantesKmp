import Foundation
import Combine

class SessionManager: ObservableObject {
    @Published var isUserLoggedIn: Bool = true
    private var inactivityTimer: Timer?
    private let inactivityThreshold: TimeInterval = 300
    private var lastBackgroundTime: Date?

    init() {
        checkIfSessionExpired()
    }

    private func checkIfSessionExpired() {
        if let lastCloseTime = UserDefaults.standard.object(forKey: "LastBackgroundTime") as? Date {
            let timeElapsed = Date().timeIntervalSince(lastCloseTime)
            if timeElapsed > inactivityThreshold {
                logOut()
            }
        }
    }

    func handleSceneDidBecomeActive() {
        resetInactivityTimer()
        if let lastBackgroundTime = lastBackgroundTime {
            let timeElapsed = Date().timeIntervalSince(lastBackgroundTime)
            if timeElapsed > inactivityThreshold {
                logOut()
            }
        }
    }

    func handleSceneDidEnterBackground() {
        lastBackgroundTime = Date()
        UserDefaults.standard.set(lastBackgroundTime, forKey: "LastBackgroundTime")
        stopInactivityTimer()
    }

    func startInactivityTimer() {
        stopInactivityTimer()
        inactivityTimer = Timer.scheduledTimer(withTimeInterval: inactivityThreshold, repeats: false) { [weak self] _ in
            self?.logOut()
        }
    }

    func resetInactivityTimer() {
        startInactivityTimer()
    }

    func stopInactivityTimer() {
        inactivityTimer?.invalidate()
        inactivityTimer = nil
    }

    func logOut() {
        isUserLoggedIn = false
        print("Usuario inactivo. Sesión cerrada.")
    }
}
