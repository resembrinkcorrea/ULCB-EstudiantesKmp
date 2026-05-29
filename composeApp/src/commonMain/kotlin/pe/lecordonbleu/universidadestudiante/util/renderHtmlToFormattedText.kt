package pe.lecordonbleu.universidadestudiante.util

import androidx.compose.ui.text.AnnotatedString

expect fun renderHtmlToFormattedText(html: String): AnnotatedString
