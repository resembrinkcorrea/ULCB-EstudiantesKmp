package pe.lecordonbleu.universidadestudiante.presentation.screens.cuentacorriente

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.navigation.NavController
import androidx.navigation.navOptions
import pe.lecordonbleu.universidadestudiante.WebViewComposable
import pe.lecordonbleu.universidadestudiante.core.config.Constantes
import pe.lecordonbleu.universidadestudiante.presentation.components.StandardTopBar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PagoFlywireScreen(
    codTransaccion: String,
    rutaRetorno: String,
    navigator: NavController
) {
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()
    val url = "${Constantes.RETURN_DOMAIN}.${Constantes.BASE_UNEG}.edu.pe/pages/$codTransaccion"

    fun volver() {
        navigator.navigate("/$rutaRetorno", navOptions {
            popUpTo("/$rutaRetorno") { inclusive = true }
        })
    }

    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            StandardTopBar(
                title = "ILCB - Flywire",
                subtitle = "Cuenta Corriente",
                onBackClick = { volver() },
                scrollBehavior = scrollBehavior
            )
        }
    ) { innerPadding ->
        WebViewComposable(
            url = url,
            returnDomain = Constantes.RETURN_DOMAIN,
            onClose = { volver() },
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        )
    }
}
