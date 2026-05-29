package pe.lecordonbleu.universidadestudiante.util

import android.content.Context
import android.content.Intent
import android.net.Uri

actual fun openUrl(context: Any?, url: String) {
    try {
        val ctx = context as? Context ?: return
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        ctx.startActivity(intent)
    } catch (_: Exception) {
    }
}
