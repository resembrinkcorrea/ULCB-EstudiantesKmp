package pe.lecordonbleu.universidadestudiante.presentation.screens.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import pe.lecordonbleu.universidadestudiante.data.remote.dto.ResponseDataMenu
import pe.lecordonbleu.universidadestudiante.data.remote.dto.ResponseHora
import pe.lecordonbleu.universidadestudiante.data.remote.dto.ResponseProyeccionValidacion
import pe.lecordonbleu.universidadestudiante.data.remote.dto.ResponseAsignaturaEncuesta
import pe.lecordonbleu.universidadestudiante.data.remote.dto.ResponseEncuestaSatisfaccionEstado
import pe.lecordonbleu.universidadestudiante.data.remote.dto.ResponseListarEncuesta
import pe.lecordonbleu.universidadestudiante.domain.model.UserMenuRequest
import pe.lecordonbleu.universidadestudiante.domain.model.ValidaProyeccionRequest
import pe.lecordonbleu.universidadestudiante.domain.model.AsignaturaEncuestaRequest
import pe.lecordonbleu.universidadestudiante.domain.model.EncuestaSatisfaccionEstadoRequest
import pe.lecordonbleu.universidadestudiante.domain.model.ListarEncuestaRequest
import pe.lecordonbleu.universidadestudiante.domain.repository.AppRepository
import pe.lecordonbleu.universidadestudiante.domain.model.FcmTokenRequest
import pe.lecordonbleu.universidadestudiante.data.remote.dto.ResponseActualizarToken
import pe.lecordonbleu.universidadestudiante.domain.model.FichaMatriculaRequest
import pe.lecordonbleu.universidadestudiante.data.remote.dto.ResponseHorario
import pe.lecordonbleu.universidadestudiante.domain.model.HorarioRequest
import pe.lecordonbleu.universidadestudiante.getTodayLocalDate
import pe.lecordonbleu.universidadestudiante.presentation.vo.ResourceUiState
import kotlinx.datetime.DatePeriod
import kotlinx.datetime.LocalDate
import kotlinx.datetime.plus


class HomeViewModel(private val repo: AppRepository) : ViewModel() {

    private val _uiState = MutableStateFlow<ResourceUiState<List<ResponseDataMenu>>>(ResourceUiState.Loading)
    private val _uiStateHora = MutableStateFlow<ResourceUiState<ResponseHora>>(ResourceUiState.Empty)
    private val _proyeccionState =
        MutableStateFlow<ResourceUiState<ResponseProyeccionValidacion>>(ResourceUiState.Empty)
    private val _encuestaDocenteState =
        MutableStateFlow<ResourceUiState<ResponseAsignaturaEncuesta>>(ResourceUiState.Empty)
    private val _encuestaSatisfaccionState =
        MutableStateFlow<ResourceUiState<ResponseEncuestaSatisfaccionEstado>>(ResourceUiState.Empty)
    private val _listarEncuestaState =
        MutableStateFlow<ResourceUiState<ResponseListarEncuesta>>(ResourceUiState.Empty)
    private val _fcmTokenState =
        MutableStateFlow<ResourceUiState<ResponseActualizarToken>>(ResourceUiState.Empty)
    private val _fichaMatrState =
        MutableStateFlow<ResourceUiState<ByteArray>>(ResourceUiState.Empty)
    private val _clasesHoyState =
        MutableStateFlow<ResourceUiState<ResponseHorario>>(ResourceUiState.Empty)

    val uiState = _uiState.asStateFlow()
    val uiStateHora = _uiStateHora.asStateFlow()
    val proyeccionState = _proyeccionState.asStateFlow()
    val encuestaDocenteState = _encuestaDocenteState.asStateFlow()
    val encuestaSatisfaccionState = _encuestaSatisfaccionState.asStateFlow()
    val listarEncuestaState = _listarEncuestaState.asStateFlow()
    val fcmTokenState = _fcmTokenState.asStateFlow()
    val fichaMatrState = _fichaMatrState.asStateFlow()
    val clasesHoyState = _clasesHoyState.asStateFlow()

    private lateinit var userMenuRequest: UserMenuRequest
    private lateinit var fcmTokenRequest: FcmTokenRequest

    private lateinit var fichaMatriculaRequest: FichaMatriculaRequest

    fun setUserMenuRequest(id_uneg: Int, id_sistema: Int, id_perfil: Int) {
        userMenuRequest =
            UserMenuRequest(id_uneg = id_uneg, id_sistema = id_sistema, id_perfil = id_perfil)
        getUserMenuData()
    }

    fun setFichaMatr(idUNEG: Int, idPeriodoAcademico: String, personaNombre: String, personaPaterno: String, personaMaterno: String, periodo: String, codigoEstud: String, carreraProf: String, valorFecha: String, idOacadArranque: String, idEstudPe: String, idUsuario: Int, idPestDet: Int, idEstud: Int, estadoIngresante: String, promUltMat: String) {
        fichaMatriculaRequest = FichaMatriculaRequest(
            idUNEG,
            idPeriodoAcademico,
            personaNombre,
            personaPaterno,
            personaMaterno,
            periodo,
            codigoEstud,
            carreraProf,
            valorFecha,
            idOacadArranque,
            idEstudPe,
            idUsuario,
            idPestDet,
            idEstud,
            estadoIngresante,
            promUltMat
        )
    }

    fun getUserMenuData() {
        viewModelScope.launch {
            try {
                val users = repo.getMenuDataUser(userMenuRequest)
                if (users.isEmpty()) {
                    _uiState.value = ResourceUiState.Error("Menu incorrecto")
                } else {
                    _uiState.value = ResourceUiState.Success(users)
                }
            } catch (e: Exception) {
                _uiState.value = ResourceUiState.Error(e.message ?: "Ocurrió un error")
            }
        }
    }

    fun getHoraServidor() {
        viewModelScope.launch {
            _uiStateHora.value = ResourceUiState.Loading
            try {
                val response = repo.getHoraServidor()
                if (response.listHoraServer.isEmpty()) {
                    _uiStateHora.value = ResourceUiState.Error("No se pudo obtener la hora del servidor")
                } else {
                    _uiStateHora.value = ResourceUiState.Success(response)
                }
            } catch (e: Exception) {
                _uiStateHora.value = ResourceUiState.Error(e.message ?: "Error al obtener hora del servidor")
            }
        }
    }


    fun fetchProyeccionValidacion(idEstud: Int) {
        viewModelScope.launch {
            _proyeccionState.value = ResourceUiState.Loading
            try {
                val response = repo.getProyeccionValidacion(ValidaProyeccionRequest(idEstud))
                _proyeccionState.value = ResourceUiState.Success(response)
            } catch (e: Exception) {
                _proyeccionState.value = ResourceUiState.Error(e.message ?: "Error al obtener proyección")
            }
        }
    }

    fun fetchEncuestaDocente(idPeracad: Int, idEstudPe: Int, idServ: Int, idOacadArranque: Int) {
        viewModelScope.launch {
            _encuestaDocenteState.value = ResourceUiState.Loading
            try {
                val response = repo.getAsignaturaEncuesta(
                    AsignaturaEncuestaRequest(
                        id_peracad = idPeracad,
                        id_estud_pe = idEstudPe,
                        id_serv = idServ,
                        id_oacad_arranque = idOacadArranque
                    )
                )
                _encuestaDocenteState.value = ResourceUiState.Success(response)
            } catch (e: Exception) {
                _encuestaDocenteState.value = ResourceUiState.Error(e.message ?: "Error al obtener encuesta docente")
            }
        }
    }

    fun fetchEncuestaSatisfaccion(idPeracad: Int, idEstudPe: Int, idServ: Int, idOacadArranque: Int) {
        viewModelScope.launch {
            _encuestaSatisfaccionState.value = ResourceUiState.Loading
            try {
                val response = repo.getEncuestaSatisfaccionEstado(
                    EncuestaSatisfaccionEstadoRequest(
                        id_peracad = idPeracad,
                        id_estud_pe = idEstudPe,
                        id_serv = idServ,
                        id_oacad_arranque = idOacadArranque
                    )
                )
                _encuestaSatisfaccionState.value = ResourceUiState.Success(response)
            } catch (e: Exception) {
                _encuestaSatisfaccionState.value = ResourceUiState.Error(e.message ?: "Error al obtener encuesta satisfacción")
            }
        }
    }

    

    fun fetchListarEncuesta(idPestDet: Int, idPeracad: Int) {
        viewModelScope.launch {
            _listarEncuestaState.value = ResourceUiState.Loading
            try {
                val response = repo.getListarEncuesta(
                    ListarEncuestaRequest(id_uneg = 1, id_pest_det = idPestDet, id_peracad = idPeracad)
                )
                _listarEncuestaState.value = ResourceUiState.Success(response)
            } catch (e: Exception) {
                _listarEncuestaState.value = ResourceUiState.Error(e.message ?: "Error al obtener encuesta")
            }
        }
    }

    fun resetEncuestaDocenteState() {
        _encuestaDocenteState.value = ResourceUiState.Empty
    }

    fun resetEncuestaSatisfaccionState() {
        _encuestaSatisfaccionState.value = ResourceUiState.Empty
    }

    fun resetListarEncuestaState() {
        _listarEncuestaState.value = ResourceUiState.Empty
    }

    fun resetHoraState() {
        _uiStateHora.value = ResourceUiState.Empty
    }

    fun setRegistrarFcmToken(idEstud: Int, token: String) {
        fcmTokenRequest = FcmTokenRequest(id_estud = idEstud, token_app = token)
        registrarFcmToken()
    }

    private fun registrarFcmToken() {
        viewModelScope.launch {
            _fcmTokenState.value = ResourceUiState.Loading
            try {
                val response = repo.actualizarTokenApp(fcmTokenRequest)
                _fcmTokenState.value = ResourceUiState.Success(response)
            } catch (e: Exception) {
                _fcmTokenState.value = ResourceUiState.Error(e.message ?: "Error")
            }
        }
    }

    fun resetFcmTokenState() {
        _fcmTokenState.value = ResourceUiState.Empty
    }

    fun fetchFichaMatricula() {
        viewModelScope.launch {
            _fichaMatrState.value = ResourceUiState.Loading
            try {
                val result = repo.generarFichaMatricula(fichaMatriculaRequest)
                if (result.isNotEmpty()) {
                    _fichaMatrState.value = ResourceUiState.Success(result)
                } else {
                    _fichaMatrState.value = ResourceUiState.Error("No se pudo generar la ficha.")
                }
            } catch (e: Exception) {
                _fichaMatrState.value = ResourceUiState.Error(e.message ?: "Error al generar ficha de matrícula")
            }
        }
    }

    fun resetFichaMatrState() {
        _fichaMatrState.value = ResourceUiState.Empty
    }

    fun fetchClasesHoy(idEstudPe: Int, idOacadArranque: Int) {
        viewModelScope.launch {
            _clasesHoyState.value = ResourceUiState.Loading
            try {
                val hoy = getTodayLocalDate()
                val siete = hoy.plus(DatePeriod(days = 7))
                fun LocalDate.fmt() = "${year}-${monthNumber.toString().padStart(2, '0')}-${dayOfMonth.toString().padStart(2, '0')}"
                val response = repo.getHorario(
                    HorarioRequest(
                        id_estud_pe = idEstudPe,
                        id_oacad_arranque = idOacadArranque,
                        fecha_ini = hoy.fmt(),
                        fecha_fin = siete.fmt()
                    )
                )
                _clasesHoyState.value = ResourceUiState.Success(response)
            } catch (e: Exception) {
                _clasesHoyState.value = ResourceUiState.Error(e.message ?: "Error")
            }
        }
    }

    fun resetClasesHoyState() {
        _clasesHoyState.value = ResourceUiState.Empty
    }
}
