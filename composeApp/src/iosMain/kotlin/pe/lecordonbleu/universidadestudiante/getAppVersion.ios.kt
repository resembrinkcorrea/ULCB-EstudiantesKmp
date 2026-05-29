package pe.lecordonbleu.universidadestudiante

import platform.Foundation.NSBundle

actual fun getAppVersion(): String {
    return NSBundle.mainBundle.infoDictionary
        ?.get("CFBundleShortVersionString") as? String ?: ""
}
