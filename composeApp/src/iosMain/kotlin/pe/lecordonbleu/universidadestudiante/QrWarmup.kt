package pe.lecordonbleu.universidadestudiante.presentation.screens.qr

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

fun warmUpQrGenerator() {
    CoroutineScope(Dispatchers.Default).launch {
        qrgenerator.generateCode("warmup")
    }
}
