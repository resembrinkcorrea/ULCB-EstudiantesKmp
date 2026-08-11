package pe.lecordonbleu.universidadestudiante.presentation.screens.mercadopago

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import pe.lecordonbleu.universidadestudiante.SettingsStorage
import pe.lecordonbleu.universidadestudiante.data.remote.dto.ResponseMercadoPago
import pe.lecordonbleu.universidadestudiante.data.remote.dto.ResponsePasarelasActivas
import pe.lecordonbleu.universidadestudiante.domain.model.PasarelasActivasRequest
import pe.lecordonbleu.universidadestudiante.getSettingsStorage
import pe.lecordonbleu.universidadestudiante.domain.model.MpIdentification
import pe.lecordonbleu.universidadestudiante.domain.model.MpPayerTarjeta
import pe.lecordonbleu.universidadestudiante.domain.model.PagoEfectivoRequest
import pe.lecordonbleu.universidadestudiante.domain.model.TarjetaRequest
import pe.lecordonbleu.universidadestudiante.domain.repository.AppRepository
import pe.lecordonbleu.universidadestudiante.presentation.vo.ResourceUiState

class MercadoPagoViewModel(private val repo: AppRepository) : ViewModel() {

    private val _uiState = MutableStateFlow<ResourceUiState<ResponseMercadoPago>>(ResourceUiState.Empty)
    val uiState = _uiState.asStateFlow()

    private val _uiStatePasarelas = MutableStateFlow<ResourceUiState<ResponsePasarelasActivas>>(ResourceUiState.Empty)
    val uiStatePasarelas = _uiStatePasarelas.asStateFlow()

    private val _mpPublicKey = MutableStateFlow("")
    val mpPublicKey = _mpPublicKey.asStateFlow()

    private var cachedPublicKey: String = ""
        set(value) { field = value; _mpPublicKey.value = value }

    fun fetchPublicKey(idUneg: Int) {
        viewModelScope.launch {
            try {
                if (cachedPublicKey.isEmpty()) {
                    val pkResponse = repo.getMpPublicKey(idUneg)
                    cachedPublicKey = pkResponse.publicKey
                }
            } catch (_: Exception) {}
        }
    }

    fun procesarConToken(
        token: String,
        paymentMethodId: String,
        monto: Double,
        email: String,
        dni: String,
        callbackId: String,
        externalReference: String,
        idUneg: Int,
        description: String,
        installments: Int = 1
    ) {
        viewModelScope.launch {
            _uiState.value = ResourceUiState.Loading
            if (paymentMethodId.isEmpty()) {
                _uiState.value = ResourceUiState.Error("No se reconoció el tipo de tarjeta. Verifica el número e intenta nuevamente.")
                return@launch
            }
            try {
                val tarjetaRequest = TarjetaRequest(
                    payment_method_id = paymentMethodId,
                    token = token,
                    transaction_amount = monto,
                    installments = installments,
                    description = description,
                    external_reference = externalReference,
                    callback_id = callbackId,
                    id_uneg = idUneg,
                    payer = MpPayerTarjeta(
                        email = email,
                        identification = MpIdentification(number = dni)
                    )
                )
                val result = repo.procesarPagoTarjeta(tarjetaRequest)
                println("[MP-RESULT] status=${result.status} | detail=${result.status_detail_message}")
                _uiState.value = ResourceUiState.Success(result)
            } catch (e: Exception) {
                _uiState.value = ResourceUiState.Error(e.message ?: "Error al procesar pago")
            }
        }
    }

    fun procesarPagoEfectivo(
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
                val request = PagoEfectivoRequest(
                    transaction_amount = monto,
                    description = description,
                    external_reference = externalReference,
                    callback_id = callbackId,
                    id_uneg = idUneg,
                    payer = MpPayerTarjeta(
                        email = email,
                        identification = MpIdentification(number = dni)
                    )
                )
                val result = repo.procesarPagoEfectivo(request)
                println("[EFEC-RESULT] status=${result.status} | ticket_url=${result.ticket_url}")
                _uiState.value = ResourceUiState.Success(result)
            } catch (e: Exception) {
                _uiState.value = ResourceUiState.Error(e.message ?: "Error al procesar PagoEfectivo")
            }
        }
    }

    fun resetState() { _uiState.value = ResourceUiState.Empty }

    fun setPasarelasActivas(condicion: Int, idUneg: Int) {
        viewModelScope.launch {
            _uiStatePasarelas.value = ResourceUiState.Loading
            try {
                val settingsStorage: SettingsStorage = getSettingsStorage()
                val idSistema = settingsStorage.getInt("idSistema", 0)
                val idUsuario = settingsStorage.getInt("idUsuario", 0)
                val result = repo.getPasarelasActivas(PasarelasActivasRequest(condicion, idUneg, idSistema, idUsuario))
                _uiStatePasarelas.value = ResourceUiState.Success(result)
            } catch (e: Exception) {
                _uiStatePasarelas.value = ResourceUiState.Error(e.message ?: "Error al obtener pasarelas")
            }
        }
    }



}
