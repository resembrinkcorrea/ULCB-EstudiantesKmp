package pe.lecordonbleu.universidadestudiante

import android.app.Activity
import android.content.Intent
import android.net.Uri
import androidx.browser.customtabs.CustomTabsIntent

actual fun openMicrosoftMFA(platformContext: Any?) {
    val activity = platformContext as? Activity ?: return

    val url = "https://mysignins.microsoft.com/security-info"

    val builder = CustomTabsIntent.Builder()
    val customTabsIntent = builder.build()

    customTabsIntent.intent.flags =
        Intent.FLAG_ACTIVITY_NO_HISTORY

    customTabsIntent.launchUrl(activity, Uri.parse(url))
}
