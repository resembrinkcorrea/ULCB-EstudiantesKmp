package pe.lecordonbleu.universidadestudiante

import platform.UserNotifications.*

actual class NotificationManagerPermission actual constructor(platformContext: Any?) {

    actual fun requestPermission(enable: Boolean) {
        if (!enable) return

        val center = UNUserNotificationCenter.currentNotificationCenter()
        center.requestAuthorizationWithOptions(
            options = UNAuthorizationOptionAlert or UNAuthorizationOptionBadge or UNAuthorizationOptionSound
        ) { granted, _ ->
            if (granted) println("Permiso de notificaciones concedido")
            else println("Permiso de notificaciones denegado")
        }
    }
}
