package pe.lecordonbleu.universidadestudiante.presentation.screens.horario.customcell

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.datetime.LocalTime
import pe.lecordonbleu.universidadestudiante.data.remote.dto.Horario
import pe.lecordonbleu.universidadestudiante.getColorsTheme

@Composable
fun BoxAgendaDiaria(clases: List<Horario>, onExpandClase: (Horario) -> Unit) {
    val horaInicio = 6
    val horaFin = 23
    val alturaTotalDp = 1700.dp

    val minutosTotales = (horaFin - horaInicio) * 60
    val density = LocalDensity.current

    val colors = getColorsTheme()

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .verticalScroll(rememberScrollState())
            .height(alturaTotalDp)
            .background(colors.colorBlancoGris)
    ) {
        BoxWithConstraints(
            modifier = Modifier
                .fillMaxWidth()
                .height(alturaTotalDp)
        ) {
            val escalaPxPorMinuto = with(density) { alturaTotalDp.toPx() / minutosTotales }

            Column(modifier = Modifier.fillMaxSize()) {
                for (hora in horaInicio until horaFin) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(with(density) { (60 * escalaPxPorMinuto).toDp() })
                    ) {
                        Text(
                            text = "${hora.toString().padStart(2, '0')}:00",
                            modifier = Modifier
                                .align(Alignment.TopStart)
                                .padding(start = 8.dp),
                            color = colors.colorGrisNeutro,
                            fontSize = 12.sp
                        )
                        HorizontalDivider(
                            color = colors.colorGrisNeutro.copy(alpha = 0.5f),
                            thickness = 0.5.dp,
                            modifier = Modifier
                                .align(Alignment.BottomStart)
                                .fillMaxWidth()
                        )
                    }
                }
            }

            clases.forEach { clase ->
                val inicio = LocalTime.parse(clase.hora_inicio)
                val fin = LocalTime.parse(clase.hora_fin)

                val minutosDesdeInicio = (inicio.hour * 60 + inicio.minute) - (horaInicio * 60)
                val duracionMin = fin.toSecondOfDay() / 60 - inicio.toSecondOfDay() / 60

                val offsetDp = with(density) { (minutosDesdeInicio * escalaPxPorMinuto).toDp() }
                val heightDp = with(density) { (duracionMin * escalaPxPorMinuto).toDp() }

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .offset(y = offsetDp)
                        .padding(horizontal = 8.dp)
                ) {
                    ClaseCard(
                        item = clase,
                        paddingStart = 42.dp,
                        modifier = Modifier.height(heightDp),
                        onExpand = { onExpandClase(clase) },
                        applyTopPadding = false
                    )
                }
            }
        }
    }
}
