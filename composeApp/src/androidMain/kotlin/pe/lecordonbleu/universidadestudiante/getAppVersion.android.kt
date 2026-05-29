package pe.lecordonbleu.universidadestudiante

import android.content.Context
import org.koin.core.context.GlobalContext

actual fun getAppVersion(): String {
    return try {
        val context: Context = GlobalContext.get().get()
        context.packageManager.getPackageInfo(context.packageName, 0).versionName ?: ""
    } catch (_: Exception) {
        ""
    }
}
