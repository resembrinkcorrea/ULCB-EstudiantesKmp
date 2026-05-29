package pe.lecordonbleu.universidadestudiante

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import ulcbintranetkmp.composeapp.generated.resources.Res
import ulcbintranetkmp.composeapp.generated.resources.myriadproregular
import ulcbintranetkmp.composeapp.generated.resources.rajdhani_bold
import org.jetbrains.compose.resources.Font
import pe.lecordonbleu.universidadestudiante.core.theme.IlcbAmbar
import pe.lecordonbleu.universidadestudiante.core.theme.IlcbAzulCielo
import pe.lecordonbleu.universidadestudiante.core.theme.IlcbAzulFuerte
import pe.lecordonbleu.universidadestudiante.core.theme.IlcbAzulMarca
import pe.lecordonbleu.universidadestudiante.core.theme.IlcbAzulMedio
import pe.lecordonbleu.universidadestudiante.core.theme.IlcbAzulProfundo
import pe.lecordonbleu.universidadestudiante.core.theme.IlcbBackground
import pe.lecordonbleu.universidadestudiante.core.theme.IlcbBackgroundDark
import pe.lecordonbleu.universidadestudiante.core.theme.IlcbBlueAccent
import pe.lecordonbleu.universidadestudiante.core.theme.IlcbBlueAccento
import pe.lecordonbleu.universidadestudiante.core.theme.IlcbBlueDeep
import pe.lecordonbleu.universidadestudiante.core.theme.IlcbBlueDark
import pe.lecordonbleu.universidadestudiante.core.theme.IlcbBlueDarkDm
import pe.lecordonbleu.universidadestudiante.core.theme.IlcbBlueIndigoDm
import pe.lecordonbleu.universidadestudiante.core.theme.IlcbBlueMid
import pe.lecordonbleu.universidadestudiante.core.theme.IlcbBlueMidDm
import pe.lecordonbleu.universidadestudiante.core.theme.IlcbCian
import pe.lecordonbleu.universidadestudiante.core.theme.IlcbError
import pe.lecordonbleu.universidadestudiante.core.theme.IlcbGrisAzulado
import pe.lecordonbleu.universidadestudiante.core.theme.IlcbGrisCharcoal
import pe.lecordonbleu.universidadestudiante.core.theme.IlcbGrisClaro
import pe.lecordonbleu.universidadestudiante.core.theme.IlcbGrisMedio
import pe.lecordonbleu.universidadestudiante.core.theme.IlcbGreenMid
import pe.lecordonbleu.universidadestudiante.core.theme.IlcbGrisNeutro
import pe.lecordonbleu.universidadestudiante.core.theme.IlcbGrisOscuro
import pe.lecordonbleu.universidadestudiante.core.theme.IlcbGrisPizarra
import pe.lecordonbleu.universidadestudiante.core.theme.IlcbNaranja
import pe.lecordonbleu.universidadestudiante.core.theme.IlcbNaranjaOscuro
import pe.lecordonbleu.universidadestudiante.core.theme.IlcbOnBrand
import pe.lecordonbleu.universidadestudiante.core.theme.IlcbOnSurface
import pe.lecordonbleu.universidadestudiante.core.theme.IlcbPastelAzul
import pe.lecordonbleu.universidadestudiante.core.theme.IlcbPastelGris
import pe.lecordonbleu.universidadestudiante.core.theme.IlcbPastelLavanda
import pe.lecordonbleu.universidadestudiante.core.theme.IlcbPastelMarron
import pe.lecordonbleu.universidadestudiante.core.theme.IlcbPastelMorado
import pe.lecordonbleu.universidadestudiante.core.theme.IlcbPastelNaranja
import pe.lecordonbleu.universidadestudiante.core.theme.IlcbPastelRosa
import pe.lecordonbleu.universidadestudiante.core.theme.IlcbPastelRosaSuave
import pe.lecordonbleu.universidadestudiante.core.theme.IlcbPastelTeal
import pe.lecordonbleu.universidadestudiante.core.theme.IlcbPastelVerde
import pe.lecordonbleu.universidadestudiante.core.theme.IlcbPurple
import pe.lecordonbleu.universidadestudiante.core.theme.IlcbSurface
import pe.lecordonbleu.universidadestudiante.core.theme.IlcbSurfaceDark
import pe.lecordonbleu.universidadestudiante.core.theme.IlcbTheme
import pe.lecordonbleu.universidadestudiante.core.theme.IlcbVerdeMedio
import pe.lecordonbleu.universidadestudiante.core.theme.IlcbVerdeClaro
import pe.lecordonbleu.universidadestudiante.core.theme.IlcbVioleta
import pe.lecordonbleu.universidadestudiante.core.theme.IlcbWarning
import pe.lecordonbleu.universidadestudiante.core.theme.IlcbVioletaMedio
import pe.lecordonbleu.universidadestudiante.core.theme.IlcbEsmeralda
import pe.lecordonbleu.universidadestudiante.core.theme.IlcbRosado
import pe.lecordonbleu.universidadestudiante.core.theme.IlcbNaranjaDorado
import pe.lecordonbleu.universidadestudiante.core.theme.IlcbDoradoClaro
import pe.lecordonbleu.universidadestudiante.core.theme.IlcbStripeRed
import pe.lecordonbleu.universidadestudiante.core.theme.IlcbStripeBlue
import pe.lecordonbleu.universidadestudiante.core.theme.IlcbStripeGreen

@Composable
fun AppTheme(content: @Composable () -> Unit) {
    IlcbTheme(content)
}

@Composable
fun getColorsTheme(): DarkModeColors {
    val isDarkMode = isSystemInDarkTheme()
    return DarkModeColors(
        colorPurpura             = IlcbPurple,
        colorExpenseItem         = if (isDarkMode) IlcbSurfaceDark else IlcbSurface,
        backGroundColor          = if (isDarkMode) IlcbBackgroundDark else IlcbBackground,
        textColor                = if (isDarkMode) Color.White else IlcbOnSurface,
        colorIcono               = if (isDarkMode) Color.White else IlcbOnSurface,
        colorArrowRound          = Color.Gray.copy(alpha = .2F),
        colorAzulProfundo        = IlcbAzulProfundo,
        colorAzulContraste       = if (isDarkMode) IlcbAzulCielo else IlcbBlueDark,
        colorCardRespondida      = if (isDarkMode) IlcbAzulCielo.copy(alpha = 0.16f) else Color(0xFFDDE3FF),
        colorCardNoRespondida    = if (isDarkMode) IlcbSurfaceDark else IlcbPastelGris,
        colorCardDeshabilitada   = if (isDarkMode) IlcbGrisCharcoal.copy(alpha = 0.55f) else IlcbGrisAzulado.copy(alpha = 0.25f),
        colorOptionNoSeleccionado = if (isDarkMode) IlcbGrisAzulado else IlcbGrisNeutro,
        colorTransparente        = Color.Transparent,
        colorVerdeMedio          = IlcbVerdeMedio,
        colorAmbar               = IlcbAmbar,
        colorMixPrimary          = if (isDarkMode) IlcbBlueMidDm else IlcbBlueMid,
        colorAzulCielo           = IlcbAzulCielo,
        primary                  = if (isDarkMode) IlcbBlueDarkDm else IlcbBlueDark,
        secondary                = if (isDarkMode) IlcbBlueIndigoDm else IlcbBlueAccent,
        colorBlueAccento         = IlcbBlueAccento,
        colorGrisMedio           = IlcbGrisMedio,
        colorBlanco              = IlcbOnBrand,
        colorGrisClaro           = IlcbGrisClaro,
        colorGrisOscuro          = IlcbGrisOscuro,
        colorVioletaIntenso      = IlcbVioleta,
        colorAzulMedio           = IlcbAzulMedio,
        colorNaranjaBrillante    = IlcbNaranja,
        colorAzulFuerte          = IlcbAzulFuerte,
        colorRojo                = IlcbError,
        colorAzulMarcacionClaro  = IlcbAzulMarca,
        colorCian                = IlcbCian,
        colorNaranjaAmbar        = IlcbWarning,
        colorNegro               = IlcbOnSurface,
        colorVerdeClaro          = IlcbVerdeClaro,
        colorIndigoMedio         = IlcbBlueAccent,
        colorPastelTeal          = if (isDarkMode) IlcbSurfaceDark else IlcbPastelTeal,
        colorPastelAzul          = if (isDarkMode) IlcbSurfaceDark else IlcbPastelAzul,
        colorPastelMarron        = if (isDarkMode) IlcbSurfaceDark else IlcbPastelMarron,
        colorPastelVerde         = if (isDarkMode) IlcbSurfaceDark else IlcbPastelVerde,
        colorPastelLavanda       = if (isDarkMode) IlcbSurfaceDark else IlcbPastelLavanda,
        colorPastelGris          = if (isDarkMode) IlcbSurfaceDark else IlcbPastelGris,
        colorPastelRosa          = if (isDarkMode) IlcbSurfaceDark else IlcbPastelRosa,
        colorPastelMorado        = if (isDarkMode) IlcbSurfaceDark else IlcbPastelMorado,
        colorPastelNaranja       = if (isDarkMode) IlcbSurfaceDark else IlcbPastelNaranja,
        colorPastelRosaSuave     = if (isDarkMode) IlcbGrisOscuro else IlcbPastelRosaSuave,
        colorAzulOscuro          = if (isDarkMode) Color.Black else IlcbBlueDeep,
        colorNaranjaOscuro       = IlcbNaranjaOscuro,
        colorAzulGrisado         = if (isDarkMode) IlcbGrisAzulado else IlcbBlueAccent,
        colorGrisAzulado         = IlcbGrisAzulado,
        colorGrisNeutro          = if (isDarkMode) IlcbGrisAzulado else IlcbGrisNeutro,
        colorGrisPizarra         = if (isDarkMode) IlcbGrisAzulado else IlcbGrisPizarra,
        colorIndigo              = IlcbBlueAccent,
        colorBlancoGris          = if (isDarkMode) IlcbGrisCharcoal else Color.White,
        colorVerdeFuerte         = IlcbGreenMid,
        colorVioletaMedio        = IlcbVioletaMedio,
        colorEsmeralda           = IlcbEsmeralda,
        colorRosado              = IlcbRosado,
        colorNaranjaDorado       = IlcbNaranjaDorado,
        colorDoradoClaro         = IlcbDoradoClaro,
        colorOrange800           = IlcbNaranjaOscuro,
        colorStripeRojo          = IlcbStripeRed,
        colorStripeAzul          = IlcbStripeBlue,
        colorStripeVerde         = IlcbStripeGreen
    )
}


@Composable
fun radjhaniFonFamily() = FontFamily(
    Font(Res.font.rajdhani_bold, weight = FontWeight.Bold)
)

@Composable
fun leCordonBleuFont() = FontFamily(
    Font(Res.font.myriadproregular, FontWeight.Bold)
)

data class DarkModeColors(
    val colorPurpura: Color,
    val colorExpenseItem: Color,
    val backGroundColor: Color,
    val textColor: Color,
    val colorIcono: Color,
    val colorArrowRound: Color,
    val colorAzulProfundo: Color,
    val colorAzulContraste: Color,
    val colorCardRespondida: Color,
    val colorCardNoRespondida: Color,
    val colorCardDeshabilitada: Color,
    val colorOptionNoSeleccionado: Color,
    val colorTransparente: Color,
    val colorVerdeMedio: Color,
    val colorAmbar: Color,
    val colorMixPrimary: Color,
    val colorAzulCielo: Color,
    val primary: Color,
    val secondary: Color,
    val colorBlueAccento: Color,
    val colorGrisMedio: Color,
    val colorBlanco: Color,
    val colorGrisClaro: Color,
    val colorGrisOscuro: Color,
    val colorVioletaIntenso: Color,
    val colorAzulMedio: Color,
    val colorNaranjaBrillante: Color,
    val colorAzulFuerte: Color,
    val colorRojo: Color,
    val colorAzulMarcacionClaro: Color,
    val colorCian: Color,
    val colorNaranjaAmbar: Color,
    val colorNegro: Color,
    val colorVerdeClaro: Color,
    val colorIndigoMedio: Color,
    val colorPastelTeal: Color,
    val colorPastelAzul: Color,
    val colorPastelMarron: Color,
    val colorPastelVerde: Color,
    val colorPastelLavanda: Color,
    val colorPastelGris: Color,
    val colorPastelRosa: Color,
    val colorPastelMorado: Color,
    val colorPastelNaranja: Color,
    val colorPastelRosaSuave: Color,
    val colorAzulOscuro: Color,
    val colorNaranjaOscuro: Color,
    val colorAzulGrisado: Color,
    val colorGrisAzulado: Color,
    val colorGrisNeutro: Color,
    val colorGrisPizarra: Color,
    val colorIndigo: Color,
    val colorBlancoGris: Color,
    val colorVerdeFuerte: Color,
    val colorVioletaMedio: Color,
    val colorEsmeralda: Color,
    val colorRosado: Color,
    val colorNaranjaDorado: Color,
    val colorDoradoClaro: Color,
    val colorOrange800: Color,
    val colorStripeRojo: Color,
    val colorStripeAzul: Color,
    val colorStripeVerde: Color
)
