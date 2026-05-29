package pe.lecordonbleu.universidadestudiante.presentation.screens.notas

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.HorizontalDivider
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
import pe.lecordonbleu.universidadestudiante.data.remote.dto.HistorialNotasItem
import pe.lecordonbleu.universidadestudiante.getColorsTheme
import pe.lecordonbleu.universidadestudiante.presentation.components.StandardTopBar
import pe.lecordonbleu.universidadestudiante.presentation.screens.notas.customcell.HistorialNotasCell
import pe.lecordonbleu.universidadestudiante.presentation.vo.ResourceUiState

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun HistorialNotasScreen(
    idEstudPe: Int,
    idOacadArranque: Int,
    viewModel: HistorialNotasViewModel,
    navigator: NavController
) {
    val colors = getColorsTheme()

    var historial by remember { mutableStateOf<List<HistorialNotasItem>>(emptyList()) }
    var showLoading by remember { mutableStateOf(false) }

    var isFilterExpanded by remember { mutableStateOf(false) }
    var filterCondicion by remember { mutableStateOf<Set<String>>(emptySet()) }
    var filterPeriodo by remember { mutableStateOf<Set<String>>(emptySet()) }
    var filterCiclo by remember { mutableStateOf<Set<String>>(emptySet()) }

    val periodosDisponibles = remember(historial) {
        historial.map { it.peracad_nombre }.distinct().sorted()
    }
    val ciclosDisponibles = remember(historial) {
        historial.map { it.ciclo_nivel }.distinct().sortedBy { it.toIntOrNull() ?: 0 }
    }
    val activeFilterCount = filterCondicion.size + filterPeriodo.size + filterCiclo.size
    val filteredHistorial = remember(historial, filterCondicion, filterPeriodo, filterCiclo) {
        historial.filter { item ->
            val condicionOk = filterCondicion.isEmpty() || filterCondicion.contains(item.matric_not_flag_aprobado)
            val periodoOk = filterPeriodo.isEmpty() || filterPeriodo.contains(item.peracad_nombre)
            val cicloOk = filterCiclo.isEmpty() || filterCiclo.contains(item.ciclo_nivel)
            condicionOk && periodoOk && cicloOk
        }
    }

    val historialState by viewModel.historialState.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.setHistorialRequest(idEstudPe, idOacadArranque)
    }

    Scaffold(
        topBar = {
            StandardTopBar(
                title = "Historial",
                subtitle = "HISTORIAL DE NOTAS",
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
            if (historial.isNotEmpty()) {
                item {
                    Surface(
                        modifier = Modifier.fillMaxWidth(),
                        color = colors.colorExpenseItem,
                        shape = MaterialTheme.shapes.medium,
                        border = BorderStroke(1.dp, colors.textColor.copy(alpha = 0.05f))
                    ) {
                        Column(modifier = Modifier.fillMaxWidth()) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable { isFilterExpanded = !isFilterExpanded }
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
                                    text = if (isFilterExpanded) "OCULTAR FILTROS" else "MOSTRAR FILTROS",
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = colors.colorMixPrimary,
                                    letterSpacing = 1.sp
                                )
                                if (activeFilterCount > 0) {
                                    Spacer(modifier = Modifier.size(6.dp))
                                    Surface(
                                        shape = androidx.compose.foundation.shape.RoundedCornerShape(50),
                                        color = colors.colorMixPrimary
                                    ) {
                                        Text(
                                            text = "$activeFilterCount",
                                            style = MaterialTheme.typography.labelSmall,
                                            color = Color.White,
                                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
                                        )
                                    }
                                }
                                Spacer(modifier = Modifier.size(4.dp))
                                Icon(
                                    imageVector = if (isFilterExpanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                                    contentDescription = null,
                                    tint = colors.colorMixPrimary,
                                    modifier = Modifier.size(16.dp)
                                )
                            }
                            if (isFilterExpanded) {
                                HorizontalDivider(color = colors.textColor.copy(alpha = 0.07f))
                            }
                            AnimatedVisibility(visible = isFilterExpanded) {
                                Column(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(start = 16.dp, end = 16.dp, bottom = 16.dp),
                                    verticalArrangement = Arrangement.spacedBy(10.dp)
                                ) {
                                    FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                        val chipColor = mapOf(
                                            "APROBADO" to colors.colorVerdeMedio,
                                            "DESAPROBADO" to colors.colorRojo
                                        )
                                        listOf("APROBADO", "DESAPROBADO").forEach { condicion ->
                                            val selected = filterCondicion.contains(condicion)
                                            val color = chipColor[condicion] ?: colors.colorMixPrimary
                                            FilterChip(
                                                selected = selected,
                                                onClick = {
                                                    filterCondicion = if (selected) filterCondicion - condicion else filterCondicion + condicion
                                                },
                                                label = { Text(condicion, style = MaterialTheme.typography.labelSmall) },
                                                colors = FilterChipDefaults.filterChipColors(
                                                    selectedContainerColor = color.copy(alpha = 0.15f),
                                                    selectedLabelColor = color,
                                                    selectedLeadingIconColor = color
                                                )
                                            )
                                        }
                                    }
                                    HorizontalDivider(color = colors.textColor.copy(alpha = 0.06f))
                                    Text(
                                        text = "Período",
                                        style = MaterialTheme.typography.labelMedium,
                                        color = colors.textColor.copy(alpha = 0.55f)
                                    )
                                    FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                        periodosDisponibles.forEach { periodo ->
                                            val selected = filterPeriodo.contains(periodo)
                                            FilterChip(
                                                selected = selected,
                                                onClick = {
                                                    filterPeriodo = if (selected) filterPeriodo - periodo else filterPeriodo + periodo
                                                },
                                                label = { Text(periodo, style = MaterialTheme.typography.labelSmall) }
                                            )
                                        }
                                    }
                                    HorizontalDivider(color = colors.textColor.copy(alpha = 0.06f))
                                    Text(
                                        text = "Ciclo",
                                        style = MaterialTheme.typography.labelMedium,
                                        color = colors.textColor.copy(alpha = 0.55f)
                                    )
                                    FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                        ciclosDisponibles.forEach { ciclo ->
                                            val selected = filterCiclo.contains(ciclo)
                                            FilterChip(
                                                selected = selected,
                                                onClick = {
                                                    filterCiclo = if (selected) filterCiclo - ciclo else filterCiclo + ciclo
                                                },
                                                label = { Text("Ciclo $ciclo", style = MaterialTheme.typography.labelSmall) }
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
            items(filteredHistorial) { item ->
                HistorialNotasCell(item = item)
            }
        }
    }

    when (historialState) {
        is ResourceUiState.Loading -> { showLoading = true }
        is ResourceUiState.Success -> {
            showLoading = false
            historial = (historialState as ResourceUiState.Success).data.listadoNotas
        }
        is ResourceUiState.Error -> { showLoading = false }
        ResourceUiState.Empty -> {}
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
