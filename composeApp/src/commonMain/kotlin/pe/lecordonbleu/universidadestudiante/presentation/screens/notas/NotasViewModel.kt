package pe.lecordonbleu.universidadestudiante.presentation.screens.notas

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import pe.lecordonbleu.universidadestudiante.data.remote.dto.ResponseCarrera
import pe.lecordonbleu.universidadestudiante.data.remote.dto.ResponseCursos
import pe.lecordonbleu.universidadestudiante.data.remote.dto.ResponsePeriodo
import pe.lecordonbleu.universidadestudiante.domain.model.CarreraRequest
import pe.lecordonbleu.universidadestudiante.domain.model.CursosRequest
import pe.lecordonbleu.universidadestudiante.domain.model.PeriodoRequest
import pe.lecordonbleu.universidadestudiante.domain.repository.AppRepository
import pe.lecordonbleu.universidadestudiante.presentation.vo.ResourceUiState

class NotasViewModel(private val repo: AppRepository) : ViewModel() {

    private val _carreraState = MutableStateFlow<ResourceUiState<ResponseCarrera>>(ResourceUiState.Empty)
    val carreraState = _carreraState.asStateFlow()

    private val _periodoState = MutableStateFlow<ResourceUiState<ResponsePeriodo>>(ResourceUiState.Empty)
    val periodoState = _periodoState.asStateFlow()

    private val _cursosState = MutableStateFlow<ResourceUiState<ResponseCursos>>(ResourceUiState.Empty)
    val cursosState = _cursosState.asStateFlow()

    private lateinit var carreraRequest: CarreraRequest
    private lateinit var periodoRequest: PeriodoRequest
    private lateinit var cursosRequest: CursosRequest

    fun setCarreraRequest(idEstud: Int) {
        carreraRequest = CarreraRequest(id_estud = idEstud)
        getCarreraData()
    }

    fun setPeriodoRequest(idEstudServ: Int) {
        periodoRequest = PeriodoRequest(id_estud_serv = idEstudServ)
        getPeriodoData()
    }

    fun setCursosRequest(idEstudPe: Int, idOacadArranque: Int) {
        cursosRequest = CursosRequest(id_estud_pe = idEstudPe, id_oacad_arranque = idOacadArranque)
        getCursosData()
    }

    private fun getCarreraData() {
        viewModelScope.launch {
            _carreraState.value = ResourceUiState.Loading
            try {
                val result = repo.getCarrera(carreraRequest)
                _carreraState.value = if (result.carrera.isEmpty()) ResourceUiState.Error("No se encontró carrera.")
                else ResourceUiState.Success(result)
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
                _periodoState.value = if (result.periodo.isEmpty()) ResourceUiState.Error("No se encontró período.")
                else ResourceUiState.Success(result)
            } catch (e: Exception) {
                _periodoState.value = ResourceUiState.Error(e.message ?: "Ocurrió un error")
            }
        }
    }

    private fun getCursosData() {
        viewModelScope.launch {
            _cursosState.value = ResourceUiState.Loading
            try {
                val result = repo.getCursosNotas(cursosRequest)
                _cursosState.value = if (result.listadoNotas.isEmpty()) ResourceUiState.Error("No hay notas para mostrar.")
                else ResourceUiState.Success(result)
            } catch (e: Exception) {
                _cursosState.value = ResourceUiState.Error(e.message ?: "Ocurrió un error")
            }
        }
    }

    fun resetUiState() {
        _carreraState.value = ResourceUiState.Empty
        _periodoState.value = ResourceUiState.Empty
        _cursosState.value = ResourceUiState.Empty
    }
}
