package pe.lecordonbleu.universidadestudiante.presentation.screens.notas

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.History
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import pe.lecordonbleu.universidadestudiante.LoadingIndicator
import pe.lecordonbleu.universidadestudiante.data.remote.dto.Carrera
import pe.lecordonbleu.universidadestudiante.data.remote.dto.ListadoNotas
import pe.lecordonbleu.universidadestudiante.data.remote.dto.Periodo
import pe.lecordonbleu.universidadestudiante.getColorsTheme
import pe.lecordonbleu.universidadestudiante.getSettingsStorage
import pe.lecordonbleu.universidadestudiante.presentation.components.AppDropdownMenu
import pe.lecordonbleu.universidadestudiante.presentation.components.StandardTopBar
import pe.lecordonbleu.universidadestudiante.presentation.screens.notas.customcell.CursoNotasCell
import pe.lecordonbleu.universidadestudiante.presentation.vo.ResourceUiState
import pe.lecordonbleu.universidadestudiante.showToast

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NotasScreen(
    viewModel: NotasViewModel,
    navigator: NavController
) {
    val colors = getColorsTheme()
    val settings = getSettingsStorage()
    val idEstud = settings.getInt("idEstud", 0)

    var carreras by remember { mutableStateOf<List<Carrera>>(emptyList()) }
    var periodos by remember { mutableStateOf<List<Periodo>>(emptyList()) }
    var cursos by remember { mutableStateOf<List<ListadoNotas>>(emptyList()) }
    var selectedCarrera by remember { mutableStateOf<Carrera?>(null) }
    var selectedPeriodo by remember { mutableStateOf<Periodo?>(null) }
    var showLoading by remember { mutableStateOf(false) }

    var autoCarrera by remember { mutableStateOf(false) }
    var autoPeriodo by remember { mutableStateOf(false) }
    var showToastSinDatos by remember { mutableStateOf(false) }

    val carreraState by viewModel.carreraState.collectAsStateWithLifecycle()
    val periodoState by viewModel.periodoState.collectAsStateWithLifecycle()
    val cursosState by viewModel.cursosState.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.setCarreraRequest(idEstud)
    }

    Scaffold(
        topBar = {
            StandardTopBar(
                title = "Notas",
                subtitle = "RESUMEN ACADÉMICO",
                onBackClick = { navigator.popBackStack() }
            )
        },
        containerColor = colors.backGroundColor
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 16.dp, vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            item {
                Surface(
                    modifier = Modifier.fillMaxWidth(),
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
                            label = "Carrera",
                            itemLabel = { it.serv_nombre },
                            onItemSelected = { carrera ->
                                selectedCarrera = carrera
                                selectedPeriodo = null
                                periodos = emptyList()
                                cursos = emptyList()
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
                                cursos = emptyList()
                                viewModel.setCursosRequest(
                                    periodo.id_estud_pe.toIntOrNull() ?: 0,
                                    periodo.id_oacad_arranque.toIntOrNull() ?: 0
                                )
                            },
                            enabled = periodos.isNotEmpty()
                        )
                        if (selectedPeriodo != null) {
                            Spacer(modifier = Modifier.height(4.dp))
                            Button(
                                onClick = {
                                    selectedPeriodo?.let {
                                        navigator.navigate("/historialNotas/${it.id_estud_pe}/${it.id_oacad_arranque}")
                                    }
                                },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(48.dp),
                                shape = MaterialTheme.shapes.small,
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = colors.colorMixPrimary,
                                    contentColor = Color.White
                                )
                            ) {
                                Icon(Icons.Default.History, null, modifier = Modifier.size(18.dp))
                                Spacer(Modifier.width(8.dp))
                                Text("VER HISTORIAL COMPLETO", fontWeight = FontWeight.Bold, fontSize = 13.sp)
                            }
                        }
                    }
                }
            }
            items(cursos) { curso ->
                CursoNotasCell(
                    item = curso,
                    onClick = {
                        navigator.navigate("/detalleNotas/${curso.id_matric_not}")
                    }
                )
            }
        }
    }

    when (val s = carreraState) {
        is ResourceUiState.Loading -> { showLoading = true }
        is ResourceUiState.Success -> {
            showLoading = false
            carreras = s.data.carrera
            if (!autoCarrera && carreras.isNotEmpty()) {
                autoCarrera = true
                selectedCarrera = carreras.first()
                viewModel.setPeriodoRequest(carreras.first().id_estud_serv.toIntOrNull() ?: 0)
            }
        }
        is ResourceUiState.Error -> { showLoading = false }
        ResourceUiState.Empty -> {}
    }

    when (val s = periodoState) {
        is ResourceUiState.Loading -> { showLoading = true }
        is ResourceUiState.Success -> {
            showLoading = false
            periodos = s.data.periodo
            if (!autoPeriodo && periodos.isNotEmpty()) {
                autoPeriodo = true
                selectedPeriodo = periodos.first()
                viewModel.setCursosRequest(
                    periodos.first().id_estud_pe.toIntOrNull() ?: 0,
                    periodos.first().id_oacad_arranque.toIntOrNull() ?: 0
                )
            }
        }
        is ResourceUiState.Error -> { showLoading = false }
        ResourceUiState.Empty -> {}
    }

    when (val s = cursosState) {
        is ResourceUiState.Loading -> { showLoading = true }
        is ResourceUiState.Success -> {
            showLoading = false
            cursos = s.data.listadoNotas
            if (cursos.isEmpty()) showToastSinDatos = true
        }
        is ResourceUiState.Error -> { showLoading = false }
        ResourceUiState.Empty -> {}
    }

    if (showToastSinDatos) {
        showToast("No hay información disponible")
        showToastSinDatos = false
    }

    if (showLoading) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            LoadingIndicator(modifier = Modifier.size(50.dp))
        }
    }
}

