package pe.lecordonbleu.universidadestudiante.presentation.screens.misofertas

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.navigation.NavController
import pe.lecordonbleu.universidadestudiante.getColorsTheme
import pe.lecordonbleu.universidadestudiante.getPlatformContext
import pe.lecordonbleu.universidadestudiante.presentation.components.StandardTopBar
import pe.lecordonbleu.universidadestudiante.util.openUrl

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MisOfertasScreen(navigator: NavController) {
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()
    val colors = getColorsTheme()
    val context = getPlatformContext()

    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        containerColor = colors.backGroundColor,
        topBar = {
            StandardTopBar(
                title = "Mis Ofertas",
                subtitle = "Portal de empleabilidad",
                onBackClick = { navigator.popBackStack() },
                scrollBehavior = scrollBehavior
            )
        }
    ) { paddingValues ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 24.dp, vertical = 32.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            Text(
                text = "Aún no hay ofertas disponibles.",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = colors.textColor,
                textAlign = TextAlign.Center
            )

            Text(
                text = "¿Te encuentras en búsqueda de prácticas o empleo?",
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium,
                color = colors.textColor.copy(alpha = 0.8f),
                textAlign = TextAlign.Center
            )

            Text(
                text = "Accede a nuestro nuevo portal de empleabilidad y entérate de las oportunidades que tenemos para ti:",
                fontSize = 14.sp,
                color = colors.textColor.copy(alpha = 0.6f),
                textAlign = TextAlign.Center
            )

            Text(
                text = "https://lecordonbleu-peru.hosco.com/es/",
                fontSize = 14.sp,
                color = colors.colorMixPrimary,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                modifier = Modifier.clickable {
                    openUrl(context, "https://lecordonbleu-peru.hosco.com/es/")
                }
            )

            Text(
                text = "Si tienes dudas o consultas respecto a tu cuenta, contáctanos al correo:",
                fontSize = 14.sp,
                color = colors.textColor.copy(alpha = 0.6f),
                textAlign = TextAlign.Center
            )

            Text(
                text = "empleabilidad@cordonbleu.edu.pe",
                fontSize = 14.sp,
                color = colors.colorMixPrimary,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                modifier = Modifier.clickable {
                    openUrl(context, "mailto:empleabilidad@cordonbleu.edu.pe")
                }
            )
        }
    }
}
