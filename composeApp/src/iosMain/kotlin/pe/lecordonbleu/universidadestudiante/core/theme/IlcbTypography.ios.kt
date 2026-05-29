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
    // iOS usa SF Pro — FontFamily.Default resuelve al sistema en Compose Multiplatform
    val rajdhani  = FontFamily(Font(Res.font.rajdhani_bold, FontWeight.Bold))
    val myriadPro = FontFamily(Font(Res.font.myriadproregular, FontWeight.Normal))

    return Typography(
        // Display — SF Pro sistema
        displayLarge  = TextStyle(fontFamily = FontFamily.Default, fontSize = 57.sp, lineHeight = 64.sp),
        displayMedium = TextStyle(fontFamily = FontFamily.Default, fontSize = 45.sp, lineHeight = 52.sp),
        displaySmall  = TextStyle(fontFamily = FontFamily.Default, fontSize = 36.sp, lineHeight = 44.sp),

        // Headline — SF Pro Bold (HIG: titulares usan la fuente del sistema en iOS)
        headlineLarge  = TextStyle(fontFamily = FontFamily.Default, fontWeight = FontWeight.Bold, fontSize = 32.sp, lineHeight = 40.sp),
        headlineMedium = TextStyle(fontFamily = FontFamily.Default, fontWeight = FontWeight.Bold, fontSize = 28.sp, lineHeight = 36.sp),
        headlineSmall  = TextStyle(fontFamily = FontFamily.Default, fontWeight = FontWeight.Bold, fontSize = 24.sp, lineHeight = 32.sp),

        // Title — SF Pro sistema
        titleLarge  = TextStyle(fontFamily = FontFamily.Default, fontSize = 22.sp, fontWeight = FontWeight.Medium),
        titleMedium = TextStyle(fontFamily = FontFamily.Default, fontSize = 16.sp, fontWeight = FontWeight.Medium),
        titleSmall  = TextStyle(fontFamily = FontFamily.Default, fontSize = 14.sp, fontWeight = FontWeight.Medium),

        // Body — SF Pro sistema
        bodyLarge   = TextStyle(fontFamily = FontFamily.Default, fontSize = 16.sp),
        bodyMedium  = TextStyle(fontFamily = FontFamily.Default, fontSize = 14.sp),
        bodySmall   = TextStyle(fontFamily = FontFamily.Default, fontSize = 12.sp),

        // Label — MyriadPro (institucional, subtítulos, captions)
        labelLarge  = TextStyle(fontFamily = myriadPro, fontSize = 14.sp, fontWeight = FontWeight.Medium),
        labelMedium = TextStyle(fontFamily = myriadPro, fontSize = 12.sp, letterSpacing = 0.5.sp),
        labelSmall  = TextStyle(fontFamily = myriadPro, fontSize = 11.sp, letterSpacing = 0.5.sp),
    )
}
