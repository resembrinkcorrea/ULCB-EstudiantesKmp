@file:OptIn(ExperimentalMaterial3Api::class)

package pe.lecordonbleu.universidadestudiante.presentation.screens.historialacademico

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import pe.lecordonbleu.universidadestudiante.data.remote.dto.ListadoDetacad
import pe.lecordonbleu.universidadestudiante.getColorsTheme
import pe.lecordonbleu.universidadestudiante.presentation.components.AppDropdownMenu
import pe.lecordonbleu.universidadestudiante.presentation.components.StandardTopBar
import pe.lecordonbleu.universidadestudiante.presentation.screens.historialacademico.customcell.HistorialAcademicoDetalleCard
import pe.lecordonbleu.universidadestudiante.presentation.vo.ResourceUiState

@Composable
fun HistorialAcademicoDetalleScreen(
    viewModel: HistorialAcademicoDetalleViewModel,
    idEstudPe: Int,
    idPeracad: Int,
    navigator: NavController
) {
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()
    val colors = getColorsTheme()
    val keyboardController = LocalSoftwareKeyboardController.current

    // --- Sección 1: Variables y estados ---
    var asignaturas by remember { mutableStateOf<List<ListadoDetacad>>(emptyList()) }
    var showLoading by remember { mutableStateOf(false) }
    var showError by remember { mutableStateOf(false) }

    var searchText by remember { mutableStateOf("") }
    var filtroVisible by remember { mutableStateOf(false) }
    var selectedEstado by remember { mutableStateOf("") }
    var selectedModalidad by remember { mutableStateOf("") }

    val detalleState by viewModel.detalleState.collectAsStateWithLifecycle()

    LaunchedEffect(idEstudPe, idPeracad) {
        viewModel.setDetalleRequest(idEstudPe, idPeracad)
    }

    // --- Sección 2: UI ---
    Scaffold(
        topBar = {
            StandardTopBar(
                title = "Detalle Académico",
                subtitle = "ASIGNATURAS DEL PERIODO",
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
        } else if (showError || asignaturas.isEmpty()) {
            Box(
                modifier = Modifier.fillMaxSize().padding(padding),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "No se encontraron asignaturas para este periodo.",
                    color = colors.textColor.copy(alpha = 0.5f)
                )
            }
        } else {
            val opcionesEstado = asignaturas.map { it.pg_estado_resultado_nota.trim() }.distinct()
            val opcionesModalidad = asignaturas.map { it.modal_asign_nombre.trim() }.distinct()

            val filtrado = asignaturas.filter { item ->
                val matchSearch = searchText.isBlank() ||
                        item.asign_nombre.contains(searchText, ignoreCase = true) ||
                        item.pg_estado_resultado_nota.contains(searchText, ignoreCase = true) ||
                        item.modal_asign_nombre.contains(searchText, ignoreCase = true)
                val matchEstado = selectedEstado.isEmpty() ||
                        item.pg_estado_resultado_nota.trim().equals(selectedEstado, ignoreCase = true)
                val matchModalidad = selectedModalidad.isEmpty() ||
                        item.modal_asign_nombre.trim().equals(selectedModalidad, ignoreCase = true)
                matchSearch && matchEstado && matchModalidad
            }

            LazyColumn(
                modifier = Modifier.fillMaxSize().padding(padding),
                contentPadding = PaddingValues(bottom = 24.dp),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                item {
                    Column(modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)) {
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
                                        text = "Buscar por asignatura...",
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
                                        selectedEstado = ""
                                        selectedModalidad = ""
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
                            Surface(
                                modifier = Modifier.fillMaxWidth().padding(top = 12.dp),
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
                                        items = opcionesEstado,
                                        selectedItem = selectedEstado,
                                        onItemSelected = { selectedEstado = it },
                                        itemLabel = { it },
                                        label = "Estado del Curso",
                                        modifier = Modifier.fillMaxWidth()
                                    )
                                    AppDropdownMenu(
                                        items = opcionesModalidad,
                                        selectedItem = selectedModalidad,
                                        onItemSelected = { selectedModalidad = it },
                                        itemLabel = { it },
                                        label = "Modalidad",
                                        modifier = Modifier.fillMaxWidth()
                                    )
                                }
                            }
                        }

                        Text(
                            text = "ASIGNATURAS ENCONTRADAS (${filtrado.size})",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = colors.textColor.copy(alpha = 0.45f),
                            letterSpacing = 0.5.sp,
                            modifier = Modifier.padding(top = 8.dp)
                        )
                    }
                }

                if (filtrado.isEmpty()) {
                    item {
                        Box(
                            modifier = Modifier.fillMaxWidth().padding(vertical = 48.dp),
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
                    items(filtrado) { item ->
                        HistorialAcademicoDetalleCard(item = item)
                    }
                }
            }
        }
    }

    // --- Sección 3: when(uiState) ---
    when (val s = detalleState) {
        is ResourceUiState.Loading -> {
            showLoading = true
            showError = false
        }
        is ResourceUiState.Success -> {
            showLoading = false
            showError = false
            asignaturas = s.data.firstOrNull()?.listado_detacad ?: emptyList()
        }
        is ResourceUiState.Error -> {
            showLoading = false
            showError = true
        }
        ResourceUiState.Empty -> {}
    }
}
