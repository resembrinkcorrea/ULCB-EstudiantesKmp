package pe.lecordonbleu.universidadestudiante.presentation.screens.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import pe.lecordonbleu.universidadestudiante.data.remote.dto.ResponseLoginUser
import pe.lecordonbleu.universidadestudiante.domain.model.UserLoginRequest
import pe.lecordonbleu.universidadestudiante.domain.repository.Repository
import pe.lecordonbleu.universidadestudiante.presentation.vo.ResourceUiState

class LoginViewModel(private val repo: Repository) : ViewModel() {

    private val _uiState = MutableStateFlow<ResourceUiState<List<ResponseLoginUser>>>(ResourceUiState.Empty)
    val uiState = _uiState.asStateFlow()

    private lateinit var userLoginRequest: UserLoginRequest

    fun setUsuarioRequest(
        tipoLogin: Int,
        rstaLoginMicrosoft: Int,
        usuario: String,
        contrasena: String,
        sistema: Int,
        idUneg: Int,
        pc: String,
        ip: String,
        lastPwd: String
    ) {
        userLoginRequest = UserLoginRequest(
            tipoLogin = tipoLogin,
            rstaLoginMicrosoft = rstaLoginMicrosoft,
            usuario = usuario,
            contrasena = contrasena,
            sistema = sistema,
            idUneg = idUneg,
            pc = pc,
            ip = ip,
            lastPwd = lastPwd
        )
        login()
    }

    private fun login() {
        viewModelScope.launch {
            _uiState.value = ResourceUiState.Loading
            try {
                val result = repo.getDataUsuario(userLoginRequest)
                if (result.isEmpty()) {
                    _uiState.value = ResourceUiState.Error("Usuario o contraseña incorrecta")
                } else {
                    val first = result.firstOrNull()
                    if (first?.flag_val == 0) {
                        _uiState.value = ResourceUiState.Error("No se encuentran datos del sistema")
                    } else {
                        _uiState.value = ResourceUiState.Success(result)
                    }
                }
            } catch (e: Exception) {
                _uiState.value = ResourceUiState.Error(e.message ?: "Ocurrió un error")
            }
        }
    }

    fun resetUiState() {
        _uiState.value = ResourceUiState.Empty
    }
}
