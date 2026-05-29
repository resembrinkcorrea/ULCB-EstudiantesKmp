package pe.lecordonbleu.universidadestudiante.core.theme

import androidx.compose.material3.Typography
import androidx.compose.runtime.Composable

/**
 * Platform-resolved Typography.
 * Android → Roboto (sistema, FontFamily.Default)
 * iOS     → SF Pro  (sistema, FontFamily.Default)
 * Fuentes de marca (rajdhani_bold, myriadproregular) aplicadas solo en
 * los estilos headline y label donde el diseño institucional lo requiere.
 */
@Composable
expect fun ilcbTypography(): Typography
