package pe.lecordonbleu.universidadestudiante.presentation.screens.tramitedocumentario

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.serialization.json.JsonObject
import pe.lecordonbleu.universidadestudiante.SettingsStorage
import pe.lecordonbleu.universidadestudiante.data.remote.dto.ResponseCarreraRemote
import pe.lecordonbleu.universidadestudiante.data.remote.dto.ResponseCrearTramites
import pe.lecordonbleu.universidadestudiante.data.remote.dto.ResponsePasarelasActivas
import pe.lecordonbleu.universidadestudiante.domain.model.PasarelasActivasRequest
import pe.lecordonbleu.universidadestudiante.getSettingsStorage
import pe.lecordonbleu.universidadestudiante.data.remote.dto.ResponseDuplicadoTituloGuardar
import pe.lecordonbleu.universidadestudiante.data.remote.dto.ResponsePerfilEstudiante
import pe.lecordonbleu.universidadestudiante.data.remote.dto.ResponseRegistrarTramite
import pe.lecordonbleu.universidadestudiante.data.remote.dto.ResponseRequisitosTemp
import pe.lecordonbleu.universidadestudiante.data.remote.dto.ResponseTemporalCuentaCorriente
import pe.lecordonbleu.universidadestudiante.data.remote.dto.ResponseTramiteDocFiltro
import pe.lecordonbleu.universidadestudiante.data.remote.dto.ResponseTramitePaises
import pe.lecordonbleu.universidadestudiante.data.remote.dto.ResponseValidarEgresado
import pe.lecordonbleu.universidadestudiante.domain.model.CrearTramitesRequest
import pe.lecordonbleu.universidadestudiante.domain.model.DataCarreraRequest
import pe.lecordonbleu.universidadestudiante.domain.model.DataPerfilRequest
import pe.lecordonbleu.universidadestudiante.domain.model.DuplicadoTituloGuardarRequest
import pe.lecordonbleu.universidadestudiante.domain.model.GuardarArchivoTramiteRequest
import pe.lecordonbleu.universidadestudiante.domain.model.RegistrarTramiteRequest
import pe.lecordonbleu.universidadestudiante.domain.model.RequisitoTramiteC
import pe.lecordonbleu.universidadestudiante.domain.model.RequisitoTramiteModoCheck
import pe.lecordonbleu.universidadestudiante.domain.model.RequisitosTramiteSealed
import pe.lecordonbleu.universidadestudiante.domain.model.TemporalCuentaCorrienteRequest
import pe.lecordonbleu.universidadestudiante.domain.model.TramiteDocFiltroRequest
import pe.lecordonbleu.universidadestudiante.domain.model.TramitePaisesRequest
import pe.lecordonbleu.universidadestudiante.domain.model.ValidarEgresadoRequest
import pe.lecordonbleu.universidadestudiante.domain.repository.AppRepository
import pe.lecordonbleu.universidadestudiante.presentation.screens.tramitedocumentario.customcell.RequisitoTramiteB
import pe.lecordonbleu.universidadestudiante.presentation.screens.tramitedocumentario.customcell.RequisitoTramiteD
import pe.lecordonbleu.universidadestudiante.presentation.vo.ResourceUiState

class RegistrarTramiteDocumentarioViewModel(private val repo: AppRepository) : ViewModel() {

    private val _uiStatePasarelas =
        MutableStateFlow<ResourceUiState<ResponsePasarelasActivas>>(ResourceUiState.Empty)
    private val _uiStateCarrera =
        MutableStateFlow<ResourceUiState<List<ResponseCarreraRemote>>>(ResourceUiState.Empty)
    private val _uiStateTramiteDocFiltro =
        MutableStateFlow<ResourceUiState<ResponseTramiteDocFiltro>>(ResourceUiState.Empty)
    private val _uiStatePaises =
        MutableStateFlow<ResourceUiState<ResponseTramitePaises>>(ResourceUiState.Empty)
    private val _uiStateCrearTramites =
        MutableStateFlow<ResourceUiState<ResponseCrearTramites>>(ResourceUiState.Empty)
    private val _uiStateRegistrarTramiteTemp =
        MutableStateFlow<ResourceUiState<ResponseRequisitosTemp>>(ResourceUiState.Empty)
    private val _uiStateGuardarArchivo =
        MutableStateFlow<ResourceUiState<ResponseRequisitosTemp>>(ResourceUiState.Empty)
    private val _uiStateDuplicadoTitulo =
        MutableStateFlow<ResourceUiState<ResponseDuplicadoTituloGuardar>>(ResourceUiState.Empty)
    private val _uiStateValidarEgresado =
        MutableStateFlow<ResourceUiState<ResponseValidarEgresado>>(ResourceUiState.Empty)
    private val _uiStateRegistrarTramite =
        MutableStateFlow<ResourceUiState<ResponseRegistrarTramite>>(ResourceUiState.Empty)
    private val _uiStateTTRFiltro =
        MutableStateFlow<ResourceUiState<JsonObject>>(ResourceUiState.Empty)
    private val _uiStateTREFiltro =
        MutableStateFlow<ResourceUiState<JsonObject>>(ResourceUiState.Empty)
    private val _uiStatePerfilEstudiante =
        MutableStateFlow<ResourceUiState<ResponsePerfilEstudiante>>(ResourceUiState.Empty)
    private val _uiStateTemporalCuentaCorriente =
        MutableStateFlow<ResourceUiState<ResponseTemporalCuentaCorriente>>(ResourceUiState.Empty)

    val uiStatePasarelas = _uiStatePasarelas.asStateFlow()
    val uiStateCarrera = _uiStateCarrera.asStateFlow()
    val uiStateTramiteDocFiltro = _uiStateTramiteDocFiltro.asStateFlow()
    val uiStatePaises = _uiStatePaises.asStateFlow()
    val uiStateCrearTramites = _uiStateCrearTramites.asStateFlow()
    val uiStateRegistrarTramiteTemp = _uiStateRegistrarTramiteTemp.asStateFlow()
    val uiStateGuardarArchivo = _uiStateGuardarArchivo.asStateFlow()
    val uiStateDuplicadoTitulo = _uiStateDuplicadoTitulo.asStateFlow()
    val uiStateValidarEgresado = _uiStateValidarEgresado.asStateFlow()
    val uiStateRegistrarTramite = _uiStateRegistrarTramite.asStateFlow()
    val uiStateTTRFiltro = _uiStateTTRFiltro.asStateFlow()
    val uiStateTREFiltro = _uiStateTREFiltro.asStateFlow()
    val uiStatePerfilEstudiante = _uiStatePerfilEstudiante.asStateFlow()
    val uiStateTemporalCuentaCorriente = _uiStateTemporalCuentaCorriente.asStateFlow()

    private lateinit var dataCarreraRequest: DataCarreraRequest
    private lateinit var dataPerfilRequest: DataPerfilRequest
    private lateinit var temporalCuentaCorrienteRequest: TemporalCuentaCorrienteRequest
    private lateinit var tramiteDocFiltroRequest: TramiteDocFiltroRequest
    private lateinit var tramitePaisesRequest: TramitePaisesRequest
    private lateinit var crearTramitesRequest: CrearTramitesRequest
    private lateinit var validarEgresadoRequest: ValidarEgresadoRequest
    private lateinit var registrarTramiteRequest: RegistrarTramiteRequest
    private lateinit var registrarTramiteTempRequest: RegistrarTramiteRequest
    private lateinit var guardarArchivoTramiteRequest: GuardarArchivoTramiteRequest
    private lateinit var duplicadoTituloGuardarRequest: DuplicadoTituloGuardarRequest

    fun setUserCarreraRequest(idEstud: Int) {
        dataCarreraRequest = DataCarreraRequest(idEstud)
        getUserCarreraData()
    }

    fun setPerfilEstudianteRequest(idUsuario: Int) {
        dataPerfilRequest = DataPerfilRequest(idUsuario)
        getPerfilEstudianteData()
    }

    fun setTemporalCuentaCorriente(body: String) {
        temporalCuentaCorrienteRequest = TemporalCuentaCorrienteRequest(body)
        getTemporalCuentaCorrienteData()
    }

    fun setTramitePaisesRequest(idUneg: Int) {
        tramitePaisesRequest = TramitePaisesRequest(idUneg)
        getTramitePaisesData()
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
            "TTR" -> getTramiteTTRFiltroData()
            "TRE" -> getTramiteTREFiltroData()
            else -> getTramiteDocFiltroData()
        }
    }

    fun setValidarEgresadoRequest(
        id_sistema: Int, id_estud_pe: Int, id_pest_det: Int, idTramiteDt: Int,
        id_tipo_usuario: Int, id_estud_serv: Int, id_uneg: Int, id_estud: Int
    ) {
        validarEgresadoRequest = ValidarEgresadoRequest(
            id_sistema, id_estud_pe, id_pest_det, idTramiteDt,
            id_tipo_usuario, id_estud_serv, id_uneg, id_estud
        )
        getValidarEgresadoData()
    }

    fun setCrearTramitesRequest(
        id_sistema: Int, id_tiposervad: Int, id_usuario: Int,
        id_tipo_usuario: Int, idUNEG: Int, condicion: Int, id_estud: Int
    ) {
        crearTramitesRequest = CrearTramitesRequest(
            id_sistema, id_tiposervad, id_usuario, id_tipo_usuario, idUNEG, condicion, id_estud
        )
        getCrearTramitesData()
    }

    fun setRegistrarTramiteRequest(
        id_estud: Int, id_uneg: Int, id_tari_gen: Int, id_user: Int,
        estado_pasarela: String, tari_gen_cod_nav: String, transw_id_tx: String,
        id_pest_det: Int, id_estud_pe: Int, id_estud_serv: Int, pg_tipo_entrega: Int,
        motivo: String, flag_recojo: Int, recojo_dni: String, recojo_nombre: String,
        flag_pago: Int, id_req_temp: Int, id_tramite: Int, id_sistema: Int,
        monto: Double, id_modalidad: Int, tipo_tramite_reg: Int,
        requisitosCheckList: List<RequisitoTramiteModoCheck>
    ) {
        val sealedReq: RequisitosTramiteSealed = when (tipo_tramite_reg) {
            2 -> RequisitosTramiteSealed.RequisitoTramiteTempA(requisitosCheckList)
            else -> RequisitosTramiteSealed.Vacio
        }
        registrarTramiteRequest = RegistrarTramiteRequest(
            id_estud, id_uneg, id_tari_gen, id_user, estado_pasarela, tari_gen_cod_nav,
            transw_id_tx, id_pest_det, id_estud_pe, id_estud_serv, pg_tipo_entrega,
            motivo, flag_recojo, recojo_dni, recojo_nombre, flag_pago, id_req_temp,
            id_tramite, id_sistema, monto, id_modalidad, tipo_tramite_reg, sealedReq
        )
        getRegistrarTramiteData()
    }

    fun setRegistrarTramiteTempRequest(
        id_estud: Int, id_uneg: Int, id_tari_gen: Int, id_user: Int,
        estado_pasarela: String, tari_gen_cod_nav: String, transw_id_tx: String,
        id_pest_det: Int, id_estud_pe: Int, id_estud_serv: Int, pg_tipo_entrega: Int,
        motivo: String, flag_recojo: Int, recojo_dni: String, recojo_nombre: String,
        flag_pago: Int, id_req_temp: Int, id_tramite: Int, id_sistema: Int,
        monto: Double, id_modalidad: Int, tipo_tramite_reg: Int,
        requisitosModoCheckList: List<RequisitoTramiteModoCheck>,
        requisitoTramiteB: List<RequisitoTramiteB>,
        requisitoTramiteD: List<RequisitoTramiteD>
    ) {
        val sealedReq: RequisitosTramiteSealed = when (tipo_tramite_reg) {
            1 -> RequisitosTramiteSealed.RequisitoTramiteTempA(requisitosModoCheckList)
            2 -> RequisitosTramiteSealed.RequisitoTramiteTempB(requisitoTramiteB)
            3 -> RequisitosTramiteSealed.RequisitoTramiteTempD(requisitoTramiteD)
            else -> RequisitosTramiteSealed.Vacio
        }
        registrarTramiteTempRequest = RegistrarTramiteRequest(
            id_estud, id_uneg, id_tari_gen, id_user, estado_pasarela, tari_gen_cod_nav,
            transw_id_tx, id_pest_det, id_estud_pe, id_estud_serv, pg_tipo_entrega,
            motivo, flag_recojo, recojo_dni, recojo_nombre, flag_pago, id_req_temp,
            id_tramite, id_sistema, monto, id_modalidad, tipo_tramite_reg, sealedReq
        )
        getRegistrarTramiteTempData()
    }

    fun setDuplicadoTituloGuardarRequest(request: DuplicadoTituloGuardarRequest) {
        duplicadoTituloGuardarRequest = request
        guardarDuplicadoTitulo()
    }

    fun setGuardarArchivoTramiteRequest(
        id_uneg: Int, id_estud: Int, image64: String, pdfbase64: String,
        extFile: String, nombreDocAbrev: String,
        requisitosDoc: List<RequisitoTramiteC.Doc>,
        requisitosMain: List<RequisitoTramiteC.Main>
    ) {
        val requisitosList = mutableListOf<RequisitoTramiteC>()
        requisitosList.addAll(requisitosDoc)
        requisitosList.addAll(requisitosMain)
        guardarArchivoTramiteRequest = GuardarArchivoTramiteRequest(
            id_uneg, id_estud, image64, pdfbase64, extFile, nombreDocAbrev,
            RequisitosTramiteSealed.RequisitoTramiteTempC(requisitosList)
        )
        guardarArchivoTramite()
    }

    private fun getUserCarreraData() {
        _uiStateCarrera.value = ResourceUiState.Loading
        viewModelScope.launch {
            try {
                val result = repo.getAsistenciaCarrera(dataCarreraRequest)
                if (result.isEmpty()) {
                    _uiStateCarrera.value = ResourceUiState.Error("Carrera incorrecta")
                } else {
                    _uiStateCarrera.value = ResourceUiState.Success(result)
                }
            } catch (e: Exception) {
                _uiStateCarrera.value = ResourceUiState.Error(e.message ?: "Ocurrio un error")
            }
        }
    }

    private fun getPerfilEstudianteData() {
        _uiStatePerfilEstudiante.value = ResourceUiState.Loading
        viewModelScope.launch {
            try {
                val result = repo.getPerfilEstudiante(dataPerfilRequest)
                val perfil = result.firstOrNull()
                if (perfil != null) {
                    _uiStatePerfilEstudiante.value = ResourceUiState.Success(perfil)
                } else {
                    _uiStatePerfilEstudiante.value = ResourceUiState.Error("Sin perfil")
                }
            } catch (e: Exception) {
                _uiStatePerfilEstudiante.value =
                    ResourceUiState.Error(e.message ?: "Error al obtener perfil")
            }
        }
    }

    private fun getTemporalCuentaCorrienteData() {
        _uiStateTemporalCuentaCorriente.value = ResourceUiState.Loading
        viewModelScope.launch {
            try {
                val result = repo.getTemporalCuentaCorriente(temporalCuentaCorrienteRequest)
                _uiStateTemporalCuentaCorriente.value = ResourceUiState.Success(result)
            } catch (e: Exception) {
                _uiStateTemporalCuentaCorriente.value =
                    ResourceUiState.Error(e.message ?: "Error al obtener temporal")
            }
        }
    }

    private fun getTramitePaisesData() {
        _uiStatePaises.value = ResourceUiState.Loading
        viewModelScope.launch {
            try {
                val result = repo.getTramitePaises(tramitePaisesRequest)
                if (result.flag_val == 0) {
                    _uiStatePaises.value = ResourceUiState.Error("No hay paises disponibles")
                } else {
                    _uiStatePaises.value = ResourceUiState.Success(result)
                }
            } catch (e: Exception) {
                _uiStatePaises.value = ResourceUiState.Error(e.message ?: "Error al obtener paises")
            }
        }
    }

    private fun getTramiteDocFiltroData() {
        _uiStateTramiteDocFiltro.value = ResourceUiState.Loading
        viewModelScope.launch {
            try {
                val result = repo.getTramiteDocFiltro(tramiteDocFiltroRequest)
                if (result.flag_val == 0) {
                    _uiStateTramiteDocFiltro.value =
                        ResourceUiState.Error("No hay datos disponibles")
                } else {
                    _uiStateTramiteDocFiltro.value = ResourceUiState.Success(result)
                }
            } catch (e: Exception) {
                _uiStateTramiteDocFiltro.value =
                    ResourceUiState.Error(e.message ?: "Ocurrio un error")
            }
        }
    }

    private fun getCrearTramitesData() {
        _uiStateCrearTramites.value = ResourceUiState.Loading
        viewModelScope.launch {
            try {
                val result = repo.crearTramitesEstudiante(crearTramitesRequest)
                if (result.flag_val == 0) {
                    _uiStateCrearTramites.value =
                        ResourceUiState.Error("No se pudo crear el tramite")
                } else {
                    _uiStateCrearTramites.value = ResourceUiState.Success(result)
                }
            } catch (e: Exception) {
                _uiStateCrearTramites.value = ResourceUiState.Error(e.message ?: "Ocurrio un error")
            }
        }
    }

    private fun getTramiteTTRFiltroData() {
        _uiStateTTRFiltro.value = ResourceUiState.Loading
        viewModelScope.launch {
            try {
                val result = repo.getTramiteFiltroJson(tramiteDocFiltroRequest)
                _uiStateTTRFiltro.value = ResourceUiState.Success(result)
            } catch (e: Exception) {
                _uiStateTTRFiltro.value = ResourceUiState.Error("Error al obtener TTR")
            }
        }
    }

    private fun getTramiteTREFiltroData() {
        _uiStateTREFiltro.value = ResourceUiState.Loading
        viewModelScope.launch {
            try {
                val result = repo.getTramiteFiltroJson(tramiteDocFiltroRequest)
                _uiStateTREFiltro.value = ResourceUiState.Success(result)
            } catch (e: Exception) {
                _uiStateTREFiltro.value = ResourceUiState.Error("Error al obtener TRE")
            }
        }
    }

    private fun getValidarEgresadoData() {
        _uiStateValidarEgresado.value = ResourceUiState.Loading
        viewModelScope.launch {
            try {
                val result = repo.validarEgresado(validarEgresadoRequest)
                _uiStateValidarEgresado.value = ResourceUiState.Success(result)
            } catch (e: Exception) {
                _uiStateValidarEgresado.value =
                    ResourceUiState.Error("Error al validar egresado: ${e.message}")
            }
        }
    }

    private fun getRegistrarTramiteData() {
        _uiStateRegistrarTramite.value = ResourceUiState.Loading
        viewModelScope.launch {
            try {
                val result = repo.registrarTramiteEstudiante(registrarTramiteRequest)
                _uiStateRegistrarTramite.value = ResourceUiState.Success(result)
            } catch (e: Exception) {
                _uiStateRegistrarTramite.value =
                    ResourceUiState.Error("Error al registrar tramite: ${e.message}")
            }
        }
    }

    private fun getRegistrarTramiteTempData() {
        _uiStateRegistrarTramiteTemp.value = ResourceUiState.Loading
        viewModelScope.launch {
            try {
                val result = repo.registrarTramiteTemp(registrarTramiteTempRequest)
                _uiStateRegistrarTramiteTemp.value = ResourceUiState.Success(result)
            } catch (e: Exception) {
                _uiStateRegistrarTramiteTemp.value =
                    ResourceUiState.Error("Error al registrar tramite temporal: ${e.message}")
            }
        }
    }

    private fun guardarArchivoTramite() {
        _uiStateGuardarArchivo.value = ResourceUiState.Loading
        viewModelScope.launch {
            try {
                val result = repo.guardarTramiteArchivo(guardarArchivoTramiteRequest)
                _uiStateGuardarArchivo.value = ResourceUiState.Success(result)
            } catch (e: Exception) {
                _uiStateGuardarArchivo.value =
                    ResourceUiState.Error("Error al guardar archivo: ${e.message}")
            }
        }
    }

    private fun guardarDuplicadoTitulo() {
        _uiStateDuplicadoTitulo.value = ResourceUiState.Loading
        viewModelScope.launch {
            try {
                val result = repo.duplicadoTituloGuardar(duplicadoTituloGuardarRequest)
                _uiStateDuplicadoTitulo.value = ResourceUiState.Success(result)
            } catch (e: Exception) {
                _uiStateDuplicadoTitulo.value =
                    ResourceUiState.Error("Error duplicado titulo: ${e.message}")
            }
        }
    }

    fun resetTramiteDocFiltroState() {
        _uiStateTramiteDocFiltro.value = ResourceUiState.Empty
    }

    fun resetTramiteTTRComboState() {
        _uiStateTTRFiltro.value = ResourceUiState.Empty
    }

    fun resetTramiteTREComboState() {
        _uiStateTREFiltro.value = ResourceUiState.Empty
    }

    fun resetTREFiltroState() {
        _uiStateTREFiltro.value = ResourceUiState.Empty
    }

    fun resetRegistrarTramiteState() {
        _uiStateRegistrarTramite.value = ResourceUiState.Empty
    }

    fun resetRegistrarTramiteTempState() {
        _uiStateRegistrarTramiteTemp.value = ResourceUiState.Empty
    }

    fun resetValidarEgresadoState() {
        _uiStateValidarEgresado.value = ResourceUiState.Empty
    }

    fun resetGuardarArchivoState() {
        _uiStateGuardarArchivo.value = ResourceUiState.Empty
    }

    fun resetCrearTramitesState() {
        _uiStateCrearTramites.value = ResourceUiState.Empty
    }

    fun resetDuplicadoTituloState() {
        _uiStateDuplicadoTitulo.value = ResourceUiState.Empty
    }

    fun resetTempCuentaCorrienteState() {
        _uiStateTemporalCuentaCorriente.value = ResourceUiState.Empty
    }

    fun resetPerfilEstudianteState() {
        _uiStatePerfilEstudiante.value = ResourceUiState.Empty
    }

    fun resetTramitePaisesState() {
        _uiStatePaises.value = ResourceUiState.Empty
    }

    fun resetCarreraState() {
        _uiStateCarrera.value = ResourceUiState.Empty
    }

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

    fun resetPasarelasState() {
        _uiStatePasarelas.value = ResourceUiState.Empty
    }
}
