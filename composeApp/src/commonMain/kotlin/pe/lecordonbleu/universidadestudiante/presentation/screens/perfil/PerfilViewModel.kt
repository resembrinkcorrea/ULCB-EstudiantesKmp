package pe.lecordonbleu.universidadestudiante.presentation.screens.perfil

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import pe.lecordonbleu.universidadestudiante.data.remote.dto.ResponsePerfilEstudiante
import pe.lecordonbleu.universidadestudiante.domain.model.DataPerfilRequest
import pe.lecordonbleu.universidadestudiante.domain.repository.AppRepository
import pe.lecordonbleu.universidadestudiante.presentation.vo.ResourceUiState

class PerfilViewModel(private val repo: AppRepository) : ViewModel() {

    private val _uiStatePerfil = MutableStateFlow<ResourceUiState<List<ResponsePerfilEstudiante>>>(
        ResourceUiState.Loading
    )

    val uiStatePerfil = _uiStatePerfil.asStateFlow()

    private lateinit var dataPerfilRequest: DataPerfilRequest

    fun setPerfilRequest(id_usuario: Int) {
        dataPerfilRequest = DataPerfilRequest(id_usuario)
        getPerfilData()
    }

    private fun getPerfilData() {
        viewModelScope.launch {
            try {
                val result = repo.getPerfilEstudiante(dataPerfilRequest)
                if (result.firstOrNull()?.ListPerfilEstudiante?.isEmpty() == true) {
                    _uiStatePerfil.value = ResourceUiState.Error("Sin resultados")
                } else {
                    _uiStatePerfil.value = ResourceUiState.Success(result)
                }
            } catch (e: Exception) {
                _uiStatePerfil.value = ResourceUiState.Error(e.message ?: "Ocurrió un error")
            }
        }
    }
}
