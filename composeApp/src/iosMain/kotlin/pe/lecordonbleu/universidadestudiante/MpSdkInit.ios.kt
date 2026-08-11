@file:OptIn(ExperimentalForeignApi::class)

package pe.lecordonbleu.universidadestudiante

import cocoapods.MPCoreBridge.MPCoreBridge
import kotlinx.cinterop.ExperimentalForeignApi
import kotlin.text.isEmpty

actual fun initMpSdkIfNeeded(context: Any?, publicKey: String) {
    if (publicKey.isEmpty()) return
    MPCoreBridge.initializeSDKWithPublicKey(publicKey)
}
