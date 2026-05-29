package pe.lecordonbleu.universidadestudiante.presentation.screens.historialacademico

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import pe.lecordonbleu.universidadestudiante.data.remote.dto.ResponseHistorialAcademicoAlumnoDetalle
import pe.lecordonbleu.universidadestudiante.domain.model.HistorialAcademicoAlumnoDetalleRequest
import pe.lecordonbleu.universidadestudiante.domain.repository.AppRepository
import pe.lecordonbleu.universidadestudiante.presentation.vo.ResourceUiState

class HistorialAcademicoDetalleViewModel(private val repo: AppRepository) : ViewModel() {

    private val _detalleState = MutableStateFlow<ResourceUiState<List<ResponseHistorialAcademicoAlumnoDetalle>>>(ResourceUiState.Empty)
    val detalleState = _detalleState.asStateFlow()

    private lateinit var detalleRequest: HistorialAcademicoAlumnoDetalleRequest

    fun setDetalleRequest(id_estud_pe: Int, id_peracad: Int) {
        detalleRequest = HistorialAcademicoAlumnoDetalleRequest(id_estud_pe, id_peracad)
        getDetalleData()
    }

    private fun getDetalleData() {
        viewModelScope.launch {
            _detalleState.value = ResourceUiState.Loading
            try {
                val result = repo.getHistorialAcademicoAlumnoDetalle(detalleRequest)
                _detalleState.value = if (result.isEmpty() || result.first().listado_detacad.isEmpty()) {
                    ResourceUiState.Error("Sin resultados")
                } else {
                    ResourceUiState.Success(result)
                }
            } catch (e: Exception) {
                _detalleState.value = ResourceUiState.Error(e.message ?: "Ocurrio un error")
            }
        }
    }

    fun resetUiState() {
        _detalleState.value = ResourceUiState.Empty
    }
}
