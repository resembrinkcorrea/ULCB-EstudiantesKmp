package pe.lecordonbleu.universidadestudiante.presentation.screens.mercadopago

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.CreditCard
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.navOptions
import coil3.compose.AsyncImage
import androidx.compose.foundation.isSystemInDarkTheme
import pe.lecordonbleu.universidadestudiante.imeKeyboardPadding
import pe.lecordonbleu.universidadestudiante.core.theme.IlcbBlueDeep
import pe.lecordonbleu.universidadestudiante.core.theme.IlcbSurface
import pe.lecordonbleu.universidadestudiante.core.theme.IlcbStripeAmber
import pe.lecordonbleu.universidadestudiante.MpCardForm
import pe.lecordonbleu.universidadestudiante.initMpSdkIfNeeded
import pe.lecordonbleu.universidadestudiante.currentTimeMillis
import pe.lecordonbleu.universidadestudiante.getColorsTheme
import pe.lecordonbleu.universidadestudiante.getPlatformContext
import pe.lecordonbleu.universidadestudiante.util.openUrl
import pe.lecordonbleu.universidadestudiante.presentation.components.FullScreenLoadingOverlay
import pe.lecordonbleu.universidadestudiante.presentation.components.StandardTopBar
import pe.lecordonbleu.universidadestudiante.presentation.vo.ResourceUiState
import pe.lecordonbleu.universidadestudiante.randomAlphanumeric4

private enum class MetodoPago { TARJETA, YAPE, PAGO_EFECTIVO }

private val ColorMP = Color(0xFF009EE3)
private val ColorYape = Color(0xFF6B1FA8)
private val ColorEfectivo = Color(0xFF1A3A5C)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PagoMercadoPagoScreen(
    viewModel: MercadoPagoViewModel,
    navigator: NavController
) {
    val colors = getColorsTheme()
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()
    val uiState by viewModel.uiState.collectAsState()
    val uiStatePasarelas by viewModel.uiStatePasarelas.collectAsState()
    val mpPublicKey by viewModel.mpPublicKey.collectAsState()
    var metodosHabilitados by remember { mutableStateOf(mapOf("tarjeta" to true, "yape" to true, "pagoefectivo" to true)) }
    var sdkReady by remember { mutableStateOf(false) }
    val platformContext = getPlatformContext()

    val session = MpPaymentSession
    val description = if (session.tipo == "APPTD") "Pago Tramite Documentario ULCB" else "Pago Cuenta Corriente ULCB"

    LaunchedEffect(Unit) {
        viewModel.setPasarelasActivas(3, session.idUneg)
        viewModel.fetchPublicKey(session.idUneg)
    }

    LaunchedEffect(mpPublicKey) {
        if (mpPublicKey.isNotEmpty()) {
            initMpSdkIfNeeded(platformContext, mpPublicKey)
            sdkReady = true
        }
    }
    var selectedMethod by remember { mutableStateOf(MetodoPago.TARJETA) }

    // Tarjeta
    //var email by remember { mutableStateOf("prueba@testuser.com") }
    var email by remember { mutableStateOf(session.email) }
    var tipoDocumento by remember { mutableStateOf(TipoDocumentoMP.DNI) }
    var numeroDocumento by remember { mutableStateOf(session.dni) }
    var dropdownExpanded by remember { mutableStateOf(false) }
    var titular by remember { mutableStateOf("") }
    var errorMsg by remember { mutableStateOf("") }

    // Cuotas
    var listaCuotas by remember { mutableStateOf<List<pe.lecordonbleu.universidadestudiante.domain.model.MpPayerCosto>>(emptyList()) }
    var cuotaSeleccionada by remember { mutableStateOf<pe.lecordonbleu.universidadestudiante.domain.model.MpPayerCosto?>(null) }
    var cuotasDropdownExpanded by remember { mutableStateOf(false) }

    var paymentMethodId by remember { mutableStateOf("") }
    var showResultDialog by remember { mutableStateOf(false) }

    fun volver() {
        when (session.tipo) {
            "APPCC" -> navigator.navigate("/cuentaCorriente", navOptions {
                popUpTo("/cuentaCorriente") { inclusive = true }
            })
            "APPTD" -> navigator.navigate("/tramiteDocumentario", navOptions {
                popUpTo("/tramiteDocumentario") { inclusive = true }
            })
            else -> navigator.popBackStack()
        }
    }

    when (uiState) {
        is ResourceUiState.Success, is ResourceUiState.Error -> if (!showResultDialog) showResultDialog = true
        else -> {}
    }

    when (uiStatePasarelas) {
        is ResourceUiState.Success -> {
            metodosHabilitados = (uiStatePasarelas as ResourceUiState.Success).data.pasarelas
                .associate { it.metodo.orEmpty().lowercase() to (it.activo == 1) }
        }
        else -> {}
    }

    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            StandardTopBar(
                title = "Método de pago",
                subtitle = when (session.tipo) {
                    "APPTD" -> "Tramite Documentario"
                    else -> "Cuenta Corriente"
                },
                onBackClick = { volver() },
                scrollBehavior = scrollBehavior
            )
        }
    ) { innerPadding ->
        Box(modifier = Modifier.fillMaxSize().padding(innerPadding).imeKeyboardPadding()) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = 16.dp, vertical = 16.dp),
                verticalArrangement = Arrangement.spacedBy(0.dp)
            ) {
                // Header monto
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = colors.backGroundColor),
                    elevation = CardDefaults.cardElevation(4.dp)
                ) {
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(
                                    Brush.linearGradient(
                                        colors = listOf(IlcbBlueDeep, ColorMP),
                                        start = Offset(0f, 0f),
                                        end = Offset(600f, 0f)
                                    )
                                )
                                .padding(vertical = 14.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                AsyncImage(
                                    model = "https://mercadeo.blob.core.windows.net/logo/mercadopago_large_app.png",
                                    contentDescription = "Mercado Pago",
                                    contentScale = ContentScale.Fit,
                                    modifier = Modifier
                                        .height(35.dp)
                                        .clip(RoundedCornerShape(4.dp))
                                        .padding(horizontal = 6.dp, vertical = 2.dp)
                                )
                            }
                        }
                        Spacer(Modifier.height(10.dp))
                        Text("Total a pagar", style = MaterialTheme.typography.labelSmall, color = colors.textColor.copy(alpha = 0.6f))
                        Text(
                            text = "S/ ${session.montoDisplay}",
                            fontSize = 30.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = colors.textColor
                        )
                        Spacer(Modifier.height(12.dp))
                    }
                }

                Spacer(Modifier.height(16.dp))

                Text(
                    "Selecciona cómo deseas pagar",
                    style = MaterialTheme.typography.labelMedium,
                    color = colors.textColor.copy(alpha = 0.55f),
                    modifier = Modifier.padding(bottom = 8.dp)
                )

                // --- Tarjeta ---
                MetodoAccordion(
                    selected = selectedMethod == MetodoPago.TARJETA,
                    enabled = metodosHabilitados["tarjeta"] != false,
                    titulo = "Tarjeta de crédito / débito",
                    logoContent = {
                        val logoBg = if (isSystemInDarkTheme()) IlcbSurface else Color.White
                        Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                            TarjetaLogoMP.todos.forEach { logo ->
                                Box(
                                    modifier = Modifier
                                        .border(0.5.dp, MaterialTheme.colorScheme.outlineVariant, RoundedCornerShape(4.dp))
                                        .clip(RoundedCornerShape(4.dp))
                                        .background(logoBg)
                                        .padding(horizontal = 4.dp, vertical = 2.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    AsyncImage(
                                        model = logo.url,
                                        contentDescription = logo.descripcion,
                                        contentScale = ContentScale.Fit,
                                        modifier = Modifier.height(14.dp).width(24.dp)
                                    )
                                }
                            }
                        }
                    },
                    onSelect = { selectedMethod = MetodoPago.TARJETA },
                    colors = colors
                ) {
                    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                        OutlinedTextField(
                            value = email,
                            onValueChange = { email = it },
                            label = { Text("Correo electrónico") },
                            placeholder = { Text("correo@ejemplo.com") },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                            singleLine = true,
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(12.dp),
                            colors = OutlinedTextFieldDefaults.colors(
                                unfocusedPlaceholderColor = colors.textColor.copy(alpha = 0.4f),
                                focusedPlaceholderColor   = colors.textColor.copy(alpha = 0.4f),
                                unfocusedLabelColor       = colors.textColor.copy(alpha = 0.6f),
                                focusedLabelColor         = colors.textColor
                            )
                        )
                        Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                            ExposedDropdownMenuBox(
                                expanded = dropdownExpanded,
                                onExpandedChange = { dropdownExpanded = it },
                                modifier = Modifier.weight(1f)
                            ) {
                                OutlinedTextField(
                                    value = tipoDocumento.etiqueta,
                                    onValueChange = {},
                                    readOnly = true,
                                    label = { Text("Tipo doc.") },
                                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = dropdownExpanded) },
                                    singleLine = true,
                                    shape = RoundedCornerShape(12.dp),
                                    modifier = Modifier.menuAnchor()
                                )
                                ExposedDropdownMenu(
                                    expanded = dropdownExpanded,
                                    onDismissRequest = { dropdownExpanded = false }
                                ) {
                                    TipoDocumentoMP.todos.forEach { tipo ->
                                        DropdownMenuItem(
                                            text = { Text(tipo.etiqueta) },
                                            onClick = {
                                                tipoDocumento = tipo
                                                numeroDocumento = ""
                                                dropdownExpanded = false
                                            }
                                        )
                                    }
                                }
                            }
                            OutlinedTextField(
                                value = numeroDocumento,
                                onValueChange = {
                                    if (it.length <= tipoDocumento.maxLen) {
                                        numeroDocumento = if (tipoDocumento.soloNumeros) it.filter { c -> c.isDigit() } else it.uppercase()
                                    }
                                },
                                label = { Text("N° documento") },
                                keyboardOptions = KeyboardOptions(
                                    keyboardType = if (tipoDocumento.soloNumeros) KeyboardType.Number else KeyboardType.Ascii
                                ),
                                singleLine = true,
                                shape = RoundedCornerShape(12.dp),
                                modifier = Modifier.weight(1.4f)
                            )
                        }

                        MpCardForm(
                            publicKey = mpPublicKey,
                            sdkReady = sdkReady,
                            titular = titular,
                            onTitularChange = { titular = it.uppercase() },
                            dniNumber = numeroDocumento,
                            dniType = tipoDocumento.valor,
                            cuotasContent = {
                                if (listaCuotas.isNotEmpty()) {
                                    ExposedDropdownMenuBox(
                                        expanded = cuotasDropdownExpanded,
                                        onExpandedChange = { cuotasDropdownExpanded = it },
                                        modifier = Modifier.fillMaxWidth()
                                    ) {
                                        OutlinedTextField(
                                            value = cuotaSeleccionada?.recommended_message ?: "",
                                            onValueChange = {},
                                            readOnly = true,
                                            label = { Text("Cuotas") },
                                            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = cuotasDropdownExpanded) },
                                            singleLine = true,
                                            shape = RoundedCornerShape(12.dp),
                                            modifier = Modifier.fillMaxWidth().menuAnchor(),
                                            colors = OutlinedTextFieldDefaults.colors(
                                                unfocusedPlaceholderColor = colors.textColor.copy(alpha = 0.4f),
                                                focusedPlaceholderColor   = colors.textColor.copy(alpha = 0.4f),
                                                unfocusedLabelColor       = colors.textColor.copy(alpha = 0.6f),
                                                focusedLabelColor         = colors.textColor
                                            )
                                        )
                                        ExposedDropdownMenu(
                                            expanded = cuotasDropdownExpanded,
                                            onDismissRequest = { cuotasDropdownExpanded = false }
                                        ) {
                                            listaCuotas.forEach { costo ->
                                                DropdownMenuItem(
                                                    text = { Text(costo.recommended_message) },
                                                    onClick = {
                                                        cuotaSeleccionada = costo
                                                        cuotasDropdownExpanded = false
                                                    }
                                                )
                                            }
                                        }
                                    }
                                }
                            },
                            montoDisplay = session.montoDisplay,
                            monto = session.monto,
                            isLoading = uiState is ResourceUiState.Loading,
                            errorMsg = errorMsg,
                            onInstallmentsReady = { pmId, cuotas ->
                                paymentMethodId = pmId
                                listaCuotas = cuotas
                                if (cuotaSeleccionada == null || !cuotas.contains(cuotaSeleccionada)) {
                                    cuotaSeleccionada = cuotas.firstOrNull()
                                }
                            },
                            onBinCleared = {
                                paymentMethodId = ""
                                listaCuotas = emptyList()
                                cuotaSeleccionada = null
                            },
                            onTokenReady = { token ->
                                viewModel.procesarConToken(
                                    token = token,
                                    paymentMethodId = paymentMethodId,
                                    monto = session.monto,
                                    email = email,
                                    dni = numeroDocumento,
                                    callbackId = session.callbackId,
                                    externalReference = session.externalReference,
                                    idUneg = session.idUneg,
                                    description = description,
                                    installments = cuotaSeleccionada?.installments ?: 1
                                )
                            },
                            onError = { msg -> errorMsg = msg },
                            colors = colors
                        )
                    }
                }

                // --- Yape ---
                MetodoAccordion(
                    selected = selectedMethod == MetodoPago.YAPE,
                    enabled = metodosHabilitados["yape"] != false,
                    titulo = "Yape",
                    logoContent = {
                        AsyncImage(
                            model = "https://www.yape.com.pe/images/logo-yape_positive.png",
                            contentDescription = "Yape",
                            contentScale = ContentScale.Fit,
                            modifier = Modifier
                                .size(33.dp)
                                .border(1.5.dp, ColorYape, CircleShape)
                                .clip(CircleShape)
                                .background(Color.White)
                                .padding(3.dp)
                        )
                    },
                    onSelect = { selectedMethod = MetodoPago.YAPE },
                    colors = colors
                ) {
                    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(12.dp))
                                .background(
                                    Brush.linearGradient(
                                        colors = listOf(Color(0xFF6B1FA8), Color(0xFF9C27B0)),
                                        start = Offset(0f, 0f),
                                        end = Offset(600f, 0f)
                                    )
                                )
                                .padding(horizontal = 16.dp, vertical = 14.dp)
                        ) {
                            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    AsyncImage(
                                        model = "https://www.yape.com.pe/images/logo-yape_positive.png",
                                        contentDescription = "Yape",
                                        contentScale = ContentScale.Fit,
                                        modifier = Modifier
                                            .size(36.dp)
                                            .clip(CircleShape)
                                            .background(Color.White)
                                            .padding(4.dp)
                                    )
                                    Spacer(Modifier.width(10.dp))
                                    Column {
                                        Text("Paga con Yape", fontWeight = FontWeight.Bold, fontSize = 14.sp, color = Color.White)
                                        Text("Rápido, seguro y sin comisiones", fontSize = 11.sp, color = Color.White.copy(alpha = 0.8f))
                                    }
                                }
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceEvenly
                                ) {
                                    listOf("1. Abre Yape", "2. Aprobar Compras", "3. Código OTP").forEach { paso ->
                                        Surface(
                                            shape = RoundedCornerShape(8.dp),
                                            color = Color.White.copy(alpha = 0.15f)
                                        ) {
                                            Text(
                                                paso,
                                                fontSize = 10.sp,
                                                color = Color.White,
                                                fontWeight = FontWeight.SemiBold,
                                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                            )
                                        }
                                    }
                                }
                            }
                        }
                        Button(
                            onClick = {
                                val ts = currentTimeMillis()
                                val prefix = if (session.tipo == "APPTD") "APPTD" else "APPCC"
                                MpPaymentSession.externalReference = "$prefix-YAPE-$ts-${randomAlphanumeric4()}"
                                navigator.navigate("/pagoYape")
                            },
                            modifier = Modifier.fillMaxWidth().height(48.dp),
                            shape = RoundedCornerShape(12.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = ColorYape)
                        ) {
                            Text("CONTINUAR CON YAPE", color = Color.White, fontWeight = FontWeight.Bold)
                        }
                    }
                }

                // --- PagoEfectivo ---
                MetodoAccordion(
                    selected = selectedMethod == MetodoPago.PAGO_EFECTIVO,
                    enabled = metodosHabilitados["pagoefectivo"] != false,
                    titulo = "PagoEfectivo (Depósito bancario)",
                    logoContent = {
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(6.dp))
                                .background(ColorEfectivo)
                                .padding(horizontal = 6.dp, vertical = 3.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            AsyncImage(
                                model = "https://mercadeo.blob.core.windows.net/logo/pagoefectivo.png",
                                contentDescription = "PagoEfectivo",
                                contentScale = ContentScale.Fit,
                                modifier = Modifier.height(20.dp)
                            )
                        }
                    },
                    onSelect = { selectedMethod = MetodoPago.PAGO_EFECTIVO },
                    colors = colors
                ) {
                    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                        Text(
                            "Genera un ticket y paga en cualquier banco o agente autorizado.",
                            style = MaterialTheme.typography.bodySmall,
                            color = colors.textColor.copy(alpha = 0.65f)
                        )
                        Button(
                            onClick = {
                                val ts = currentTimeMillis()
                                val prefix = if (session.tipo == "APPTD") "APPTD" else "APPCC"
                                val ref = "$prefix-PE-$ts-${randomAlphanumeric4()}"
                                MpPaymentSession.externalReference = ref
                                viewModel.procesarPagoEfectivo(
                                    monto = session.monto,
                                    email = session.email,
                                    dni = session.dni,
                                    callbackId = session.callbackId,
                                    externalReference = ref,
                                    idUneg = session.idUneg,
                                    description = description
                                )
                            },
                            enabled = uiState !is ResourceUiState.Loading,
                            modifier = Modifier.fillMaxWidth().height(48.dp),
                            shape = RoundedCornerShape(12.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = IlcbStripeAmber)
                        ) {
                            Icon(Icons.Default.Lock, contentDescription = null, tint = Color.Black, modifier = Modifier.size(16.dp))
                            Spacer(Modifier.width(8.dp))
                            Text("Generar ticket de pago", color = Color.Black, fontWeight = FontWeight.Bold)
                        }
                    }
                }

                Spacer(Modifier.height(24.dp))
            }

            if (uiState is ResourceUiState.Loading) {
                FullScreenLoadingOverlay()
            }
        }
    }

    if (showResultDialog) {
        val data = (uiState as? ResourceUiState.Success)?.data
        val aprobado = data?.status == "approved" || data?.status == "pending"
        val esPagoEfectivo = data?.ticket_url?.isNotEmpty() == true
        val mensaje = when {
            esPagoEfectivo -> "Tu voucher está listo. Paga en cualquier banco o agente autorizado antes del vencimiento."
            uiState is ResourceUiState.Success && aprobado -> "Tu pago fue procesado exitosamente."
            uiState is ResourceUiState.Success -> {
                val msg = data?.status_detail_message ?: ""
                msg.ifEmpty { "El pago fue rechazado. Intenta nuevamente." }
            }
            uiState is ResourceUiState.Error -> (uiState as ResourceUiState.Error).message ?: ""
            else -> ""
        }
        val referencia = data?.id?.toString() ?: ""
        val ticketUrl = data?.ticket_url ?: ""

        ResultadoPagoMPDialog(
            aprobado = aprobado,
            esPagoEfectivo = esPagoEfectivo,
            mensaje = mensaje,
            referencia = referencia,
            ticketUrl = ticketUrl,
            onVerVoucher = { openUrl(platformContext, ticketUrl) },
            onDismiss = {
                showResultDialog = false
                viewModel.resetState()
                if (aprobado) {
                    volver()
                    MpPaymentSession.clear()
                }
            }
        )
    }
}

@Composable
private fun MetodoAccordion(
    selected: Boolean,
    enabled: Boolean = true,
    titulo: String,
    logoContent: @Composable () -> Unit,
    onSelect: () -> Unit,
    colors: pe.lecordonbleu.universidadestudiante.DarkModeColors,
    content: @Composable () -> Unit
) {
    val borderColor = when {
        !enabled -> colors.textColor.copy(alpha = 0.08f)
        selected  -> ColorMP
        else      -> colors.textColor.copy(alpha = 0.15f)
    }
    val bgColor = when {
        !enabled -> colors.textColor.copy(alpha = 0.04f)
        selected  -> ColorMP.copy(alpha = 0.04f)
        else      -> colors.backGroundColor
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .border(1.dp, borderColor, RoundedCornerShape(12.dp))
            .background(bgColor)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .then(if (enabled) Modifier.clickable { onSelect() } else Modifier)
                .padding(horizontal = 14.dp, vertical = 14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            RadioButton(
                selected = selected,
                onClick = if (enabled) onSelect else null,
                enabled = enabled,
                colors = RadioButtonDefaults.colors(selectedColor = ColorMP)
            )
            Spacer(Modifier.width(8.dp))
            Text(
                titulo,
                fontWeight = if (selected) FontWeight.SemiBold else FontWeight.Normal,
                fontSize = 14.sp,
                color = if (enabled) colors.textColor else colors.textColor.copy(alpha = 0.35f),
                modifier = Modifier.weight(1f)
            )
            logoContent()
        }

        AnimatedVisibility(
            visible = selected,
            enter = expandVertically(),
            exit = shrinkVertically()
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 16.dp, end = 16.dp, bottom = 16.dp)
            ) {
                content()
            }
        }
    }

    Spacer(Modifier.height(8.dp))
}

@Composable
private fun ResultadoPagoMPDialog(
    aprobado: Boolean,
    esPagoEfectivo: Boolean,
    mensaje: String,
    referencia: String = "",
    ticketUrl: String = "",
    onVerVoucher: () -> Unit = {},
    onDismiss: () -> Unit
) {
    val colors = getColorsTheme()
    val colorPrimary = if (aprobado) Color(0xFF1A7A4A) else MaterialTheme.colorScheme.error
    val colorBg = if (aprobado) Color(0xFFE8F5E9) else Color(0xFFFFEBEE)

    androidx.compose.ui.window.Dialog(onDismissRequest = onDismiss) {
        Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = colors.backGroundColor),
            elevation = CardDefaults.cardElevation(8.dp)
        ) {
            Column(
                modifier = Modifier.padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Surface(shape = CircleShape, color = colorBg, modifier = Modifier.size(56.dp)) {
                    Icon(
                        imageVector = if (aprobado) Icons.Default.Check else Icons.Default.Close,
                        contentDescription = null,
                        tint = colorPrimary,
                        modifier = Modifier.padding(12.dp)
                    )
                }
                Text(
                    text = if (esPagoEfectivo) "Voucher generado" else if (aprobado) "Pago aprobado" else "Pago no procesado",
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    color = colorPrimary
                )
                Text(
                    text = mensaje,
                    style = MaterialTheme.typography.bodySmall,
                    color = colors.textColor.copy(alpha = 0.7f),
                    textAlign = TextAlign.Center
                )
                if (referencia.isNotEmpty() && !esPagoEfectivo) {
                    Text(
                        text = "REF. #$referencia",
                        style = MaterialTheme.typography.labelSmall,
                        color = colors.textColor.copy(alpha = 0.5f),
                        textAlign = TextAlign.Center
                    )
                }
                if (esPagoEfectivo && ticketUrl.isNotEmpty()) {
                    Button(
                        onClick = onVerVoucher,
                        modifier = Modifier.fillMaxWidth().height(44.dp),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = ColorEfectivo)
                    ) {
                        Text("Ver voucher →", color = Color.White, fontWeight = FontWeight.SemiBold)
                    }
                }
                OutlinedButton(
                    onClick = onDismiss,
                    modifier = Modifier.fillMaxWidth().height(44.dp),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text("Cerrar", fontWeight = FontWeight.SemiBold)
                }
            }
        }
    }
}
