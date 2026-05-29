package pe.lecordonbleu.universidadestudiante.presentation.screens.mallacurricular

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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Book
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.HighlightOff
import androidx.compose.material.icons.filled.MilitaryTech
import androidx.compose.material.icons.filled.PictureAsPdf
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import pe.lecordonbleu.universidadestudiante.data.remote.dto.ResponseCarreraRemote
import pe.lecordonbleu.universidadestudiante.data.remote.dto.ResponsePlanEstudio
import pe.lecordonbleu.universidadestudiante.data.remote.dto.ResponseTablaPlan
import pe.lecordonbleu.universidadestudiante.data.remote.dto.ResponseValoresPlan
import pe.lecordonbleu.universidadestudiante.getColorsTheme
import pe.lecordonbleu.universidadestudiante.getSettingsStorage
import pe.lecordonbleu.universidadestudiante.presentation.components.MyComboBoxComponentModel2
import pe.lecordonbleu.universidadestudiante.presentation.components.StandardTopBar
import pe.lecordonbleu.universidadestudiante.presentation.screens.mallacurricular.customcell.GraficaMallaCurricularCell
import pe.lecordonbleu.universidadestudiante.presentation.screens.mallacurricular.customcell.MallaResumenCell
import pe.lecordonbleu.universidadestudiante.presentation.screens.mallacurricular.customcell.TablaPlanEstudioExpandableCell
import pe.lecordonbleu.universidadestudiante.presentation.vo.ResourceUiState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MallaCurricularScreen(
    viewModel: MallaCurricularViewModel,
    navigator: NavController
) {
    val colors = getColorsTheme()
    val settingsStorage = getSettingsStorage()
    val idEstud = settingsStorage.getInt("idEstud", 0)

    val carreraUiState by viewModel.uiState.collectAsStateWithLifecycle()
    val planEstudioState by viewModel.planEstudioState.collectAsStateWithLifecycle()
    val valoresPlanState by viewModel.valoresPlanState.collectAsStateWithLifecycle()
    val tablaPlanState by viewModel.tablaPlanState.collectAsStateWithLifecycle()

    var seleccionCarrera by remember { mutableStateOf("") }
    var seleccionPlanEstudio by remember { mutableStateOf("") }
    var cicloExpandido by rememberSaveable { mutableStateOf<Int?>(null) }

    val listaCarreras = (carreraUiState as? ResourceUiState.Success<List<ResponseCarreraRemote>>)
        ?.data?.firstOrNull()
        ?.carrera?.map { it.serv_nombre } ?: emptyList()

    val listaPlanesEstudio =
        (planEstudioState as? ResourceUiState.Success<List<ResponsePlanEstudio>>)
            ?.data?.firstOrNull()
            ?.ListPlanEstudio
            ?.map { it.pest_det_nombre } ?: emptyList()

    val grupos = (tablaPlanState as? ResourceUiState.Success<List<ResponseTablaPlan>>)
        ?.data?.firstOrNull()?.ListTablaPlanEstudio
        ?.groupBy { it.CICLO_ACADÉMICO } ?: emptyMap()

    val todasAsignaturas = grupos.values.flatten()
    val coloresAsignaturas = ColoresPorPrerequisito(todasAsignaturas)

    LaunchedEffect(Unit) {
        viewModel.setUserCarreraRequest(idEstud)
    }

    LaunchedEffect(listaCarreras) {
        if (listaCarreras.isNotEmpty()) {
            seleccionCarrera = listaCarreras.first()
            val carreraSeleccionada =
                (carreraUiState as? ResourceUiState.Success<List<ResponseCarreraRemote>>)
                    ?.data?.firstOrNull()
                    ?.carrera?.firstOrNull { it.serv_nombre == seleccionCarrera }

            carreraSeleccionada?.let {
                viewModel.setPlanEstudioRequest(it.id_estud_serv.toInt())
            }
        }
    }

    LaunchedEffect(listaPlanesEstudio) {
        if (listaPlanesEstudio.isNotEmpty()) {
            seleccionPlanEstudio = listaPlanesEstudio.first()
            val planSeleccionado =
                (planEstudioState as? ResourceUiState.Success<List<ResponsePlanEstudio>>)
                    ?.data?.firstOrNull()
                    ?.ListPlanEstudio
                    ?.firstOrNull { it.pest_det_nombre == seleccionPlanEstudio }

            planSeleccionado?.let {
                viewModel.setValoresPlanRequest(it.id_estud_pe, it.id_pest_det, it.id_serv, 1)
                viewModel.setTablaPlanRequest(it.id_estud_pe, it.id_pest_det, it.id_serv, 1, idEstud)
                viewModel.setGenerarPdfRequest(
                    id_estud_pe = it.id_estud_pe,
                    id_pest_det = it.id_pest_det,
                    id_serv = it.id_serv,
                    id_uneg = 1,
                    id_estud = idEstud
                )
            }
        }
    }

    Scaffold(
        topBar = {
            StandardTopBar(
                title = "MALLA CURRICULAR",
                subtitle = "",
                onBackClick = { navigator.popBackStack() }
            )
        },
        containerColor = colors.backGroundColor
    ) { paddingValues ->

        Column(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 16.dp, vertical = 8.dp)
        ) {
            if (listaCarreras.isNotEmpty()) {
                MyComboBoxComponentModel2(
                    items = listaCarreras,
                    label = "Seleccioná una carrera",
                    initialSelection = seleccionCarrera,
                    onItemSelected = { nuevaSeleccion ->
                        seleccionCarrera = nuevaSeleccion
                        val carreraSeleccionada =
                            (carreraUiState as? ResourceUiState.Success<List<ResponseCarreraRemote>>)
                                ?.data?.firstOrNull()
                                ?.carrera?.firstOrNull { it.serv_nombre == nuevaSeleccion }

                        carreraSeleccionada?.let {
                            viewModel.setPlanEstudioRequest(it.id_estud_serv.toInt())
                        }
                    }
                )
            }

            Spacer(Modifier.height(12.dp))

            if (listaPlanesEstudio.isNotEmpty()) {
                MyComboBoxComponentModel2(
                    items = listaPlanesEstudio,
                    label = "Seleccioná un plan de estudio",
                    initialSelection = seleccionPlanEstudio,
                    onItemSelected = { nuevaSeleccion ->
                        val planSeleccionado =
                            (planEstudioState as? ResourceUiState.Success<List<ResponsePlanEstudio>>)
                                ?.data?.firstOrNull()
                                ?.ListPlanEstudio
                                ?.firstOrNull { it.pest_det_nombre == nuevaSeleccion }

                        planSeleccionado?.let {
                            viewModel.setValoresPlanRequest(it.id_estud_pe, it.id_pest_det, it.id_serv, 2)
                            viewModel.setTablaPlanRequest(it.id_estud_pe, it.id_pest_det, it.id_serv, 2, idEstud)
                            viewModel.setGenerarPdfRequest(
                                id_estud_pe = it.id_estud_pe,
                                id_pest_det = it.id_pest_det,
                                id_serv = it.id_serv,
                                id_uneg = 1,
                                id_estud = idEstud
                            )
                        }
                    }
                )
            }

            Spacer(Modifier.height(16.dp))

            val valores = (valoresPlanState as? ResourceUiState.Success<List<ResponseValoresPlan>>)
                ?.data?.firstOrNull()?.ListValoresPlanEstudio?.firstOrNull()

            if (valores != null) {
                val obligatorios = if (valores.cant_cred_obligatorios > 0)
                    "Obligatorios: ${valores.cant_cred_obligatorios}" else ""
                val electivos = "Electivos: ${valores.cant_cred_electivos}"
                val textoCreditos = buildString {
                    append("Total Créditos")
                    if (obligatorios.isNotBlank()) append("\n$obligatorios")
                    append("\n$electivos")
                }
                val cards = listOf(
                    MallaResumen("Cursadas", valores.cant_cursados.toString(), Color(0xFF5A5B9F), Icons.Default.Book),
                    MallaResumen("Aprobadas\n(${valores.cantidad_creditos_aprob} créditos)", valores.cant_aprobados.toString(), Color(0xFF5BA57C), Icons.Default.MilitaryTech),
                    MallaResumen("Desaprobadas", valores.cant_desaprobados.toString(), Color(0xFFD9534F), Icons.Default.HighlightOff),
                    MallaResumen(textoCreditos, valores.cant_total_creditos.toString(), Color(0xFF7BAFE1), Icons.Default.CheckCircle),
                    MallaResumen("Total Asig. Malla", valores.cant_total.toString(), Color(0xFFE5C84D), Icons.Default.Star)
                )

                cards.chunked(2).forEach { fila ->
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        fila.forEach { card ->
                            Box(modifier = Modifier.weight(1f)) {
                                MallaResumenCell(resumen = card)
                            }
                        }
                        if (fila.size == 1) Spacer(modifier = Modifier.weight(1f))
                    }
                    Spacer(Modifier.height(12.dp))
                }
            }

            val tabs = listOf("Tabla Malla Curricular", "Gráfica Malla Curricular")
            var selectedTabIndex by rememberSaveable { mutableStateOf(0) }

            TabRow(selectedTabIndex = selectedTabIndex, containerColor = colors.backGroundColor) {
                tabs.forEachIndexed { index, title ->
                    Tab(
                        selected = selectedTabIndex == index,
                        onClick = { selectedTabIndex = index },
                        text = { Text(title, color = colors.textColor) }
                    )
                }
            }

            Spacer(Modifier.height(12.dp))

            when (selectedTabIndex) {
                0 -> TablaMallaCurricularContent(
                    tablaPlanState = tablaPlanState,
                    cicloExpandido = cicloExpandido,
                    onExpandToggle = { nuevo ->
                        cicloExpandido = if (cicloExpandido == nuevo) null else nuevo
                    }
                )
                1 -> GraficaMallaCurricularCell(
                    grupos = grupos,
                    coloresAsignaturas = coloresAsignaturas
                )
            }
        }
    }
}

@Composable
fun TablaMallaCurricularContent(
    tablaPlanState: ResourceUiState<List<ResponseTablaPlan>>,
    cicloExpandido: Int?,
    onExpandToggle: (Int) -> Unit
) {
    val colors = getColorsTheme()
    Spacer(modifier = Modifier.height(24.dp))

    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = androidx.compose.ui.Alignment.CenterVertically
    ) {
        Text(
            text = "Tabla Malla Curricular",
            style = MaterialTheme.typography.titleMedium,
            color = colors.textColor,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.weight(1f)
        )

        Button(
            onClick = { },
            colors = ButtonDefaults.buttonColors(containerColor = colors.colorMixPrimary),
            shape = RoundedCornerShape(8.dp),
            contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp),
            modifier = Modifier.height(40.dp)
        ) {
            Icon(
                imageVector = Icons.Default.PictureAsPdf,
                contentDescription = "Exportar PDF",
                tint = Color.White,
                modifier = Modifier.size(18.dp)
            )
            Spacer(modifier = Modifier.width(6.dp))
            Text(text = "Exportar a PDF", color = Color.White, fontSize = 13.sp)
        }
    }

    Spacer(modifier = Modifier.height(8.dp))

    TablaPlanEstudioExpandableCell(
        state = tablaPlanState,
        cicloExpandido = cicloExpandido,
        onExpandToggle = onExpandToggle
    )
}

fun obtenerCodigoDelNombre(nombre: String): String {
    return nombre.substringBefore(" ").trim()
}
