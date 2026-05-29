package pe.lecordonbleu.universidadestudiante

import android.content.Context
import android.net.Uri
import android.provider.OpenableColumns
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import pe.lecordonbleu.universidadestudiante.presentation.components.dialogs.ConfirmarEnvioDialog

@Composable
actual fun SelectorDocumentoPDF(
    onDocumentoSeleccionado: (ByteArray, String) -> Unit,
    onDismiss: () -> Unit
) {
    val context = LocalContext.current

    var bytesSeleccionados by remember { mutableStateOf<ByteArray?>(null) }
    var nombreSeleccionado by remember { mutableStateOf<String?>(null) }

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.OpenDocument(),
        onResult = { uri ->
            if (uri != null) {
                val nombre = obtenerNombreDesdeUri(context, uri)
                val bytes = leerBytesDesdeUri(context, uri)
                if (nombre != null && bytes != null) {
                    bytesSeleccionados = bytes
                    nombreSeleccionado = nombre
                } else {
                    onDismiss()
                }
            } else {
                onDismiss()
            }
        }
    )

    LaunchedEffect(Unit) {
        launcher.launch(arrayOf("application/pdf"))
    }

    if (bytesSeleccionados != null && nombreSeleccionado != null) {
        ConfirmarEnvioDialog(
            nombreArchivo = nombreSeleccionado!!,
            onConfirmar = {
                onDocumentoSeleccionado(bytesSeleccionados!!, nombreSeleccionado!!)
                bytesSeleccionados = null
                nombreSeleccionado = null
                onDismiss()
            },
            onCancelar = {
                bytesSeleccionados = null
                nombreSeleccionado = null
                onDismiss()
            }
        )
    }
}

private fun obtenerNombreDesdeUri(context: Context, uri: Uri): String? {
    val cursor = context.contentResolver.query(uri, null, null, null, null)
    cursor?.use {
        val nameIndex = it.getColumnIndex(OpenableColumns.DISPLAY_NAME)
        it.moveToFirst()
        return it.getString(nameIndex)
    }
    return null
}

private fun leerBytesDesdeUri(context: Context, uri: Uri): ByteArray? {
    return try {
        val inputStream = context.contentResolver.openInputStream(uri)
        val bytes = inputStream?.readBytes()
        inputStream?.close()
        bytes
    } catch (e: Exception) {
        null
    }
}
