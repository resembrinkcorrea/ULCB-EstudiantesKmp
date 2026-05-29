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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.LocalDate
import kotlinx.datetime.minus
import kotlinx.datetime.plus
import pe.lecordonbleu.universidadestudiante.data.remote.dto.Carrera
import pe.lecordonbleu.universidadestudiante.data.remote.dto.Horario
import pe.lecordonbleu.universidadestudiante.data.remote.dto.Periodo
import pe.lecordonbleu.universidadestudiante.domain.usecase.agruparHorasClase
import pe.lecordonbleu.universidadestudiante.getColorsTheme
import pe.lecordonbleu.universidadestudiante.getSettingsStorage
import pe.lecordonbleu.universidadestudiante.getTodayLocalDate
import pe.lecordonbleu.universidadestudiante.presentation.components.AppDropdownMenu
import pe.lecordonbleu.universidadestudiante.presentation.components.CalendarioMensualHeader
import pe.lecordonbleu.universidadestudiante.presentation.components.CalendarioMensualPanel
import pe.lecordonbleu.universidadestudiante.presentation.components.MonthYearPickerContent
import pe.lecordonbleu.universidadestudiante.presentation.components.StandardTopBar
import pe.lecordonbleu.universidadestudiante.presentation.components.getNombreMesEnEspanol
import pe.lecordonbleu.universidadestudiante.presentation.screens.horario.customcell.ClaseCard
import pe.lecordonbleu.universidadestudiante.presentation.vo.ResourceUiState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HorarioEstudianteScreen(
    navigator: NavController,
    viewModel: HorarioViewModel
) {
    // ── Sección 1: Variables y estados ──────────────────────────────────────
    val colors = getColorsTheme()
    val settingsStorage = getSettingsStorage()

    val uiStateCarrera by viewModel.uiStateCarrera.collectAsStateWithLifecycle()
    val uiStatePeriodo by viewModel.uiStatePeriodo.collectAsStateWithLifecycle()
    val uiStateHorario by viewModel.uiStateHorario.collectAsStateWithLifecycle()

    val today = remember { getTodayLocalDate() }
    var selectedDate by remember { mutableStateOf(today) }
    var mesVisual by remember { mutableStateOf(today) }
    var showMonthYearPicker by remember { mutableStateOf(false) }
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    var idEstudPe by remember { mutableStateOf<Int?>(null) }
    var idOacadArranque by remember { mutableStateOf<Int?>(null) }

    var carrerasData by remember { mutableStateOf<List<Carrera>>(emptyList()) }
    var periodosData by remember { mutableStateOf<List<Periodo>>(emptyList()) }
    var selectedCarrera by remember { mutableStateOf<Carrera?>(null) }
    var selectedPeriodo by remember { mutableStateOf<Periodo?>(null) }

    var allHorariosDelMes by remember { mutableStateOf<List<Horario>>(emptyList()) }
    var listaCursos by remember { mutableStateOf<List<String>>(emptyList()) }
    var selectedCurso by remember { mutableStateOf("") }

    val diasConClase by viewModel.diasConClase.collectAsStateWithLifecycle()

    val clasesDelDia = remember(selectedDate, selectedCurso, allHorariosDelMes) {
        agruparHorasClase(
            allHorariosDelMes
                .filter { it.hor_asis_dia == selectedDate.toString() }
                .filter { selectedCurso.isEmpty() || it.pest_asign_nombre == selectedCurso }
        )
    }

    var showLoadingClases by remember { mutableStateOf(false) }
    var errorHorario by remember { mutableStateOf("") }

    var showFiltros by remember { mutableStateOf(true) }

    // Gates para servicios encadenados
    var carreraLanzada by remember { mutableStateOf(false) }
    var periodoLanzado by remember { mutableStateOf(false) }

    // ── LaunchedEffect de arranque ───────────────────────────────────────────
    LaunchedEffect(Unit) {
        viewModel.setCarreraRequest(settingsStorage.getInt("idEstud", 0))
    }

    // ── Sección 2: UI ────────────────────────────────────────────────────────
    Scaffold(
        topBar = {
            StandardTopBar(
                title = "HORARIO",
                subtitle = "Clases por período",
                onBackClick = { navigator.popBackStack() }
            )
        },
        containerColor = colors.backGroundColor
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {
            Box(modifier = Modifier.padding(16.dp)) {
                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    color = colors.colorExpenseItem,
                    shape = RoundedCornerShape(20.dp),
                    border = BorderStroke(1.dp, colors.textColor.copy(alpha = 0.05f)),
                    shadowElevation = 6.dp
                ) {
                    Column(modifier = Modifier.padding(vertical = 8.dp)) {
                        CalendarioMensualHeader(
                            mesVisual = mesVisual,
                            colors = colors,
                            onPrevMonth = {
                                val nuevoMes = mesVisual.minus(1, DateTimeUnit.MONTH)
                                mesVisual = nuevoMes
                                if (idEstudPe != null && idOacadArranque != null) {
                                    val primerDia = LocalDate(nuevoMes.year, nuevoMes.month, 1)
                                    val ultimoDia = primerDia.plus(1, DateTimeUnit.MONTH).minus(1, DateTimeUnit.DAY)
                                    viewModel.setHorarioRequest(idEstudPe!!, idOacadArranque!!, primerDia.toString(), ultimoDia.toString())
                                }
                            },
                            onNextMonth = {
                                val nuevoMes = mesVisual.plus(1, DateTimeUnit.MONTH)
                                mesVisual = nuevoMes
                                if (idEstudPe != null && idOacadArranque != null) {
                                    val primerDia = LocalDate(nuevoMes.year, nuevoMes.month, 1)
                                    val ultimoDia = primerDia.plus(1, DateTimeUnit.MONTH).minus(1, DateTimeUnit.DAY)
                                    viewModel.setHorarioRequest(idEstudPe!!, idOacadArranque!!, primerDia.toString(), ultimoDia.toString())
                                }
                            },
                            onPickerClick = { showMonthYearPicker = true }
                        )

                        if (showFiltros) {
                            Column(modifier = Modifier.padding(horizontal = 16.dp)) {
                                if (carrerasData.isNotEmpty()) {
                                    AppDropdownMenu(
                                        items = carrerasData,
                                        selectedItem = selectedCarrera,
                                        label = "Carrera",
                                        itemLabel = { it.serv_nombre },
                                        onItemSelected = { carrera ->
                                            selectedCarrera = carrera
                                            periodoLanzado = false
                                            viewModel.setPeriodoRequest(carrera.id_estud_serv.toInt())
                                        }
                                    )
                                    Spacer(modifier = Modifier.height(8.dp))
                                }

                                AppDropdownMenu(
                                    items = periodosData,
                                    selectedItem = selectedPeriodo,
                                    label = "Período Académico",
                                    itemLabel = { it.peracad_nombre },
                                    onItemSelected = { periodo ->
                                        selectedPeriodo = periodo
                                        idEstudPe = periodo.id_estud_pe.toInt()
                                        idOacadArranque = periodo.id_oacad_arranque.toInt()
                                        val primerDia = LocalDate(mesVisual.year, mesVisual.month, 1)
                                        val ultimoDia = primerDia.plus(1, DateTimeUnit.MONTH).minus(1, DateTimeUnit.DAY)
                                        viewModel.setHorarioRequest(idEstudPe!!, idOacadArranque!!, primerDia.toString(), ultimoDia.toString())
                                    },
                                    enabled = periodosData.isNotEmpty()
                                )

                                if (listaCursos.isNotEmpty()) {
                                    Spacer(modifier = Modifier.height(8.dp))
                                    val todosLosCursos = listOf("Todos los cursos") + listaCursos
                                    AppDropdownMenu(
                                        items = todosLosCursos,
                                        selectedItem = if (selectedCurso.isEmpty()) "Todos los cursos" else selectedCurso,
                                        label = "Filtrar por Curso",
                                        itemLabel = { it },
                                        onItemSelected = { seleccion ->
                                            selectedCurso = if (seleccion == "Todos los cursos") "" else seleccion
                                            viewModel.filtrarPorCurso(selectedCurso)
                                        }
                                    )
                                }

                                Spacer(modifier = Modifier.height(8.dp))
                            }
                        }

                        HorizontalDivider(color = colors.textColor.copy(alpha = 0.07f))

                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { showFiltros = !showFiltros }
                                .padding(horizontal = 16.dp, vertical = 12.dp),
                            horizontalArrangement = Arrangement.Center,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.FilterList,
                                contentDescription = null,
                                tint = colors.colorMixPrimary,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.size(6.dp))
                            Text(
                                text = if (showFiltros) "OCULTAR FILTROS" else "MOSTRAR FILTROS",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = colors.colorMixPrimary,
                                letterSpacing = 1.sp
                            )
                            Spacer(modifier = Modifier.size(4.dp))
                            Icon(
                                imageVector = if (showFiltros) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                                contentDescription = null,
                                tint = colors.colorMixPrimary,
                                modifier = Modifier.size(16.dp)
                            )
                        }
                    }
                }
            }

            CalendarioMensualPanel(
                mesVisual = mesVisual,
                selectedDate = selectedDate,
                diasConClase = diasConClase,
                isLoading = showLoadingClases,
                colors = colors,
                showHeader = false,
                onDateSelected = { dia -> selectedDate = dia },
                onPrevMonth = {},
                onNextMonth = {},
                onPickerClick = {}
            )

            Spacer(modifier = Modifier.height(24.dp))

            Column(modifier = Modifier.padding(horizontal = 16.dp)) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 12.dp, start = 4.dp, end = 4.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "CLASES DEL ${selectedDate.dayOfMonth} DE ${getNombreMesEnEspanol(selectedDate.monthNumber).uppercase()}",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = colors.textColor.copy(alpha = 0.6f),
                        letterSpacing = 1.sp
                    )
                    if (selectedDate == today) {
                        Surface(
                            color = colors.colorMixPrimary.copy(alpha = 0.1f),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Text(
                                "HOY",
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Black,
                                color = colors.colorMixPrimary,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                            )
                        }
                    }
                }

                if (showLoadingClases) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(100.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator(color = colors.colorMixPrimary)
                    }
                } else if (errorHorario.isNotEmpty()) {
                    Text("Error: $errorHorario", modifier = Modifier.padding(16.dp))
                } else if (clasesDelDia.isEmpty()) {
                    Surface(
                        modifier = Modifier.fillMaxWidth(),
                        color = colors.textColor.copy(alpha = 0.02f),
                        shape = RoundedCornerShape(16.dp),
                        border = BorderStroke(1.dp, colors.textColor.copy(alpha = 0.05f))
                    ) {
                        Column(
                            modifier = Modifier.padding(32.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                "Dia libre",
                                color = colors.textColor.copy(alpha = 0.8f),
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                "No tienes clases programadas para esta fecha.",
                                color = colors.textColor.copy(alpha = 0.5f),
                                fontSize = 13.sp,
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                } else {
                    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                        clasesDelDia.forEach { clase ->
                            ClaseCard(
                                item = clase,
                                showExpandIcon = false,
                                onNavigate = if (idEstudPe != null && idOacadArranque != null) {
                                    { navigator.navigate("/horarioPorDia/$selectedDate/$idEstudPe/$idOacadArranque") }
                                } else null
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))
            }
        }
    }

    // ── Sección 3: Observers (fuera del árbol de UI) ─────────────────────────

    when (val state = uiStateCarrera) {
        is ResourceUiState.Success -> {
            carrerasData = state.data.carrera
            if (!carreraLanzada) {
                carreraLanzada = true
                state.data.carrera.firstOrNull()?.let {
                    selectedCarrera = it
                    viewModel.setPeriodoRequest(it.id_estud_serv.toInt())
                }
            }
        }
        else -> {}
    }

    when (val state = uiStatePeriodo) {
        is ResourceUiState.Success -> {
            periodosData = state.data.periodo
            if (!periodoLanzado) {
                periodoLanzado = true
                state.data.periodo.firstOrNull()?.let { primero ->
                    selectedPeriodo = primero
                    idEstudPe = primero.id_estud_pe.toInt()
                    idOacadArranque = primero.id_oacad_arranque.toInt()
                    val primerDia = LocalDate(mesVisual.year, mesVisual.month, 1)
                    val ultimoDia = primerDia.plus(1, DateTimeUnit.MONTH).minus(1, DateTimeUnit.DAY)
                    viewModel.setHorarioRequest(primero.id_estud_pe.toInt(), primero.id_oacad_arranque.toInt(), primerDia.toString(), ultimoDia.toString())
                }
            }
        }
        else -> {}
    }

    when (val state = uiStateHorario) {
        is ResourceUiState.Loading -> {
            showLoadingClases = true
            errorHorario = ""
        }
        is ResourceUiState.Success -> {
            showLoadingClases = false
            errorHorario = ""
            allHorariosDelMes = state.data.listadoHorario
            listaCursos = state.data.listadoHorario.map { it.pest_asign_nombre }.distinct()
        }
        is ResourceUiState.Error -> {
            showLoadingClases = false
            errorHorario = state.message
        }
        else -> {}
    }

    if (showMonthYearPicker) {
        ModalBottomSheet(
            onDismissRequest = { showMonthYearPicker = false },
            sheetState = sheetState,
            containerColor = colors.backGroundColor,
            shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp)
        ) {
            MonthYearPickerContent(
                currentVisualMonth = mesVisual,
                onMonthSelected = { nuevoMesVisual ->
                    mesVisual = nuevoMesVisual
                    showMonthYearPicker = false
                    if (idEstudPe != null && idOacadArranque != null) {
                        val primerDia = LocalDate(nuevoMesVisual.year, nuevoMesVisual.month, 1)
                        val ultimoDia = primerDia.plus(1, DateTimeUnit.MONTH).minus(1, DateTimeUnit.DAY)
                        viewModel.setHorarioRequest(idEstudPe!!, idOacadArranque!!, primerDia.toString(), ultimoDia.toString())
                    }
                }
            )
        }
    }
}

fun getFormattedDateEs(date: LocalDate): String {
    val day = date.dayOfMonth.toString().padStart(2, '0')
    val month = date.monthNumber.toString().padStart(2, '0')
    return "$day/$month/${date.year}"
}

fun getNombreDiaEnEspanol(dia: kotlinx.datetime.DayOfWeek): String {
    return when (dia) {
        kotlinx.datetime.DayOfWeek.MONDAY -> "Lunes"
        kotlinx.datetime.DayOfWeek.TUESDAY -> "Martes"
        kotlinx.datetime.DayOfWeek.WEDNESDAY -> "Miércoles"
        kotlinx.datetime.DayOfWeek.THURSDAY -> "Jueves"
        kotlinx.datetime.DayOfWeek.FRIDAY -> "Viernes"
        kotlinx.datetime.DayOfWeek.SATURDAY -> "Sabado"
        kotlinx.datetime.DayOfWeek.SUNDAY -> "Domingo"
        else -> ""
    }
}
