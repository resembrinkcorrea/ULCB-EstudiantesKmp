package pe.lecordonbleu.universidadestudiante.presentation.screens.historialacademico

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import pe.lecordonbleu.universidadestudiante.data.remote.dto.ResponseHistorialAcademicoAlumno
import pe.lecordonbleu.universidadestudiante.domain.model.HistorialAcademicoAlumnoRequest
import pe.lecordonbleu.universidadestudiante.domain.repository.AppRepository
import pe.lecordonbleu.universidadestudiante.presentation.vo.ResourceUiState

class HistorialAcademicoViewModel(private val repo: AppRepository) : ViewModel() {

    private val _historialState = MutableStateFlow<ResourceUiState<List<ResponseHistorialAcademicoAlumno>>>(ResourceUiState.Empty)
    val historialState = _historialState.asStateFlow()

    private lateinit var historialRequest: HistorialAcademicoAlumnoRequest

    fun setHistorialRequest(id_estud_serv: String, id_estud: String, ped_estado_reg: String) {
        historialRequest = HistorialAcademicoAlumnoRequest(id_estud_serv, id_estud, ped_estado_reg)
        getHistorialData()
    }

    private fun getHistorialData() {
        viewModelScope.launch {
            _historialState.value = ResourceUiState.Loading
            try {
                val result = repo.getHistorialAcademicoAlumno(historialRequest)
                _historialState.value = if (result.isEmpty() || result.first().data_hist_acad.isEmpty()) {
                    ResourceUiState.Error("Sin resultados")
                } else {
                    ResourceUiState.Success(result)
                }
            } catch (e: Exception) {
                _historialState.value = ResourceUiState.Error(e.message ?: "Ocurrio un error")
            }
        }
    }

    fun resetUiState() {
        _historialState.value = ResourceUiState.Empty
    }
}
