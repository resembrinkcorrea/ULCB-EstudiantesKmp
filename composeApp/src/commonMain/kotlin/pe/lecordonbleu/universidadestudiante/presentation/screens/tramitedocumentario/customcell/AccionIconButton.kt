package pe.lecordonbleu.universidadestudiante.presentation.screens.tramitedocumentario.customcell

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp

@Composable
fun AccionIconButton(
    icono: ImageVector,
    colorFondo: Color,
    descripcion: String,
    onClick: () -> Unit,
    habilitado: Boolean = true
) {
    val fondo = if (habilitado) colorFondo else Color.LightGray

    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Box(
            modifier = Modifier
                .size(40.dp)
                .clip(RoundedCornerShape(8.dp))
                .background(fondo)
                .then(if (habilitado) Modifier.clickable { onClick() } else Modifier),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icono,
                contentDescription = descripcion,
                tint = Color.White,
                modifier = Modifier.size(20.dp)
            )
        }
    }
}
