package pe.lecordonbleu.universidadestudiante.presentation.screens.mallacurricular

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import pe.lecordonbleu.universidadestudiante.data.remote.dto.ResponseCarreraRemote
import pe.lecordonbleu.universidadestudiante.data.remote.dto.ResponsePlanEstudio
import pe.lecordonbleu.universidadestudiante.data.remote.dto.ResponseTablaPlan
import pe.lecordonbleu.universidadestudiante.data.remote.dto.ResponseValoresPlan
import pe.lecordonbleu.universidadestudiante.domain.model.DataCarreraRequest
import pe.lecordonbleu.universidadestudiante.domain.model.GenerarPdfMallaRequest
import pe.lecordonbleu.universidadestudiante.domain.model.PlanEstudioRequest
import pe.lecordonbleu.universidadestudiante.domain.model.TablaPlanRequest
import pe.lecordonbleu.universidadestudiante.domain.model.ValoresPlanRequest
import pe.lecordonbleu.universidadestudiante.domain.repository.Repository
import pe.lecordonbleu.universidadestudiante.presentation.vo.ResourceUiState

class MallaCurricularViewModel(private val repo: Repository) : ViewModel() {

    private val _uiState = MutableStateFlow<ResourceUiState<List<ResponseCarreraRemote>>>(
        ResourceUiState.Loading)

    private val _planEstudioState = MutableStateFlow<ResourceUiState<List<ResponsePlanEstudio>>>(
        ResourceUiState.Loading)

    private val _valoresPlanState = MutableStateFlow<ResourceUiState<List<ResponseValoresPlan>>>(
        ResourceUiState.Loading
    )

    private val _tablaPlanState = MutableStateFlow<ResourceUiState<List<ResponseTablaPlan>>>(
        ResourceUiState.Loading
    )

    private val _pdfMallaState = MutableStateFlow<ResourceUiState<ByteArray>>(ResourceUiState.Loading)

    val uiState = _uiState.asStateFlow()
    val planEstudioState = _planEstudioState.asStateFlow()
    val valoresPlanState = _valoresPlanState.asStateFlow()
    val tablaPlanState = _tablaPlanState.asStateFlow()
    val pdfMallaState = _pdfMallaState.asStateFlow()

    private lateinit var dataCarreraRequest: DataCarreraRequest
    private lateinit var planEstudioRequest: PlanEstudioRequest
    private lateinit var valoresPlanRequest: ValoresPlanRequest
    private lateinit var tablaPlanRequest: TablaPlanRequest
    private lateinit var generarPdfRequest: GenerarPdfMallaRequest

    fun setUserCarreraRequest(id_stud: Int) {
        dataCarreraRequest = DataCarreraRequest(id_stud)
        getUserCarreraData()
    }

    fun setPlanEstudioRequest(idEstudServ: Int) {
        planEstudioRequest = PlanEstudioRequest(id_estud_serv = idEstudServ)
        getPlanEstudioData()
    }

    fun setValoresPlanRequest(
        id_estud_pe: Int,
        id_pest_det: Int,
        id_serv: Int,
        id_uneg: Int
    ) {
        valoresPlanRequest = ValoresPlanRequest(id_estud_pe, id_pest_det, id_serv, id_uneg)
        getValoresPlanData()
    }

    fun setTablaPlanRequest(
        id_estud_pe: Int,
        id_pest_det: Int,
        id_serv: Int,
        id_uneg: Int,
        id_estud: Int
    ) {
        tablaPlanRequest = TablaPlanRequest(id_estud_pe, id_pest_det, id_serv, id_uneg, id_estud)
        getTablaPlanData()
    }

    fun setGenerarPdfRequest(
        id_estud_pe: Int,
        id_pest_det: Int,
        id_serv: Int,
        id_uneg: Int,
        id_estud: Int
    ) {
        generarPdfRequest = GenerarPdfMallaRequest(id_estud_pe, id_pest_det, id_serv, id_uneg, id_estud)
        getGenerarPdfData()
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
                _uiState.value = ResourceUiState.Error(e.message ?: "Ocurrió un error")
            }
        }
    }

    private fun getPlanEstudioData() {
        viewModelScope.launch {
            try {
                val result = repo.getPlanEstudioMalla(planEstudioRequest)
                if (result.isEmpty()) {
                    _planEstudioState.value = ResourceUiState.Error("No se encontró el plan de estudio")
                } else {
                    _planEstudioState.value = ResourceUiState.Success(result)
                }
            } catch (e: Exception) {
                _planEstudioState.value = ResourceUiState.Error(e.message ?: "Ocurrió un error")
            }
        }
    }

    private fun getValoresPlanData() {
        viewModelScope.launch {
            try {
                val result = repo.getResumenValoresPlan(valoresPlanRequest)
                _valoresPlanState.value = if (result.isEmpty()) {
                    ResourceUiState.Error("No se pudo obtener el resumen del plan.")
                } else {
                    ResourceUiState.Success(result)
                }
            } catch (e: Exception) {
                _valoresPlanState.value = ResourceUiState.Error(e.message ?: "Error al cargar resumen.")
            }
        }
    }

    private fun getTablaPlanData() {
        viewModelScope.launch {
            try {
                val result = repo.getTablaPlanEstudio(tablaPlanRequest)
                if (result.isEmpty()) {
                    _tablaPlanState.value = ResourceUiState.Error("No hay asignaturas para mostrar.")
                } else {
                    _tablaPlanState.value = ResourceUiState.Success(result)
                }
            } catch (e: Exception) {
                _tablaPlanState.value = ResourceUiState.Error(e.message ?: "Error al cargar asignaturas.")
            }
        }
    }

    private fun getGenerarPdfData() {
        viewModelScope.launch {
            _pdfMallaState.value = ResourceUiState.Loading
            try {
                val result = repo.generarPdfMalla(generarPdfRequest)
                if (result.isNotEmpty()) {
                    _pdfMallaState.value = ResourceUiState.Success(result)
                } else {
                    _pdfMallaState.value = ResourceUiState.Error("No se pudo generar el PDF.")
                }
            } catch (e: Exception) {
                _pdfMallaState.value = ResourceUiState.Error(e.message ?: "Error desconocido al generar el PDF.")
            }
        }
    }
}
