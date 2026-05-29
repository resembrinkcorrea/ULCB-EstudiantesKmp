package pe.lecordonbleu.universidadestudiante.presentation.screens.notas.helpers

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import pe.lecordonbleu.universidadestudiante.LoadingIndicator
import pe.lecordonbleu.universidadestudiante.data.remote.dto.ListadoNotasDetalle
import pe.lecordonbleu.universidadestudiante.data.remote.dto.TareaAcad
import pe.lecordonbleu.universidadestudiante.getColorsTheme
import pe.lecordonbleu.universidadestudiante.presentation.screens.notas.customcell.PromedioConsolidadoCard
import pe.lecordonbleu.universidadestudiante.presentation.screens.notas.customcell.SubTabCard
import pe.lecordonbleu.universidadestudiante.presentation.screens.notas.customcell.TareaAcadCell
import pe.lecordonbleu.universidadestudiante.presentation.screens.notas.uibuilder.GrupoPestanaTarea

@Composable
fun ContenidoTareasPanel(
    grupo       : GrupoPestanaTarea?,
    tareasPorId : Map<Int, List<TareaAcad>>,
    detalle     : ListadoNotasDetalle?,
    isLoading   : Boolean
) {
    val colors = getColorsTheme()

    var selectedSubTab by remember { mutableStateOf(0) }

    val hasSubTabs = (grupo?.idsTeoria?.isNotEmpty() == true) && (grupo.idsPractica.isNotEmpty())

    val tareasActuales: List<TareaAcad> = when {
        hasSubTabs && selectedSubTab == 0 -> grupo?.idsTeoria?.flatMap   { tareasPorId[it] ?: emptyList() } ?: emptyList()
        hasSubTabs && selectedSubTab == 1 -> grupo?.idsPractica?.flatMap { tareasPorId[it] ?: emptyList() } ?: emptyList()
        else                              -> grupo?.idsGeneral?.flatMap  { tareasPorId[it] ?: emptyList() } ?: emptyList()
    }

    val tareasTeoria   = grupo?.idsTeoria?.flatMap   { tareasPorId[it] ?: emptyList() } ?: emptyList()
    val tareasPractica = grupo?.idsPractica?.flatMap { tareasPorId[it] ?: emptyList() } ?: emptyList()
    val tareasGeneral  = grupo?.idsGeneral?.flatMap  { tareasPorId[it] ?: emptyList() } ?: emptyList()

    val promedTeoria   = tareasTeoria.firstOrNull()?.matric_not_det_prom ?: ""
    val pesoTeoria     = (tareasTeoria.firstOrNull()?.peso_act_det?.toDoubleOrNull()?.times(100))?.toInt()
    val promedPractica = tareasPractica.firstOrNull()?.matric_not_det_prom ?: ""
    val pesoPractica   = (tareasPractica.firstOrNull()?.peso_act_det?.toDoubleOrNull()?.times(100))?.toInt()

    if (isLoading) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            LoadingIndicator(modifier = Modifier.size(50.dp))
        }
    } else {
        LazyColumn(
            modifier            = Modifier.fillMaxSize().padding(horizontal = 16.dp, vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Card promedio consolidado
            item {
                PromedioConsolidadoCard(
                    tareasTeoria   = tareasTeoria,
                    tareasPractica = tareasPractica,
                    tareasGeneral  = tareasGeneral,
                    nombreGrupo    = grupo?.nombrePestana ?: ""
                )
            }

            // Sub-tabs T / P con promedio y peso dentro
            if (hasSubTabs) {
                item {
                    Row(
                        modifier              = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        SubTabCard(title = "TEORIA",   prom = promedTeoria,   peso = pesoTeoria,   isSelected = selectedSubTab == 0, modifier = Modifier.weight(1f)) { selectedSubTab = 0 }
                        SubTabCard(title = "PRACTICA", prom = promedPractica, peso = pesoPractica, isSelected = selectedSubTab == 1, modifier = Modifier.weight(1f)) { selectedSubTab = 1 }
                    }
                }
            }

            // Lista
            items(tareasActuales) { tarea ->
                TareaAcadCell(item = tarea)
            }
        }
    }
}

