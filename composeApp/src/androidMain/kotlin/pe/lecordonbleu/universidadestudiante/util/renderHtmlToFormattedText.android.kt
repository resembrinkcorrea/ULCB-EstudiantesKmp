package pe.lecordonbleu.universidadestudiante.util

import android.graphics.Typeface
import android.text.Html
import android.text.style.StyleSpan
import android.text.style.UnderlineSpan
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration

actual fun renderHtmlToFormattedText(html: String): AnnotatedString {
    val spanned = Html.fromHtml(html, Html.FROM_HTML_MODE_LEGACY)
    val rawText = spanned.toString()
    val normalizedText = rawText.replace(Regex("\\n{2,}"), "\n").trim()
    val offset = rawText.length - rawText.trimStart().length

    return buildAnnotatedString {
        append(normalizedText)
        spanned.getSpans(0, spanned.length, Any::class.java).forEach { span ->
            val rawStart = spanned.getSpanStart(span) - offset
            val rawEnd = spanned.getSpanEnd(span) - offset
            val start = rawStart.coerceIn(0, normalizedText.length)
            val end = rawEnd.coerceIn(0, normalizedText.length)
            if (start >= end) return@forEach
            when (span) {
                is StyleSpan -> when (span.style) {
                    Typeface.BOLD -> addStyle(SpanStyle(fontWeight = FontWeight.Bold), start, end)
                    Typeface.ITALIC -> addStyle(SpanStyle(fontStyle = FontStyle.Italic), start, end)
                    Typeface.BOLD_ITALIC -> addStyle(
                        SpanStyle(fontWeight = FontWeight.Bold, fontStyle = FontStyle.Italic),
                        start, end
                    )
                }
                is UnderlineSpan -> addStyle(SpanStyle(textDecoration = TextDecoration.Underline), start, end)
            }
        }
    }
}
