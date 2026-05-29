@file:OptIn(ExperimentalMaterial3Api::class, androidx.compose.foundation.ExperimentalFoundationApi::class)

package pe.lecordonbleu.universidadestudiante.presentation.screens.historialacademico

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
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
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import pe.lecordonbleu.universidadestudiante.data.remote.dto.DataHistorialAcadamico
import pe.lecordonbleu.universidadestudiante.getColorsTheme
import pe.lecordonbleu.universidadestudiante.getSettingsStorage
import pe.lecordonbleu.universidadestudiante.presentation.components.AppDropdownMenu
import pe.lecordonbleu.universidadestudiante.presentation.components.StandardTopBar
import pe.lecordonbleu.universidadestudiante.presentation.screens.historialacademico.customcell.DatosEstudianteCell
import pe.lecordonbleu.universidadestudiante.presentation.screens.historialacademico.customcell.PlanEstudioCell
import pe.lecordonbleu.universidadestudiante.presentation.vo.ResourceUiState

@Composable
fun HistorialAcademicoScreen(
    viewModel: HistorialAcademicoViewModel,
    navigator: NavController
) {
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()
    val colors = getColorsTheme()
    val settings = getSettingsStorage()
    val idEstud = settings.getInt("idEstud", 0)
    val keyboardController = LocalSoftwareKeyboardController.current

    // --- Sección 1: Variables y estados ---
    var historial by remember { mutableStateOf<List<DataHistorialAcadamico>>(emptyList()) }
    var showLoading by remember { mutableStateOf(false) }
    var showError by remember { mutableStateOf(false) }
    var expandedId by remember { mutableStateOf<String?>(null) }
    var expandedInitialized by remember { mutableStateOf(false) }

    var searchText by remember { mutableStateOf("") }
    var filtroVisible by remember { mutableStateOf(false) }
    var selectedServicio by remember { mutableStateOf("") }
    var selectedPlan by remember { mutableStateOf("") }
    var selectedPeriodo by remember { mutableStateOf("") }

    val historialState by viewModel.historialState.collectAsStateWithLifecycle()

    LaunchedEffect(idEstud) {
        viewModel.setHistorialRequest("0", idEstud.toString(), "1")
    }

    // --- Sección 2: UI ---
    Scaffold(
        topBar = {
            StandardTopBar(
                title = "Historial Académico",
                subtitle = "REGISTRO ACADEMICO",
                onBackClick = { navigator.popBackStack() },
                scrollBehavior = scrollBehavior
            )
        },
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        containerColor = colors.backGroundColor
    ) { padding ->
        if (showLoading) {
            Box(
                modifier = Modifier.fillMaxSize().padding(padding),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(color = colors.colorMixPrimary)
            }
        } else if (showError || historial.isEmpty()) {
            Box(
                modifier = Modifier.fillMaxSize().padding(padding),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "No hay datos del estudiante disponibles.",
                    color = colors.textColor.copy(alpha = 0.5f)
                )
            }
        } else {
            val agrupado = historial.groupBy { it.id_estud_pe }

            val opcionesServicio = agrupado.values.map { it.first().serv_nombre }.distinct()
            val opcionesPlan = agrupado.values.map { it.first().pest_det_nombre }.distinct()
            val opcionesPeriodo = agrupado.values.map { it.first().peracad_nombre }.distinct()

            val filtrado = agrupado.entries.filter { (_, items) ->
                val primer = items.first()
                val matchSearch = searchText.isBlank() || items.any { item ->
                    item.pestd_cod.contains(searchText, ignoreCase = true) ||
                    item.serv_nombre.contains(searchText, ignoreCase = true) ||
                    item.peracad_nombre.contains(searchText, ignoreCase = true)
                }
                val matchServicio = selectedServicio.isEmpty() || primer.serv_nombre == selectedServicio
                val matchPlan = selectedPlan.isEmpty() || primer.pest_det_nombre == selectedPlan
                val matchPeriodo = selectedPeriodo.isEmpty() || items.any { it.peracad_nombre == selectedPeriodo }
                matchSearch && matchServicio && matchPlan && matchPeriodo
            }

            LazyColumn(
                modifier = Modifier.fillMaxSize().padding(padding),
                contentPadding = PaddingValues(bottom = 24.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                item {
                    DatosEstudianteCell(estudiante = historial.first())
                }

                item {
                    Column(modifier = Modifier.padding(horizontal = 16.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            OutlinedTextField(
                                value = searchText,
                                onValueChange = { searchText = it },
                                modifier = Modifier.weight(1f),
                                placeholder = {
                                    Text(
                                        text = "Buscar por servicio, codigo, periodo...",
                                        fontSize = 14.sp,
                                        color = colors.textColor.copy(alpha = 0.4f)
                                    )
                                },
                                leadingIcon = {
                                    Icon(
                                        imageVector = Icons.Default.Search,
                                        contentDescription = null,
                                        tint = colors.textColor.copy(alpha = 0.4f)
                                    )
                                },
                                singleLine = true,
                                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
                                keyboardActions = KeyboardActions(onSearch = { keyboardController?.hide() }),
                                shape = RoundedCornerShape(12.dp),
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedBorderColor = colors.colorMixPrimary,
                                    unfocusedBorderColor = colors.textColor.copy(alpha = 0.15f),
                                    focusedTextColor = colors.textColor,
                                    unfocusedTextColor = colors.textColor,
                                    cursorColor = colors.colorMixPrimary,
                                    focusedContainerColor = colors.colorExpenseItem,
                                    unfocusedContainerColor = colors.colorExpenseItem
                                )
                            )
                            Surface(
                                onClick = {
                                    if (filtroVisible) {
                                        selectedServicio = ""
                                        selectedPlan = ""
                                        selectedPeriodo = ""
                                    }
                                    filtroVisible = !filtroVisible
                                },
                                shape = RoundedCornerShape(12.dp),
                                color = if (filtroVisible) colors.colorMixPrimary else colors.colorExpenseItem,
                                border = BorderStroke(1.dp, if (filtroVisible) colors.colorMixPrimary else colors.textColor.copy(alpha = 0.15f)),
                                modifier = Modifier.size(52.dp)
                            ) {
                                Box(contentAlignment = Alignment.Center) {
                                    Icon(
                                        imageVector = Icons.Default.Tune,
                                        contentDescription = null,
                                        tint = if (filtroVisible) colors.backGroundColor else colors.colorMixPrimary,
                                        modifier = Modifier.size(20.dp)
                                    )
                                }
                            }
                        }

                        if (filtroVisible) {
                            Spacer(modifier = Modifier.height(12.dp))
                            Surface(
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(12.dp),
                                color = colors.colorExpenseItem,
                                border = BorderStroke(1.dp, colors.textColor.copy(alpha = 0.07f)),
                                shadowElevation = 2.dp
                            ) {
                                Column(
                                    modifier = Modifier.padding(16.dp),
                                    verticalArrangement = Arrangement.spacedBy(12.dp)
                                ) {
                                    AppDropdownMenu(
                                        items = opcionesServicio,
                                        selectedItem = selectedServicio,
                                        onItemSelected = { selectedServicio = it },
                                        itemLabel = { it },
                                        label = "Servicio",
                                        modifier = Modifier.fillMaxWidth()
                                    )
                                    AppDropdownMenu(
                                        items = opcionesPlan,
                                        selectedItem = selectedPlan,
                                        onItemSelected = { selectedPlan = it },
                                        itemLabel = { it },
                                        label = "Plan de Estudio",
                                        modifier = Modifier.fillMaxWidth()
                                    )
                                    AppDropdownMenu(
                                        items = opcionesPeriodo,
                                        selectedItem = selectedPeriodo,
                                        onItemSelected = { selectedPeriodo = it },
                                        itemLabel = { it },
                                        label = "Periodo",
                                        modifier = Modifier.fillMaxWidth()
                                    )
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "REGISTROS ENCONTRADOS (${filtrado.size})",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = colors.textColor.copy(alpha = 0.45f),
                            letterSpacing = 0.5.sp
                        )
                    }
                }

                if (filtrado.isEmpty()) {
                    item {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 48.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Search,
                                    contentDescription = null,
                                    tint = colors.textColor.copy(alpha = 0.25f),
                                    modifier = Modifier.size(48.dp)
                                )
                                Text(
                                    text = "No hay coincidencias con la búsqueda.",
                                    fontSize = 13.sp,
                                    color = colors.textColor.copy(alpha = 0.4f)
                                )
                            }
                        }
                    }
                } else {
                    itemsIndexed(filtrado) { _, entry ->
                        PlanEstudioCell(
                            items = entry.value,
                            isExpanded = expandedId == entry.key,
                            onToggle = {
                                expandedId = if (expandedId == entry.key) null else entry.key
                            },
                            onChildClick = { child ->
                                val idEstudPe = child.id_estud_pe.toIntOrNull() ?: 0
                                val idPeracad = child.id_peracad.toIntOrNull() ?: 0
                                navigator.navigate("/historialAcademicoDetalle/$idEstudPe/$idPeracad")
                            }
                        )
                    }
                }
            }
        }
    }

    // --- Sección 3: when(uiState) ---
    when (val s = historialState) {
        is ResourceUiState.Loading -> {
            showLoading = true
            showError = false
        }
        is ResourceUiState.Success -> {
            showLoading = false
            showError = false
            historial = s.data.firstOrNull()?.data_hist_acad ?: emptyList()
            if (!expandedInitialized && historial.isNotEmpty()) {
                expandedId = historial.first().id_estud_pe
                expandedInitialized = true
            }
        }
        is ResourceUiState.Error -> {
            showLoading = false
            showError = true
        }
        ResourceUiState.Empty -> {}
    }
}
