package pe.lecordonbleu.universidadestudiante.util

import android.content.Context
import android.content.Intent
import android.util.Base64
import androidx.core.content.FileProvider
import java.io.File
import java.io.FileOutputStream

actual fun openPdfFromBase64(context: Any?, base64Pdf: String) {
    try {
        val ctx = context as? Context ?: return
        val data = Base64.decode(base64Pdf, Base64.DEFAULT)
        val file = File(ctx.cacheDir, "boleta.pdf")
        FileOutputStream(file).use { it.write(data) }
        val uri = FileProvider.getUriForFile(ctx, "${ctx.packageName}.fileprovider", file)
        val intent = Intent(Intent.ACTION_VIEW).apply {
            setDataAndType(uri, "application/pdf")
            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION or Intent.FLAG_ACTIVITY_NO_HISTORY)
        }
        ctx.startActivity(Intent.createChooser(intent, "Abrir PDF con..."))
    } catch (_: Exception) {
    }
}
