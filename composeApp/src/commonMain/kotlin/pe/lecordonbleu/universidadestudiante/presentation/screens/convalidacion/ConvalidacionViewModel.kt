package pe.lecordonbleu.universidadestudiante.presentation.screens.convalidacion

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import pe.lecordonbleu.universidadestudiante.data.remote.dto.ResponseCarreraPlanEstudio
import pe.lecordonbleu.universidadestudiante.data.remote.dto.ResponseCarrerasConvalidacion
import pe.lecordonbleu.universidadestudiante.data.remote.dto.ResponseCursosConvalidacion
import pe.lecordonbleu.universidadestudiante.data.remote.dto.ResponseEstudianteOAcadConvalidacion
import pe.lecordonbleu.universidadestudiante.data.remote.dto.ResponseTipoTraslado
import pe.lecordonbleu.universidadestudiante.domain.model.CarrerasConvalidacionRequest
import pe.lecordonbleu.universidadestudiante.domain.model.CursosConvalidacionRequest
import pe.lecordonbleu.universidadestudiante.domain.model.DataCarreraRequest
import pe.lecordonbleu.universidadestudiante.domain.model.EstudianteOAcadConvalidacionRequest
import pe.lecordonbleu.universidadestudiante.domain.model.TipoTrasladoRequest
import pe.lecordonbleu.universidadestudiante.domain.repository.AppRepository
import pe.lecordonbleu.universidadestudiante.presentation.vo.ResourceUiState

class ConvalidacionViewModel(private val repo: AppRepository) : ViewModel() {

    private val _uiState = MutableStateFlow<ResourceUiState<ResponseCarreraPlanEstudio>>(ResourceUiState.Loading)
    private val _uiStateTipoTrasladoConvalidacion = MutableStateFlow<ResourceUiState<ResponseTipoTraslado>>(ResourceUiState.Loading)
    private val _uiStateEstudianteOAcadConvalidacion = MutableStateFlow<ResourceUiState<List<ResponseEstudianteOAcadConvalidacion>>>(ResourceUiState.Loading)
    private val _uiStateSedeConvalidacion = MutableStateFlow<ResourceUiState<List<ResponseEstudianteOAcadConvalidacion>>>(ResourceUiState.Loading)
    private val _uiStateCarrerasConvalidacion = MutableStateFlow<ResourceUiState<List<ResponseCarrerasConvalidacion>>>(ResourceUiState.Loading)
    private val _uiStateCursosConvalidacion = MutableStateFlow<ResourceUiState<ResponseCursosConvalidacion>>(ResourceUiState.Loading)

    val uiState = _uiState.asStateFlow()
    val uiStateTipoTrasladoConvalidacion = _uiStateTipoTrasladoConvalidacion.asStateFlow()
    val uiStateEstudianteOAcadConvalidacion = _uiStateEstudianteOAcadConvalidacion.asStateFlow()
    val uiStateSedeConvalidacion = _uiStateSedeConvalidacion.asStateFlow()
    val uiStateCarrerasConvalidacion = _uiStateCarrerasConvalidacion.asStateFlow()
    val uiStateCursosConvalidacion = _uiStateCursosConvalidacion.asStateFlow()

    private lateinit var dataCarreraRequest: DataCarreraRequest
    private lateinit var tipoTrasladoRequest: TipoTrasladoRequest
    private lateinit var estudianteOAcadConvalidacionRequest: EstudianteOAcadConvalidacionRequest
    private lateinit var carrerasConvalidacionRequest: CarrerasConvalidacionRequest
    private lateinit var cursosConvalidacionRequest: CursosConvalidacionRequest

    fun setUserCarreraRequest(id_estud: Int) {
        dataCarreraRequest = DataCarreraRequest(id_estud)
        getUserCarreraData()
    }

    fun setTipoTrasladoRequest(id_uneg: Int) {
        tipoTrasladoRequest = TipoTrasladoRequest(id_uneg)
        getTipoTrasladoConvalidacion()
    }

    fun setEstudianteOAcadConvalidacionRequest(condicion: Int, id_uneg: Int, id_tipo_traslado: Int) {
        estudianteOAcadConvalidacionRequest = EstudianteOAcadConvalidacionRequest(condicion, id_uneg, id_tipo_traslado)
        getEstudianteOAcadConvalidacionData()
    }

    fun setSedeConvalidacionRequest(condicion: Int, id_uneg: Int, id_tipo_traslado: Int) {
        estudianteOAcadConvalidacionRequest = EstudianteOAcadConvalidacionRequest(condicion, id_uneg, id_tipo_traslado)
        getSedeConvalidacionData()
    }

    fun setCarrerasConvalidacionRequest(id_ofer_adm: Int, id_serv: Int) {
        carrerasConvalidacionRequest = CarrerasConvalidacionRequest(id_ofer_adm, id_serv)
        getCarrerasConvalidacionData()
    }

    fun setCursosConvalidacionRequest(id_estud_pe: Int, id_pest_det_destino: Int, id_tipo_traslado: Int) {
        cursosConvalidacionRequest = CursosConvalidacionRequest(id_estud_pe, id_pest_det_destino, id_tipo_traslado)
        getCursosConvalidacionData()
    }

    private fun getUserCarreraData() {
        viewModelScope.launch {
            try {
                val result = repo.getCarreraPlanEstudio(dataCarreraRequest)
                if (result.ListPlanEstudioConv.isEmpty()) {
                    _uiState.value = ResourceUiState.Empty
                } else {
                    _uiState.value = ResourceUiState.Success(result)
                }
            } catch (e: Exception) {
                _uiState.value = ResourceUiState.Error(e.message ?: "Ocurrió un error")
            }
        }
    }

    private fun getTipoTrasladoConvalidacion() {
        viewModelScope.launch {
            try {
                val result = repo.getTipoTrasladoConvalidacion(tipoTrasladoRequest)
                if (result.ListTipoTraslado.isEmpty()) {
                    _uiStateTipoTrasladoConvalidacion.value = ResourceUiState.Empty
                } else {
                    _uiStateTipoTrasladoConvalidacion.value = ResourceUiState.Success(result)
                }
            } catch (e: Exception) {
                _uiStateTipoTrasladoConvalidacion.value = ResourceUiState.Error(e.message ?: "Ocurrió un error")
            }
        }
    }

    private fun getEstudianteOAcadConvalidacionData() {
        viewModelScope.launch {
            _uiStateEstudianteOAcadConvalidacion.value = ResourceUiState.Loading
            try {
                val result = repo.getEstudianteOAcadConvalidacion(estudianteOAcadConvalidacionRequest)
                if (result.isEmpty()) {
                    _uiStateEstudianteOAcadConvalidacion.value = ResourceUiState.Empty
                } else {
                    _uiStateEstudianteOAcadConvalidacion.value = ResourceUiState.Success(result)
                }
            } catch (e: Exception) {
                _uiStateEstudianteOAcadConvalidacion.value = ResourceUiState.Error(e.message ?: "Ocurrió un error")
            }
        }
    }

    private fun getSedeConvalidacionData() {
        viewModelScope.launch {
            _uiStateSedeConvalidacion.value = ResourceUiState.Loading
            try {
                val result = repo.getEstudianteOAcadConvalidacion(estudianteOAcadConvalidacionRequest)
                if (result.isEmpty()) {
                    _uiStateSedeConvalidacion.value = ResourceUiState.Empty
                } else {
                    _uiStateSedeConvalidacion.value = ResourceUiState.Success(result)
                }
            } catch (e: Exception) {
                _uiStateSedeConvalidacion.value = ResourceUiState.Error(e.message ?: "Ocurrió un error")
            }
        }
    }

    private fun getCarrerasConvalidacionData() {
        viewModelScope.launch {
            try {
                val result = repo.getCarrerasConvalidacion(carrerasConvalidacionRequest)
                if (result.firstOrNull()?.ListCarrerasAcademica?.isEmpty() == true) {
                    _uiStateCarrerasConvalidacion.value = ResourceUiState.Empty
                } else {
                    _uiStateCarrerasConvalidacion.value = ResourceUiState.Success(result)
                }
            } catch (e: Exception) {
                _uiStateCarrerasConvalidacion.value = ResourceUiState.Error(e.message ?: "Ocurrió un error")
            }
        }
    }

    private fun getCursosConvalidacionData() {
        viewModelScope.launch {
            try {
                val result = repo.getCursosConvalidacion(cursosConvalidacionRequest)
                if (result.ListCursosAcademica.isEmpty()) {
                    _uiStateCursosConvalidacion.value = ResourceUiState.Empty
                } else {
                    _uiStateCursosConvalidacion.value = ResourceUiState.Success(result)
                }
            } catch (e: Exception) {
                _uiStateCursosConvalidacion.value = ResourceUiState.Error(e.message ?: "Ocurrió un error")
            }
        }
    }

    fun resetEstudianteOAcadConvalidacionState() {
        _uiStateEstudianteOAcadConvalidacion.value = ResourceUiState.Empty
    }

    fun resetSedeConvalidacionState() {
        _uiStateSedeConvalidacion.value = ResourceUiState.Empty
    }

    fun resetCarrerasConvalidacionState() {
        _uiStateCarrerasConvalidacion.value = ResourceUiState.Empty
    }

    fun resetCursosConvalidacionState() {
        _uiStateCursosConvalidacion.value = ResourceUiState.Empty
    }

    fun resetPlanEstudioState() {
        _uiState.value = ResourceUiState.Empty
    }
}
