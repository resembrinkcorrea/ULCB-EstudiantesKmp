package pe.lecordonbleu.universidadestudiante.util

import platform.Foundation.NSURL
import platform.UIKit.UIApplication

actual fun openUrl(context: Any?, url: String) {
    val nsUrl = NSURL.URLWithString(url) ?: return
    UIApplication.sharedApplication.openURL(
        nsUrl,
        options = emptyMap<Any?, Any?>(),
        completionHandler = null
    )
}
