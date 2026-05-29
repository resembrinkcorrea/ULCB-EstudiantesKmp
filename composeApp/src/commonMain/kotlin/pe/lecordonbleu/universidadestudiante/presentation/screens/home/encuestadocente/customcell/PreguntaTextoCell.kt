package pe.lecordonbleu.universidadestudiante.presentation.screens.home.encuestadocente.customcell

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
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

private val azulTexto = Color(0xFF0D47A1)

@Composable
fun PreguntaTextoCell(
    pregunta: PreguntaVM,
    textoActual: String,
    onTextoChanged: (String) -> Unit,
    enabled: Boolean
) {
    var textoTemp by remember(pregunta.id) { mutableStateOf(textoActual) }
    val coroutineScope = rememberCoroutineScope()
    var debounceJob by remember { mutableStateOf<Job?>(null) }

    val contestada = textoTemp.isNotBlank()

    LaunchedEffect(textoActual, enabled) {
        if (!enabled) {
            textoTemp = ""
        } else if (textoTemp != textoActual) {
            textoTemp = textoActual
        }
    }

    val backgroundColor = when {
        !enabled -> Color.LightGray.copy(alpha = 0.1f)
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
                    .background(azulTexto, CircleShape),
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
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(min = 60.dp),
            enabled = enabled,
            placeholder = { Text("escribe tu respuesta aquí...", fontSize = 12.sp) },
            singleLine = false,
            maxLines = 5
        )
    }
}
