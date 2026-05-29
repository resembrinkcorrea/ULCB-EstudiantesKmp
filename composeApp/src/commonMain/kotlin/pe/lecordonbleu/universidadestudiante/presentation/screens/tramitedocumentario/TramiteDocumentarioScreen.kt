package pe.lecordonbleu.universidadestudiante.presentation.screens.tramitedocumentario

import androidx.compose.foundation.background
import androidx.compose.ui.draw.clip
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import pe.lecordonbleu.universidadestudiante.presentation.components.StandardTopBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import kotlinx.datetime.LocalDate
import kotlinx.serialization.json.contentOrNull
import kotlinx.serialization.json.intOrNull
import kotlinx.serialization.json.jsonArray
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive
import pe.lecordonbleu.universidadestudiante.LoadingIndicator
import pe.lecordonbleu.universidadestudiante.data.remote.dto.CarreraRemote
import pe.lecordonbleu.universidadestudiante.data.remote.dto.CorreccionDocumentos
import pe.lecordonbleu.universidadestudiante.data.remote.dto.EstadoTramite
import pe.lecordonbleu.universidadestudiante.data.remote.dto.ResponseTramiteDocFiltro
import pe.lecordonbleu.universidadestudiante.data.remote.dto.TReTramiteItem
import pe.lecordonbleu.universidadestudiante.data.remote.dto.TipoTramite
import pe.lecordonbleu.universidadestudiante.data.remote.dto.Tramite
import pe.lecordonbleu.universidadestudiante.data.remote.dto.TramiteDocumentos
import pe.lecordonbleu.universidadestudiante.data.remote.dto.TrcTramiteItem
import pe.lecordonbleu.universidadestudiante.getColorsTheme
import pe.lecordonbleu.universidadestudiante.getPlatformContext
import pe.lecordonbleu.universidadestudiante.getSettingsStorage
import pe.lecordonbleu.universidadestudiante.getTodayLocalDate
import pe.lecordonbleu.universidadestudiante.presentation.components.AppDropdownMenu
import pe.lecordonbleu.universidadestudiante.presentation.components.dialogs.CustomDialogBasic
import pe.lecordonbleu.universidadestudiante.presentation.screens.tramitedocumentario.customcell.DocumentoItemCard
import pe.lecordonbleu.universidadestudiante.presentation.screens.tramitedocumentario.customcell.FiltrosTramiteExpandable
import pe.lecordonbleu.universidadestudiante.presentation.vo.ResourceUiState
import pe.lecordonbleu.universidadestudiante.util.openPdfFromBase64

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TramiteDocumentarioScreen(
    viewModel: TramiteDocumentarioViewModel,
    navigator: NavController
) {
    val colors = getColorsTheme()
    val context = getPlatformContext()
    val settings = getSettingsStorage()
    val idUsuario = settings.getInt("idUsuario", settings.getInt("id_usuario", 0))
    val idTipoUsuario = settings.getInt("idTipoUsuario", settings.getInt("id_tipo_usuario", 0))
    val idUneg = settings.getInt("id_uneg", 1)
    val idEstud = settings.getInt("idEstud", 0)

    val today = getTodayLocalDate()
    val primerDiaMes = LocalDate(today.year, today.month, 1)
    val fechaInicioDefault = "${
        primerDiaMes.dayOfMonth.toString().padStart(2, '0')
    }/${primerDiaMes.monthNumber.toString().padStart(2, '0')}/${primerDiaMes.year}"
    val fechaFinDefault = "${today.dayOfMonth.toString().padStart(2, '0')}/${
        today.monthNumber.toString().padStart(2, '0')
    }/${today.year}"

    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()
    val scrollState = rememberLazyListState()

    var isRefreshing by remember { mutableStateOf(false) }
    var isLoadingComprobante by remember { mutableStateOf(false) }
    var filtrosExpandido by rememberSaveable { mutableStateOf(true) }

    var filtroData by remember { mutableStateOf<ResponseTramiteDocFiltro?>(null) }
    val tramites = filtroData?.Tramite ?: emptyList()

    var idEstudServ by remember { mutableStateOf<Int?>(null) }
    var idPestDet by remember { mutableStateOf<Int?>(null) }
    var idEstudPe by remember { mutableStateOf(0) }
    var idTipoServa by remember { mutableStateOf<Int?>(null) }
    var idServ by remember { mutableStateOf<Int?>(null) }

    var selectedTipoTramite by remember { mutableStateOf<TipoTramite?>(null) }
    var selectedEstado by remember { mutableStateOf<EstadoTramite?>(null) }
    var selectedTramite by remember { mutableStateOf<Tramite?>(null) }
    var fechaInicio by remember { mutableStateOf(fechaInicioDefault) }
    var fechaFin by remember { mutableStateOf(fechaFinDefault) }

    var documentos by remember { mutableStateOf(emptyList<TramiteDocumentos>()) }
    var carreras by remember { mutableStateOf(emptyList<CarreraRemote?>()) }
    val requisitosMap = remember { mutableStateMapOf<String, List<TReTramiteItem>>() }
    var trcList by remember { mutableStateOf<List<TrcTramiteItem>>(emptyList()) }
    var itemExpandidoId by remember { mutableStateOf<String?>(null) }
    var selectedCarrera by remember { mutableStateOf<CarreraRemote?>(null) }
    var showDialogCorreccion by remember { mutableStateOf(false) }
    var dialogData by remember { mutableStateOf<CorreccionDocumentos?>(null) }
    var filtrosLanzados by remember { mutableStateOf(false) }

    val uiStateCarrera by viewModel.uiState.collectAsStateWithLifecycle()
    val uiStateDocumentosCreados by viewModel.uiStateDocumentosCreados.collectAsStateWithLifecycle()
    val uiStateTramiteCombo by viewModel.uiStateTramiteDocFiltro.collectAsStateWithLifecycle()
    val uiStateTRCFiltro by viewModel.uiStateTRCFiltro.collectAsStateWithLifecycle()
    val uiStateVerificarComprobante by viewModel.uiStateVerificarComprobante.collectAsStateWithLifecycle()
    val uiStateRequisitosComprobante by viewModel.uiStateTREFiltro.collectAsStateWithLifecycle()
    val uiStateCorreccionTramiteSave by viewModel.uiStateCorreccionTramiteSave.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.setUserCarreraRequest(idEstud)
    }

    Scaffold(
        topBar = {
            StandardTopBar(
                title = "TRAMITE DOCUMENTARIO",
                subtitle = "Gestión de Trámites",
                onBackClick = { navigator.popBackStack() },
                scrollBehavior = scrollBehavior
            )
        },
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        containerColor = colors.backGroundColor
    ) { paddingValues ->
        PullToRefreshBox(
            isRefreshing = isRefreshing,
            onRefresh = {
                isRefreshing = true
                selectedTipoTramite = null
                selectedEstado = null
                selectedTramite = null
                fechaInicio = fechaInicioDefault
                fechaFin = fechaFinDefault
                filtroData = null
                documentos = emptyList()
                itemExpandidoId = null
                trcList = emptyList()
                requisitosMap.clear()
                filtrosExpandido = true
                filtrosLanzados = false
                viewModel.setUserCarreraRequest(idEstud)
            },
            modifier = Modifier.padding(paddingValues)
        ) {
        LazyColumn(
            state = scrollState,
            contentPadding = PaddingValues(bottom = 16.dp),
            modifier = Modifier.fillMaxSize()
        ) {
            item {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 8.dp, vertical = 12.dp),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = colors.colorExpenseItem),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Column {
                        Column(modifier = Modifier.padding(12.dp)) {
                            AppDropdownMenu(
                                items = carreras.filterNotNull(),
                                selectedItem = selectedCarrera,
                                label = "Programa Académico",
                                itemLabel = { it.serv_nombre },
                                onItemSelected = { carrera ->
                                    selectedCarrera = carrera
                                    idEstudServ = carrera.id_estud_serv.toIntOrNull()
                                    idPestDet = carrera.id_pest_det.toIntOrNull()
                                    idEstudPe = carrera.id_estud_pe
                                    idTipoServa = carrera.id_tiposerva.toIntOrNull()
                                    idServ = carrera.id_serv.toIntOrNull()
                                    filtrosLanzados = false
                                }
                            )
                        }

                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 8.dp)
                                .padding(bottom = 8.dp),
                            shape = RoundedCornerShape(12.dp),
                            colors = CardDefaults.cardColors(containerColor = colors.colorBlancoGris),
                            elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
                        ) {
                            FiltrosTramiteExpandable(
                                estados = filtroData?.Estados ?: emptyList(),
                                tiposTramite = filtroData?.TipoTramite ?: emptyList(),
                                tramites = filtroData?.Tramite ?: emptyList(),
                                selectedEstado = selectedEstado,
                                selectedTipoTramite = selectedTipoTramite,
                                selectedTramite = selectedTramite,
                                onEstadoSelected = {
                                    selectedEstado = it
                                    viewModel.filtrarDocumentos(idEstud, idUneg, idUsuario, idTipoUsuario, idTipoServa ?: 0, it, selectedTipoTramite, selectedTramite, fechaInicio, fechaFin)
                                },
                                onTipoTramiteSelected = {
                                    selectedTipoTramite = it
                                    viewModel.filtrarDocumentos(idEstud, idUneg, idUsuario, idTipoUsuario, idTipoServa ?: 0, selectedEstado, it, selectedTramite, fechaInicio, fechaFin)
                                },
                                onTramiteSelected = {
                                    selectedTramite = it
                                    viewModel.filtrarDocumentos(idEstud, idUneg, idUsuario, idTipoUsuario, idTipoServa ?: 0, selectedEstado, selectedTipoTramite, it, fechaInicio, fechaFin)
                                },
                                fechaInicio = fechaInicio,
                                fechaFin = fechaFin,
                                onFechaInicioChange = {
                                    fechaInicio = it
                                    viewModel.filtrarDocumentos(idEstud, idUneg, idUsuario, idTipoUsuario, idTipoServa ?: 0, selectedEstado, selectedTipoTramite, selectedTramite, it, fechaFin)
                                },
                                onFechaFinChange = {
                                    fechaFin = it
                                    viewModel.filtrarDocumentos(idEstud, idUneg, idUsuario, idTipoUsuario, idTipoServa ?: 0, selectedEstado, selectedTipoTramite, selectedTramite, fechaInicio, it)
                                },
                                isInitiallyExpanded = filtrosExpandido,
                                onExpandedChange = { filtrosExpandido = it }
                            )
                        }
                    } // Column
                } // Card padre
            }

            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 8.dp, vertical = 8.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(colors.colorAzulOscuro)
                        .padding(horizontal = 12.dp, vertical = 3.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text("Listado de Documentos", color = Color.White, fontSize = 16.sp)

                    Button(
                        onClick = {
                            navigator.navigate("/registrarTramite")
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = Color.White),
                        shape = RoundedCornerShape(8.dp),
                        elevation = ButtonDefaults.buttonElevation(defaultElevation = 4.dp)
                    ) {
                        Icon(
                            Icons.Default.Add,
                            contentDescription = "Nuevo",
                            tint = colors.secondary
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Nuevo", color = colors.secondary)
                    }
                }

            }

            items(
                items = documentos,
                key = { it.id_tramite_estud.ifBlank { it.N } }
            ) { tramite ->
                val tramiteId = tramite.id_tramite_estud
                DocumentoItemCard(
                    tramite = tramite,
                    isExpanded = itemExpandidoId == tramiteId,
                    requisitos = requisitosMap[tramiteId].orEmpty(),
                    trcList = if (itemExpandidoId == tramiteId) trcList else emptyList(),
                    onComprobante = { comprobante ->
                        viewModel.setVerificarComprobante(comprobante, idUneg)
                    },
                    onClickItem = { itemSeleccionado ->
                        val id = itemSeleccionado.id_tramite_estud
                        itemExpandidoId = if (itemExpandidoId == id) null else id
                        trcList = emptyList()
                        if (itemExpandidoId == id) {
                            viewModel.setTramiteDocFiltroRequest(
                                id_uneg = idUneg,
                                id_estud = idEstud,
                                tipoCombo = "TRE",
                                idEstado = 0,
                                idTipoTramite = 0,
                                idTramite = 0,
                                fechaInicio = 0,
                                fechaFin = 0,
                                idTramiteEstud = id.toIntOrNull() ?: 0,
                                idTramiteDt = itemSeleccionado.id_tramite.toIntOrNull() ?: 0,
                                idTipoServa = idTipoServa ?: 0,
                                id_sistema = 13,
                                cantidadMultiple = 0,
                                id_pest_det = idPestDet ?: 0,
                                id_estud_pe = idEstudPe,
                                id_estud_serv = idEstudServ ?: 0
                            )
                        }
                    },
                    onCorregirClick = {
                        viewModel.setTramiteDocFiltroRequest(
                            id_uneg = idUneg,
                            id_estud = idEstud ?: 0,
                            tipoCombo = "TRC",
                            idEstado = 0,
                            idTipoTramite = 0,
                            idTramite = 0,
                            fechaInicio = 0,
                            fechaFin = 0,
                            idTramiteEstud = tramite.id_tramite_estud.toIntOrNull() ?: 0,
                            idTramiteDt = tramite.id_tramite.toIntOrNull() ?: 0,
                            idTipoServa = 0,
                            id_sistema = 13,
                            cantidadMultiple = 0,
                            id_pest_det = 0,
                            id_estud_pe = 0,
                            id_estud_serv = 0
                        )
                    },
                    saveClickCorregir = { textoCorregido ->
                        if (textoCorregido.isNotBlank()) {
                            viewModel.setCorreccionTramiteSaveRequest(
                                descripcion = textoCorregido,
                                id_sistema = 13,
                                id_tramite = tramite.id_tramite.toIntOrNull() ?: 0,
                                id_usuario = idUsuario,
                                id_tipo_usuario = idTipoUsuario,
                                idTramiteEstud = tramite.id_tramite_estud.toIntOrNull() ?: 0,
                                id_uneg = idUneg,
                                id_estud = idEstud,
                                condicion = 5
                            )
                        }
                    }
                )
            }
        }
        } // PullToRefreshBox
    }

    when (val s = uiStateCarrera) {
        is ResourceUiState.Success -> {
            carreras = s.data.firstOrNull()?.carrera ?: emptyList()
            selectedCarrera = when {
                selectedCarrera == null -> carreras.firstOrNull()
                carreras.none { it?.serv_nombre == selectedCarrera?.serv_nombre } -> carreras.firstOrNull()
                else -> selectedCarrera
            }
            selectedCarrera?.let {
                idEstudServ = it.id_estud_serv.toIntOrNull()
                idPestDet = it.id_pest_det.toIntOrNull()
                idTipoServa = it.id_tiposerva.toIntOrNull()
                idServ = it.id_serv.toIntOrNull()
                idEstudPe = it.id_estud_pe
                if (!filtrosLanzados) {
                    filtrosLanzados = true
                    viewModel.setTramiteDocFiltroRequest(
                        id_uneg = idUneg,
                        id_estud = it.id_estud.toIntOrNull() ?: 0,
                        tipoCombo = "CIM",
                        idEstado = 0,
                        idTipoTramite = 0,
                        idTramite = 0,
                        fechaInicio = 0,
                        fechaFin = 0,
                        idTramiteEstud = 0,
                        idTramiteDt = 0,
                        idTipoServa = it.id_tiposerva.toIntOrNull() ?: 0,
                        id_sistema = 13,
                        cantidadMultiple = 0,
                        id_pest_det = it.id_pest_det.toIntOrNull() ?: 0,
                        id_estud_pe = 0,
                        id_estud_serv = it.id_estud_serv.toIntOrNull() ?: 0
                    )
                    viewModel.setDocumentosCreadosRequest(
                        idTramite = selectedTramite?.id?.toIntOrNull() ?: 0,
                        idSistema = 13,
                        idEstado = selectedEstado?.id_paragene?.toIntOrNull() ?: 0,
                        fechaInicio = fechaInicio,
                        idUsuario = idUsuario,
                        idTipoUsuario = idTipoUsuario,
                        idUNEG = idUneg,
                        idTipoTramite = selectedTipoTramite?.id?.toIntOrNull() ?: 0,
                        condicion = 1,
                        fechaFin = fechaFin,
                        idEstud = idEstud,
                        idTipoServa = it.id_tiposerva.toIntOrNull() ?: 0
                    )
                }
            }
        }

        is ResourceUiState.Error -> carreras = emptyList()
        is ResourceUiState.Loading -> Unit
        ResourceUiState.Empty -> Unit
    }

    when (val s = uiStateTramiteCombo) {
        is ResourceUiState.Success -> {
            filtroData = s.data
            viewModel.resetTramiteDocFiltroState()
        }

        is ResourceUiState.Error -> {
            filtroData = null
            viewModel.resetTramiteDocFiltroState()
        }

        is ResourceUiState.Loading -> Unit
        ResourceUiState.Empty -> Unit
    }

    when (val s = uiStateDocumentosCreados) {
        is ResourceUiState.Success -> { documentos = s.data.TramiteDocumentos; isRefreshing = false }
        is ResourceUiState.Error -> { documentos = emptyList(); isRefreshing = false }
        is ResourceUiState.Loading -> Unit
        ResourceUiState.Empty -> Unit
    }

    when (val s = uiStateTRCFiltro) {
        is ResourceUiState.Success -> {
            trcList = s.data["TRCData"]?.jsonArray?.mapNotNull { item ->
                val obj = item.jsonObject
                try {
                    TrcTramiteItem(
                        contador = obj["contador"]?.jsonPrimitive?.intOrNull ?: 0,
                        descripcion = obj["Descripcion"]?.jsonPrimitive?.contentOrNull.orEmpty(),
                        accionHtml = obj["ACCION"]?.jsonPrimitive?.contentOrNull.orEmpty(),
                        estadoHtml = obj["Estado"]?.jsonPrimitive?.contentOrNull.orEmpty()
                    )
                } catch (_: Exception) {
                    null
                }
            }.orEmpty()
            viewModel.resetTramiteTRComboState()
        }

        is ResourceUiState.Error -> viewModel.resetTramiteTRComboState()
        else -> Unit
    }

    when (val s = uiStateVerificarComprobante) {
        is ResourceUiState.Loading -> isLoadingComprobante = true
        is ResourceUiState.Success -> {
            val base64Pdf = s.data.resultado
            if (base64Pdf.isNotBlank()) openPdfFromBase64(context, base64Pdf)
            viewModel.resetVerificarComprobanteState()
            isLoadingComprobante = false
        }

        is ResourceUiState.Error -> {
            viewModel.resetVerificarComprobanteState()
            isLoadingComprobante = false
        }

        is ResourceUiState.Empty -> isLoadingComprobante = false
        else -> Unit
    }

    when (val s = uiStateRequisitosComprobante) {
        is ResourceUiState.Success -> {
            val treData = s.data["TREData1"]?.jsonArray?.takeIf { it.isNotEmpty() }
                ?: s.data["TREData"]?.jsonArray?.takeIf { it.isNotEmpty() }
            val requisitos = mapTReItems(treData?.map { it.jsonObject }.orEmpty())
            val idTramiteEstud = requisitos.firstOrNull()?.id_tramite_estud
            if (!idTramiteEstud.isNullOrBlank()) {
                requisitosMap[idTramiteEstud] = requisitos
            }
            viewModel.resetTREFiltroState()
        }

        is ResourceUiState.Error -> viewModel.resetTREFiltroState()
        else -> Unit
    }

    when (val s = uiStateCorreccionTramiteSave) {
        is ResourceUiState.Success -> {
            isLoadingComprobante = false
            val docs = s.data.CorreccionDocumentos
            if (docs.isNotEmpty()) {
                dialogData = docs.first()
                showDialogCorreccion = true
            }
        }

        is ResourceUiState.Error -> {
            isLoadingComprobante = false
            if (!showDialogCorreccion) {
                showDialogCorreccion = true
                dialogData = null
            }
        }

        is ResourceUiState.Empty -> isLoadingComprobante = false
        is ResourceUiState.Loading -> Unit
        else -> Unit
    }

    if (isLoadingComprobante) {
        LoadingIndicator()
    }

    if (showDialogCorreccion) {
        CustomDialogBasic(
            visible = true,
            titulo = dialogData?.titulo ?: "TRAMITE DOCUMENTARIO",
            mensaje = dialogData?.mensaje ?: "Se registro la correccion.",
            flag_val = 1,
            confirmado = true,
            onDismiss = {
                showDialogCorreccion = false
                dialogData = null
                viewModel.resetCorreccionTramiteState()
            }
        )
    }

}

