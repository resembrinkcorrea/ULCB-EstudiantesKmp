package pe.lecordonbleu.universidadestudiante.core.utils

expect object NetworkUtils {
    suspend fun getPublicIPAddress(): String?
}
