package pe.lecordonbleu.universidadestudiante.domain.repository

import pe.lecordonbleu.universidadestudiante.domain.model.RegisterPaymentRequest
import pe.lecordonbleu.universidadestudiante.data.remote.dto.ResponseCarrera
import pe.lecordonbleu.universidadestudiante.data.remote.dto.ResponseCarreraPlanEstudio
import pe.lecordonbleu.universidadestudiante.data.remote.dto.ResponseCarrerasConvalidacion
import pe.lecordonbleu.universidadestudiante.data.remote.dto.ResponseCursosConvalidacion
import pe.lecordonbleu.universidadestudiante.data.remote.dto.ResponseDataMenu
import pe.lecordonbleu.universidadestudiante.data.remote.dto.ResponseEstudianteOAcadConvalidacion
import pe.lecordonbleu.universidadestudiante.data.remote.dto.ResponseHabilitarAulaDemo
import pe.lecordonbleu.universidadestudiante.data.remote.dto.ResponseHora
import pe.lecordonbleu.universidadestudiante.data.remote.dto.ResponseHorario
import pe.lecordonbleu.universidadestudiante.data.remote.dto.ResponsePeriodo
import pe.lecordonbleu.universidadestudiante.data.remote.dto.ResponseQr
import pe.lecordonbleu.universidadestudiante.data.remote.dto.ResponsePerfilEstudiante
import pe.lecordonbleu.universidadestudiante.data.remote.dto.ResponseTipoTraslado
import pe.lecordonbleu.universidadestudiante.domain.model.CarreraRequest
import pe.lecordonbleu.universidadestudiante.domain.model.CarrerasConvalidacionRequest
import pe.lecordonbleu.universidadestudiante.domain.model.CursosConvalidacionRequest
import pe.lecordonbleu.universidadestudiante.domain.model.DataCarreraRequest
import pe.lecordonbleu.universidadestudiante.domain.model.EstudianteOAcadConvalidacionRequest
import pe.lecordonbleu.universidadestudiante.domain.model.HabilitarAulaDemoRequest
import pe.lecordonbleu.universidadestudiante.domain.model.HorarioRequest
import pe.lecordonbleu.universidadestudiante.domain.model.PeriodoRequest
import pe.lecordonbleu.universidadestudiante.domain.model.QrEntity
import pe.lecordonbleu.universidadestudiante.domain.model.DataPerfilRequest
import pe.lecordonbleu.universidadestudiante.domain.model.TipoTrasladoRequest
import pe.lecordonbleu.universidadestudiante.domain.model.UserMenuRequest
import pe.lecordonbleu.universidadestudiante.data.remote.dto.ResponseCarreraRemote
import pe.lecordonbleu.universidadestudiante.data.remote.dto.ResponsePlanEstudio
import pe.lecordonbleu.universidadestudiante.data.remote.dto.ResponseValoresPlan
import pe.lecordonbleu.universidadestudiante.data.remote.dto.ResponseTablaPlan
import pe.lecordonbleu.universidadestudiante.domain.model.PlanEstudioRequest
import pe.lecordonbleu.universidadestudiante.domain.model.ValoresPlanRequest
import pe.lecordonbleu.universidadestudiante.domain.model.TablaPlanRequest
import pe.lecordonbleu.universidadestudiante.domain.model.GenerarPdfMallaRequest
import pe.lecordonbleu.universidadestudiante.data.remote.dto.ResponseListaServicio
import pe.lecordonbleu.universidadestudiante.data.remote.dto.ResponseTipoServicio
import pe.lecordonbleu.universidadestudiante.data.remote.dto.ResponseTagsCompartidos
import pe.lecordonbleu.universidadestudiante.data.remote.dto.ResponseContenidoTags
import pe.lecordonbleu.universidadestudiante.data.remote.dto.ResponseEstadoArchivo
import pe.lecordonbleu.universidadestudiante.domain.model.ServicioRequest
import pe.lecordonbleu.universidadestudiante.domain.model.ServicioTipoRequest
import pe.lecordonbleu.universidadestudiante.domain.model.TagsArchivosRequest
import pe.lecordonbleu.universidadestudiante.domain.model.ContenidoTagsRequest
import pe.lecordonbleu.universidadestudiante.domain.model.EstadoArchivoRequest
import pe.lecordonbleu.universidadestudiante.data.remote.dto.ResponseLinksInstitucional
import pe.lecordonbleu.universidadestudiante.domain.model.LinksItemRequest
import pe.lecordonbleu.universidadestudiante.data.remote.dto.ResponseCursos
import pe.lecordonbleu.universidadestudiante.data.remote.dto.ResponsePromedioNotas
import pe.lecordonbleu.universidadestudiante.data.remote.dto.ResponseTareasAcad
import pe.lecordonbleu.universidadestudiante.data.remote.dto.ResponseTiposTareasAcad
import pe.lecordonbleu.universidadestudiante.data.remote.dto.ResponseHistorialNotas
import pe.lecordonbleu.universidadestudiante.domain.model.CursosRequest
import pe.lecordonbleu.universidadestudiante.domain.model.PromedioNotasRequest
import pe.lecordonbleu.universidadestudiante.domain.model.TareasAcadRequest
import pe.lecordonbleu.universidadestudiante.domain.model.TiposTareasAcadRequest
import pe.lecordonbleu.universidadestudiante.domain.model.HistorialNotasRequest
import pe.lecordonbleu.universidadestudiante.data.remote.dto.ResponseAsignaturaEstudiante
import pe.lecordonbleu.universidadestudiante.data.remote.dto.ResponseAsistencia
import pe.lecordonbleu.universidadestudiante.data.remote.dto.ResponseHistorialAcademicoAlumno
import pe.lecordonbleu.universidadestudiante.data.remote.dto.ResponseHistorialAcademicoAlumnoDetalle
import pe.lecordonbleu.universidadestudiante.domain.model.AsignaturaEstudianteRequest
import pe.lecordonbleu.universidadestudiante.domain.model.DetalleAsistenciaRequest
import pe.lecordonbleu.universidadestudiante.domain.model.HistorialAcademicoAlumnoRequest
import pe.lecordonbleu.universidadestudiante.domain.model.HistorialAcademicoAlumnoDetalleRequest
import pe.lecordonbleu.universidadestudiante.data.remote.dto.ResponseBiblioteca
import pe.lecordonbleu.universidadestudiante.domain.model.BibliotecaRequest
import kotlinx.coroutines.flow.Flow
import pe.lecordonbleu.universidadestudiante.data.remote.dto.ResponseEstadoMarcacion
import pe.lecordonbleu.universidadestudiante.data.remote.dto.ResponseMarcarAsistencia
import pe.lecordonbleu.universidadestudiante.data.remote.dto.ResponseNavigationLog
import pe.lecordonbleu.universidadestudiante.data.remote.dto.ResponseProyeccionValidacion
import pe.lecordonbleu.universidadestudiante.domain.model.EstadoMarcacionRequest
import pe.lecordonbleu.universidadestudiante.domain.model.MarcarRequest
import pe.lecordonbleu.universidadestudiante.domain.model.NavigationLogRequest
import pe.lecordonbleu.universidadestudiante.domain.model.ValidaProyeccionRequest
import pe.lecordonbleu.universidadestudiante.data.remote.dto.ResponseAsignaturaEncuesta
import pe.lecordonbleu.universidadestudiante.data.remote.dto.ResponseEncuestaSatisfaccionEstado
import pe.lecordonbleu.universidadestudiante.data.remote.dto.ResponseEncuestaSatisfaccion
import pe.lecordonbleu.universidadestudiante.data.remote.dto.ResponseGuardarEncuestaSatisfaccion
import pe.lecordonbleu.universidadestudiante.data.remote.dto.ResponseListarEncuesta
import pe.lecordonbleu.universidadestudiante.domain.model.AsignaturaEncuestaRequest
import pe.lecordonbleu.universidadestudiante.domain.model.EncuestaSatisfaccionEstadoRequest
import pe.lecordonbleu.universidadestudiante.domain.model.EncuestaSatisfaccionRequest
import pe.lecordonbleu.universidadestudiante.domain.model.EncuestaSatisfaccionGuardarRequest
import pe.lecordonbleu.universidadestudiante.domain.model.ListarEncuestaRequest
import pe.lecordonbleu.universidadestudiante.data.remote.dto.ResponseActualizarToken
import pe.lecordonbleu.universidadestudiante.domain.model.FcmTokenRequest
import pe.lecordonbleu.universidadestudiante.data.remote.dto.ResponseServicioCuentaCorriente
import pe.lecordonbleu.universidadestudiante.data.remote.dto.ResponsePeriodoCuentaCorriente
import pe.lecordonbleu.universidadestudiante.data.remote.dto.ResponseListarCuentaCorriente
import pe.lecordonbleu.universidadestudiante.data.remote.dto.ResponseDetalleCuentaCorriente
import pe.lecordonbleu.universidadestudiante.data.remote.dto.ResponseTemporalCuentaCorriente
import pe.lecordonbleu.universidadestudiante.data.remote.dto.ResponseDeudasCuentasCorrientes
import pe.lecordonbleu.universidadestudiante.data.remote.dto.ResponseVerificarComprobante
import pe.lecordonbleu.universidadestudiante.data.remote.dto.ResponseTextosHtml
import pe.lecordonbleu.universidadestudiante.domain.model.ServicioCuentaCorrienteRequest
import pe.lecordonbleu.universidadestudiante.domain.model.PeriodoCuentaCorrienteRequest
import pe.lecordonbleu.universidadestudiante.domain.model.ListarCuentaCorrienteRequest
import pe.lecordonbleu.universidadestudiante.domain.model.DetalleCuentaCorrienteRequest
import pe.lecordonbleu.universidadestudiante.domain.model.TemporalCuentaCorrienteRequest
import pe.lecordonbleu.universidadestudiante.domain.model.DeudasCuentasCorrientesRequest
import pe.lecordonbleu.universidadestudiante.domain.model.VerificarComprobanteRequest
import pe.lecordonbleu.universidadestudiante.domain.model.TextosHtmlRequest
import pe.lecordonbleu.universidadestudiante.domain.model.ComprobantePecanoRequest
import pe.lecordonbleu.universidadestudiante.data.remote.dto.ResponseComprobantePecano
import pe.lecordonbleu.universidadestudiante.data.remote.dto.ResponseListarCampania
import pe.lecordonbleu.universidadestudiante.data.remote.dto.ResponseSolicitarCampania
import pe.lecordonbleu.universidadestudiante.domain.model.ListarCampaniaRequest
import pe.lecordonbleu.universidadestudiante.domain.model.SolicitarCampaniaRequest
import pe.lecordonbleu.universidadestudiante.data.remote.dto.ResponseVerMatricula
import pe.lecordonbleu.universidadestudiante.data.remote.dto.ResponseDetalleMatricula
import pe.lecordonbleu.universidadestudiante.data.remote.dto.ResponseResumenHistorico
import pe.lecordonbleu.universidadestudiante.domain.model.VerMatriculaRequest
import pe.lecordonbleu.universidadestudiante.domain.model.DetalleMatriculaRequest
import pe.lecordonbleu.universidadestudiante.domain.model.ResumenHistoricoRequest
import kotlinx.serialization.json.JsonObject
import pe.lecordonbleu.universidadestudiante.data.remote.dto.ResponseComprobanteTramite
import pe.lecordonbleu.universidadestudiante.data.remote.dto.ResponseCorreccionTramiteSave
import pe.lecordonbleu.universidadestudiante.data.remote.dto.ResponseCrearTramites
import pe.lecordonbleu.universidadestudiante.data.remote.dto.ResponseDuplicadoTituloGuardar
import pe.lecordonbleu.universidadestudiante.data.remote.dto.ResponseHoraPagoMatricula
import pe.lecordonbleu.universidadestudiante.data.remote.dto.ResponseHorarioPDF
import pe.lecordonbleu.universidadestudiante.data.remote.dto.ResponseListaMatriculaDeudas
import pe.lecordonbleu.universidadestudiante.data.remote.dto.ResponseMercadoPago
import pe.lecordonbleu.universidadestudiante.data.remote.dto.ResponseObtenerEstudianteMatricula
import pe.lecordonbleu.universidadestudiante.data.remote.dto.ResponseObtenerTurnoMatricula
import pe.lecordonbleu.universidadestudiante.data.remote.dto.ResponsePasarelasActivas
import pe.lecordonbleu.universidadestudiante.data.remote.dto.ResponsePublicKeyMP
import pe.lecordonbleu.universidadestudiante.data.remote.dto.ResponseRegistrarMatricula
import pe.lecordonbleu.universidadestudiante.data.remote.dto.ResponseRegistrarTramite
import pe.lecordonbleu.universidadestudiante.data.remote.dto.ResponseRequisitosTemp
import pe.lecordonbleu.universidadestudiante.data.remote.dto.ResponseTramiteDocFiltro
import pe.lecordonbleu.universidadestudiante.data.remote.dto.ResponseTramitePaises
import pe.lecordonbleu.universidadestudiante.data.remote.dto.ResponseTramitesDocumentos
import pe.lecordonbleu.universidadestudiante.data.remote.dto.ResponseValidarDocumentos
import pe.lecordonbleu.universidadestudiante.data.remote.dto.ResponseValidarEgresado
import pe.lecordonbleu.universidadestudiante.data.remote.dto.ResponseValidarInicioMatricula
import pe.lecordonbleu.universidadestudiante.domain.model.ComprobanteTramiteRequest
import pe.lecordonbleu.universidadestudiante.domain.model.CorreccionTramiteSaveRequest
import pe.lecordonbleu.universidadestudiante.domain.model.CrearTramitesRequest
import pe.lecordonbleu.universidadestudiante.domain.model.DocumentosCreadosRequest
import pe.lecordonbleu.universidadestudiante.domain.model.DuplicadoTituloGuardarRequest
import pe.lecordonbleu.universidadestudiante.domain.model.FichaMatriculaRequest
import pe.lecordonbleu.universidadestudiante.domain.model.GuardarArchivoTramiteRequest
import pe.lecordonbleu.universidadestudiante.domain.model.HoraPagoMatriculaRequest
import pe.lecordonbleu.universidadestudiante.domain.model.HorarioPDFRequest
import pe.lecordonbleu.universidadestudiante.domain.model.ListaMatriculaDeudasRequest
import pe.lecordonbleu.universidadestudiante.domain.model.ObtenerEstudianteMatriculaRequest
import pe.lecordonbleu.universidadestudiante.domain.model.ObtenerTurnoMatriculaRequest
import pe.lecordonbleu.universidadestudiante.domain.model.PagoEfectivoRequest
import pe.lecordonbleu.universidadestudiante.domain.model.PasarelasActivasRequest
import pe.lecordonbleu.universidadestudiante.domain.model.RegistrarMatriculaBodyRequest
import pe.lecordonbleu.universidadestudiante.domain.model.RegistrarTramiteRequest
import pe.lecordonbleu.universidadestudiante.domain.model.TarjetaRequest
import pe.lecordonbleu.universidadestudiante.domain.model.TramiteDocFiltroRequest
import pe.lecordonbleu.universidadestudiante.domain.model.TramitePaisesRequest
import pe.lecordonbleu.universidadestudiante.domain.model.ValidarDocumentosRequest
import pe.lecordonbleu.universidadestudiante.domain.model.ValidarEgresadoRequest
import pe.lecordonbleu.universidadestudiante.domain.model.ValidarInicioMatriculaRequest
import pe.lecordonbleu.universidadestudiante.domain.model.YapeRequest
import pe.lecordonbleu.universidadestudiante.domain.model.YapeTokenResponse
import pe.lecordonbleu.universidadestudiante.domain.model.ArchivosObligatoriosRequest
import pe.lecordonbleu.universidadestudiante.data.remote.dto.ResponseArchivosObligatorios

interface AppRepository {
    suspend fun getMenuDataUser(userRequest: UserMenuRequest): List<ResponseDataMenu>
    suspend fun getHabilitarAulaDemo(request: HabilitarAulaDemoRequest): ResponseHabilitarAulaDemo
    suspend fun getHoraServidor(): ResponseHora
    suspend fun getQrUsuario(qrEntity: QrEntity): List<ResponseQr>
    suspend fun getCarrera(request: CarreraRequest): ResponseCarrera
    suspend fun getPeriodo(request: PeriodoRequest): ResponsePeriodo
    suspend fun getHorario(request: HorarioRequest): ResponseHorario

    suspend fun getCarreraPlanEstudio(dataCarreraRequest: DataCarreraRequest): ResponseCarreraPlanEstudio
    suspend fun getCarrerasConvalidacion(carrerasConvalidacionRequest: CarrerasConvalidacionRequest): List<ResponseCarrerasConvalidacion>
    suspend fun getCursosConvalidacion(cursosConvalidacionRequest: CursosConvalidacionRequest): ResponseCursosConvalidacion
    suspend fun getTipoTrasladoConvalidacion(tipoTrasladoRequest: TipoTrasladoRequest): ResponseTipoTraslado
    suspend fun getEstudianteOAcadConvalidacion(estudianteOAcadConvalidacionRequest: EstudianteOAcadConvalidacionRequest): List<ResponseEstudianteOAcadConvalidacion>

    suspend fun getAsistenciaCarrera(dataCarreraRequest: DataCarreraRequest): List<ResponseCarreraRemote>
    suspend fun getPlanEstudioMalla(request: PlanEstudioRequest): List<ResponsePlanEstudio>
    suspend fun getResumenValoresPlan(request: ValoresPlanRequest): List<ResponseValoresPlan>
    suspend fun getTablaPlanEstudio(request: TablaPlanRequest): List<ResponseTablaPlan>
    suspend fun generarPdfMalla(request: GenerarPdfMallaRequest): ByteArray
    suspend fun getPerfilEstudiante(request: DataPerfilRequest): List<ResponsePerfilEstudiante>

    suspend fun getListaServicio(request: ServicioRequest): ResponseListaServicio
    suspend fun getServicioTipo(request: ServicioTipoRequest): ResponseTipoServicio
    suspend fun getTagsCompartidos(request: TagsArchivosRequest): ResponseTagsCompartidos
    suspend fun getContenidoTags(request: ContenidoTagsRequest): ResponseContenidoTags
    suspend fun getEstadoArchivo(request: EstadoArchivoRequest): ResponseEstadoArchivo

    suspend fun getLinksInstitucional(request: LinksItemRequest): ResponseLinksInstitucional

    suspend fun getCursosNotas(request: CursosRequest): ResponseCursos
    suspend fun getDetalleNotas(request: PromedioNotasRequest): ResponsePromedioNotas
    suspend fun getTiposTareasAcad(request: TiposTareasAcadRequest): ResponseTiposTareasAcad
    suspend fun getTareasAcad(request: TareasAcadRequest): ResponseTareasAcad
    suspend fun getHistorialNotas(request: HistorialNotasRequest): ResponseHistorialNotas

    suspend fun getAsignaturaEstudiante(request: AsignaturaEstudianteRequest): List<ResponseAsignaturaEstudiante>
    suspend fun getDetalleAsistencia(request: DetalleAsistenciaRequest): ResponseAsistencia

    suspend fun getHistorialAcademicoAlumno(request: HistorialAcademicoAlumnoRequest): List<ResponseHistorialAcademicoAlumno>
    suspend fun getHistorialAcademicoAlumnoDetalle(request: HistorialAcademicoAlumnoDetalleRequest): List<ResponseHistorialAcademicoAlumnoDetalle>

    suspend fun getBiblioteca(request: BibliotecaRequest): ResponseBiblioteca

    suspend fun getProyeccionValidacion(request: ValidaProyeccionRequest): ResponseProyeccionValidacion
    fun getEstadoMarcacionEstudiante(request: EstadoMarcacionRequest): Flow<ResponseEstadoMarcacion>
    suspend fun marcarAsistencia(request: MarcarRequest): ResponseMarcarAsistencia
    suspend fun logNavigation(request: NavigationLogRequest): ResponseNavigationLog

    suspend fun getAsignaturaEncuesta(request: AsignaturaEncuestaRequest): ResponseAsignaturaEncuesta
    suspend fun getEncuestaSatisfaccionEstado(request: EncuestaSatisfaccionEstadoRequest): ResponseEncuestaSatisfaccionEstado
    suspend fun getListarEncuesta(request: ListarEncuestaRequest): ResponseListarEncuesta
    suspend fun getEncuestaSatisfaccion(request: EncuestaSatisfaccionRequest): ResponseEncuestaSatisfaccion
    suspend fun guardarEncuestaSatisfaccion(request: EncuestaSatisfaccionGuardarRequest): ResponseGuardarEncuestaSatisfaccion

    suspend fun actualizarTokenApp(request: FcmTokenRequest): ResponseActualizarToken

    suspend fun getServicioCuentaCorriente(request: ServicioCuentaCorrienteRequest): ResponseServicioCuentaCorriente
    suspend fun getPeriodoCuentaCorriente(request: PeriodoCuentaCorrienteRequest): ResponsePeriodoCuentaCorriente
    suspend fun getListarCuentaCorriente(request: ListarCuentaCorrienteRequest): ResponseListarCuentaCorriente
    suspend fun getDetalleCuentaCorriente(request: DetalleCuentaCorrienteRequest): ResponseDetalleCuentaCorriente
    suspend fun getTemporalCuentaCorriente(request: TemporalCuentaCorrienteRequest): ResponseTemporalCuentaCorriente
    suspend fun getDeudasCuentasCorrientes(request: DeudasCuentasCorrientesRequest): ResponseDeudasCuentasCorrientes
    suspend fun getVerificarComprobante(request: VerificarComprobanteRequest): ResponseVerificarComprobante
    suspend fun getTextosHtml(request: TextosHtmlRequest): ResponseTextosHtml
    suspend fun getComprobantePecano(request: ComprobantePecanoRequest): ResponseComprobantePecano
    suspend fun getListarCampania(request: ListarCampaniaRequest): ResponseListarCampania
    suspend fun getSolicitarCampania(request: SolicitarCampaniaRequest): ResponseSolicitarCampania

    suspend fun getVerMatricula(request: VerMatriculaRequest): ResponseVerMatricula
    suspend fun getDetalleMatricula(request: DetalleMatriculaRequest): ResponseDetalleMatricula
    suspend fun getResumenHistorico(request: ResumenHistoricoRequest): ResponseResumenHistorico

    suspend fun getTramiteDocFiltro(request: TramiteDocFiltroRequest): ResponseTramiteDocFiltro
    suspend fun getTramiteFiltroJson(request: TramiteDocFiltroRequest): JsonObject
    suspend fun getTramitePaises(request: TramitePaisesRequest): ResponseTramitePaises
    suspend fun getDocumentosCreados(request: DocumentosCreadosRequest): ResponseTramitesDocumentos
    suspend fun crearTramitesEstudiante(request: CrearTramitesRequest): ResponseCrearTramites
    suspend fun validarEgresado(request: ValidarEgresadoRequest): ResponseValidarEgresado
    suspend fun registrarTramiteEstudiante(request: RegistrarTramiteRequest): ResponseRegistrarTramite
    suspend fun registrarTramiteTemp(request: RegistrarTramiteRequest): ResponseRequisitosTemp
    suspend fun getVerificarComprobanteTramite(request: ComprobanteTramiteRequest): ResponseComprobanteTramite
    suspend fun getCorreccionTramiteSave(request: CorreccionTramiteSaveRequest): ResponseCorreccionTramiteSave
    suspend fun guardarTramiteArchivo(request: GuardarArchivoTramiteRequest): ResponseRequisitosTemp
    suspend fun duplicadoTituloGuardar(request: DuplicadoTituloGuardarRequest): ResponseDuplicadoTituloGuardar

    suspend fun generarFichaMatricula(request: FichaMatriculaRequest): ByteArray

    suspend fun getObtenerEstudianteMatricula(request: ObtenerEstudianteMatriculaRequest): ResponseObtenerEstudianteMatricula
    suspend fun getListaMatriculaDeudas(request: ListaMatriculaDeudasRequest): ResponseListaMatriculaDeudas
    suspend fun getHoraPagoMatricula(request: HoraPagoMatriculaRequest): ResponseHoraPagoMatricula

    suspend fun getObtenerTurnoMatricula(request: ObtenerTurnoMatriculaRequest): ResponseObtenerTurnoMatricula

    suspend fun getValidarDocumentos(request: ValidarDocumentosRequest): ResponseValidarDocumentos
    suspend fun getValidarInicioMatricula(request: ValidarInicioMatriculaRequest): ResponseValidarInicioMatricula
    suspend fun registrarMatricula(request: RegistrarMatriculaBodyRequest): ResponseRegistrarMatricula
    suspend fun getHorarioPDF(request: HorarioPDFRequest): ResponseHorarioPDF
    suspend fun getMpPublicKey(idUneg: Int): ResponsePublicKeyMP
    suspend fun procesarPagoTarjeta(request: TarjetaRequest): ResponseMercadoPago
    suspend fun procesarPagoEfectivo(request: PagoEfectivoRequest): ResponseMercadoPago
    suspend fun getPasarelasActivas(request: PasarelasActivasRequest): ResponsePasarelasActivas

    suspend fun tokenizarYapeMP(phoneNumber: String, otp: String, publicKey: String): YapeTokenResponse

    suspend fun procesarPagoYape(request: YapeRequest): ResponseMercadoPago
    suspend fun registrarPagoMP(request: RegisterPaymentRequest)

    suspend fun getArchivosObligatorios(request: ArchivosObligatoriosRequest): ResponseArchivosObligatorios
}
