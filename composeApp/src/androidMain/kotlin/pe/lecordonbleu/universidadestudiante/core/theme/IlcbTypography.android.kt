package pe.lecordonbleu.universidadestudiante.core.theme

import androidx.compose.material3.Typography
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import ulcbintranetkmp.composeapp.generated.resources.Res
import ulcbintranetkmp.composeapp.generated.resources.myriadproregular
import ulcbintranetkmp.composeapp.generated.resources.rajdhani_bold
import org.jetbrains.compose.resources.Font

@Composable
actual fun ilcbTypography(): Typography {
    // Android usa Roboto por defecto — null FontFamily = sistema
    val rajdhani  = FontFamily(Font(Res.font.rajdhani_bold, FontWeight.Bold))
    val myriadPro = FontFamily(Font(Res.font.myriadproregular, FontWeight.Normal))

    return Typography(
        // Display — Roboto sistema
        displayLarge  = TextStyle(fontSize = 57.sp, lineHeight = 64.sp),
        displayMedium = TextStyle(fontSize = 45.sp, lineHeight = 52.sp),
        displaySmall  = TextStyle(fontSize = 36.sp, lineHeight = 44.sp),

        // Headline — Rajdhani Bold (impacto de marca)
        headlineLarge  = TextStyle(fontFamily = rajdhani, fontWeight = FontWeight.Bold, fontSize = 32.sp, lineHeight = 40.sp),
        headlineMedium = TextStyle(fontFamily = rajdhani, fontWeight = FontWeight.Bold, fontSize = 28.sp, lineHeight = 36.sp),
        headlineSmall  = TextStyle(fontFamily = rajdhani, fontWeight = FontWeight.Bold, fontSize = 24.sp, lineHeight = 32.sp),

        // Title — Roboto sistema
        titleLarge  = TextStyle(fontSize = 22.sp, fontWeight = FontWeight.Medium),
        titleMedium = TextStyle(fontSize = 16.sp, fontWeight = FontWeight.Medium),
        titleSmall  = TextStyle(fontSize = 14.sp, fontWeight = FontWeight.Medium),

        // Body — Roboto sistema
        bodyLarge   = TextStyle(fontSize = 16.sp),
        bodyMedium  = TextStyle(fontSize = 14.sp),
        bodySmall   = TextStyle(fontSize = 12.sp),

        // Label — MyriadPro (institucional, subtítulos, captions)
        labelLarge  = TextStyle(fontFamily = myriadPro, fontSize = 14.sp, fontWeight = FontWeight.Medium),
        labelMedium = TextStyle(fontFamily = myriadPro, fontSize = 12.sp, letterSpacing = 0.5.sp),
        labelSmall  = TextStyle(fontFamily = myriadPro, fontSize = 11.sp, letterSpacing = 0.5.sp),
    )
}
