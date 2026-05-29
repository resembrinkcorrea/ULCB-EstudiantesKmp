package pe.lecordonbleu.universidadestudiante.util

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.core.content.FileProvider
import java.io.File
import java.io.FileOutputStream

actual fun openPdfFromBytes(context: Any?, bytes: ByteArray) {
    try {
        val ctx = context as? Context ?: run {
            Log.e("openPdfFromBytes", "context es nulo o no es Context")
            return
        }

        Log.d("openPdfFromBytes", "bytes recibidos: ${bytes.size}")
        if (bytes.isEmpty()) {
            Log.e("openPdfFromBytes", "bytes vacíos — el servidor no devolvió PDF")
            return
        }

        val file = File(ctx.cacheDir, "ficha_matricula.pdf")
        FileOutputStream(file).use { it.write(bytes) }
        Log.d("openPdfFromBytes", "archivo escrito: ${file.absolutePath} — tamaño: ${file.length()} bytes")

        val uri = FileProvider.getUriForFile(ctx, "${ctx.packageName}.fileprovider", file)
        Log.d("openPdfFromBytes", "URI generada: $uri")

        val intent = Intent(Intent.ACTION_VIEW).apply {
            setDataAndType(uri, "application/pdf")
            flags = Intent.FLAG_ACTIVITY_NO_HISTORY or Intent.FLAG_GRANT_READ_URI_PERMISSION
        }
        ctx.startActivity(intent)
    } catch (e: ActivityNotFoundException) {
        Log.e("openPdfFromBytes", "No hay app para abrir PDF: ${e.message}")
    } catch (e: Exception) {
        Log.e("openPdfFromBytes", "Error al abrir PDF: ${e.message}")
    }
}
