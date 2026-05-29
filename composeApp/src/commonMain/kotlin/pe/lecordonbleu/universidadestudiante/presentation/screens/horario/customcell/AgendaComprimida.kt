package pe.lecordonbleu.universidadestudiante.presentation.screens.horario.customcell

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.datetime.LocalTime
import pe.lecordonbleu.universidadestudiante.data.remote.dto.Horario
import pe.lecordonbleu.universidadestudiante.getColorsTheme

@Composable
fun AgendaComprimida(clases: List<Horario>, onExpandClase: (Horario) -> Unit) {
    val colors          = getColorsTheme()
    val horaInicio      = 6
    val horaFin         = 23
    val alturaVaciaF    = 24f
    val alturaClaseF    = 100f

    val horasConClase = remember(clases) {
        buildSet {
            clases.forEach { c ->
                val ini = LocalTime.parse(c.hora_inicio).hour
                val fin = LocalTime.parse(c.hora_fin).let { t -> t.hour + if (t.minute > 0) 1 else 0 }
                for (h in ini until fin) add(h)
            }
        }
    }

    val yPerHour = remember(horasConClase) {
        buildMap {
            var y = 0f
            for (h in horaInicio..horaFin) {
                put(h, y)
                if (h < horaFin) y += if (h in horasConClase) alturaClaseF else alturaVaciaF
            }
        }
    }
    val totalAltura = (yPerHour[horaFin] ?: 0f).dp

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .verticalScroll(rememberScrollState())
            .background(colors.colorBlancoGris)
    ) {
        BoxWithConstraints(
            modifier = Modifier
                .fillMaxWidth()
                .height(totalAltura)
        ) {
            Column(modifier = Modifier.fillMaxWidth()) {
                for (h in horaInicio until horaFin) {
                    val slotH = if (h in horasConClase) alturaClaseF.dp else alturaVaciaF.dp
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(slotH)
                            .padding(start = 8.dp),
                        verticalAlignment = Alignment.Top
                    ) {
                        Text(
                            text     = "${h.toString().padStart(2, '0')}:00",
                            color    = colors.colorGrisNeutro,
                            fontSize = 12.sp,
                            modifier = Modifier.width(40.dp)
                        )
                        HorizontalDivider(
                            color     = colors.colorGrisNeutro.copy(alpha = 0.3f),
                            thickness = 0.5.dp
                        )
                    }
                }
            }

            clases.forEach { clase ->
                val inicio     = LocalTime.parse(clase.hora_inicio)
                val fin        = LocalTime.parse(clase.hora_fin)
                val durMinutos = fin.toSecondOfDay() / 60 - inicio.toSecondOfDay() / 60
                val yHora      = yPerHour[inicio.hour] ?: 0f
                val fracOffset = (inicio.minute / 60f) * alturaClaseF
                val offsetDp   = (yHora + fracOffset).dp
                val cardHeight = (alturaClaseF / 60f * durMinutos).dp

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .offset(y = offsetDp)
                        .padding(horizontal = 8.dp)
                ) {
                    ClaseCard(
                        item            = clase,
                        paddingStart    = 42.dp,
                        modifier        = Modifier.height(cardHeight),
                        onExpand        = { onExpandClase(clase) },
                        applyTopPadding = false
                    )
                }
            }
        }
    }
}
