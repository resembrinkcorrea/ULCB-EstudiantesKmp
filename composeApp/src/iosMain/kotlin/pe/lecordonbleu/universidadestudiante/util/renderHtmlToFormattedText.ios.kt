package pe.lecordonbleu.universidadestudiante.util

import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration

actual fun renderHtmlToFormattedText(html: String): AnnotatedString {
    val raw = buildAnnotatedString {
        val stack = mutableListOf<Pair<String, Int>>()
        var idx = 0
        var pos = 0

        fun appendText(text: String) {
            val decoded = text
                .replace("&amp;", "&")
                .replace("&lt;", "<")
                .replace("&gt;", ">")
                .replace("&nbsp;", " ")
                .replace("&quot;", "\"")
            // collapse whitespace-only text between tags (tabs, newlines in source HTML)
            val cleaned = decoded.replace(Regex("[ \\t\\r\\n]+"), " ")
            if (cleaned.isNotBlank()) {
                append(cleaned)
                pos += cleaned.length
            }
        }

        while (idx < html.length) {
            if (html[idx] == '<') {
                val end = html.indexOf('>', idx)
                if (end == -1) { idx++; continue }
                val tag = html.substring(idx + 1, end).trim()
                val isClose = tag.startsWith("/")
                val tagName = tag.removePrefix("/").split(Regex("[ >]"))[0].lowercase()

                when {
                    isClose -> {
                        val openIdx = stack.indexOfLast { it.first == tagName }
                        if (openIdx >= 0) {
                            val (_, startPos) = stack.removeAt(openIdx)
                            val style = when (tagName) {
                                "b", "strong" -> SpanStyle(fontWeight = FontWeight.Bold)
                                "i", "em"     -> SpanStyle(fontStyle = FontStyle.Italic)
                                "u"           -> SpanStyle(textDecoration = TextDecoration.Underline)
                                else          -> null
                            }
                            style?.let { addStyle(it, startPos, pos) }
                        }
                        if (tagName == "p") {
                            append("\n")
                            pos++
                        }
                    }
                    tagName == "br" -> { append("\n"); pos++ }
                    tagName in listOf("b", "strong", "i", "em", "u") -> stack.add(tagName to pos)
                }
                idx = end + 1
            } else {
                val nextTag = html.indexOf('<', idx)
                val textEnd = if (nextTag == -1) html.length else nextTag
                appendText(html.substring(idx, textEnd))
                idx = textEnd
            }
        }
    }

    val normalized = raw.text.replace(Regex("\\n{2,}"), "\n").trim()
    return AnnotatedString(
        text = normalized,
        spanStyles = raw.spanStyles
            .filter { it.start < normalized.length }
            .map { it.copy(end = minOf(it.end, normalized.length)) }
    )
}
