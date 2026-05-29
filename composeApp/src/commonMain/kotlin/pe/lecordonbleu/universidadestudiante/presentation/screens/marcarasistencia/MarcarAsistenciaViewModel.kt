package pe.lecordonbleu.universidadestudiante.presentation.screens.marcarasistencia

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import pe.lecordonbleu.universidadestudiante.data.remote.dto.ResponseEstadoMarcacion
import pe.lecordonbleu.universidadestudiante.data.remote.dto.ResponseHora
import pe.lecordonbleu.universidadestudiante.data.remote.dto.ResponseMarcarAsistencia
import pe.lecordonbleu.universidadestudiante.domain.model.EstadoMarcacionRequest
import pe.lecordonbleu.universidadestudiante.domain.model.MarcarRequest
import pe.lecordonbleu.universidadestudiante.domain.model.NavigationLogRequest
import pe.lecordonbleu.universidadestudiante.domain.repository.AppRepository
import pe.lecordonbleu.universidadestudiante.presentation.vo.ResourceUiState

class MarcarAsistenciaViewModel(private val repo: AppRepository) : ViewModel() {

    private val _horaUiState = MutableStateFlow<ResourceUiState<ResponseHora>>(ResourceUiState.Empty)
    private val _verMarcarUiState = MutableStateFlow<ResourceUiState<ResponseEstadoMarcacion>>(ResourceUiState.Empty)
    private val _marcarAsistenciaUiState = MutableStateFlow<ResourceUiState<ResponseMarcarAsistencia>>(ResourceUiState.Empty)

    val horaUiState = _horaUiState.asStateFlow()
    val verMarcarUiState = _verMarcarUiState.asStateFlow()
    val marcarAsistenciaUiState = _marcarAsistenciaUiState.asStateFlow()

    private lateinit var estadoMarcacionRequest: EstadoMarcacionRequest
    private lateinit var marcarRequest: MarcarRequest
    private var pollingJob: Job? = null

    fun fetchHoraServidor() {
        viewModelScope.launch {
            _horaUiState.value = ResourceUiState.Loading
            try {
                val hora = repo.getHoraServidor()
                _horaUiState.value = ResourceUiState.Success(hora)
            } catch (e: Exception) {
                _horaUiState.value = ResourceUiState.Error(e.message ?: "Error al obtener hora")
            }
        }
    }

    fun setVerMarcar(id_uneg: Int, id_estud_pe: Int, id_serv: Int) {
        estadoMarcacionRequest = EstadoMarcacionRequest(id_uneg, id_estud_pe, id_serv)
        getEstadoMarcacion()
    }

    private fun getEstadoMarcacion() {
        pollingJob = viewModelScope.launch {
            _verMarcarUiState.value = ResourceUiState.Loading
            repo.getEstadoMarcacionEstudiante(estadoMarcacionRequest)
                .catch { _verMarcarUiState.value = ResourceUiState.Error(it.message ?: "Error al obtener estado") }
                .collect { response ->
                    _verMarcarUiState.value = ResourceUiState.Success(response)
                    if (response.estado_marcar.isNotEmpty()) {
                        pollingJob?.cancel()
                    }
                }
        }
    }

    fun stopPolling() {
        pollingJob?.cancel()
    }

    fun setMarcarAsistencia(id_unidad_negocio: Int, id_usuario: Int, id_hor_asis: Int, id_estud_pe: Int, id_sistema: Int) {
        marcarRequest = MarcarRequest(id_unidad_negocio, id_usuario, id_hor_asis, id_estud_pe, id_sistema)
        getMarcarAsistencia()
    }

    private fun getMarcarAsistencia() {
        viewModelScope.launch {
            _marcarAsistenciaUiState.value = ResourceUiState.Loading
            try {
                val response = repo.marcarAsistencia(marcarRequest)
                _marcarAsistenciaUiState.value = ResourceUiState.Success(response)
            } catch (e: Exception) {
                _marcarAsistenciaUiState.value = ResourceUiState.Error(e.message ?: "Error al marcar asistencia")
            }
        }
    }

    fun setNavigationLog(
        nomCompleto: String,
        nombreArchivo: String,
        perf_nombre: String,
        divasitAulaDemo: String,
        idUNEG: Int,
        id_usuario: Int,
        idPerfil: Int,
        dato: String,
        sistema: String,
        ip: String,
        flag_boton: Int,
        nombreUNEG: String
    ) {
        val request = NavigationLogRequest(
            nomCompleto, nombreArchivo, perf_nombre, divasitAulaDemo,
            idUNEG, id_usuario, idPerfil, dato, sistema, ip, flag_boton, nombreUNEG
        )
        viewModelScope.launch {
            try {
                repo.logNavigation(request)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun resetVerMarcarState() {
        _verMarcarUiState.value = ResourceUiState.Empty
    }

    fun resetMarcarAsistenciaState() {
        _marcarAsistenciaUiState.value = ResourceUiState.Empty
    }
}
