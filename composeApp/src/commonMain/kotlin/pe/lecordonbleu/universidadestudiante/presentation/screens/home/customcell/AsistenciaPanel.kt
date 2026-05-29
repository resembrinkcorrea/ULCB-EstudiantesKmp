package pe.lecordonbleu.universidadestudiante.presentation.screens.home.customcell

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import pe.lecordonbleu.universidadestudiante.data.remote.dto.ListClaseHabilitada

@Composable
fun AsistenciaPanel(
    isVisible: Boolean,
    onToggle: () -> Unit,
    data: List<ListClaseHabilitada>,
    modifier: Modifier = Modifier
) {

    if (data.isEmpty()) return
    val clase = data.first()
    val fecha = clase.hora_inicio.split(" ").firstOrNull() ?: ""
    val horaInicio = clase.hora_inicio.split(" ").getOrNull(1)?.replace(".0", "") ?: ""
    val horaFin = clase.hora_fin.split(" ").getOrNull(1)?.replace(".0", "") ?: ""
    val rango = "$horaInicio - $horaFin"

    BoxWithConstraints(modifier = modifier) {
        val screenWidth = maxWidth
        val offset = screenWidth * 0.25f

        AnimatedVisibility(
            visible = isVisible,
            enter = slideInHorizontally(initialOffsetX = { offset.value.toInt() }),
            exit = slideOutHorizontally(targetOffsetX = { offset.value.toInt() }),
            modifier = Modifier.align(Alignment.BottomEnd)
        ) {
            Box(
                modifier = Modifier
                    .wrapContentSize()
                    .clip(RoundedCornerShape(16.dp))
                    .background(Color.Black.copy(alpha = 0.85f))
                    .border(2.dp, Color.DarkGray, shape = RoundedCornerShape(16.dp))
                    .padding(16.dp)
            ) {
                Column(
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = "Asistencia habilitada:",
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp
                    )

                    if (data.isNotEmpty()) {
                        Text("Inicio: ${clase.hora_inicio}", color = Color.White, fontSize = 14.sp)
                        Text("Fin: ${clase.hora_fin}", color = Color.White, fontSize = 14.sp)
                    }
                }
            }
        }
    }
}
