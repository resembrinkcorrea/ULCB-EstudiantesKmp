package pe.lecordonbleu.universidadestudiante

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.interop.LocalUIViewController
import kotlinx.cinterop.BetaInteropApi
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.addressOf
import kotlinx.cinterop.usePinned
import pe.lecordonbleu.universidadestudiante.presentation.components.dialogs.ConfirmarEnvioDialog
import platform.Foundation.NSData
import platform.Foundation.NSFileManager
import platform.Foundation.NSURL
import platform.UIKit.UIDocumentPickerDelegateProtocol
import platform.UIKit.UIDocumentPickerViewController
import platform.UniformTypeIdentifiers.UTType
import platform.darwin.NSObject
import platform.posix.memcpy

@OptIn(ExperimentalForeignApi::class, BetaInteropApi::class)
@Composable
actual fun SelectorDocumentoPDF(
    onDocumentoSeleccionado: (ByteArray, String) -> Unit,
    onDismiss: () -> Unit
) {
    var bytesSeleccionados by remember { mutableStateOf<ByteArray?>(null) }
    var nombreSeleccionado by remember { mutableStateOf<String?>(null) }

    val viewController = LocalUIViewController.current

    val delegate = remember {
        DocumentPickerDelegateImpl(
            onDocumentPicked = { bytes, nombre ->
                bytesSeleccionados = bytes
                nombreSeleccionado = nombre
            },
            onCancel = onDismiss
        )
    }

    DisposableEffect(Unit) {
        val pdfType = UTType.typeWithIdentifier("com.adobe.pdf")
        val tipos = if (pdfType != null) listOf(pdfType) else emptyList<UTType>()
        val picker = UIDocumentPickerViewController(
            forOpeningContentTypes = tipos,
            asCopy = true
        )
        picker.delegate = delegate
        picker.allowsMultipleSelection = false
        viewController.presentViewController(picker, animated = true, completion = null)
        onDispose { }
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

@OptIn(ExperimentalForeignApi::class, BetaInteropApi::class)
private class DocumentPickerDelegateImpl(
    private val onDocumentPicked: (ByteArray, String) -> Unit,
    private val onCancel: () -> Unit
) : NSObject(), UIDocumentPickerDelegateProtocol {

    override fun documentPicker(
        controller: UIDocumentPickerViewController,
        didPickDocumentsAtURLs: List<*>
    ) {
        val url = didPickDocumentsAtURLs.firstOrNull() as? NSURL ?: run {
            onCancel()
            return
        }
        val nombre = url.lastPathComponent ?: "documento.pdf"
        val path = url.path ?: run {
            onCancel()
            return
        }
        val data: NSData = NSFileManager.defaultManager().contentsAtPath(path) ?: run {
            onCancel()
            return
        }
        val length = data.length.toInt()
        val bytes = ByteArray(length)
        if (length > 0) {
            bytes.usePinned { pinned ->
                memcpy(pinned.addressOf(0), data.bytes, data.length)
            }
        }
        onDocumentPicked(bytes, nombre)
    }

    override fun documentPickerWasCancelled(controller: UIDocumentPickerViewController) {
        onCancel()
    }
}
