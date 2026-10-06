package pe.lecordonbleu.universidadestudiante

import androidx.compose.runtime.Composable
import pe.lecordonbleu.universidadestudiante.domain.model.MpPayerCosto

@Composable
expect fun MpCardForm(
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
)
