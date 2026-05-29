package pe.lecordonbleu.universidadestudiante.core.theme

import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.ui.graphics.Color

val IlcbLightColorScheme = lightColorScheme(
    primary             = IlcbBlueDark,
    onPrimary           = IlcbOnBrand,
    primaryContainer    = IlcbBlueDeep,
    onPrimaryContainer  = IlcbOnBrand,

    secondary           = IlcbBlueMid,
    onSecondary         = IlcbOnBrand,
    secondaryContainer  = Color(0xFFDDE3FF),
    onSecondaryContainer= IlcbBlueDark,

    tertiary            = IlcbGold,
    onTertiary          = IlcbOnBrand,
    tertiaryContainer   = Color(0xFFF5EDD8),
    onTertiaryContainer = Color(0xFF3B2A00),

    error               = IlcbError,
    onError             = IlcbOnBrand,
    errorContainer      = Color(0xFFFFDAD6),
    onErrorContainer    = Color(0xFF410002),

    background          = IlcbBackground,
    onBackground        = IlcbOnSurface,

    surface             = IlcbSurface,
    onSurface           = IlcbOnSurface,
    surfaceVariant      = IlcbSurface,
    onSurfaceVariant    = Color(0xFF7B6F72),

    outline             = Color(0xFFBDBDBD),
    outlineVariant      = Color(0xFFE0E0E0),
)

val IlcbDarkColorScheme = darkColorScheme(
    primary             = IlcbBlueDarkDm,
    onPrimary           = IlcbOnBrand,
    primaryContainer    = Color.Black,
    onPrimaryContainer  = IlcbOnBrand,

    secondary           = IlcbBlueMidDm,
    onSecondary         = IlcbOnBrand,
    secondaryContainer  = Color(0xFF1A2A4A),
    onSecondaryContainer= IlcbBlueMidDm,

    tertiary            = IlcbGold,
    onTertiary          = IlcbOnBrand,
    tertiaryContainer   = Color(0xFF3B2A00),
    onTertiaryContainer = Color(0xFFF5EDD8),

    error               = IlcbError,
    onError             = IlcbOnBrand,
    errorContainer      = Color(0xFF93000A),
    onErrorContainer    = Color(0xFFFFDAD6),

    background          = Color(0xFF1E1C1C),
    onBackground        = Color.White,

    surface             = Color(0xFF090808),
    onSurface           = Color.White,
    surfaceVariant      = Color(0xFF1E1C1C),
    onSurfaceVariant    = Color(0xFFB0BEC5),

    outline             = Color(0xFF424242),
    outlineVariant      = Color(0xFF303030),
)
