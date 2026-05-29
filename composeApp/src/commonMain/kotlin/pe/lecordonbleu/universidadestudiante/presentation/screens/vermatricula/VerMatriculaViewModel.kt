package pe.lecordonbleu.universidadestudiante.presentation.screens.vermatricula

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import pe.lecordonbleu.universidadestudiante.data.remote.dto.ResponseCarrera
import pe.lecordonbleu.universidadestudiante.data.remote.dto.ResponseVerMatricula
import pe.lecordonbleu.universidadestudiante.data.remote.dto.ResponseDetalleMatricula
import pe.lecordonbleu.universidadestudiante.data.remote.dto.ResponseResumenHistorico
import pe.lecordonbleu.universidadestudiante.data.remote.dto.ResponseProyeccionValidacion
import pe.lecordonbleu.universidadestudiante.domain.model.CarreraRequest
import pe.lecordonbleu.universidadestudiante.domain.model.VerMatriculaRequest
import pe.lecordonbleu.universidadestudiante.domain.model.DetalleMatriculaRequest
import pe.lecordonbleu.universidadestudiante.domain.model.ResumenHistoricoRequest
import pe.lecordonbleu.universidadestudiante.domain.model.ValidaProyeccionRequest
import pe.lecordonbleu.universidadestudiante.domain.repository.AppRepository
import pe.lecordonbleu.universidadestudiante.presentation.vo.ResourceUiState

class VerMatriculaViewModel(private val repo: AppRepository) : ViewModel() {

    private val _uiStateProyeccion = MutableStateFlow<ResourceUiState<ResponseProyeccionValidacion>>(ResourceUiState.Empty)
    private val _uiStateCarrera = MutableStateFlow<ResourceUiState<ResponseCarrera>>(ResourceUiState.Empty)
    private val _uiStateVerMatricula = MutableStateFlow<ResourceUiState<ResponseVerMatricula>>(ResourceUiState.Empty)
    private val _uiStateDetalleMatricula = MutableStateFlow<ResourceUiState<ResponseDetalleMatricula>>(ResourceUiState.Empty)
    private val _uiStateResumenHistorico = MutableStateFlow<ResourceUiState<ResponseResumenHistorico>>(ResourceUiState.Empty)

    val uiStateProyeccion = _uiStateProyeccion.asStateFlow()
    val uiStateCarrera = _uiStateCarrera.asStateFlow()
    val uiStateVerMatricula = _uiStateVerMatricula.asStateFlow()
    val uiStateDetalleMatricula = _uiStateDetalleMatricula.asStateFlow()
    val uiStateResumenHistorico = _uiStateResumenHistorico.asStateFlow()

    private lateinit var carreraRequest: CarreraRequest
    private lateinit var verMatriculaRequest: VerMatriculaRequest
    private lateinit var detalleMatriculaRequest: DetalleMatriculaRequest
    private lateinit var resumenHistoricoRequest: ResumenHistoricoRequest

    fun setProyeccion(idEstud: Int) {
        viewModelScope.launch {
            _uiStateProyeccion.value = ResourceUiState.Loading
            _uiStateProyeccion.value = try {
                ResourceUiState.Success(repo.getProyeccionValidacion(ValidaProyeccionRequest(idEstud)))
            } catch (e: Exception) {
                ResourceUiState.Error(e.message ?: "Error al obtener proyeccion")
            }
        }
    }

    fun setCarrera(idEstud: Int) {
        carreraRequest = CarreraRequest(idEstud)
        fetchCarrera()
    }

    fun setVerMatricula(
        idPeriodoAcad: Int,
        idServicio: Int,
        idPlanEstudioDet: Int,
        idEstudiante: Int,
        id_sistema: Int,
        uneg: Int,
        id_usuario: Int
    ) {
        verMatriculaRequest = VerMatriculaRequest(idPeriodoAcad, idServicio, idPlanEstudioDet, idEstudiante, id_sistema, uneg, id_usuario)
        fetchVerMatricula()
    }

    fun setDetalleMatricula(
        idOfertaAcadDet: Int,
        id_asign_det_cr: String,
        id_hora_dia: String,
        id_dia_semana: String,
        hora_ini_cr: String,
        hora_fin_cr: String
    ) {
        detalleMatriculaRequest = DetalleMatriculaRequest(idOfertaAcadDet, id_asign_det_cr, id_hora_dia, id_dia_semana, hora_ini_cr, hora_fin_cr)
        fetchDetalleMatricula()
    }

    private fun fetchCarrera() {
        viewModelScope.launch {
            _uiStateCarrera.value = ResourceUiState.Loading
            _uiStateCarrera.value = try {
                ResourceUiState.Success(repo.getCarrera(carreraRequest))
            } catch (e: Exception) {
                ResourceUiState.Error(e.message ?: "Error al obtener carreras")
            }
        }
    }

    private fun fetchVerMatricula() {
        viewModelScope.launch {
            _uiStateVerMatricula.value = ResourceUiState.Loading
            _uiStateVerMatricula.value = try {
                ResourceUiState.Success(repo.getVerMatricula(verMatriculaRequest))
            } catch (e: Exception) {
                ResourceUiState.Error(e.message ?: "Error al obtener matricula")
            }
        }
    }

    private fun fetchDetalleMatricula() {
        viewModelScope.launch {
            _uiStateDetalleMatricula.value = ResourceUiState.Loading
            _uiStateDetalleMatricula.value = try {
                ResourceUiState.Success(repo.getDetalleMatricula(detalleMatriculaRequest))
            } catch (e: Exception) {
                ResourceUiState.Error(e.message ?: "Error al obtener detalle")
            }
        }
    }

    fun setResumenHistorico(idEstudPe: Int, idPeriodoAcadVal: Int) {
        resumenHistoricoRequest = ResumenHistoricoRequest(idEstudPe, idPeriodoAcadVal)
        fetchResumenHistorico()
    }

    private fun fetchResumenHistorico() {
        viewModelScope.launch {
            _uiStateResumenHistorico.value = ResourceUiState.Loading
            _uiStateResumenHistorico.value = try {
                ResourceUiState.Success(repo.getResumenHistorico(resumenHistoricoRequest))
            } catch (e: Exception) {
                ResourceUiState.Error(e.message ?: "Error al obtener resumen historico")
            }
        }
    }

    fun resetProyeccionState() { _uiStateProyeccion.value = ResourceUiState.Empty }
    fun resetCarreraState() { _uiStateCarrera.value = ResourceUiState.Empty }
    fun resetVerMatriculaState() { _uiStateVerMatricula.value = ResourceUiState.Empty }
    fun resetDetalleMatriculaState() { _uiStateDetalleMatricula.value = ResourceUiState.Empty }
    fun resetResumenHistoricoState() { _uiStateResumenHistorico.value = ResourceUiState.Empty }
}
