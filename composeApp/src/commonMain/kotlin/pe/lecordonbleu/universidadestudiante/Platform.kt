package pe.lecordonbleu.universidadestudiante

import androidx.compose.runtime.Composable

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform

expect fun getSistemaCapByPlatform(): Int

@Composable
expect fun getPlatformContext(): Any?