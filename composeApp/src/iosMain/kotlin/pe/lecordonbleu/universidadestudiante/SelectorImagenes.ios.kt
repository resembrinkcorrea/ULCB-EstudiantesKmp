package pe.lecordonbleu.universidadestudiante

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.interop.LocalUIViewController
import kotlinx.cinterop.BetaInteropApi
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.addressOf
import kotlinx.cinterop.usePinned
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
actual fun SelectorImagenes(
    onImagenSeleccionada: (ByteArray, String, String) -> Unit,
    onDismiss: () -> Unit
) {
    val viewController = LocalUIViewController.current

    val delegate = remember {
        ImagePickerDelegateImpl(
            onPicked = { bytes, nombre ->
                val ext = if (nombre.contains(".")) ".${nombre.substringAfterLast(".")}" else ".jpg"
                onImagenSeleccionada(bytes, nombre, ext)
                onDismiss()
            },
            onCancel = onDismiss
        )
    }

    DisposableEffect(Unit) {
        val imageType = UTType.typeWithIdentifier("public.image")
        val tipos = if (imageType != null) listOf(imageType) else emptyList<UTType>()
        val picker = UIDocumentPickerViewController(
            forOpeningContentTypes = tipos,
            asCopy = true
        )
        picker.delegate = delegate
        picker.allowsMultipleSelection = false
        viewController.presentViewController(picker, animated = true, completion = null)
        onDispose { }
    }
}

@OptIn(ExperimentalForeignApi::class, BetaInteropApi::class)
private class ImagePickerDelegateImpl(
    private val onPicked: (ByteArray, String) -> Unit,
    private val onCancel: () -> Unit
) : NSObject(), UIDocumentPickerDelegateProtocol {

    override fun documentPicker(
        controller: UIDocumentPickerViewController,
        didPickDocumentsAtURLs: List<*>
    ) {
        val url = didPickDocumentsAtURLs.firstOrNull() as? NSURL ?: run { onCancel(); return }
        val nombre = url.lastPathComponent ?: "imagen.jpg"
        val path = url.path ?: run { onCancel(); return }
        val data: NSData = NSFileManager.defaultManager().contentsAtPath(path) ?: run { onCancel(); return }
        val length = data.length.toInt()
        val bytes = ByteArray(length)
        if (length > 0) {
            bytes.usePinned { pinned ->
                memcpy(pinned.addressOf(0), data.bytes, data.length)
            }
        }
        onPicked(bytes, nombre)
    }

    override fun documentPickerWasCancelled(controller: UIDocumentPickerViewController) {
        onCancel()
    }
}
