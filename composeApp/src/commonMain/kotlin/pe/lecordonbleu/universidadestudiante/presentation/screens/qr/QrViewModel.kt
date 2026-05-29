package pe.lecordonbleu.universidadestudiante.presentation.screens.qr

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import pe.lecordonbleu.universidadestudiante.data.remote.dto.ResponseQr
import pe.lecordonbleu.universidadestudiante.domain.model.QrEntity
import pe.lecordonbleu.universidadestudiante.domain.repository.AppRepository
import pe.lecordonbleu.universidadestudiante.presentation.vo.ResourceUiState


class QrViewModel(private val repo: AppRepository) : ViewModel() {

    private val _uiState = MutableStateFlow<ResourceUiState<List<ResponseQr>>>(ResourceUiState.Empty)
    val uiState = _uiState.asStateFlow()

    private lateinit var userQrRequest: QrEntity

    fun setQRequest(id_estud: Int) {
        userQrRequest = QrEntity(id_estud = id_estud)
        getQrUsuarioData()
    }

    fun getQrUsuarioData() {
        viewModelScope.launch {
            _uiState.value = ResourceUiState.Loading
            try {
                val users = repo.getQrUsuario(userQrRequest)
                if (users.isEmpty()) {
                    _uiState.value = ResourceUiState.Error("QR incorrecto")
                } else {
                    val firstUser = users.firstOrNull()
                    if (firstUser?.flag == 0) {
                        _uiState.value = ResourceUiState.Error(firstUser.mensaje)
                    } else {
                        _uiState.value = ResourceUiState.Success(users)
                    }
                }
            } catch (e: Exception) {
                _uiState.value = ResourceUiState.Error(e.message ?: "Ocurrió un error QR")
            }
        }
    }

    fun resetUiState() {
        _uiState.value = ResourceUiState.Empty
    }
}
