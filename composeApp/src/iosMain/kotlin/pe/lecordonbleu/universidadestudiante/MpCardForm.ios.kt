@file:OptIn(ExperimentalForeignApi::class)

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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.interop.UIKitView
import cocoapods.MPCoreBridge.MPCoreBridge
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import pe.lecordonbleu.universidadestudiante.domain.model.MpPayerCosto
import platform.UIKit.UIView
import kotlin.collections.isNotEmpty
import kotlin.text.isBlank
import kotlin.text.isNotEmpty
import kotlin.text.isNullOrEmpty

@Serializable
private data class InstallmentsResult(
    val payment_method_id: String = "",
    val cuotas: List<MpPayerCosto> = emptyList()
)

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

    val bridge = remember { MPCoreBridge() }
    var tokenizando by remember { mutableStateOf(false) }
    val jsonParser = remember { Json { ignoreUnknownKeys = true } }

    LaunchedEffect(bridge) {
        bridge.setOnBinChanged { bin ->
            if (bin.isNullOrEmpty()) {
                onBinCleared()
            } else {
                bridge.fetchInstallmentsWithBin(
                    bin = bin,
                    amount = monto,
                    onSuccess = { json ->
                        val result = try {
                            jsonParser.decodeFromString<InstallmentsResult>(json ?: "")
                        } catch (_: Exception) { null }
                        if (result != null && result.cuotas.isNotEmpty())
                            onInstallmentsReady(result.payment_method_id, result.cuotas)
                        else
                            onBinCleared()
                    },
                    onError = { _ -> onBinCleared() }
                )
            }
        }
        bridge.setOnBinCleared { onBinCleared() }
    }

    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {

        Text(
            "Número de tarjeta",
            style = MaterialTheme.typography.labelSmall,
            color = colors.textColor.copy(alpha = 0.6f)
        )
        UIKitView<UIView>(
            factory = { bridge.cardNumberField() as UIView },
            modifier = Modifier
                .fillMaxWidth()
                .height(OutlinedTextFieldDefaults.MinHeight)
                .border(1.dp, MaterialTheme.colorScheme.outline, RoundedCornerShape(12.dp))
                .padding(horizontal = 16.dp)
        )

        cuotasContent()

        OutlinedTextField(
            value = titular,
            onValueChange = onTitularChange,
            label = { Text("Nombre del titular") },
            placeholder = { Text("Como figura en la tarjeta") },
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
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    "Vencimiento",
                    style = MaterialTheme.typography.labelSmall,
                    color = colors.textColor.copy(alpha = 0.6f)
                )
                Spacer(Modifier.height(4.dp))
                UIKitView<UIView>(
                    factory = { bridge.expirationField() as UIView },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(OutlinedTextFieldDefaults.MinHeight)
                        .border(1.dp, MaterialTheme.colorScheme.outline, RoundedCornerShape(12.dp))
                        .padding(horizontal = 16.dp)
                )
            }
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    "CVV",
                    style = MaterialTheme.typography.labelSmall,
                    color = colors.textColor.copy(alpha = 0.6f)
                )
                Spacer(Modifier.height(4.dp))
                UIKitView<UIView>(
                    factory = { bridge.securityCodeField() as UIView },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(OutlinedTextFieldDefaults.MinHeight)
                        .border(1.dp, MaterialTheme.colorScheme.outline, RoundedCornerShape(12.dp))
                        .padding(horizontal = 16.dp)
                )
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
                if (titular.isBlank()) {
                    onError("Ingresa el nombre del titular")
                    return@Button
                }
                tokenizando = true
                bridge.generateTokenWithCardHolderName(
                    cardHolderName = titular,
                    docTypeName = dniType,
                    docNumber = dniNumber,
                    onSuccess = { token: String? ->
                        tokenizando = false
                        onTokenReady(token ?: "")
                    },
                    onError = { msg: String? ->
                        tokenizando = false
                        onError(msg ?: "Error al generar el token")
                    }
                )
            },
            enabled = !isLoading && !tokenizando,
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp),
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(containerColor = ColorMP)
        ) {
            Text("PAGAR S/ $montoDisplay", color = Color.White, fontWeight = FontWeight.Bold)
        }
    }
}
