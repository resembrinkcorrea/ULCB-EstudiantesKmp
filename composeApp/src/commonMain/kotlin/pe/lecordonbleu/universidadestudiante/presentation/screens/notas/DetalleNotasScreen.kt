package pe.lecordonbleu.universidadestudiante.presentation.screens.notas

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectVerticalDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.pager.VerticalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import kotlinx.coroutines.launch
import pe.lecordonbleu.universidadestudiante.LoadingIndicator
import pe.lecordonbleu.universidadestudiante.data.remote.dto.ListadoNotasDetalle
import pe.lecordonbleu.universidadestudiante.data.remote.dto.TareaAcad
import pe.lecordonbleu.universidadestudiante.getColorsTheme
import pe.lecordonbleu.universidadestudiante.getSettingsStorage
import pe.lecordonbleu.universidadestudiante.presentation.components.StandardTopBar
import pe.lecordonbleu.universidadestudiante.presentation.screens.notas.customcell.PromedioNotasCell
import pe.lecordonbleu.universidadestudiante.presentation.screens.notas.helpers.ContenidoTareasPanel
import pe.lecordonbleu.universidadestudiante.presentation.screens.notas.helpers.SimuladorNotasSheet
import pe.lecordonbleu.universidadestudiante.presentation.screens.notas.uibuilder.NotasUiBuilder
import pe.lecordonbleu.universidadestudiante.presentation.screens.notas.uibuilder.SimuladorNota
import pe.lecordonbleu.universidadestudiante.presentation.vo.ResourceUiState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetalleNotasScreen(
    idMatricNot: Int,
    viewModel: DetalleNotasViewModel,
    navigator: NavController
) {
    // ─── Sección 1 — Variables ────────────────────────────────────────────────
    val colors = getColorsTheme()
    val settings = getSettingsStorage()
    val idUneg = 1

    val gruposTareas by viewModel.gruposTareas.collectAsStateWithLifecycle()
    val tabs = listOf("PROMEDIO NOTAS") + gruposTareas.map { it.nombrePestana.uppercase() }
    val pagerState = rememberPagerState(pageCount = { tabs.size })
    val scope = rememberCoroutineScope()

    var detalle by remember { mutableStateOf<ListadoNotasDetalle?>(null) }
    var tareasPorId by remember { mutableStateOf<Map<Int, List<TareaAcad>>>(emptyMap()) }
    var showLoading by remember { mutableStateOf(false) }
    var loadingTareas by remember { mutableStateOf(false) }
    var mostrarSimulador by remember { mutableStateOf(false) }
    var notasSimuladas by remember { mutableStateOf<List<SimuladorNota>>(emptyList()) }

    val detalleState by viewModel.detalleState.collectAsStateWithLifecycle()
    val tareasState by viewModel.tareasState.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.setDetalleRequest(idMatricNot, idUneg)
        viewModel.setTiposTareasRequest(idMatricNot)
    }

    // Se usa LaunchedEffect porque reacciona a un gesto del usuario (swipe del pager),
    // no a un ResourceUiState. El swipe no tiene onClick — este es su único punto de captura.
    LaunchedEffect(pagerState.currentPage) {
        val page = pagerState.currentPage
        if (page > 0) {
            val grupo = gruposTareas.getOrNull(page - 1)
            if (grupo != null) {
                viewModel.resetTareasState()
                viewModel.setGrupoRequest(grupo)
            }
        }
    }

    // ─── Sección 2 — UI ──────────────────────────────────────────────────────
    Scaffold(
        topBar = {
            StandardTopBar(
                title = "Detalle",
                subtitle = "CALIFICACIONES",
                onBackClick = { navigator.popBackStack() }
            )
        },
        containerColor = colors.backGroundColor
    ) { paddingValues ->
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            // Tabs verticales — izquierda
            Column(
                modifier = Modifier
                    .fillMaxHeight()
                    .width(48.dp)
                    .background(colors.colorExpenseItem)
                    .pointerInput(Unit) {
                        detectVerticalDragGestures { _, dragAmount ->
                            val target = if (dragAmount > 0) {
                                (pagerState.currentPage + 1).coerceAtMost(tabs.lastIndex)
                            } else {
                                (pagerState.currentPage - 1).coerceAtLeast(0)
                            }
                            scope.launch { pagerState.animateScrollToPage(target) }
                        }
                    },
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                tabs.forEachIndexed { index, title ->
                    val isSelected = pagerState.currentPage == index
                    val tabColor =
                        if (isSelected) colors.colorMixPrimary else colors.textColor.copy(alpha = 0.4f)
                    Box(
                        modifier = Modifier
                            .width(48.dp)
                            .height(120.dp)
                            .clickable {
                                scope.launch { pagerState.animateScrollToPage(index) }
                                if (index > 0) {
                                    val grupo = gruposTareas.getOrNull(index - 1)
                                    if (grupo != null) {
                                        viewModel.resetTareasState()
                                        viewModel.setGrupoRequest(grupo)
                                    }
                                }
                            }
                            .background(
                                if (isSelected) colors.colorMixPrimary.copy(alpha = 0.1f)
                                else colors.colorExpenseItem
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        if (isSelected) {
                            Box(
                                modifier = Modifier
                                    .align(Alignment.CenterEnd)
                                    .width(3.dp)
                                    .height(48.dp)
                                    .background(colors.colorMixPrimary)
                            )
                        }
                        Text(
                            text = title,
                            color = tabColor,
                            fontSize = 10.sp,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                            modifier = Modifier.graphicsLayer { rotationZ = -90f }
                        )
                    }
                    if (index < tabs.lastIndex) {
                        Spacer(
                            modifier = Modifier
                                .width(48.dp)
                                .height(1.dp)
                                .background(colors.textColor.copy(alpha = 0.08f))
                        )
                    }
                }
            }

            // Contenido — derecha
            VerticalPager(
                state = pagerState,
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight()
            ) { page ->
                if (page == 0) {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize()
                            .padding(horizontal = 16.dp, vertical = 8.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        detalle?.let { d ->
                            item {
                                PromedioNotasCell(
                                    item           = d,
                                    onSimularClick = {
                                        notasSimuladas   = NotasUiBuilder.prepararSimulacion(d)
                                        mostrarSimulador = true
                                    }
                                )
                            }
                        }
                    }
                } else {
                    val grupo = gruposTareas.getOrNull(page - 1)
                    ContenidoTareasPanel(
                        grupo = grupo,
                        tareasPorId = tareasPorId,
                        detalle = detalle,
                        isLoading = loadingTareas
                    )
                }
            }
        }
    }

    // ─── Sección 3 — when(uiState) ───────────────────────────────────────────
    when (val s = detalleState) {
        is ResourceUiState.Loading -> {
            showLoading = true
        }

        is ResourceUiState.Success -> {
            showLoading = false; detalle = s.data.listadoNotasDetalle.firstOrNull()
        }

        is ResourceUiState.Error -> {
            showLoading = false
        }

        ResourceUiState.Empty -> {}
    }

    when (val s = tareasState) {
        is ResourceUiState.Loading -> {
            loadingTareas = true
        }

        is ResourceUiState.Success -> {
            loadingTareas = false; tareasPorId = s.data
        }

        is ResourceUiState.Error -> {
            loadingTareas = false
        }

        ResourceUiState.Empty -> { tareasPorId = emptyMap() }
    }

    if (showLoading) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            LoadingIndicator(modifier = Modifier.size(50.dp))
        }
    }

    if (mostrarSimulador && notasSimuladas.isNotEmpty()) {
        SimuladorNotasSheet(
            notas     = notasSimuladas,
            notaMin   = detalle?.nota_min ?: "0",
            onDismiss = { mostrarSimulador = false }
        )
    }
}
