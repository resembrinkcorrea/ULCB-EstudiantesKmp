package pe.lecordonbleu.universidadestudiante

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.interop.UIKitView
import kotlinx.cinterop.ExperimentalForeignApi
import platform.CoreGraphics.CGRectMake
import platform.WebKit.WKWebView
import platform.WebKit.WKWebViewConfiguration

@OptIn(ExperimentalForeignApi::class)
@Composable
actual fun HtmlStringWebView(
    html: String,
    modifier: Modifier
) {
    val webView = remember {
        val config = WKWebViewConfiguration()
        WKWebView(frame = CGRectMake(0.0, 0.0, 0.0, 0.0), configuration = config)
    }

    UIKitView(
        modifier = modifier,
        factory = {
            webView.loadHTMLString(html, baseURL = null)
            webView
        },
        update = { view ->
            view.loadHTMLString(html, baseURL = null)
        }
    )
}
