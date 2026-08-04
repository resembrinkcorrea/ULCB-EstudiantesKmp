package pe.lecordonbleu.universidadestudiante.presentation.screens.vermatricula

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import pe.lecordonbleu.universidadestudiante.data.remote.dto.ResponseCarrera
import pe.lecordonbleu.universidadestudiante.data.remote.dto.ResponseDetalleMatricula
import pe.lecordonbleu.universidadestudiante.data.remote.dto.ResponseHora
import pe.lecordonbleu.universidadestudiante.data.remote.dto.ResponseHoraPagoMatricula
import pe.lecordonbleu.universidadestudiante.data.remote.dto.ResponseHorarioPDF
import pe.lecordonbleu.universidadestudiante.data.remote.dto.ResponseListaMatriculaDeudas
import pe.lecordonbleu.universidadestudiante.data.remote.dto.ResponseObtenerEstudianteMatricula
import pe.lecordonbleu.universidadestudiante.domain.model.ObtenerEstudianteMatriculaRequest
import pe.lecordonbleu.universidadestudiante.data.remote.dto.ResponseObtenerTurnoMatricula
import pe.lecordonbleu.universidadestudiante.data.remote.dto.ResponseProyeccionValidacion
import pe.lecordonbleu.universidadestudiante.data.remote.dto.ResponseRegistrarMatricula
import pe.lecordonbleu.universidadestudiante.data.remote.dto.ResponseResumenHistorico
import pe.lecordonbleu.universidadestudiante.data.remote.dto.ResponseTextosHtml
import pe.lecordonbleu.universidadestudiante.data.remote.dto.ResponseValidarDocumentos
import pe.lecordonbleu.universidadestudiante.data.remote.dto.ResponseValidarInicioMatricula
import pe.lecordonbleu.universidadestudiante.data.remote.dto.ResponseVerMatricula
import pe.lecordonbleu.universidadestudiante.data.remote.dto.ListVerMatric
import pe.lecordonbleu.universidadestudiante.domain.model.CarreraRequest
import pe.lecordonbleu.universidadestudiante.domain.model.DetalleMatriculaRequest
import pe.lecordonbleu.universidadestudiante.domain.model.HoraPagoMatriculaRequest
import pe.lecordonbleu.universidadestudiante.domain.model.HorarioPDFRequest
import pe.lecordonbleu.universidadestudiante.domain.model.ListaMatriculaDeudasRequest
import pe.lecordonbleu.universidadestudiante.domain.model.MatriculaBody
import pe.lecordonbleu.universidadestudiante.domain.model.ObtenerTurnoMatriculaRequest
import pe.lecordonbleu.universidadestudiante.domain.model.RegistrarMatriculaBodyRequest
import pe.lecordonbleu.universidadestudiante.domain.model.ResumenHistoricoRequest
import pe.lecordonbleu.universidadestudiante.domain.model.TextosHtmlRequest
import pe.lecordonbleu.universidadestudiante.domain.model.ValidaProyeccionRequest
import pe.lecordonbleu.universidadestudiante.domain.model.ValidarDocumentosRequest
import pe.lecordonbleu.universidadestudiante.domain.model.ValidarInicioMatriculaRequest
import pe.lecordonbleu.universidadestudiante.domain.model.VerMatriculaRequest
import pe.lecordonbleu.universidadestudiante.domain.model.toCursoHorarioPDF
import pe.lecordonbleu.universidadestudiante.domain.repository.AppRepository
import pe.lecordonbleu.universidadestudiante.presentation.vo.ResourceUiState

class MatriculaViewModel(private val repo: AppRepository) : ViewModel() {

    private val _uiStateProyeccion = MutableStateFlow<ResourceUiState<ResponseProyeccionValidacion>>(ResourceUiState.Empty)
    private val _uiStateCarrera = MutableStateFlow<ResourceUiState<ResponseCarrera>>(ResourceUiState.Empty)
    private val _uiStateVerMatricula = MutableStateFlow<ResourceUiState<ResponseVerMatricula>>(ResourceUiState.Empty)
    private val _uiStateDetalleMatricula = MutableStateFlow<ResourceUiState<ResponseDetalleMatricula>>(ResourceUiState.Empty)
    private val _uiStateResumenHistorico = MutableStateFlow<ResourceUiState<ResponseResumenHistorico>>(ResourceUiState.Empty)
    private val _uiStateEstudianteMatricula = MutableStateFlow<ResourceUiState<ResponseObtenerEstudianteMatricula>>(ResourceUiState.Empty)
    private val _uiStateDeudas = MutableStateFlow<ResourceUiState<ResponseListaMatriculaDeudas>>(ResourceUiState.Empty)
    private val _uiStateHoraPago = MutableStateFlow<ResourceUiState<ResponseHoraPagoMatricula>>(ResourceUiState.Empty)
    private val _uiStateTurno = MutableStateFlow<ResourceUiState<ResponseObtenerTurnoMatricula>>(ResourceUiState.Empty)
    private val _uiStateValidarDocs = MutableStateFlow<ResourceUiState<ResponseValidarDocumentos>>(ResourceUiState.Empty)
    private val _uiStateTextosHtml = MutableStateFlow<ResourceUiState<ResponseTextosHtml>>(ResourceUiState.Empty)
    private val _uiStateValidarInicio = MutableStateFlow<ResourceUiState<ResponseValidarInicioMatricula>>(ResourceUiState.Empty)
    private val _uiStateHoraServidor = MutableStateFlow<ResourceUiState<ResponseHora>>(ResourceUiState.Empty)
    private val _uiStateRegistrar = MutableStateFlow<ResourceUiState<ResponseRegistrarMatricula>>(ResourceUiState.Empty)
    private val _uiStateHorarioPDF = MutableStateFlow<ResourceUiState<ResponseHorarioPDF>>(ResourceUiState.Empty)

    val uiStateProyeccion = _uiStateProyeccion.asStateFlow()
    val uiStateCarrera = _uiStateCarrera.asStateFlow()
    val uiStateVerMatricula = _uiStateVerMatricula.asStateFlow()
    val uiStateDetalleMatricula = _uiStateDetalleMatricula.asStateFlow()
    val uiStateResumenHistorico = _uiStateResumenHistorico.asStateFlow()
    val uiStateEstudianteMatricula = _uiStateEstudianteMatricula.asStateFlow()
    val uiStateDeudas = _uiStateDeudas.asStateFlow()
    val uiStateHoraPago = _uiStateHoraPago.asStateFlow()
    val uiStateTurno = _uiStateTurno.asStateFlow()
    val uiStateValidarDocs = _uiStateValidarDocs.asStateFlow()
    val uiStateTextosHtml = _uiStateTextosHtml.asStateFlow()
    val uiStateValidarInicio = _uiStateValidarInicio.asStateFlow()
    val uiStateHoraServidor = _uiStateHoraServidor.asStateFlow()
    val uiStateRegistrar = _uiStateRegistrar.asStateFlow()
    val uiStateHorarioPDF = _uiStateHorarioPDF.asStateFlow()

    private lateinit var carreraRequest: CarreraRequest
    private lateinit var verMatriculaRequest: VerMatriculaRequest
    private lateinit var detalleMatriculaRequest: DetalleMatriculaRequest
    private lateinit var resumenHistoricoRequest: ResumenHistoricoRequest
    private lateinit var obtenerEstudianteMatriculaRequest: ObtenerEstudianteMatriculaRequest
    private lateinit var listaDeudasRequest: ListaMatriculaDeudasRequest
    private lateinit var horaPagoRequest: HoraPagoMatriculaRequest
    private lateinit var obtenerTurnoRequest: ObtenerTurnoMatriculaRequest
    private lateinit var validarDocumentosRequest: ValidarDocumentosRequest
    private lateinit var textosHtmlRequest: TextosHtmlRequest
    private lateinit var validarInicioRequest: ValidarInicioMatriculaRequest
    private lateinit var registrarMatriculaRequest: RegistrarMatriculaBodyRequest
    private lateinit var horarioPDFRequest: HorarioPDFRequest

    // ─── Ver Matricula ────────────────────────────────────────────────────────

    fun setProyeccion(idEstud: Int) {
        viewModelScope.launch {
            _uiStateProyeccion.value = ResourceUiState.Loading
            _uiStateProyeccion.value = try {
                ResourceUiState.Success(repo.getProyeccionValidacion(ValidaProyeccionRequest(idEstud)))
            } catch (e: Exception) {
                ResourceUiState.Error(e.message ?: "Error al obtener proyeccion")
            }
        }
    }

    fun setCarrera(idEstud: Int) {
        carreraRequest = CarreraRequest(idEstud)
        fetchCarrera()
    }

    fun setVerMatricula(
        idPeriodoAcad: Int,
        idServicio: Int,
        idPlanEstudioDet: Int,
        idEstudiante: Int,
        id_sistema: Int,
        uneg: Int,
        id_usuario: Int
    ) {
        verMatriculaRequest = VerMatriculaRequest(idPeriodoAcad, idServicio, idPlanEstudioDet, idEstudiante, id_sistema, uneg, id_usuario)
        fetchVerMatricula()
    }

    fun setDetalleMatricula(
        idOfertaAcadDet: Int,
        id_asign_det_cr: String,
        id_hora_dia: String,
        id_dia_semana: String,
        hora_ini_cr: String,
        hora_fin_cr: String
    ) {
        detalleMatriculaRequest = DetalleMatriculaRequest(idOfertaAcadDet, id_asign_det_cr, id_hora_dia, id_dia_semana, hora_ini_cr, hora_fin_cr)
        fetchDetalleMatricula()
    }

    fun setResumenHistorico(idEstudPe: Int, idPeriodoAcadVal: Int) {
        resumenHistoricoRequest = ResumenHistoricoRequest(idEstudPe, idPeriodoAcadVal)
        fetchResumenHistorico()
    }

    private fun fetchCarrera() {
        viewModelScope.launch {
            _uiStateCarrera.value = ResourceUiState.Loading
            _uiStateCarrera.value = try {
                ResourceUiState.Success(repo.getCarrera(carreraRequest))
            } catch (e: Exception) {
                ResourceUiState.Error(e.message ?: "Error al obtener carreras")
            }
        }
    }

    private fun fetchVerMatricula() {
        viewModelScope.launch {
            _uiStateVerMatricula.value = ResourceUiState.Loading
            _uiStateVerMatricula.value = try {
                ResourceUiState.Success(repo.getVerMatricula(verMatriculaRequest))
            } catch (e: Exception) {
                ResourceUiState.Error(e.message ?: "Error al obtener matricula")
            }
        }
    }

    private fun fetchDetalleMatricula() {
        viewModelScope.launch {
            _uiStateDetalleMatricula.value = ResourceUiState.Loading
            _uiStateDetalleMatricula.value = try {
                ResourceUiState.Success(repo.getDetalleMatricula(detalleMatriculaRequest))
            } catch (e: Exception) {
                ResourceUiState.Error(e.message ?: "Error al obtener detalle")
            }
        }
    }

    private fun fetchResumenHistorico() {
        viewModelScope.launch {
            _uiStateResumenHistorico.value = ResourceUiState.Loading
            _uiStateResumenHistorico.value = try {
                ResourceUiState.Success(repo.getResumenHistorico(resumenHistoricoRequest))
            } catch (e: Exception) {
                ResourceUiState.Error(e.message ?: "Error al obtener resumen historico")
            }
        }
    }

    // ─── Registrar Matricula ──────────────────────────────────────────────────

    fun setObtenerEstudianteMatricula(idEstud: Int, idTiposerva: Int) {
        obtenerEstudianteMatriculaRequest = ObtenerEstudianteMatriculaRequest(idEstud, idTiposerva)
        fetchObtenerEstudianteMatricula()
    }

    fun setDeudas(idPlanEstudioDet: Int, idEstudServ: Int, idEstudiante: Int, idPeriodoAcad: Int) {
        listaDeudasRequest = ListaMatriculaDeudasRequest(idPlanEstudioDet, idEstudServ, idEstudiante, idPeriodoAcad)
        fetchDeudas()
    }

    fun setHoraPago(promedio: Double, idPestDet: Int, estadoIngresante: Int, uneg: Int, idEstudServ: Int, idPeracad: Int, idEstudPe: Int) {
        horaPagoRequest = HoraPagoMatriculaRequest(promedio, idPestDet, estadoIngresante, uneg, idEstudServ, idPeracad, idEstudPe)
        fetchHoraPago()
    }

    fun setObtenerTurno(promedio: Double, idPestDet: Int, estadoIngresante: Int, uneg: Int, idEstudServ: Int, idPeracad: Int) {
        obtenerTurnoRequest = ObtenerTurnoMatriculaRequest(promedio, idPestDet, estadoIngresante, uneg, idEstudServ, idPeracad)
        fetchObtenerTurno()
    }

    fun setValidarDocs(idPeracad: Int, estadoIngresante: Int, idEstudPe: Int) {
        validarDocumentosRequest = ValidarDocumentosRequest(idPeracad, estadoIngresante, idEstudPe)
        fetchValidarDocs()
    }

    fun setTextosHtml(idUneg: Int, idUsuario: Int) {
        textosHtmlRequest = TextosHtmlRequest(idUneg, idUsuario)
        fetchTextosHtml()
    }

    fun setValidarInicio(idGrmatricd: Int, idOacadArranque: Int, idEstudPe: Int) {
        validarInicioRequest = ValidarInicioMatriculaRequest(idGrmatricd, idOacadArranque, idEstudPe)
        fetchValidarInicio()
    }

    fun fetchHoraServidor() {
        viewModelScope.launch {
            _uiStateHoraServidor.value = ResourceUiState.Loading
            _uiStateHoraServidor.value = try {
                ResourceUiState.Success(repo.getHoraServidor())
            } catch (e: Exception) {
                ResourceUiState.Error(e.message ?: "Error al obtener hora servidor")
            }
        }
    }

    fun setRegistrarMatricula(idTiposerva: Int, matriculaBody: MatriculaBody) {
        registrarMatriculaRequest = RegistrarMatriculaBodyRequest(idTiposerva, matriculaBody)
        fetchRegistrarMatricula()
    }

    private fun fetchObtenerEstudianteMatricula() {
        viewModelScope.launch {
            _uiStateEstudianteMatricula.value = ResourceUiState.Loading
            _uiStateEstudianteMatricula.value = try {
                ResourceUiState.Success(repo.getObtenerEstudianteMatricula(obtenerEstudianteMatriculaRequest))
            } catch (e: Exception) {
                ResourceUiState.Error(e.message ?: "Error al obtener estudiante matrícula")
            }
        }
    }

    private fun fetchDeudas() {
        viewModelScope.launch {
            _uiStateDeudas.value = ResourceUiState.Loading
            _uiStateDeudas.value = try {
                ResourceUiState.Success(repo.getListaMatriculaDeudas(listaDeudasRequest))
            } catch (e: Exception) {
                ResourceUiState.Error(e.message ?: "Error al obtener deudas")
            }
        }
    }

    private fun fetchHoraPago() {
        viewModelScope.launch {
            _uiStateHoraPago.value = ResourceUiState.Loading
            _uiStateHoraPago.value = try {
                ResourceUiState.Success(repo.getHoraPagoMatricula(horaPagoRequest))
            } catch (e: Exception) {
                ResourceUiState.Error(e.message ?: "Error al obtener hora pago")
            }
        }
    }

    private fun fetchObtenerTurno() {
        viewModelScope.launch {
            _uiStateTurno.value = ResourceUiState.Loading
            _uiStateTurno.value = try {
                ResourceUiState.Success(repo.getObtenerTurnoMatricula(obtenerTurnoRequest))
            } catch (e: Exception) {
                ResourceUiState.Error(e.message ?: "Error al obtener turno")
            }
        }
    }

    private fun fetchValidarDocs() {
        viewModelScope.launch {
            _uiStateValidarDocs.value = ResourceUiState.Loading
            _uiStateValidarDocs.value = try {
                ResourceUiState.Success(repo.getValidarDocumentos(validarDocumentosRequest))
            } catch (e: Exception) {
                ResourceUiState.Error(e.message ?: "Error al validar documentos")
            }
        }
    }

    private fun fetchTextosHtml() {
        viewModelScope.launch {
            _uiStateTextosHtml.value = ResourceUiState.Loading
            _uiStateTextosHtml.value = try {
                ResourceUiState.Success(repo.getTextosHtml(textosHtmlRequest))
            } catch (e: Exception) {
                ResourceUiState.Error(e.message ?: "Error al obtener textos")
            }
        }
    }

    private fun fetchValidarInicio() {
        viewModelScope.launch {
            _uiStateValidarInicio.value = ResourceUiState.Loading
            _uiStateValidarInicio.value = try {
                ResourceUiState.Success(repo.getValidarInicioMatricula(validarInicioRequest))
            } catch (e: Exception) {
                ResourceUiState.Error(e.message ?: "Error al validar inicio")
            }
        }
    }

    private fun fetchRegistrarMatricula() {
        viewModelScope.launch {
            _uiStateRegistrar.value = ResourceUiState.Loading
            _uiStateRegistrar.value = try {
                ResourceUiState.Success(repo.registrarMatricula(registrarMatriculaRequest))
            } catch (e: Exception) {
                ResourceUiState.Success(ResponseRegistrarMatricula(flag_val = 0, mensaje = e.message ?: "Error al registrar matricula", titulo = "", icono = "", tipo = 0))
            }
        }
    }

    // ─── Resets ───────────────────────────────────────────────────────────────

    fun resetProyeccionState() { _uiStateProyeccion.value = ResourceUiState.Empty }
    fun resetCarreraState() { _uiStateCarrera.value = ResourceUiState.Empty }
    fun resetVerMatriculaState() { _uiStateVerMatricula.value = ResourceUiState.Empty }
    fun resetDetalleMatriculaState() { _uiStateDetalleMatricula.value = ResourceUiState.Empty }
    fun resetResumenHistoricoState() { _uiStateResumenHistorico.value = ResourceUiState.Empty }

    fun resetObtenerEstudianteMatriculaState() { _uiStateEstudianteMatricula.value = ResourceUiState.Empty }
    fun resetDeudasState() { _uiStateDeudas.value = ResourceUiState.Empty }
    fun resetHoraPagoState() { _uiStateHoraPago.value = ResourceUiState.Empty }
    fun resetTurnoState() { _uiStateTurno.value = ResourceUiState.Empty }
    fun resetValidarDocsState() { _uiStateValidarDocs.value = ResourceUiState.Empty }
    fun resetTextosHtmlState() { _uiStateTextosHtml.value = ResourceUiState.Empty }
    fun resetValidarInicioState() { _uiStateValidarInicio.value = ResourceUiState.Empty }
    fun resetHoraServidorState() { _uiStateHoraServidor.value = ResourceUiState.Empty }
    fun resetRegistrarState() { _uiStateRegistrar.value = ResourceUiState.Empty }
    fun resetHorarioPDFState() { _uiStateHorarioPDF.value = ResourceUiState.Empty }

    // ─── Horario PDF ──────────────────────────────────────────────────────────

    fun setHorarioPDF(periodo: String, cursos: List<ListVerMatric>) {
        horarioPDFRequest = HorarioPDFRequest(periodo, cursos.map { it.toCursoHorarioPDF() })
        fetchHorarioPDF()
    }

    private fun fetchHorarioPDF() {
        viewModelScope.launch {
            _uiStateHorarioPDF.value = ResourceUiState.Loading
            _uiStateHorarioPDF.value = try {
                ResourceUiState.Success(repo.getHorarioPDF(horarioPDFRequest))
            } catch (e: Exception) {
                ResourceUiState.Error(e.message ?: "Error al generar PDF")
            }
        }
    }
}
