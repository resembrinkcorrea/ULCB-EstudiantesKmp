package pe.lecordonbleu.universidadestudiante.presentation.screens.archivoscompartidos

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import pe.lecordonbleu.universidadestudiante.data.remote.dto.ResponseContenidoTags
import pe.lecordonbleu.universidadestudiante.data.remote.dto.ResponseEstadoArchivo
import pe.lecordonbleu.universidadestudiante.data.remote.dto.ResponseListaServicio
import pe.lecordonbleu.universidadestudiante.data.remote.dto.ResponseTagsCompartidos
import pe.lecordonbleu.universidadestudiante.data.remote.dto.ResponseTipoServicio
import pe.lecordonbleu.universidadestudiante.domain.model.ContenidoTagsRequest
import pe.lecordonbleu.universidadestudiante.domain.model.EstadoArchivoRequest
import pe.lecordonbleu.universidadestudiante.domain.model.ServicioRequest
import pe.lecordonbleu.universidadestudiante.domain.model.ServicioTipoRequest
import pe.lecordonbleu.universidadestudiante.domain.model.TagsArchivosRequest
import pe.lecordonbleu.universidadestudiante.domain.repository.AppRepository
import pe.lecordonbleu.universidadestudiante.presentation.vo.ResourceUiState

data class TagsItems(
    val orden_oferta_cab: Int,
    val id_oferta_carpeta_det: Int
)

class ArchivosCompartidosViewModel(private val repo: AppRepository) : ViewModel() {

    // UIStates
    private val _uiStateListaServicio = MutableStateFlow<ResourceUiState<ResponseListaServicio>>(ResourceUiState.Loading)
    val uiStateListaServicio = _uiStateListaServicio.asStateFlow()

    private val _uiStateTipoServicio = MutableStateFlow<ResourceUiState<ResponseTipoServicio>>(ResourceUiState.Loading)
    val uiStateTipoServicio = _uiStateTipoServicio.asStateFlow()

    private val _uiStateTags = MutableStateFlow<ResourceUiState<ResponseTagsCompartidos>>(ResourceUiState.Loading)
    val uiStateTags = _uiStateTags.asStateFlow()

    private val _uiStateContenido = MutableStateFlow<ResourceUiState<ResponseContenidoTags>>(ResourceUiState.Loading)
    val uiStateContenido = _uiStateContenido.asStateFlow()

    private val _uiStateEstadoArchivo = MutableStateFlow<ResourceUiState<ResponseEstadoArchivo>>(ResourceUiState.Loading)
    val uiStateEstadoArchivo = _uiStateEstadoArchivo.asStateFlow()

    // Estado local
    private val _itemsTags = MutableStateFlow<List<TagsItems>>(emptyList())
    val itemsTags = _itemsTags.asStateFlow()

    private val _currentPosition = MutableStateFlow(0)
    val currentPosition = _currentPosition.asStateFlow()

    // Requests
    private lateinit var requestServicio: ServicioRequest
    private lateinit var requestServicioTipo: ServicioTipoRequest
    private lateinit var requestTags: TagsArchivosRequest
    private lateinit var requestContenido: ContenidoTagsRequest
    private lateinit var requestEstadoArchivo: EstadoArchivoRequest

    // === Setters ===
    fun setServicioRequest(idEstud: Int) {
        requestServicio = ServicioRequest(idEstud)
        getListaServicio()
    }

    fun setServicioTipoRequest(idUneg: Int, idEstud: Int, idServ: Int) {
        requestServicioTipo = ServicioTipoRequest(idUneg, idEstud, idServ)
        getServicioTipo()
    }

    fun setTagsRequest(idUneg: Int, idEstud: Int, idTipoServa: Int, idServ: Int) {
        requestTags = TagsArchivosRequest(idUneg, idEstud, idTipoServa, idServ)
        getTagsCompartidos()
    }

    fun setContenidoTagsRequest(idUneg: Int, idCarpetaDet: Int, idUsuario: Int) {
        requestContenido = ContenidoTagsRequest(idUneg, idCarpetaDet, idUsuario)
        getContenidoTags()
    }

    fun setEstadoArchivoRequest(idUneg: Int, idUsuario: Int, idEstado: Int, flagLeido: Int) {
        requestEstadoArchivo = EstadoArchivoRequest(idUneg, idUsuario, idEstado, flagLeido)
        getEstadoArchivo()
    }

    fun setPosition(position: Int) {
        _currentPosition.value = position
    }

    fun resetTipoServicioState() { _uiStateTipoServicio.value = ResourceUiState.Empty }
    fun resetTagsState() { _uiStateTags.value = ResourceUiState.Empty }
    fun resetContenidoState() { _uiStateContenido.value = ResourceUiState.Empty }
    fun resetEstadoArchivoState() { _uiStateEstadoArchivo.value = ResourceUiState.Empty }

    // === Repositorio interno ===
    private fun getListaServicio() {
        viewModelScope.launch {
            try {
                val result = repo.getListaServicio(requestServicio)
                _uiStateListaServicio.value = ResourceUiState.Success(result)
            } catch (e: Exception) {
                _uiStateListaServicio.value = ResourceUiState.Error(e.message ?: "Error al obtener servicios")
            }
        }
    }

    private fun getServicioTipo() {
        viewModelScope.launch {
            try {
                val result = repo.getServicioTipo(requestServicioTipo)
                _uiStateTipoServicio.value = ResourceUiState.Success(result)
            } catch (e: Exception) {
                _uiStateTipoServicio.value = ResourceUiState.Error(e.message ?: "Error al obtener tipo servicio")
            }
        }
    }

    private fun getTagsCompartidos() {
        viewModelScope.launch {
            try {
                val result = repo.getTagsCompartidos(requestTags)
                _uiStateTags.value = ResourceUiState.Success(result)
                _itemsTags.value = result.TagsCompartidosEstudiante.map {
                    TagsItems(it.orden_oferta_cab, it.id_oferta_carpeta_det)
                }
            } catch (e: Exception) {
                _uiStateTags.value = ResourceUiState.Error(e.message ?: "Error al obtener tags")
            }
        }
    }

    private fun getContenidoTags() {
        viewModelScope.launch {
            try {
                val result = repo.getContenidoTags(requestContenido)
                _uiStateContenido.value = ResourceUiState.Success(result)
            } catch (e: Exception) {
                _uiStateContenido.value = ResourceUiState.Error(e.message ?: "Error al obtener contenido")
            }
        }
    }

    private fun getEstadoArchivo() {
        viewModelScope.launch {
            _uiStateEstadoArchivo.value = ResourceUiState.Loading
            try {
                val result = repo.getEstadoArchivo(requestEstadoArchivo)
                _uiStateEstadoArchivo.value = ResourceUiState.Success(result)
            } catch (e: Exception) {
                _uiStateEstadoArchivo.value = ResourceUiState.Error(e.message ?: "Error al obtener estado")
            }
        }
    }
}
