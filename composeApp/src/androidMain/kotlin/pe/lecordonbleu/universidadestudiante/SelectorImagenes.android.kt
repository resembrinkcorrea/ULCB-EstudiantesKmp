package pe.lecordonbleu.universidadestudiante

import android.content.Context
import android.net.Uri
import android.provider.OpenableColumns
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalContext

@Composable
actual fun SelectorImagenes(
    onImagenSeleccionada: (ByteArray, String, String) -> Unit,
    onDismiss: () -> Unit
) {
    val context = LocalContext.current

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.OpenDocument(),
        onResult = { uri ->
            if (uri == null) {
                onDismiss()
                return@rememberLauncherForActivityResult
            }
            val cr = context.contentResolver
            val mime = cr.getType(uri)?.lowercase()
            val ext = when (mime) {
                "image/jpeg" -> ".jpg"
                "image/png" -> ".png"
                "image/webp" -> ".webp"
                "image/heic" -> ".heic"
                "image/heif" -> ".heif"
                else -> ".jpg"
            }
            val nombre = obtenerNombreImagenDesdeUri(context, uri) ?: "imagen$ext"
            val bytes = cr.openInputStream(uri)?.use { it.readBytes() } ?: ByteArray(0)
            onImagenSeleccionada(bytes, nombre, ext)
            onDismiss()
        }
    )

    LaunchedEffect(Unit) {
        launcher.launch(arrayOf("image/*"))
    }
}

private fun obtenerNombreImagenDesdeUri(context: Context, uri: Uri): String? {
    val cursor = context.contentResolver.query(uri, null, null, null, null)
    cursor?.use {
        val nameIndex = it.getColumnIndex(OpenableColumns.DISPLAY_NAME)
        if (nameIndex >= 0 && it.moveToFirst()) return it.getString(nameIndex)
    }
    return null
}
