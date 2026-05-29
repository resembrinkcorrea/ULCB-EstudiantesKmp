package pe.lecordonbleu.universidadestudiante.presentation.screens.horario

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.datetime.LocalDate
import pe.lecordonbleu.universidadestudiante.data.remote.dto.ResponseCarrera
import pe.lecordonbleu.universidadestudiante.data.remote.dto.ResponseHorario
import pe.lecordonbleu.universidadestudiante.data.remote.dto.ResponsePeriodo
import pe.lecordonbleu.universidadestudiante.domain.model.CarreraRequest
import pe.lecordonbleu.universidadestudiante.domain.model.HorarioRequest
import pe.lecordonbleu.universidadestudiante.domain.model.PeriodoRequest
import pe.lecordonbleu.universidadestudiante.domain.repository.AppRepository
import pe.lecordonbleu.universidadestudiante.presentation.vo.ResourceUiState

class HorarioViewModel(private val repo: AppRepository) : ViewModel() {

    private val _uiStateHorario = MutableStateFlow<ResourceUiState<ResponseHorario>>(ResourceUiState.Loading)
    private val _uiStateCarrera = MutableStateFlow<ResourceUiState<ResponseCarrera>>(ResourceUiState.Loading)
    private val _uiStatePeriodo = MutableStateFlow<ResourceUiState<ResponsePeriodo>>(ResourceUiState.Loading)

    val uiStateHorario = _uiStateHorario.asStateFlow()
    val uiStateCarrera = _uiStateCarrera.asStateFlow()
    val uiStatePeriodo = _uiStatePeriodo.asStateFlow()

    private var _allHorarios: List<pe.lecordonbleu.universidadestudiante.data.remote.dto.Horario> = emptyList()

    private val _diasConClase = MutableStateFlow<List<LocalDate>>(emptyList())
    val diasConClase = _diasConClase.asStateFlow()

    private lateinit var horarioRequest: HorarioRequest
    private lateinit var carreraRequest: CarreraRequest
    private lateinit var periodoRequest: PeriodoRequest

    fun setHorarioRequest(idEstudPe: Int, idOacadArranque: Int, fechaIni: String, fechaFin: String) {
        horarioRequest = HorarioRequest(idEstudPe, idOacadArranque, fechaIni, fechaFin)
        fetchHorario()
    }

    fun setCarreraRequest(idStud: Int) {
        carreraRequest = CarreraRequest(idStud)
        fetchCarrera()
    }

    fun setPeriodoRequest(idEstudServ: Int) {
        periodoRequest = PeriodoRequest(idEstudServ)
        fetchPeriodo()
    }

    fun filtrarPorCurso(curso: String) {
        val base = if (curso.isEmpty()) _allHorarios else _allHorarios.filter { it.pest_asign_nombre == curso }
        _diasConClase.value = base.mapNotNull { runCatching { LocalDate.parse(it.hor_asis_dia) }.getOrNull() }.distinct()
    }


    private fun fetchHorario() {
        viewModelScope.launch {
            _uiStateHorario.value = ResourceUiState.Loading
            try {
                val response = repo.getHorario(horarioRequest)
                _allHorarios = response.listadoHorario
                _diasConClase.value = response.listadoHorario.mapNotNull {
                    runCatching { LocalDate.parse(it.hor_asis_dia) }.getOrNull()
                }.distinct()
                _uiStateHorario.value = ResourceUiState.Success(response)
            } catch (e: Exception) {
                _uiStateHorario.value = ResourceUiState.Error(e.message ?: "Error al cargar horario")
            }
        }
    }

    private fun fetchCarrera() {
        viewModelScope.launch {
            _uiStateCarrera.value = ResourceUiState.Loading
            try {
                val response = repo.getCarrera(carreraRequest)
                _uiStateCarrera.value = ResourceUiState.Success(response)
            } catch (e: Exception) {
                _uiStateCarrera.value = ResourceUiState.Error(e.message ?: "Error al cargar carrera")
            }
        }
    }

    private fun fetchPeriodo() {
        viewModelScope.launch {
            _uiStatePeriodo.value = ResourceUiState.Loading
            try {
                val response = repo.getPeriodo(periodoRequest)
                _uiStatePeriodo.value = ResourceUiState.Success(response)
            } catch (e: Exception) {
                _uiStatePeriodo.value = ResourceUiState.Error(e.message ?: "Error al cargar período")
            }
        }
    }

    fun clearHorario() {
        _uiStateHorario.value = ResourceUiState.Loading
        _allHorarios = emptyList()
        _diasConClase.value = emptyList()
    }
}
