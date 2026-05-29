package pe.lecordonbleu.universidadestudiante.presentation.screens.mallacurricular.customcell

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import pe.lecordonbleu.universidadestudiante.data.remote.dto.ListTablaPlanEstudio

@Composable
fun GraficaMallaCurricularCell(
    grupos: Map<Int, List<ListTablaPlanEstudio>>,
    coloresAsignaturas: Map<String, Color>
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(12.dp)
    ) {
        LazyRow(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
            grupos.forEach { (ciclo, asignaturas) ->
                item {
                    GraficaItemCicloColumnCell(
                        ciclo = ciclo,
                        asignaturas = asignaturas,
                        coloresAsignaturas = coloresAsignaturas
                    )
                }
            }
        }
    }
}
