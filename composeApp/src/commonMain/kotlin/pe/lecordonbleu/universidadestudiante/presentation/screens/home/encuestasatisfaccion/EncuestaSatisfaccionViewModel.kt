package pe.lecordonbleu.universidadestudiante.presentation.screens.home.encuestasatisfaccion

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import pe.lecordonbleu.universidadestudiante.data.remote.dto.ResponseEncuestaSatisfaccion
import pe.lecordonbleu.universidadestudiante.data.remote.dto.ResponseGuardarEncuestaSatisfaccion
import pe.lecordonbleu.universidadestudiante.domain.model.EncuestaSatisfaccionGuardarRequest
import pe.lecordonbleu.universidadestudiante.domain.model.EncuestaSatisfaccionRequest
import pe.lecordonbleu.universidadestudiante.domain.repository.AppRepository
import pe.lecordonbleu.universidadestudiante.presentation.vo.ResourceUiState

class EncuestaSatisfaccionViewModel(private val repo: AppRepository) : ViewModel() {

    private val _uiStateEncuesta =
        MutableStateFlow<ResourceUiState<ResponseEncuestaSatisfaccion>>(ResourceUiState.Loading)
    private val _uiStateGuardar =
        MutableStateFlow<ResourceUiState<ResponseGuardarEncuestaSatisfaccion>>(ResourceUiState.Empty)

    val uiStateEncuesta = _uiStateEncuesta.asStateFlow()
    val uiStateGuardar = _uiStateGuardar.asStateFlow()

    private var iniciado = false

    fun initIfNeeded(idPeracad: Int, idEstudPe: Int, idServ: Int, idOacadArranque: Int) {
        if (iniciado) return
        iniciado = true
        viewModelScope.launch {
            _uiStateEncuesta.value = ResourceUiState.Loading
            try {
                val result = repo.getEncuestaSatisfaccion(
                    EncuestaSatisfaccionRequest(
                        id_estud_pe = idEstudPe,
                        id_peracad = idPeracad,
                        id_serv = idServ,
                        id_oacad_arranque = idOacadArranque
                    )
                )
                if (result.EstadoEncuesta.isEmpty()) {
                    _uiStateEncuesta.value = ResourceUiState.Error("Sin resultados")
                } else {
                    _uiStateEncuesta.value = ResourceUiState.Success(result)
                }
            } catch (e: Exception) {
                _uiStateEncuesta.value = ResourceUiState.Error(e.message ?: "Ocurrió un error")
            }
        }
    }

    fun guardar(request: EncuestaSatisfaccionGuardarRequest) {
        viewModelScope.launch {
            _uiStateGuardar.value = ResourceUiState.Loading
            try {
                val result = repo.guardarEncuestaSatisfaccion(request)
                _uiStateGuardar.value = ResourceUiState.Success(result)
            } catch (e: Exception) {
                _uiStateGuardar.value = ResourceUiState.Error(e.message ?: "Error al guardar")
            }
        }
    }

    fun resetGuardarState() {
        _uiStateGuardar.value = ResourceUiState.Empty
    }
}
