package pe.lecordonbleu.universidadestudiante.presentation.screens.tramitedocumentario.customcell

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.DayOfWeek
import kotlinx.datetime.LocalDate
import kotlinx.datetime.minus
import kotlinx.datetime.plus
import pe.lecordonbleu.universidadestudiante.getColorsTheme
import pe.lecordonbleu.universidadestudiante.presentation.screens.horario.customcell.CalendarMesView

@Composable
fun FechaSelectorDialog(
    title: String = "Seleccionar fecha",
    selectedDate: LocalDate,
    onDateSelected: (LocalDate) -> Unit,
    onDismiss: () -> Unit
) {
    val colors = getColorsTheme()
    var mesVisual by remember { mutableStateOf(selectedDate) }

    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = {},
        dismissButton = {},
        title = { Text(text = title, color = colors.textColor) },
        text = {
            Column(modifier = Modifier.fillMaxWidth()) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 4.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Mes anterior",
                        tint = colors.colorGrisNeutro,
                        modifier = Modifier.clickable {
                            mesVisual = mesVisual.minus(1, DateTimeUnit.MONTH)
                        }
                    )
                    Text(
                        text = "${getNombreMesEnEspanol(mesVisual.monthNumber)} ${mesVisual.year}",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = colors.colorGrisNeutro
                    )
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                        contentDescription = "Mes siguiente",
                        tint = colors.colorGrisNeutro,
                        modifier = Modifier.clickable {
                            mesVisual = mesVisual.plus(1, DateTimeUnit.MONTH)
                        }
                    )
                }

                DayHeadersEsHor(firstDayOfWeek = DayOfWeek.SUNDAY)

                CalendarMesView(
                    selectedDate = selectedDate,
                    onDateSelected = {
                        onDateSelected(it)
                        onDismiss()
                    },
                    diasConClase = emptyList(),
                    mesVisual = mesVisual
                )
            }
        },
        containerColor = colors.backGroundColor
    )
}

@Composable
private fun DayHeadersEsHor(firstDayOfWeek: DayOfWeek) {
    val colors = getColorsTheme()
    val dias = listOf("DOM", "LUN", "MAR", "MIE", "JUE", "VIE", "SAB")
    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceAround) {
        dias.forEach {
            Text(it, color = colors.colorGrisNeutro, modifier = Modifier.padding(4.dp), fontWeight = FontWeight.Bold)
        }
    }
}

private fun getNombreMesEnEspanol(mes: Int): String = when (mes) {
    1 -> "Enero"; 2 -> "Febrero"; 3 -> "Marzo"; 4 -> "Abril"
    5 -> "Mayo"; 6 -> "Junio"; 7 -> "Julio"; 8 -> "Agosto"
    9 -> "Septiembre"; 10 -> "Octubre"; 11 -> "Noviembre"; 12 -> "Diciembre"
    else -> ""
}
