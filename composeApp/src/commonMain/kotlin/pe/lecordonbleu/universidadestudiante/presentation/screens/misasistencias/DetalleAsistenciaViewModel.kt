package pe.lecordonbleu.universidadestudiante.presentation.screens.misasistencias

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import pe.lecordonbleu.universidadestudiante.data.remote.dto.ResponseAsistencia
import pe.lecordonbleu.universidadestudiante.domain.model.DetalleAsistenciaRequest
import pe.lecordonbleu.universidadestudiante.domain.repository.AppRepository
import pe.lecordonbleu.universidadestudiante.presentation.vo.ResourceUiState

class DetalleAsistenciaViewModel(private val repo: AppRepository) : ViewModel() {

    private val _asistenciaState = MutableStateFlow<ResourceUiState<ResponseAsistencia>>(ResourceUiState.Empty)
    val asistenciaState = _asistenciaState.asStateFlow()

    private lateinit var detalleRequest: DetalleAsistenciaRequest

    fun setDetalleRequest(idEstudPe: Int, idMatricAsigSecc: Int) {
        detalleRequest = DetalleAsistenciaRequest(id_estud_pe = idEstudPe, id_matric_asig_secc = idMatricAsigSecc)
        getDetalleData()
    }

    private fun getDetalleData() {
        viewModelScope.launch {
            _asistenciaState.value = ResourceUiState.Loading
            try {
                val result = repo.getDetalleAsistencia(detalleRequest)
                _asistenciaState.value = if (result.listadoCarrera.isEmpty()) {
                    ResourceUiState.Error("No se encontró información de asistencia.")
                } else {
                    ResourceUiState.Success(result)
                }
            } catch (e: Exception) {
                _asistenciaState.value = ResourceUiState.Error(e.message ?: "Ocurrió un error")
            }
        }
    }

    fun resetUiState() {
        _asistenciaState.value = ResourceUiState.Empty
    }
}
