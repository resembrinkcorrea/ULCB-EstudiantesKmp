package pe.lecordonbleu.universidadestudiante.presentation.screens.home

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDrawerState
import androidx.compose.material3.rememberTopAppBarState
import pe.lecordonbleu.universidadestudiante.presentation.components.mainComponent.MainBottomBar
import pe.lecordonbleu.universidadestudiante.presentation.components.mainComponent.MainDrawerContent
import pe.lecordonbleu.universidadestudiante.presentation.components.mainComponent.MainTopBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import kotlinx.coroutines.launch
import pe.lecordonbleu.universidadestudiante.MicrosoftLoginListener
import pe.lecordonbleu.universidadestudiante.SettingsStorage
import pe.lecordonbleu.universidadestudiante.data.remote.dto.DataMenu
import pe.lecordonbleu.universidadestudiante.data.remote.dto.ListAsignaturaEncuesta
import pe.lecordonbleu.universidadestudiante.data.remote.dto.ResponseDataMenu
import pe.lecordonbleu.universidadestudiante.data.remote.dto.ResponseHora
import pe.lecordonbleu.universidadestudiante.data.remote.dto.ListEncuestaData
import pe.lecordonbleu.universidadestudiante.data.remote.dto.ResponseAsignaturaEncuesta
import pe.lecordonbleu.universidadestudiante.data.remote.dto.ResponseEncuestaSatisfaccionEstado
import pe.lecordonbleu.universidadestudiante.data.remote.dto.ResponseListarEncuesta
import pe.lecordonbleu.universidadestudiante.getColorsTheme
import pe.lecordonbleu.universidadestudiante.getPlatformContext
import pe.lecordonbleu.universidadestudiante.getSettingsStorage
import pe.lecordonbleu.universidadestudiante.logout
import pe.lecordonbleu.universidadestudiante.openMicrosoftMFA
import pe.lecordonbleu.universidadestudiante.openMicrosoftPasswordChange
import pe.lecordonbleu.universidadestudiante.util.openUrl
import pe.lecordonbleu.universidadestudiante.presentation.screens.home.encuestadocente.AlertDialogEncuestas
import pe.lecordonbleu.universidadestudiante.presentation.screens.home.encuestasatisfaccion.EncuestaSatisfaccionDialog
import pe.lecordonbleu.universidadestudiante.presentation.screens.home.encuestadocente.LlenarEncuestaDialog
import pe.lecordonbleu.universidadestudiante.presentation.components.mainComponent.HomeContent
import pe.lecordonbleu.universidadestudiante.presentation.vo.ResourceUiState
import pe.lecordonbleu.universidadestudiante.getFcmToken
import androidx.compose.runtime.DisposableEffect
import pe.lecordonbleu.universidadestudiante.AppUpdateChecker
import pe.lecordonbleu.universidadestudiante.fetchFirebaseVersion
import pe.lecordonbleu.universidadestudiante.getAppVersion
import pe.lecordonbleu.universidadestudiante.getStoreUpdateUrl
import pe.lecordonbleu.universidadestudiante.presentation.components.dialogs.CustomDialogUpdate
import pe.lecordonbleu.universidadestudiante.data.remote.dto.ResponseActualizarToken
import pe.lecordonbleu.universidadestudiante.data.remote.dto.Horario
import pe.lecordonbleu.universidadestudiante.data.remote.dto.ResponseHorario
import pe.lecordonbleu.universidadestudiante.getTodayLocalDateTime
import pe.lecordonbleu.universidadestudiante.util.openPdfFromBytes


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    viewModel: HomeViewModel,
    navigator: NavController,
    idSistema: Int,
    idPerfil: Int
) {
    val colors = getColorsTheme()
    val settingsStorage: SettingsStorage = getSettingsStorage()
    val platformContext = getPlatformContext()

    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior(rememberTopAppBarState())
    var currentTab by remember { mutableStateOf("inicio") }

    var menus by remember { mutableStateOf<List<DataMenu>>(emptyList()) }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    var isLoading by remember { mutableStateOf(false) }

    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val uiStateHora by viewModel.uiStateHora.collectAsStateWithLifecycle()
    val proyeccionState by viewModel.proyeccionState.collectAsStateWithLifecycle()
    val encuestaDocenteState by viewModel.encuestaDocenteState.collectAsStateWithLifecycle()
    val encuestaSatisfaccionState by viewModel.encuestaSatisfaccionState.collectAsStateWithLifecycle()
    val listarEncuestaState by viewModel.listarEncuestaState.collectAsStateWithLifecycle()
    var showEncuestaDocenteDialog by remember { mutableStateOf(false) }
    var showEncuestaSatisfaccionDialog by remember { mutableStateOf(false) }
    var showLlenarEncuestaDialog by remember { mutableStateOf(false) }
    var encuestaDocenteLanzada by remember { mutableStateOf(false) }
    var encuestaSatisfaccionLanzada by remember { mutableStateOf(false) }
    var listarEncuestaLanzada by remember { mutableStateOf(false) }

    var horaServ by remember { mutableStateOf("") }
    var todasAsignaturas by remember { mutableStateOf<List<ListAsignaturaEncuesta>>(emptyList()) }
    var preguntasEncuesta by remember { mutableStateOf<List<ListEncuestaData>>(emptyList()) }
    var tituloAsignaturaSeleccionada by remember { mutableStateOf("") }
    val idPersDet = settingsStorage.getInt("idPersDet", 0)
    val estUrlPhoto = settingsStorage.getString("estUrlFoto", "").orEmpty()
    val emailUser = settingsStorage.getString("emailUser", "").orEmpty()
    val nombreUser = settingsStorage.getString("persNombre", "").orEmpty()
    val apellidoUser = buildString {
        append(settingsStorage.getString("persApellidoPat", "").orEmpty())
        val mat = settingsStorage.getString("persApellidoMat", "").orEmpty()
        if (mat.isNotEmpty()) append(" $mat")
    }
    val idEstud = settingsStorage.getInt("idEstud", 0)
    var tokenFcm by remember { mutableStateOf("") }
    val fcmTokenState by viewModel.fcmTokenState.collectAsStateWithLifecycle()
    val fichaMatrState by viewModel.fichaMatrState.collectAsStateWithLifecycle()
    var showFichaMatri by remember { mutableStateOf(false) }
    var isFichaMatriLoading by remember { mutableStateOf(false) }
    val clasesHoyState by viewModel.clasesHoyState.collectAsStateWithLifecycle()
    var clasesHoy by remember { mutableStateOf<List<Horario>>(emptyList()) }
    var showClasesHoy by remember { mutableStateOf(false) }
    var clasesHoyDismissed by remember { mutableStateOf(false) }
    var clasesHoyLanzada by remember { mutableStateOf(false) }

    val token = getFcmToken()
    val tokenGuardado = settingsStorage.getString("fcm_token")
    var nuevaVersionFirebase by remember { mutableStateOf("") }

    AppUpdateChecker()

    DisposableEffect(Unit) {
        val cleanup = fetchFirebaseVersion { version ->
            val current = getAppVersion()
            val c = current.split(".").map { it.toIntOrNull() ?: 0 }
            val r = version.split(".").map { it.toIntOrNull() ?: 0 }
            val maxLen = maxOf(c.size, r.size)
            var result = 0
            for (i in 0 until maxLen) {
                val cv = c.getOrElse(i) { 0 }
                val rv = r.getOrElse(i) { 0 }
                if (cv < rv) { result = -1; break }
                if (cv > rv) { result = 1; break }
            }
            nuevaVersionFirebase = if (result < 0) version else ""
        }
        onDispose { cleanup() }
    }

    if (nuevaVersionFirebase.isNotEmpty()) {
        CustomDialogUpdate(
            nuevaVersion = nuevaVersionFirebase,
            versionActual = getAppVersion(),
            onOmitir = { nuevaVersionFirebase = "" },
            onActualizar = {
                openUrl(platformContext, getStoreUpdateUrl())
            }
        )
    }

    LaunchedEffect(idSistema, idPerfil) {
        viewModel.fetchProyeccionValidacion(idEstud)
        viewModel.setUserMenuRequest(1, idSistema, idPerfil)


        if (idEstud > 0 && !token.isNullOrEmpty() && token != tokenGuardado) {
            tokenFcm = token
            viewModel.setRegistrarFcmToken(idEstud, token)
        }
    }

    // ── Animación 3D del Drawer ─────────────────────
    val drawerProgress by animateFloatAsState(
        targetValue = if (drawerState.isOpen) 1f else 0f,
        animationSpec = tween(durationMillis = 300),
        label = "drawer3d"
    )
    val drawerScale = 1f - 0.10f * drawerProgress
    val drawerCorner = (32 * drawerProgress).dp
    val drawerBlur = (20 * drawerProgress).dp

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            MainDrawerContent(
                userNombre = nombreUser,
                userApellido = apellidoUser,
                userEmail = emailUser,
                userFoto = estUrlPhoto,
                colors = colors,
                onCloseDrawer = { scope.launch { drawerState.close() } },
                onLogoutClick = {
                    val flagLogueo = settingsStorage.getInt("flagLogueo", -1)
                    if (flagLogueo == 1 || flagLogueo == 0) {
                        navigator.navigate("/login") {
                            popUpTo("/homeScreen?id_sistema=$idSistema&id_perfil=$idPerfil") {
                                inclusive = true
                            }
                            launchSingleTop = true
                        }
                    } else if (flagLogueo == 3) {
                        logout(platformContext, object : MicrosoftLoginListener {
                            override suspend fun onSuccess(
                                emailM: String,
                                displayNameM: String,
                                jobTitleM: String,
                                officeLocationM: String,
                                mobilePhoneM: String,
                                photoBytesM: ByteArray?,
                                lastPasswordChangeDateM: String
                            ) {
                                settingsStorage.clearAll()
                                viewModel.resetHoraState()
                                navigator.navigate("/login") {
                                    popUpTo("/homeScreen?id_sistema=$idSistema&id_perfil=$idPerfil") {
                                        inclusive = true
                                    }
                                    launchSingleTop = true
                                }
                            }

                            override fun onError(errorMessage: String) {
                                println("Error al cerrar sesion: $errorMessage")
                            }
                        })
                    }
                },
                onGoProfile = { navigator.navigate("/perfilEstudiante") }
            )
        }
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black)
        ) {
            Scaffold(
                topBar = {
                    MainTopBar(
                        unegName = "",
                        colors = colors,
                        scrollBehavior = scrollBehavior,
                        onOpenDrawer = { scope.launch { drawerState.open() } }
                    )
                },
                bottomBar = {
                    MainBottomBar(
                        currentTab = currentTab,
                        colors = colors,
                        onTabSelected = { tab ->
                            currentTab = tab
                            when (tab) {
                                "qr"         -> navigator.navigate("/qrEstudiante")
                                "asistencia" -> navigator.navigate("/misAsistencias")
                                "horario"    -> navigator.navigate("/horarioEstudiante")
                            }
                        }
                    )
                },
                containerColor = colors.backGroundColor,
                modifier = Modifier
                    .fillMaxSize()
                    .blur(radius = drawerBlur)
                    .graphicsLayer {
                        scaleX = drawerScale
                        scaleY = drawerScale
                        shape = RoundedCornerShape(drawerCorner)
                        clip = true
                    }
            ) { paddingValues ->
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues)
                ) {
                    HomeContent(
                        menus = menus,
                        isLoading = isLoading,
                        colors = colors,
                        showFichaMatri = showFichaMatri,
                        isFichaMatriLoading = isFichaMatriLoading,
                        clasesHoy = clasesHoy,
                        showClasesHoy = showClasesHoy,
                        onClasesHoyClose = {
                            showClasesHoy = false
                            clasesHoyDismissed = true
                        },
                        onFichaMatriClick = {
                            val now = getTodayLocalDateTime()
                            val meses = listOf("enero","febrero","marzo","abril","mayo","junio",
                                "julio","agosto","septiembre","octubre","noviembre","diciembre")
                            val dias = listOf("lunes","martes","miércoles","jueves","viernes","sábado","domingo")
                            val diaName = dias[now.dayOfWeek.ordinal]
                            val fechaFormateada = "${diaName.replaceFirstChar { it.uppercase() }}, ${now.dayOfMonth} de ${meses[now.monthNumber - 1]} de ${now.year}"
                            viewModel.setFichaMatr(
                                idUNEG = 1,
                                idPeriodoAcademico = settingsStorage.getInt("idPerAcad", 0).toString(),
                                personaNombre = settingsStorage.getString("persNombre", "").orEmpty(),
                                personaPaterno = settingsStorage.getString("persApellidoPat", "").orEmpty(),
                                personaMaterno = settingsStorage.getString("persApellidoMat", "").orEmpty(),
                                periodo = settingsStorage.getString("peracadNombre", "").orEmpty(),
                                codigoEstud = settingsStorage.getString("estCodigo", "").orEmpty(),
                                carreraProf = settingsStorage.getString("pestDetNombre", "").orEmpty(),
                                valorFecha = fechaFormateada,
                                idOacadArranque = settingsStorage.getInt("idOacadArranque", 0).toString(),
                                idEstudPe = settingsStorage.getInt("idEstudPe", 0).toString(),
                                idUsuario = settingsStorage.getInt("idUsuario", 0),
                                idPestDet = settingsStorage.getInt("idPestDet", 0),
                                idEstud = idEstud,
                                estadoIngresante = settingsStorage.getString("estadoIngresante", "0").orEmpty(),
                                promUltMat = settingsStorage.getString("promedioUltMatricula", "").orEmpty()
                            )
                            viewModel.fetchFichaMatricula()
                        },
                        onMenuClick = { menu ->
                            when (menu.textoMenuAbrev) {
                                "MI QR" -> navigator.navigate("/qrEstudiante")
                                "VER MARCACION" -> navigator.navigate("/verMarcacionView")
                                "MARC. ASISTENCIA" -> navigator.navigate("/marcarAsistencia")
                                "ASIST. ESTUDIANTE" -> navigator.navigate("/asistenciaEstudianteView")
                                "MI HORARIO", "HORARIO" -> navigator.navigate("/horarioEstudiante")
                                "ARCH. COMPARTIDOS" -> navigator.navigate("/archivosCompartidos")
                                "CONVALIDACIONES" -> navigator.navigate("/convalidacion")
                                "MALLA CURRICULAR" -> navigator.navigate("/malla")
                                "PERFIL" -> navigator.navigate("/perfilEstudiante")
                                "CAMBIO DE CONTRASEÑA" -> openMicrosoftPasswordChange(platformContext)
                                "MFA" -> openMicrosoftMFA(platformContext)
                                "ETA" -> navigator.navigate("/eta")
                                "MIS ENLACES" -> navigator.navigate("/misEnlaces")
                                "MIS NOTAS" -> navigator.navigate("/notas")
                                "MIS ASISTENCIAS" -> navigator.navigate("/misAsistencias")
                                "HISTORIAL ACADEMICO" -> navigator.navigate("/historialAcademico")
                                "BIBLIOTECA" -> navigator.navigate("/biblioteca")
                                "MIS OFERTAS" -> navigator.navigate("/misOfertas")
                                "MATRICULA" -> navigator.navigate("/matricula")
                                "TRAMITE.DOC" -> navigator.navigate("/tramiteDocumentario")
                                "MIS AVISOS" -> {
                                    navigator.navigate("/misavisos")
                                }
                                "VER BOUTIQUE" -> openUrl(platformContext, "https://ecommerce.ilcb.edu.pe/pages/")
                            }
                        },
                        onNavigate = { route -> navigator.navigate(route) }
                    )
                }
            }
        }
    }


    when (uiState) {
        is ResourceUiState.Success -> {
            val responseData =
                (uiState as ResourceUiState.Success<List<ResponseDataMenu>>).data
            menus = responseData.flatMap { it.data_menu }
            errorMessage = null
            isLoading = false
        }
        is ResourceUiState.Error -> {
            menus = emptyList()
            errorMessage = (uiState as ResourceUiState.Error).message
            isLoading = false
        }
        is ResourceUiState.Loading -> {
            isLoading = true
        }
        ResourceUiState.Empty -> {
            menus = emptyList()
            errorMessage = null
            isLoading = false
        }
    }
    when (uiStateHora) {
        is ResourceUiState.Loading -> {}
        is ResourceUiState.Empty -> {}
        is ResourceUiState.Success -> {
            val response = (uiStateHora as ResourceUiState.Success<ResponseHora>).data
            val datee = response.listHoraServer.firstOrNull()?.datee?.toString().orEmpty()
            val horaServidor = datee.split('T')[1]
            horaServ = horaServidor
        }
        is ResourceUiState.Error -> {
            val error = (uiStateHora as ResourceUiState.Error).message
            Text("Error: $error", color = Color.Red)
        }
    }
    when (proyeccionState) {
        is ResourceUiState.Success -> {
            val lista = (proyeccionState as ResourceUiState.Success).data.ListProyeccionValidacion
            if (lista.isNotEmpty()) {
                settingsStorage.putInt("idEstudPe", lista[0].id_estud_pe)
                settingsStorage.putInt("idServ", lista[0].id_serv)
                settingsStorage.putInt("idOacadArranque", lista[0].id_oacad_arranque)
                settingsStorage.putInt("idPerAcad", lista[0].id_peracad)
                settingsStorage.putInt("idPestDet", lista[0].id_pest_det)
                settingsStorage.putString("estCodigo", lista[0].est_codigo)
                settingsStorage.putString("pestDetNombre", lista[0].pest_det_nombre)
                settingsStorage.putString("peracadNombre", lista[0].peracad_nombre)
                settingsStorage.putString("estadoIngresante", lista[0].estado_ingresante.toString())
                settingsStorage.putString("promedioUltMatricula", lista[0].promedio_ult_matricula)
                if (lista[0].id_proce_mat == 1) showFichaMatri = true
                if (!encuestaDocenteLanzada) {
                    encuestaDocenteLanzada = true
                    viewModel.fetchEncuestaDocente(
                        idPeracad = lista[0].id_peracad,
                        idEstudPe = lista[0].id_estud_pe,
                        idServ = lista[0].id_serv,
                        idOacadArranque = lista[0].id_oacad_arranque
                    )
                }
                if (!clasesHoyLanzada) {
                    clasesHoyLanzada = true
                    viewModel.fetchClasesHoy(lista[0].id_estud_pe, lista[0].id_oacad_arranque)
                }
            }
        }
        else -> {}
    }
    when (encuestaDocenteState) {
        is ResourceUiState.Success -> {
            val response = (encuestaDocenteState as ResourceUiState.Success<ResponseAsignaturaEncuesta>).data
            val hayPendientes = response.ListAsignaturaEncuesta.any { it.flag_registrado == 0 }
            if (hayPendientes) {
                if (!showEncuestaDocenteDialog) {
                    todasAsignaturas = response.ListAsignaturaEncuesta.sortedBy { it.flag_registrado }
                    showEncuestaDocenteDialog = true
                }
            } else {
                if (!encuestaSatisfaccionLanzada) {
                    encuestaSatisfaccionLanzada = true
                    val idEstudPe = settingsStorage.getInt("idEstudPe", 0)
                    val idServ = settingsStorage.getInt("idServ", 0)
                    val idOacadArranque = settingsStorage.getInt("idOacadArranque", 0)
                    val idPerAcad = settingsStorage.getInt("idPerAcad", 0)
                    viewModel.fetchEncuestaSatisfaccion(idPerAcad, idEstudPe, idServ, idOacadArranque)
                }
            }
        }
        else -> {}
    }
    when (encuestaSatisfaccionState) {
        is ResourceUiState.Success -> {
            val response = (encuestaSatisfaccionState as ResourceUiState.Success<ResponseEncuestaSatisfaccionEstado>).data
            val estado = response.EstadoEncuesta.firstOrNull()
            if (estado != null && estado.valida_encuesta == 0 && !showEncuestaSatisfaccionDialog) {
                showEncuestaSatisfaccionDialog = true
            }
        }
        else -> {}
    }

    when (listarEncuestaState) {
        is ResourceUiState.Success -> {
            val response = (listarEncuestaState as ResourceUiState.Success<ResponseListarEncuesta>).data
            if (response.ListEncuestaData.isNotEmpty() && !showLlenarEncuestaDialog) {
                preguntasEncuesta = response.ListEncuestaData
                showLlenarEncuestaDialog = true
            }
        }
        else -> {}
    }

    when (fcmTokenState) {
        is ResourceUiState.Success -> {
            val response = (fcmTokenState as ResourceUiState.Success<ResponseActualizarToken>).data
            if (response.flag_val == 1 && tokenFcm.isNotEmpty()) {
                settingsStorage.putString("fcm_token", tokenFcm)
            }
        }
        else -> {}
    }

    when (fichaMatrState) {
        is ResourceUiState.Loading -> isFichaMatriLoading = true
        is ResourceUiState.Success -> {
            isFichaMatriLoading = false
            val bytes = (fichaMatrState as ResourceUiState.Success<ByteArray>).data
            openPdfFromBytes(platformContext, bytes)
            viewModel.resetFichaMatrState()
        }
        is ResourceUiState.Error -> {
            isFichaMatriLoading = false
            viewModel.resetFichaMatrState()
        }
        else -> isFichaMatriLoading = false
    }

    when (clasesHoyState) {
        is ResourceUiState.Success -> {
            val response = (clasesHoyState as ResourceUiState.Success<ResponseHorario>).data
            clasesHoy = response.listadoHorario
            if (!clasesHoyDismissed) showClasesHoy = true
        }
        else -> {}
    }

    if (showEncuestaDocenteDialog) {
        AlertDialogEncuestas(
            asignaturas = todasAsignaturas,
            onAsignaturaClick = { asignatura ->
                if (!listarEncuestaLanzada) {
                    listarEncuestaLanzada = true
                    tituloAsignaturaSeleccionada = asignatura.pest_asign_nombre
                    showEncuestaDocenteDialog = false
                    viewModel.fetchListarEncuesta(
                        idPestDet = settingsStorage.getInt("idPestDet", 0),
                        idPeracad = settingsStorage.getInt("idPerAcad", 0)
                    )
                }
            },
            onDismiss = {
                showEncuestaDocenteDialog = false
                viewModel.resetEncuestaDocenteState()
            }
        )
    }

    if (showLlenarEncuestaDialog) {
        LlenarEncuestaDialog(
            preguntas = preguntasEncuesta,
            tituloAsignatura = tituloAsignaturaSeleccionada,
            onDismiss = {
                showLlenarEncuestaDialog = false
                viewModel.resetListarEncuestaState()
            }
        )
    }

    if (showEncuestaSatisfaccionDialog) {
        EncuestaSatisfaccionDialog(
            idPeracad = settingsStorage.getInt("idPerAcad", 0),
            idEstudPe = settingsStorage.getInt("idEstudPe", 0),
            idServ = settingsStorage.getInt("idServ", 0),
            idOacadArranque = settingsStorage.getInt("idOacadArranque", 0),
            idUsuario = settingsStorage.getInt("idUsuario", 0),
            onDismiss = {
                showEncuestaSatisfaccionDialog = false
                viewModel.resetEncuestaSatisfaccionState()
            }
        )
    }

    errorMessage?.let { msg ->
        Text(
            text = "Error: $msg",
            color = colors.textColor
        )
    }
}


fun isHoraDentroRango(
    horaActual: String,
    horaInicio: String,
    horaFin: String,
    fecAutDemofin: String
): Int {
    return try {
        val fecAutDemoHorfin = fecAutDemofin.split(" ")
        val fecAutDemoFin = fecAutDemoHorfin[1].split(":")

        val horAutHor = fecAutDemoFin[0].toInt() * 3600
        val minAutHor = fecAutDemoFin[1].toInt() * 60
        val segAutHor = fecAutDemoFin[2].toDouble().toInt()

        val fecDateActual = horaActual.split(" ")
        val horActual = fecDateActual[1].split(":")

        val horHor = horActual[0].toInt() * 3600
        val minHor = horActual[1].toInt() * 60
        val segHor = horActual[2].toDouble().toInt()

        val totalHorActual = horHor + minHor + segHor

        val horaIni = horaInicio.split(":")
        val horIni = horaIni[0].toInt() * 3600
        val minIni = horaIni[1].toInt() * 60
        val segIni = 0

        val totalHorInicio = horIni + minIni + segIni

        val horaFinArr = horaFin.split(":")
        val horFin = horaFinArr[0].toInt() * 3600
        val minFin = horaFinArr[1].toInt() * 60
        val segFin = 0

        val totalHorFin = horFin + minFin + segFin

        if (fecAutDemofin == "1900-01-01 00:00:00.0") {
            return if (totalHorActual in totalHorInicio..totalHorFin) 1 else 0
        } else {
            val totalHorDemoFin = horAutHor + minAutHor + segAutHor

            return if (
                totalHorActual in totalHorInicio..totalHorFin &&
                totalHorActual <= totalHorDemoFin
            ) {
                2
            } else if (totalHorActual in totalHorInicio..totalHorFin) {
                3
            } else {
                0
            }
        }

    } catch (e: Exception) {
        0
    }
}
