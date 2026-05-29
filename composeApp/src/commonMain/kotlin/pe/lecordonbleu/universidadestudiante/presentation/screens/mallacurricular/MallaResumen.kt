package pe.lecordonbleu.universidadestudiante.presentation.screens.mallacurricular

import androidx.compose.runtime.Stable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector

@Stable
data class MallaResumen(
    val titulo: String,
    val valor: String,
    val color: Color,
    val icono: ImageVector
)
