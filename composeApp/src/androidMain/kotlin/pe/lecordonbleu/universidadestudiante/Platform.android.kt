package pe.lecordonbleu.universidadestudiante

import android.os.Build
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext

class AndroidPlatform : Platform {
    override val name: String = "Android ${Build.VERSION.SDK_INT}"
}

actual fun getPlatform(): Platform = AndroidPlatform()

@Composable
actual fun getPlatformContext(): Any? = LocalContext.current

actual fun getSistemaCapByPlatform(): Int = 10
