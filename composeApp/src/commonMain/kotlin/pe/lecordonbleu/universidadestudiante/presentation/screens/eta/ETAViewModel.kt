package pe.lecordonbleu.universidadestudiante.presentation.screens.eta

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import pe.lecordonbleu.universidadestudiante.data.remote.dto.ResponseCarreraRemote
import pe.lecordonbleu.universidadestudiante.data.remote.dto.ResponseDocumentoEta
import pe.lecordonbleu.universidadestudiante.data.remote.dto.ResponseEliminarDocEta
import pe.lecordonbleu.universidadestudiante.data.remote.dto.ResponseGuardarEta
import pe.lecordonbleu.universidadestudiante.data.remote.dto.ResponsePeriodoEta
import pe.lecordonbleu.universidadestudiante.domain.model.DataCarreraRequest
import pe.lecordonbleu.universidadestudiante.domain.model.DataGuardarRequest
import pe.lecordonbleu.universidadestudiante.domain.model.DocumentosEtaRequest
import pe.lecordonbleu.universidadestudiante.domain.model.EliminarDocEtaRequest
import pe.lecordonbleu.universidadestudiante.domain.model.PeriodoEtaRequest
import pe.lecordonbleu.universidadestudiante.domain.repository.Repository
import pe.lecordonbleu.universidadestudiante.presentation.vo.ResourceUiState


class ETAViewModel(private val repo: Repository) : ViewModel() {

    private val _uiState = MutableStateFlow<ResourceUiState<List<ResponseCarreraRemote>>>(
        ResourceUiState.Loading)
    private val _uiStatePeriodo = MutableStateFlow<ResourceUiState<List<ResponsePeriodoEta>>>(
        ResourceUiState.Loading)
    private val _uiStateDocumentosEta = MutableStateFlow<ResourceUiState<List<ResponseDocumentoEta>>>(
        ResourceUiState.Loading)
    private val _uiStateGuardarEta = MutableStateFlow<ResourceUiState<List<ResponseGuardarEta>>>(
        ResourceUiState.Loading)
    private val _uiStateEliminarDocEta = MutableStateFlow<ResourceUiState<List<ResponseEliminarDocEta>>>(
        ResourceUiState.Loading)

    val uiState = _uiState.asStateFlow()
    val uiStatePeriodo = _uiStatePeriodo.asStateFlow()
    val uiStateDocumentosEta = _uiStateDocumentosEta.asStateFlow()
    val uiStateGuardarEta = _uiStateGuardarEta.asStateFlow()
    val uiStateEliminarDocEta = _uiStateEliminarDocEta.asStateFlow()

    private lateinit var dataCarreraRequest: DataCarreraRequest
    private lateinit var periodoEtaRequest: PeriodoEtaRequest
    private lateinit var documentosEtaRequest: DocumentosEtaRequest
    private lateinit var dataGuardarRequest: DataGuardarRequest
    private lateinit var eliminarDocEtaRequest: EliminarDocEtaRequest

    fun setUserCarreraRequest(id_stud: Int) {
        dataCarreraRequest = DataCarreraRequest(id_stud)
        getUserCarreraData()
    }

    fun setEtaPeriodoRequest(id_pest_det: Int, id_serv: Int) {
        periodoEtaRequest = PeriodoEtaRequest(id_pest_det, id_serv)
        getPeriodoEtaData()
    }

    fun setEliminarDocEtaRequest(id_pcs_estud_exam: Int) {
        eliminarDocEtaRequest = EliminarDocEtaRequest(id_pcs_estud_exam)
        getEliminarDocEtaData()
    }

    private fun getEliminarDocEtaData() {
        viewModelScope.launch {
            try {
                val response = repo.getEliminarDocEta(eliminarDocEtaRequest)
                if (response.firstOrNull()?.EliminarDocuEtaRes?.isEmpty() == true) {
                    _uiStateEliminarDocEta.value = ResourceUiState.Error("No hay documentos")
                } else {
                    _uiStateEliminarDocEta.value = ResourceUiState.Success(response)
                }
            } catch (e: Exception) {
                _uiStateEliminarDocEta.value = ResourceUiState.Error(e.message ?: "Ocurrió un error")
            }
        }
    }

    fun setDocumentosEtaRequest(
        id_pest_det: Int,
        id_uneg: Int,
        id_oacad_arranque: Int,
        id_estud_serv: Int,
        id_oaa_pcs: Int
    ) {
        documentosEtaRequest = DocumentosEtaRequest(id_pest_det, id_uneg, id_oacad_arranque, id_estud_serv, id_oaa_pcs)
        getDocumentosEtaData()
    }

    fun setGuardarEtaRequest(id_uneg: Int, pdfbase64: String, id_estud: String, id_user: String, id_pcs_estud: String, id_pcs_docu: String, nombreDocAbrev: String, pcs_estud_nombre: String, id_pcs_estud_exam: String, id_sistema: String) {
        dataGuardarRequest = DataGuardarRequest(id_uneg, pdfbase64, id_estud, id_user, id_pcs_estud, id_pcs_docu, nombreDocAbrev, pcs_estud_nombre, id_pcs_estud_exam, id_sistema)
        getGuardarEtaData()
    }

    private fun getDocumentosEtaData() {
        viewModelScope.launch {
            try {
                val response = repo.getDocumentosEta(documentosEtaRequest)
                if (response.firstOrNull()?.ListDocumentosEta?.isEmpty() == true) {
                    _uiStateDocumentosEta.value = ResourceUiState.Error("No hay documentos")
                } else {
                    _uiStateDocumentosEta.value = ResourceUiState.Success(response)
                }
            } catch (e: Exception) {
                _uiStateDocumentosEta.value = ResourceUiState.Error(e.message ?: "Ocurrió un error")
            }
        }
    }

    private fun getUserCarreraData() {
        viewModelScope.launch {
            _uiStateGuardarEta.value = ResourceUiState.Loading

            try {
                val result = repo.getAsistenciaCarrera(dataCarreraRequest)
                if (result.isEmpty()) {
                    _uiState.value = ResourceUiState.Error("Carrera incorrecta")
                } else {
                    _uiState.value = ResourceUiState.Success(result)
                }
            } catch (e: Exception) {
                _uiState.value = ResourceUiState.Error(e.message ?: "Ocurrió un error")
            }
        }
    }

    private fun getPeriodoEtaData() {
        viewModelScope.launch {
            try {
                val result = repo.getPeriodoEta(periodoEtaRequest)
                if (result.isEmpty()) {
                    _uiStatePeriodo.value = ResourceUiState.Error("No hay períodos")
                } else {
                    _uiStatePeriodo.value = ResourceUiState.Success(result)
                }
            } catch (e: Exception) {
                _uiStatePeriodo.value = ResourceUiState.Error(e.message ?: "Ocurrió un error")
            }
        }
    }

    private fun getGuardarEtaData() {
       viewModelScope.launch {
           _uiStateEliminarDocEta.value = ResourceUiState.Loading

           try {
               val result = repo.getGuardarEta(dataGuardarRequest)
               if(result.isEmpty()){
                   _uiStateGuardarEta.value = ResourceUiState.Error("No se pudo guardar")
               } else {
                   _uiStateGuardarEta.value = ResourceUiState.Success(result)
               }
           }catch (e:Exception){
               _uiStateGuardarEta.value = ResourceUiState.Error(e.message ?: "Ocurrió un error")
           }
       }
    }

    fun clearDocumentosEta() {
        _uiStateDocumentosEta.value = ResourceUiState.Success(listOf())
    }

    fun resetGuardarEtaState() {
        _uiStateGuardarEta.value = ResourceUiState.Empty
    }

    fun resetEliminarDocEtaState() {
        _uiStateEliminarDocEta.value = ResourceUiState.Loading
    }
}
