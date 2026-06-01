package pe.lecordonbleu.universidadestudiante.presentation.screens.cuentacorriente

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
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
import kotlinx.serialization.json.Json
import pe.lecordonbleu.universidadestudiante.core.config.Constantes
import pe.lecordonbleu.universidadestudiante.data.remote.dto.DataPerfilList
import pe.lecordonbleu.universidadestudiante.data.remote.dto.ListCampania
import pe.lecordonbleu.universidadestudiante.data.remote.dto.ListCuentaCorriente
import pe.lecordonbleu.universidadestudiante.data.remote.dto.ListPeriodoCorriente
import pe.lecordonbleu.universidadestudiante.data.remote.dto.ListServicioCorriente
import pe.lecordonbleu.universidadestudiante.data.remote.dto.ListaDeudaCuentaCorriente
import pe.lecordonbleu.universidadestudiante.domain.model.PagoDetalleTemporal
import pe.lecordonbleu.universidadestudiante.domain.model.PagoNameValuePairs
import pe.lecordonbleu.universidadestudiante.domain.model.PagosNameValuePairs
import pe.lecordonbleu.universidadestudiante.domain.model.PagosTopLevel
import pe.lecordonbleu.universidadestudiante.domain.model.TemporalBody
import pe.lecordonbleu.universidadestudiante.domain.model.TemporalCuentaCorrienteRequest
import pe.lecordonbleu.universidadestudiante.domain.model.namePairs
import pe.lecordonbleu.universidadestudiante.getColorsTheme
import pe.lecordonbleu.universidadestudiante.getPlatformContext
import pe.lecordonbleu.universidadestudiante.getSettingsStorage
import pe.lecordonbleu.universidadestudiante.getTodayLocalDateTime
import pe.lecordonbleu.universidadestudiante.presentation.components.AppDropdownMenu
import pe.lecordonbleu.universidadestudiante.presentation.components.StandardTopBar
import pe.lecordonbleu.universidadestudiante.presentation.components.dialogs.CustomDialogBasic
import pe.lecordonbleu.universidadestudiante.presentation.components.dialogs.CustomDialogConfirmCampania
import pe.lecordonbleu.universidadestudiante.presentation.components.dialogs.CustomDialogResultCampania
import pe.lecordonbleu.universidadestudiante.presentation.components.dialogs.CustomDialogConfirmPago
import pe.lecordonbleu.universidadestudiante.presentation.components.dialogs.CustomDialogDeudas
import pe.lecordonbleu.universidadestudiante.presentation.screens.cuentacorriente.customcell.CuotaCard
import pe.lecordonbleu.universidadestudiante.presentation.vo.ResourceUiState
import pe.lecordonbleu.universidadestudiante.util.Base64Encoder
import pe.lecordonbleu.universidadestudiante.util.CountryCodes
import pe.lecordonbleu.universidadestudiante.util.openPdfFromBase64
import pe.lecordonbleu.universidadestudiante.util.openUrl

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun CuentaCorrienteScreen(
    viewModel: CuentaCorrienteViewModel,
    navigator: NavController
) {
    // ─── 1. Variables y estados ───────────────────────────────────────────────
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()
    val colors = getColorsTheme()
    val context = getPlatformContext()
    val settings = getSettingsStorage()
    val idEstud = settings.getInt("idEstud", 0)
    val idUsuario = settings.getInt("idUsuario", 0)
    val idUneg = Constantes.ID_UNEG

    val uiStateServicio by viewModel.uiStateServicio.collectAsStateWithLifecycle()
    val uiStatePeriodo by viewModel.uiStatePeriodo.collectAsStateWithLifecycle()
    val uiStateListar by viewModel.uiStateListar.collectAsStateWithLifecycle()
    val uiStateDeudas by viewModel.uiStateDeudas.collectAsStateWithLifecycle()
    val uiStateTemporal by viewModel.uiStateTemporal.collectAsStateWithLifecycle()
    val uiStateVerificarComprobante by viewModel.uiStateVerificarComprobante.collectAsStateWithLifecycle()
    val uiStateComprobantePecano by viewModel.uiStateComprobantePecano.collectAsStateWithLifecycle()
    val uiStateListarCampania by viewModel.uiStateListarCampania.collectAsStateWithLifecycle()
    val uiStateSolicitarCampania by viewModel.uiStateSolicitarCampania.collectAsStateWithLifecycle()
    val uiStateTextosHtml by viewModel.uiStateTextosHtml.collectAsStateWithLifecycle()
    val detalleMap by viewModel.detalleMap.collectAsStateWithLifecycle()

    var servicioList by remember { mutableStateOf<List<ListServicioCorriente>>(emptyList()) }
    var periodoList by remember { mutableStateOf<List<ListPeriodoCorriente>>(emptyList()) }
    var selectedServicio by remember { mutableStateOf<ListServicioCorriente?>(null) }
    var selectedPeriodo by remember { mutableStateOf<ListPeriodoCorriente?>(null) }
    var flagList by remember { mutableStateOf<List<CuentaCorrienteFlag>>(emptyList()) }
    var expandedIndex by remember { mutableStateOf<Int?>(null) }
    var showLoading by remember { mutableStateOf(true) }
    var isRefreshing by remember { mutableStateOf(false) }
    var showDeudasDialog by remember { mutableStateOf(false) }
    var listaDeudasDialog by remember { mutableStateOf<List<ListaDeudaCuentaCorriente>>(emptyList()) }
    var showConfirmDialog by remember { mutableStateOf(false) }
    var temporalRequest by remember { mutableStateOf<TemporalCuentaCorrienteRequest?>(null) }
    var showErrorDialog by remember { mutableStateOf(false) }
    var mensajeErrorDialog by remember { mutableStateOf("") }
    var estadoDeuda by remember { mutableStateOf(false) }
    var perfilData by remember { mutableStateOf<DataPerfilList?>(null) }
    var firstUnpaidIndex by remember { mutableStateOf(-1) }

    var isFilterExpanded by remember { mutableStateOf(false) }
    var filterEstados by remember { mutableStateOf<Set<String>>(emptySet()) }
    var filterConMora by remember { mutableStateOf(false) }
    var filterConDescuento by remember { mutableStateOf(false) }

    val activeFilterCount = filterEstados.size + (if (filterConMora) 1 else 0) + (if (filterConDescuento) 1 else 0)
    val filteredFlagList = remember(flagList, filterEstados, filterConMora, filterConDescuento) {
        flagList.mapIndexed { idx, item -> Pair(idx, item) }.filter { (_, cuota) ->
            val estadoOk = filterEstados.isEmpty() || filterEstados.contains(cuota.estado_nombre.trim().uppercase())
            val moraOk = !filterConMora || (cuota.monto_mora.toDoubleOrNull() ?: 0.0) > 0.0
            val descuentoOk = !filterConDescuento || (cuota.monto_descuento.toDoubleOrNull() ?: 0.0) > 0.0
            estadoOk && moraOk && descuentoOk
        }
    }

    val monto = remember(flagList) {
        formatDecimal(flagList.filter { it.isChecked }
            .sumOf { it.monto_pendiente.toDoubleOrNull() ?: 0.0 })
    }

    val countryHelper = remember { CountryCodes() }
    val countryNames = remember { countryHelper.getCountryNames() }
    var selectedCountryName by remember { mutableStateOf("Peru") }
    var senderCountry by remember { mutableStateOf("PE") }

    var flag_verano by remember { mutableStateOf(0) }
    var idCampDesc by remember { mutableStateOf(0) }
    var id_estud_serv by remember { mutableStateOf(0) }
    var id_peracad by remember { mutableStateOf(0) }

    var servicioConsumido by remember { mutableStateOf(false) }
    var periodoConsumido by remember { mutableStateOf(false) }
    var campanisList by remember { mutableStateOf<List<ListCampania>>(emptyList()) }
    var selectedCampania by remember { mutableStateOf<ListCampania?>(null) }
    var flagCampanya by remember { mutableStateOf(0) }
    var showConfirmCampaniaDialog by remember { mutableStateOf(false) }
    var campaniaResultDialog by remember { mutableStateOf(false) }
    var campaniaResultMsg by remember { mutableStateOf("") }
    var campaniaResultTitulo by remember { mutableStateOf("") }
    var campaniaResultIsError by remember { mutableStateOf(false) }
    var idOacadArranque by remember { mutableStateOf(0) }
    var idOperCampania by remember { mutableStateOf(0) }
    var idEstudPeCampania by remember { mutableStateOf(0) }

    LaunchedEffect(Unit) {
        viewModel.setServicioCuentaCorriente(idEstud, idUneg)
        viewModel.setHtmlRequest(idUneg, idUsuario)
    }

    // ─── 2. UI ────────────────────────────────────────────────────────────────
    Box(modifier = Modifier.fillMaxSize()) {
        Scaffold(
            modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
            topBar = {
                StandardTopBar(
                    title = "CUENTA CORRIENTE",
                    subtitle = "Cuenta Corriente",
                    onBackClick = { navigator.popBackStack() },
                    scrollBehavior = scrollBehavior
                )
            },
            bottomBar = {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .navigationBarsPadding()
                        .padding(horizontal = 16.dp, vertical = 8.dp),
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    val haySeleccionados = flagList.any { it.isChecked }
                    val btnEnabled = haySeleccionados && monto != "0.00" && !estadoDeuda
                    Button(
                        onClick = {
                            if (estadoDeuda) {
                                showDeudasDialog = true
                            } else {
                                if (monto != "0.00") {
                                    val perfil = perfilData
                                    if (perfil != null) {
                                        val selectedItems = flagList.filter { it.isChecked }
                                        var amountTotal = 0.0
                                        var idOper = ""
                                        var nuevosCampos = ""
                                        var strOperCuota = ""
                                        selectedItems.forEach { cuenta ->
                                            amountTotal += cuenta.monto_pendiente.toDoubleOrNull()
                                                ?: 0.0
                                            idOper = cuenta.id_oper.toString()
                                            var montoSinDecimales = cuenta.monto_pendiente
                                            if (montoSinDecimales.endsWith(".0") || montoSinDecimales.endsWith(
                                                    ".00"
                                                )
                                            ) {
                                                montoSinDecimales =
                                                    montoSinDecimales.substringBefore(".")
                                            }
                                            nuevosCampos =
                                                "#${cuenta.id_estud_pe}#${cuenta.id_estud_serv}#${cuenta.id_tiposerva}#${cuenta.id_oper_cuota}"
                                            if (strOperCuota.isNotEmpty()) strOperCuota += "-"
                                            strOperCuota += "${cuenta.id_oper}V${cuenta.id_oper_cuota}V${cuenta.id_oper_cuota_det}V${montoSinDecimales}V${cuenta.id_pago}"
                                        }
                                        val amountStr = formatDecimal(amountTotal).replace(".", "")
                                        var concatenado =
                                            "$idUsuario#$idUneg#2#$idOper#$senderCountry"
                                        concatenado =
                                            "$concatenado$nuevosCampos#$strOperCuota#$flag_verano#$idCampDesc"
                                        val callbaid =
                                            Base64Encoder.encodeToBase64(concatenado).trim()

                                        temporalRequest = sendToDB(
                                            perfil = perfil,
                                            callbaid = callbaid,
                                            amountStr = amountStr,
                                            idUneg = idUneg.toString(),
                                            senderCountry = senderCountry
                                        )
                                        showConfirmDialog = true
                                    }
                                }
                            }
                        },
                        enabled = btnEnabled,
                        modifier = Modifier.fillMaxWidth().height(48.dp),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = colors.colorMixPrimary)
                    ) {
                        Text(
                            text = "PAGAR S./ $monto",
                            fontWeight = FontWeight.Bold,
                            fontSize = 15.sp
                        )
                    }
                }
            }
        ) { innerPadding ->
            PullToRefreshBox(
                isRefreshing = isRefreshing,
                onRefresh = {
                    isRefreshing = true
                    servicioConsumido = false
                    periodoConsumido = false
                    viewModel.setServicioCuentaCorriente(idEstud, idUneg)
                    viewModel.setHtmlRequest(idUneg, idUsuario)
                },
                modifier = Modifier.padding(innerPadding)
            ) {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 12.dp),
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    item {
                        Spacer(modifier = Modifier.height(8.dp))
                        Surface(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 4.dp),
                            color = colors.colorExpenseItem,
                            shape = MaterialTheme.shapes.medium,
                            border = BorderStroke(1.dp, colors.textColor.copy(alpha = 0.05f))
                        ) {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp),
                                verticalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                AppDropdownMenu(
                                    items = servicioList,
                                    selectedItem = selectedServicio,
                                    onItemSelected = { servicio ->
                                        if (selectedServicio?.id_estud_serv != servicio.id_estud_serv) {
                                            selectedServicio = servicio
                                            selectedPeriodo = null
                                            periodoConsumido = false
                                            filterEstados = emptySet()
                                            filterConMora = false
                                            filterConDescuento = false
                                            viewModel.setPeriodoCuentaCorriente(servicio.id_estud_serv)
                                        }
                                    },
                                    itemLabel = { it.nombre_servicio },
                                    label = "Servicio",
                                    enabled = servicioList.isNotEmpty()
                                )
                                AppDropdownMenu(
                                    items = periodoList,
                                    selectedItem = selectedPeriodo,
                                    onItemSelected = { periodo ->
                                        if (selectedPeriodo?.id_oper != periodo.id_oper) {
                                            selectedPeriodo = periodo
                                            expandedIndex = null
                                            flag_verano = periodo.flag_verano
                                            idCampDesc = periodo.id_camp_desc
                                            id_estud_serv = periodo.id_estud_serv
                                            id_peracad = periodo.id_peracad
                                            flagList = emptyList()
                                            estadoDeuda = false
                                            flagCampanya = periodo.flag_campanya
                                            idOacadArranque = periodo.id_oacad_arranque
                                            idOperCampania = periodo.id_oper
                                            idEstudPeCampania = periodo.id_estud_pe
                                            campanisList = emptyList()
                                            selectedCampania = null
                                            filterEstados = emptySet()
                                            filterConMora = false
                                            filterConDescuento = false
                                            if (periodo.flag_campanya == 0) {
                                                viewModel.setListarCampania(periodo.id_oacad_arranque)
                                            }
                                            viewModel.setListarCuentaCorriente(
                                                periodo.id_estud_pe,
                                                periodo.id_oper
                                            )
                                            viewModel.setDeudasCuentasCorrientes(
                                                0,
                                                periodo.id_estud_serv,
                                                0,
                                                periodo.id_peracad
                                            )
                                        }
                                    },
                                    itemLabel = { it.nombre_periodo_academico },
                                    label = "Periodo",
                                    enabled = periodoList.isNotEmpty()
                                )
                                AppDropdownMenu(
                                    items = countryNames,
                                    selectedItem = selectedCountryName,
                                    onItemSelected = { name ->
                                        selectedCountryName = name
                                        senderCountry = countryHelper.getCode(name) ?: "PE"
                                    },
                                    itemLabel = { it },
                                    label = "Pais de residencia",
                                    enabled = true
                                )
                                if (flagCampanya == 0 && campanisList.isNotEmpty()) {
                                    AppDropdownMenu(
                                        items = campanisList,
                                        selectedItem = selectedCampania,
                                        onItemSelected = { selectedCampania = it },
                                        itemLabel = { it.camp_desc_nombre },
                                        label = "Campaña de descuento",
                                        enabled = true
                                    )
                                }
                                if (flagCampanya == 0 && campanisList.isNotEmpty()) {
                                    Button(
                                        onClick = { showConfirmCampaniaDialog = true },
                                        modifier = Modifier.fillMaxWidth().height(44.dp),
                                        shape = RoundedCornerShape(12.dp),
                                        colors = ButtonDefaults.buttonColors(containerColor = colors.colorVerdeMedio),
                                        enabled = selectedCampania != null
                                    ) {
                                        Text(
                                            text = "SOLICITAR CAMPAÑA",
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 14.sp
                                        )
                                    }
                                }
                            }
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                    }

                    if (flagList.isNotEmpty()) {
                        item {
                            Surface(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 4.dp),
                                color = colors.colorExpenseItem,
                                shape = MaterialTheme.shapes.medium,
                                border = BorderStroke(1.dp, colors.textColor.copy(alpha = 0.05f))
                            ) {
                                Column(modifier = Modifier.fillMaxWidth()) {
                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .clickable { isFilterExpanded = !isFilterExpanded }
                                            .padding(horizontal = 16.dp, vertical = 12.dp),
                                        horizontalArrangement = Arrangement.Center,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.FilterList,
                                            contentDescription = null,
                                            tint = colors.colorMixPrimary,
                                            modifier = Modifier.size(16.dp)
                                        )
                                        Spacer(modifier = Modifier.size(6.dp))
                                        Text(
                                            text = if (isFilterExpanded) "OCULTAR FILTROS" else "MOSTRAR FILTROS",
                                            fontSize = 11.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = colors.colorMixPrimary,
                                            letterSpacing = 1.sp
                                        )
                                        if (activeFilterCount > 0) {
                                            Spacer(modifier = Modifier.size(6.dp))
                                            Surface(
                                                shape = RoundedCornerShape(50),
                                                color = colors.colorMixPrimary
                                            ) {
                                                Text(
                                                    text = "$activeFilterCount",
                                                    style = MaterialTheme.typography.labelSmall,
                                                    color = colors.colorBlanco,
                                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
                                                )
                                            }
                                        }
                                        Spacer(modifier = Modifier.size(4.dp))
                                        Icon(
                                            imageVector = if (isFilterExpanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                                            contentDescription = null,
                                            tint = colors.colorMixPrimary,
                                            modifier = Modifier.size(16.dp)
                                        )
                                    }
                                    if (isFilterExpanded) {
                                        HorizontalDivider(color = colors.textColor.copy(alpha = 0.07f))
                                    }
                                    AnimatedVisibility(visible = isFilterExpanded) {
                                        Column(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .padding(start = 16.dp, end = 16.dp, bottom = 16.dp),
                                            verticalArrangement = Arrangement.spacedBy(10.dp)
                                        ) {
                                            Text(
                                                text = "Estado",
                                                style = MaterialTheme.typography.labelMedium,
                                                color = colors.textColor.copy(alpha = 0.55f)
                                            )
                                            FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                                listOf("PAGADO", "PENDIENTE", "VENCIDO", "PENDIENTE/VENCIDO").forEach { estado ->
                                                    val selected = filterEstados.contains(estado)
                                                    FilterChip(
                                                        selected = selected,
                                                        onClick = {
                                                            filterEstados = if (selected) filterEstados - estado else filterEstados + estado
                                                        },
                                                        label = {
                                                            Text(
                                                                text = estado,
                                                                style = MaterialTheme.typography.labelSmall
                                                            )
                                                        }
                                                    )
                                                }
                                            }
                                            HorizontalDivider(color = colors.textColor.copy(alpha = 0.06f))
                                            Row(
                                                modifier = Modifier.fillMaxWidth(),
                                                horizontalArrangement = Arrangement.SpaceBetween,
                                                verticalAlignment = Alignment.CenterVertically
                                            ) {
                                                Text(
                                                    text = "Con mora",
                                                    style = MaterialTheme.typography.bodySmall,
                                                    color = colors.textColor
                                                )
                                                Switch(
                                                    checked = filterConMora,
                                                    onCheckedChange = { filterConMora = it }
                                                )
                                            }
                                            Row(
                                                modifier = Modifier.fillMaxWidth(),
                                                horizontalArrangement = Arrangement.SpaceBetween,
                                                verticalAlignment = Alignment.CenterVertically
                                            ) {
                                                Text(
                                                    text = "Con descuento",
                                                    style = MaterialTheme.typography.bodySmall,
                                                    color = colors.textColor
                                                )
                                                Switch(
                                                    checked = filterConDescuento,
                                                    onCheckedChange = { filterConDescuento = it }
                                                )
                                            }
                                        }
                                    }
                                }
                            }
                            Spacer(modifier = Modifier.height(4.dp))
                        }
                    }

                    if (showLoading) {
                        item {
                            Row(
                                modifier = Modifier.fillMaxWidth().padding(24.dp),
                                horizontalArrangement = Arrangement.Center
                            ) {
                                CircularProgressIndicator(color = colors.colorMixPrimary)
                            }
                        }
                    }

                    itemsIndexed(filteredFlagList) { _, (originalIndex, cuota) ->
                        val key = Pair(cuota.id_pago, cuota.id_oper_cuota_det)
                        CuotaCard(
                            item = cuota,
                            detalles = detalleMap[key] ?: emptyList(),
                            isExpanded = expandedIndex == originalIndex,
                            colors = colors,
                            onExpandClick = {
                                expandedIndex = if (expandedIndex == originalIndex) null else {
                                    viewModel.setDetalleCuentaCorriente(
                                        cuota.id_pago,
                                        cuota.id_oper_cuota_det
                                    )
                                    originalIndex
                                }
                            },
                            onCheckboxChanged = { checked ->
                                flagList = onCheckboxChanged(flagList, originalIndex, checked, firstUnpaidIndex)
                                if ((checked || originalIndex != firstUnpaidIndex) && id_estud_serv > 0 && id_peracad > 0) {
                                    viewModel.setDeudasCuentasCorrientes(
                                        0,
                                        id_estud_serv,
                                        0,
                                        id_peracad
                                    )
                                }
                            },
                            onChildItemClick = { boleta, flagPecano, tipoDocuPecano, fechaOperacion ->
                                if (flagPecano == 1) {
                                    viewModel.setComprobantePecano(
                                        tipoDocuPecano,
                                        fechaOperacion,
                                        boleta
                                    )
                                } else {
                                    if (boleta.isNotEmpty()) {
                                        viewModel.setVerificarComprobante(boleta, idUneg)
                                    }
                                }
                            }
                        )
                    }
                    item { Spacer(modifier = Modifier.height(80.dp)) }
                }
            }

        }
    }

    // ─── 3. when(uiState) ────────────────────────────────────────────────────
    when (val s = uiStateServicio) {
        is ResourceUiState.Loading -> {
            showLoading = true
        }

        is ResourceUiState.Success -> {
            isRefreshing = false
            servicioList = s.data.ListServicioCorriente
            if (!servicioConsumido) {
                s.data.ListServicioCorriente.firstOrNull()?.let { primero ->
                    servicioConsumido = true
                    selectedServicio = primero
                    viewModel.setPeriodoCuentaCorriente(primero.id_estud_serv)
                }
            }
        }

        is ResourceUiState.Error -> {
            showLoading = false; isRefreshing = false
        }

        ResourceUiState.Empty -> {
            isRefreshing = false
        }
    }

    when (val s = uiStatePeriodo) {
        is ResourceUiState.Loading -> {
            showLoading = true
        }

        is ResourceUiState.Success -> {
            periodoList = s.data.ListPeriodoCorriente
            if (!periodoConsumido) {
                s.data.ListPeriodoCorriente.firstOrNull()?.let { primero ->
                    periodoConsumido = true
                    selectedPeriodo = primero
                    flag_verano = primero.flag_verano
                    idCampDesc = primero.id_camp_desc
                    id_estud_serv = primero.id_estud_serv
                    id_peracad = primero.id_peracad
                    flagCampanya = primero.flag_campanya
                    idOacadArranque = primero.id_oacad_arranque
                    idOperCampania = primero.id_oper
                    idEstudPeCampania = primero.id_estud_pe
                    if (primero.flag_campanya == 0) {
                        viewModel.setListarCampania(primero.id_oacad_arranque)
                    }
                    viewModel.setListarCuentaCorriente(primero.id_estud_pe, primero.id_oper)
                    viewModel.setDeudasCuentasCorrientes(
                        0,
                        primero.id_estud_serv,
                        0,
                        primero.id_peracad
                    )
                }
            }
        }

        is ResourceUiState.Error -> {
            showLoading = false
        }

        ResourceUiState.Empty -> {}
    }

    when (val s = uiStateListar) {
        is ResourceUiState.Loading -> {
            showLoading = true
        }

        is ResourceUiState.Success -> {
            showLoading = false
            val result = initializarFlags(s.data.ListCuentaCorriente)
            flagList = result.first
            firstUnpaidIndex = result.second
            viewModel.resetListarState()
        }

        is ResourceUiState.Error -> {
            showLoading = false
        }

        ResourceUiState.Empty -> {
            showLoading = false
        }
    }

    when (val s = uiStateDeudas) {
        is ResourceUiState.Success -> {
            val list = s.data.ListaDeudaCuentaCorriente
            if (list.isNotEmpty()) {
                estadoDeuda = list[0].estado == 0
                if (estadoDeuda && !showDeudasDialog) {
                    listaDeudasDialog = list
                    showDeudasDialog = true
                }
            }
        }

        else -> {}
    }

    when (val s = uiStateTemporal) {
        is ResourceUiState.Loading -> {
            showLoading = true
        }

        is ResourceUiState.Success -> {
            showLoading = false
            val cod = s.data.cod_transaccion
            if (cod.isNotEmpty()) {
                navigator.navigate("/pagoFlywire/$cod/cuentaCorriente")
                viewModel.resetTemporalState()
            } else if (!showErrorDialog) {
                mensajeErrorDialog = "Ha ocurrido un error, intente mas tarde"
                showErrorDialog = true
                viewModel.resetTemporalState()
            }
        }

        is ResourceUiState.Error -> {
            showLoading = false
            if (!showErrorDialog) {
                mensajeErrorDialog = s.message
                showErrorDialog = true
                viewModel.resetTemporalState()
            }
        }

        else -> {}
    }

    when (val s = uiStateVerificarComprobante) {
        is ResourceUiState.Loading -> {}
        is ResourceUiState.Success -> {
            openPdfFromBase64(context, s.data.resultado)
            viewModel.resetVerificarComprobanteState()
        }

        is ResourceUiState.Error -> {
            mensajeErrorDialog = "Ha ocurrido un error, intente mas tarde"
            showErrorDialog = true
            viewModel.resetVerificarComprobanteState()
        }

        else -> {}
    }

    when (val s = uiStateComprobantePecano) {
        is ResourceUiState.Loading -> {}
        is ResourceUiState.Success -> {
            if (s.data.enlacePDF.isNotEmpty()) {
                openUrl(context, s.data.enlacePDF)
            }
            viewModel.resetComprobantePecanoState()
        }

        is ResourceUiState.Error -> {
            mensajeErrorDialog = "Ha ocurrido un error, intente mas tarde"
            showErrorDialog = true
            viewModel.resetComprobantePecanoState()
        }

        else -> {}
    }

    when (val s = uiStateTextosHtml) {
        is ResourceUiState.Success -> {
            s.data.DataPerfilList.firstOrNull()?.let { perfilData = it }
        }

        else -> {}
    }

    when (val s = uiStateListarCampania) {
        is ResourceUiState.Success -> {
            campanisList = s.data.ListCampania
            if (campanisList.isNotEmpty()) selectedCampania = campanisList.first()
            viewModel.resetListarCampaniaState()
        }
        else -> {}
    }

    when (val s = uiStateSolicitarCampania) {
        is ResourceUiState.Loading -> { showLoading = true }
        is ResourceUiState.Success -> {
            showLoading = false
            campaniaResultTitulo = s.data.titulo
            campaniaResultMsg = s.data.mensaje
            campaniaResultIsError = s.data.flag_val != 1
            campaniaResultDialog = true
            viewModel.resetSolicitarCampaniaState()
        }
        is ResourceUiState.Error -> {
            showLoading = false
            campaniaResultTitulo = "CAMPAÑA DE DESCUENTO"
            campaniaResultMsg = s.message
            campaniaResultIsError = true
            campaniaResultDialog = true
            viewModel.resetSolicitarCampaniaState()
        }
        else -> {}
    }

    if (showConfirmCampaniaDialog) {
        CustomDialogConfirmCampania(
            visible = true,
            campaniaNombre = selectedCampania?.camp_desc_nombre ?: "",
            colors = colors,
            onConfirm = {
                showConfirmCampaniaDialog = false
                selectedCampania?.let { campania ->
                    viewModel.setSolicitarCampania(
                        idOper = idOperCampania,
                        idEstudPe = idEstudPeCampania,
                        idUser = idUsuario,
                        idCampDesc = campania.id_camp_desc
                    )
                }
            },
            onDismiss = { showConfirmCampaniaDialog = false }
        )
    }

    if (campaniaResultDialog) {
        CustomDialogResultCampania(
            visible = true,
            titulo = campaniaResultTitulo,
            mensaje = campaniaResultMsg,
            isError = campaniaResultIsError,
            colors = colors,
            onDismiss = {
                campaniaResultDialog = false
                if (!campaniaResultIsError) {
                    campanisList = emptyList()
                    selectedCampania = null
                    viewModel.resetListarCampaniaState()
                    viewModel.resetServicioState()
                    viewModel.resetPeriodoState()
                    servicioConsumido = false
                    periodoConsumido = false
                    viewModel.setServicioCuentaCorriente(idEstud, idUneg)
                    viewModel.setHtmlRequest(idUneg, idUsuario)
                }
            }
        )
    }

    if (showDeudasDialog) {
        CustomDialogDeudas(
            visible = true,
            deudas = listaDeudasDialog,
            onDismiss = {
                showDeudasDialog = false
                viewModel.resetDeudasState()
            }
        )
    }

    if (showConfirmDialog) {
        CustomDialogConfirmPago(
            visible = true,
            monto = monto,
            onConfirm = {
                showConfirmDialog = false
                showLoading = true
                temporalRequest?.let { viewModel.setTemporalCuentaCorriente(it) }
                temporalRequest = null
            },
            onDismiss = { showConfirmDialog = false }
        )
    }

    if (showErrorDialog) {
        CustomDialogBasic(
            visible = true,
            titulo = "CUENTA CORRIENTE",
            mensaje = mensajeErrorDialog,
            flag_val = 0,
            confirmado = false,
            onDismiss = { showErrorDialog = false }
        )
    }
}


private fun initializarFlags(list: List<ListCuentaCorriente>): Pair<List<CuentaCorrienteFlag>, Int> {
    val flags = list.map { convertirACuentaCorrienteFlag(it) }
    val firstUnpaid = flags.indexOfFirst { it.estado != 1 }
    val withEnabled =
        flags.mapIndexed { index, item -> item.copy(isEnabled = index == firstUnpaid) }
    val final = if (firstUnpaid == 0) {
        val afterMarcado = withEnabled.map { item ->
            if (item.nro_cuota == 1) item.copy(isChecked = true) else item
        }
        afterMarcado.map { item -> if (item.nro_cuota == 2) item.copy(isEnabled = true) else item }
    } else {
        withEnabled
    }
    return Pair(final, firstUnpaid)
}

private fun onCheckboxChanged(
    flags: List<CuentaCorrienteFlag>,
    position: Int,
    isChecked: Boolean,
    firstUnpaidIndex: Int
): List<CuentaCorrienteFlag> {
    return if (isChecked) {
        val nroCuota = flags[position].nro_cuota
        flags.mapIndexed { idx, item ->
            when {
                idx == position -> item.copy(isChecked = true)
                item.nro_cuota == nroCuota -> item.copy(isChecked = true)
                item.nro_cuota == nroCuota + 1 -> item.copy(isEnabled = true)
                else -> item
            }
        }
    } else {
        if (position == firstUnpaidIndex) {
            flags.mapIndexed { idx, item ->
                if (idx > position) item.copy(isEnabled = false, isChecked = false) else item.copy(
                    isChecked = false
                )
            }
        } else {
            val nroCuota = flags[position].nro_cuota
            flags.mapIndexed { idx, item ->
                when {
                    idx == position -> item.copy(isChecked = false)
                    item.nro_cuota == nroCuota -> item.copy(isChecked = false)
                    item.nro_cuota >= nroCuota + 1 -> item.copy(
                        isEnabled = false,
                        isChecked = false
                    )

                    else -> item
                }
            }
        }
    }
}


private fun convertirACuentaCorrienteFlag(cuenta: ListCuentaCorriente): CuentaCorrienteFlag {
    return CuentaCorrienteFlag(
        fec_vencimiento = cuenta.fec_vencimiento, estado = cuenta.estado,
        periodo = cuenta.periodo, tipo_pago_cta = cuenta.tipo_pago_cta,
        mora_aplicar = cuenta.mora_aplicar, estado_nombre = cuenta.estado_nombre,
        id_docu_comp = cuenta.id_docu_comp, id_tiposerva = cuenta.id_tiposerva,
        monto_inicial = cuenta.monto_inicial, id_peracad = cuenta.id_peracad,
        fecha_pago = cuenta.fecha_pago, nro_cuota = cuenta.nro_cuota,
        estado_reg = cuenta.estado_reg, monto_mora = cuenta.monto_mora,
        id_oper_cuota_det = cuenta.id_oper_cuota_det, mes = cuenta.mes,
        id_uneg = cuenta.id_uneg, monto_pendiente = cuenta.monto_pendiente,
        item = cuenta.item, monto_total = cuenta.monto_total,
        forma_pago_cta = cuenta.forma_pago_cta, tari_gen_abrev = cuenta.tari_gen_abrev,
        prefactura = cuenta.prefactura, id_estud_serv = cuenta.id_estud_serv,
        monto_descuento = cuenta.monto_descuento, monto_total_pago = cuenta.monto_total_pago,
        estadi_periodo = cuenta.estadi_periodo, id_oper_cuota = cuenta.id_oper_cuota,
        estado_nav_delete = cuenta.estado_nav_delete, id_oper = cuenta.id_oper,
        id_estud_pe = cuenta.id_estud_pe, concepto_nombre = cuenta.concepto_nombre,
        nro_cuota_intranet = cuenta.nro_cuota_intranet, id_pago = cuenta.id_pago,
        isChecked = false,
        isEnabled = false
    )
}

private fun sendToDB(
    perfil: DataPerfilList,
    callbaid: String,
    amountStr: String,
    idUneg: String,
    senderCountry: String
): TemporalCuentaCorrienteRequest {
    val now = getTodayLocalDateTime()
    val fechaStr = "${now.year}" +
            "${now.monthNumber.toString().padStart(2, '0')}" +
            "${now.dayOfMonth.toString().padStart(2, '0')}" +
            "${now.hour.toString().padStart(2, '0')}" +
            "${now.minute.toString().padStart(2, '0')}" +
            "${now.second.toString().padStart(2, '0')}"
    val codTransaccion = Base64Encoder.encodeToBase64(fechaStr).trim()
    val senderLastName = "${perfil.usuario_apellido_pat} ${perfil.usuario_apellido_mat}"
    val emailAddress =
        if (perfil.correoelec_ins.isNotEmpty()) perfil.correoelec_ins else perfil.correo_personal

    val pago = PagoDetalleTemporal(
        cod_transaccion = codTransaccion,
        amount = amountStr,
        callback_id = callbaid,
        callback_url = "${Constantes.BASE_DOMAIN}.${Constantes.BASE_UNEG}.edu.pe/CuentaCorrienteServlet?accion=PagoFLYWIRE",
        displayPayerInformation = "1",
        email_address = emailAddress,
        env = Constantes.ENV_DOMAIN,
        invoice_number = "1",
        locale = "es-ES",
        program_code = "1",
        provider = "embed2.0",
        recipient = Constantes.RECIPIENT_DOMAIN,
        sender_address1 = perfil.direc_resi,
        sender_city = perfil.ubig_nombdepa,
        sender_country = senderCountry,
        sender_email = emailAddress,
        sender_first_name = perfil.ususario_nombre,
        sender_last_name = senderLastName,
        sender_middle_name = "",
        sender_phone = perfil.telefono1,
        sender_state = perfil.ubig_nombdepa,
        sender_zip_code = perfil.ubig_nombdepa,
        student_first_name = perfil.ususario_nombre,
        student_id = perfil.numero_documento,
        student_last_name = senderLastName,
        return_url = "${Constantes.RETURN_DOMAIN}.${Constantes.BASE_UNEG}.edu.pe/pages/success.jsp",
        sender_address2 = "",
        id_uneg = idUneg
    )

    val topLevel =
        TemporalBody(PagosTopLevel(PagosNameValuePairs(PagoNameValuePairs(namePairs(pago)))))
    return TemporalCuentaCorrienteRequest(Json.encodeToString(topLevel))
}

private fun formatDecimal(value: Double): String {
    val rounded = kotlin.math.round(value * 100).toLong()
    val intPart = rounded / 100L
    val decPart = (rounded % 100L).toString().padStart(2, '0')
    return "$intPart.$decPart"
}
