package pe.lecordonbleu.universidadestudiante

import androidx.compose.ui.window.ComposeUIViewController
import org.koin.core.context.startKoin
import pe.lecordonbleu.universidadestudiante.core.di.appModule

fun MainViewController() = ComposeUIViewController { App() }

fun doInitKoin() {
    startKoin {
        modules(appModule())
    }
}