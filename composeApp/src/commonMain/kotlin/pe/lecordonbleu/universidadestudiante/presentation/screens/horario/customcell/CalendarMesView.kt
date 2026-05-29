package pe.lecordonbleu.universidadestudiante.presentation.screens.horario.customcell

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.LocalDate
import kotlinx.datetime.minus
import kotlinx.datetime.plus
import pe.lecordonbleu.universidadestudiante.getColorsTheme
import pe.lecordonbleu.universidadestudiante.getTodayLocalDate

@Composable
fun CalendarMesView(
    selectedDate: LocalDate,
    onDateSelected: (LocalDate) -> Unit,
    diasConClase: List<LocalDate>,
    mesVisual: LocalDate
) {
    val colors = getColorsTheme()

    val today = remember { getTodayLocalDate() }

    val primerDiaMes = LocalDate.parse("${mesVisual.year}-${mesVisual.monthNumber.toString().padStart(2, '0')}-01")
    val diaSemana = (primerDiaMes.dayOfWeek.ordinal + 1) % 7
    val inicio = primerDiaMes.minus(diaSemana.toLong(), DateTimeUnit.DAY)

    val diasMes = List(42) { i -> inicio.plus(i.toLong(), DateTimeUnit.DAY) }

    BoxWithConstraints(
        modifier = Modifier
            .fillMaxWidth()
            .background(colors.backGroundColor)
    ) {
        val isTablet = maxWidth > 600.dp
        val circleSize = if (isTablet) 10.dp else 6.dp

        LazyVerticalGrid(
            columns = GridCells.Fixed(7),
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(min = 320.dp, max = 800.dp),
            horizontalArrangement = Arrangement.SpaceAround,
            verticalArrangement = Arrangement.Top,
            userScrollEnabled = false
        ) {
            items(diasMes) { dia ->
                val esDelMes =
                    dia.monthNumber == mesVisual.monthNumber && dia.year == mesVisual.year
                val esHoy = dia == today
                val esSeleccionado = dia == selectedDate
                val tieneClase = diasConClase.contains(dia)

                val fondo = when {
                    esSeleccionado -> colors.colorPurpura
                    esHoy -> colors.colorGrisAzulado
                    else -> Color.Transparent
                }

                val colorTexto = when {
                    !esDelMes -> colors.colorGrisNeutro
                    esSeleccionado || esHoy -> Color.White
                    else -> colors.colorGrisPizarra
                }

                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center,
                    modifier = Modifier
                        .padding(2.dp)
                        .aspectRatio(1f)
                        .clickable { onDateSelected(dia) }
                        .background(fondo, shape = CircleShape)
                        .padding(4.dp)
                ) {
                    Text(
                        text = dia.dayOfMonth.toString(),
                        fontSize = 14.sp,
                        color = colorTexto,
                        fontWeight = if (esHoy || esSeleccionado) FontWeight.Bold else FontWeight.Normal
                    )

                    if (tieneClase) {
                        Spacer(modifier = Modifier.height(2.dp))
                        Box(
                            modifier = Modifier
                                .size(circleSize)
                                .background(colors.secondary, shape = CircleShape)
                        )
                    }
                }
            }
        }
    }
}
