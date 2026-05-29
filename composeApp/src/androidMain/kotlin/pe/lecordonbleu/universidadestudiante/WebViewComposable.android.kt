package pe.lecordonbleu.universidadestudiante

import android.annotation.SuppressLint
import android.net.http.SslError
import android.webkit.JavascriptInterface
import android.webkit.SslErrorHandler
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView

@SuppressLint("SetJavaScriptEnabled")
@Composable
actual fun WebViewComposable(
    url: String,
    returnDomain: String,
    onClose: () -> Unit,
    modifier: Modifier
) {
    AndroidView(
        modifier = modifier,
        factory = { context ->
            WebView(context).apply {
                settings.javaScriptEnabled = true
                addJavascriptInterface(object : Any() {
                    @JavascriptInterface
                    fun onButtonCloseClicked() {
                        android.os.Handler(android.os.Looper.getMainLooper()).post {
                            onClose()
                        }
                    }
                }, "AndroidBridge")
                webViewClient = object : WebViewClient() {
                    override fun shouldOverrideUrlLoading(
                        view: WebView?,
                        request: WebResourceRequest?
                    ): Boolean {
                        return false
                    }

                    override fun onPageFinished(view: WebView?, url: String?) {
                        super.onPageFinished(view, url)
                        if (url?.contains(returnDomain) == true && url.contains("/pages/")) {
                            view?.loadUrl(
                                "javascript:(function() { " +
                                    "document.getElementById('cancelarLCB').onclick = function() { " +
                                    "AndroidBridge.onButtonCloseClicked(); " +
                                    "}; " +
                                    "})()"
                            )
                        }
                    }

                    override fun onReceivedSslError(
                        view: WebView?,
                        handler: SslErrorHandler?,
                        error: SslError?
                    ) {
                        handler?.cancel()
                    }
                }
                loadUrl(url)
            }
        }
    )
}
