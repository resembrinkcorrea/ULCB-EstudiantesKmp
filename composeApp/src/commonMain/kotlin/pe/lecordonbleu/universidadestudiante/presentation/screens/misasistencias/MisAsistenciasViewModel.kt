package pe.lecordonbleu.universidadestudiante.presentation.screens.misasistencias

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import pe.lecordonbleu.universidadestudiante.data.remote.dto.ResponseAsignaturaEstudiante
import pe.lecordonbleu.universidadestudiante.data.remote.dto.ResponseCarreraRemote
import pe.lecordonbleu.universidadestudiante.data.remote.dto.ResponsePeriodo
import pe.lecordonbleu.universidadestudiante.domain.model.AsignaturaEstudianteRequest
import pe.lecordonbleu.universidadestudiante.domain.model.DataCarreraRequest
import pe.lecordonbleu.universidadestudiante.domain.model.PeriodoRequest
import pe.lecordonbleu.universidadestudiante.domain.repository.AppRepository
import pe.lecordonbleu.universidadestudiante.presentation.vo.ResourceUiState

class MisAsistenciasViewModel(private val repo: AppRepository) : ViewModel() {

    private val _carreraState = MutableStateFlow<ResourceUiState<List<ResponseCarreraRemote>>>(ResourceUiState.Empty)
    val carreraState = _carreraState.asStateFlow()

    private val _periodoState = MutableStateFlow<ResourceUiState<ResponsePeriodo>>(ResourceUiState.Empty)
    val periodoState = _periodoState.asStateFlow()

    private val _asignaturaState = MutableStateFlow<ResourceUiState<ResponseAsignaturaEstudiante>>(ResourceUiState.Empty)
    val asignaturaState = _asignaturaState.asStateFlow()

    private lateinit var dataCarreraRequest: DataCarreraRequest
    private lateinit var periodoRequest: PeriodoRequest
    private lateinit var asignaturaEstudianteRequest: AsignaturaEstudianteRequest

    fun setCarreraRequest(idEstud: Int) {
        dataCarreraRequest = DataCarreraRequest(id_estud = idEstud)
        getCarreraData()
    }

    fun setPeriodoRequest(idEstudServ: Int) {
        periodoRequest = PeriodoRequest(id_estud_serv = idEstudServ)
        getPeriodoData()
    }

    fun setAsignaturaRequest(idEstudPe: Int, idPeracad: Int) {
        asignaturaEstudianteRequest = AsignaturaEstudianteRequest(id_estud_pe = idEstudPe, id_peracad = idPeracad)
        getAsignaturaData()
    }

    private fun getCarreraData() {
        viewModelScope.launch {
            _carreraState.value = ResourceUiState.Loading
            try {
                val result = repo.getAsistenciaCarrera(dataCarreraRequest)
                _carreraState.value = if (result.isEmpty() || result.first().carrera.isEmpty()) {
                    ResourceUiState.Error("No se encontró carrera.")
                } else {
                    ResourceUiState.Success(result)
                }
            } catch (e: Exception) {
                _carreraState.value = ResourceUiState.Error(e.message ?: "Ocurrió un error")
            }
        }
    }

    private fun getPeriodoData() {
        viewModelScope.launch {
            _periodoState.value = ResourceUiState.Loading
            try {
                val result = repo.getPeriodo(periodoRequest)
                _periodoState.value = if (result.periodo.isEmpty()) {
                    ResourceUiState.Error("No se encontró período.")
                } else {
                    ResourceUiState.Success(result)
                }
            } catch (e: Exception) {
                _periodoState.value = ResourceUiState.Error(e.message ?: "Ocurrió un error")
            }
        }
    }

    private fun getAsignaturaData() {
        viewModelScope.launch {
            _asignaturaState.value = ResourceUiState.Loading
            try {
                val result = repo.getAsignaturaEstudiante(asignaturaEstudianteRequest)
                _asignaturaState.value = if (result.isEmpty() || result.first().asignatura.isEmpty() || result.first().flag_val == -1) {
                    ResourceUiState.Error("No se encontraron asignaturas.")
                } else {
                    ResourceUiState.Success(result.first())
                }
            } catch (e: Exception) {
                _asignaturaState.value = ResourceUiState.Error(e.message ?: "Ocurrió un error")
            }
        }
    }

    fun resetUiState() {
        _carreraState.value = ResourceUiState.Empty
        _periodoState.value = ResourceUiState.Empty
        _asignaturaState.value = ResourceUiState.Empty
    }
}
