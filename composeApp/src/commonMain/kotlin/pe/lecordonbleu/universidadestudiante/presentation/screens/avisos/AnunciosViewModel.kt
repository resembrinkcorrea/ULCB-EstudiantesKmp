package pe.lecordonbleu.universidadestudiante.presentation.screens.avisos

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import pe.lecordonbleu.universidadestudiante.data.remote.dto.ResponseAnuncios
import pe.lecordonbleu.universidadestudiante.data.remote.dto.listHoraServer
import pe.lecordonbleu.universidadestudiante.domain.model.AnunciosRequest
import pe.lecordonbleu.universidadestudiante.domain.repository.Repository
import pe.lecordonbleu.universidadestudiante.presentation.vo.ResourceUiState

class AnunciosViewModel(private val repo: Repository) : ViewModel() {

    private val _uiStateAnuncios = MutableStateFlow<ResourceUiState<ResponseAnuncios>>(ResourceUiState.Empty)
    val uiStateAnuncios = _uiStateAnuncios.asStateFlow()

    private val _uiStateHora = MutableStateFlow<ResourceUiState<listHoraServer>>(ResourceUiState.Empty)
    val uiStateHora = _uiStateHora.asStateFlow()

    private lateinit var anunciosRequest: AnunciosRequest

    fun setAnunciosRequest(id_uneg: Int, id_sistema: Int, id_usuario: Int) {
        anunciosRequest = AnunciosRequest(
            id_uneg = id_uneg,
            id_sistema = id_sistema,
            id_usuario = id_usuario
        )
        getAnunciosData()
    }

    fun setHoraServidorRequest() {
        getHoraServidorData()
    }

    fun resetAnunciosState() {
        _uiStateAnuncios.value = ResourceUiState.Empty
    }

    fun resetHoraServidorState() {
        _uiStateHora.value = ResourceUiState.Empty
    }

    private fun getAnunciosData() {
        viewModelScope.launch {
            _uiStateAnuncios.value = ResourceUiState.Loading
            try {
                val result = repo.getAnuncios(anunciosRequest)
                _uiStateAnuncios.value = if (result.data_notificaciones.isEmpty()) {
                    ResourceUiState.Empty
                } else {
                    ResourceUiState.Success(result)
                }
            } catch (e: Exception) {
                _uiStateAnuncios.value = ResourceUiState.Error(e.message ?: "Ocurrió un error")
            }
        }
    }

    private fun getHoraServidorData() {
        viewModelScope.launch {
            _uiStateHora.value = ResourceUiState.Loading
            try {
                val result = repo.getHoraServidor()
                val horaServer = result.listHoraServer.firstOrNull()
                if (horaServer != null) {
                    _uiStateHora.value = ResourceUiState.Success(horaServer)
                } else {
                    _uiStateHora.value = ResourceUiState.Empty
                }
            } catch (e: Exception) {
                _uiStateHora.value = ResourceUiState.Error(e.message ?: "Ocurrió un error")
            }
        }
    }
}
