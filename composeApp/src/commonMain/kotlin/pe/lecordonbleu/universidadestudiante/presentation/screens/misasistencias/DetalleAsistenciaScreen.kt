@file:OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)

package pe.lecordonbleu.universidadestudiante.presentation.screens.misasistencias

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
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
import androidx.compose.runtime.derivedStateOf
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import kotlinx.coroutines.launch
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.LocalDate
import kotlinx.datetime.isoDayNumber
import kotlinx.datetime.plus
import kotlinx.datetime.minus
import pe.lecordonbleu.universidadestudiante.data.remote.dto.ListadoAsistencia
import pe.lecordonbleu.universidadestudiante.getColorsTheme
import pe.lecordonbleu.universidadestudiante.getTodayLocalDate
import pe.lecordonbleu.universidadestudiante.presentation.components.StandardTopBar
import pe.lecordonbleu.universidadestudiante.presentation.screens.misasistencias.customcell.AsistenciaGraficoPremium
import pe.lecordonbleu.universidadestudiante.presentation.screens.misasistencias.customcell.AsistenciaTabsSegmented
import pe.lecordonbleu.universidadestudiante.presentation.screens.misasistencias.customcell.ClaseAsistenciaCard
import pe.lecordonbleu.universidadestudiante.presentation.vo.ResourceUiState

@Composable
fun DetalleAsistenciaScreen(
    viewModel: DetalleAsistenciaViewModel,
    idEstudPe: Int,
    idMatricAsigSecc: Int,
    nombreAsignatura: String,
    totalMaxInas: Int,
    porcentajeInasistencia: Float,
    detasismin: Float,
    navigator: NavController
) {
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()
    val colors = getColorsTheme()

    var listAsistencia by remember { mutableStateOf<List<ListadoAsistencia>>(emptyList()) }
    var showLoading by remember { mutableStateOf(false) }
    var showError by remember { mutableStateOf(false) }
    val asistenciaState by viewModel.asistenciaState.collectAsStateWithLifecycle()
    val hoy = remember { getTodayLocalDate() }
    val limite = remember(hoy) {
        val finDeMes = LocalDate(hoy.year, hoy.monthNumber, 1)
            .plus(1, DateTimeUnit.MONTH)
            .minus(1, DateTimeUnit.DAY)
        val finDeSemana = hoy.plus(7 - hoy.dayOfWeek.isoDayNumber, DateTimeUnit.DAY)
        if (finDeSemana > finDeMes) finDeSemana else finDeMes
    }
    val clasesAnteriores = listAsistencia.filter {
        try { LocalDate.parse(it.fecha_dia) <= hoy } catch (_: Exception) { true }
    }
    val clasesFuturas = listAsistencia.filter {
        try {
            val fecha = LocalDate.parse(it.fecha_dia)
            fecha > hoy && fecha <= limite
        } catch (_: Exception) { false }
    }
    val practicasAsistio = clasesAnteriores.count { it.clase.equals("PRACTICO", ignoreCase = true) && it.asistio == "1" }
    val practicasFalto   = clasesAnteriores.count { it.clase.equals("PRACTICO", ignoreCase = true) && it.asistio == "0" }
    val practicasTotal   = clasesAnteriores.count { it.clase.equals("PRACTICO", ignoreCase = true) && (it.asistio == "1" || it.asistio == "0") }
    val teoricaAsistio   = clasesAnteriores.count { it.clase.equals("TEORICO", ignoreCase = true) && it.asistio == "1" }
    val teoricaFalto     = clasesAnteriores.count { it.clase.equals("TEORICO", ignoreCase = true) && it.asistio == "0" }
    val teoricaTotal     = clasesAnteriores.count { it.clase.equals("TEORICO", ignoreCase = true) && (it.asistio == "1" || it.asistio == "0") }
    val tabs = listOf("Historial (${clasesAnteriores.size})", "Proximas (${clasesFuturas.size})")
    val pagerState = rememberPagerState(pageCount = { 2 })
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(idEstudPe, idMatricAsigSecc) {
        viewModel.setDetalleRequest(idEstudPe, idMatricAsigSecc)
    }

    val isResumenVisible by remember { derivedStateOf { scrollBehavior.state.heightOffset >= 0f } }

    Scaffold(
        topBar = {
            StandardTopBar(
                title = nombreAsignatura,
                subtitle = "DETALLE DE ASISTENCIA",
                onBackClick = { navigator.popBackStack() },
                scrollBehavior = scrollBehavior,
                titleFontSize = 18.sp
            )
        },
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        containerColor = colors.backGroundColor
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            if (showLoading) {
                CircularProgressIndicator(
                    modifier = Modifier.align(Alignment.Center),
                    color = colors.colorMixPrimary
                )
            } else if (showError && listAsistencia.isEmpty()) {
                Text(
                    text = "No se pudo cargar la información de asistencia.",
                    color = colors.textColor.copy(alpha = 0.5f),
                    modifier = Modifier
                        .align(Alignment.Center)
                        .padding(32.dp),
                    textAlign = TextAlign.Center
                )
            } else {
                Column(modifier = Modifier.fillMaxSize()) {
                    AnimatedVisibility(
                        visible = isResumenVisible,
                        enter = expandVertically(),
                        exit = shrinkVertically()
                    ) {
                    AsistenciaGraficoPremium(
                        colors = colors,
                        porcentajeInasistencia = porcentajeInasistencia,
                        detasismin = detasismin,
                        totalMaxInas = totalMaxInas,
                        practicasAsistio = practicasAsistio,
                        practicasFalto = practicasFalto,
                        practicasTotal = practicasTotal,
                        teoricaAsistio = teoricaAsistio,
                        teoricaFalto = teoricaFalto,
                        teoricaTotal = teoricaTotal
                    )
                    } // AnimatedVisibility

                    AsistenciaTabsSegmented(
                        tabs = tabs,
                        selectedTabIndex = pagerState.currentPage,
                        onTabSelected = { index ->
                            coroutineScope.launch { pagerState.animateScrollToPage(index) }
                        },
                        colors = colors
                    )

                    HorizontalPager(
                        state = pagerState,
                        modifier = Modifier.fillMaxSize()
                    ) { page ->
                        val currentList = if (page == 0) clasesAnteriores else clasesFuturas
                        LazyColumn(
                            modifier = Modifier.fillMaxSize(),
                            contentPadding = PaddingValues(horizontal = 20.dp, vertical = 16.dp),
                            verticalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
                            items(currentList) { clase ->
                                ClaseAsistenciaCard(
                                    clase = clase,
                                    colors = colors
                                )
                            }
                        }
                    }
                }
            }
        }
    }

    when (val s = asistenciaState) {
        is ResourceUiState.Loading -> { showLoading = true; showError = false }
        is ResourceUiState.Success -> {
            showLoading = false
            showError = false
            listAsistencia = s.data.listadoCarrera
        }
        is ResourceUiState.Error -> {
            showLoading = false
            showError = true
            listAsistencia = emptyList()
        }
        ResourceUiState.Empty -> { showLoading = false }
    }
}
