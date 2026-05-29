package pe.lecordonbleu.universidadestudiante.core.utils

import kotlinx.coroutines.suspendCancellableCoroutine
import platform.Foundation.*
import kotlin.coroutines.resume

actual object NetworkUtils {
    actual suspend fun getPublicIPAddress(): String? = suspendCancellableCoroutine { continuation ->
        val url = NSURL(string = "https://api.ipify.org")
            ?: return@suspendCancellableCoroutine continuation.resume(null)

        val task = NSURLSession.sharedSession.dataTaskWithURL(url) { data, _, error ->
            if (error != null || data == null) {
                continuation.resume(null)
            } else {
                val ipAddress = NSString.create(data, NSUTF8StringEncoding) as String
                continuation.resume(ipAddress)
            }
        }
        task.resume()
    }
}
