package pe.lecordonbleu.universidadestudiante.presentation.screens.home.encuestadocente.customcell

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Checkbox
import androidx.compose.material.OutlinedTextField
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

private val azulCheck = Color(0xFF0D47A1)

@Composable
fun PreguntaCheckCell(
    pregunta: PreguntaVM,
    seleccionadas: Set<Int>,
    textosPorAlternativa: Map<Int, String>,
    onAlternativaChanged: (Int, Boolean) -> Unit,
    onTextoChanged: (Int, String) -> Unit,
    enabled: Boolean
) {
    val contestada = seleccionadas.isNotEmpty()
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
                    .background(if (enabled) azulCheck else Color.Gray, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Text(pregunta.numero.toString(), color = Color.White, fontSize = 12.sp)
            }
            Spacer(Modifier.width(8.dp))
            Text(
                buildAnnotatedString {
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
            var textoTemp by remember(pregunta.id, alt.id) {
                mutableStateOf(textosPorAlternativa[alt.id] ?: "")
            }
            val coroutineScope = rememberCoroutineScope()
            var debounceJob by remember { mutableStateOf<Job?>(null) }

            LaunchedEffect(textosPorAlternativa[alt.id], enabled, seleccionadas.contains(alt.id)) {
                if (!enabled || !seleccionadas.contains(alt.id)) {
                    textoTemp = ""
                } else {
                    val externo = textosPorAlternativa[alt.id] ?: ""
                    if (textoTemp != externo) textoTemp = externo
                }
            }

            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable(enabled = enabled) {
                        onAlternativaChanged(alt.id, !seleccionadas.contains(alt.id))
                    }
                    .padding(vertical = 4.dp)
            ) {
                Checkbox(
                    checked = seleccionadas.contains(alt.id),
                    onCheckedChange = { onAlternativaChanged(alt.id, it) },
                    enabled = enabled
                )
                Spacer(Modifier.width(8.dp))
                Text(text = alt.titulo, color = if (enabled) Color.Black else Color.Gray, fontSize = 11.sp)

                if (alt.flagTexto == 1) {
                    Spacer(Modifier.width(8.dp))
                    OutlinedTextField(
                        value = textoTemp,
                        onValueChange = { nuevo ->
                            textoTemp = nuevo
                            debounceJob?.cancel()
                            debounceJob = coroutineScope.launch {
                                delay(400)
                                onTextoChanged(alt.id, nuevo)
                            }
                        },
                        label = { Text("Otros") },
                        enabled = enabled && seleccionadas.contains(alt.id),
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        }
    }
}
