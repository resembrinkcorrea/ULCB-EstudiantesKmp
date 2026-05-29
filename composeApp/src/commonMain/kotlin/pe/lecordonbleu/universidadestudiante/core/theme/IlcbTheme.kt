package pe.lecordonbleu.universidadestudiante.core.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.lightColors
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

@Composable
fun IlcbTheme(content: @Composable () -> Unit) {
    val isDarkMode = isSystemInDarkTheme()
    val colorScheme = if (isDarkMode) IlcbDarkColorScheme else IlcbLightColorScheme
    val m2Primary = if (isDarkMode) Color(0xFF90CAF9) else IlcbBlueDark

    // M3 — pantallas nuevas usan MaterialTheme.colorScheme
    MaterialTheme(
        colorScheme = colorScheme,
        typography  = ilcbTypography(),
        shapes      = IlcbShapes
    ) {
        // M2 — componentes existentes (OutlinedTextField, Scaffold, TopAppBar) reciben el azul correcto
        androidx.compose.material.MaterialTheme(
            colors = lightColors(primary = m2Primary)
        ) {
            content()
        }
    }
}
