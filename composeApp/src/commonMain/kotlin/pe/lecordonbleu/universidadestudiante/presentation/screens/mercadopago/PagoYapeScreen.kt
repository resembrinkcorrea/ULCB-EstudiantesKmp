package pe.lecordonbleu.universidadestudiante.presentation.screens.mercadopago

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
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
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
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
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.navigation.NavController
import androidx.navigation.navOptions
import pe.lecordonbleu.universidadestudiante.getPlatformContext
import pe.lecordonbleu.universidadestudiante.presentation.components.FullScreenLoadingOverlay
import pe.lecordonbleu.universidadestudiante.core.theme.IlcbBlueDeep
import pe.lecordonbleu.universidadestudiante.core.theme.IlcbBlueMid
import pe.lecordonbleu.universidadestudiante.core.theme.IlcbOnBrand
import pe.lecordonbleu.universidadestudiante.getColorsTheme
import pe.lecordonbleu.universidadestudiante.initMpDevice
import pe.lecordonbleu.universidadestudiante.presentation.components.StandardTopBar
import pe.lecordonbleu.universidadestudiante.presentation.vo.ResourceUiState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PagoYapeScreen(
    viewModel: YapeViewModel,
    navigator: NavController
) {
    val colors = getColorsTheme()
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()
    val uiState by viewModel.uiState.collectAsState()
    val keyboardController = LocalSoftwareKeyboardController.current

    val session = MpPaymentSession
    val platformContext = getPlatformContext()
    val description = if (session.tipo == "APPTD") "Pago Tramite Documentario ULCB" else "Pago Cuenta Corriente ULCB"
    var email by remember { mutableStateOf("prueba@testuser.com") }
    //var email by remember { mutableStateOf(session.email) }

    LaunchedEffect(Unit) {
        initMpDevice(platformContext)
    }
    var celular by remember { mutableStateOf("") }
    var otp by remember { mutableStateOf("") }
    var errorMsg by remember { mutableStateOf("") }
    var showResultDialog by remember { mutableStateOf(false) }
    var resultAprobado by remember { mutableStateOf(false) }
    var resultMensaje by remember { mutableStateOf("") }
    var resultReferencia by remember { mutableStateOf("") }

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

    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            StandardTopBar(
                title = "Pago con Yape",
                subtitle = when (session.tipo) {
                    "APPTD" -> "Tramite Documentario"
                    else -> "Cuenta Corriente"
                },
                onBackClick = { volver() },
                scrollBehavior = scrollBehavior
            )
        }
    ) { innerPadding ->
        Box(modifier = Modifier.fillMaxSize().padding(innerPadding)) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .imePadding()
                    .padding(horizontal = 20.dp, vertical = 16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Logo y monto
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
                                        colors = listOf(Color(0xFF6B1FA8), Color(0xFF9C27B0)),
                                        start = Offset(0f, 0f),
                                        end = Offset(600f, 0f)
                                    )
                                )
                                .padding(vertical = 14.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.Center
                            ) {
                                Surface(shape = CircleShape, color = Color.White, modifier = Modifier.size(38.dp)) {
                                    coil3.compose.AsyncImage(
                                        model = "https://www.yape.com.pe/images/logo-yape_positive.png",
                                        contentDescription = "Yape",
                                        contentScale = androidx.compose.ui.layout.ContentScale.Fit,
                                        modifier = Modifier.padding(3.dp)
                                    )
                                }
                                Spacer(Modifier.width(10.dp))
                                Text("PAGAR CON YAPE", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 15.sp)
                            }
                        }
                        Spacer(Modifier.height(12.dp))
                        Text("Total a pagar", style = MaterialTheme.typography.labelSmall, color = colors.textColor.copy(alpha = 0.6f))
                        Text(
                            text = "S/ ${session.montoDisplay}",
                            fontSize = 32.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = colors.textColor
                        )
                        Spacer(Modifier.height(12.dp))
                    }
                }

                // Campo email
                OutlinedTextField(
                    value = email,
                    onValueChange = { email = it },
                    label = { Text("Correo electrónico") },
                    placeholder = { Text("ejemplo@correo.com") },
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

                // Campo celular
                OutlinedTextField(
                    value = celular,
                    onValueChange = { if (it.length <= 9) celular = it },
                    label = { Text("Celular Yape") },
                    placeholder = { Text("9 dígitos") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
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

                // Campo OTP
                OutlinedTextField(
                    value = otp,
                    onValueChange = { if (it.length <= 6) otp = it },
                    label = { Text("Código de aprobación") },
                    placeholder = { Text("6 dígitos") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
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

                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(10.dp),
                    colors = CardDefaults.cardColors(containerColor = colors.colorExpenseItem),
                    elevation = CardDefaults.cardElevation(0.dp)
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 10.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = buildAnnotatedString {
                                append("Abre la app Yape → ")
                                withStyle(SpanStyle(fontWeight = FontWeight.Bold)) {
                                    append("Aprobar Compras")
                                }
                                append(" → digita el código de 6 dígitos.")
                            },
                            style = MaterialTheme.typography.bodySmall,
                            color = colors.textColor.copy(alpha = 0.65f),
                            lineHeight = 18.sp
                        )
                    }
                }

                if (errorMsg.isNotEmpty()) {
                    Text(errorMsg, color = MaterialTheme.colorScheme.error, style = MaterialTheme.typography.bodySmall)
                }

                // Botón pagar
                Button(
                    onClick = {
                        keyboardController?.hide()
                        errorMsg = when {
                            email.isBlank() -> "El correo electrónico es requerido"
                            celular.length != 9 -> "El número Yape debe tener 9 dígitos"
                            otp.length != 6 -> "El código OTP debe tener 6 dígitos"
                            else -> ""
                        }
                        if (errorMsg.isEmpty()) {
                            viewModel.procesarYape(
                                celular = celular,
                                otp = otp,
                                monto = session.monto,
                                email = email,
                                dni = session.dni,
                                callbackId = session.callbackId,
                                externalReference = session.externalReference,
                                idUneg = session.idUneg,
                                description = description
                            )
                        }
                    },
                    enabled = uiState !is ResourceUiState.Loading,
                    modifier = Modifier.fillMaxWidth().height(50.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF6B1FA8))
                ) {
                    Text("PAGAR S./ ${session.montoDisplay}", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 15.sp)
                }
            }

            if (uiState is ResourceUiState.Loading) {
                FullScreenLoadingOverlay()
            }
        }
    }

    when (uiState) {
        is ResourceUiState.Success -> {
            val data = (uiState as ResourceUiState.Success).data
            resultAprobado = data.status == "approved"
            resultMensaje = if (resultAprobado) {
                "Tu pago fue procesado exitosamente."
            } else {
                data.status_detail_message.ifEmpty { "El pago fue rechazado. Verifica tus datos e intenta nuevamente." }
            }
            resultReferencia = data.id.toString()
            if (!showResultDialog) showResultDialog = true
        }
        is ResourceUiState.Error -> {
            resultAprobado = false
            resultMensaje = (uiState as ResourceUiState.Error).message ?: ""
            resultReferencia = ""
            if (!showResultDialog) showResultDialog = true
        }
        else -> {}
    }

    if (showResultDialog) {
        ResultadoPagoDialog(
            aprobado = resultAprobado,
            mensaje = resultMensaje,
            referencia = resultReferencia,
            onDismiss = {
                showResultDialog = false
                viewModel.resetState()
                if (resultAprobado) {
                    volver()
                    MpPaymentSession.clear()
                } else {
                    otp = ""
                }
            }
        )
    }
}

@Composable
fun ResultadoPagoDialog(aprobado: Boolean, mensaje: String, referencia: String = "", onDismiss: () -> Unit) {
    val colors = getColorsTheme()
    val colorPrimary = if (aprobado) Color(0xFF1A7A4A) else MaterialTheme.colorScheme.error
    val colorBg = if (aprobado) Color(0xFFE8F5E9) else Color(0xFFFFEBEE)

   Dialog(onDismissRequest = onDismiss) {
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
                    text = if (aprobado) "Pago aprobado" else "Pago no procesado",
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
                if (referencia.isNotEmpty()) {
                    Text(
                        text = "REF. #$referencia",
                        style = MaterialTheme.typography.labelSmall,
                        color = colors.textColor.copy(alpha = 0.5f),
                        textAlign = TextAlign.Center
                    )
                }
                Button(
                    onClick = onDismiss,
                    modifier = Modifier.fillMaxWidth().height(44.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = colorPrimary)
                ) {
                    Text("Aceptar", color = Color.White, fontWeight = FontWeight.SemiBold)
                }
            }
        }
    }
}
