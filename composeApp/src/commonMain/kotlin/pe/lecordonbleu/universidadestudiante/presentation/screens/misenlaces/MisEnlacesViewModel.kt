package pe.lecordonbleu.universidadestudiante.presentation.screens.misenlaces

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import pe.lecordonbleu.universidadestudiante.data.remote.dto.ResponseLinksInstitucional
import pe.lecordonbleu.universidadestudiante.domain.model.LinksItemRequest
import pe.lecordonbleu.universidadestudiante.domain.repository.AppRepository
import pe.lecordonbleu.universidadestudiante.presentation.vo.ResourceUiState

class MisEnlacesViewModel(private val repo: AppRepository) : ViewModel() {

    private val _uiState = MutableStateFlow<ResourceUiState<ResponseLinksInstitucional>>(ResourceUiState.Loading)
    val uiState = _uiState.asStateFlow()

    private lateinit var request: LinksItemRequest

    fun setRequest(idUneg: Int, idSistema: Int) {
        request = LinksItemRequest(idUneg, idSistema)
        getLinksInstitucional()
    }

    private fun getLinksInstitucional() {
        viewModelScope.launch {
            try {
                val result = repo.getLinksInstitucional(request)
                _uiState.value = ResourceUiState.Success(result)
            } catch (e: Exception) {
                _uiState.value = ResourceUiState.Error(e.message ?: "Error al obtener enlaces")
            }
        }
    }

    fun resetUiState() {
        _uiState.value = ResourceUiState.Empty
    }
}
