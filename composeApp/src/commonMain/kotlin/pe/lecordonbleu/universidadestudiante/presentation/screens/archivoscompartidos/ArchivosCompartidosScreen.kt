package pe.lecordonbleu.universidadestudiante.presentation.screens.archivoscompartidos

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Folder
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberModalBottomSheetState
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
import pe.lecordonbleu.universidadestudiante.data.remote.dto.ContenidoTags
import pe.lecordonbleu.universidadestudiante.data.remote.dto.TagsCompartidosEstudiante
import pe.lecordonbleu.universidadestudiante.getColorsTheme
import pe.lecordonbleu.universidadestudiante.getSettingsStorage
import pe.lecordonbleu.universidadestudiante.presentation.components.AppDropdownMenu
import pe.lecordonbleu.universidadestudiante.presentation.components.StandardTopBar
import pe.lecordonbleu.universidadestudiante.presentation.components.dialogs.CustomDialogBasic
import pe.lecordonbleu.universidadestudiante.presentation.screens.archivoscompartidos.components.ContenidoTabCompartidos
import pe.lecordonbleu.universidadestudiante.presentation.vo.ResourceUiState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ArchivosCompartidosScreen(
    viewModel: ArchivosCompartidosViewModel,
    navigator: NavController
) {
    // ─── 1. Variables y estados ───────────────────────────────────────────────
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()
    val colors = getColorsTheme()
    val settings = getSettingsStorage()
    val idEstud = settings.getInt("idEstud", 0)
    val idUsuario = settings.getInt("idUsuario", 0)
    val idUneg = settings.getInt("id_uneg", 2)

    val uiStateListaServicio by viewModel.uiStateListaServicio.collectAsStateWithLifecycle()
    val uiStateTipoServicio by viewModel.uiStateTipoServicio.collectAsStateWithLifecycle()
    val uiStateTags by viewModel.uiStateTags.collectAsStateWithLifecycle()
    val uiStateContenido by viewModel.uiStateContenido.collectAsStateWithLifecycle()
    val uiStateEstadoArchivo by viewModel.uiStateEstadoArchivo.collectAsStateWithLifecycle()

    var servicioList by remember { mutableStateOf(emptyList<Triple<String, Int, Int>>()) }
    var tipoServicioList by remember { mutableStateOf(emptyList<Pair<String, Int>>()) }
    var tagsList by remember { mutableStateOf(emptyList<TagsCompartidosEstudiante>()) }
    var contenidoList by remember { mutableStateOf(emptyList<ContenidoTags>()) }

    var selectedServicio by remember { mutableStateOf<Triple<String, Int, Int>?>(null) }
    var selectedTipoServicio by remember { mutableStateOf<Pair<String, Int>?>(null) }
    var selectedTag by remember { mutableStateOf<TagsCompartidosEstudiante?>(null) }

    var showTagSheet by remember { mutableStateOf(false) }
    var showLoadingContenido by remember { mutableStateOf(false) }

    var servicioLanzado by remember { mutableStateOf(false) }
    var tipoServicioLanzado by remember { mutableStateOf(false) }
    var tagsLanzado by remember { mutableStateOf(false) }

    var showDialogEstado by remember { mutableStateOf(false) }
    var dialogEstadoTitulo by remember { mutableStateOf("") }
    var dialogEstadoMensaje by remember { mutableStateOf("") }
    var dialogEstadoFlagVal by remember { mutableStateOf(0) }
    var pendingEstadoDialog by remember { mutableStateOf(false) }

    val idServ = selectedServicio?.second ?: 0

    LaunchedEffect(Unit) {
        viewModel.setServicioRequest(idEstud)
    }

    // ─── 2. UI ────────────────────────────────────────────────────────────────
    Scaffold(
        topBar = {
            StandardTopBar(
                title = "Archivos Compartidos",
                subtitle = "ARCHIVOS COMPARTIDOS",
                onBackClick = { navigator.popBackStack() },
                scrollBehavior = scrollBehavior
            )
        },
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        containerColor = colors.backGroundColor
    ) { padding ->
        Column(Modifier.fillMaxSize().padding(padding)) {

            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 16.dp, end = 16.dp, top = 12.dp),
                color = colors.colorExpenseItem,
                shape = MaterialTheme.shapes.medium,
                border = BorderStroke(1.dp, colors.textColor.copy(alpha = 0.05f))
            ) {
                Column(
                    modifier = Modifier.fillMaxWidth().padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    AppDropdownMenu(
                        items = servicioList,
                        selectedItem = selectedServicio,
                        label = "Servicio Académico",
                        itemLabel = { it.first },
                        onItemSelected = { triple ->
                            selectedServicio = triple
                            selectedTipoServicio = null
                            selectedTag = null
                            tipoServicioList = emptyList()
                            tagsList = emptyList()
                            contenidoList = emptyList()
                            tipoServicioLanzado = false
                            tagsLanzado = false
                            viewModel.resetTipoServicioState()
                            viewModel.resetTagsState()
                            viewModel.resetContenidoState()
                            viewModel.setServicioTipoRequest(idUneg, idEstud, triple.second)
                        },
                        enabled = servicioList.isNotEmpty()
                    )
                    AppDropdownMenu(
                        items = tipoServicioList,
                        selectedItem = selectedTipoServicio,
                        label = "Carrera",
                        itemLabel = { it.first },
                        onItemSelected = { pair ->
                            selectedTipoServicio = pair
                            selectedTag = null
                            tagsList = emptyList()
                            contenidoList = emptyList()
                            tagsLanzado = false
                            viewModel.resetTagsState()
                            viewModel.resetContenidoState()
                            viewModel.setTagsRequest(idUneg, idEstud, pair.second, idServ)
                        },
                        enabled = tipoServicioList.isNotEmpty()
                    )
                }
            }

            if (tagsList.isNotEmpty()) {
                Text(
                    text = "CARPETA ACTUAL",
                    modifier = Modifier.padding(start = 16.dp, top = 16.dp, bottom = 8.dp),
                    style = MaterialTheme.typography.labelSmall,
                    fontWeight = FontWeight.SemiBold,
                    color = colors.textColor.copy(alpha = 0.5f),
                    letterSpacing = 1.sp
                )

                Surface(
                    onClick = { showTagSheet = true },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    color = colors.colorExpenseItem,
                    shape = MaterialTheme.shapes.medium,
                    border = BorderStroke(1.dp, colors.textColor.copy(alpha = 0.05f))
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth().padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Folder,
                            contentDescription = null,
                            tint = colors.colorMixPrimary
                        )
                        Text(
                            text = selectedTag?.nombre_oferta_cab ?: "",
                            modifier = Modifier.weight(1f),
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.Bold,
                            color = colors.textColor
                        )
                        Icon(
                            imageVector = Icons.Default.KeyboardArrowDown,
                            contentDescription = null,
                            tint = colors.textColor.copy(alpha = 0.5f)
                        )
                    }
                }

                ContenidoTabCompartidos(
                    items = contenidoList,
                    isLoading = showLoadingContenido,
                    onItemClick = { doc ->
                        viewModel.setEstadoArchivoRequest(idUneg, idUsuario, doc.id_carpeta_docu_estado, 1)
                    },
                    onCheckClick = { doc ->
                        val newFlag = if (doc.flag_leido == 1) 0 else 1
                        pendingEstadoDialog = true
                        viewModel.setEstadoArchivoRequest(idUneg, idUsuario, doc.id_carpeta_docu_estado, newFlag)
                    }
                )
            }
        }
    }

    if (showDialogEstado) {
        CustomDialogBasic(
            visible = true,
            titulo = dialogEstadoTitulo,
            mensaje = dialogEstadoMensaje,
            flag_val = dialogEstadoFlagVal,
            confirmado = dialogEstadoFlagVal == 1,
            onDismiss = {
                showDialogEstado = false
                viewModel.resetEstadoArchivoState()
                selectedTag?.let { tag ->
                    viewModel.setContenidoTagsRequest(idUneg, tag.id_oferta_carpeta_det, idUsuario)
                }
            }
        )
    }

    if (showTagSheet) {
        ModalBottomSheet(
            onDismissRequest = { showTagSheet = false },
            sheetState = rememberModalBottomSheetState()
        ) {
            Text(
                text = "Seleccionar Carpeta",
                modifier = Modifier.padding(start = 16.dp, bottom = 16.dp),
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = colors.textColor
            )
            tagsList.forEach { tag ->
                val isSelected = tag.id_oferta_carpeta_det == selectedTag?.id_oferta_carpeta_det
                Surface(
                    onClick = {
                        selectedTag = tag
                        showTagSheet = false
                        contenidoList = emptyList()
                        viewModel.resetContenidoState()
                        viewModel.setContenidoTagsRequest(idUneg, tag.id_oferta_carpeta_det, idUsuario)
                    },
                    modifier = Modifier.fillMaxWidth(),
                    color = if (isSelected) colors.colorMixPrimary.copy(alpha = 0.1f) else colors.backGroundColor
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 14.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Folder,
                            contentDescription = null,
                            tint = if (isSelected) colors.colorMixPrimary else colors.textColor.copy(alpha = 0.4f)
                        )
                        Text(
                            text = tag.nombre_oferta_cab,
                            modifier = Modifier.weight(1f),
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                            color = if (isSelected) colors.colorMixPrimary else colors.textColor
                        )
                        if (isSelected) {
                            Icon(
                                imageVector = Icons.Default.Check,
                                contentDescription = null,
                                tint = colors.colorMixPrimary
                            )
                        }
                    }
                }
            }
            Spacer(Modifier.height(24.dp))
        }
    }

    // ─── 3. when ─────────────────────────────────────────────────────────────
    when (val state = uiStateListaServicio) {
        is ResourceUiState.Success -> {
            servicioList = state.data.ListarServicio.map { Triple(it.serv_nombre, it.id_serv, it.id_tiposerva) }
            if (!servicioLanzado && servicioList.isNotEmpty()) {
                servicioLanzado = true
                selectedServicio = servicioList.first()
                viewModel.setServicioTipoRequest(idUneg, idEstud, servicioList.first().second)
            }
        }
        else -> {}
    }

    when (val state = uiStateTipoServicio) {
        is ResourceUiState.Success -> {
            tipoServicioList = state.data.ListarTipoServicio.map { it.tiposerva_nombre to it.id_tiposerva }
            if (!tipoServicioLanzado && tipoServicioList.isNotEmpty()) {
                tipoServicioLanzado = true
                selectedTipoServicio = tipoServicioList.first()
                viewModel.setTagsRequest(idUneg, idEstud, tipoServicioList.first().second, idServ)
            }
        }
        else -> {}
    }

    when (val state = uiStateTags) {
        is ResourceUiState.Success -> {
            tagsList = state.data.TagsCompartidosEstudiante
            if (!tagsLanzado && tagsList.isNotEmpty()) {
                tagsLanzado = true
                selectedTag = tagsList.first()
                viewModel.setContenidoTagsRequest(idUneg, tagsList.first().id_oferta_carpeta_det, idUsuario)
            }
        }
        else -> {}
    }

    when (uiStateContenido) {
        is ResourceUiState.Loading -> { showLoadingContenido = true }
        is ResourceUiState.Success -> {
            showLoadingContenido = false
            contenidoList = (uiStateContenido as ResourceUiState.Success).data.ContenidoTags
        }
        is ResourceUiState.Error -> { showLoadingContenido = false }
        ResourceUiState.Empty -> { showLoadingContenido = false }
    }

    when (val state = uiStateEstadoArchivo) {
        is ResourceUiState.Success -> {
            if (pendingEstadoDialog) {
                val item = state.data.ListadoArchivoEstado.firstOrNull()
                dialogEstadoTitulo = item?.titulo ?: ""
                dialogEstadoMensaje = item?.mensaje ?: ""
                dialogEstadoFlagVal = state.data.flag_val
                showDialogEstado = true
                pendingEstadoDialog = false
            }
        }
        else -> {}
    }
}
