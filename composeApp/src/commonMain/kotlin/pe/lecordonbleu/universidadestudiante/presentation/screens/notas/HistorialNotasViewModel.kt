package pe.lecordonbleu.universidadestudiante.presentation.screens.notas

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import pe.lecordonbleu.universidadestudiante.data.remote.dto.ResponseHistorialNotas
import pe.lecordonbleu.universidadestudiante.domain.model.HistorialNotasRequest
import pe.lecordonbleu.universidadestudiante.domain.repository.AppRepository
import pe.lecordonbleu.universidadestudiante.presentation.vo.ResourceUiState

class HistorialNotasViewModel(private val repo: AppRepository) : ViewModel() {

    private val _historialState = MutableStateFlow<ResourceUiState<ResponseHistorialNotas>>(ResourceUiState.Empty)
    val historialState = _historialState.asStateFlow()

    private lateinit var historialRequest: HistorialNotasRequest

    fun setHistorialRequest(idEstudPe: Int, idOacadArranque: Int) {
        historialRequest = HistorialNotasRequest(id_estud_pe = idEstudPe, id_oacad_arranque = idOacadArranque)
        getHistorialData()
    }

    private fun getHistorialData() {
        viewModelScope.launch {
            _historialState.value = ResourceUiState.Loading
            try {
                val result = repo.getHistorialNotas(historialRequest)
                _historialState.value = if (result.flag_val == 1) ResourceUiState.Success(result)
                else ResourceUiState.Error("No se encontró historial de notas.")
            } catch (e: Exception) {
                _historialState.value = ResourceUiState.Error(e.message ?: "Error de conexión")
            }
        }
    }

    fun resetUiState() {
        _historialState.value = ResourceUiState.Empty
    }
}
