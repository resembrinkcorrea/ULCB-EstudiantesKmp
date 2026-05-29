package pe.lecordonbleu.universidadestudiante.core.utils

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.net.URL

actual object NetworkUtils {
    actual suspend fun getPublicIPAddress(): String? = withContext(Dispatchers.IO) {
        try {
            val url = URL("https://api.ipify.org")
            val connection = url.openConnection()
            connection.getInputStream().bufferedReader().use { it.readLine() }
        } catch (e: Exception) {
            null
        }
    }
}
