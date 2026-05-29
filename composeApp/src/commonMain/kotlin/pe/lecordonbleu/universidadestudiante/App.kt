package pe.lecordonbleu.universidadestudiante

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.rememberNavController
import org.koin.compose.KoinContext
import pe.lecordonbleu.universidadestudiante.presentation.navigation.Navigation

@Composable
@Preview
fun App() {
    KoinContext {
        AppTheme {
            val navigator = rememberNavController()

            Scaffold(
                modifier = Modifier.fillMaxSize()
            ) {
                Navigation(navigator)
            }
        }
    }
}
