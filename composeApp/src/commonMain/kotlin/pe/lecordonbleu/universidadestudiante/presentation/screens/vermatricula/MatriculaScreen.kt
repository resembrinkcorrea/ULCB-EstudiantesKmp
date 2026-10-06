@file:OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)

package pe.lecordonbleu.universidadestudiante.presentation.screens.vermatricula

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import kotlinx.coroutines.launch
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Surface
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import pe.lecordonbleu.universidadestudiante.data.remote.dto.ListProyeccionValidacion
import pe.lecordonbleu.universidadestudiante.data.remote.dto.ListResumenHist
import pe.lecordonbleu.universidadestudiante.presentation.screens.vermatricula.customcell.ResumenHistoricoCard
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import pe.lecordonbleu.universidadestudiante.LoadingIndicator
import pe.lecordonbleu.universidadestudiante.SettingsStorage
import pe.lecordonbleu.universidadestudiante.getPlatformContext
import pe.lecordonbleu.universidadestudiante.showToast
import pe.lecordonbleu.universidadestudiante.util.openPdfFromBase64
import pe.lecordonbleu.universidadestudiante.WebViewComposable
import pe.lecordonbleu.universidadestudiante.core.config.Constantes
import pe.lecordonbleu.universidadestudiante.data.remote.dto.Carrera
import pe.lecordonbleu.universidadestudiante.data.remote.dto.ListDetMatric
import pe.lecordonbleu.universidadestudiante.data.remote.dto.ListVerMatric
import pe.lecordonbleu.universidadestudiante.domain.model.MatriculaBody
import pe.lecordonbleu.universidadestudiante.getColorsTheme
import pe.lecordonbleu.universidadestudiante.getSettingsStorage
import pe.lecordonbleu.universidadestudiante.presentation.components.AppDropdownMenu
import pe.lecordonbleu.universidadestudiante.presentation.components.FullScreenLoadingOverlay
import pe.lecordonbleu.universidadestudiante.presentation.components.StandardTopBar
import pe.lecordonbleu.universidadestudiante.presentation.components.dialogs.ConfirmarEnvioDialog
import pe.lecordonbleu.universidadestudiante.presentation.components.dialogs.CustomDialogBasic
import pe.lecordonbleu.universidadestudiante.presentation.components.dialogs.HoraPagoMatriculaDialog
import pe.lecordonbleu.universidadestudiante.presentation.components.dialogs.MatriculaDeudasDialog
import pe.lecordonbleu.universidadestudiante.presentation.screens.vermatricula.registrar.CondicionesMatriculaTab
import pe.lecordonbleu.universidadestudiante.presentation.screens.vermatricula.registrar.GuardarMatriculaTab
import pe.lecordonbleu.universidadestudiante.presentation.screens.vermatricula.registrar.InicioMatriculaTab
import pe.lecordonbleu.universidadestudiante.presentation.vo.ResourceUiState

@Composable
fun MatriculaScreen(
    viewModel: MatriculaViewModel,
    navigator: NavController
) {
    // ─── 1. Variables y estados ───────────────────────────────────────────────
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
    val uiStateEstudianteMatricula by viewModel.uiStateEstudianteMatricula.collectAsStateWithLifecycle()
    val uiStateDeudas by viewModel.uiStateDeudas.collectAsStateWithLifecycle()
    val uiStateHoraPago by viewModel.uiStateHoraPago.collectAsStateWithLifecycle()
    val uiStateTurno by viewModel.uiStateTurno.collectAsStateWithLifecycle()
    val uiStateValidarDocs by viewModel.uiStateValidarDocs.collectAsStateWithLifecycle()
    val uiStateTextosHtml by viewModel.uiStateTextosHtml.collectAsStateWithLifecycle()
    val uiStateValidarInicio by viewModel.uiStateValidarInicio.collectAsStateWithLifecycle()
    val uiStateHoraServidor by viewModel.uiStateHoraServidor.collectAsStateWithLifecycle()
    val uiStateRegistrar by viewModel.uiStateRegistrar.collectAsStateWithLifecycle()
    val uiStateResumenHistorico by viewModel.uiStateResumenHistorico.collectAsStateWithLifecycle()
    val uiStateHorarioPDF by viewModel.uiStateHorarioPDF.collectAsStateWithLifecycle()

    val context = getPlatformContext()

    var carrerasList by remember { mutableStateOf<List<Carrera>>(emptyList()) }
    var selectedCarreraDisplay by remember { mutableStateOf<Carrera?>(null) }
    var proyeccionList by remember { mutableStateOf<List<ListProyeccionValidacion>>(emptyList()) }
    var msgSinProyeccion by remember { mutableStateOf("") }
    var idOacadDetPendiente by remember { mutableStateOf("") }

    // Modo de pantalla
    var modoVer by remember { mutableStateOf(false) }
    var modoRegistrar by remember { mutableStateOf(false) }

    // Datos de proyeccion
    var idEstudPe by remember { mutableStateOf(0) }
    var idPeracad by remember { mutableStateOf(0) }
    var idOacadArranque by remember { mutableStateOf(0) }
    var idPestDet by remember { mutableStateOf(0) }
    var idEstudProyeccion by remember { mutableStateOf(0) }
    var idEstudServProyeccion by remember { mutableStateOf(0) }
    var idTiposerva by remember { mutableStateOf(0) }
    var estadoIngresante by remember { mutableStateOf(0) }
    var promedioUltMatricula by remember { mutableStateOf(0.0) }
    var idTipmatric by remember { mutableStateOf("") }

    // Datos de turno
    var turnoInicio by remember { mutableStateOf("") }
    var turnoFin by remember { mutableStateOf("") }
    var turnoHorario by remember { mutableStateOf("") }
    var turnoIdGrmatricd by remember { mutableStateOf(0) }

    // Datos de carrera para registrar
    var servId by remember { mutableStateOf("0") }

    // UI
    var showLoading by remember { mutableStateOf(true) }
    var showProgress by remember { mutableStateOf(false) }
    var flagEnabledButton by remember { mutableStateOf(0) }

    // Textos HTML
    var welcomeHtml by remember { mutableStateOf("") }
    var condicionesHtml by remember { mutableStateOf("") }
    var pdfUrl by remember { mutableStateOf("") }

    // Cursos para GuardarTab
    var cursosList by remember { mutableStateOf<List<ListVerMatric>>(emptyList()) }
    val detalleMap = remember { mutableStateMapOf<String, List<ListDetMatric>>() }

    // Hora servidor contexto: 1=inicio, 2=guardar
    var horaContexto by remember { mutableStateOf(0) }
    var pendingMatriculaBody by remember { mutableStateOf<MatriculaBody?>(null) }

    // Dialogs
    var showDialogNoTurno by remember { mutableStateOf(false) }
    var showDialogSinProyeccion by remember { mutableStateOf(false) }
    var showDialogMatriculaDeudas by remember { mutableStateOf(false) }
    var deudasMatricula by remember { mutableStateOf<List<pe.lecordonbleu.universidadestudiante.data.remote.dto.ListaMatriculaValidacion>>(emptyList()) }
    var showDialogDocsEstado0 by remember { mutableStateOf(false) }
    var mensajeDocsEstado0 by remember { mutableStateOf("") }
    var showDialogFueraHorario by remember { mutableStateOf(false) }
    var showDialogFueraFecha by remember { mutableStateOf(false) }
    var showDialogPdf by remember { mutableStateOf(false) }
    var showDialogConfirmarRegistrar by remember { mutableStateOf(false) }
    var showDialogResultadoRegistrar by remember { mutableStateOf(false) }
    var tituloDialogRegistrar by remember { mutableStateOf("") }
    var mensajeDialogRegistrar by remember { mutableStateOf("") }
    var flagValDialogRegistrar by remember { mutableStateOf(0) }

    var showToastErrorDocs by remember { mutableStateOf(false) }

    // Resumen Historico sheet
    var showResumenHistoricoSheet by remember { mutableStateOf(false) }
    var resumenHistoricoList by remember { mutableStateOf<List<ListResumenHist>>(emptyList()) }
    var resumenHistoricoLoading by remember { mutableStateOf(false) }
    val resumenHistoricoSheetState = rememberModalBottomSheetState()

    // Tab pager para registrar
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()
    val pagerState = rememberPagerState(pageCount = { 3 })
    val coroutineScope = rememberCoroutineScope()
    val registrarTabs = listOf("Inicio", "Condiciones", "Matricula")
    var tabToNavigate by remember { mutableStateOf(-1) }



    LaunchedEffect(Unit) {
        viewModel.setProyeccion(idEstud)
    }

    LaunchedEffect(tabToNavigate) {
        if (tabToNavigate >= 0) {
            pagerState.animateScrollToPage(tabToNavigate)
            tabToNavigate = -1
        }
    }

    // ─── 2. UI ────────────────────────────────────────────────────────────────
    when {
        modoVer -> {
            VerMatriculaScreen(viewModel = viewModel, navigator = navigator)
        }

        modoRegistrar -> {
            Scaffold(
                modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
                topBar = {
                    StandardTopBar(
                        title = "MATRICULA",
                        subtitle = "Registrar Matricula",
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
                    if (carrerasList.isNotEmpty()) {
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
                                items = carrerasList,
                                selectedItem = selectedCarreraDisplay,
                                onItemSelected = { carrera ->
                                    if (selectedCarreraDisplay?.id_serv != carrera.id_serv) {
                                        selectedCarreraDisplay = carrera
                                        modoVer = false
                                        modoRegistrar = false
                                        val item = proyeccionList.firstOrNull { it.id_serv == carrera.id_serv.toIntOrNull() }
                                        if (item != null) {
                                            idEstudPe = item.id_estud_pe
                                            idPeracad = item.id_peracad
                                            idOacadArranque = item.id_oacad_arranque
                                            idPestDet = item.id_pest_det
                                            idEstudProyeccion = item.id_estud
                                            idEstudServProyeccion = item.id_estud_serv
                                            idTiposerva = item.id_tiposerva
                                            estadoIngresante = item.estado_ingresante
                                            promedioUltMatricula = item.promedio_ult_matricula.toDoubleOrNull() ?: 0.0
                                            if (item.flag_matricula == 1) {
                                                modoVer = true
                                            } else if (item.flag_proyeccion == 0) {
                                                msgSinProyeccion = item.msg_proyeccion
                                                showDialogSinProyeccion = true
                                            } else if (item.flag_proyeccion == 1) {
                                                showLoading = true
                                                viewModel.setObtenerEstudianteMatricula(
                                                    idEstud = item.id_estud,
                                                    idTiposerva = item.id_tiposerva
                                                )
                                                viewModel.setDeudas(
                                                    idPlanEstudioDet = item.id_pest_det,
                                                    idEstudServ = item.id_estud_serv,
                                                    idEstudiante = item.id_estud,
                                                    idPeriodoAcad = item.id_peracad
                                                )
                                            }
                                        }
                                    }
                                },
                                itemLabel = { it.serv_nombre },
                                label = "Carrera",
                                enabled = carrerasList.isNotEmpty()
                            )
                        }
                    }

                    Surface(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(horizontal = 12.dp, vertical = 4.dp),
                        shape = MaterialTheme.shapes.medium,
                        color = colors.colorExpenseItem,
                        border = BorderStroke(1.dp, colors.textColor.copy(alpha = 0.07f))
                    ) {
                    Column(modifier = Modifier.fillMaxSize()) {
                    TabRow(
                        selectedTabIndex = pagerState.currentPage,
                        containerColor = colors.colorExpenseItem,
                        contentColor = colors.colorMixPrimary
                    ) {
                        registrarTabs.forEachIndexed { index, title ->
                            Tab(
                                selected = pagerState.currentPage == index,
                                onClick = {},
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
                        userScrollEnabled = false,
                        modifier = Modifier.fillMaxSize()
                    ) { page ->
                        when (page) {
                            0 -> InicioMatriculaTab(
                                welcomeHtml = welcomeHtml,
                                turnoInicio = turnoInicio,
                                turnoHorario = turnoHorario,
                                flagEnabled = flagEnabledButton,
                                onIniciarClick = {
                                    viewModel.setValidarInicio(
                                        idGrmatricd = turnoIdGrmatricd,
                                        idOacadArranque = idOacadArranque,
                                        idEstudPe = idEstudPe
                                    )
                                }
                            )
                            1 -> CondicionesMatriculaTab(
                                condicionesHtml = condicionesHtml,
                                onAceptarClick = {
                                    viewModel.setVerMatricula(
                                        idPeriodoAcad = idPeracad,
                                        idServicio = servId.toIntOrNull() ?: 0,
                                        idPlanEstudioDet = idPestDet,
                                        idEstudiante = idEstudProyeccion,
                                        id_sistema = idSistema,
                                        uneg = idUneg,
                                        id_usuario = idUsuario
                                    )
                                    coroutineScope.launch {
                                        pagerState.animateScrollToPage(2)
                                    }
                                },
                                onAtrasClick = {
                                    coroutineScope.launch {
                                        pagerState.animateScrollToPage(0)
                                    }
                                }
                            )
                            2 -> GuardarMatriculaTab(
                                cursosList = cursosList,
                                detalleMap = detalleMap,
                                servNombre = selectedCarreraDisplay?.serv_nombre ?: "",
                                idEstud = idEstud.toString(),
                                idServ = servId,
                                idPestDet = idPestDet.toString(),
                                idUneg = idUneg.toString(),
                                idUsuario = idUsuario.toString(),
                                idTipmatric = idTipmatric,
                                estadoIngresante = estadoIngresante,
                                onCursoTapped = { curso, ids_AsignDetCr, id_HoraDia, id_DiaSemana, hora_IniCr, hora_FinCr ->
                                    idOacadDetPendiente = curso.id_oacad_det
                                    viewModel.setDetalleMatricula(
                                        idOfertaAcadDet = curso.id_oacad_det.toIntOrNull() ?: 0,
                                        id_asign_det_cr = ids_AsignDetCr,
                                        id_hora_dia = id_HoraDia,
                                        id_dia_semana = id_DiaSemana,
                                        hora_ini_cr = hora_IniCr,
                                        hora_fin_cr = hora_FinCr
                                    )
                                },
                                onMatricularClick = { body ->
                                    pendingMatriculaBody = body
                                    horaContexto = 2
                                    viewModel.resetHoraServidorState()
                                    viewModel.fetchHoraServidor()
                                },
                                onResumenHistoricoClick = {
                                    resumenHistoricoList = emptyList()
                                    viewModel.setResumenHistorico(idEstudPe, idPeracad)
                                    showResumenHistoricoSheet = true
                                },
                                onCuentaCorrienteClick = {
                                    navigator.navigate("/cuentaCorriente")
                                },
                                onCompartirHorarioClick = { periodo, cursos ->
                                    viewModel.setHorarioPDF(periodo, cursos, Constantes.ID_UNEG)
                                }
                            )
                        }
                    }
                    } // Column interno
                    } // Surface
                }
            }
        }

        else -> {
            Scaffold(
                topBar = {
                    StandardTopBar(
                        title = "MATRICULA",
                        subtitle = "Matricula",
                        onBackClick = { navigator.popBackStack() }
                    )
                }
            ) { innerPadding ->
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding)
                ) {
                    if (carrerasList.isNotEmpty()) {
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
                                items = carrerasList,
                                selectedItem = selectedCarreraDisplay,
                                onItemSelected = { carrera ->
                                    if (selectedCarreraDisplay?.id_serv != carrera.id_serv) {
                                        selectedCarreraDisplay = carrera
                                        modoVer = false
                                        modoRegistrar = false
                                        val item = proyeccionList.firstOrNull { it.id_serv == carrera.id_serv.toIntOrNull() }
                                        if (item != null) {
                                            idEstudPe = item.id_estud_pe
                                            idPeracad = item.id_peracad
                                            idOacadArranque = item.id_oacad_arranque
                                            idPestDet = item.id_pest_det
                                            idEstudProyeccion = item.id_estud
                                            idEstudServProyeccion = item.id_estud_serv
                                            idTiposerva = item.id_tiposerva
                                            estadoIngresante = item.estado_ingresante
                                            promedioUltMatricula = item.promedio_ult_matricula.toDoubleOrNull() ?: 0.0
                                            if (item.flag_matricula == 1) {
                                                modoVer = true
                                            } else if (item.flag_proyeccion == 0) {
                                                msgSinProyeccion = item.msg_proyeccion
                                                showDialogSinProyeccion = true
                                            } else if (item.flag_proyeccion == 1) {
                                                showLoading = true
                                                viewModel.setObtenerEstudianteMatricula(
                                                    idEstud = item.id_estud,
                                                    idTiposerva = item.id_tiposerva
                                                )
                                                viewModel.setDeudas(
                                                    idPlanEstudioDet = item.id_pest_det,
                                                    idEstudServ = item.id_estud_serv,
                                                    idEstudiante = item.id_estud,
                                                    idPeriodoAcad = item.id_peracad
                                                )
                                            }
                                        }
                                    }
                                },
                                itemLabel = { it.serv_nombre },
                                label = "Carrera",
                                enabled = carrerasList.isNotEmpty()
                            )
                        }
                    }
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        if (showLoading) LoadingIndicator()
                    }
                }
            }
        }
    }

    // Dialogs overlays
    if (showDialogNoTurno) {
        CustomDialogBasic(
            visible = true,
            titulo = "Matricula",
            mensaje = "No tiene Matricula Vigente",
            flag_val = 0,
            confirmado = false,
            onDismiss = {
                showDialogNoTurno = false
                //navigator.popBackStack()
            }
        )
    }

    if (showDialogSinProyeccion) {
        CustomDialogBasic(
            visible = true,
            titulo = "Matricula",
            mensaje = msgSinProyeccion,
            flag_val = 0,
            confirmado = false,
            onDismiss = {
                showDialogSinProyeccion = false
                navigator.popBackStack()
            }
        )
    }

    if (showDialogMatriculaDeudas) {
        MatriculaDeudasDialog(
            visible = true,
            deudas = deudasMatricula,
            onContinuar = { showDialogMatriculaDeudas = false },
            onCuentaCorriente = {
                showDialogMatriculaDeudas = false
                navigator.navigate("/cuentaCorriente")
            },
            onRegresar = {
                showDialogMatriculaDeudas = false
               // navigator.popBackStack()
            }
        )
    }

    if (showDialogDocsEstado0) {
        HoraPagoMatriculaDialog(
            visible = true,
            fechaApertura = turnoInicio,
            fechaCierre = turnoFin,
            horario = turnoHorario,
            mensaje = mensajeDocsEstado0,
            onAceptar = { showDialogDocsEstado0 = false }
        )
    }


    if (showDialogFueraFecha) {
        CustomDialogBasic(
            visible = true,
            titulo = "AVISO INFORMATIVO",
            mensaje = "No tiene permiso para continuar con su matricula, por favor contactar con el area academica",
            flag_val = 0,
            confirmado = false,
            onDismiss = {
                showDialogFueraFecha = false
                horaContexto = 0
            }
        )
    }

    if (showDialogPdf && pdfUrl.isNotEmpty()) {
        Dialog(
            onDismissRequest = {},
            properties = DialogProperties(
                usePlatformDefaultWidth = false,
                dismissOnBackPress = false,
                dismissOnClickOutside = false
            )
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .clip(RoundedCornerShape(12.dp))
                    .background(colors.colorExpenseItem)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(40.dp)
                        .background(
                            colors.colorMixPrimary,
                            shape = RoundedCornerShape(topStart = 12.dp, topEnd = 12.dp)
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Alerta",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                    IconButton(
                        onClick = { showDialogPdf = false },
                        modifier = Modifier.align(Alignment.CenterEnd)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Cerrar",
                            tint = Color.White
                        )
                    }
                }
                WebViewComposable(
                    url = "https://docs.google.com/gview?embedded=true&url=$pdfUrl",
                    returnDomain = "",
                    onClose = {},
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth()
                )
                Button(
                    onClick = {
                        showDialogPdf = false
                        tabToNavigate = 1
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 12.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = colors.colorMixPrimary
                    )
                ) {
                    Text(
                        text = "Acepto Terminos y Condiciones",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }
        }
    }

    if (showDialogConfirmarRegistrar) {
        ConfirmarEnvioDialog(
            nombreArchivo = "",
            titulo = "Matricula",
            mensaje = "Estas seguro de generar la matricula?",
            onConfirmar = {
                showProgress = true
                pendingMatriculaBody?.let { body ->
                    viewModel.setRegistrarMatricula(idTiposerva, body)
                    pendingMatriculaBody = null
                }
                showDialogConfirmarRegistrar = false
            },
            onCancelar = {
                showDialogConfirmarRegistrar = false
                pendingMatriculaBody = null
            }
        )
    }

    if (showDialogFueraHorario) {
        CustomDialogBasic(
            visible = true,
            titulo = "Matricula",
            mensaje = "Se encuentra fuera del rango permitido para iniciar su matricula",
            flag_val = 0,
            confirmado = false,
            onDismiss = {
                showDialogFueraHorario = false
                horaContexto = 0
            }
        )
    }

    if (showDialogResultadoRegistrar) {
        CustomDialogBasic(
            visible = true,
            titulo = tituloDialogRegistrar,
            mensaje = mensajeDialogRegistrar,
            flag_val = flagValDialogRegistrar,
            confirmado = flagValDialogRegistrar == 1,
            aceptarSelected = if (flagValDialogRegistrar == 1) 5 else 0,
            dismissOnOutsideClick = false,
            onDismiss = {
                showDialogResultadoRegistrar = false
            },
            onDismissGoScreen = if (flagValDialogRegistrar == 1) ({
                navigator.navigate("/matricula") {
                    popUpTo("/matricula") { inclusive = true }
                    launchSingleTop = false
                    restoreState = false
                }
            }) else null
        )
    }

    if (showToastErrorDocs) {
        showToast("Error documentos S1")
        showToastErrorDocs = false
    }

    if (showResumenHistoricoSheet) {
        val colors = getColorsTheme()
        ModalBottomSheet(
            onDismissRequest = { showResumenHistoricoSheet = false },
            sheetState = resumenHistoricoSheetState,
            containerColor = MaterialTheme.colorScheme.surface
        ) {
            if (resumenHistoricoLoading) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp),
                    contentAlignment = Alignment.Center
                ) {
                    LoadingIndicator()
                }
            } else {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 12.dp),
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    item { Spacer(modifier = Modifier.height(8.dp)) }
                    items(resumenHistoricoList) { item ->
                        ResumenHistoricoCard(item = item)
                    }
                    item { Spacer(modifier = Modifier.height(24.dp)) }
                }
            }
        }
    }

    // ─── 3. when(uiState) — cadena gate ──────────────────────────────────────

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
        is ResourceUiState.Success -> {
            carrerasList = s.data.carrera
            val primeraCarrera = s.data.carrera.firstOrNull { it.flag_carrera == "1" }
                ?: s.data.carrera.firstOrNull()
            selectedCarreraDisplay = primeraCarrera
            viewModel.resetCarreraState()
            val item = primeraCarrera?.let { c ->
                proyeccionList.firstOrNull { it.id_serv == c.id_serv.toIntOrNull() }
            }
            if (item != null) {
                idEstudPe = item.id_estud_pe
                idPeracad = item.id_peracad
                idOacadArranque = item.id_oacad_arranque
                idPestDet = item.id_pest_det
                idEstudProyeccion = item.id_estud
                idEstudServProyeccion = item.id_estud_serv
                idTiposerva = item.id_tiposerva
                estadoIngresante = item.estado_ingresante
                promedioUltMatricula = item.promedio_ult_matricula.toDoubleOrNull() ?: 0.0
                showLoading = false
                if (item.flag_matricula == 1) {
                    modoVer = true
                } else if (item.flag_proyeccion == 0) {
                    msgSinProyeccion = item.msg_proyeccion
                    showDialogSinProyeccion = true
                } else if (item.flag_proyeccion == 1) {
                    viewModel.setObtenerEstudianteMatricula(
                        idEstud = item.id_estud,
                        idTiposerva = item.id_tiposerva
                    )
                    viewModel.setDeudas(
                        idPlanEstudioDet = item.id_pest_det,
                        idEstudServ = item.id_estud_serv,
                        idEstudiante = item.id_estud,
                        idPeriodoAcad = item.id_peracad
                    )
                }
            } else {
                showLoading = false
            }
        }
        else -> {}
    }

    when (val s = uiStateEstudianteMatricula) {
        is ResourceUiState.Success -> {
            val data = s.data.DataObtenerMatricula.firstOrNull()
            if (data != null)
                idTipmatric = data.id_tipmatric.toString()
            viewModel.resetObtenerEstudianteMatriculaState()
        }
        else -> {}
    }

    when (val s = uiStateDeudas) {
        is ResourceUiState.Success -> {
            val lista = s.data.ListMatriculaValidacion
            val deuda = lista.firstOrNull()
            if (deuda != null) {
                when {
                    deuda.flag_deuda == 1 -> {
                        deudasMatricula = lista
                        showDialogMatriculaDeudas = true
                    }
                    (deuda.monto_deuda?.toDoubleOrNull() ?: 0.0) > 0.0 -> {
                        deudasMatricula = lista
                        showDialogMatriculaDeudas = true
                    }
                }
            }
            viewModel.resetDeudasState()
            viewModel.setHoraPago(
                promedio = promedioUltMatricula,
                idPestDet = idPestDet,
                estadoIngresante = estadoIngresante,
                uneg = idUneg,
                idEstudServ = idEstudServProyeccion,
                idPeracad = idPeracad,
                idEstudPe = idEstudPe
            )
        }
        else -> {}
    }

    when (val s = uiStateHoraPago) {
        is ResourceUiState.Success -> {
            val listTurnoPago = s.data.ListTurnoPago
            if (listTurnoPago.isNotEmpty()) {
                if (listTurnoPago[0].estado == 0) {
                    viewModel.resetHoraPagoState()
                    viewModel.setObtenerTurno(
                        promedio = promedioUltMatricula,
                        idPestDet = idPestDet,
                        estadoIngresante = estadoIngresante,
                        uneg = idUneg,
                        idEstudServ = idEstudServProyeccion,
                        idPeracad = idPeracad
                    )
                } else if (listTurnoPago[0].estado == 1) {
                    val last = listTurnoPago.last()
                    turnoInicio = last.matric_inicio
                    turnoFin = last.matric_fin
                    turnoHorario = last.horario
                    turnoIdGrmatricd = last.id_grmatricd
                    viewModel.resetHoraPagoState()
                    viewModel.setValidarDocs(
                        idPeracad = idPeracad,
                        estadoIngresante = estadoIngresante,
                        idEstudPe = idEstudPe
                    )
                }
            }
        }
        else -> {}
    }

    when (val s = uiStateTurno) {
        is ResourceUiState.Success -> {
            val obtenerTurno = s.data.ObtenerTurnoMatricula
            viewModel.resetTurnoState()
            if (obtenerTurno.isEmpty()) {
                showLoading = false
                showDialogNoTurno = true
            } else {
                val last = obtenerTurno.last()
                turnoInicio = last.matric_inicio
                turnoFin = last.matric_fin
                turnoHorario = last.horario
                turnoIdGrmatricd = last.id_grmatricd
                viewModel.setValidarDocs(
                    idPeracad = idPeracad,
                    estadoIngresante = estadoIngresante,
                    idEstudPe = idEstudPe
                )
            }
        }
        else -> {}
    }

    when (val s = uiStateValidarDocs) {
        is ResourceUiState.Success -> {
            val docsIngresante = s.data.dataDocumentosIngresante
            if (docsIngresante.isEmpty()) {
                showToastErrorDocs = true
                showLoading = false
                viewModel.resetValidarDocsState()
            } else if (docsIngresante.isNotEmpty()) {
                if (docsIngresante[0].estado == 0) {
                    flagEnabledButton = 0
                    mensajeDocsEstado0 = docsIngresante[0].mensaje
                    showDialogDocsEstado0 = true
                } else {
                    flagEnabledButton = 1
                }
                servId = selectedCarreraDisplay?.id_serv ?: "0"
                viewModel.resetValidarDocsState()
                viewModel.setTextosHtml(idUneg, idUsuario)
                modoRegistrar = true
                tabToNavigate = 0
                showLoading = false
            }
        }
        else -> {}
    }

    when (val s = uiStateTextosHtml) {
        is ResourceUiState.Success -> {
            welcomeHtml = s.data.ListTextosHtml.firstOrNull()?.mensaje ?: ""
            condicionesHtml = s.data.TxtCondiciones.firstOrNull()?.mensaje ?: ""
            pdfUrl = s.data.pdf
            viewModel.resetTextosHtmlState()
        }
        else -> {}
    }

    when (val s = uiStateVerMatricula) {
        is ResourceUiState.Success -> {
            if (s.data.flag_val != 0) {
                cursosList = s.data.list_vermatric
            }
            viewModel.resetVerMatriculaState()
        }
        else -> {}
    }

    when (val s = uiStateDetalleMatricula) {
        is ResourceUiState.Success -> {
            if (idOacadDetPendiente.isNotEmpty()) {
                detalleMap[idOacadDetPendiente] = s.data.list_detmatric
                idOacadDetPendiente = ""
                viewModel.resetDetalleMatriculaState()
            }
        }
        else -> {}
    }

    when (val s = uiStateValidarInicio) {
        is ResourceUiState.Success -> {
            viewModel.resetValidarInicioState()
            horaContexto = 1
            viewModel.resetHoraServidorState()
            viewModel.fetchHoraServidor()
        }
        else -> {}
    }

    when (val s = uiStateHoraServidor) {
        is ResourceUiState.Success -> {
            if (horaContexto != 0) {
                val serverData = s.data.listHoraServer.firstOrNull()
                if (serverData != null) {
                    val datee = serverData.datee
                    val fechaServidor = datee.substring(0, 10)

                    val iniParts = turnoInicio.split("/")
                    val finParts = turnoFin.split("/")
                    val fechaIni = "${iniParts[2]}-${iniParts[1]}-${iniParts[0]}"
                    val fechaFinStr = "${finParts[2]}-${finParts[1]}-${finParts[0]}"

                    val horaMin = datee.substring(11, 16)
                    val hor = horaMin.substring(0, 2).toIntOrNull() ?: 0
                    val min = horaMin.substring(3, 5).toIntOrNull() ?: 0
                    val minHorTick = hor * 60 + min

                    val horas = turnoHorario.split("-").map { it.trim() }
                    val horaInicio = convertirAHorasMinutos(horas[0])
                    val horaFin = convertirAHorasMinutos(horas[1])

                    if (fechaServidor >= fechaIni && fechaServidor <= fechaFinStr) {
                        if (minHorTick in horaInicio..horaFin) {
                            when (horaContexto) {
                                1 -> { showDialogPdf = true }
                                2 -> { showDialogConfirmarRegistrar = true }
                            }
                        } else {
                            showDialogFueraHorario = true
                        }
                    } else {
                        showDialogFueraFecha = true
                    }
                }
                horaContexto = 0
                viewModel.resetHoraServidorState()
            }
        }
        else -> {}
    }

    when (val s = uiStateRegistrar) {
        is ResourceUiState.Success -> {
            showProgress = false
            tituloDialogRegistrar = s.data.titulo ?: "Matricula"
            mensajeDialogRegistrar = s.data.mensaje
            flagValDialogRegistrar = if (s.data.tipo == 1 || s.data.mensaje == "Estudiante ya se encuentra Matriculado.") 1 else 0
            showDialogResultadoRegistrar = true
            viewModel.resetRegistrarState()
        }
        is ResourceUiState.Error -> {
            showProgress = false
            viewModel.resetRegistrarState()
        }
        else -> {}
    }

    if (modoRegistrar) {
        when (val s = uiStateResumenHistorico) {
            is ResourceUiState.Loading -> { resumenHistoricoLoading = true }
            is ResourceUiState.Success -> {
                resumenHistoricoLoading = false
                resumenHistoricoList = s.data.list_resumenhist
                viewModel.resetResumenHistoricoState()
            }
            is ResourceUiState.Error -> {
                resumenHistoricoLoading = false
                viewModel.resetResumenHistoricoState()
            }
            ResourceUiState.Empty -> {}
        }
    }

    when (val s = uiStateHorarioPDF) {
        is ResourceUiState.Loading -> { showLoading = true }
        is ResourceUiState.Success -> {
            showLoading = false
            if (s.data.flag_val == 1 && s.data.pdfbase64.isNotBlank()) {
                openPdfFromBase64(context, s.data.pdfbase64)
            } else {
                showToast(s.data.mensaje.ifBlank { "No se pudo generar el PDF" })
            }
            viewModel.resetHorarioPDFState()
        }
        is ResourceUiState.Error -> {
            showLoading = false
            showToast("Error al generar PDF")
            viewModel.resetHorarioPDFState()
        }
        ResourceUiState.Empty -> {}
    }

    if (showProgress) {
        FullScreenLoadingOverlay()
    }

}

private fun convertirAHorasMinutos(hora: String): Int {
    val partes = hora.split(":").map { it.toInt() }
    return partes[0] * 60 + partes[1]
}
