package pe.lecordonbleu.universidadestudiante.presentation.screens.home.encuestadocente.customcell

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.RadioButton
import androidx.compose.material.RadioButtonDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import pe.lecordonbleu.universidadestudiante.presentation.screens.home.encuestasatisfaccion.helpers.PreguntaVM

private val azulLineal = Color(0xFF0D47A1)

@Composable
fun PreguntaOptionLinealCell(
    pregunta: PreguntaVM,
    seleccionAlternativaId: Int?,
    onAlternativaSeleccionada: (Int) -> Unit,
    enabled: Boolean
) {
    val contestada = seleccionAlternativaId != null
    val backgroundColor = when {
        !enabled -> Color.Transparent
        contestada -> Color.Green.copy(alpha = 0.08f)
        else -> Color.Red.copy(alpha = 0.05f)
    }

    Column(
        modifier = Modifier
            .padding(vertical = 4.dp)
            .background(backgroundColor, RoundedCornerShape(8.dp))
            .padding(4.dp)
    ) {
        Row(verticalAlignment = Alignment.Top) {
            Box(
                modifier = Modifier
                    .size(24.dp)
                    .background(if (enabled) azulLineal else Color.Gray, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Text(text = pregunta.numero.toString(), color = Color.White, fontSize = 12.sp)
            }
            Spacer(Modifier.width(8.dp))
            Text(
                text = buildAnnotatedString {
                    append(pregunta.titulo)
                    if (pregunta.obligatorio) {
                        withStyle(SpanStyle(color = Color.Red)) { append(" (*)") }
                    }
                },
                fontSize = 13.sp,
                color = if (enabled) Color.Black else Color.Gray
            )
        }

        Spacer(Modifier.height(6.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            pregunta.opciones.forEach { alt ->
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .clickable(enabled = enabled) { onAlternativaSeleccionada(alt.id) }
                        .padding(horizontal = 1.dp)
                        .border(1.dp, Color.LightGray, RoundedCornerShape(6.dp))
                        .padding(vertical = 5.dp, horizontal = 5.dp)
                ) {
                    Text(text = alt.titulo, color = if (enabled) Color.Black else Color.Gray, fontSize = 14.sp)
                    RadioButton(
                        selected = seleccionAlternativaId == alt.id,
                        onClick = { onAlternativaSeleccionada(alt.id) },
                        enabled = enabled,
                        modifier = Modifier.size(18.dp),
                        colors = RadioButtonDefaults.colors(
                            selectedColor = azulLineal,
                            unselectedColor = if (enabled) Color.Gray else Color.LightGray
                        )
                    )
                }
            }
        }
    }
}
