package pe.lecordonbleu.universidadestudiante.presentation.screens.cuentacorriente

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import pe.lecordonbleu.universidadestudiante.data.remote.dto.ListDetalleCuentaCorriente
import pe.lecordonbleu.universidadestudiante.data.remote.dto.ResponseDetalleCuentaCorriente
import pe.lecordonbleu.universidadestudiante.data.remote.dto.ResponseDeudasCuentasCorrientes
import pe.lecordonbleu.universidadestudiante.data.remote.dto.ResponseListarCuentaCorriente
import pe.lecordonbleu.universidadestudiante.data.remote.dto.ResponsePeriodoCuentaCorriente
import pe.lecordonbleu.universidadestudiante.data.remote.dto.ResponseServicioCuentaCorriente
import pe.lecordonbleu.universidadestudiante.data.remote.dto.ResponseTemporalCuentaCorriente
import pe.lecordonbleu.universidadestudiante.data.remote.dto.ResponseTextosHtml
import pe.lecordonbleu.universidadestudiante.data.remote.dto.ResponseVerificarComprobante
import pe.lecordonbleu.universidadestudiante.data.remote.dto.ResponseComprobantePecano
import pe.lecordonbleu.universidadestudiante.domain.model.ComprobantePecanoRequest
import pe.lecordonbleu.universidadestudiante.domain.model.DetalleCuentaCorrienteRequest
import pe.lecordonbleu.universidadestudiante.data.remote.dto.ResponseListarCampania
import pe.lecordonbleu.universidadestudiante.data.remote.dto.ResponseSolicitarCampania
import pe.lecordonbleu.universidadestudiante.domain.model.ListarCampaniaRequest
import pe.lecordonbleu.universidadestudiante.domain.model.SolicitarCampaniaRequest
import pe.lecordonbleu.universidadestudiante.domain.model.DeudasCuentasCorrientesRequest
import pe.lecordonbleu.universidadestudiante.domain.model.ListarCuentaCorrienteRequest
import pe.lecordonbleu.universidadestudiante.domain.model.PeriodoCuentaCorrienteRequest
import pe.lecordonbleu.universidadestudiante.domain.model.ServicioCuentaCorrienteRequest
import pe.lecordonbleu.universidadestudiante.domain.model.TemporalCuentaCorrienteRequest
import pe.lecordonbleu.universidadestudiante.domain.model.TextosHtmlRequest
import pe.lecordonbleu.universidadestudiante.domain.model.VerificarComprobanteRequest
import pe.lecordonbleu.universidadestudiante.domain.repository.AppRepository
import pe.lecordonbleu.universidadestudiante.presentation.vo.ResourceUiState

class CuentaCorrienteViewModel(private val repo: AppRepository) : ViewModel() {

    private val _uiStateServicio = MutableStateFlow<ResourceUiState<ResponseServicioCuentaCorriente>>(ResourceUiState.Empty)
    private val _uiStatePeriodo = MutableStateFlow<ResourceUiState<ResponsePeriodoCuentaCorriente>>(ResourceUiState.Empty)
    private val _uiStateListar = MutableStateFlow<ResourceUiState<ResponseListarCuentaCorriente>>(ResourceUiState.Empty)
    private val _uiStateDetalle = MutableStateFlow<ResourceUiState<ResponseDetalleCuentaCorriente>>(ResourceUiState.Empty)
    private val _uiStateTemporal = MutableStateFlow<ResourceUiState<ResponseTemporalCuentaCorriente>>(ResourceUiState.Empty)
    private val _uiStateDeudas = MutableStateFlow<ResourceUiState<ResponseDeudasCuentasCorrientes>>(ResourceUiState.Empty)
    private val _uiStateVerificarComprobante = MutableStateFlow<ResourceUiState<ResponseVerificarComprobante>>(ResourceUiState.Empty)
    private val _uiStateComprobantePecano = MutableStateFlow<ResourceUiState<ResponseComprobantePecano>>(ResourceUiState.Empty)
    private val _uiStateListarCampania = MutableStateFlow<ResourceUiState<ResponseListarCampania>>(ResourceUiState.Empty)
    private val _uiStateSolicitarCampania = MutableStateFlow<ResourceUiState<ResponseSolicitarCampania>>(ResourceUiState.Empty)
    private val _uiStateTextosHtml = MutableStateFlow<ResourceUiState<ResponseTextosHtml>>(ResourceUiState.Empty)
    private val _detalleMap = MutableStateFlow<Map<Pair<Int, Int>, List<ListDetalleCuentaCorriente>>>(emptyMap())

    val uiStateServicio = _uiStateServicio.asStateFlow()
    val uiStatePeriodo = _uiStatePeriodo.asStateFlow()
    val uiStateListar = _uiStateListar.asStateFlow()
    val uiStateDetalle = _uiStateDetalle.asStateFlow()
    val uiStateTemporal = _uiStateTemporal.asStateFlow()
    val uiStateDeudas = _uiStateDeudas.asStateFlow()
    val uiStateVerificarComprobante = _uiStateVerificarComprobante.asStateFlow()
    val uiStateComprobantePecano = _uiStateComprobantePecano.asStateFlow()
    val uiStateListarCampania = _uiStateListarCampania.asStateFlow()
    val uiStateSolicitarCampania = _uiStateSolicitarCampania.asStateFlow()
    val uiStateTextosHtml = _uiStateTextosHtml.asStateFlow()
    val detalleMap = _detalleMap.asStateFlow()

    private lateinit var servicioCuentaCorrienteRequest: ServicioCuentaCorrienteRequest
    private lateinit var periodoCuentaCorrienteRequest: PeriodoCuentaCorrienteRequest
    private lateinit var listaCuentaCorrienteRequest: ListarCuentaCorrienteRequest
    private lateinit var detalleCuentaCorrienteRequest: DetalleCuentaCorrienteRequest
    private lateinit var temporalCuentaCorrienteRequest: TemporalCuentaCorrienteRequest
    private lateinit var deudasCuentasCorrientesRequest: DeudasCuentasCorrientesRequest
    private lateinit var verificarComprobanteRequest: VerificarComprobanteRequest
    private lateinit var comprobantePecanoRequest: ComprobantePecanoRequest
    private lateinit var listarCampaniaRequest: ListarCampaniaRequest
    private lateinit var solicitarCampaniaRequest: SolicitarCampaniaRequest
    private lateinit var textosHtmlRequest: TextosHtmlRequest

    fun setServicioCuentaCorriente(idEstud: Int, idUneg: Int) {
        servicioCuentaCorrienteRequest = ServicioCuentaCorrienteRequest(idEstud, idUneg)
        fetchServicioCuentaCorriente()
    }

    fun setPeriodoCuentaCorriente(idEstudServ: Int) {
        periodoCuentaCorrienteRequest = PeriodoCuentaCorrienteRequest(idEstudServ)
        fetchPeriodoCuentaCorriente()
    }

    fun setListarCuentaCorriente(idEstudPe: Int, idOper: Int) {
        listaCuentaCorrienteRequest = ListarCuentaCorrienteRequest(idEstudPe, idOper)
        fetchListarCuentaCorriente()
    }

    fun setDetalleCuentaCorriente(idPago: Int, idOperCuotaDet: Int) {
        detalleCuentaCorrienteRequest = DetalleCuentaCorrienteRequest(idPago, idOperCuotaDet)
        fetchDetalleCuentaCorriente()
    }

    fun setTemporalCuentaCorriente(request: TemporalCuentaCorrienteRequest) {
        temporalCuentaCorrienteRequest = request
        fetchTemporalCuentaCorriente()
    }

    fun setDeudasCuentasCorrientes(idPlanEstudioDet: Int, idEstudServ: Int, idEstudiante: Int, idPeriodoAcad: Int) {
        deudasCuentasCorrientesRequest = DeudasCuentasCorrientesRequest(idPlanEstudioDet, idEstudServ, idEstudiante, idPeriodoAcad)
        fetchDeudasCuentasCorrientes()
    }

    fun setVerificarComprobante(comprobante: String, idUneg: Int) {
        verificarComprobanteRequest = VerificarComprobanteRequest(comprobante, idUneg)
        fetchVerificarComprobante()
    }

    fun setComprobantePecano(tipoDocuPecano: Int, fechaOperacion: String, boleta: String) {
        comprobantePecanoRequest = ComprobantePecanoRequest(tipoDocuPecano, fechaOperacion, boleta)
        fetchComprobantePecano()
    }

    fun setHtmlRequest(idUneg: Int, idUsuario: Int) {
        textosHtmlRequest = TextosHtmlRequest(idUneg, idUsuario)
        fetchHtmlRequest()
    }

    private fun fetchServicioCuentaCorriente() {
        viewModelScope.launch {
            try {
                val result = repo.getServicioCuentaCorriente(servicioCuentaCorrienteRequest)
                _uiStateServicio.value = ResourceUiState.Success(result)
            } catch (e: Exception) {
                _uiStateServicio.value = ResourceUiState.Error(e.message ?: "Error al obtener servicios")
            }
        }
    }

    private fun fetchPeriodoCuentaCorriente() {
        viewModelScope.launch {
            _uiStatePeriodo.value = ResourceUiState.Loading
            try {
                val result = repo.getPeriodoCuentaCorriente(periodoCuentaCorrienteRequest)
                _uiStatePeriodo.value = ResourceUiState.Success(result)
            } catch (e: Exception) {
                _uiStatePeriodo.value = ResourceUiState.Error(e.message ?: "Error al obtener periodos")
            }
        }
    }

    private fun fetchListarCuentaCorriente() {
        viewModelScope.launch {
            _uiStateListar.value = ResourceUiState.Loading
            try {
                val result = repo.getListarCuentaCorriente(listaCuentaCorrienteRequest)
                _uiStateListar.value = ResourceUiState.Success(result)
            } catch (e: Exception) {
                _uiStateListar.value = ResourceUiState.Error(e.message ?: "Error al obtener cuotas")
            }
        }
    }

    private fun fetchDetalleCuentaCorriente() {
        viewModelScope.launch {
            _uiStateDetalle.value = ResourceUiState.Loading
            try {
                val req = detalleCuentaCorrienteRequest
                val result = repo.getDetalleCuentaCorriente(req)
                val key = Pair(req.id_pago, req.id_oper_cuota_det)
                val current = _detalleMap.value.toMutableMap()
                current[key] = result.ListDetalleCuentaCorriente
                _detalleMap.value = current
                _uiStateDetalle.value = ResourceUiState.Success(result)
            } catch (e: Exception) {
                _uiStateDetalle.value = ResourceUiState.Error(e.message ?: "Error al obtener detalle")
            }
        }
    }

    private fun fetchTemporalCuentaCorriente() {
        viewModelScope.launch {
            _uiStateTemporal.value = ResourceUiState.Loading
            try {
                val result = repo.getTemporalCuentaCorriente(temporalCuentaCorrienteRequest)
                _uiStateTemporal.value = ResourceUiState.Success(result)
            } catch (e: Exception) {
                _uiStateTemporal.value = ResourceUiState.Error(e.message ?: "Error al iniciar pago")
            }
        }
    }

    private fun fetchDeudasCuentasCorrientes() {
        viewModelScope.launch {
            _uiStateDeudas.value = ResourceUiState.Loading
            try {
                val result = repo.getDeudasCuentasCorrientes(deudasCuentasCorrientesRequest)
                _uiStateDeudas.value = ResourceUiState.Success(result)
            } catch (e: Exception) {
                _uiStateDeudas.value = ResourceUiState.Error(e.message ?: "Error al verificar deudas")
            }
        }
    }

    private fun fetchVerificarComprobante() {
        viewModelScope.launch {
            _uiStateVerificarComprobante.value = ResourceUiState.Loading
            try {
                val result = repo.getVerificarComprobante(verificarComprobanteRequest)
                _uiStateVerificarComprobante.value = ResourceUiState.Success(result)
            } catch (e: Exception) {
                _uiStateVerificarComprobante.value = ResourceUiState.Error(e.message ?: "Error al verificar comprobante")
            }
        }
    }

    private fun fetchComprobantePecano() {
        viewModelScope.launch {
            _uiStateComprobantePecano.value = ResourceUiState.Loading
            try {
                val result = repo.getComprobantePecano(comprobantePecanoRequest)
                _uiStateComprobantePecano.value = ResourceUiState.Success(result)
            } catch (e: Exception) {
                _uiStateComprobantePecano.value = ResourceUiState.Error(e.message ?: "Error al obtener comprobante Pecano")
            }
        }
    }

    private fun fetchHtmlRequest() {
        viewModelScope.launch {
            try {
                val result = repo.getTextosHtml(textosHtmlRequest)
                _uiStateTextosHtml.value = ResourceUiState.Success(result)
            } catch (e: Exception) {
                _uiStateTextosHtml.value = ResourceUiState.Error(e.message ?: "Error al obtener perfil")
            }
        }
    }

    fun setListarCampania(idOacadArranque: Int) {
        listarCampaniaRequest = ListarCampaniaRequest(idOacadArranque)
        viewModelScope.launch {
            _uiStateListarCampania.value = ResourceUiState.Loading
            try {
                val result = repo.getListarCampania(listarCampaniaRequest)
                _uiStateListarCampania.value = ResourceUiState.Success(result)
            } catch (e: Exception) {
                _uiStateListarCampania.value = ResourceUiState.Error(e.message ?: "Error al listar campañas")
            }
        }
    }

    fun setSolicitarCampania(idOper: Int, idEstudPe: Int, idUser: Int, idCampDesc: Int) {
        solicitarCampaniaRequest = SolicitarCampaniaRequest(idOper, idEstudPe, idUser, idCampDesc)
        viewModelScope.launch {
            _uiStateSolicitarCampania.value = ResourceUiState.Loading
            try {
                val result = repo.getSolicitarCampania(solicitarCampaniaRequest)
                _uiStateSolicitarCampania.value = ResourceUiState.Success(result)
            } catch (e: Exception) {
                _uiStateSolicitarCampania.value = ResourceUiState.Error(e.message ?: "Error al solicitar campaña")
            }
        }
    }

    fun resetTemporalState() { _uiStateTemporal.value = ResourceUiState.Empty }
    fun resetVerificarComprobanteState() { _uiStateVerificarComprobante.value = ResourceUiState.Empty }
    fun resetComprobantePecanoState() { _uiStateComprobantePecano.value = ResourceUiState.Empty }
    fun resetDeudasState() { _uiStateDeudas.value = ResourceUiState.Empty }
    fun resetListarState() { _uiStateListar.value = ResourceUiState.Empty }
    fun resetListarCampaniaState() { _uiStateListarCampania.value = ResourceUiState.Empty }
    fun resetSolicitarCampaniaState() { _uiStateSolicitarCampania.value = ResourceUiState.Empty }
    fun resetServicioState() { _uiStateServicio.value = ResourceUiState.Empty }
    fun resetPeriodoState() { _uiStatePeriodo.value = ResourceUiState.Empty }
}
