package pe.lecordonbleu.universidadestudiante.presentation.screens.mercadopago

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import pe.lecordonbleu.universidadestudiante.data.remote.dto.ResponseMercadoPago
import pe.lecordonbleu.universidadestudiante.domain.model.MpIdentification
import pe.lecordonbleu.universidadestudiante.domain.model.MpPayerYape
import pe.lecordonbleu.universidadestudiante.domain.model.MpPhone
import pe.lecordonbleu.universidadestudiante.domain.model.YapeRequest
import pe.lecordonbleu.universidadestudiante.domain.repository.AppRepository
import pe.lecordonbleu.universidadestudiante.presentation.vo.ResourceUiState

class YapeViewModel(private val repo: AppRepository) : ViewModel() {

    private val _uiState = MutableStateFlow<ResourceUiState<ResponseMercadoPago>>(ResourceUiState.Empty)
    val uiState = _uiState.asStateFlow()

    fun procesarYape(
        celular: String,
        otp: String,
        monto: Double,
        email: String,
        dni: String,
        callbackId: String,
        externalReference: String,
        idUneg: Int,
        description: String
    ) {
        viewModelScope.launch {
            _uiState.value = ResourceUiState.Loading
            try {
                val pkResponse = repo.getMpPublicKey(idUneg)
                if (pkResponse.publicKey.isEmpty()) {
                    //No se pudo obtener la clave pública
                    _uiState.value = ResourceUiState.Error("No se pudo iniciar el pago. Intenta nuevamente")
                    return@launch
                }

                val yapeTokenResponse = repo.tokenizarYapeMP(celular, otp, pkResponse.publicKey)
                val yapeToken = yapeTokenResponse.id.ifEmpty { yapeTokenResponse.token }
                if (yapeToken.isEmpty()) {
                    _uiState.value = ResourceUiState.Error("No se pudo procesar el pago Yape")
                    return@launch
                }

                val request = YapeRequest(
                    token = yapeToken,
                    transaction_amount = monto,
                    description = description,
                    external_reference = externalReference,
                    callback_id = callbackId,
                    id_uneg = idUneg,
                    payer = MpPayerYape(
                        email = email,
                        phone = MpPhone(number = celular),
                        identification = MpIdentification(number = dni)
                    )
                )
                val result = repo.procesarPagoYape(request)
                println("[YAPE-RESULT] status=${result.status} | status_detail=${result.status_detail} | status_detail_message=${result.status_detail_message}")
                _uiState.value = ResourceUiState.Success(result)
            } catch (e: Exception) {
                _uiState.value = ResourceUiState.Error(e.message ?: "Error al procesar pago Yape")
            }
        }
    }

    fun resetState() { _uiState.value = ResourceUiState.Empty }
}
