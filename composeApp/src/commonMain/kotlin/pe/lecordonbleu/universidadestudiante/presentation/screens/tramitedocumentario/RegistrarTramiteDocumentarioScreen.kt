package pe.lecordonbleu.universidadestudiante.presentation.screens.tramitedocumentario

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChatBubble
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.contentOrNull
import kotlinx.serialization.json.intOrNull
import kotlinx.serialization.json.jsonArray
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive
import pe.lecordonbleu.universidadestudiante.core.config.Constantes
import pe.lecordonbleu.universidadestudiante.data.remote.dto.CarreraRemote
import pe.lecordonbleu.universidadestudiante.data.remote.dto.ListPaises
import pe.lecordonbleu.universidadestudiante.data.remote.dto.ListPerfilEstudiante
import pe.lecordonbleu.universidadestudiante.data.remote.dto.TipoEntrega
import pe.lecordonbleu.universidadestudiante.data.remote.dto.Tramite
import pe.lecordonbleu.universidadestudiante.domain.model.DuplicadoTituloGuardarRequest
import pe.lecordonbleu.universidadestudiante.domain.model.PagoDetalleTemporal
import pe.lecordonbleu.universidadestudiante.domain.model.PagoNameValuePairs
import pe.lecordonbleu.universidadestudiante.domain.model.PagosNameValuePairs
import pe.lecordonbleu.universidadestudiante.domain.model.PagosTopLevel
import pe.lecordonbleu.universidadestudiante.domain.model.RequisitoItemRequest
import pe.lecordonbleu.universidadestudiante.domain.model.RequisitoTramiteC
import pe.lecordonbleu.universidadestudiante.domain.model.RequisitoTramiteModoCheck
import pe.lecordonbleu.universidadestudiante.domain.model.RequisitosRequest
import pe.lecordonbleu.universidadestudiante.domain.model.TemporalBody
import pe.lecordonbleu.universidadestudiante.domain.model.TemporalCuentaCorrienteRequest
import pe.lecordonbleu.universidadestudiante.domain.model.namePairs
import pe.lecordonbleu.universidadestudiante.getColorsTheme
import pe.lecordonbleu.universidadestudiante.getSettingsStorage
import pe.lecordonbleu.universidadestudiante.getTodayLocalDateTime
import pe.lecordonbleu.universidadestudiante.presentation.components.AppDropdownMenu
import pe.lecordonbleu.universidadestudiante.presentation.components.LockedRow
import pe.lecordonbleu.universidadestudiante.presentation.components.ProgressBarLoading
import pe.lecordonbleu.universidadestudiante.presentation.components.StandardTopBar
import pe.lecordonbleu.universidadestudiante.presentation.components.StatusLabel
import pe.lecordonbleu.universidadestudiante.presentation.components.dialogs.CustomDialogBasic
import pe.lecordonbleu.universidadestudiante.presentation.screens.tramitedocumentario.customcell.AccionIconButton
import pe.lecordonbleu.universidadestudiante.presentation.screens.tramitedocumentario.customcell.DatosRecojo
import pe.lecordonbleu.universidadestudiante.presentation.screens.tramitedocumentario.customcell.DialogEntregaPresencial
import pe.lecordonbleu.universidadestudiante.presentation.screens.tramitedocumentario.customcell.MostrarFormularioPorTipo
import pe.lecordonbleu.universidadestudiante.presentation.screens.tramitedocumentario.customcell.RequisitoSeleccionado
import pe.lecordonbleu.universidadestudiante.presentation.screens.tramitedocumentario.customcell.RequisitoTramiteB
import pe.lecordonbleu.universidadestudiante.presentation.screens.tramitedocumentario.customcell.RequisitoTramiteD
import pe.lecordonbleu.universidadestudiante.presentation.screens.tramitedocumentario.customcell.stripHtml
import pe.lecordonbleu.universidadestudiante.presentation.vo.ResourceUiState
import pe.lecordonbleu.universidadestudiante.util.Base64Encoder
import kotlin.math.round

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegistrarTramiteDocumentarioScreen(
    viewModel: RegistrarTramiteDocumentarioViewModel,
    navigator: NavController
) {
    val colors = getColorsTheme()
    val focusManager = LocalFocusManager.current
    val settings = getSettingsStorage()
    val idSistema = 13
    val idUneg = settings.getInt("id_uneg", 1)
    val idEstud = settings.getInt("idEstud", 0)
    val idTipoUsuario = settings.getInt("idTipoUsuario", settings.getInt("id_tipo_usuario", 0))
    val idUsuario = settings.getInt("idUsuario", settings.getInt("id_usuario", 0))

    val scrollState = rememberScrollState()
    val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior()

    val modalidades = listOf("Seleccionar")
    val onCorregirClick: () -> Unit = {}
    val onDismiss: () -> Unit = { navigator.popBackStack() }

    val uiStateTramiteDocFiltro by viewModel.uiStateTramiteDocFiltro.collectAsStateWithLifecycle()
    val uiStatePaises by viewModel.uiStatePaises.collectAsStateWithLifecycle()

    var tramites by remember { mutableStateOf(emptyList<Tramite>()) }
    var paises by remember { mutableStateOf(emptyList<ListPaises?>()) }
    var paisSeleccionado by remember { mutableStateOf<ListPaises?>(null) }

    val uiStateCrearTramites by viewModel.uiStateCrearTramites.collectAsStateWithLifecycle()
    val uiStateTTRFiltro by viewModel.uiStateTTRFiltro.collectAsStateWithLifecycle()
    val uiStateTREFiltro by viewModel.uiStateTREFiltro.collectAsStateWithLifecycle()
    val uiStateValidarEgresado by viewModel.uiStateValidarEgresado.collectAsStateWithLifecycle()
    val uiStateRegistrarTramite by viewModel.uiStateRegistrarTramite.collectAsStateWithLifecycle()
    val uiStatePerfil by viewModel.uiStatePerfilEstudiante.collectAsStateWithLifecycle()
    val uiStateTemporalCuentaCorriente by viewModel.uiStateTemporalCuentaCorriente.collectAsStateWithLifecycle()
    val uiStateRegistrarTramiteTemp by viewModel.uiStateRegistrarTramiteTemp.collectAsStateWithLifecycle()
    val uiStateGuardarArchivoTramite by viewModel.uiStateGuardarArchivo.collectAsStateWithLifecycle()
    val uiStateDuplicadoTitulo by viewModel.uiStateDuplicadoTitulo.collectAsStateWithLifecycle()
    val uiStateCarrera by viewModel.uiStateCarrera.collectAsStateWithLifecycle()

    val opcionSeleccione =
        remember { Tramite(tipo = "", contador = 0, id = "0", nombre = "SELECCIONE") }
    val tramitesCombo = remember(tramites) { listOf(opcionSeleccione) + tramites }

    var perfilEstudiante by remember { mutableStateOf<ListPerfilEstudiante?>(null) }
    var selectedTramite by remember { mutableStateOf(opcionSeleccione) }
    var selectedModalidad by remember { mutableStateOf(modalidades.firstOrNull() ?: "Seleccionar") }
    var motivo by remember { mutableStateOf("") }
    var fechaSolicitante by remember { mutableStateOf("") }
    var requisitos by remember { mutableStateOf("NO") }
    var monto by remember { mutableStateOf("0") }
    var tipoTramite by remember { mutableStateOf("") }
    var tipoEntregaList by remember { mutableStateOf(emptyList<TipoEntrega>()) }
    var selectedTipoEntrega by remember { mutableStateOf<TipoEntrega?>(null) }
    var modalidadHabilitada by remember { mutableStateOf(false) }
    var showDialogRecojo by remember { mutableStateOf(false) }
    var datosRecojo by remember { mutableStateOf(DatosRecojo()) }
    var idTipoTramiteDT by remember { mutableStateOf(0) }
    var idTariGen by remember { mutableStateOf("") }
    var tariGenCodNav by remember { mutableStateOf("") }
    var requisitosList by remember { mutableStateOf(emptyList<Any>()) }
    var idTipoEntrega by remember { mutableStateOf(0) }
    var modalPresencial by remember { mutableStateOf(0) }
    var flagPago by remember { mutableStateOf(0) }
    var idTempRequisitos by remember { mutableStateOf(0) }
    var showProgress by remember { mutableStateOf(false) }

    var requisitosSeleccionadosA by remember { mutableStateOf(emptyList<RequisitoSeleccionado>()) }
    var requisitosSeleccionadosE by remember { mutableStateOf(emptyList<RequisitoSeleccionado>()) }
    var requisitosSeleccionadosB by remember { mutableStateOf(emptyList<RequisitoTramiteB>()) }
    var requisitosSeleccionadosC by remember { mutableStateOf(emptyList<RequisitoTramiteC>()) }
    var requisitosSeleccionadosD by remember { mutableStateOf(emptyList<RequisitoTramiteD>()) }
    var requisitosSeleccionadosH by remember { mutableStateOf(emptyList<RequisitoTramiteC>()) }
    var requisitosSeleccionadosL by remember { mutableStateOf(emptyList<RequisitoTramiteC>()) }
    var image64 by remember { mutableStateOf("") }
    var pdf64 by remember { mutableStateOf("") }

    var carreras by remember { mutableStateOf(emptyList<CarreraRemote?>()) }
    var selectedCarrera by remember { mutableStateOf<CarreraRemote?>(null) }
    var idTipoServa by remember { mutableStateOf(0) }
    var idEstudServ by remember { mutableStateOf(0) }
    var idPestDet by remember { mutableStateOf(0) }
    var idEstudPe by remember { mutableStateOf(0) }
    var idServ by remember { mutableStateOf(0) }

    var showAlertDialog by remember { mutableStateOf(false) }
    var alertTitulo by remember { mutableStateOf("") }
    var alertMensaje by remember { mutableStateOf("") }
    var alertConfirmado by remember { mutableStateOf(false) }
    var alertFlag by remember { mutableStateOf(0) }
    var showSuccessDialog by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        viewModel.setUserCarreraRequest(idEstud)
        viewModel.setTramitePaisesRequest(idUneg)
        viewModel.setPerfilEstudianteRequest(idUsuario)
    }

    val onSolicitar: () -> Unit = {
        when {
            idTipoTramiteDT == 0 || motivo.isBlank() || idTipoEntrega == 0 -> {
                alertTitulo = "Solicitar Tramite"
                alertMensaje = "Datos del tramite, tipo de entrega y/o motivo incompleto. Por favor ingresa los datos."
                alertFlag = 0
                alertConfirmado = false
                showAlertDialog = true
                showProgress = false
            }
            modalPresencial == 1 && datosRecojo.recoger == -1 -> {
                alertTitulo = "ATENCION"
                alertMensaje = "Debes completar los datos de recojo antes de continuar."
                alertFlag = 0
                alertConfirmado = false
                showAlertDialog = true
                showProgress = false
            }
            else -> {
                if (modalPresencial == 0 && datosRecojo.recoger == -1) {
                    datosRecojo = DatosRecojo(recoger = 0)
                }
                showProgress = true
                viewModel.setValidarEgresadoRequest(
                    id_sistema = idSistema,
                    id_estud_pe = idEstudPe,
                    id_pest_det = idPestDet,
                    idTramiteDt = idTipoTramiteDT,
                    id_tipo_usuario = idTipoUsuario,
                    id_estud_serv = idEstudServ,
                    id_uneg = idUneg,
                    id_estud = idEstud
                )
            }
        }
    }

    Scaffold(
        topBar = {
            StandardTopBar(
                title = "TRAMITE DOCUMENTARIO",
                subtitle = "Solicitud de Tramite Documentario",
                onBackClick = onDismiss,
                scrollBehavior = scrollBehavior
            )
        },
        floatingActionButton = {
            androidx.compose.material3.Button(
                onClick = onSolicitar,
                enabled = !showProgress,
                shape = RoundedCornerShape(50.dp),
                colors = androidx.compose.material3.ButtonDefaults.buttonColors(
                    containerColor = colors.primary,
                    disabledContainerColor = colors.primary.copy(alpha = 0.5f)
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 32.dp)
            ) {
                Text(
                    "Solicitar trámite",
                    color = Color.White,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 15.sp
                )
            }
        },
        floatingActionButtonPosition = androidx.compose.material3.FabPosition.Center,
        modifier = Modifier.fillMaxSize().nestedScroll(scrollBehavior.nestedScrollConnection),
        containerColor = colors.backGroundColor
    )
    { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 16.dp, vertical = 8.dp)
                .verticalScroll(scrollState)
                .pointerInput(Unit) {
                    detectTapGestures(onTap = { focusManager.clearFocus() })
                },
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = colors.colorExpenseItem),
                elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
            )
            {
            Column(
                modifier = Modifier.padding(12.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
            Text(
                "Información General",
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp,
                color = colors.textColor
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    "Nro",
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    color = colors.textColor
                )
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    if (selectedTipoEntrega?.modal_presencial == "1") {
                        AccionIconButton(
                            icono = Icons.Default.Person,
                            colorFondo = colors.secondary,
                            descripcion = "Recojo",
                            onClick = { showDialogRecojo = true }
                        )
                    }
                    AccionIconButton(
                        icono = Icons.Default.ChatBubble,
                        colorFondo = colors.colorRojo,
                        descripcion = "Corregir",
                        onClick = onCorregirClick
                    )
                }
            }

            AppDropdownMenu(
                items = carreras.filterNotNull(),
                selectedItem = selectedCarrera,
                label = "Programa Académico",
                itemLabel = { it.serv_nombre },
                onItemSelected = { nuevaSeleccion ->
                    selectedCarrera = nuevaSeleccion
                    selectedCarrera?.let {
                        idEstudServ = it.id_estud_serv.toIntOrNull() ?: 0
                        idPestDet = it.id_pest_det.toIntOrNull() ?: 0
                        idEstudPe = it.id_estud_pe
                        idTipoServa = it.id_tiposerva.toIntOrNull() ?: 0
                        idServ = it.id_serv.toIntOrNull() ?: 0
                        tramites = emptyList()
                        selectedTramite = opcionSeleccione
                        tipoEntregaList = emptyList()
                        selectedTipoEntrega = null
                        requisitosList = emptyList()
                        requisitos = "NO"
                        monto = "0"
                        tipoTramite = ""
                        idTipoEntrega = 0
                        modalPresencial = 0
                        idTipoTramiteDT = 0
                        viewModel.setTramiteDocFiltroRequest(
                            id_uneg = idUneg,
                            id_estud = it.id_estud.toIntOrNull() ?: idEstud,
                            tipoCombo = "CIM",
                            idEstado = 0, idTipoTramite = 0, idTramite = 0,
                            fechaInicio = 0, fechaFin = 0, idTramiteEstud = 0, idTramiteDt = 0,
                            idTipoServa = it.id_tiposerva.toIntOrNull() ?: 0,
                            id_sistema = idSistema, cantidadMultiple = 0,
                            id_pest_det = it.id_pest_det.toIntOrNull() ?: 0,
                            id_estud_pe = 0,
                            id_estud_serv = it.id_estud_serv.toIntOrNull() ?: 0
                        )
                        viewModel.setCrearTramitesRequest(
                            id_sistema = idSistema,
                            id_tiposervad = it.id_serv.toIntOrNull() ?: 0,
                            id_usuario = idUsuario,
                            id_tipo_usuario = idTipoUsuario,
                            idUNEG = idUneg,
                            condicion = 2,
                            id_estud = idEstud
                        )
                    }
                }
            )

            AppDropdownMenu(
                items = paises.filterNotNull(),
                selectedItem = paisSeleccionado,
                label = "País",
                itemLabel = { it.pais_nombre },
                onItemSelected = { paisSeleccionado = it },
                enabled = paises.isNotEmpty()
            )

            AppDropdownMenu(
                items = tramitesCombo,
                selectedItem = selectedTramite,
                label = "Tramite",
                itemLabel = { it.nombre },
                onItemSelected = { tramiteSeleccionado ->
                    selectedTramite = tramiteSeleccionado
                    tipoEntregaList = emptyList()
                    selectedTipoEntrega = null
                    requisitosList = emptyList()
                    requisitos = "NO"
                    monto = "0"
                    tipoTramite = ""
                    idTipoEntrega = 0
                    modalPresencial = 0
                    idTipoTramiteDT = tramiteSeleccionado.id.toIntOrNull() ?: 0
                    if (idTipoTramiteDT != 0) {
                        viewModel.setTramiteDocFiltroRequest(
                            id_uneg = idUneg,
                            id_estud = idEstud,
                            tipoCombo = "TTR",
                            idEstado = 0,
                            idTipoTramite = 0,
                            idTramite = 0,
                            fechaInicio = 0,
                            fechaFin = 0,
                            idTramiteEstud = 0,
                            idTramiteDt = idTipoTramiteDT,
                            idTipoServa = idTipoServa,
                            id_sistema = idSistema,
                            cantidadMultiple = 0,
                            id_pest_det = 0,
                            id_estud_pe = 0,
                            id_estud_serv = 0
                        )
                    }
                }
            )

            OutlinedTextField(
                value = motivo,
                onValueChange = { motivo = it },
                label = { Text("Motivo del Trámite") },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedTextColor = colors.textColor,
                    unfocusedTextColor = colors.textColor,
                    focusedBorderColor = colors.secondary,
                    unfocusedBorderColor = colors.colorGrisNeutro
                )
            )

            }
            }

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = colors.colorExpenseItem),
                elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
            )
            {
                Column(
                    modifier = Modifier.padding(12.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Text(
                        "Entrega y Modalidad",
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp,
                        color = colors.textColor
                    )
                    AppDropdownMenu(
                        items = tipoEntregaList,
                        selectedItem = selectedTipoEntrega,
                        label = "Tipo de entrega",
                        itemLabel = { it.nombre },
                        onItemSelected = { tipoEntrega ->
                            selectedTipoEntrega = tipoEntrega
                            idTipoEntrega = tipoEntrega.id.toIntOrNull() ?: 0
                            modalPresencial = tipoEntrega.modal_presencial.toIntOrNull() ?: 0
                            if (modalPresencial == 0) datosRecojo = DatosRecojo(recoger = 0)
                        },
                        enabled = tipoEntregaList.isNotEmpty()
                    )
                    AppDropdownMenu(
                        items = modalidades,
                        selectedItem = selectedModalidad,
                        label = "Modalidad",
                        itemLabel = { it },
                        onItemSelected = { selectedModalidad = it },
                        enabled = modalidadHabilitada
                    )
                }
            }

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = colors.colorExpenseItem),
                elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
            )
            {
                Column(modifier = Modifier.padding(12.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Text(
                        "Resumen y Estado",
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp,
                        color = colors.textColor
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text("Requisitos: $requisitos", color = colors.textColor, fontSize = 13.sp)
                        Text(
                            "Monto: S/ $monto",
                            fontWeight = FontWeight.Bold,
                            color = colors.colorMixPrimary,
                            fontSize = 16.sp
                        )
                    }

                    HorizontalDivider(color = colors.colorGrisNeutro.copy(alpha = 0.2f))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        StatusLabel("Estado de Pago:", colors.colorAmbar)
                        StatusLabel("Estado Trámite:", colors.colorAmbar)
                    }

                    LockedRow(label = "Comprobante")

                    LockedRow(label = "Archivo de Respuesta")

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text("Respuesta:", color = colors.textColor, fontSize = 13.sp)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("", color = colors.textColor, fontSize = 13.sp)
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            if (requisitosList.isNotEmpty()) {
                Row(
                    Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(6.dp))
                        .background(MaterialTheme.colorScheme.surfaceVariant)
                        .border(1.dp, colors.colorGrisNeutro, RoundedCornerShape(6.dp))
                        .padding(9.dp)
                ) {
                    Text(
                        "REQUISITO",
                        fontWeight = FontWeight.Bold,
                        fontSize = 12.sp,
                        modifier = Modifier.weight(1f)
                    )
                    Text(
                        "DATOCUMPLE",
                        fontWeight = FontWeight.Bold,
                        fontSize = 10.sp,
                        modifier = Modifier.weight(0.35f)
                    )
                    Text(
                        "EST.",
                        fontWeight = FontWeight.Bold,
                        fontSize = 10.sp,
                        modifier = Modifier.weight(0.11f)
                    )
                }
                Spacer(modifier = Modifier.height(8.dp))
                MostrarFormularioPorTipo(
                    tipoFormulario = TipoFormularioTramite.desdeTipoTramite(idTipoTramiteDT),
                    requisitosList = requisitosList,
                    onCheckedListUpdatedA = { requisitosSeleccionadosA = it },
                    onCheckedListUpdatedE = { requisitosSeleccionadosE = it },
                    onCheckedListUpdatedB = { requisitosSeleccionadosB = it },
                    onCheckedListUpdatedC = { lista, b64 ->
                        requisitosSeleccionadosC = lista
                        if (b64 != null) image64 = b64
                    },
                    flag_crear = true,
                    onCheckedListUpdatedD = { requisitosSeleccionadosD = it },
                    onCheckedListUpdatedH = { lista, b64 ->
                        requisitosSeleccionadosH = lista
                        if (b64 != null) pdf64 = b64
                    },
                    onCheckedListUpdatedL = { lista, b64 ->
                        requisitosSeleccionadosL = lista
                        if (b64 != null) pdf64 = b64
                    }
                )
            }

            Spacer(modifier = Modifier.height(80.dp))
        }
    }

    when (val s = uiStateCarrera) {
        is ResourceUiState.Success -> {
            carreras = s.data.firstOrNull()?.carrera ?: emptyList()
            if (selectedCarrera == null) {
                selectedCarrera = carreras.firstOrNull()
                selectedCarrera?.let {
                    idEstudServ = it.id_estud_serv.toIntOrNull() ?: 0
                    idPestDet = it.id_pest_det.toIntOrNull() ?: 0
                    idEstudPe = it.id_estud_pe
                    idTipoServa = it.id_tiposerva.toIntOrNull() ?: 0
                    idServ = it.id_serv.toIntOrNull() ?: 0
                    viewModel.setTramiteDocFiltroRequest(
                        id_uneg = idUneg,
                        id_estud = it.id_estud.toIntOrNull() ?: idEstud,
                        tipoCombo = "CIM",
                        idEstado = 0, idTipoTramite = 0, idTramite = 0,
                        fechaInicio = 0, fechaFin = 0, idTramiteEstud = 0, idTramiteDt = 0,
                        idTipoServa = it.id_tiposerva.toIntOrNull() ?: 0,
                        id_sistema = idSistema, cantidadMultiple = 0,
                        id_pest_det = it.id_pest_det.toIntOrNull() ?: 0,
                        id_estud_pe = 0,
                        id_estud_serv = it.id_estud_serv.toIntOrNull() ?: 0
                    )
                    viewModel.setCrearTramitesRequest(
                        id_sistema = idSistema,
                        id_tiposervad = it.id_serv.toIntOrNull() ?: 0,
                        id_usuario = idUsuario,
                        id_tipo_usuario = idTipoUsuario,
                        idUNEG = idUneg,
                        condicion = 2,
                        id_estud = idEstud
                    )
                }
            }
            viewModel.resetCarreraState()
        }
        is ResourceUiState.Error -> viewModel.resetCarreraState()
        else -> Unit
    }

    when (val s = uiStateTramiteDocFiltro) {
        is ResourceUiState.Success -> {
            tramites = (s.data.Tramite ?: emptyList()).filter { it.id.toIntOrNull() != 0 }
            viewModel.resetTramiteDocFiltroState()
        }

        is ResourceUiState.Error -> viewModel.resetTramiteDocFiltroState()
        else -> Unit
    }

    when (val s = uiStatePaises) {
        is ResourceUiState.Success -> {
            paises = s.data.ListPaises
            val peru =
                paises.firstOrNull { it?.pais_nombre?.equals("PERU", ignoreCase = true) == true }
            paisSeleccionado = peru ?: paises.firstOrNull()
            viewModel.resetTramitePaisesState()
        }

        is ResourceUiState.Error -> {
            paises = emptyList()
            viewModel.resetTramitePaisesState()
        }
        else -> Unit
    }

    when (val s = uiStateCrearTramites) {
        is ResourceUiState.Success -> {
            fechaSolicitante = stripHtml(s.data.CrearDocumentos.firstOrNull()?.FECHA_SOLICITANTE)
            viewModel.resetCrearTramitesState()
        }

        is ResourceUiState.Error -> viewModel.resetCrearTramitesState()
        else -> Unit
    }

    when (val s = uiStatePerfil) {
        is ResourceUiState.Success -> {
            perfilEstudiante = s.data.ListPerfilEstudiante.firstOrNull()
            viewModel.resetPerfilEstudianteState()
        }

        is ResourceUiState.Error -> viewModel.resetPerfilEstudianteState()
        else -> Unit
    }

    when (val s = uiStateTTRFiltro) {
        is ResourceUiState.Success -> {
            val json = s.data
            val ttrData1 = json["TTRData1"]?.jsonArray
            val ttrItem = ttrData1?.firstOrNull()?.jsonObject
            val requisitosJson = ttrItem?.get("requisitos")?.jsonPrimitive?.contentOrNull.orEmpty()
            val montoJson = ttrItem?.get("tg_valor")?.jsonPrimitive?.contentOrNull.orEmpty()
            val tipoTramiteJson =
                ttrItem?.get("tipo_tramite_nombre")?.jsonPrimitive?.contentOrNull.orEmpty()
            flagPago = ttrItem?.get("flag_pago")?.jsonPrimitive?.intOrNull ?: 0
            idTariGen = ttrItem?.get("id_tari_gen")?.jsonPrimitive?.contentOrNull.orEmpty()
            tariGenCodNav = ttrItem?.get("tari_gen_cod_nav")?.jsonPrimitive?.contentOrNull.orEmpty()

            if (idTipoTramiteDT != 0) {
                viewModel.setTramiteDocFiltroRequest(
                    id_uneg = idUneg,
                    id_estud = idEstud,
                    tipoCombo = "TRE",
                    idEstado = 0,
                    idTipoTramite = 0,
                    idTramite = 0,
                    fechaInicio = 0,
                    fechaFin = 0,
                    idTramiteEstud = 0,
                    idTramiteDt = idTipoTramiteDT,
                    idTipoServa = idTipoServa,
                    id_sistema = idSistema,
                    cantidadMultiple = 0,
                    id_pest_det = idPestDet,
                    id_estud_pe = idEstudPe,
                    id_estud_serv = idEstudServ
                )
            }

            val entregaList = json["TTRData"]?.jsonArray?.mapNotNull { item ->
                val obj = item.jsonObject
                TipoEntrega(
                    contador = obj["contador"]?.jsonPrimitive?.intOrNull ?: 0,
                    modal_presencial = obj["modal_presencial"]?.jsonPrimitive?.contentOrNull.orEmpty(),
                    id = obj["id"]?.jsonPrimitive?.contentOrNull.orEmpty(),
                    nombre = obj["nombre"]?.jsonPrimitive?.contentOrNull.orEmpty()
                )
            }.orEmpty()
            tipoEntregaList = entregaList
            selectedTipoEntrega =
                entregaList.find { it.id == selectedTipoEntrega?.id } ?: entregaList.firstOrNull()
            idTipoEntrega = selectedTipoEntrega?.id?.toIntOrNull() ?: 0
            modalPresencial = selectedTipoEntrega?.modal_presencial?.toIntOrNull() ?: 0
            requisitos = if (stripHtml(requisitosJson).isBlank()) "NO" else "SI"
            monto = stripHtml(montoJson).ifBlank { "0" }
            tipoTramite = stripHtml(tipoTramiteJson)
            viewModel.resetTramiteTTRComboState()
        }

        is ResourceUiState.Error -> viewModel.resetTramiteTTRComboState()
        else -> Unit
    }

    when (val s = uiStateTREFiltro) {
        is ResourceUiState.Success -> {
            requisitosList =
                mapTReItems(s.data["TREData"]?.jsonArray?.map { it.jsonObject }.orEmpty())
            viewModel.resetTREFiltroState()
        }

        is ResourceUiState.Error -> viewModel.resetTREFiltroState()
        else -> Unit
    }

    when (val s = uiStateValidarEgresado) {
        is ResourceUiState.Success -> {
            val item = s.data.ValidarTramite?.firstOrNull()
            if (s.data.flag_val == 1 && item?.esCorrecto == "1") {
                val tipoFormulario = TipoFormularioTramite.desdeTipoTramite(idTipoTramiteDT)
                if (requisitos == "SI") {
                    when (tipoFormulario) {
                        TipoFormularioTramite.A -> {
                            if (requisitosSeleccionadosA.isEmpty()) {
                                alertTitulo = "Estimado Estudiante"
                                alertMensaje = "Seleccione al menos uno."
                                alertFlag = 0
                                alertConfirmado = false
                                showAlertDialog = true
                                showProgress = false
                            } else {
                                viewModel.setRegistrarTramiteRequest(
                                    id_estud = idEstud, id_uneg = idUneg,
                                    id_tari_gen = idTariGen.toIntOrNull() ?: 0, id_user = idUsuario,
                                    estado_pasarela = "", tari_gen_cod_nav = tariGenCodNav, transw_id_tx = "",
                                    id_pest_det = idPestDet, id_estud_pe = idEstudPe, id_estud_serv = idEstudServ,
                                    pg_tipo_entrega = idTipoEntrega, motivo = motivo,
                                    flag_recojo = datosRecojo.recoger.takeIf { it >= 0 } ?: 0,
                                    recojo_dni = if (datosRecojo.recoger == 0) datosRecojo.dni else "",
                                    recojo_nombre = if (datosRecojo.recoger == 0) datosRecojo.nombres else "",
                                    flag_pago = flagPago, id_req_temp = 0, id_tramite = idTipoTramiteDT,
                                    id_sistema = idSistema, monto = parseMontoTramite(monto), id_modalidad = 0,
                                    tipo_tramite_reg = 2,
                                    requisitosCheckList = requisitosSeleccionadosA.map {
                                        RequisitoTramiteModoCheck(it.id_asignatura, it.id_tramite_req_doc, it.requisito_nombre, it.valorinput, it.cumplio)
                                    }
                                )
                            }
                        }

                        TipoFormularioTramite.E -> {
                            if (requisitosSeleccionadosE.isEmpty()) {
                                alertTitulo = "Selección requerida"
                                alertMensaje = "Debe seleccionar una opción."
                                alertFlag = 0
                                alertConfirmado = false
                                showAlertDialog = true
                                showProgress = false
                            } else {
                                viewModel.setRegistrarTramiteTempRequest(
                                    id_estud = idEstud, id_uneg = idUneg,
                                    id_tari_gen = idTariGen.toIntOrNull() ?: 0, id_user = idUsuario,
                                    estado_pasarela = "", tari_gen_cod_nav = tariGenCodNav, transw_id_tx = "",
                                    id_pest_det = idPestDet, id_estud_pe = idEstudPe, id_estud_serv = idEstudServ,
                                    pg_tipo_entrega = idTipoEntrega, motivo = motivo,
                                    flag_recojo = datosRecojo.recoger.takeIf { it >= 0 } ?: 0,
                                    recojo_dni = if (datosRecojo.recoger == 0) datosRecojo.dni else "",
                                    recojo_nombre = if (datosRecojo.recoger == 0) datosRecojo.nombres else "",
                                    flag_pago = flagPago, id_req_temp = 0, id_tramite = idTipoTramiteDT,
                                    id_sistema = idSistema, monto = parseMontoTramite(monto), id_modalidad = 0,
                                    tipo_tramite_reg = 1,
                                    requisitosModoCheckList = requisitosSeleccionadosE.map {
                                        RequisitoTramiteModoCheck(it.id_asignatura, it.id_tramite_req_doc, it.requisito_nombre, it.valorinput, it.cumplio)
                                    },
                                    requisitoTramiteB = emptyList(),
                                    requisitoTramiteD = emptyList()
                                )
                            }
                        }

                        TipoFormularioTramite.B -> viewModel.setRegistrarTramiteTempRequest(
                            id_estud = idEstud, id_uneg = idUneg,
                            id_tari_gen = idTariGen.toIntOrNull() ?: 0, id_user = idUsuario,
                            estado_pasarela = "", tari_gen_cod_nav = tariGenCodNav, transw_id_tx = "",
                            id_pest_det = idPestDet, id_estud_pe = idEstudPe, id_estud_serv = idEstudServ,
                            pg_tipo_entrega = idTipoEntrega, motivo = motivo,
                            flag_recojo = datosRecojo.recoger.takeIf { it >= 0 } ?: 0,
                            recojo_dni = if (datosRecojo.recoger == 0) datosRecojo.dni else "",
                            recojo_nombre = if (datosRecojo.recoger == 0) datosRecojo.nombres else "",
                            flag_pago = flagPago, id_req_temp = 0, id_tramite = idTipoTramiteDT,
                            id_sistema = idSistema, monto = parseMontoTramite(monto), id_modalidad = 0,
                            tipo_tramite_reg = 2, requisitosModoCheckList = emptyList(),
                            requisitoTramiteB = requisitosSeleccionadosB, requisitoTramiteD = emptyList()
                        )

                        TipoFormularioTramite.C -> {
                            val requisitosDoc =
                                requisitosSeleccionadosC.filterIsInstance<RequisitoTramiteC.Doc>()
                            val requisitosMain =
                                requisitosSeleccionadosC.filterIsInstance<RequisitoTramiteC.Main>()
                            if (requisitosDoc.isEmpty() && requisitosMain.isEmpty()) {
                                alertTitulo = "Verificacion de Requisito"
                                alertMensaje = "Seleccione al menos uno."
                                alertFlag = 0
                                alertConfirmado = false
                                showAlertDialog = true
                                showProgress = false
                            } else {
                                if (requisitosMain.isNotEmpty()) monto =
                                    formatMontoTramite(parseMontoTramite(monto) * requisitosMain.size)
                                viewModel.setGuardarArchivoTramiteRequest(
                                    id_uneg = idUneg,
                                    id_estud = idEstud,
                                    image64 = image64,
                                    pdfbase64 = "",
                                    extFile = requisitosDoc.firstOrNull()?.extFile.orEmpty(),
                                    nombreDocAbrev = requisitosDoc.firstOrNull()?.fileNameDocTitle
                                        ?: "DOC_TRAMITE",
                                    requisitosDoc = requisitosDoc,
                                    requisitosMain = requisitosMain
                                )
                            }
                        }

                        TipoFormularioTramite.D -> viewModel.setRegistrarTramiteTempRequest(
                            id_estud = idEstud, id_uneg = idUneg,
                            id_tari_gen = idTariGen.toIntOrNull() ?: 0, id_user = idUsuario,
                            estado_pasarela = "", tari_gen_cod_nav = tariGenCodNav, transw_id_tx = "",
                            id_pest_det = idPestDet, id_estud_pe = idEstudPe, id_estud_serv = idEstudServ,
                            pg_tipo_entrega = idTipoEntrega, motivo = motivo,
                            flag_recojo = datosRecojo.recoger.takeIf { it >= 0 } ?: 0,
                            recojo_dni = if (datosRecojo.recoger == 0) datosRecojo.dni else "",
                            recojo_nombre = if (datosRecojo.recoger == 0) datosRecojo.nombres else "",
                            flag_pago = flagPago, id_req_temp = 0, id_tramite = idTipoTramiteDT,
                            id_sistema = idSistema, monto = parseMontoTramite(monto), id_modalidad = 0,
                            tipo_tramite_reg = 3, requisitosModoCheckList = emptyList(),
                            requisitoTramiteB = emptyList(), requisitoTramiteD = requisitosSeleccionadosD
                        )

                        TipoFormularioTramite.H -> {
                            val requisitosDoc =
                                requisitosSeleccionadosH.filterIsInstance<RequisitoTramiteC.Doc>()
                            if (requisitosDoc.isEmpty()) {
                                alertTitulo = "Verificacion de Requisito"
                                alertMensaje = "Adjunte al menos un archivo."
                                alertFlag = 0
                                alertConfirmado = false
                                showAlertDialog = true
                                showProgress = false
                            } else {
                                viewModel.setGuardarArchivoTramiteRequest(
                                    id_uneg = idUneg,
                                    id_estud = idEstud,
                                    image64 = "",
                                    pdfbase64 = pdf64,
                                    extFile = requisitosDoc.firstOrNull()?.extFile.orEmpty(),
                                    nombreDocAbrev = requisitosDoc.firstOrNull()?.fileNameDocTitle
                                        ?: "DOC_TRAMITE",
                                    requisitosDoc = requisitosDoc,
                                    requisitosMain = emptyList()
                                )
                            }
                        }

                        TipoFormularioTramite.L -> {
                            val requisitosDoc =
                                requisitosSeleccionadosL.filterIsInstance<RequisitoTramiteC.Doc>()
                            if (requisitosDoc.isEmpty()) {
                                alertTitulo = "Verificacion de Requisitos"
                                alertMensaje = "Debe adjuntar al menos un archivo."
                                alertFlag = 0
                                alertConfirmado = false
                                showAlertDialog = true
                                showProgress = false
                            } else {
                                println("📦 L requisitos a enviar: ${requisitosDoc.size}")
                                requisitosDoc.forEach { println("  → id_req_doc=${it.id_tramite_req_doc} nombre=${it.nombre} len=${it.pdfBase64?.length} pdf_end=${it.pdfBase64?.takeLast(20)}") }
                                viewModel.setDuplicadoTituloGuardarRequest(
                                    DuplicadoTituloGuardarRequest(
                                        id_uneg = idUneg,
                                        id_estud = idEstud,
                                        requisitos = RequisitosRequest(array = requisitosDoc.map { it.toRequisitoItemRequest() })
                                    )
                                )
                            }
                        }

                        else -> viewModel.setRegistrarTramiteRequest(
                            id_estud = idEstud, id_uneg = idUneg,
                            id_tari_gen = idTariGen.toIntOrNull() ?: 0, id_user = idUsuario,
                            estado_pasarela = "", tari_gen_cod_nav = tariGenCodNav, transw_id_tx = "",
                            id_pest_det = idPestDet, id_estud_pe = idEstudPe, id_estud_serv = idEstudServ,
                            pg_tipo_entrega = idTipoEntrega, motivo = motivo,
                            flag_recojo = datosRecojo.recoger.takeIf { it >= 0 } ?: 0,
                            recojo_dni = if (datosRecojo.recoger == 0) datosRecojo.dni else "",
                            recojo_nombre = if (datosRecojo.recoger == 0) datosRecojo.nombres else "",
                            flag_pago = flagPago, id_req_temp = 0, id_tramite = idTipoTramiteDT,
                            id_sistema = idSistema, monto = parseMontoTramite(monto), id_modalidad = 0,
                            tipo_tramite_reg = 1, requisitosCheckList = emptyList()
                        )
                    }
                } else {
                    when (tipoFormulario) {
                        TipoFormularioTramite.F -> {
                            val perfil = perfilEstudiante
                            if (perfil != null) {
                                val callbackId = listOf(
                                    idEstud.toString(), idUneg.toString(), idUsuario.toString(),
                                    paisSeleccionado?.pais_prefijo.orEmpty(), idTariGen, tariGenCodNav,
                                    perfil.correo_personal, idPestDet.toString(), idEstudPe.toString(),
                                    idEstudServ.toString(), idTipoEntrega.toString(), motivo,
                                    datosRecojo.recoger.takeIf { it >= 0 }?.toString().orEmpty(),
                                    if (datosRecojo.recoger == 0) datosRecojo.dni else "",
                                    if (datosRecojo.recoger == 0) datosRecojo.nombres else "",
                                    flagPago.toString(), "0", "0", idTipoTramiteDT.toString(), monto, "0"
                                ).joinToString("#")
                                val request = construirTemporalTramiteRequest(
                                    callbackId = callbackId, monto = monto,
                                    senderCountry = paisSeleccionado?.pais_prefijo.orEmpty(),
                                    perfilEstudiante = perfil, idUsuario = idUsuario, idUneg = idUneg
                                )
                                viewModel.setTemporalCuentaCorriente(request.body)
                            } else {
                                alertTitulo = "TRAMITE DOCUMENTARIO"
                                alertMensaje = "No se pudo cargar el perfil del estudiante."
                                alertFlag = 0; alertConfirmado = false
                                showAlertDialog = true; showProgress = false
                            }
                        }
                        TipoFormularioTramite.A -> viewModel.setRegistrarTramiteRequest(
                            id_estud = idEstud, id_uneg = idUneg,
                            id_tari_gen = idTariGen.toIntOrNull() ?: 0, id_user = idUsuario,
                            estado_pasarela = "", tari_gen_cod_nav = tariGenCodNav, transw_id_tx = "",
                            id_pest_det = idPestDet, id_estud_pe = idEstudPe, id_estud_serv = idEstudServ,
                            pg_tipo_entrega = idTipoEntrega, motivo = motivo,
                            flag_recojo = datosRecojo.recoger.takeIf { it >= 0 } ?: 0,
                            recojo_dni = if (datosRecojo.recoger == 0) datosRecojo.dni else "",
                            recojo_nombre = if (datosRecojo.recoger == 0) datosRecojo.nombres else "",
                            flag_pago = flagPago, id_req_temp = 0, id_tramite = idTipoTramiteDT,
                            id_sistema = idSistema, monto = parseMontoTramite(monto), id_modalidad = 0,
                            tipo_tramite_reg = 2,
                            requisitosCheckList = requisitosSeleccionadosA.map {
                                RequisitoTramiteModoCheck(it.id_asignatura, it.id_tramite_req_doc, it.requisito_nombre, it.valorinput, it.cumplio)
                            }
                        )
                        else -> viewModel.setRegistrarTramiteRequest(
                            id_estud = idEstud, id_uneg = idUneg,
                            id_tari_gen = idTariGen.toIntOrNull() ?: 0, id_user = idUsuario,
                            estado_pasarela = "", tari_gen_cod_nav = tariGenCodNav, transw_id_tx = "",
                            id_pest_det = idPestDet, id_estud_pe = idEstudPe, id_estud_serv = idEstudServ,
                            pg_tipo_entrega = idTipoEntrega, motivo = motivo,
                            flag_recojo = datosRecojo.recoger.takeIf { it >= 0 } ?: 0,
                            recojo_dni = if (datosRecojo.recoger == 0) datosRecojo.dni else "",
                            recojo_nombre = if (datosRecojo.recoger == 0) datosRecojo.nombres else "",
                            flag_pago = flagPago, id_req_temp = 0, id_tramite = idTipoTramiteDT,
                            id_sistema = idSistema, monto = parseMontoTramite(monto), id_modalidad = 0,
                            tipo_tramite_reg = 1, requisitosCheckList = emptyList()
                        )
                    }
                }
            } else {
                alertTitulo = "TRAMITE DOCUMENTARIO"
                alertMensaje = stripHtml(item?.mensaje).ifBlank { "El estudiante no cumple para solicitar el tramite." }
                alertFlag = s.data.flag_val
                alertConfirmado = false
                showAlertDialog = true
                showProgress = false
            }
            viewModel.resetValidarEgresadoState()
        }

        is ResourceUiState.Error -> {
            alertTitulo = "TRAMITE DOCUMENTARIO"
            alertMensaje = s.message
            alertFlag = 0
            alertConfirmado = false
            showAlertDialog = true
            showProgress = false
            viewModel.resetValidarEgresadoState()
            viewModel.resetRegistrarTramiteState()
        }

        else -> Unit
    }

    when (val s = uiStateRegistrarTramite) {
        is ResourceUiState.Success -> {
            showProgress = false
            if (s.data.flag_val == 1) {
                showSuccessDialog = true
            } else {
                alertTitulo = "TRAMITE DOCUMENTARIO"
                alertMensaje = "No se pudo registrar el tramite. Flag: ${s.data.flag_val}"
                alertFlag = s.data.flag_val
                alertConfirmado = false
                showAlertDialog = true
                showProgress = false
                viewModel.resetRegistrarTramiteState()
            }
        }

        is ResourceUiState.Error -> {
            alertTitulo = "TRAMITE DOCUMENTARIO"
            alertMensaje = s.message
            alertFlag = 0
            alertConfirmado = false
            showAlertDialog = true
            showProgress = false
            viewModel.resetRegistrarTramiteState()
        }

        else -> Unit
    }

    when (val s = uiStateRegistrarTramiteTemp) {
        is ResourceUiState.Success -> {
            if (s.data.flag_val == 1) {
                val idReqTemp = s.data.ListTempRequisito.firstOrNull()?.id_req_temp ?: 0
                idTempRequisitos = idReqTemp
                if (parseMontoTramite(monto) <= 0.0 && idReqTemp != 0) {
                    viewModel.setRegistrarTramiteRequest(
                        id_estud = idEstud, id_uneg = idUneg,
                        id_tari_gen = idTariGen.toIntOrNull() ?: 0, id_user = idUsuario,
                        estado_pasarela = "", tari_gen_cod_nav = tariGenCodNav, transw_id_tx = "",
                        id_pest_det = idPestDet, id_estud_pe = idEstudPe, id_estud_serv = idEstudServ,
                        pg_tipo_entrega = idTipoEntrega, motivo = motivo,
                        flag_recojo = datosRecojo.recoger.takeIf { it >= 0 } ?: 0,
                        recojo_dni = if (datosRecojo.recoger == 0) datosRecojo.dni else "",
                        recojo_nombre = if (datosRecojo.recoger == 0) datosRecojo.nombres else "",
                        flag_pago = flagPago, id_req_temp = idReqTemp, id_tramite = idTipoTramiteDT,
                        id_sistema = idSistema, monto = parseMontoTramite(monto), id_modalidad = 0,
                        tipo_tramite_reg = 1, requisitosCheckList = emptyList()
                    )
                } else {
                    showProgress = false
                    val perfil = perfilEstudiante
                    if (perfil != null) {
                        val callbackId = listOf(
                            idEstud.toString(), idUneg.toString(), idUsuario.toString(),
                            paisSeleccionado?.pais_prefijo.orEmpty(), idTariGen, tariGenCodNav,
                            perfil.correo_personal, idPestDet.toString(), idEstudPe.toString(),
                            idEstudServ.toString(), idTipoEntrega.toString(), motivo,
                            datosRecojo.recoger.takeIf { it >= 0 }?.toString().orEmpty(),
                            if (datosRecojo.recoger == 0) datosRecojo.dni else "",
                            if (datosRecojo.recoger == 0) datosRecojo.nombres else "",
                            flagPago.toString(), "0", idReqTemp.toString(), idTipoTramiteDT.toString(), monto, "0"
                        ).joinToString("#")
                        val request = construirTemporalTramiteRequest(
                            callbackId = callbackId, monto = monto,
                            senderCountry = paisSeleccionado?.pais_prefijo.orEmpty(),
                            perfilEstudiante = perfil, idUsuario = idUsuario, idUneg = idUneg
                        )
                        viewModel.setTemporalCuentaCorriente(request.body)
                    } else {
                        alertTitulo = "TRAMITE DOCUMENTARIO"
                        alertMensaje = "No se pudo cargar el perfil del estudiante."
                        alertFlag = 0; alertConfirmado = false
                        showAlertDialog = true; showProgress = false
                    }
                }
            } else {
                alertTitulo = "TRAMITE DOCUMENTARIO"
                alertMensaje = "No se pudo obtener el temporal de requisitos."
                alertFlag = 0
                alertConfirmado = false
                showAlertDialog = true
                showProgress = false
            }
            viewModel.resetRegistrarTramiteTempState()
        }

        is ResourceUiState.Error -> {
            alertTitulo = "TRAMITE DOCUMENTARIO"
            alertMensaje = s.message
            alertFlag = 0
            alertConfirmado = false
            showAlertDialog = true
            showProgress = false
            viewModel.resetRegistrarTramiteTempState()
        }

        else -> Unit
    }

    when (val s = uiStateGuardarArchivoTramite) {
        is ResourceUiState.Success -> {
            if (s.data.flag_val == 1) {
                val idReqTemp = s.data.ListTempRequisito.firstOrNull()?.id_req_temp ?: 0
                idTempRequisitos = idReqTemp
                if (parseMontoTramite(monto) <= 0.0 && idReqTemp != 0) {
                    viewModel.setRegistrarTramiteRequest(
                        id_estud = idEstud, id_uneg = idUneg,
                        id_tari_gen = idTariGen.toIntOrNull() ?: 0, id_user = idUsuario,
                        estado_pasarela = "", tari_gen_cod_nav = tariGenCodNav, transw_id_tx = "",
                        id_pest_det = idPestDet, id_estud_pe = idEstudPe, id_estud_serv = idEstudServ,
                        pg_tipo_entrega = idTipoEntrega, motivo = motivo,
                        flag_recojo = datosRecojo.recoger.takeIf { it >= 0 } ?: 0,
                        recojo_dni = if (datosRecojo.recoger == 0) datosRecojo.dni else "",
                        recojo_nombre = if (datosRecojo.recoger == 0) datosRecojo.nombres else "",
                        flag_pago = flagPago, id_req_temp = idReqTemp, id_tramite = idTipoTramiteDT,
                        id_sistema = idSistema, monto = parseMontoTramite(monto), id_modalidad = 0,
                        tipo_tramite_reg = 1, requisitosCheckList = emptyList()
                    )
                } else {
                    showProgress = false
                    val perfil = perfilEstudiante
                    if (perfil != null) {
                        val callbackId = listOf(
                            idEstud.toString(), idUneg.toString(), idUsuario.toString(),
                            paisSeleccionado?.pais_prefijo.orEmpty(), idTariGen, tariGenCodNav,
                            perfil.correo_personal, idPestDet.toString(), idEstudPe.toString(),
                            idEstudServ.toString(), idTipoEntrega.toString(), motivo,
                            datosRecojo.recoger.takeIf { it >= 0 }?.toString().orEmpty(),
                            if (datosRecojo.recoger == 0) datosRecojo.dni else "",
                            if (datosRecojo.recoger == 0) datosRecojo.nombres else "",
                            flagPago.toString(), "0", idReqTemp.toString(), idTipoTramiteDT.toString(), monto, "0"
                        ).joinToString("#")
                        val request = construirTemporalTramiteRequest(
                            callbackId = callbackId, monto = monto,
                            senderCountry = paisSeleccionado?.pais_prefijo.orEmpty(),
                            perfilEstudiante = perfil, idUsuario = idUsuario, idUneg = idUneg
                        )
                        viewModel.setTemporalCuentaCorriente(request.body)
                    } else {
                        alertTitulo = "TRAMITE DOCUMENTARIO"
                        alertMensaje = "No se pudo cargar el perfil del estudiante."
                        alertFlag = 0; alertConfirmado = false
                        showAlertDialog = true; showProgress = false
                    }
                }
            } else {
                alertTitulo = "TRAMITE DOCUMENTARIO"
                alertMensaje = "No se pudo guardar el archivo del requisito."
                alertFlag = 0
                alertConfirmado = false
                showAlertDialog = true
                showProgress = false
            }
            viewModel.resetGuardarArchivoState()
        }

        is ResourceUiState.Error -> {
            alertTitulo = "TRAMITE DOCUMENTARIO"
            alertMensaje = s.message
            alertFlag = 0
            alertConfirmado = false
            showAlertDialog = true
            showProgress = false
            viewModel.resetGuardarArchivoState()
        }

        else -> Unit
    }

    when (val s = uiStateDuplicadoTitulo) {
        is ResourceUiState.Success -> {
            if (s.data.flag_val == 1) {
                val idReqTemp = s.data.ListTempRequisito.firstOrNull()?.id_req_temp ?: 0
                idTempRequisitos = idReqTemp
                if (parseMontoTramite(monto) <= 0.0 && idReqTemp != 0) {
                    viewModel.setRegistrarTramiteRequest(
                        id_estud = idEstud, id_uneg = idUneg,
                        id_tari_gen = idTariGen.toIntOrNull() ?: 0, id_user = idUsuario,
                        estado_pasarela = "", tari_gen_cod_nav = tariGenCodNav, transw_id_tx = "",
                        id_pest_det = idPestDet, id_estud_pe = idEstudPe, id_estud_serv = idEstudServ,
                        pg_tipo_entrega = idTipoEntrega, motivo = motivo,
                        flag_recojo = datosRecojo.recoger.takeIf { it >= 0 } ?: 0,
                        recojo_dni = if (datosRecojo.recoger == 0) datosRecojo.dni else "",
                        recojo_nombre = if (datosRecojo.recoger == 0) datosRecojo.nombres else "",
                        flag_pago = flagPago, id_req_temp = idReqTemp, id_tramite = idTipoTramiteDT,
                        id_sistema = idSistema, monto = parseMontoTramite(monto), id_modalidad = 0,
                        tipo_tramite_reg = 1, requisitosCheckList = emptyList()
                    )
                } else {
                    showProgress = false
                    val perfil = perfilEstudiante
                    if (perfil != null) {
                        val callbackId = listOf(
                            idEstud.toString(), idUneg.toString(), idUsuario.toString(),
                            paisSeleccionado?.pais_prefijo.orEmpty(), idTariGen, tariGenCodNav,
                            perfil.correo_personal, idPestDet.toString(), idEstudPe.toString(),
                            idEstudServ.toString(), idTipoEntrega.toString(), motivo,
                            datosRecojo.recoger.takeIf { it >= 0 }?.toString().orEmpty(),
                            if (datosRecojo.recoger == 0) datosRecojo.dni else "",
                            if (datosRecojo.recoger == 0) datosRecojo.nombres else "",
                            flagPago.toString(), "0", idReqTemp.toString(), idTipoTramiteDT.toString(), monto, "0"
                        ).joinToString("#")
                        val request = construirTemporalTramiteRequest(
                            callbackId = callbackId, monto = monto,
                            senderCountry = paisSeleccionado?.pais_prefijo.orEmpty(),
                            perfilEstudiante = perfil, idUsuario = idUsuario, idUneg = idUneg
                        )
                        viewModel.setTemporalCuentaCorriente(request.body)
                    } else {
                        alertTitulo = "TRAMITE DOCUMENTARIO"
                        alertMensaje = "No se pudo cargar el perfil del estudiante."
                        alertFlag = 0; alertConfirmado = false
                        showAlertDialog = true; showProgress = false
                    }
                }
            } else {
                alertTitulo = "TRAMITE DOCUMENTARIO"
                alertMensaje = "No se pudo generar el temporal de duplicado de titulo."
                alertFlag = 0
                alertConfirmado = false
                showAlertDialog = true
                showProgress = false
            }
            viewModel.resetDuplicadoTituloState()
        }

        is ResourceUiState.Error -> {
            alertTitulo = "TRAMITE DOCUMENTARIO"
            alertMensaje = s.message
            alertFlag = 0
            alertConfirmado = false
            showAlertDialog = true
            showProgress = false
            viewModel.resetDuplicadoTituloState()
        }

        else -> Unit
    }

    when (val s = uiStateTemporalCuentaCorriente) {
        is ResourceUiState.Success -> {
            showProgress = false
            val codTransaccion = s.data.cod_transaccion
            viewModel.resetTempCuentaCorrienteState()
            if (codTransaccion.isNotBlank()) {
                onDismiss()
                navigator.navigate("/pagoFlywire/$codTransaccion/tramiteDocumentario")
            } else {
                alertTitulo = "TRAMITE DOCUMENTARIO"
                alertMensaje = "Ha ocurrido un error, intente mas tarde."
                alertFlag = 0
                alertConfirmado = false
                showAlertDialog = true
                showProgress = false
            }
        }

        is ResourceUiState.Error -> {
            alertTitulo = "TRAMITE DOCUMENTARIO"
            alertMensaje = s.message
            alertFlag = 0
            alertConfirmado = false
            showAlertDialog = true
            showProgress = false
            viewModel.resetTempCuentaCorrienteState()
        }

        else -> Unit
    }

    if (showDialogRecojo) {
        DialogEntregaPresencial(
            visible = true,
            onDismiss = { showDialogRecojo = false },
            onConfirm = { recoger, dni, nombre ->
                datosRecojo = DatosRecojo(dni = dni, nombres = nombre, recoger = recoger)
                showDialogRecojo = false
            }
        )
    }

    if (showAlertDialog) {
        CustomDialogBasic(
            visible = true,
            titulo = alertTitulo,
            mensaje = alertMensaje,
            flag_val = alertFlag,
            confirmado = alertConfirmado,
            onDismiss = { showAlertDialog = false },
            dismissOnOutsideClick = true
        )
    }

    if (showSuccessDialog) {
        CustomDialogBasic(
            visible = true,
            titulo = "TRAMITE DOCUMENTARIO",
            flag_val = 1,
            mensaje = "Se ha solicitado correctamente el tramite.",
            aceptarSelected = 5,
            confirmado = true,
            onDismiss = {
                showSuccessDialog = false
                viewModel.resetRegistrarTramiteState()
            },
            onDismissGoScreen = {
                onDismiss()
                navigator.navigate("/tramiteDocumentario") {
                    popUpTo("/tramiteDocumentario") { inclusive = true }
                    launchSingleTop = false
                    restoreState = false
                }
            },
            dismissOnOutsideClick = false
        )
    }

    if (showProgress) {
        ProgressBarLoading("")
    }
}




private fun RequisitoTramiteC.Doc.toRequisitoItemRequest(): RequisitoItemRequest =
    RequisitoItemRequest(
        extFile = extFile.replace(".", ""),
        fileNameDocTitle = fileNameDocTitle,
        pdfBase64 = pdfBase64.orEmpty(),
        id_tramite_req_doc = id_tramite_req_doc.toIntOrNull() ?: 0,
        contador = contador.toString(),
        nombre = nombre,
        requisito_nombre = requisito_nombre,
        empresa = empresa,
        carrera = carrera,
        multiple = multiple,
        documento = documento,
        id_tramite_estud = id_tramite_estud,
        id_tramite_estud_req_doc = id_tramite_estud_req_doc,
        periodo_mat = periodo_mat,
        id_tramite_estud_req = id_tramite_estud_req,
        cumplio = cumplio
    )

private fun construirTemporalTramiteRequest(
    callbackId: String,
    monto: String,
    senderCountry: String,
    perfilEstudiante: ListPerfilEstudiante,
    idUsuario: Int,
    idUneg: Int
): TemporalCuentaCorrienteRequest {
    val codTransaccion = Base64Encoder.encodeToBase64(fechaTransaccionTramite(idUsuario)).trim()
    val callBackID = Base64Encoder.encodeToBase64(callbackId)
    val email = perfilEstudiante.correo_personal.ifBlank { perfilEstudiante.correoelec_ins }
    val senderLastName =
        "${perfilEstudiante.usuario_apellido_pat} ${perfilEstudiante.usuario_apellido_mat}".trim()
    val pago = PagoDetalleTemporal(
        cod_transaccion = codTransaccion,
        amount = formatMontoTramite(parseMontoTramite(monto)).replace(".", ""),
        callback_id = callBackID,
        callback_url = "${Constantes.RETURN_TRAMITE}.${Constantes.BASE_UNEG}.edu.pe/TramitesEstudianteServlet?accion=PagoFLYWIRE",
        displayPayerInformation = "1",
        email_address = email,
        env = Constantes.ENV_DOMAIN,
        invoice_number = "1",
        locale = "es-ES",
        program_code = "1",
        provider = "embed2.0",
        recipient = Constantes.RECIPIENT_DOMAIN,
        sender_address1 = perfilEstudiante.direc_resi,
        sender_city = perfilEstudiante.ubig_nombdepa,
        sender_country = senderCountry,
        sender_email = email,
        sender_first_name = perfilEstudiante.ususario_nombre,
        sender_last_name = senderLastName,
        sender_middle_name = "",
        sender_phone = perfilEstudiante.telefono1,
        sender_state = perfilEstudiante.ubig_nombdepa,
        sender_zip_code = perfilEstudiante.ubig_nombdepa,
        student_first_name = perfilEstudiante.ususario_nombre,
        student_id = perfilEstudiante.numero_documento.toString(),
        student_last_name = senderLastName,
        return_url = "${Constantes.RETURN_DOMAIN}.${Constantes.BASE_UNEG}.edu.pe/pages/success.jsp",
        sender_address2 = "",
        id_uneg = idUneg.toString()
    )
    val topLevel =
        TemporalBody(PagosTopLevel(PagosNameValuePairs(PagoNameValuePairs(namePairs(pago)))))
    return TemporalCuentaCorrienteRequest(Json.encodeToString(topLevel))
}

private fun fechaTransaccionTramite(idUsuario: Int): String {
    val now = getTodayLocalDateTime()
    return "$idUsuario${now.year}-${
        now.monthNumber.toString().padStart(2, '0')
    }-${now.dayOfMonth.toString().padStart(2, '0')} " +
            "${now.hour.toString().padStart(2, '0')}:${
                now.minute.toString().padStart(2, '0')
            }:${now.second.toString().padStart(2, '0')}"
}

private fun parseMontoTramite(value: String): Double {
    val clean = value.replace(",", ".").filter { it.isDigit() || it == '.' || it == '-' }
    return clean.toDoubleOrNull() ?: 0.0
}

private fun formatMontoTramite(value: Double): String {
    val rounded = round(value * 100).toLong()
    val intPart = rounded / 100L
    val decPart = (rounded % 100L).toString().padStart(2, '0')
    return "$intPart.$decPart"
}
