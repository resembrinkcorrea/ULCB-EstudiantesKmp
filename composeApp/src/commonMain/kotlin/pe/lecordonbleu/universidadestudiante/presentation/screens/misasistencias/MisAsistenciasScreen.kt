@file:OptIn(ExperimentalMaterial3Api::class)

package pe.lecordonbleu.universidadestudiante.presentation.screens.misasistencias

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import io.ktor.http.encodeURLPathPart
import pe.lecordonbleu.universidadestudiante.data.remote.dto.AsignaturaEstudiante
import pe.lecordonbleu.universidadestudiante.data.remote.dto.CarreraRemote
import pe.lecordonbleu.universidadestudiante.data.remote.dto.Periodo
import pe.lecordonbleu.universidadestudiante.getColorsTheme
import pe.lecordonbleu.universidadestudiante.getSettingsStorage
import pe.lecordonbleu.universidadestudiante.presentation.components.AppDropdownMenu
import pe.lecordonbleu.universidadestudiante.presentation.components.StandardTopBar
import pe.lecordonbleu.universidadestudiante.presentation.screens.misasistencias.customcell.AsignaturaAsistenciaCard
import pe.lecordonbleu.universidadestudiante.presentation.vo.ResourceUiState

@Composable
fun MisAsistenciasScreen(
    viewModel: MisAsistenciasViewModel,
    navigator: NavController
) {
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()
    val colors = getColorsTheme()
    val settings = getSettingsStorage()
    val idEstud = settings.getInt("idEstud", 0)

    // --- Sección 1: Variables y estados ---
    var carreras by remember { mutableStateOf<List<CarreraRemote>>(emptyList()) }
    var periodos by remember { mutableStateOf<List<Periodo>>(emptyList()) }
    var asignaturas by remember { mutableStateOf<List<AsignaturaEstudiante>>(emptyList()) }
    var selectedCarrera by remember { mutableStateOf<CarreraRemote?>(null) }
    var selectedPeriodo by remember { mutableStateOf<Periodo?>(null) }
    var showLoadingAsignaturas by remember { mutableStateOf(false) }
    val idEstudPeFinal = selectedPeriodo?.id_estud_pe?.toIntOrNull() ?: 0
    val carreraState by viewModel.carreraState.collectAsStateWithLifecycle()
    val periodoState by viewModel.periodoState.collectAsStateWithLifecycle()
    val asignaturaState by viewModel.asignaturaState.collectAsStateWithLifecycle()

    LaunchedEffect(idEstud) {
        viewModel.setCarreraRequest(idEstud)
    }

  
    Scaffold(
        topBar = {
            StandardTopBar(
                title = "Mi Asistencia",
                subtitle = "CONTROL DE ASISTENCIA",
                onBackClick = { navigator.popBackStack() },
                scrollBehavior = scrollBehavior
            )
        },
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        containerColor = colors.backGroundColor
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            contentPadding = PaddingValues(bottom = 24.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            item {
                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 16.dp, end = 16.dp, top = 12.dp),
                    color = colors.colorExpenseItem,
                    shape = MaterialTheme.shapes.medium,
                    border = BorderStroke(1.dp, colors.textColor.copy(alpha = 0.05f))
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        AppDropdownMenu(
                            items = carreras,
                            selectedItem = selectedCarrera,
                            label = "Programa Académico",
                            itemLabel = { it.serv_nombre },
                            onItemSelected = { carrera ->
                                selectedCarrera = carrera
                                selectedPeriodo = null
                                periodos = emptyList()
                                asignaturas = emptyList()
                                viewModel.setPeriodoRequest(carrera.id_estud_serv.toIntOrNull() ?: 0)
                            },
                            enabled = carreras.isNotEmpty()
                        )
                        AppDropdownMenu(
                            items = periodos,
                            selectedItem = selectedPeriodo,
                            label = "Periodo",
                            itemLabel = { it.peracad_nombre },
                            onItemSelected = { periodo ->
                                selectedPeriodo = periodo
                                asignaturas = emptyList()
                                viewModel.setAsignaturaRequest(
                                    periodo.id_estud_pe.toIntOrNull() ?: 0,
                                    periodo.id_peracad.toIntOrNull() ?: 0
                                )
                            },
                            enabled = periodos.isNotEmpty()
                        )
                    }
                }
            }

            if (showLoadingAsignaturas) {
                item {
                    Box(
                        modifier = Modifier
                            .fillParentMaxWidth()
                            .height(200.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator(color = colors.colorMixPrimary)
                    }
                }
            } else if (asignaturas.isEmpty()) {
                item {
                    Box(
                        modifier = Modifier
                            .fillParentMaxWidth()
                            .height(200.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "No hay asignaturas para este periodo.",
                            color = colors.textColor.copy(alpha = 0.5f)
                        )
                    }
                }
            } else {
                items(asignaturas) { asignatura ->
                    Box(modifier = Modifier.padding(horizontal = 16.dp)) {
                        AsignaturaAsistenciaCard(
                            item = asignatura,
                            colors = colors,
                            onClick = {
                                val idMatricAsigSecc = asignatura.id_matric_asig_secc.toDoubleOrNull()?.toInt() ?: 0
                                val totalMaxInas = asignatura.total_max_inas.toDoubleOrNull()?.toInt() ?: 0
                                val porcentajeInasistencia = asignatura.matric_asig_porc_inasistencia.toFloatOrNull() ?: 0f
                                val detasismin = asignatura.pest_det_asis_min.toFloatOrNull() ?: 30f
                                val nombreEncoded = asignatura.pest_asign_nombre.encodeURLPathPart()
                                navigator.navigate("/detalleAsistencia/$idEstudPeFinal/$idMatricAsigSecc/$nombreEncoded/$totalMaxInas/$porcentajeInasistencia/$detasismin")
                            }
                        )
                    }
                }
            }

            item { Spacer(modifier = Modifier.height(8.dp)) }
        }
    }


    when (val s = carreraState) {
        is ResourceUiState.Success -> {
            carreras = s.data.firstOrNull()?.carrera ?: emptyList()
            if (selectedCarrera == null && carreras.isNotEmpty()) {
                selectedCarrera = carreras.first()
                viewModel.setPeriodoRequest(carreras.first().id_estud_serv.toIntOrNull() ?: 0)
            }
        }
        else -> {}
    }

    when (val s = periodoState) {
        is ResourceUiState.Success -> {
            periodos = s.data.periodo
            if (selectedPeriodo == null && periodos.isNotEmpty()) {
                selectedPeriodo = periodos.first()
                viewModel.setAsignaturaRequest(
                    periodos.first().id_estud_pe.toIntOrNull() ?: 0,
                    periodos.first().id_peracad.toIntOrNull() ?: 0
                )
            }
        }
        else -> {}
    }

    when (val s = asignaturaState) {
        is ResourceUiState.Loading -> { showLoadingAsignaturas = true }
        is ResourceUiState.Success -> {
            showLoadingAsignaturas = false
            asignaturas = s.data.asignatura
        }
        is ResourceUiState.Error -> {
            showLoadingAsignaturas = false
            asignaturas = emptyList()
        }
        ResourceUiState.Empty -> { showLoadingAsignaturas = false }
    }
}
