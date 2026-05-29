package pe.lecordonbleu.universidadestudiante.presentation.screens.eta

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import pe.lecordonbleu.universidadestudiante.presentation.components.StandardTopBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import kotlinx.coroutines.launch
import org.koin.compose.koinInject
import pe.lecordonbleu.universidadestudiante.SelectorDocumentoPDF
import pe.lecordonbleu.universidadestudiante.SettingsStorage
import pe.lecordonbleu.universidadestudiante.getPlatformContext
import pe.lecordonbleu.universidadestudiante.util.openUrl
import pe.lecordonbleu.universidadestudiante.data.remote.dto.ListDocumentosEta
import pe.lecordonbleu.universidadestudiante.data.remote.dto.ResponseCarreraRemote
import pe.lecordonbleu.universidadestudiante.data.remote.dto.ResponseDocumentoEta
import pe.lecordonbleu.universidadestudiante.data.remote.dto.ResponseEliminarDocEta
import pe.lecordonbleu.universidadestudiante.data.remote.dto.ResponseGuardarEta
import pe.lecordonbleu.universidadestudiante.data.remote.dto.ResponsePeriodoEta
import pe.lecordonbleu.universidadestudiante.getColorsTheme
import pe.lecordonbleu.universidadestudiante.getSettingsStorage
import pe.lecordonbleu.universidadestudiante.presentation.components.ProgressBarLoading
import pe.lecordonbleu.universidadestudiante.presentation.components.dialogs.DialogoEliminarDocumento
import pe.lecordonbleu.universidadestudiante.presentation.screens.eta.customcell.DocumentosETACell
import pe.lecordonbleu.universidadestudiante.presentation.vo.ResourceUiState


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ETAScreen(
    viewModel: ETAViewModel,
    navigator: NavController
) {
    val colors = getColorsTheme()
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    val settingsStorage: SettingsStorage = getSettingsStorage()
    val idEstud = settingsStorage.getInt("idEstud", 0)


    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val uiStatePeriodo by viewModel.uiStatePeriodo.collectAsStateWithLifecycle()
    val uiStateDocumentosEta by viewModel.uiStateDocumentosEta.collectAsStateWithLifecycle()
    val uiStateGuardarEta by viewModel.uiStateGuardarEta.collectAsStateWithLifecycle()
    val uiStateEliminarDocEta by viewModel.uiStateEliminarDocEta.collectAsStateWithLifecycle()

    var id_estud_serv by remember { mutableStateOf<Int?>(null) }
    var id_estud by remember { mutableStateOf<String?>(null) }
    var id_pest_det by remember { mutableStateOf<Int?>(null) }
    var id_serv by remember { mutableStateOf<Int?>(null) }
    var id_oacad_arranque by remember { mutableStateOf<Int?>(null) }
    var id_oaa_pcs by remember { mutableStateOf<Int?>(null) }

    var estaCargando by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        viewModel.setUserCarreraRequest(idEstud)
    }


    val listaOpciones = when (uiState) {
        is ResourceUiState.Success -> {
            val data = (uiState as ResourceUiState.Success<List<ResponseCarreraRemote>>).data
            val carreras = data.firstOrNull()?.carrera ?: emptyList()
            carreras.map { it.serv_nombre }
        }

        else -> emptyList()
    }

    val listaPeriodos = when (uiStatePeriodo) {
        is ResourceUiState.Success -> {
            val data = (uiStatePeriodo as ResourceUiState.Success<List<ResponsePeriodoEta>>).data
            val periodos = data.firstOrNull()?.ListEtaPeriodo ?: emptyList()
            periodos.map { it.peracad_nombre }
        }

        else -> emptyList()
    }

    val listaDocumentos = when (uiStateDocumentosEta) {
        is ResourceUiState.Success -> {
            val lista =
                (uiStateDocumentosEta as ResourceUiState.Success<List<ResponseDocumentoEta>>).data
            lista.firstOrNull()?.ListDocumentosEta ?: emptyList()
        }

        else -> emptyList()
    }

    val seleccionInicial = listaOpciones.firstOrNull() ?: ""
    val seleccionPeriodoInicial = listaPeriodos.firstOrNull() ?: ""

    LaunchedEffect(uiState) {
        if (uiState is ResourceUiState.Success) {
            val carrera = (uiState as ResourceUiState.Success<List<ResponseCarreraRemote>>)
                .data.firstOrNull()?.carrera?.firstOrNull()
            carrera?.let {
                id_estud_serv = it.id_estud_serv.toInt()
                id_pest_det = it.id_pest_det.toInt()
                id_serv = it.id_serv.toInt()
                id_estud = it.id_estud
                viewModel.setEtaPeriodoRequest(id_pest_det!!, id_serv!!)
            }
        }
    }

    LaunchedEffect(uiStatePeriodo) {
        if (uiStatePeriodo is ResourceUiState.Success) {
            val periodo = (uiStatePeriodo as ResourceUiState.Success<List<ResponsePeriodoEta>>)
                .data.firstOrNull()?.ListEtaPeriodo?.firstOrNull()

            periodo?.let {
                id_oacad_arranque = it.id_oacad_arranque
                id_oaa_pcs = it.id_oaa_pcs

                if (id_estud_serv != null && id_pest_det != null) {
                    viewModel.setDocumentosEtaRequest(
                        id_pest_det = id_pest_det!!,
                        id_uneg = 2,
                        id_oacad_arranque = id_oacad_arranque!!,
                        id_estud_serv = id_estud_serv!!,
                        id_oaa_pcs = id_oaa_pcs!!
                    )
                } else {
                    viewModel.clearDocumentosEta()
                }
            } ?: run {
                viewModel.clearDocumentosEta()
            }
        } else if (uiStatePeriodo is ResourceUiState.Error) {
            viewModel.clearDocumentosEta()
        }
    }

    LaunchedEffect(uiStateGuardarEta) {
        when (uiStateGuardarEta) {
            is ResourceUiState.Success -> {
                estaCargando = false
                val resultado =
                    (uiStateGuardarEta as ResourceUiState.Success<List<ResponseGuardarEta>>).data
                val mensaje =
                    resultado.firstOrNull()?.guardar_documentos_eta?.firstOrNull()?.mensaje
                scope.launch { snackbarHostState.showSnackbar(mensaje ?: "Guardado exitoso") }
                id_estud_serv?.let { estud ->
                    id_pest_det?.let { pest ->
                        id_oacad_arranque?.let { arranque ->
                            id_oaa_pcs?.let { pcs ->
                                viewModel.setDocumentosEtaRequest(pest, 2, arranque, estud, pcs)
                            }
                        }
                    }
                }

                viewModel.resetGuardarEtaState()
            }

            is ResourceUiState.Error -> {
                estaCargando = false
                scope.launch { snackbarHostState.showSnackbar("Error al guardar") }
            }

            else -> Unit
        }
    }

    LaunchedEffect(uiStateEliminarDocEta) {
        when (uiStateEliminarDocEta) {
            is ResourceUiState.Success -> {
                val mensaje =
                    (uiStateEliminarDocEta as ResourceUiState.Success<List<ResponseEliminarDocEta>>)
                        .data.firstOrNull()?.EliminarDocuEtaRes?.firstOrNull()?.mensaje

                scope.launch { snackbarHostState.showSnackbar(mensaje ?: "Documento eliminado") }

                id_estud_serv?.let { estud ->
                    id_pest_det?.let { pest ->
                        id_oacad_arranque?.let { arranque ->
                            id_oaa_pcs?.let { pcs ->
                                viewModel.setDocumentosEtaRequest(pest, 2, arranque, estud, pcs)
                            }
                        }
                    }
                }

                viewModel.resetEliminarDocEtaState()
            }

            is ResourceUiState.Error -> {
                scope.launch { snackbarHostState.showSnackbar("Error al eliminar documento") }
            }

            else -> Unit
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            StandardTopBar(
                title = "ETA",
                subtitle = "EXAMEN DE TRANSFERENCIA",
                onBackClick = { navigator.popBackStack() }
            )
        },
        containerColor = colors.backGroundColor
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)

        ) {

            if (listaOpciones.isNotEmpty()) {
                MyComboBoxComponentModel2(
                    items = listaOpciones,
                    label = "Seleccioná una carrera",
                    initialSelection = seleccionInicial,
                    onItemSelected = { nuevaSeleccion ->
                        val carreraSeleccionada =
                            (uiState as? ResourceUiState.Success<List<ResponseCarreraRemote>>)
                                ?.data?.firstOrNull()
                                ?.carrera?.firstOrNull { it.serv_nombre == nuevaSeleccion }

                        carreraSeleccionada?.let {
                            id_estud_serv = it.id_estud_serv.toInt()
                            id_pest_det = it.id_pest_det.toInt()
                            id_serv = it.id_serv.toInt()
                            viewModel.setEtaPeriodoRequest(id_pest_det!!, id_serv!!)
                        }
                    }
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            MyComboBoxComponentModel2(
                items = listaPeriodos,
                label = "Seleccioná un período",
                initialSelection = seleccionPeriodoInicial,
                onItemSelected = { nuevoPeriodo ->
                    val periodoSeleccionado =
                        (uiStatePeriodo as? ResourceUiState.Success<List<ResponsePeriodoEta>>)
                            ?.data?.firstOrNull()?.ListEtaPeriodo?.firstOrNull { it.peracad_nombre == nuevoPeriodo }

                    periodoSeleccionado?.let {
                        id_oacad_arranque = it.id_oacad_arranque
                        id_oaa_pcs = it.id_oaa_pcs
                        if (id_estud_serv != null && id_pest_det != null && id_oacad_arranque != null && id_oaa_pcs != null) {
                            viewModel.setDocumentosEtaRequest(
                                id_pest_det = id_pest_det!!,
                                id_uneg = 2,
                                id_oacad_arranque = id_oacad_arranque!!,
                                id_estud_serv = id_estud_serv!!,
                                id_oaa_pcs = id_oaa_pcs!!
                            )
                        }
                    }
                }
            )

            Spacer(modifier = Modifier.height(24.dp))

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(colors.colorAzulProfundo)
                    .padding(vertical = 12.dp, horizontal = 16.dp)
            ) {
                Text(
                    text = "Listado de Documentos ETA",
                    style = MaterialTheme.typography.titleMedium.copy(
                        color = Color.White,
                        fontWeight = FontWeight.Bold
                    )
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            if (uiStateDocumentosEta is ResourceUiState.Loading) {
                Box(
                    modifier = Modifier
                        .fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            } else {
                ListaDocumentosETA(
                    listaDocumentos = listaDocumentos,
                    viewModel = viewModel,
                    idEstud = id_estud,
                    estaCargando = estaCargando,
                    onCambiarCarga = { estaCargando = it }
                )
            }

        }
    }
}


@Composable
fun ListaDocumentosETA(
    listaDocumentos: List<ListDocumentosEta>,
    viewModel: ETAViewModel,
    idEstud: String?,
    estaCargando: Boolean,
    onCambiarCarga: (Boolean) -> Unit
) {
    val settingsStorage = koinInject<SettingsStorage>()
    val id_usuario = settingsStorage.getInt("idUsuario", 0).toString()
    val context = getPlatformContext()

    var documentoParaSubir by remember { mutableStateOf<ListDocumentosEta?>(null) }
    var documentoParaVer by remember { mutableStateOf<ListDocumentosEta?>(null) }
    var documentoParaEliminar by remember { mutableStateOf<ListDocumentosEta?>(null) }

    LaunchedEffect(documentoParaVer) {
        val url = documentoParaVer?.url_pcs_exam_exam
        if (!url.isNullOrBlank()) {
            openUrl(context, url)
        }
        documentoParaVer = null
    }

    documentoParaEliminar?.let { doc ->
        DialogoEliminarDocumento(
            onConfirmar = {
                viewModel.setEliminarDocEtaRequest(doc.idPcsEstudExam)
                documentoParaEliminar = null
            },
            onCancelar = { documentoParaEliminar = null }
        )
    }

    documentoParaSubir?.let { doc ->
        SelectorDocumentoPDF(
            onDocumentoSeleccionado = { bytes, _ ->
                @OptIn(kotlin.io.encoding.ExperimentalEncodingApi::class)
                val base64 = kotlin.io.encoding.Base64.encode(bytes)
                val id_sistema = settingsStorage.getInt("idSistema", 0).toString()
                onCambiarCarga(true)
                viewModel.setGuardarEtaRequest(
                    id_uneg = 2,
                    pdfbase64 = base64,
                    id_estud = idEstud ?: "",
                    id_user = id_usuario,
                    id_pcs_estud = doc.idPcsEstud.toString(),
                    id_pcs_docu = doc.idPcsDocu.toString(),
                    nombreDocAbrev = doc.nombreDocAbrev,
                    pcs_estud_nombre = doc.estudNombre,
                    id_pcs_estud_exam = doc.idPcsEstudExam.toString(),
                    id_sistema = id_sistema
                )
                documentoParaSubir = null
            },
            onDismiss = { documentoParaSubir = null }
        )
    }

    Box(modifier = Modifier.fillMaxSize()) {
        if (listaDocumentos.isNotEmpty()) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp)
            ) {
                items(listaDocumentos) { documento ->
                    DocumentosETACell(
                        documento = documento,
                        onVer = { documentoParaVer = documento },
                        onEliminar = { documentoParaEliminar = documento },
                        onSubir = { documentoParaSubir = documento }
                    )
                }
            }
        } else {
            Text(
                "No hay documentos disponibles.",
                modifier = Modifier
                    .padding(top = 8.dp)
                    .align(Alignment.TopCenter),
                color = Color.Gray
            )
        }

        if (estaCargando) {
            ProgressBarLoading(mensaje = "Subiendo documento...")
        }
    }
}
