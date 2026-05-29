import Foundation
import UIKit
import MSAL
import ComposeApp
import FirebaseCore
import FirebaseMessaging
import FirebaseDatabase
import UserNotifications

class AppDelegate: NSObject, UIApplicationDelegate, UNUserNotificationCenterDelegate, MessagingDelegate {
    var window: UIWindow?

    override init() {
        super.init()
        MSALGlobalConfig.brokerAvailability = .none
        MSALGlobalConfig.loggerConfig.setLogCallback { logLevel, message, containsPII in
            if containsPII {
                print("MSAL Log (PII): \(message ?? "No message")")
            } else {
                print("MSAL Log: \(message ?? "No message")")
            }
        }
        MSALGlobalConfig.loggerConfig.logLevel = .verbose
    }

    func application(_ app: UIApplication, open url: URL, options: [UIApplication.OpenURLOptionsKey: Any] = [:]) -> Bool {
        print("🔥 CALLBACK URL:", url.absoluteString)
        return MSALPublicClientApplication.handleMSALResponse(url, sourceApplication: options[.sourceApplication] as? String)
    }

    func application(_ application: UIApplication, didFinishLaunchingWithOptions launchOptions: [UIApplication.LaunchOptionsKey: Any]?) -> Bool {
        print("AppDelegate: didFinishLaunchingWithOptions")
        
        FirebaseApp.configure()

        UNUserNotificationCenter.current().delegate = self
        Messaging.messaging().delegate = self

        requestNotificationPermission(application: application)
        
        return true
    }
    
    private func requestNotificationPermission(application: UIApplication) {
        let center = UNUserNotificationCenter.current()
        center.requestAuthorization(options: [.alert, .badge, .sound]) { granted, error in
            if let error = error {
                print("Error solicitando permisos de notificaciones: \(error.localizedDescription)")
                return
            }

            print("Permiso de notificaciones iOS: \(granted)")

            DispatchQueue.main.async {
                application.registerForRemoteNotifications()
            }
        }
    }
    
    func application(_ application: UIApplication, didRegisterForRemoteNotificationsWithDeviceToken deviceToken: Data) {
        Messaging.messaging().apnsToken = deviceToken
        
        print("APNs Device Token (Data): \(deviceToken.map { String(format: "%02.2hhx", $0) }.joined())")
        
        print("APNs token registrado correctamente.")
    }

    func application(_ application: UIApplication, didFailToRegisterForRemoteNotificationsWithError error: Error) {
        print("Error registrando APNs: \(error.localizedDescription)")
    }

    func messaging(_ messaging: Messaging, didReceiveRegistrationToken fcmToken: String?) {
        guard let fcmToken = fcmToken else {
            print("FCM token no disponible.")
            return
        }
        print("FCM token iOS: \(fcmToken)")
        FcmTokenHolder.shared.token = fcmToken
    }

    func userNotificationCenter(
        _ center: UNUserNotificationCenter,
        willPresent notification: UNNotification,
        withCompletionHandler completionHandler: @escaping (UNNotificationPresentationOptions) -> Void
    ) {
        completionHandler([.banner, .badge, .sound])
    }

    func userNotificationCenter(
        _ center: UNUserNotificationCenter,
        didReceive response: UNNotificationResponse,
        withCompletionHandler completionHandler: @escaping () -> Void
    ) {
        let userInfo = response.notification.request.content.userInfo
        print("Notificación abierta desde iOS: \(userInfo)")
        completionHandler()
    }
}
