package pe.lecordonbleu.universidadestudiante

expect class NotificationManagerPermission(
    platformContext: Any?
) {
    fun requestPermission(enable: Boolean)
}
