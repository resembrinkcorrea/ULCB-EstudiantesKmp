package pe.lecordonbleu.universidadestudiante.presentation.screens.horario

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import pe.lecordonbleu.universidadestudiante.presentation.components.getNombreMesEnEspanol
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.UnfoldLess
import androidx.compose.material.icons.filled.UnfoldMore
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import pe.lecordonbleu.universidadestudiante.presentation.components.StandardTopBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
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
import kotlinx.datetime.LocalDate
import kotlinx.datetime.minus
import kotlinx.datetime.plus
import pe.lecordonbleu.universidadestudiante.data.remote.dto.Horario
import pe.lecordonbleu.universidadestudiante.domain.usecase.agruparHorasClase
import pe.lecordonbleu.universidadestudiante.getColorsTheme
import pe.lecordonbleu.universidadestudiante.presentation.screens.horario.customcell.AgendaComprimida
import pe.lecordonbleu.universidadestudiante.presentation.screens.horario.customcell.BoxAgendaDiaria
import pe.lecordonbleu.universidadestudiante.presentation.screens.horario.customcell.ClaseCard
import pe.lecordonbleu.universidadestudiante.presentation.vo.ResourceUiState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HorarioPorDiaScreen(
    idStudPe: Int,
    idOacadArranque: Int,
    fechaInicial: LocalDate,
    viewModel: HorarioViewModel,
    onBack: () -> Unit
) {
    val colors = getColorsTheme()
    var selectedDate by remember { mutableStateOf(fechaInicial) }
    var claseExpandida by remember { mutableStateOf<Horario?>(null) }
    var vistaComprimida by remember { mutableStateOf(true) }
    val uiState by viewModel.uiStateHorario.collectAsState()

    val clasesDelDia = remember(selectedDate, uiState) {
        when (val state = uiState) {
            is ResourceUiState.Success -> agruparHorasClase(
                state.data.listadoHorario.filter { it.hor_asis_dia == selectedDate.toString() }
            )
            else -> emptyList()
        }
    }

    LaunchedEffect(selectedDate) {
        viewModel.setHorarioRequest(
            idEstudPe = idStudPe,
            idOacadArranque = idOacadArranque,
            fechaIni = selectedDate.toString(),
            fechaFin = selectedDate.toString()
        )
    }

    Scaffold(
        topBar = {
            StandardTopBar(
                title = "MI HORARIO",
                subtitle = "Horario del día",
                onBackClick = { onBack() }
            )
        },
        containerColor = colors.backGroundColor
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
        ) {
            Box(modifier = androidx.compose.ui.Modifier.padding(16.dp)) {
                Surface(
                    modifier = androidx.compose.ui.Modifier.fillMaxWidth(),
                    color = colors.colorExpenseItem,
                    shape = RoundedCornerShape(20.dp),
                    border = BorderStroke(1.dp, colors.textColor.copy(alpha = 0.05f)),
                    shadowElevation = 6.dp
                ) {
                    Row(
                        modifier = androidx.compose.ui.Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Surface(
                            color = colors.colorMixPrimary.copy(alpha = 0.1f),
                            shape = CircleShape,
                            modifier = androidx.compose.ui.Modifier
                                .size(42.dp)
                                .clickable { selectedDate = selectedDate.minus(1, DateTimeUnit.DAY) }
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Icon(
                                    Icons.Default.ArrowBack,
                                    contentDescription = "Día anterior",
                                    tint = colors.colorMixPrimary
                                )
                            }
                        }

                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(
                                text = getNombreDiaEnEspanol(selectedDate.dayOfWeek).uppercase(),
                                fontSize = 12.sp,
                                fontWeight = FontWeight.ExtraBold,
                                color = colors.colorMixPrimary,
                                letterSpacing = 1.sp
                            )
                            Spacer(modifier = androidx.compose.ui.Modifier.height(2.dp))
                            Text(
                                text = "${selectedDate.dayOfMonth} ${getNombreMesEnEspanol(selectedDate.monthNumber).uppercase()}",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Black,
                                color = colors.textColor,
                                letterSpacing = 0.5.sp
                            )
                        }

                        Surface(
                            color = colors.colorMixPrimary.copy(alpha = 0.1f),
                            shape = CircleShape,
                            modifier = androidx.compose.ui.Modifier
                                .size(42.dp)
                                .clickable { selectedDate = selectedDate.plus(1, DateTimeUnit.DAY) }
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Icon(
                                    Icons.Default.ArrowForward,
                                    contentDescription = "Día siguiente",
                                    tint = colors.colorMixPrimary
                                )
                            }
                        }
                    }
                }
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 4.dp),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Surface(
                    color = colors.colorMixPrimary.copy(alpha = 0.12f),
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier.clickable { vistaComprimida = !vistaComprimida }
                ) {
                    Icon(
                        imageVector = if (vistaComprimida) Icons.Default.UnfoldMore else Icons.Default.UnfoldLess,
                        contentDescription = "Cambiar vista",
                        tint = colors.colorMixPrimary,
                        modifier = Modifier.padding(6.dp).size(20.dp)
                    )
                }
            }

            if (vistaComprimida) {
                AgendaComprimida(clases = clasesDelDia, onExpandClase = { claseExpandida = it })
            } else {
                BoxAgendaDiaria(clases = clasesDelDia, onExpandClase = { claseExpandida = it })
            }

            if (claseExpandida != null) {
                ModalBottomSheet(
                    onDismissRequest = { claseExpandida = null },
                    containerColor = colors.colorBlancoGris
                ) {
                    claseExpandida?.let {
                        ClaseCard(
                            item = it,
                            paddingStart = 16.dp,
                            showExpandIcon = false,
                            applyTopPadding = false
                        )
                    }
                }
            }
        }
    }
}


