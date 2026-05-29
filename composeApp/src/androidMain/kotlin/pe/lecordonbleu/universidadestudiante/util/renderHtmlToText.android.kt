package pe.lecordonbleu.universidadestudiante.util

import android.text.Html
import android.text.Spanned

actual fun renderHtmlToText(html: String): String {
    val spanned: Spanned = Html.fromHtml(html, Html.FROM_HTML_MODE_LEGACY)
    return spanned.toString()
}
