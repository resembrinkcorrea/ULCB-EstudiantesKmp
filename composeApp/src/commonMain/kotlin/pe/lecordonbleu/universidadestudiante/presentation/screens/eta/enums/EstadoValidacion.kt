package pe.lecordonbleu.universidadestudiante.presentation.screens.eta.enums

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AttachFile
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Details
import androidx.compose.material.icons.outlined.Block
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import pe.lecordonbleu.universidadestudiante.core.theme.IlcbError
import pe.lecordonbleu.universidadestudiante.core.theme.IlcbGreenDark
import pe.lecordonbleu.universidadestudiante.core.theme.IlcbWarning

enum class EstadoValidacion(val label: String, val icon: ImageVector, val color: Color) {
    PENDIENTE("Pendiente", Icons.Default.AttachFile, Color.Gray),
    VALIDADO("Validado", Icons.Default.CheckCircle, IlcbGreenDark),
    RECHAZADO("Rechazado", Icons.Outlined.Block, IlcbError),
    OBSERVADO("Observado", Icons.Default.Details, IlcbWarning);

    companion object {
        fun fromHtml(html: String?): EstadoValidacion {
            if (html == null) return PENDIENTE
            return when {
                html.contains("Validado", ignoreCase = true) -> VALIDADO
                html.contains("Rechazado", ignoreCase = true) -> RECHAZADO
                html.contains("Observado", ignoreCase = true) -> OBSERVADO
                else -> PENDIENTE
            }
        }
    }
}
