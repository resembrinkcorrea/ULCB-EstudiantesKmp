package pe.lecordonbleu.universidadestudiante

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform

expect fun getSistemaCapByPlatform(): Int

@Composable
expect fun getPlatformContext(): Any?

expect fun Modifier.imeKeyboardPadding(): Modifier