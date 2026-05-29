package pe.lecordonbleu.universidadestudiante.presentation.screens.home.encuestasatisfaccion.customcell

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import pe.lecordonbleu.universidadestudiante.getColorsTheme
import pe.lecordonbleu.universidadestudiante.presentation.screens.home.encuestasatisfaccion.helpers.PreguntaVM

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
    val colors = getColorsTheme()
    val colorScheme = MaterialTheme.colorScheme
    val contestada = textoTemp.isNotBlank()

    LaunchedEffect(textoActual, enabled) {
        if (!enabled) {
            textoTemp = ""
        } else if (textoTemp != textoActual) {
            textoTemp = textoActual
        }
    }

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 8.dp),
        shape = RoundedCornerShape(22.dp),
        color = when {
            !enabled -> colors.colorCardDeshabilitada
            contestada -> colors.colorCardRespondida
            else -> colors.colorCardNoRespondida
        },
        tonalElevation = 0.dp,
        shadowElevation = 0.dp
    ) {
        Column(
            modifier = Modifier.padding(18.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            Row(verticalAlignment = Alignment.Top) {
                Surface(
                    modifier = Modifier.size(30.dp),
                    shape = RoundedCornerShape(10.dp),
                    color = if (enabled) colorScheme.primary else colorScheme.outlineVariant
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Text(
                            text = pregunta.numero.toString(),
                            style = MaterialTheme.typography.labelLarge,
                            color = colorScheme.onPrimary
                        )
                    }
                }
                Spacer(Modifier.width(10.dp))
                Text(
                    text = buildAnnotatedString {
                        append(pregunta.titulo)
                        if (pregunta.obligatorio) {
                            withStyle(SpanStyle(color = colors.colorRojo)) { append(" *") }
                        }
                    },
                    style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.SemiBold),
                    color = if (enabled) colorScheme.onSurface else colorScheme.onSurfaceVariant
                )
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
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(min = 96.dp),
                enabled = enabled,
                placeholder = {
                    Text(
                        "Escribe tu respuesta aqui...",
                        style = MaterialTheme.typography.bodyMedium
                    )
                },
                singleLine = false,
                maxLines = 5
            )
        }
    }
}
