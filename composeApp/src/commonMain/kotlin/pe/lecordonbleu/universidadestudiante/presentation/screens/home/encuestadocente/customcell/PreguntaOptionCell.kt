package pe.lecordonbleu.universidadestudiante.presentation.screens.home.encuestadocente.customcell

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.RadioButton
import androidx.compose.material.RadioButtonDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import pe.lecordonbleu.universidadestudiante.presentation.screens.home.encuestasatisfaccion.helpers.PreguntaVM

private val azul = Color(0xFF0D47A1)

@Composable
fun PreguntaOptionCell(
    pregunta: PreguntaVM,
    seleccionAlternativaId: Int?,
    textoActual: String,
    onAlternativaSeleccionada: (Int) -> Unit,
    onTextoChanged: (String) -> Unit,
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
            .padding(vertical = 8.dp)
            .background(backgroundColor, RoundedCornerShape(8.dp))
            .padding(8.dp)
    ) {
        Row(verticalAlignment = Alignment.Top) {
            Box(
                modifier = Modifier
                    .size(24.dp)
                    .background(if (enabled) azul else Color.Gray, CircleShape),
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

        Spacer(Modifier.height(4.dp))

        pregunta.opciones.forEach { alt ->
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable(enabled = enabled) { onAlternativaSeleccionada(alt.id) }
                    .padding(vertical = 4.dp)
            ) {
                RadioButton(
                    selected = seleccionAlternativaId == alt.id,
                    onClick = { onAlternativaSeleccionada(alt.id) },
                    enabled = enabled,
                    colors = RadioButtonDefaults.colors(
                        selectedColor = azul,
                        unselectedColor = if (enabled) Color.Gray else Color.LightGray
                    )
                )
                Spacer(Modifier.width(4.dp))
                Text(text = alt.titulo, color = if (enabled) Color.Black else Color.Gray, fontSize = 11.sp)

                if (alt.flagTexto == 1) {
                    val coroutineScope = rememberCoroutineScope()
                    var debounceJob by remember { mutableStateOf<Job?>(null) }
                    var textoTemp by remember(pregunta.id) { mutableStateOf(textoActual) }

                    LaunchedEffect(seleccionAlternativaId) {
                        if (alt.id != seleccionAlternativaId) textoTemp = ""
                    }

                    OutlinedTextField(
                        value = textoTemp,
                        onValueChange = { nuevo ->
                            textoTemp = nuevo
                            debounceJob?.cancel()
                            debounceJob = coroutineScope.launch {
                                delay(400)
                                onTextoChanged(nuevo)
                            }
                        },
                        label = { Text("escribe tu respuesta..") },
                        enabled = enabled && alt.id == seleccionAlternativaId,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = 32.dp, bottom = 8.dp)
                    )
                }
            }
        }
    }
}
