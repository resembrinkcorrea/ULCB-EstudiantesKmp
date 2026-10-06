package pe.lecordonbleu.universidadestudiante.presentation.screens.mallacurricular.customcell

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import pe.lecordonbleu.universidadestudiante.data.remote.dto.ResponseTablaPlan
import pe.lecordonbleu.universidadestudiante.getColorsTheme
import pe.lecordonbleu.universidadestudiante.presentation.vo.ResourceUiState

@Composable
fun TablaPlanEstudioExpandableCell(
    state: ResourceUiState<List<ResponseTablaPlan>>,
    cicloExpandido: Int?,
    onExpandToggle: (Int) -> Unit
) {
    val grupos = when (state) {
        is ResourceUiState.Success -> {
            state.data.firstOrNull()?.ListTablaPlanEstudio?.groupBy { it.CICLO_ACADÉMICO }
                ?: emptyMap()
        }
        else -> emptyMap()
    }

    val colors = getColorsTheme()
    val isDarkMode = isSystemInDarkTheme()
    val colorCicloCompletado = if (isDarkMode) Color(0xFF0D2A40) else Color(0xFFBBDEFB)

    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        grupos.forEach { (ciclo, asignaturas) ->
            val expandido = cicloExpandido == ciclo
            val todosCompletados = asignaturas.all { it.ESTADO_MALLA == "Cursado" }
            val cardColor = if (todosCompletados) colorCicloCompletado else colors.colorPastelAzul

            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = cardColor),
                onClick = { onExpandToggle(ciclo) }
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Ciclo $ciclo",
                        style = MaterialTheme.typography.titleMedium,
                        modifier = Modifier.weight(1f)
                    )
                    Icon(
                        imageVector = if (expandido) Icons.Default.ExpandLess else Icons.Default.ExpandMore,
                        contentDescription = null
                    )
                }
            }

            AnimatedVisibility(visible = expandido) {
                Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                    asignaturas.forEach { asignatura ->
                        TablaAsignaturaItemCell(asignatura)
                    }
                }
            }
        }
    }
}
