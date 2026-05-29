package pe.lecordonbleu.universidadestudiante.presentation.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.datetime.LocalDate
import pe.lecordonbleu.universidadestudiante.DarkModeColors
import pe.lecordonbleu.universidadestudiante.getColorsTheme
import pe.lecordonbleu.universidadestudiante.presentation.screens.horario.customcell.CalendarMesView

@Composable
fun CalendarioMensualHeader(
    mesVisual: LocalDate,
    colors: DarkModeColors,
    onPrevMonth: () -> Unit,
    onNextMonth: () -> Unit,
    onPickerClick: () -> Unit
) {
    Box(modifier = Modifier.padding(16.dp)) {
        Surface(
            modifier = Modifier.fillMaxWidth(),
            color = colors.colorExpenseItem,
            shape = RoundedCornerShape(20.dp),
            border = BorderStroke(1.dp, colors.textColor.copy(alpha = 0.05f)),
            shadowElevation = 6.dp
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Surface(
                    color = colors.colorMixPrimary.copy(alpha = 0.1f),
                    shape = CircleShape,
                    modifier = Modifier
                        .size(42.dp)
                        .clip(CircleShape)
                        .clickable { onPrevMonth() }
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(
                            Icons.Default.KeyboardArrowLeft,
                            contentDescription = "Mes anterior",
                            tint = colors.colorMixPrimary
                        )
                    }
                }

                Surface(
                    color = Color.Transparent,
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier
                        .clip(RoundedCornerShape(12.dp))
                        .clickable { onPickerClick() }
                        .padding(horizontal = 16.dp, vertical = 6.dp)
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = getNombreMesEnEspanol(mesVisual.monthNumber).uppercase(),
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Black,
                            color = colors.textColor,
                            letterSpacing = 0.5.sp
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = mesVisual.year.toString(),
                            fontSize = 12.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = colors.colorMixPrimary,
                            letterSpacing = 1.sp
                        )
                    }
                }

                Surface(
                    color = colors.colorMixPrimary.copy(alpha = 0.1f),
                    shape = CircleShape,
                    modifier = Modifier
                        .size(42.dp)
                        .clip(CircleShape)
                        .clickable { onNextMonth() }
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(
                            Icons.Default.KeyboardArrowRight,
                            contentDescription = "Mes siguiente",
                            tint = colors.colorMixPrimary
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun CalendarioMensualPanel(
    mesVisual: LocalDate,
    selectedDate: LocalDate,
    diasConClase: List<LocalDate>,
    isLoading: Boolean,
    colors: DarkModeColors,
    showHeader: Boolean = true,
    onDateSelected: (LocalDate) -> Unit,
    onPrevMonth: () -> Unit,
    onNextMonth: () -> Unit,
    onPickerClick: () -> Unit
) {
    if (showHeader) {
        CalendarioMensualHeader(
            mesVisual = mesVisual,
            colors = colors,
            onPrevMonth = onPrevMonth,
            onNextMonth = onNextMonth,
            onPickerClick = onPickerClick
        )
    }

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        shape = RoundedCornerShape(20.dp),
        color = colors.textColor.copy(alpha = 0.02f),
        border = BorderStroke(1.dp, colors.textColor.copy(alpha = 0.05f))
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            DayHeadersRow()
            Spacer(modifier = Modifier.height(12.dp))
            if (isLoading) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(325.dp),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(color = colors.colorMixPrimary)
                }
            } else {
                CalendarMesView(
                    selectedDate = selectedDate,
                    onDateSelected = onDateSelected,
                    diasConClase = diasConClase,
                    mesVisual = mesVisual
                )
            }
        }
    }
}

@Composable
fun DayHeadersRow() {
    val colors = getColorsTheme()
    val dias = listOf("DOM", "LUN", "MAR", "MIE", "JUE", "VIE", "SAB")
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 4.dp, vertical = 4.dp),
        shape = RoundedCornerShape(12.dp),
        color = colors.textColor.copy(alpha = 0.04f),
        border = BorderStroke(1.dp, colors.textColor.copy(alpha = 0.05f))
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 10.dp)
        ) {
            dias.forEach { dia ->
                Text(
                    text = dia,
                    color = colors.textColor.copy(alpha = 0.5f),
                    modifier = Modifier.weight(1f),
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 11.sp,
                    letterSpacing = 1.sp,
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MonthYearPickerContent(
    currentVisualMonth: LocalDate,
    onMonthSelected: (LocalDate) -> Unit
) {
    val colors = getColorsTheme()
    var pickerYear by remember { mutableStateOf(currentVisualMonth.year) }
    val mesesCortos = listOf("Ene", "Feb", "Mar", "Abr", "May", "Jun", "Jul", "Ago", "Sep", "Oct", "Nov", "Dic")

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp, vertical = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Saltar a fecha",
            fontSize = 12.sp,
            color = colors.textColor.copy(alpha = 0.5f),
            fontWeight = FontWeight.Bold,
            letterSpacing = 1.sp
        )

        Spacer(modifier = Modifier.height(24.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(
                onClick = { pickerYear -= 1 },
                modifier = Modifier.background(colors.colorMixPrimary.copy(alpha = 0.1f), CircleShape)
            ) {
                Icon(
                    Icons.Default.KeyboardArrowLeft,
                    contentDescription = "-1 Año",
                    tint = colors.colorMixPrimary
                )
            }
            Text(
                text = pickerYear.toString(),
                fontSize = 28.sp,
                fontWeight = FontWeight.Black,
                color = colors.textColor
            )
            IconButton(
                onClick = { pickerYear += 1 },
                modifier = Modifier.background(colors.colorMixPrimary.copy(alpha = 0.1f), CircleShape)
            ) {
                Icon(
                    Icons.Default.KeyboardArrowRight,
                    contentDescription = "+1 Año",
                    tint = colors.colorMixPrimary
                )
            }
        }

        Spacer(modifier = Modifier.height(32.dp))

        LazyVerticalGrid(
            columns = GridCells.Fixed(3),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier.heightIn(max = 300.dp)
        ) {
            itemsIndexed(mesesCortos) { index, mesNombre ->
                val monthNumber = index + 1
                val isSelected = pickerYear == currentVisualMonth.year && monthNumber == currentVisualMonth.monthNumber
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = if (isSelected) colors.colorMixPrimary else colors.textColor.copy(alpha = 0.05f),
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(12.dp))
                        .clickable { onMonthSelected(LocalDate(pickerYear, monthNumber, 1)) }
                ) {
                    Box(
                        modifier = Modifier.padding(vertical = 14.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = mesNombre.uppercase(),
                            color = if (isSelected) Color.White else colors.textColor,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                            fontSize = 14.sp
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(32.dp))
    }
}

fun getNombreMesEnEspanol(mes: Int): String {
    return when (mes) {
        1 -> "Enero"
        2 -> "Febrero"
        3 -> "Marzo"
        4 -> "Abril"
        5 -> "Mayo"
        6 -> "Junio"
        7 -> "Julio"
        8 -> "Agosto"
        9 -> "Septiembre"
        10 -> "Octubre"
        11 -> "Noviembre"
        12 -> "Diciembre"
        else -> ""
    }
}
