package pe.lecordonbleu.universidadestudiante.presentation.screens.tramitedocumentario

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.serialization.json.JsonObject
import pe.lecordonbleu.universidadestudiante.data.remote.dto.ResponseComprobanteTramite
import pe.lecordonbleu.universidadestudiante.data.remote.dto.ResponseCorreccionTramiteSave
import pe.lecordonbleu.universidadestudiante.data.remote.dto.EstadoTramite
import pe.lecordonbleu.universidadestudiante.data.remote.dto.ResponseTramiteDocFiltro
import pe.lecordonbleu.universidadestudiante.data.remote.dto.TipoTramite
import pe.lecordonbleu.universidadestudiante.data.remote.dto.Tramite
import pe.lecordonbleu.universidadestudiante.data.remote.dto.ResponseTramitesDocumentos
import pe.lecordonbleu.universidadestudiante.data.remote.dto.ResponseCarreraRemote
import pe.lecordonbleu.universidadestudiante.domain.model.ComprobanteTramiteRequest
import pe.lecordonbleu.universidadestudiante.domain.model.CorreccionTramiteSaveRequest
import pe.lecordonbleu.universidadestudiante.domain.model.DataCarreraRequest
import pe.lecordonbleu.universidadestudiante.domain.model.DocumentosCreadosRequest
import pe.lecordonbleu.universidadestudiante.domain.model.TramiteDocFiltroRequest
import pe.lecordonbleu.universidadestudiante.domain.repository.AppRepository
import pe.lecordonbleu.universidadestudiante.presentation.vo.ResourceUiState

class TramiteDocumentarioViewModel(private val repo: AppRepository) : ViewModel() {

    private val _uiState = MutableStateFlow<ResourceUiState<List<ResponseCarreraRemote>>>(ResourceUiState.Loading)
    private val _uiStateDocumentosCreados = MutableStateFlow<ResourceUiState<ResponseTramitesDocumentos>>(ResourceUiState.Loading)
    private val _uiStateTramiteDocFiltro = MutableStateFlow<ResourceUiState<ResponseTramiteDocFiltro>>(ResourceUiState.Loading)
    private val _uiStateVerificarComprobante = MutableStateFlow<ResourceUiState<ResponseComprobanteTramite>>(ResourceUiState.Empty)
    private val _uiStateCorreccionTramiteSave = MutableStateFlow<ResourceUiState<ResponseCorreccionTramiteSave>>(ResourceUiState.Loading)
    private val _uiStateTREFiltro = MutableStateFlow<ResourceUiState<JsonObject>>(ResourceUiState.Loading)
    private val _uiStateTRCFiltro = MutableStateFlow<ResourceUiState<JsonObject>>(ResourceUiState.Loading)

    val uiState = _uiState.asStateFlow()
    val uiStateDocumentosCreados = _uiStateDocumentosCreados.asStateFlow()
    val uiStateTramiteDocFiltro = _uiStateTramiteDocFiltro.asStateFlow()
    val uiStateVerificarComprobante = _uiStateVerificarComprobante.asStateFlow()
    val uiStateCorreccionTramiteSave = _uiStateCorreccionTramiteSave.asStateFlow()
    val uiStateTREFiltro = _uiStateTREFiltro.asStateFlow()
    val uiStateTRCFiltro = _uiStateTRCFiltro.asStateFlow()

    private lateinit var dataCarreraRequest: DataCarreraRequest
    private lateinit var tramiteDocFiltroRequest: TramiteDocFiltroRequest
    private lateinit var documentosCreadosRequest: DocumentosCreadosRequest
    private lateinit var comprobanteTramiteRequest: ComprobanteTramiteRequest
    private lateinit var correccionTramiteSaveRequest: CorreccionTramiteSaveRequest

    fun setUserCarreraRequest(idEstud: Int) {
        dataCarreraRequest = DataCarreraRequest(idEstud)
        getUserCarreraData()
    }

    fun setTramiteDocFiltroRequest(
        id_uneg: Int, id_estud: Int, tipoCombo: String, idEstado: Int,
        idTipoTramite: Int, idTramite: Int, fechaInicio: Long, fechaFin: Long,
        idTramiteEstud: Int, idTramiteDt: Int, idTipoServa: Int, id_sistema: Int,
        cantidadMultiple: Int, id_pest_det: Int, id_estud_pe: Int, id_estud_serv: Int
    ) {
        tramiteDocFiltroRequest = TramiteDocFiltroRequest(
            id_uneg, id_estud, tipoCombo, idEstado, idTipoTramite, idTramite,
            fechaInicio, fechaFin, idTramiteEstud, idTramiteDt,
            idTipoServa, id_sistema, cantidadMultiple, id_pest_det, id_estud_pe, id_estud_serv
        )
        when (tipoCombo) {
            "TRE" -> getTramiteTREFiltroData()
            "TRC" -> getTramiteTRCFiltroData()
            else  -> getTramiteDocFiltroData()
        }
    }

    fun filtrarDocumentos(
        idEstud: Int,
        idUneg: Int,
        idUsuario: Int,
        idTipoUsuario: Int,
        idTipoServa: Int,
        idSistema: Int,
        estado: EstadoTramite?,
        tipoTramite: TipoTramite?,
        tramite: Tramite?,
        fechaInicio: String,
        fechaFin: String
    ) {
        documentosCreadosRequest = DocumentosCreadosRequest(
            idTramite = tramite?.id?.toIntOrNull() ?: 0,
            id_sistema = idSistema,
            idEstado = estado?.id_paragene?.toIntOrNull() ?: 0,
            fechaInicio = fechaInicio,
            idUsuario = idUsuario,
            id_tipo_usuario = idTipoUsuario,
            idUNEG = idUneg,
            idTipoTramite = tipoTramite?.id?.toIntOrNull() ?: 0,
            condicion = 1,
            fechaFin = fechaFin,
            id_estud = idEstud,
            idTipoServa = idTipoServa
        )
        getDocumentosCreados()
    }

    fun setDocumentosCreadosRequest(
        idTramite: Int, idSistema: Int, idEstado: Int, fechaInicio: String,
        idUsuario: Int, idTipoUsuario: Int, idUNEG: Int, idTipoTramite: Int,
        condicion: Int, fechaFin: String, idEstud: Int, idTipoServa: Int
    ) {
        documentosCreadosRequest = DocumentosCreadosRequest(
            idTramite = idTramite, id_sistema = idSistema, idEstado = idEstado,
            fechaInicio = fechaInicio, idUsuario = idUsuario, id_tipo_usuario = idTipoUsuario,
            idUNEG = idUNEG, idTipoTramite = idTipoTramite, condicion = condicion,
            fechaFin = fechaFin, id_estud = idEstud, idTipoServa = idTipoServa
        )
        getDocumentosCreados()
    }

    fun setVerificarComprobante(comprobante: String, idUnidadNegocio: Int) {
        comprobanteTramiteRequest = ComprobanteTramiteRequest(comprobante, idUnidadNegocio)
        getVerificarComprobante()
    }

    fun setCorreccionTramiteSaveRequest(
        descripcion: String, id_sistema: Int, id_tramite: Int, id_usuario: Int,
        id_tipo_usuario: Int, idTramiteEstud: Int, id_uneg: Int, id_estud: Int, condicion: Int
    ) {
        correccionTramiteSaveRequest = CorreccionTramiteSaveRequest(
            descripcion, id_sistema, id_tramite, id_usuario, id_tipo_usuario,
            idTramiteEstud, id_uneg, id_estud, condicion
        )
        getCorreccionTramiteSave()
    }

    private fun getUserCarreraData() {
        viewModelScope.launch {
            try {
                val result = repo.getAsistenciaCarrera(dataCarreraRequest)
                if (result.isEmpty()) {
                    _uiState.value = ResourceUiState.Error("Carrera incorrecta")
                } else {
                    _uiState.value = ResourceUiState.Success(result)
                }
            } catch (e: Exception) {
                _uiState.value = ResourceUiState.Error(e.message ?: "Ocurrio un error")
            }
        }
    }

    private fun getTramiteDocFiltroData() {
        viewModelScope.launch {
            try {
                val result = repo.getTramiteDocFiltro(tramiteDocFiltroRequest)
                if (result.flag_val == 0) {
                    _uiStateTramiteDocFiltro.value = ResourceUiState.Error("No hay datos disponibles")
                } else {
                    _uiStateTramiteDocFiltro.value = ResourceUiState.Success(result)
                }
            } catch (e: Exception) {
                _uiStateTramiteDocFiltro.value = ResourceUiState.Error(e.message ?: "Ocurrio un error")
            }
        }
    }

    private fun getDocumentosCreados() {
        viewModelScope.launch {
            try {
                val result = repo.getDocumentosCreados(documentosCreadosRequest)
                if (result.flag_val == 0) {
                    _uiStateDocumentosCreados.value = ResourceUiState.Error("No hay documentos disponibles")
                } else {
                    _uiStateDocumentosCreados.value = ResourceUiState.Success(result)
                }
            } catch (e: Exception) {
                _uiStateDocumentosCreados.value = ResourceUiState.Error(e.message ?: "Ocurrio un error")
            }
        }
    }

    private fun getTramiteTREFiltroData() {
        viewModelScope.launch {
            try {
                val result = repo.getTramiteFiltroJson(tramiteDocFiltroRequest)
                _uiStateTREFiltro.value = ResourceUiState.Success(result)
            } catch (e: Exception) {
                _uiStateTREFiltro.value = ResourceUiState.Error("Error al obtener TRE")
            }
        }
    }

    private fun getTramiteTRCFiltroData() {
        viewModelScope.launch {
            try {
                val result = repo.getTramiteFiltroJson(tramiteDocFiltroRequest)
                _uiStateTRCFiltro.value = ResourceUiState.Success(result)
            } catch (e: Exception) {
                _uiStateTRCFiltro.value = ResourceUiState.Error("Error al obtener TRC")
            }
        }
    }

    private fun getVerificarComprobante() {
        viewModelScope.launch {
            try {
                val result = repo.getVerificarComprobanteTramite(comprobanteTramiteRequest)
                if (result.resultado.isNotBlank()) {
                    _uiStateVerificarComprobante.value = ResourceUiState.Success(result)
                } else {
                    _uiStateVerificarComprobante.value = ResourceUiState.Empty
                }
            } catch (e: Exception) {
                _uiStateVerificarComprobante.value = ResourceUiState.Error("Error al verificar comprobante: ${e.message}")
            }
        }
    }

    private fun getCorreccionTramiteSave() {
        viewModelScope.launch {
            try {
                val result = repo.getCorreccionTramiteSave(correccionTramiteSaveRequest)
                _uiStateCorreccionTramiteSave.value = ResourceUiState.Success(result)
            } catch (e: Exception) {
                _uiStateCorreccionTramiteSave.value = ResourceUiState.Error("Error al solicitar correccion: ${e.message}")
            }
        }
    }

    fun resetTramiteDocFiltroState() { _uiStateTramiteDocFiltro.value = ResourceUiState.Empty }
    fun resetTramiteTREComboState() { _uiStateTREFiltro.value = ResourceUiState.Empty }
    fun resetDocumentosCreadosState() { _uiStateDocumentosCreados.value = ResourceUiState.Loading }
    fun resetVerificarComprobanteState() { _uiStateVerificarComprobante.value = ResourceUiState.Empty }
    fun resetTREFiltroState() { _uiStateTREFiltro.value = ResourceUiState.Empty }
    fun resetCorreccionTramiteState() { _uiStateCorreccionTramiteSave.value = ResourceUiState.Empty }
    fun resetTramiteTRComboState() { _uiStateTRCFiltro.value = ResourceUiState.Empty }
}
