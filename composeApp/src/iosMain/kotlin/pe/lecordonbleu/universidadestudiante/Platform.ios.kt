package pe.lecordonbleu.universidadestudiante

import androidx.compose.foundation.layout.imePadding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.interop.LocalUIViewController
import platform.UIKit.UIDevice

class IOSPlatform : Platform {
    override val name: String = UIDevice.currentDevice.systemName() + " " + UIDevice.currentDevice.systemVersion
}

actual fun getPlatform(): Platform = IOSPlatform()

@Composable
actual fun getPlatformContext(): Any? = LocalUIViewController.current

actual fun getSistemaCapByPlatform(): Int = 12

actual fun Modifier.imeKeyboardPadding(): Modifier = this.imePadding()
