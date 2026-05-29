package pe.lecordonbleu.universidadestudiante.presentation.screens.notas

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import pe.lecordonbleu.universidadestudiante.data.remote.dto.ResponsePromedioNotas
import pe.lecordonbleu.universidadestudiante.data.remote.dto.ResponseTiposTareasAcad
import pe.lecordonbleu.universidadestudiante.data.remote.dto.TareaAcad
import pe.lecordonbleu.universidadestudiante.domain.model.PromedioNotasRequest
import pe.lecordonbleu.universidadestudiante.domain.model.TareasAcadRequest
import pe.lecordonbleu.universidadestudiante.domain.model.TiposTareasAcadRequest
import pe.lecordonbleu.universidadestudiante.domain.repository.AppRepository
import pe.lecordonbleu.universidadestudiante.presentation.screens.notas.uibuilder.GrupoPestanaTarea
import pe.lecordonbleu.universidadestudiante.presentation.screens.notas.uibuilder.NotasUiBuilder
import pe.lecordonbleu.universidadestudiante.presentation.vo.ResourceUiState

class DetalleNotasViewModel(private val repo: AppRepository) : ViewModel() {

    private val _detalleState = MutableStateFlow<ResourceUiState<ResponsePromedioNotas>>(ResourceUiState.Empty)
    val detalleState = _detalleState.asStateFlow()

    private val _tiposTareasState = MutableStateFlow<ResourceUiState<ResponseTiposTareasAcad>>(ResourceUiState.Empty)
    val tiposTareasState = _tiposTareasState.asStateFlow()

    private val _gruposTareas = MutableStateFlow<List<GrupoPestanaTarea>>(emptyList())
    val gruposTareas = _gruposTareas.asStateFlow()

    private val _tareasState = MutableStateFlow<ResourceUiState<Map<Int, List<TareaAcad>>>>(ResourceUiState.Empty)
    val tareasState = _tareasState.asStateFlow()

    private lateinit var detalleRequest: PromedioNotasRequest
    private lateinit var tiposTareasRequest: TiposTareasAcadRequest
    private var idMatricNotActual: Int = 0
    private var ultimoGrupoSolicitado: String? = null

    fun setDetalleRequest(idMatricNot: Int, uneg: Int) {
        detalleRequest = PromedioNotasRequest(id_estud_pe = idMatricNot, uneg = uneg)
        getDetalleData()
    }

    fun setTiposTareasRequest(idMatricNot: Int) {
        idMatricNotActual = idMatricNot
        tiposTareasRequest = TiposTareasAcadRequest(id_matric_not = idMatricNot)
        getTiposTareasData()
    }

    fun setGrupoRequest(grupo: GrupoPestanaTarea) {
        if (grupo.nombrePestana == ultimoGrupoSolicitado) return
        ultimoGrupoSolicitado = grupo.nombrePestana
        val ids = grupo.idsTeoria + grupo.idsPractica + grupo.idsGeneral
        if (ids.isEmpty()) return

        viewModelScope.launch {
            _tareasState.value = ResourceUiState.Loading
            try {
                val resultados = ids.map { id ->
                    async { id to repo.getTareasAcad(TareasAcadRequest(idMatricNotActual, id)).listadoTareaAcad }
                }.awaitAll().toMap()
                _tareasState.value = ResourceUiState.Success(resultados)
            } catch (e: Exception) {
                _tareasState.value = ResourceUiState.Error(e.message ?: "Error al obtener tareas")
            }
        }
    }

    private fun getDetalleData() {
        viewModelScope.launch {
            _detalleState.value = ResourceUiState.Loading
            try {
                val result = repo.getDetalleNotas(detalleRequest)
                _detalleState.value = ResourceUiState.Success(result)
            } catch (e: Exception) {
                _detalleState.value = ResourceUiState.Error(e.message ?: "Error al obtener detalle")
            }
        }
    }

    private fun getTiposTareasData() {
        viewModelScope.launch {
            _tiposTareasState.value = ResourceUiState.Loading
            try {
                val result = repo.getTiposTareasAcad(tiposTareasRequest)
                _tiposTareasState.value = ResourceUiState.Success(result)
                _gruposTareas.value = NotasUiBuilder.agruparTiposTareas(result.listadoTiposTareaAcad)
            } catch (e: Exception) {
                _tiposTareasState.value = ResourceUiState.Error(e.message ?: "Error al obtener tipos de tarea")
            }
        }
    }

    fun resetTareasState() {
        _tareasState.value = ResourceUiState.Empty
        ultimoGrupoSolicitado = null
    }

    fun resetUiState() {
        _detalleState.value = ResourceUiState.Empty
        _tiposTareasState.value = ResourceUiState.Empty
        _tareasState.value = ResourceUiState.Empty
        _gruposTareas.value = emptyList()
        ultimoGrupoSolicitado = null
    }
}
