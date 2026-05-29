package pe.lecordonbleu.universidadestudiante.presentation.screens.convalidacion

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowRight
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import pe.lecordonbleu.universidadestudiante.data.remote.dto.ListCursosAcademicaItem
import pe.lecordonbleu.universidadestudiante.getColorsTheme
import pe.lecordonbleu.universidadestudiante.data.remote.dto.ListOfertaAcademica
import pe.lecordonbleu.universidadestudiante.data.remote.dto.ListPlanEstudioConv
import pe.lecordonbleu.universidadestudiante.data.remote.dto.ListTipoTraslado
import pe.lecordonbleu.universidadestudiante.getSettingsStorage
import pe.lecordonbleu.universidadestudiante.presentation.components.ComboBoxGenericModel
import pe.lecordonbleu.universidadestudiante.presentation.components.StandardTopBar
import pe.lecordonbleu.universidadestudiante.presentation.screens.convalidacion.customcell.CursosConvalidadosCell
import pe.lecordonbleu.universidadestudiante.presentation.vo.ResourceUiState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ConvalidacionScreen(
    navigator: NavController,
    convalidacionViewModel: ConvalidacionViewModel = viewModel()
) {
    val colors = getColorsTheme()
    val settingsStorage = getSettingsStorage()
    val idEstud = settingsStorage.getInt("idEstud", 0)

    val uiStatePlanEstudio by convalidacionViewModel.uiState.collectAsStateWithLifecycle()
    val uiStateTipoTraslado by convalidacionViewModel.uiStateTipoTrasladoConvalidacion.collectAsStateWithLifecycle()
    val uiStateEstudianteOAcad by convalidacionViewModel.uiStateEstudianteOAcadConvalidacion.collectAsStateWithLifecycle()
    val uiStateSede by convalidacionViewModel.uiStateSedeConvalidacion.collectAsStateWithLifecycle()
    val uiStateCarreras by convalidacionViewModel.uiStateCarrerasConvalidacion.collectAsStateWithLifecycle()
    val uiStateCursos by convalidacionViewModel.uiStateCursosConvalidacion.collectAsStateWithLifecycle()

    var listPlanes by remember { mutableStateOf(emptyList<ListPlanEstudioConv>()) }
    var listTipos by remember { mutableStateOf(emptyList<ListTipoTraslado>()) }
    var listOfertas by remember { mutableStateOf(emptyList<ListOfertaAcademica>()) }
    var listSedes by remember { mutableStateOf(emptyList<ListOfertaAcademica>()) }
    var listCarreras by remember { mutableStateOf(emptyList<String>()) }

    var selectedPlan by remember { mutableStateOf<ListPlanEstudioConv?>(null) }
    var selectedTipo by remember { mutableStateOf<ListTipoTraslado?>(null) }
    var selectedOferta by remember { mutableStateOf<ListOfertaAcademica?>(null) }
    var selectedSede by remember { mutableStateOf<ListOfertaAcademica?>(null) }
    var selectedCarrera by remember { mutableStateOf<String?>(null) }
    var cursos by remember { mutableStateOf(emptyList<ListCursosAcademicaItem>()) }
    var mapaCarreras by remember { mutableStateOf(emptyMap<String, Int>()) }

    var autoPlan by remember { mutableStateOf(false) }
    var autoTipo by remember { mutableStateOf(false) }
    var autoOferta by remember { mutableStateOf(false) }
    var autoSede by remember { mutableStateOf(false) }
    var autoCarrera by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        convalidacionViewModel.setUserCarreraRequest(idEstud)
    }

    Scaffold(
        topBar = {
            StandardTopBar(
                title = "CONVALIDACIONES",
                subtitle = "Simulación de traslado",
                onBackClick = { navigator.popBackStack() }
            )
        },
        containerColor = colors.backGroundColor
    ) { padding ->

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
        ) {

            item {
                ComboBoxGenericModel(
                    items = listPlanes,
                    selectedItem = selectedPlan,
                    label = "Plan de Estudio",
                    itemLabel = { it.pest_det_nombre },
                    onItemSelected = {
                        selectedPlan = it
                        selectedOferta = null
                        selectedSede = null
                        selectedCarrera = null
                        autoOferta = false
                        autoSede = false
                        autoCarrera = false
                        convalidacionViewModel.resetEstudianteOAcadConvalidacionState()
                        convalidacionViewModel.resetSedeConvalidacionState()
                        convalidacionViewModel.resetCarrerasConvalidacionState()
                        convalidacionViewModel.resetCursosConvalidacionState()
                        convalidacionViewModel.setTipoTrasladoRequest(id_uneg = 1)
                    },
                    enabled = listPlanes.isNotEmpty(),
                    backgroundColorComboBox = colors.colorNaranjaOscuro
                )
            }

            item {
                ComboBoxGenericModel(
                    items = listTipos,
                    selectedItem = selectedTipo,
                    label = "Tipo de Simulación",
                    itemLabel = { it.tipo_traslado_nombre },
                    onItemSelected = {
                        selectedTipo = it
                        selectedOferta = null
                        selectedSede = null
                        selectedCarrera = null
                        autoOferta = false
                        autoSede = false
                        autoCarrera = false
                        convalidacionViewModel.resetEstudianteOAcadConvalidacionState()
                        convalidacionViewModel.resetSedeConvalidacionState()
                        convalidacionViewModel.resetCarrerasConvalidacionState()
                        convalidacionViewModel.resetCursosConvalidacionState()
                        convalidacionViewModel.setEstudianteOAcadConvalidacionRequest(
                            condicion = 1,
                            id_uneg = 1,
                            id_tipo_traslado = it.id_tipo_traslado
                        )
                    },
                    enabled = listTipos.isNotEmpty(),
                    backgroundColorComboBox = colors.colorNaranjaOscuro
                )
            }

            item {
                ComboBoxGenericModel(
                    items = listOfertas,
                    selectedItem = selectedOferta,
                    label = "Período",
                    itemLabel = { it.peracad_nombre ?: "" },
                    onItemSelected = {
                        selectedOferta = it
                        selectedSede = null
                        selectedCarrera = null
                        autoSede = false
                        autoCarrera = false
                        convalidacionViewModel.resetSedeConvalidacionState()
                        convalidacionViewModel.resetCarrerasConvalidacionState()
                        convalidacionViewModel.resetCursosConvalidacionState()
                        convalidacionViewModel.setSedeConvalidacionRequest(
                            condicion = 2,
                            id_uneg = 1,
                            id_tipo_traslado = selectedTipo?.id_tipo_traslado ?: 0
                        )
                    },
                    enabled = listOfertas.isNotEmpty(),
                    backgroundColorComboBox = colors.colorNaranjaOscuro
                )
            }

            item {
                ComboBoxGenericModel(
                    items = listSedes,
                    selectedItem = selectedSede,
                    label = "Centro de Estudios",
                    itemLabel = { it.cente_nombre ?: "" },
                    onItemSelected = {
                        selectedSede = it
                        selectedCarrera = null
                        autoCarrera = false
                        convalidacionViewModel.resetCarrerasConvalidacionState()
                        convalidacionViewModel.resetCursosConvalidacionState()
                        selectedOferta?.id_ofer_adm?.let { idOferta ->
                            val idServ = selectedPlan?.id_serv ?: 0
                            convalidacionViewModel.setCarrerasConvalidacionRequest(idOferta, idServ)
                        }
                    },
                    enabled = listSedes.isNotEmpty(),
                    backgroundColorComboBox = colors.primary
                )
            }

            item {
                ComboBoxGenericModel(
                    items = listCarreras,
                    selectedItem = selectedCarrera,
                    label = "Programa Académico",
                    itemLabel = { it },
                    onItemSelected = {
                        autoCarrera = true
                        selectedCarrera = it
                        convalidacionViewModel.resetCursosConvalidacionState()
                        mapaCarreras[it]?.let { id ->
                            convalidacionViewModel.setCursosConvalidacionRequest(
                                id_estud_pe = selectedPlan?.id_estud_pe ?: 0,
                                id_pest_det_destino = id,
                                id_tipo_traslado = selectedTipo?.id_tipo_traslado ?: 0
                            )
                        }
                    },
                    enabled = listCarreras.isNotEmpty(),
                    backgroundColorComboBox = colors.primary
                )
            }

            item {
                if (cursos.isNotEmpty()) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 12.dp)
                            .background(color = colors.colorAzulMarcacionClaro, shape = RoundedCornerShape(12.dp))
                            .padding(vertical = 10.dp, horizontal = 16.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = cursos.first().mensaje_porc,
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.Bold,
                            color = colors.colorExpenseItem
                        )
                    }

                    Spacer(Modifier.height(18.dp))

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Filled.ArrowRight,
                            contentDescription = null,
                            tint = colors.colorNaranjaOscuro
                        )
                        Spacer(Modifier.width(8.dp))
                        Column {
                            Text(
                                text = "Origen: Programa actual",
                                style = MaterialTheme.typography.bodyMedium,
                                fontWeight = FontWeight.Bold,
                                color = colors.textColor
                            )
                            Text(
                                text = "Destino: programa para traslado",
                                style = MaterialTheme.typography.bodyMedium,
                                fontWeight = FontWeight.Bold,
                                color = colors.colorNaranjaOscuro
                            )
                        }
                    }
                }
            }

            item {
                when {
                    cursos.isNotEmpty() -> {
                        Spacer(modifier = Modifier.height(16.dp))
                        CursosConvalidadosCell(cursos = cursos)
                    }
                    uiStateCursos is ResourceUiState.Empty && selectedCarrera != null -> {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 20.dp)
                                .background(color = colors.colorExpenseItem, shape = RoundedCornerShape(12.dp))
                                .padding(16.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "No se encontraron resultados.",
                                style = MaterialTheme.typography.bodyMedium,
                                fontWeight = FontWeight.Bold,
                                color = colors.textColor
                            )
                        }
                    }
                }
            }
        }
    }

    when (val s = uiStatePlanEstudio) {
        is ResourceUiState.Success -> {
            listPlanes = s.data.ListPlanEstudioConv
            if (!autoPlan && listPlanes.isNotEmpty()) {
                autoPlan = true
                selectedPlan = listPlanes.first()
                convalidacionViewModel.setTipoTrasladoRequest(id_uneg = 1)
            }
        }
        else -> listPlanes = emptyList()
    }

    when (val s = uiStateTipoTraslado) {
        is ResourceUiState.Success -> {
            listTipos = s.data.ListTipoTraslado
            if (!autoTipo && listTipos.isNotEmpty()) {
                autoTipo = true
                selectedTipo = listTipos.first()
                convalidacionViewModel.setEstudianteOAcadConvalidacionRequest(
                    condicion = 1,
                    id_uneg = 1,
                    id_tipo_traslado = selectedTipo!!.id_tipo_traslado
                )
            }
        }
        else -> listTipos = emptyList()
    }

    when (val s = uiStateEstudianteOAcad) {
        is ResourceUiState.Success -> {
            listOfertas = s.data.firstOrNull()?.ListOfertaAcademica ?: emptyList()
            if (!autoOferta && listOfertas.isNotEmpty()) {
                autoOferta = true
                selectedOferta = listOfertas.first()
                convalidacionViewModel.setSedeConvalidacionRequest(
                    condicion = 2,
                    id_uneg = 1,
                    id_tipo_traslado = selectedTipo?.id_tipo_traslado ?: 0
                )
            }
        }
        else -> listOfertas = emptyList()
    }

    when (val s = uiStateSede) {
        is ResourceUiState.Success -> {
            listSedes = s.data.firstOrNull()?.ListOfertaAcademica ?: emptyList()
            if (!autoSede && listSedes.isNotEmpty()) {
                autoSede = true
                selectedSede = listSedes.first()
                selectedOferta?.id_ofer_adm?.let { idOferta ->
                    val idServ = selectedPlan?.id_serv ?: 0
                    convalidacionViewModel.setCarrerasConvalidacionRequest(idOferta, idServ)
                }
            }
        }
        else -> listSedes = emptyList()
    }
    when (val s = uiStateCarreras) {
        is ResourceUiState.Success -> {
            val items = s.data.firstOrNull()?.ListCarrerasAcademica ?: emptyList()
            listCarreras = items.map { it.pest_det_nombre }
            mapaCarreras = items.associate { it.pest_det_nombre to it.id_pest_det_destino }
            if (!autoCarrera && selectedCarrera == null && listCarreras.isNotEmpty()) {
                autoCarrera = true
                selectedCarrera = listCarreras.first()
                mapaCarreras[selectedCarrera!!]?.let { id ->
                    convalidacionViewModel.setCursosConvalidacionRequest(
                        id_estud_pe = selectedPlan?.id_estud_pe ?: 0,
                        id_pest_det_destino = id,
                        id_tipo_traslado = selectedTipo?.id_tipo_traslado ?: 0
                    )
                }
            }
        }
        else -> {
            listCarreras = emptyList()
            mapaCarreras = emptyMap()
        }
    }
    when (val s = uiStateCursos) {
        is ResourceUiState.Success -> cursos = s.data.ListCursosAcademica
        is ResourceUiState.Empty -> cursos = emptyList()
        is ResourceUiState.Error -> cursos = emptyList()
        ResourceUiState.Loading -> cursos = emptyList()
    }
}
