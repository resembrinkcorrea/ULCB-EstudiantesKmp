package pe.lecordonbleu.universidadestudiante

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.interop.UIKitView
import kotlinx.cinterop.ExperimentalForeignApi
import platform.CoreGraphics.CGRectMake
import platform.Foundation.NSURL
import platform.Foundation.NSURLRequest
import platform.WebKit.WKNavigation
import platform.WebKit.WKNavigationDelegateProtocol
import platform.WebKit.WKScriptMessage
import platform.WebKit.WKScriptMessageHandlerProtocol
import platform.WebKit.WKUserContentController
import platform.WebKit.WKWebView
import platform.WebKit.WKWebViewConfiguration
import platform.darwin.NSObject

@OptIn(ExperimentalForeignApi::class)
@Composable
actual fun WebViewComposable(
    url: String,
    returnDomain: String,
    onClose: () -> Unit,
    modifier: Modifier
) {
    val messageHandler = remember {
        object : NSObject(), WKScriptMessageHandlerProtocol {
            override fun userContentController(
                userContentController: WKUserContentController,
                didReceiveScriptMessage: WKScriptMessage
            ) {
                println("iOS WebView — mensaje recibido: ${didReceiveScriptMessage.name}")
                if (didReceiveScriptMessage.name == "iOSBridge") {
                    onClose()
                }
            }
        }
    }

    val navigationDelegate = remember {
        object : NSObject(), WKNavigationDelegateProtocol {
            override fun webView(webView: WKWebView, didFinishNavigation: WKNavigation?) {
                val currentUrl = webView.URL?.absoluteString ?: ""
                println("iOS WebView — página cargada: $currentUrl")
                if (currentUrl.contains(returnDomain) && currentUrl.contains("/pages/")) {
                    println("iOS WebView — URL de retorno detectada, inyectando JS")
                    val js = """
                        (function() {
                            var btn = document.getElementById('cancelarLCB');
                            console.log('cancelarLCB encontrado: ' + (btn != null));
                            if (btn) {
                                btn.onclick = function() {
                                    window.webkit.messageHandlers.iOSBridge.postMessage('close');
                                };
                            }
                        })();
                    """.trimIndent()
                    webView.evaluateJavaScript(js) { result, error ->
                        println("iOS WebView — evaluateJavaScript result: $result, error: $error")
                    }
                }
            }
        }
    }

    UIKitView(
        modifier = modifier,
        factory = {
            val config = WKWebViewConfiguration()
            config.userContentController.addScriptMessageHandler(messageHandler, name = "iOSBridge")
            val webView = WKWebView(frame = CGRectMake(0.0, 0.0, 0.0, 0.0), configuration = config)
            webView.navigationDelegate = navigationDelegate
            val nsUrl = NSURL.URLWithString(url)
            if (nsUrl != null) {
                webView.loadRequest(NSURLRequest.requestWithURL(nsUrl))
            }
            webView
        }
    )
}
