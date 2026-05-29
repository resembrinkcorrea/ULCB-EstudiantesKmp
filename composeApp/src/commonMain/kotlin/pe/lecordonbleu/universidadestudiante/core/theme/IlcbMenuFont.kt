package pe.lecordonbleu.universidadestudiante.core.theme

import androidx.compose.runtime.Composable
import androidx.compose.ui.text.font.FontFamily

/**
 * Fuente de acento para los labels del menú de servicios.
 * Cada plataforma resuelve su propia fuente bold/geométrica:
 *   - Android → Rajdhani Bold
 *   - iOS     → Futura Bold
 */
@Composable
expect fun menuLabelFontFamily(): FontFamily
