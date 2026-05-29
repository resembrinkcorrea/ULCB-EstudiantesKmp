package pe.lecordonbleu.universidadestudiante.util

import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.addressOf
import kotlinx.cinterop.usePinned
import platform.Foundation.NSData
import platform.Foundation.NSTemporaryDirectory
import platform.Foundation.NSURL
import platform.Foundation.create
import platform.Foundation.writeToURL
import platform.UIKit.UIApplication

@OptIn(ExperimentalForeignApi::class)
actual fun openPdfFromBytes(context: Any?, bytes: ByteArray) {
    try {
        val nsData = bytes.usePinned { pinned ->
            NSData.create(bytes = pinned.addressOf(0), length = bytes.size.toULong())
        }
        val path = NSTemporaryDirectory() + "ficha_matricula.pdf"
        val fileUrl = NSURL.fileURLWithPath(path)
        nsData.writeToURL(fileUrl, atomically = true)
        UIApplication.sharedApplication.openURL(
            fileUrl,
            options = emptyMap<Any?, Any?>(),
            completionHandler = null
        )
    } catch (_: Exception) {
    }
}
