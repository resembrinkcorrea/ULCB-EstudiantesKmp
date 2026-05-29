package pe.lecordonbleu.universidadestudiante.util

actual fun renderHtmlToText(html: String): String {
    return html.replace(Regex("<[^>]*>"), "").trim()
}
