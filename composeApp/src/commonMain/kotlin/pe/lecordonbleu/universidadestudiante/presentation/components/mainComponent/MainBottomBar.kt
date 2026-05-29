package pe.lecordonbleu.universidadestudiante.presentation.components.mainComponent

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.FactCheck
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.QrCode
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.lerp
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import pe.lecordonbleu.universidadestudiante.DarkModeColors

@Composable
fun MainBottomBar(
    currentTab: String,
    colors: DarkModeColors,
    onTabSelected: (String) -> Unit
) {
    Box(
        modifier = Modifier
            .padding(start = 16.dp, end = 16.dp, bottom = 24.dp)
            .shadow(
                elevation = 16.dp,
                shape = RoundedCornerShape(24.dp),
                spotColor = Color.Black.copy(alpha = 0.5f)
            )
            .clip(RoundedCornerShape(24.dp))
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        colors.colorAzulOscuro,
                        lerp(colors.colorAzulOscuro, Color.White, 0.15f)
                    )
                )
            )
            .border(
                width = 1.dp,
                brush = Brush.verticalGradient(
                    colors = listOf(
                        Color.White.copy(alpha = 0.2f),
                        Color.Transparent,
                        Color.White.copy(alpha = 0.05f)
                    )
                ),
                shape = RoundedCornerShape(24.dp)
            )
    ) {
        NavigationBar(
            containerColor = Color.Transparent,
            contentColor = Color.White,
            tonalElevation = 0.dp,
            windowInsets = WindowInsets(0.dp, 0.dp, 0.dp, 0.dp),
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 6.dp)
                .background(Color.Transparent)
        ) {
            val navColors = NavigationBarItemDefaults.colors(
                selectedIconColor = if (isSystemInDarkTheme()) colors.colorMixPrimary else Color.White,
                selectedTextColor = if (isSystemInDarkTheme()) colors.colorMixPrimary else Color.White,
                indicatorColor = if (isSystemInDarkTheme()) colors.colorMixPrimary.copy(alpha = 0.15f) else Color.White.copy(alpha = 0.15f),
                unselectedIconColor = Color.White.copy(alpha = 0.5f),
                unselectedTextColor = Color.White.copy(alpha = 0.5f)
            )

            NavigationBarItem(
                icon = { Icon(Icons.Default.Home, contentDescription = "Inicio") },
                label = { Text(text = "Inicio", fontWeight = FontWeight.SemiBold, fontSize = 11.sp) },
                selected = currentTab == "inicio",
                onClick = { onTabSelected("inicio") },
                colors = navColors
            )

            NavigationBarItem(
                icon = { Icon(Icons.Default.QrCode, contentDescription = "QR") },
                label = { Text(text = "QR", fontWeight = FontWeight.SemiBold, fontSize = 11.sp) },
                selected = currentTab == "qr",
                onClick = { onTabSelected("qr") },
                colors = navColors
            )

            NavigationBarItem(
                icon = { Icon(Icons.AutoMirrored.Filled.FactCheck, contentDescription = "Asistencia") },
                label = { Text(text = "Asistencia", fontWeight = FontWeight.SemiBold, fontSize = 11.sp) },
                selected = currentTab == "asistencia",
                onClick = { onTabSelected("asistencia") },
                colors = navColors
            )

            NavigationBarItem(
                icon = { Icon(Icons.Default.CalendarMonth, contentDescription = "Horario") },
                label = { Text(text = "Horario", fontWeight = FontWeight.SemiBold, fontSize = 11.sp) },
                selected = currentTab == "horario",
                onClick = { onTabSelected("horario") },
                colors = navColors
            )
        }
    }
}
