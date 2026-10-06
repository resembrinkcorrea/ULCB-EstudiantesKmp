package pe.lecordonbleu.universidadestudiante

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.selection.LocalTextSelectionColors
import androidx.compose.foundation.text.selection.TextSelectionColors
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.mercadopago.sdk.android.coremethods.domain.interactor.coreMethods
import com.mercadopago.sdk.android.coremethods.domain.model.BuyerIdentification
import com.mercadopago.sdk.android.coremethods.domain.model.ResultError
import com.mercadopago.sdk.android.coremethods.domain.utils.Result
import com.mercadopago.sdk.android.coremethods.ui.components.textfield.cardnumber.CardNumberTextField
import com.mercadopago.sdk.android.coremethods.ui.components.textfield.cardnumber.CardNumberTextFieldEvent
import com.mercadopago.sdk.android.coremethods.ui.components.textfield.expirationdate.ExpirationDateFormat
import com.mercadopago.sdk.android.coremethods.ui.components.textfield.expirationdate.ExpirationDateTextField
import com.mercadopago.sdk.android.coremethods.ui.components.textfield.expirationdate.ExpirationDateTextFieldEvent
import com.mercadopago.sdk.android.coremethods.ui.components.textfield.pcitextfield.PCIFieldState
import com.mercadopago.sdk.android.coremethods.ui.components.textfield.securitycode.SecurityCodeTextField
import com.mercadopago.sdk.android.coremethods.ui.components.textfield.securitycode.SecurityCodeTextFieldEvent
import com.mercadopago.sdk.android.initializer.MercadoPagoSDK
import kotlinx.coroutines.launch
import pe.lecordonbleu.universidadestudiante.domain.model.MpPayerCosto
import kotlin.collections.firstOrNull
import kotlin.collections.isNotEmpty
import kotlin.collections.map
import kotlin.let
import kotlin.text.format
import kotlin.text.isBlank
import kotlin.text.isEmpty
import kotlin.text.isNotEmpty
import kotlin.text.orEmpty
import kotlin.toBigDecimal

private val ColorMP = Color(0xFF009EE3)

@Composable
actual fun MpCardForm(
    publicKey: String,
    sdkReady: Boolean,
    titular: String,
    onTitularChange: (String) -> Unit,
    dniNumber: String,
    dniType: String,
    cuotasContent: @Composable () -> Unit,
    montoDisplay: String,
    monto: Double,
    isLoading: Boolean,
    errorMsg: String,
    onInstallmentsReady: (paymentMethodId: String, cuotas: List<MpPayerCosto>) -> Unit,
    onBinCleared: () -> Unit,
    onTokenReady: (token: String) -> Unit,
    onError: (msg: String) -> Unit,
    colors: DarkModeColors
) {
    if (!sdkReady) {
        Box(
            modifier = Modifier.fillMaxWidth().height(200.dp),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator()
        }
        return
    }

    val scope = rememberCoroutineScope()
    var tokenizando by remember { mutableStateOf(false) }

    val cardNumberState = remember { PCIFieldState.create() }
    val expirationDateState = remember { PCIFieldState.create() }
    val securityCodeState = remember { PCIFieldState.create() }

    var cardNumberLength by remember { mutableIntStateOf(0) }
    var expirationLength by remember { mutableIntStateOf(0) }
    var cvvLength by remember { mutableIntStateOf(0) }

    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {

        // Número de tarjeta (PCI)
        Text(
            "Número de tarjeta",
            style = MaterialTheme.typography.labelSmall,
            color = colors.textColor.copy(alpha = 0.6f)
        )
        CardNumberTextField(
            state = cardNumberState,
            onEvent = { event ->
                when (event) {
                    is CardNumberTextFieldEvent.OnBinChanged -> {
                        val bin = event.cardBin.orEmpty()
                        if (bin.isNotEmpty()) {
                            scope.launch {
                                try {
                                    val coreMethods = MercadoPagoSDK.getInstance().coreMethods
                                    val result =
                                        coreMethods.getInstallments(bin, monto.toBigDecimal())
                                    when (result) {
                                        is Result.Success -> {
                                            result.data.firstOrNull()?.let { installment ->
                                                val cuotas = installment.payerCost?.map { pc ->
                                                    val n = pc.instalments ?: 1
                                                    val amt =
                                                        pc.installmentAmount?.toDouble() ?: 0.0
                                                    val total = pc.totalAmount?.toDouble() ?: 0.0
                                                    val rate = pc.instalmentsRate?.toDouble() ?: 0.0
                                                    val msg = if (n == 1)
                                                        "1 cuota de S/ ${"%.2f".format(amt)} (S/ ${
                                                            "%.2f".format(
                                                                total
                                                            )
                                                        })"
                                                    else
                                                        "$n cuotas de S/ ${"%.2f".format(amt)} (S/ ${
                                                            "%.2f".format(
                                                                total
                                                            )
                                                        })"
                                                    MpPayerCosto(
                                                        installments = n,
                                                        installment_rate = rate,
                                                        recommended_message = msg,
                                                        installment_amount = amt,
                                                        total_amount = total
                                                    )
                                                } ?: emptyList()
                                                val pmId = installment.paymentMethodId ?: ""
                                                if (cuotas.isNotEmpty()) onInstallmentsReady(
                                                    pmId,
                                                    cuotas
                                                )
                                                else onBinCleared()
                                            } ?: onBinCleared()
                                        }

                                        is Result.Error -> onBinCleared()
                                    }
                                } catch (_: Exception) {
                                    onBinCleared()
                                }
                            }
                        } else {
                            onBinCleared()
                        }
                    }

                    is CardNumberTextFieldEvent.OnLengthChanged -> cardNumberLength = event.length
                    else -> {}
                }
            },
            textStyle = MaterialTheme.typography.bodyMedium.copy(color = colors.textColor),
            decorationBox = { innerTextField ->
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .border(
                            1.dp,
                            MaterialTheme.colorScheme.outline,
                            androidx.compose.foundation.shape.RoundedCornerShape(12.dp)
                        )
                        .height(OutlinedTextFieldDefaults.MinHeight)
                        .padding(horizontal = 16.dp),
                    contentAlignment = Alignment.CenterStart
                ) {
                    if (cardNumberLength == 0) {
                        Text(
                            "1234 5678 9012 3456",
                            color = colors.textColor.copy(alpha = 0.4f),
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                    innerTextField()
                }
            },
            modifier = Modifier.fillMaxWidth()
        )

        // Cuotas (slot desde commonMain)
        cuotasContent()

        // Nombre del titular
        OutlinedTextField(
            value = titular,
            onValueChange = onTitularChange,
            label = { Text("Nombre del titular") },
            placeholder = { Text("Como figura en la tarjeta") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth(),
            shape = androidx.compose.foundation.shape.RoundedCornerShape(12.dp),
            colors = OutlinedTextFieldDefaults.colors(
                unfocusedPlaceholderColor = colors.textColor.copy(alpha = 0.4f),
                focusedPlaceholderColor = colors.textColor.copy(alpha = 0.4f),
                unfocusedLabelColor = colors.textColor.copy(alpha = 0.6f),
                focusedLabelColor = colors.textColor
            )
        )

        // Vencimiento + CVV (PCI)
        Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    "Vencimiento",
                    style = MaterialTheme.typography.labelSmall,
                    color = colors.textColor.copy(alpha = 0.6f)
                )
                Spacer(Modifier.height(4.dp))
                CompositionLocalProvider(
                    LocalTextSelectionColors provides TextSelectionColors(
                        MaterialTheme.colorScheme.primary,
                        MaterialTheme.colorScheme.tertiaryContainer
                    )
                ) {
                    ExpirationDateTextField(
                        state = expirationDateState,
                        dateFormat = ExpirationDateFormat.ShortFormat,
                        onEvent = { event ->
                            if (event is ExpirationDateTextFieldEvent.OnLengthChanged) {
                                expirationLength = event.length
                            }
                        },
                        textStyle = MaterialTheme.typography.bodyMedium.copy(color = colors.textColor),
                        decorationBox = { innerTextField ->
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .border(
                                        1.dp,
                                        MaterialTheme.colorScheme.outline,
                                        androidx.compose.foundation.shape.RoundedCornerShape(12.dp)
                                    )
                                    .height(OutlinedTextFieldDefaults.MinHeight)
                                    .padding(horizontal = 16.dp),
                                contentAlignment = Alignment.CenterStart
                            ) {
                                if (expirationLength == 0) {
                                    Text(
                                        "MM/AA",
                                        color = colors.textColor.copy(alpha = 0.4f),
                                        style = MaterialTheme.typography.bodyMedium
                                    )
                                }
                                innerTextField()
                            }
                        },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    "CVV",
                    style = MaterialTheme.typography.labelSmall,
                    color = colors.textColor.copy(alpha = 0.6f)
                )
                Spacer(Modifier.height(4.dp))
                CompositionLocalProvider(
                    LocalTextSelectionColors provides TextSelectionColors(
                        MaterialTheme.colorScheme.primary,
                        MaterialTheme.colorScheme.tertiaryContainer
                    )
                ) {
                    SecurityCodeTextField(
                        state = securityCodeState,
                        onEvent = { event ->
                            if (event is SecurityCodeTextFieldEvent.OnLengthChanged) {
                                cvvLength = event.length
                            }
                        },
                        textStyle = MaterialTheme.typography.bodyMedium.copy(color = colors.textColor),
                        decorationBox = { innerTextField ->
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .border(
                                        1.dp,
                                        MaterialTheme.colorScheme.outline,
                                        androidx.compose.foundation.shape.RoundedCornerShape(12.dp)
                                    )
                                    .height(OutlinedTextFieldDefaults.MinHeight)
                                    .padding(horizontal = 16.dp),
                                contentAlignment = Alignment.CenterStart
                            ) {
                                if (cvvLength == 0) {
                                    Text(
                                        "3-4",
                                        color = colors.textColor.copy(alpha = 0.4f),
                                        style = MaterialTheme.typography.bodyMedium
                                    )
                                }
                                innerTextField()
                            }
                        },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        }

        if (errorMsg.isNotEmpty()) {
            Text(
                errorMsg,
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodySmall
            )
        }

        Button(
            onClick = {
                if (publicKey.isEmpty()) {
                    onError("Ingresa el número de tarjeta para continuar")
                    return@Button
                }
                if (titular.isBlank()) {
                    onError("Ingresa el nombre del titular")
                    return@Button
                }
                tokenizando = true
                scope.launch {
                    try {
                        val coreMethods = MercadoPagoSDK.getInstance().coreMethods
                        val result = coreMethods.generateCardToken(
                            cardNumberState = cardNumberState,
                            expirationDateState = expirationDateState,
                            securityCodeState = securityCodeState,
                            buyerIdentification = BuyerIdentification(
                                name = titular,
                                number = dniNumber,
                                type = dniType
                            )
                        )
                        when (result) {
                            is Result.Success -> {
                                tokenizando = false
                                onTokenReady(result.data.token)
                            }

                            is Result.Error -> {
                                tokenizando = false
                                when (result.error) {
                                    is ResultError.Validation -> onError("Datos inválidos. Revisa el número de tarjeta, vencimiento o CVV.")
                                    is ResultError.Request -> onError("Error de conexión. Inténtalo de nuevo.")
                                }
                            }
                        }
                    } catch (e: Exception) {
                        tokenizando = false
                        onError("Error inesperado. Inténtalo de nuevo.")
                    }
                }
            },
            enabled = !isLoading && !tokenizando,
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp),
            shape = androidx.compose.foundation.shape.RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(containerColor = ColorMP)
        ) {
            Text("PAGAR S/ $montoDisplay", color = Color.White, fontWeight = FontWeight.Bold)
        }
    }
}
