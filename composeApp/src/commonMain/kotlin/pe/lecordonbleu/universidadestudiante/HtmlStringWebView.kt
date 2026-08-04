package pe.lecordonbleu.universidadestudiante

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
expect fun HtmlStringWebView(
    html: String,
    modifier: Modifier = Modifier
)

@Composable
fun ThemedHtmlWebView(html: String, modifier: Modifier = Modifier) {
    val isDark = isSystemInDarkTheme()
    val styledHtml = if (isDark) {
        """<style>
            body { background-color: #121212 !important; color: #e0e0e0 !important; }
            * { color: #e0e0e0 !important; }
            a { color: #90CAF9 !important; }
        </style>$html"""
    } else html
    HtmlStringWebView(html = styledHtml, modifier = modifier)
}
