package pe.lecordonbleu.universidadestudiante.core.theme

import androidx.compose.runtime.Composable
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import ulcbintranetkmp.composeapp.generated.resources.Res
import ulcbintranetkmp.composeapp.generated.resources.rajdhani_bold
import org.jetbrains.compose.resources.Font

@Composable
actual fun menuLabelFontFamily(): FontFamily =
    FontFamily(Font(Res.font.rajdhani_bold, FontWeight.Bold))
