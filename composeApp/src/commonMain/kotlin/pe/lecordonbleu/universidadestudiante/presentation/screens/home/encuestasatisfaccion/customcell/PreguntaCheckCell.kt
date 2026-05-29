package pe.lecordonbleu.universidadestudiante.presentation.screens.home.encuestasatisfaccion.customcell

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Checkbox
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
fun PreguntaCheckCell(
    pregunta: PreguntaVM,
    seleccionadas: Set<Int>,
    textosPorAlternativa: Map<Int, String>,
    onAlternativaChanged: (Int, Boolean) -> Unit,
    onTextoChanged: (Int, String) -> Unit,
    enabled: Boolean
) {
    val colors = getColorsTheme()
    val colorScheme = MaterialTheme.colorScheme
    val contestada = seleccionadas.isNotEmpty()

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
                            pregunta.numero.toString(),
                            style = MaterialTheme.typography.labelLarge,
                            color = colorScheme.onPrimary
                        )
                    }
                }
                Spacer(Modifier.width(10.dp))
                Text(
                    buildAnnotatedString {
                        append(pregunta.titulo)
                        if (pregunta.obligatorio) {
                            withStyle(SpanStyle(color = colors.colorRojo)) { append(" *") }
                        }
                    },
                    style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.SemiBold),
                    color = if (enabled) colorScheme.onSurface else colorScheme.onSurfaceVariant
                )
            }

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

                Column {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable(enabled = enabled) {
                                onAlternativaChanged(alt.id, !seleccionadas.contains(alt.id))
                            }
                            .padding(vertical = 2.dp)
                    ) {
                        Checkbox(
                            checked = seleccionadas.contains(alt.id),
                            onCheckedChange = { onAlternativaChanged(alt.id, it) },
                            enabled = enabled
                        )
                        Spacer(Modifier.width(8.dp))
                        Text(
                            text = alt.titulo,
                            style = MaterialTheme.typography.bodyMedium,
                            color = if (enabled) colorScheme.onSurface else colorScheme.onSurfaceVariant
                        )
                    }

                    if (alt.flagTexto == 1) {
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
                            label = { Text("Detalle adicional") },
                            enabled = enabled && seleccionadas.contains(alt.id),
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(start = 52.dp)
                        )
                    }
                }
            }
        }
    }
}
