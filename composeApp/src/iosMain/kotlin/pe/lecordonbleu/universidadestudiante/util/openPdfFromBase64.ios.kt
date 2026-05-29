package pe.lecordonbleu.universidadestudiante.util

import kotlinx.cinterop.ExperimentalForeignApi
import platform.Foundation.NSData
import platform.Foundation.NSTemporaryDirectory
import platform.Foundation.NSURL
import platform.Foundation.create
import platform.Foundation.writeToURL
import platform.UIKit.UIApplication

@OptIn(ExperimentalForeignApi::class)
actual fun openPdfFromBase64(context: Any?, base64Pdf: String) {
    try {
        val nsData = NSData.create(base64Encoding = base64Pdf) ?: return
        val path = NSTemporaryDirectory() + "boleta.pdf"
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
