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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import pe.lecordonbleu.universidadestudiante.getColorsTheme
import pe.lecordonbleu.universidadestudiante.presentation.screens.home.encuestasatisfaccion.helpers.PreguntaVM

@Composable
fun PreguntaOptionLinealCell(
    pregunta: PreguntaVM,
    seleccionAlternativaId: Int?,
    onAlternativaSeleccionada: (Int) -> Unit,
    enabled: Boolean
) {
    val colors = getColorsTheme()
    val colorScheme = MaterialTheme.colorScheme
    val contestada = seleccionAlternativaId != null

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

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 4.dp),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {
                pregunta.opciones.forEach { alt ->
                    Surface(
                        shape = RoundedCornerShape(18.dp),
                        color = if (seleccionAlternativaId == alt.id) {
                            colors.colorCardRespondida
                        } else {
                            colors.colorCardNoRespondida
                        }
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier
                                .clickable(enabled = enabled) { onAlternativaSeleccionada(alt.id) }
                                .padding(vertical = 10.dp, horizontal = 12.dp)
                        ) {
                            Text(
                                text = alt.titulo,
                                style = MaterialTheme.typography.titleMedium,
                                color = if (enabled) colorScheme.onSurface else colorScheme.onSurfaceVariant
                            )
                            RadioButton(
                                selected = seleccionAlternativaId == alt.id,
                                onClick = { onAlternativaSeleccionada(alt.id) },
                                enabled = enabled,
                                modifier = Modifier.size(22.dp),
                                colors = RadioButtonDefaults.colors(
                                    selectedColor = colors.colorAzulContraste,
                                    unselectedColor = colors.colorOptionNoSeleccionado
                                )
                            )
                        }
                    }
                }
            }
        }
    }
}
