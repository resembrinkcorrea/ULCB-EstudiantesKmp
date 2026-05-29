package pe.lecordonbleu.universidadestudiante.presentation.screens.onboarding

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.navigation.NavController
import pe.lecordonbleu.universidadestudiante.getSettingsStorage
import pe.lecordonbleu.universidadestudiante.presentation.components.Carousel

@Composable
fun OnBoardingScreen(navigator: NavController) {
    val settingsStorage = getSettingsStorage()
    var carouselFinished by remember { mutableStateOf(false) }
    val session = settingsStorage.getInt("Session", -1)

    LaunchedEffect(session) {
        if (session != -1) {
            navigator.navigate("/login") {
                popUpTo(navigator.graph.startDestinationRoute ?: "/") { inclusive = true }
                launchSingleTop = true
            }
        }
    }

    if (session == -1) {
        if (carouselFinished) {
            navigator.navigate("/login") {
                popUpTo(navigator.graph.startDestinationRoute ?: "/") { inclusive = true }
                launchSingleTop = true
            }
        } else {
            Carousel { carouselFinished = true }
        }
    }
}
