@file:OptIn(ExperimentalMaterial3Api::class)

package pe.lecordonbleu.universidadestudiante.presentation.screens.marcarasistencia

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
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
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import kotlinx.coroutines.delay
import pe.lecordonbleu.universidadestudiante.LoadingIndicator
import pe.lecordonbleu.universidadestudiante.data.remote.dto.estado_marcar
import pe.lecordonbleu.universidadestudiante.getAppVersion
import pe.lecordonbleu.universidadestudiante.getColorsTheme
import pe.lecordonbleu.universidadestudiante.getSettingsStorage
import pe.lecordonbleu.universidadestudiante.presentation.components.StandardTopBar
import pe.lecordonbleu.universidadestudiante.presentation.components.dialogs.CustomDialogBasic
import pe.lecordonbleu.universidadestudiante.presentation.vo.ResourceUiState

@Composable
fun MarcarAsistenciaScreen(
    viewModel: MarcarAsistenciaViewModel,
    navigator: NavController
) {
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()
    val colors = getColorsTheme()
    val settings = getSettingsStorage()
    val uneg = settings.getInt("id_uneg", 1)
    val idEstudPe = settings.getInt("idEstudPe", 0)
    val idServ = settings.getInt("idServ", 0)
    val idUsuario = settings.getInt("idUsuario", 0)
    val idSistema = settings.getInt("idSistema", 0)
    val idPerfil = settings.getInt("idPerfil", 0)
    val persNombre = settings.getString("persNombre", "").orEmpty()
    val persApellidoPat = settings.getString("persApellidoPat", "").orEmpty()
    val persApellidoMat = settings.getString("persApellidoMat", "").orEmpty()

    // --- Sección 1: Variables y estados ---
    var showLoading by remember { mutableStateOf(false) }
    var estadoMarcacion by remember { mutableStateOf<List<estado_marcar>>(emptyList()) }
    var btnEnabled by remember { mutableStateOf(false) }
    var showDialogMarcacion by remember { mutableStateOf(false) }
    var tituloDialog by remember { mutableStateOf("") }
    var mensajeDialog by remember { mutableStateOf("") }
    var flagValDialog by remember { mutableStateOf(0) }
    var fechaDia by remember { mutableStateOf("") }
    var horaDisplay by remember { mutableStateOf("--:--:--") }
    var tiempoActualEnSegundos by remember { mutableStateOf(0) }
    var horaDemoLimite by remember { mutableStateOf(Int.MAX_VALUE) }
    var clockStarted by remember { mutableStateOf(false) }
    var estadoCargado by remember { mutableStateOf(false) }

    val horaUiState by viewModel.horaUiState.collectAsStateWithLifecycle()
    val verMarcarUiState by viewModel.verMarcarUiState.collectAsStateWithLifecycle()
    val marcarUiState by viewModel.marcarAsistenciaUiState.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.fetchHoraServidor()
        viewModel.setVerMarcar(uneg, idEstudPe, idServ)
    }

    LaunchedEffect(clockStarted) {
        if (clockStarted) {
            while (true) {
                delay(1000L)
                tiempoActualEnSegundos++
                val h = tiempoActualEnSegundos / 3600
                val m = (tiempoActualEnSegundos % 3600) / 60
                val s = tiempoActualEnSegundos % 60
                horaDisplay = "${h.toString().padStart(2, '0')}:${m.toString().padStart(2, '0')}:${s.toString().padStart(2, '0')}"
                if (tiempoActualEnSegundos >= horaDemoLimite) btnEnabled = false
            }
        }
    }

    LaunchedEffect(estadoCargado) {
        if (estadoCargado) {
            viewModel.setNavigationLog(
                nomCompleto = "$persNombre, $persApellidoPat $persApellidoMat",
                nombreArchivo = "MarcarAsistenciaScreen",
                perf_nombre = "Estudiante",
                divasitAulaDemo = if (estadoMarcacion.isNotEmpty()) "Con boton" else "Sin boton",
                idUNEG = uneg,
                id_usuario = idUsuario,
                idPerfil = idPerfil,
                dato = "",
                sistema = "APP ESTUDIANTE KMP",
                ip = "",
                flag_boton = if (estadoMarcacion.isNotEmpty()) 1 else 0,
                nombreUNEG = "ULCB"
            )
        }
    }

    // --- Sección 2: UI ---
    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            StandardTopBar(
                title = "Marcar Asistencia",
                subtitle = fechaDia,
                onBackClick = { navigator.popBackStack() },
                scrollBehavior = scrollBehavior
            )
        },
        containerColor = colors.backGroundColor
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 16.dp)
        ) {
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    text = horaDisplay,
                    fontSize = 32.sp,
                    fontWeight = FontWeight.Bold,
                    color = colors.textColor
                )
                Spacer(modifier = Modifier.height(16.dp))

                if (estadoMarcacion.isNotEmpty()) {
                    val marcar = estadoMarcacion[0]
                    Text(
                        text = marcar.asign_det_nombre,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = colors.textColor
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = marcar.detalle_marcacion,
                        fontSize = 14.sp,
                        color = colors.colorGrisNeutro
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = marcar.docente,
                        fontSize = 14.sp,
                        color = colors.colorGrisNeutro
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "$persApellidoPat $persApellidoMat, $persNombre",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = colors.textColor
                    )
                    Text(
                        text = "v${getAppVersion()}",
                        fontSize = 12.sp,
                        color = colors.colorGrisNeutro
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Habilitada: ${marcar.fec_aut_demo_ini} hasta ${marcar.fec_aut_demo_fin}",
                        fontSize = 12.sp,
                        color = colors.colorGrisNeutro
                    )
                    Spacer(modifier = Modifier.height(24.dp))
                    Button(
                        onClick = {
                            btnEnabled = false
                            viewModel.setMarcarAsistencia(
                                uneg,
                                idUsuario,
                                marcar.id_hor_asis.toIntOrNull() ?: 0,
                                idEstudPe,
                                idSistema
                            )
                        },
                        enabled = btnEnabled,
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = colors.colorNaranjaOscuro,
                            disabledContainerColor = colors.colorGrisMedio
                        )
                    ) {
                        Text("Marcar Asistencia", color = colors.colorBlanco)
                    }
                } else if (!showLoading) {
                    Text(
                        text = "Sin actividades para marcar",
                        fontSize = 16.sp,
                        color = colors.colorGrisNeutro
                    )
                }
            }
        }
    }

    // --- Sección 3: when (uiState) ---
    when (horaUiState) {
        is ResourceUiState.Loading -> { showLoading = true }
        is ResourceUiState.Success -> {
            showLoading = false
            val data = (horaUiState as ResourceUiState.Success).data
            if (data.listHoraServer.isNotEmpty() && !clockStarted) {
                val datee = data.listHoraServer[0].datee
                fechaDia = data.listHoraServer[0].fecha_dia
                val horamin = datee.substring(11, 16)
                val minuto = horamin.substring(3).toInt()
                val hor = horamin.substring(0, 2).toInt()
                val seg = datee.takeLast(2).toInt()
                tiempoActualEnSegundos = (hor * 3600) + (minuto * 60) + seg
                clockStarted = true
            }
        }
        is ResourceUiState.Error -> { showLoading = false }
        ResourceUiState.Empty -> {}
    }

    when (verMarcarUiState) {
        is ResourceUiState.Loading -> { showLoading = true }
        is ResourceUiState.Success -> {
            showLoading = false
            val marcarDetalle = (verMarcarUiState as ResourceUiState.Success).data.estado_marcar
            estadoMarcacion = marcarDetalle
            if (marcarDetalle.isNotEmpty()) {
                val horaDemoFin = marcarDetalle[0].fec_aut_demo_fin.split(" ").getOrNull(1) ?: ""
                if (horaDemoFin.isNotEmpty()) {
                    val h = horaDemoFin.substring(0, 2).toIntOrNull() ?: 0
                    val m = horaDemoFin.substring(3, 5).toIntOrNull() ?: 0
                    val s = horaDemoFin.substring(6).toIntOrNull() ?: 0
                    horaDemoLimite = (h * 3600) + (m * 60) + s
                }
                btnEnabled = tiempoActualEnSegundos < horaDemoLimite
            }
            estadoCargado = true
        }
        is ResourceUiState.Error -> { showLoading = false }
        ResourceUiState.Empty -> {}
    }

    when (marcarUiState) {
        is ResourceUiState.Loading -> { showLoading = true }
        is ResourceUiState.Success -> {
            showLoading = false
            val response = (marcarUiState as ResourceUiState.Success).data
            val registros = response.registrar_marcacion
            if (registros.isNotEmpty() && !showDialogMarcacion) {
                tituloDialog = registros[0].titulo
                mensajeDialog = registros[0].mensaje
                flagValDialog = response.flag_val
                showDialogMarcacion = true
            }
        }
        is ResourceUiState.Error -> { showLoading = false }
        ResourceUiState.Empty -> {}
    }

    if (showLoading) {
        LoadingIndicator()
    }

    if (showDialogMarcacion) {
        CustomDialogBasic(
            visible = true,
            titulo = tituloDialog,
            mensaje = mensajeDialog,
            flag_val = flagValDialog,
            confirmado = flagValDialog == 1,
            onDismiss = {
                showDialogMarcacion = false
                viewModel.resetMarcarAsistenciaState()
                viewModel.setVerMarcar(uneg, idEstudPe, idServ)
            }
        )
    }
}
