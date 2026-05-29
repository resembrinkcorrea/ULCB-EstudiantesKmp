package pe.lecordonbleu.universidadestudiante.presentation.screens.biblioteca

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import pe.lecordonbleu.universidadestudiante.data.remote.dto.ResponseBiblioteca
import pe.lecordonbleu.universidadestudiante.domain.model.BibliotecaRequest
import pe.lecordonbleu.universidadestudiante.domain.repository.AppRepository
import pe.lecordonbleu.universidadestudiante.presentation.vo.ResourceUiState

class BibliotecaViewModel(private val repo: AppRepository) : ViewModel() {

    private val _uiState = MutableStateFlow<ResourceUiState<ResponseBiblioteca>>(ResourceUiState.Empty)
    val uiState = _uiState.asStateFlow()

    private lateinit var bibliotecaRequest: BibliotecaRequest

    fun setRequest(uneg: Int, idTipoUsuario: Int) {
        bibliotecaRequest = BibliotecaRequest(uneg = uneg, id_tipo_usuario = idTipoUsuario)
        getBibliotecaData()
    }

    private fun getBibliotecaData() {
        viewModelScope.launch {
            _uiState.value = ResourceUiState.Loading
            try {
                val result = repo.getBiblioteca(bibliotecaRequest)
                _uiState.value = if (result.flag_val != 1 || result.listadoBiblioteca.isEmpty()) {
                    ResourceUiState.Error("No se encontraron recursos de biblioteca")
                } else {
                    ResourceUiState.Success(result)
                }
            } catch (e: Exception) {
                _uiState.value = ResourceUiState.Error(e.message ?: "Error al cargar la biblioteca")
            }
        }
    }

    fun resetUiState() {
        _uiState.value = ResourceUiState.Empty
    }
}
