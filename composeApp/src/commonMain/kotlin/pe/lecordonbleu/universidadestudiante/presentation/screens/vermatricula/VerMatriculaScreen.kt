@file:OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)

package pe.lecordonbleu.universidadestudiante.presentation.screens.vermatricula

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import kotlinx.coroutines.launch
import pe.lecordonbleu.universidadestudiante.core.config.Constantes
import pe.lecordonbleu.universidadestudiante.data.remote.dto.Carrera
import pe.lecordonbleu.universidadestudiante.data.remote.dto.ListProyeccionValidacion
import pe.lecordonbleu.universidadestudiante.data.remote.dto.ListDetMatric
import pe.lecordonbleu.universidadestudiante.data.remote.dto.ListResumenHist
import pe.lecordonbleu.universidadestudiante.data.remote.dto.ListVerMatric
import pe.lecordonbleu.universidadestudiante.getColorsTheme
import pe.lecordonbleu.universidadestudiante.getSettingsStorage
import pe.lecordonbleu.universidadestudiante.presentation.components.AppDropdownMenu
import pe.lecordonbleu.universidadestudiante.presentation.components.StandardTopBar
import pe.lecordonbleu.universidadestudiante.presentation.screens.vermatricula.customcell.MatriculaCard
import pe.lecordonbleu.universidadestudiante.presentation.screens.vermatricula.customcell.ResumenHistoricoCard
import pe.lecordonbleu.universidadestudiante.presentation.screens.vermatricula.customcell.HorarioMatriculaTab
import pe.lecordonbleu.universidadestudiante.presentation.vo.ResourceUiState

@Composable
fun VerMatriculaScreen(
    viewModel: MatriculaViewModel,
    navigator: NavController
) {
    // ─── 1. Variables y estados ───────────────────────────────────────────────
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()
    val colors = getColorsTheme()
    val settings = getSettingsStorage()
    val idEstud = settings.getInt("idEstud", 0)
    val idUsuario = settings.getInt("idUsuario", 0)
    val idUneg = Constantes.ID_UNEG
    val idSistema = settings.getInt("idSistema", 0)

    val uiStateProyeccion by viewModel.uiStateProyeccion.collectAsStateWithLifecycle()
    val uiStateCarrera by viewModel.uiStateCarrera.collectAsStateWithLifecycle()
    val uiStateVerMatricula by viewModel.uiStateVerMatricula.collectAsStateWithLifecycle()
    val uiStateDetalleMatricula by viewModel.uiStateDetalleMatricula.collectAsStateWithLifecycle()
    val uiStateResumenHistorico by viewModel.uiStateResumenHistorico.collectAsStateWithLifecycle()

    var idEstudPe by remember { mutableStateOf(0) }
    var idPeracad by remember { mutableStateOf(0) }
    var carreras by remember { mutableStateOf<List<Carrera>>(emptyList()) }
    var selectedCarrera by remember { mutableStateOf<Carrera?>(null) }
    var proyeccionList by remember { mutableStateOf<List<ListProyeccionValidacion>>(emptyList()) }
    var verMatriculaList by remember { mutableStateOf<List<ListVerMatric>>(emptyList()) }
    var resumenHistoricoList by remember { mutableStateOf<List<ListResumenHist>>(emptyList()) }
    var detalleMap by remember { mutableStateOf<Map<Int, List<ListDetMatric>>>(emptyMap()) }
    var expandedIdOacad by remember { mutableStateOf<Int?>(null) }
    var totalCreditos by remember { mutableStateOf("") }
    var maxCreditos by remember { mutableStateOf("") }
    var showLoading by remember { mutableStateOf(true) }

    // Tab 0 = Resumen Historico, Tab 1 = Ver Matricula, Tab 2 = Horario
    val tabs = listOf("Resumen Estudiante", "Ver Matricula", "Horario")
    val pagerState = rememberPagerState(pageCount = { 3 })
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        viewModel.setProyeccion(idEstud)
    }

    // ─── 2. UI ────────────────────────────────────────────────────────────────
    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            StandardTopBar(
                title = "MATRICULA",
                subtitle = "Ver Matricula",
                onBackClick = { navigator.popBackStack() },
                scrollBehavior = scrollBehavior
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                color = colors.colorExpenseItem,
                shape = MaterialTheme.shapes.medium,
                border = BorderStroke(1.dp, colors.textColor.copy(alpha = 0.05f))
            ) {
                AppDropdownMenu(
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp),
                    items = carreras,
                    selectedItem = selectedCarrera,
                    onItemSelected = { carrera ->
                        if (selectedCarrera?.id_serv != carrera.id_serv) {
                            selectedCarrera = carrera
                            verMatriculaList = emptyList()
                            resumenHistoricoList = emptyList()
                            detalleMap = emptyMap()
                            expandedIdOacad = null
                            totalCreditos = ""
                            maxCreditos = ""
                            val item = proyeccionList.firstOrNull { it.id_serv == carrera.id_serv.toIntOrNull() }
                            if (item != null) {
                                idEstudPe = item.id_estud_pe
                                idPeracad = item.id_peracad
                                viewModel.setVerMatricula(
                                    idPeracad,
                                    item.id_serv,
                                    item.id_pest_det,
                                    idEstud,
                                    idSistema,
                                    idUneg,
                                    idUsuario
                                )
                                viewModel.setResumenHistorico(idEstudPe, idPeracad)
                            }
                        }
                    },
                    itemLabel = { it.serv_nombre },
                    label = "Carrera",
                    enabled = carreras.isNotEmpty()
                )
            }

            TabRow(
                selectedTabIndex = pagerState.currentPage,
                containerColor = colors.colorExpenseItem,
                contentColor = colors.colorMixPrimary
            ) {
                tabs.forEachIndexed { index, title ->
                    Tab(
                        selected = pagerState.currentPage == index,
                        onClick = { coroutineScope.launch { pagerState.animateScrollToPage(index) } },
                        text = {
                            Text(
                                text = title,
                                fontSize = 12.sp,
                                fontWeight = if (pagerState.currentPage == index) FontWeight.Bold else FontWeight.Normal
                            )
                        }
                    )
                }
            }

            HorizontalPager(
                state = pagerState,
                modifier = Modifier.fillMaxSize()
            ) { page ->
                when (page) {
                    0 -> LazyColumn(
                        modifier = Modifier.fillMaxSize().padding(horizontal = 12.dp),
                        verticalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        item { Spacer(modifier = Modifier.height(8.dp)) }
                        items(resumenHistoricoList) { item ->
                            ResumenHistoricoCard(item = item)
                        }
                        item { Spacer(modifier = Modifier.height(16.dp)) }
                    }
                    1 -> LazyColumn(
                        modifier = Modifier.fillMaxSize().padding(horizontal = 12.dp),
                        verticalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        item { Spacer(modifier = Modifier.height(8.dp)) }
                        if (totalCreditos.isNotEmpty()) {
                            item {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(horizontal = 4.dp, vertical = 4.dp),
                                    horizontalArrangement = Arrangement.End,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = "Creditos: $totalCreditos / $maxCreditos",
                                        style = MaterialTheme.typography.labelMedium,
                                        fontWeight = FontWeight.SemiBold,
                                        color = colors.colorMixPrimary
                                    )
                                }
                            }
                        }
                        items(verMatriculaList) { item ->
                            MatriculaCard(
                                item = item,
                                detalles = emptyList(),
                                isExpanded = false,
                                onExpandClick = {}
                            )
                        }
                        item { Spacer(modifier = Modifier.height(16.dp)) }
                    }
                    2 -> HorarioMatriculaTab(items = verMatriculaList)
                }
            }
        }
    }

    // ─── 3. when(uiState) ────────────────────────────────────────────────────
    when (val s = uiStateProyeccion) {
        is ResourceUiState.Loading -> { showLoading = true }
        is ResourceUiState.Success -> {
            proyeccionList = s.data.ListProyeccionValidacion
            viewModel.resetProyeccionState()
            viewModel.setCarrera(idEstud)
        }
        is ResourceUiState.Error -> { showLoading = false }
        ResourceUiState.Empty -> {}
    }

    when (val s = uiStateCarrera) {
        is ResourceUiState.Loading -> { showLoading = true }
        is ResourceUiState.Success -> {
            showLoading = false
            carreras = s.data.carrera
            val primeraCarrera = s.data.carrera.firstOrNull { it.flag_carrera == "1" }
                ?: s.data.carrera.firstOrNull()
            selectedCarrera = primeraCarrera
            viewModel.resetCarreraState()
            val item = primeraCarrera?.let { c ->
                proyeccionList.firstOrNull { it.id_serv == c.id_serv.toIntOrNull() }
            }
            if (item != null) {
                idEstudPe = item.id_estud_pe
                idPeracad = item.id_peracad
                viewModel.setVerMatricula(
                    idPeracad,
                    item.id_serv,
                    item.id_pest_det,
                    idEstud,
                    idSistema,
                    idUneg,
                    idUsuario
                )
                viewModel.setResumenHistorico(idEstudPe, idPeracad)
            }
        }
        is ResourceUiState.Error -> { showLoading = false }
        ResourceUiState.Empty -> {}
    }

    when (val s = uiStateVerMatricula) {
        is ResourceUiState.Loading -> { showLoading = true }
        is ResourceUiState.Success -> {
            showLoading = false
            if (s.data.flag_val != 0) {
                verMatriculaList = s.data.list_vermatric
                s.data.list_vermatric.forEach { println(it) }
                totalCreditos = s.data.list_vermatric.sumOf { it.cant_tot_cred.toIntOrNull() ?: 0 }.toString()
                maxCreditos = s.data.list_vermatric.firstOrNull()?.limite_maxcred ?: ""
            }
            viewModel.resetVerMatriculaState()
        }
        is ResourceUiState.Error -> { showLoading = false }
        ResourceUiState.Empty -> {}
    }

    when (val s = uiStateDetalleMatricula) {
        is ResourceUiState.Loading -> {}
        is ResourceUiState.Success -> {
            val idOacad = expandedIdOacad
            if (idOacad != null && s.data.flag_val != 0) {
                detalleMap = detalleMap + (idOacad to s.data.list_detmatric)
            }
            viewModel.resetDetalleMatriculaState()
        }
        else -> {}
    }

    when (val s = uiStateResumenHistorico) {
        is ResourceUiState.Loading -> { showLoading = true }
        is ResourceUiState.Success -> {
            showLoading = false
            resumenHistoricoList = s.data.list_resumenhist
            viewModel.resetResumenHistoricoState()
        }
        is ResourceUiState.Error -> { showLoading = false }
        ResourceUiState.Empty -> {}
    }
}
