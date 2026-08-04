package pe.lecordonbleu.universidadestudiante.data.repository

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.jsonObject
import pe.lecordonbleu.universidadestudiante.core.config.Constantes
import pe.lecordonbleu.universidadestudiante.domain.model.FichaMatriculaRequest
import pe.lecordonbleu.universidadestudiante.data.remote.datasource.MarcarAsistenciaDataSource
import pe.lecordonbleu.universidadestudiante.data.remote.dto.ResponseActualizarToken
import pe.lecordonbleu.universidadestudiante.data.remote.dto.ResponseAsignaturaEncuesta
import pe.lecordonbleu.universidadestudiante.data.remote.dto.ResponseAsignaturaEstudiante
import pe.lecordonbleu.universidadestudiante.data.remote.dto.ResponseAsistencia
import pe.lecordonbleu.universidadestudiante.data.remote.dto.ResponseBiblioteca
import pe.lecordonbleu.universidadestudiante.data.remote.dto.ResponseCarrera
import pe.lecordonbleu.universidadestudiante.data.remote.dto.ResponseCarreraPlanEstudio
import pe.lecordonbleu.universidadestudiante.data.remote.dto.ResponseCarreraRemote
import pe.lecordonbleu.universidadestudiante.data.remote.dto.ResponseCarrerasConvalidacion
import pe.lecordonbleu.universidadestudiante.data.remote.dto.ResponseComprobantePecano
import pe.lecordonbleu.universidadestudiante.data.remote.dto.ResponseListarCampania
import pe.lecordonbleu.universidadestudiante.data.remote.dto.ResponseSolicitarCampania
import pe.lecordonbleu.universidadestudiante.domain.model.ListarCampaniaRequest
import pe.lecordonbleu.universidadestudiante.domain.model.SolicitarCampaniaRequest
import pe.lecordonbleu.universidadestudiante.data.remote.dto.ResponseComprobanteTramite
import pe.lecordonbleu.universidadestudiante.data.remote.dto.ResponseContenidoTags
import pe.lecordonbleu.universidadestudiante.data.remote.dto.ResponseCorreccionTramiteSave
import pe.lecordonbleu.universidadestudiante.data.remote.dto.ResponseCrearTramites
import pe.lecordonbleu.universidadestudiante.data.remote.dto.ResponseCursos
import pe.lecordonbleu.universidadestudiante.data.remote.dto.ResponseCursosConvalidacion
import pe.lecordonbleu.universidadestudiante.data.remote.dto.ResponseDataMenu
import pe.lecordonbleu.universidadestudiante.data.remote.dto.ResponseDetalleCuentaCorriente
import pe.lecordonbleu.universidadestudiante.data.remote.dto.ResponseDetalleMatricula
import pe.lecordonbleu.universidadestudiante.data.remote.dto.ResponseDeudasCuentasCorrientes
import pe.lecordonbleu.universidadestudiante.data.remote.dto.ResponseDuplicadoTituloGuardar
import pe.lecordonbleu.universidadestudiante.data.remote.dto.ResponseEncuestaSatisfaccion
import pe.lecordonbleu.universidadestudiante.data.remote.dto.ResponseEncuestaSatisfaccionEstado
import pe.lecordonbleu.universidadestudiante.data.remote.dto.ResponseEstadoArchivo
import pe.lecordonbleu.universidadestudiante.data.remote.dto.ResponseEstadoMarcacion
import pe.lecordonbleu.universidadestudiante.data.remote.dto.ResponseEstudianteOAcadConvalidacion
import pe.lecordonbleu.universidadestudiante.data.remote.dto.ResponseGuardarEncuestaSatisfaccion
import pe.lecordonbleu.universidadestudiante.data.remote.dto.ResponseHabilitarAulaDemo
import pe.lecordonbleu.universidadestudiante.data.remote.dto.ResponseHistorialAcademicoAlumno
import pe.lecordonbleu.universidadestudiante.data.remote.dto.ResponseHistorialAcademicoAlumnoDetalle
import pe.lecordonbleu.universidadestudiante.data.remote.dto.ResponseHistorialNotas
import pe.lecordonbleu.universidadestudiante.data.remote.dto.ResponseHora
import pe.lecordonbleu.universidadestudiante.data.remote.dto.ResponseHoraPagoMatricula
import pe.lecordonbleu.universidadestudiante.data.remote.dto.ResponseHorario
import pe.lecordonbleu.universidadestudiante.data.remote.dto.ResponseHorarioPDF
import pe.lecordonbleu.universidadestudiante.data.remote.dto.ResponseLinksInstitucional
import pe.lecordonbleu.universidadestudiante.data.remote.dto.ResponseListaMatriculaDeudas
import pe.lecordonbleu.universidadestudiante.data.remote.dto.ResponseListaServicio
import pe.lecordonbleu.universidadestudiante.data.remote.dto.ResponseListarCuentaCorriente
import pe.lecordonbleu.universidadestudiante.data.remote.dto.ResponseListarEncuesta
import pe.lecordonbleu.universidadestudiante.data.remote.dto.ResponseMarcarAsistencia
import pe.lecordonbleu.universidadestudiante.data.remote.dto.ResponseNavigationLog
import pe.lecordonbleu.universidadestudiante.data.remote.dto.ResponseObtenerEstudianteMatricula
import pe.lecordonbleu.universidadestudiante.data.remote.dto.ResponseObtenerTurnoMatricula
import pe.lecordonbleu.universidadestudiante.data.remote.dto.ResponsePerfilEstudiante
import pe.lecordonbleu.universidadestudiante.data.remote.dto.ResponsePeriodo
import pe.lecordonbleu.universidadestudiante.data.remote.dto.ResponsePeriodoCuentaCorriente
import pe.lecordonbleu.universidadestudiante.data.remote.dto.ResponsePlanEstudio
import pe.lecordonbleu.universidadestudiante.data.remote.dto.ResponsePromedioNotas
import pe.lecordonbleu.universidadestudiante.data.remote.dto.ResponseProyeccionValidacion
import pe.lecordonbleu.universidadestudiante.data.remote.dto.ResponseQr
import pe.lecordonbleu.universidadestudiante.data.remote.dto.ResponseRegistrarMatricula
import pe.lecordonbleu.universidadestudiante.data.remote.dto.ResponseRegistrarTramite
import pe.lecordonbleu.universidadestudiante.data.remote.dto.ResponseRequisitosTemp
import pe.lecordonbleu.universidadestudiante.data.remote.dto.ResponseResumenHistorico
import pe.lecordonbleu.universidadestudiante.data.remote.dto.ResponseServicioCuentaCorriente
import pe.lecordonbleu.universidadestudiante.data.remote.dto.ResponseTablaPlan
import pe.lecordonbleu.universidadestudiante.data.remote.dto.ResponseTagsCompartidos
import pe.lecordonbleu.universidadestudiante.data.remote.dto.ResponseTareasAcad
import pe.lecordonbleu.universidadestudiante.data.remote.dto.ResponseTemporalCuentaCorriente
import pe.lecordonbleu.universidadestudiante.data.remote.dto.ResponseTextosHtml
import pe.lecordonbleu.universidadestudiante.data.remote.dto.ResponseTipoServicio
import pe.lecordonbleu.universidadestudiante.data.remote.dto.ResponseTipoTraslado
import pe.lecordonbleu.universidadestudiante.data.remote.dto.ResponseTiposTareasAcad
import pe.lecordonbleu.universidadestudiante.data.remote.dto.ResponseTramiteDocFiltro
import pe.lecordonbleu.universidadestudiante.data.remote.dto.ResponseTramitePaises
import pe.lecordonbleu.universidadestudiante.data.remote.dto.ResponseTramitesDocumentos
import pe.lecordonbleu.universidadestudiante.data.remote.dto.ResponseValidarDocumentos
import pe.lecordonbleu.universidadestudiante.data.remote.dto.ResponseValidarEgresado
import pe.lecordonbleu.universidadestudiante.data.remote.dto.ResponseValidarInicioMatricula
import pe.lecordonbleu.universidadestudiante.data.remote.dto.ResponseValoresPlan
import pe.lecordonbleu.universidadestudiante.data.remote.dto.ResponseVerMatricula
import pe.lecordonbleu.universidadestudiante.data.remote.dto.ResponseVerificarComprobante
import pe.lecordonbleu.universidadestudiante.domain.model.AsignaturaEncuestaRequest
import pe.lecordonbleu.universidadestudiante.domain.model.AsignaturaEstudianteRequest
import pe.lecordonbleu.universidadestudiante.domain.model.BibliotecaRequest
import pe.lecordonbleu.universidadestudiante.domain.model.CarreraRequest
import pe.lecordonbleu.universidadestudiante.domain.model.CarrerasConvalidacionRequest
import pe.lecordonbleu.universidadestudiante.domain.model.ComprobantePecanoRequest
import pe.lecordonbleu.universidadestudiante.domain.model.ComprobanteTramiteRequest
import pe.lecordonbleu.universidadestudiante.domain.model.ContenidoTagsRequest
import pe.lecordonbleu.universidadestudiante.domain.model.CorreccionTramiteSaveRequest
import pe.lecordonbleu.universidadestudiante.domain.model.CrearTramitesRequest
import pe.lecordonbleu.universidadestudiante.domain.model.CursosConvalidacionRequest
import pe.lecordonbleu.universidadestudiante.domain.model.CursosRequest
import pe.lecordonbleu.universidadestudiante.domain.model.DataCarreraRequest
import pe.lecordonbleu.universidadestudiante.domain.model.DataPerfilRequest
import pe.lecordonbleu.universidadestudiante.domain.model.DetalleAsistenciaRequest
import pe.lecordonbleu.universidadestudiante.domain.model.DetalleCuentaCorrienteRequest
import pe.lecordonbleu.universidadestudiante.domain.model.DetalleMatriculaRequest
import pe.lecordonbleu.universidadestudiante.domain.model.DeudasCuentasCorrientesRequest
import pe.lecordonbleu.universidadestudiante.domain.model.DocumentosCreadosRequest
import pe.lecordonbleu.universidadestudiante.domain.model.DuplicadoTituloGuardarRequest
import pe.lecordonbleu.universidadestudiante.domain.model.EncuestaSatisfaccionEstadoRequest
import pe.lecordonbleu.universidadestudiante.domain.model.EncuestaSatisfaccionGuardarRequest
import pe.lecordonbleu.universidadestudiante.domain.model.EncuestaSatisfaccionRequest
import pe.lecordonbleu.universidadestudiante.domain.model.EstadoArchivoRequest
import pe.lecordonbleu.universidadestudiante.domain.model.EstadoMarcacionRequest
import pe.lecordonbleu.universidadestudiante.domain.model.EstudianteOAcadConvalidacionRequest
import pe.lecordonbleu.universidadestudiante.domain.model.FcmTokenRequest
import pe.lecordonbleu.universidadestudiante.domain.model.GenerarPdfMallaRequest
import pe.lecordonbleu.universidadestudiante.domain.model.GuardarArchivoTramiteRequest
import pe.lecordonbleu.universidadestudiante.domain.model.HabilitarAulaDemoRequest
import pe.lecordonbleu.universidadestudiante.domain.model.HistorialAcademicoAlumnoDetalleRequest
import pe.lecordonbleu.universidadestudiante.domain.model.HistorialAcademicoAlumnoRequest
import pe.lecordonbleu.universidadestudiante.domain.model.HistorialNotasRequest
import pe.lecordonbleu.universidadestudiante.domain.model.HoraPagoMatriculaRequest
import pe.lecordonbleu.universidadestudiante.domain.model.HorarioPDFRequest
import pe.lecordonbleu.universidadestudiante.domain.model.HorarioRequest
import pe.lecordonbleu.universidadestudiante.domain.model.LinksItemRequest
import pe.lecordonbleu.universidadestudiante.domain.model.ListaMatriculaDeudasRequest
import pe.lecordonbleu.universidadestudiante.domain.model.ListarCuentaCorrienteRequest
import pe.lecordonbleu.universidadestudiante.domain.model.ListarEncuestaRequest
import pe.lecordonbleu.universidadestudiante.domain.model.MarcarRequest
import pe.lecordonbleu.universidadestudiante.domain.model.NavigationLogRequest
import pe.lecordonbleu.universidadestudiante.domain.model.ObtenerEstudianteMatriculaRequest
import pe.lecordonbleu.universidadestudiante.domain.model.ObtenerTurnoMatriculaRequest
import pe.lecordonbleu.universidadestudiante.domain.model.PeriodoCuentaCorrienteRequest
import pe.lecordonbleu.universidadestudiante.domain.model.PeriodoRequest
import pe.lecordonbleu.universidadestudiante.domain.model.PlanEstudioRequest
import pe.lecordonbleu.universidadestudiante.domain.model.PromedioNotasRequest
import pe.lecordonbleu.universidadestudiante.domain.model.QrEntity
import pe.lecordonbleu.universidadestudiante.domain.model.RegistrarMatriculaBodyRequest
import pe.lecordonbleu.universidadestudiante.domain.model.RegistrarTramiteRequest
import pe.lecordonbleu.universidadestudiante.domain.model.ResumenHistoricoRequest
import pe.lecordonbleu.universidadestudiante.domain.model.ServicioCuentaCorrienteRequest
import pe.lecordonbleu.universidadestudiante.domain.model.ServicioRequest
import pe.lecordonbleu.universidadestudiante.domain.model.ServicioTipoRequest
import pe.lecordonbleu.universidadestudiante.domain.model.TablaPlanRequest
import pe.lecordonbleu.universidadestudiante.domain.model.TagsArchivosRequest
import pe.lecordonbleu.universidadestudiante.domain.model.TareasAcadRequest
import pe.lecordonbleu.universidadestudiante.domain.model.TemporalCuentaCorrienteRequest
import pe.lecordonbleu.universidadestudiante.domain.model.TextosHtmlRequest
import pe.lecordonbleu.universidadestudiante.domain.model.TipoTrasladoRequest
import pe.lecordonbleu.universidadestudiante.domain.model.TiposTareasAcadRequest
import pe.lecordonbleu.universidadestudiante.domain.model.TramiteDocFiltroRequest
import pe.lecordonbleu.universidadestudiante.domain.model.TramitePaisesRequest
import pe.lecordonbleu.universidadestudiante.domain.model.UserMenuRequest
import pe.lecordonbleu.universidadestudiante.domain.model.ValidaProyeccionRequest
import pe.lecordonbleu.universidadestudiante.domain.model.ValidarDocumentosRequest
import pe.lecordonbleu.universidadestudiante.domain.model.ValidarEgresadoRequest
import pe.lecordonbleu.universidadestudiante.domain.model.ValidarInicioMatriculaRequest
import pe.lecordonbleu.universidadestudiante.domain.model.ValoresPlanRequest
import pe.lecordonbleu.universidadestudiante.domain.model.VerMatriculaRequest
import pe.lecordonbleu.universidadestudiante.domain.model.VerificarComprobanteRequest
import pe.lecordonbleu.universidadestudiante.domain.repository.AppRepository

class AppRepositoryImpl(private val httpClient: HttpClient) : AppRepository {

    private val marcarAsistenciaDataSource = MarcarAsistenciaDataSource(httpClient)

    override suspend fun getMenuDataUser(userMenuRequest: UserMenuRequest): List<ResponseDataMenu> {
        val response =
            httpClient.post("${Constantes.BASE_ROOT_INTRANET}${Constantes.URL_BASE_GENERAL}login/menu") {
                contentType(ContentType.Application.Json)
                setBody(userMenuRequest)
            }
        return try {
            val responseBody = response.body<String>()
            val networkResponse = Json.decodeFromString<ResponseDataMenu>(responseBody)
            listOf(networkResponse)
        } catch (e: Exception) {
            e.printStackTrace()
            emptyList()
        }
    }

    override suspend fun getHabilitarAulaDemo(request: HabilitarAulaDemoRequest): ResponseHabilitarAulaDemo {
        val response =
            httpClient.post("${Constantes.BASE_ROOT_INTRANET}${Constantes.URL_BASE_INTRANET}habilitarAulaDemo") {
                contentType(ContentType.Application.Json)
                setBody(request)
            }
        return try {
            val responseBody = response.body<String>()
            val json = Json { ignoreUnknownKeys = true }
            json.decodeFromString<ResponseHabilitarAulaDemo>(responseBody)
        } catch (e: Exception) {
            e.printStackTrace()
            ResponseHabilitarAulaDemo(flag_val = 0, ListClaseHabilitada = emptyList())
        }
    }

    override suspend fun getHoraServidor(): ResponseHora {
        val response =
            httpClient.post("${Constantes.BASE_ROOT_INTRANET}${Constantes.URL_BASE_INTRANET}horaServidor") {
                contentType(ContentType.Application.Json)
            }
        return try {
            val responseBody = response.body<String>()
            val json = Json { ignoreUnknownKeys = true }
            json.decodeFromString<ResponseHora>(responseBody)
        } catch (e: Exception) {
            e.printStackTrace()
            ResponseHora(flag_val = 0, listHoraServer = emptyList())
        }
    }

    override suspend fun getQrUsuario(qrEntity: QrEntity): List<ResponseQr> {
        val response =
            httpClient.post("${Constantes.BASE_ROOT_INTRANET}${Constantes.URL_BASE_INTRANET}estudianteQr") {
                contentType(ContentType.Application.Json)
                setBody(qrEntity)
            }
        return try {
            val responseBody = response.body<String>()
            val networkResponse = Json.decodeFromString<ResponseQr>(responseBody)
            println(networkResponse)
            listOf(networkResponse)
        } catch (e: Exception) {
            e.printStackTrace()
            emptyList()
        }
    }

    override suspend fun getCarrera(request: CarreraRequest): ResponseCarrera {
        val response =
            httpClient.post("${Constantes.BASE_ROOT_INTRANET}${Constantes.URL_BASE_INTRANET}estudianteCarrera") {
                contentType(ContentType.Application.Json)
                setBody(request)
            }
        return try {
            val responseBody = response.body<String>()
            val json = Json { ignoreUnknownKeys = true }
            json.decodeFromString<ResponseCarrera>(responseBody)
        } catch (e: Exception) {
            e.printStackTrace()
            ResponseCarrera(flag_val = 0, carrera = emptyList())
        }
    }

    override suspend fun getPeriodo(request: PeriodoRequest): ResponsePeriodo {
        val response =
            httpClient.post("${Constantes.BASE_ROOT_INTRANET}${Constantes.URL_BASE_INTRANET}estudiantePeriodo") {
                contentType(ContentType.Application.Json)
                setBody(request)
            }
        return try {
            val responseBody = response.body<String>()
            val json = Json { ignoreUnknownKeys = true }
            json.decodeFromString<ResponsePeriodo>(responseBody)
        } catch (e: Exception) {
            e.printStackTrace()
            ResponsePeriodo(flag_val = 0, periodo = emptyList())
        }
    }

    override suspend fun getHorario(request: HorarioRequest): ResponseHorario {
        val response =
            httpClient.post("${Constantes.BASE_ROOT_INTRANET}${Constantes.URL_BASE_INTRANET}estudianteListadoHorario") {
                contentType(ContentType.Application.Json)
                setBody(request)
            }
        return try {
            val responseBody = response.body<String>()
            val json = Json { ignoreUnknownKeys = true }
            json.decodeFromString<ResponseHorario>(responseBody)
        } catch (e: Exception) {
            e.printStackTrace()
            ResponseHorario(flag_val = 0, listadoHorario = emptyList())
        }
    }


    override suspend fun getCarrerasConvalidacion(carrerasConvalidacionRequest: CarrerasConvalidacionRequest): List<ResponseCarrerasConvalidacion> {
        return try {
            val response =
                httpClient.post("${Constantes.BASE_ROOT_INTRANET}${Constantes.URL_BASE_INTRANET}carrerasConvalidacion") {
                    contentType(ContentType.Application.Json)
                    setBody(carrerasConvalidacionRequest)
                }

            val responseBody = response.body<String>()
            println("JSON recibido (getCarrerasConvalidacion): $responseBody")

            val json = Json { ignoreUnknownKeys = true }
            val parsed = json.decodeFromString<ResponseCarrerasConvalidacion>(responseBody)
            listOf(parsed)

        } catch (e: Exception) {
            e.printStackTrace()
            emptyList()
        }
    }

    override suspend fun getCursosConvalidacion(cursosConvalidacionRequest: CursosConvalidacionRequest): ResponseCursosConvalidacion {
        return try {
            val response =
                httpClient.post("${Constantes.BASE_ROOT_INTRANET}${Constantes.URL_BASE_INTRANET}cursosConvalidacion") {
                    contentType(ContentType.Application.Json)
                    setBody(cursosConvalidacionRequest)
                }

            val responseBody = response.body<String>()
            println("JSON recibido (getCursosConvalidacion): $responseBody")

            val json = Json { ignoreUnknownKeys = true }
            json.decodeFromString<ResponseCursosConvalidacion>(responseBody)

        } catch (e: Exception) {
            e.printStackTrace()
            ResponseCursosConvalidacion(flag_val = 0, ListCursosAcademica = emptyList())
        }
    }

    override suspend fun getCarreraPlanEstudio(dataCarreraRequest: DataCarreraRequest): ResponseCarreraPlanEstudio {
        return try {
            val response =
                httpClient.post("${Constantes.BASE_ROOT_INTRANET}${Constantes.URL_BASE_INTRANET}planEstudioConvalidacion") {
                    contentType(ContentType.Application.Json)
                    setBody(dataCarreraRequest)
                }

            val responseBody = response.body<String>()
            println("JSON recibido (getCursosConvalidacion): $responseBody")

            val json = Json { ignoreUnknownKeys = true }
            json.decodeFromString<ResponseCarreraPlanEstudio>(responseBody)

        } catch (e: Exception) {
            e.printStackTrace()
            ResponseCarreraPlanEstudio(flag_val = 0, ListPlanEstudioConv = emptyList())
        }
    }

    override suspend fun getTipoTrasladoConvalidacion(tipoTrasladoRequest: TipoTrasladoRequest): ResponseTipoTraslado {
        return try {
            val response =
                httpClient.post("${Constantes.BASE_ROOT_INTRANET}${Constantes.URL_BASE_INTRANET}tipoTrasladoConvalidacion") {
                    contentType(ContentType.Application.Json)
                    setBody(tipoTrasladoRequest)
                }

            val responseBody = response.body<String>()
            println("JSON recibido (getTipoTrasladoConvalidacion): $responseBody")

            val json = Json { ignoreUnknownKeys = true }
            json.decodeFromString<ResponseTipoTraslado>(responseBody)

        } catch (e: Exception) {
            e.printStackTrace()
            ResponseTipoTraslado(flag_val = 0, ListTipoTraslado = emptyList())
        }
    }

    override suspend fun getAsistenciaCarrera(dataCarreraRequest: DataCarreraRequest): List<ResponseCarreraRemote> {
        return try {
            val response =
                httpClient.post("${Constantes.BASE_ROOT_INTRANET}${Constantes.URL_BASE_INTRANET}estudianteCarrera") {
                    contentType(ContentType.Application.Json)
                    setBody(dataCarreraRequest)
                }
            val responseBody = response.body<String>()
            val json = Json { ignoreUnknownKeys = true }
            val parsed = json.decodeFromString<ResponseCarreraRemote>(responseBody)
            listOf(parsed)
        } catch (e: Exception) {
            e.printStackTrace()
            emptyList()
        }
    }

    override suspend fun getPlanEstudioMalla(request: PlanEstudioRequest): List<ResponsePlanEstudio> {
        return try {
            val response =
                httpClient.post("${Constantes.BASE_ROOT_INTRANET}${Constantes.URL_BASE_INTRANET}planEstudioMalla") {
                    contentType(ContentType.Application.Json)
                    setBody(request)
                }
            val responseBody = response.body<String>()
            val json = Json { ignoreUnknownKeys = true }
            val parsed = json.decodeFromString<ResponsePlanEstudio>(responseBody)
            listOf(parsed)
        } catch (e: Exception) {
            e.printStackTrace()
            emptyList()
        }
    }

    override suspend fun getResumenValoresPlan(request: ValoresPlanRequest): List<ResponseValoresPlan> {
        return try {
            val response =
                httpClient.post("${Constantes.BASE_ROOT_INTRANET}${Constantes.URL_BASE_INTRANET}valoresPlanEstudio") {
                    contentType(ContentType.Application.Json)
                    setBody(request)
                }
            val responseBody = response.body<String>()
            val json = Json { ignoreUnknownKeys = true }
            val parsed = json.decodeFromString<ResponseValoresPlan>(responseBody)
            listOf(parsed)
        } catch (e: Exception) {
            e.printStackTrace()
            emptyList()
        }
    }

    override suspend fun getTablaPlanEstudio(request: TablaPlanRequest): List<ResponseTablaPlan> {
        return try {
            val response =
                httpClient.post("${Constantes.BASE_ROOT_INTRANET}${Constantes.URL_BASE_INTRANET}tablaPlanEstudio") {
                    contentType(ContentType.Application.Json)
                    setBody(request)
                }
            val responseBody = response.body<String>()
            val json = Json { ignoreUnknownKeys = true }
            val parsed = json.decodeFromString<ResponseTablaPlan>(responseBody)
            listOf(parsed)
        } catch (e: Exception) {
            e.printStackTrace()
            emptyList()
        }
    }

    override suspend fun generarPdfMalla(request: GenerarPdfMallaRequest): ByteArray {
        return try {
            val response =
                httpClient.post("${Constantes.BASE_FICHA_MTR}MallaCurricularServlet?accion=generarPdfMalla") {
                    contentType(ContentType.Application.Json)
                    setBody(request)
                }
            response.body<ByteArray>()
        } catch (e: Exception) {
            e.printStackTrace()
            ByteArray(0)
        }
    }

    override suspend fun getEstudianteOAcadConvalidacion(estudianteOAcadConvalidacionRequest: EstudianteOAcadConvalidacionRequest): List<ResponseEstudianteOAcadConvalidacion> {
        return try {
            val response =
                httpClient.post("${Constantes.BASE_ROOT_INTRANET}${Constantes.URL_BASE_INTRANET}estudianteOAcadConvalidacion") {
                    contentType(ContentType.Application.Json)
                    setBody(estudianteOAcadConvalidacionRequest)
                }

            val responseBody = response.body<String>()
            println("JSON recibido (getEstudianteOAcadConvalidacion): $responseBody")

            val json = Json { ignoreUnknownKeys = true }
            val parsed = json.decodeFromString<ResponseEstudianteOAcadConvalidacion>(responseBody)
            listOf(parsed)

        } catch (e: Exception) {
            e.printStackTrace()
            emptyList()
        }
    }

    override suspend fun getPerfilEstudiante(dataPerfilRequest: DataPerfilRequest): List<ResponsePerfilEstudiante> {
        return try {
            val response =
                httpClient.post("${Constantes.BASE_ROOT_INTRANET}${Constantes.URL_BASE_INTRANET}perfilEstudiante") {
                    contentType(ContentType.Application.Json)
                    setBody(dataPerfilRequest)
                }

            val responseBody = response.body<String>()
            println("JSONPerfil recibido: $responseBody")

            val json = Json { ignoreUnknownKeys = true }
            val parsed = json.decodeFromString<ResponsePerfilEstudiante>(responseBody)
            listOf(parsed)

        } catch (e: Exception) {
            e.printStackTrace()
            emptyList()
        }
    }

    override suspend fun getListaServicio(request: ServicioRequest): ResponseListaServicio {
        return try {
            val response =
                httpClient.get("${Constantes.BASE_ROOT_INTRANET}${Constantes.URL_BASE_INTRANET}listarServicio") {
                    contentType(ContentType.Application.Json)
                    parameter("id_estud", request.id_estud)
                }
            val responseBody = response.body<String>()
            Json { ignoreUnknownKeys = true }.decodeFromString(responseBody)
        } catch (e: Exception) {
            e.printStackTrace()
            ResponseListaServicio(flag_val = 0, ListarServicio = emptyList())
        }
    }

    override suspend fun getServicioTipo(request: ServicioTipoRequest): ResponseTipoServicio {
        return try {
            val response =
                httpClient.get("${Constantes.BASE_ROOT_INTRANET}${Constantes.URL_BASE_INTRANET}listarTipoServicio") {
                    contentType(ContentType.Application.Json)
                    parameter("id_uneg", request.id_uneg)
                    parameter("id_estud", request.id_estud)
                    parameter("id_serv", request.id_serv)
                }
            val responseBody = response.body<String>()
            Json { ignoreUnknownKeys = true }.decodeFromString(responseBody)
        } catch (e: Exception) {
            e.printStackTrace()
            ResponseTipoServicio(flag_val = 0, ListarTipoServicio = emptyList())
        }
    }

    override suspend fun getTagsCompartidos(request: TagsArchivosRequest): ResponseTagsCompartidos {
        return try {
            val response =
                httpClient.get("${Constantes.BASE_ROOT_INTRANET}${Constantes.URL_BASE_INTRANET}tagsCompartidosEstudiante") {
                    contentType(ContentType.Application.Json)
                    parameter("id_uneg", request.id_uneg)
                    parameter("id_estud", request.id_estud)
                    parameter("id_tiposerva", request.id_tiposerva)
                    parameter("id_serv", request.id_serv)
                }
            val responseBody = response.body<String>()
            Json { ignoreUnknownKeys = true }.decodeFromString(responseBody)
        } catch (e: Exception) {
            e.printStackTrace()
            ResponseTagsCompartidos(0, emptyList())
        }
    }

    override suspend fun getContenidoTags(request: ContenidoTagsRequest): ResponseContenidoTags {
        return try {
            val response =
                httpClient.get("${Constantes.BASE_ROOT_INTRANET}${Constantes.URL_BASE_INTRANET}obtenerContenidoTag") {
                    contentType(ContentType.Application.Json)
                    parameter("id_uneg", request.id_uneg)
                    parameter("id_oferta_carpeta_det", request.id_oferta_carpeta_det)
                    parameter("id_usuario", request.id_usuario)
                }
            val responseBody = response.body<String>()
            Json { ignoreUnknownKeys = true }.decodeFromString(responseBody)
        } catch (e: Exception) {
            e.printStackTrace()
            ResponseContenidoTags(0, emptyList())
        }
    }

    override suspend fun getEstadoArchivo(request: EstadoArchivoRequest): ResponseEstadoArchivo {
        return try {
            val response =
                httpClient.post("${Constantes.BASE_ROOT_INTRANET}${Constantes.URL_BASE_INTRANET}estadoArchivoLectura") {
                    contentType(ContentType.Application.Json)
                    setBody(request)
                }
            val responseBody = response.body<String>()
            Json { ignoreUnknownKeys = true }.decodeFromString(responseBody)
        } catch (e: Exception) {
            e.printStackTrace()
            ResponseEstadoArchivo(0, emptyList())
        }
    }

    override suspend fun getLinksInstitucional(request: LinksItemRequest): ResponseLinksInstitucional {
        return try {
            val response =
                httpClient.post("${Constantes.BASE_ROOT_INTRANET}${Constantes.URL_BASE_INTRANET}linksIntitucional") {
                    contentType(ContentType.Application.Json)
                    setBody(request)
                }
            val responseBody = response.body<String>()
            Json { ignoreUnknownKeys = true }.decodeFromString(responseBody)
        } catch (e: Exception) {
            e.printStackTrace()
            ResponseLinksInstitucional(flag_val = 0, ListLinksInstitucional = emptyList())
        }
    }

    override suspend fun getCursosNotas(request: CursosRequest): ResponseCursos {
        return try {
            val response =
                httpClient.post("${Constantes.BASE_ROOT_INTRANET}${Constantes.URL_BASE_INTRANET}estudianteListadoNotas") {
                    contentType(ContentType.Application.Json)
                    setBody(request)
                }
            val responseBody = response.body<String>()
            Json { ignoreUnknownKeys = true }.decodeFromString(responseBody)
        } catch (e: Exception) {
            e.printStackTrace()
            ResponseCursos(flag_val = 0, listadoNotas = emptyList())
        }
    }

    override suspend fun getDetalleNotas(request: PromedioNotasRequest): ResponsePromedioNotas {
        return try {
            val response =
                httpClient.post("${Constantes.BASE_ROOT_INTRANET}${Constantes.URL_BASE_INTRANET}estudianteDetalleNotas") {
                    contentType(ContentType.Application.Json)
                    setBody(request)
                }
            val responseBody = response.body<String>()
            Json { ignoreUnknownKeys = true }.decodeFromString(responseBody)
        } catch (e: Exception) {
            e.printStackTrace()
            ResponsePromedioNotas(flag_val = 0, listadoNotasDetalle = emptyList())
        }
    }

    override suspend fun getTiposTareasAcad(request: TiposTareasAcadRequest): ResponseTiposTareasAcad {
        return try {
            val response =
                httpClient.post("${Constantes.BASE_ROOT_INTRANET}${Constantes.URL_BASE_INTRANET}estudianteTiposTareasAcad") {
                    contentType(ContentType.Application.Json)
                    setBody(request)
                }
            val responseBody = response.body<String>()
            Json { ignoreUnknownKeys = true }.decodeFromString(responseBody)
        } catch (e: Exception) {
            e.printStackTrace()
            ResponseTiposTareasAcad(listadoTiposTareaAcad = emptyList())
        }
    }

    override suspend fun getTareasAcad(request: TareasAcadRequest): ResponseTareasAcad {
        return try {
            val response =
                httpClient.post("${Constantes.BASE_ROOT_INTRANET}${Constantes.URL_BASE_INTRANET}estudianteTareasAcad") {
                    contentType(ContentType.Application.Json)
                    setBody(request)
                }
            val responseBody = response.body<String>()
            Json { ignoreUnknownKeys = true }.decodeFromString(responseBody)
        } catch (e: Exception) {
            e.printStackTrace()
            ResponseTareasAcad(flag_val = 0, listadoTareaAcad = emptyList())
        }
    }

    override suspend fun getHistorialNotas(request: HistorialNotasRequest): ResponseHistorialNotas {
        return try {
            val response =
                httpClient.post("${Constantes.BASE_ROOT_INTRANET}${Constantes.URL_BASE_INTRANET}estudianteHistorialNotas") {
                    contentType(ContentType.Application.Json)
                    setBody(request)
                }
            val responseBody = response.body<String>()
            Json { ignoreUnknownKeys = true }.decodeFromString(responseBody)
        } catch (e: Exception) {
            e.printStackTrace()
            ResponseHistorialNotas(flag_val = 0, listadoNotas = emptyList())
        }
    }

    override suspend fun getAsignaturaEstudiante(request: AsignaturaEstudianteRequest): List<ResponseAsignaturaEstudiante> {
        return try {
            val response =
                httpClient.post("${Constantes.BASE_ROOT_INTRANET}${Constantes.URL_BASE_INTRANET}estudianteAsignatura") {
                    contentType(ContentType.Application.Json)
                    setBody(request)
                }
            val responseBody = response.body<String>()
            val json = Json { ignoreUnknownKeys = true }
            val parsed = json.decodeFromString<ResponseAsignaturaEstudiante>(responseBody)
            listOf(parsed)
        } catch (e: Exception) {
            e.printStackTrace()
            emptyList()
        }
    }

    override suspend fun getDetalleAsistencia(request: DetalleAsistenciaRequest): ResponseAsistencia {
        return try {
            val response =
                httpClient.post("${Constantes.BASE_ROOT_INTRANET}${Constantes.URL_BASE_INTRANET}estudianteListadoCarrera") {
                    contentType(ContentType.Application.Json)
                    setBody(request)
                }
            val responseBody = response.body<String>()
            val json = Json { ignoreUnknownKeys = true }
            json.decodeFromString<ResponseAsistencia>(responseBody)
        } catch (e: Exception) {
            e.printStackTrace()
            ResponseAsistencia(flag_val = 0, listadoCarrera = emptyList())
        }
    }

    override suspend fun getHistorialAcademicoAlumno(request: HistorialAcademicoAlumnoRequest): List<ResponseHistorialAcademicoAlumno> {
        return try {
            val response =
                httpClient.post("${Constantes.BASE_ROOT_INTRANET}${Constantes.URL_BASE_INTRANET}HistorialAcademico") {
                    contentType(ContentType.Application.Json)
                    setBody(request)
                }
            val responseBody = response.body<String>()
            val json = Json { ignoreUnknownKeys = true }
            val parsed = json.decodeFromString<ResponseHistorialAcademicoAlumno>(responseBody)
            listOf(parsed)
        } catch (e: Exception) {
            e.printStackTrace()
            emptyList()
        }
    }

    override suspend fun getHistorialAcademicoAlumnoDetalle(request: HistorialAcademicoAlumnoDetalleRequest): List<ResponseHistorialAcademicoAlumnoDetalle> {
        return try {
            val response =
                httpClient.post("${Constantes.BASE_ROOT_INTRANET}${Constantes.URL_BASE_INTRANET}HistorialAcademicoDetalle") {
                    contentType(ContentType.Application.Json)
                    setBody(request)
                }
            val responseBody = response.body<String>()
            val json = Json { ignoreUnknownKeys = true }
            val parsed =
                json.decodeFromString<ResponseHistorialAcademicoAlumnoDetalle>(responseBody)
            listOf(parsed)
        } catch (e: Exception) {
            e.printStackTrace()
            emptyList()
        }
    }

    override suspend fun getBiblioteca(request: BibliotecaRequest): ResponseBiblioteca {
        return try {
            val response =
                httpClient.post("${Constantes.BASE_ROOT_INTRANET}${Constantes.URL_BASE_INTRANET}estudianteListadoBiblioteca") {
                    contentType(ContentType.Application.Json)
                    setBody(request)
                }
            val responseBody = response.body<String>()
            val json = Json { ignoreUnknownKeys = true }
            json.decodeFromString<ResponseBiblioteca>(responseBody)
        } catch (e: Exception) {
            e.printStackTrace()
            ResponseBiblioteca(flag_val = 0, listadoBiblioteca = emptyList())
        }
    }

    override suspend fun getProyeccionValidacion(request: ValidaProyeccionRequest): ResponseProyeccionValidacion {
        return try {
            val response =
                httpClient.post("${Constantes.BASE_ROOT_INTRANET}${Constantes.URL_BASE_INTRANET}ValidacionProyeccion") {
                    contentType(ContentType.Application.Json)
                    setBody(request)
                }
            val responseBody = response.body<String>()
            val json = Json { ignoreUnknownKeys = true }
            json.decodeFromString<ResponseProyeccionValidacion>(responseBody)
        } catch (e: Exception) {
            e.printStackTrace()
            ResponseProyeccionValidacion(flag_val = 0)
        }
    }

    override fun getEstadoMarcacionEstudiante(request: EstadoMarcacionRequest): Flow<ResponseEstadoMarcacion> =
        marcarAsistenciaDataSource.getEstadoMarcacion(request)
            .catch { throw Exception("Network call has failed", it) }

    override suspend fun marcarAsistencia(request: MarcarRequest): ResponseMarcarAsistencia {
        return try {
            val response =
                httpClient.post("${Constantes.BASE_ROOT_INTRANET}${Constantes.URL_BASE_INTRANET}marcarAsistenciaEstudiante") {
                    contentType(ContentType.Application.Json)
                    setBody(request)
                }
            val responseBody = response.body<String>()
            val json = Json { ignoreUnknownKeys = true }
            json.decodeFromString<ResponseMarcarAsistencia>(responseBody)
        } catch (e: Exception) {
            e.printStackTrace()
            ResponseMarcarAsistencia(flag_val = 0)
        }
    }

    override suspend fun logNavigation(request: NavigationLogRequest): ResponseNavigationLog {
        return try {
            val response =
                httpClient.post("${Constantes.BASE_ROOT_INTRANET}${Constantes.URL_BASE_INTRANET}guardarLogNav") {
                    contentType(ContentType.Application.Json)
                    setBody(request)
                }
            val responseBody = response.body<String>()
            val json = Json { ignoreUnknownKeys = true }
            json.decodeFromString<ResponseNavigationLog>(responseBody)
        } catch (e: Exception) {
            e.printStackTrace()
            ResponseNavigationLog(flag_val = 0)
        }
    }

    override suspend fun getAsignaturaEncuesta(request: AsignaturaEncuestaRequest): ResponseAsignaturaEncuesta {
        return try {
            val response =
                httpClient.post("${Constantes.BASE_ROOT_INTRANET}${Constantes.URL_BASE_INTRANET}estudianteEncuesta") {
                    contentType(ContentType.Application.Json)
                    setBody(request)
                }
            val responseBody = response.body<String>()
            val json = Json { ignoreUnknownKeys = true }
            json.decodeFromString<ResponseAsignaturaEncuesta>(responseBody)
        } catch (e: Exception) {
            e.printStackTrace()
            ResponseAsignaturaEncuesta(flag_val = 0)
        }
    }

    override suspend fun getEncuestaSatisfaccionEstado(request: EncuestaSatisfaccionEstadoRequest): ResponseEncuestaSatisfaccionEstado {
        return try {
            val response =
                httpClient.post("${Constantes.BASE_ROOT_INTRANET}${Constantes.URL_BASE_INTRANET}encuestaSatisfaccionEstudiante") {
                    contentType(ContentType.Application.Json)
                    setBody(request)
                }
            val responseBody = response.body<String>()
            val json = Json { ignoreUnknownKeys = true }
            json.decodeFromString<ResponseEncuestaSatisfaccionEstado>(responseBody)
        } catch (e: Exception) {
            e.printStackTrace()
            ResponseEncuestaSatisfaccionEstado(flag_val = 0)
        }
    }

    override suspend fun getListarEncuesta(request: ListarEncuestaRequest): ResponseListarEncuesta {
        return try {
            val response =
                httpClient.post("${Constantes.BASE_ROOT_INTRANET}${Constantes.URL_BASE_INTRANET}listarEncuesta") {
                    contentType(ContentType.Application.Json)
                    setBody(request)
                }
            val responseBody = response.body<String>()
            val json = Json { ignoreUnknownKeys = true }
            json.decodeFromString<ResponseListarEncuesta>(responseBody)
        } catch (e: Exception) {
            e.printStackTrace()
            ResponseListarEncuesta(flag_val = 0)
        }
    }

    override suspend fun getEncuestaSatisfaccion(request: EncuestaSatisfaccionRequest): ResponseEncuestaSatisfaccion {
        return try {
            val response =
                httpClient.post("${Constantes.BASE_ROOT_INTRANET}${Constantes.URL_BASE_INTRANET}encuestaSatisfaccionEstudiante") {
                    contentType(ContentType.Application.Json)
                    setBody(request)
                }
            val responseBody = response.body<String>()
            val json = Json { ignoreUnknownKeys = true }
            json.decodeFromString<ResponseEncuestaSatisfaccion>(responseBody)
        } catch (e: Exception) {
            e.printStackTrace()
            ResponseEncuestaSatisfaccion(flag_val = 0)
        }
    }

    override suspend fun guardarEncuestaSatisfaccion(request: EncuestaSatisfaccionGuardarRequest): ResponseGuardarEncuestaSatisfaccion {
        return try {
            val response =
                httpClient.post("${Constantes.BASE_ROOT_INTRANET}${Constantes.URL_BASE_INTRANET}guardarEncuestaSatisfaccion") {
                    contentType(ContentType.Application.Json)
                    setBody(request)
                }
            val responseBody = response.body<String>()
            val json = Json { ignoreUnknownKeys = true }
            json.decodeFromString<ResponseGuardarEncuestaSatisfaccion>(responseBody)
        } catch (e: Exception) {
            e.printStackTrace()
            ResponseGuardarEncuestaSatisfaccion(flag_val = 0)
        }
    }

    override suspend fun actualizarTokenApp(request: FcmTokenRequest): ResponseActualizarToken {
        return try {
            val response =
                httpClient.post("${Constantes.BASE_ROOT_INTRANET}${Constantes.URL_BASE_INTRANET}actualizarTokenApp") {
                    contentType(ContentType.Application.Json)
                    setBody(request)
                }
            val responseBody = response.body<String>()
            val json = Json { ignoreUnknownKeys = true }
            json.decodeFromString<ResponseActualizarToken>(responseBody)
        } catch (e: Exception) {
            e.printStackTrace()
            ResponseActualizarToken(flag_val = 0)
        }
    }

    override suspend fun getServicioCuentaCorriente(request: ServicioCuentaCorrienteRequest): ResponseServicioCuentaCorriente {
        return try {
            val response =
                httpClient.post("${Constantes.BASE_ROOT_INTRANET}${Constantes.URL_BASE_INTRANET}ServicioCuentaCorriente") {
                    contentType(ContentType.Application.Json)
                    setBody(request)
                }
            val responseBody = response.body<String>()
            val json = Json { ignoreUnknownKeys = true }
            json.decodeFromString<ResponseServicioCuentaCorriente>(responseBody)
        } catch (e: Exception) {
            e.printStackTrace()
            ResponseServicioCuentaCorriente(flag_val = 0)
        }
    }

    override suspend fun getPeriodoCuentaCorriente(request: PeriodoCuentaCorrienteRequest): ResponsePeriodoCuentaCorriente {
        return try {
            val response =
                httpClient.post("${Constantes.BASE_ROOT_INTRANET}${Constantes.URL_BASE_INTRANET}PeriodoCuentaCorriente") {
                    contentType(ContentType.Application.Json)
                    setBody(request)
                }
            val responseBody = response.body<String>()
            val json = Json { ignoreUnknownKeys = true }
            json.decodeFromString<ResponsePeriodoCuentaCorriente>(responseBody)
        } catch (e: Exception) {
            e.printStackTrace()
            ResponsePeriodoCuentaCorriente(flag_val = 0)
        }
    }

    override suspend fun getListarCuentaCorriente(request: ListarCuentaCorrienteRequest): ResponseListarCuentaCorriente {
        return try {
            val response =
                httpClient.post("${Constantes.BASE_ROOT_INTRANET}${Constantes.URL_BASE_INTRANET}ListaCuentaCorriente") {
                    contentType(ContentType.Application.Json)
                    setBody(request)
                }
            val responseBody = response.body<String>()
            val json = Json { ignoreUnknownKeys = true }
            json.decodeFromString<ResponseListarCuentaCorriente>(responseBody)
        } catch (e: Exception) {
            e.printStackTrace()
            ResponseListarCuentaCorriente(flag_val = 0)
        }
    }

    override suspend fun getDetalleCuentaCorriente(request: DetalleCuentaCorrienteRequest): ResponseDetalleCuentaCorriente {
        return try {
            val response =
                httpClient.post("${Constantes.BASE_ROOT_INTRANET}${Constantes.URL_BASE_INTRANET}DetalleCuentaCorriente") {
                    contentType(ContentType.Application.Json)
                    setBody(request)
                }
            val responseBody = response.body<String>()
            val json = Json { ignoreUnknownKeys = true }
            json.decodeFromString<ResponseDetalleCuentaCorriente>(responseBody)
        } catch (e: Exception) {
            e.printStackTrace()
            ResponseDetalleCuentaCorriente(flag_val = 0)
        }
    }

    override suspend fun getTemporalCuentaCorriente(request: TemporalCuentaCorrienteRequest): ResponseTemporalCuentaCorriente {
        return try {
            val response =
                httpClient.post("${Constantes.BASE_ROOT_INTRANET}${Constantes.URL_BASE_INTRANET}temporalCuentaCorriente") {
                    contentType(ContentType.Application.Json)
                    setBody(request.body)
                }
            val responseBody = response.body<String>()
            val json = Json { ignoreUnknownKeys = true }
            json.decodeFromString<ResponseTemporalCuentaCorriente>(responseBody)
        } catch (e: Exception) {
            e.printStackTrace()
            ResponseTemporalCuentaCorriente(flag_val = 0, mensaje = "", cod_transaccion = "")
        }
    }

    override suspend fun getDeudasCuentasCorrientes(request: DeudasCuentasCorrientesRequest): ResponseDeudasCuentasCorrientes {
        return try {
            val response =
                httpClient.post("${Constantes.BASE_ROOT_INTRANET}${Constantes.URL_BASE_INTRANET}DeudasCuentasCorrientes") {
                    contentType(ContentType.Application.Json)
                    setBody(request)
                }
            val responseBody = response.body<String>()
            val json = Json { ignoreUnknownKeys = true }
            json.decodeFromString<ResponseDeudasCuentasCorrientes>(responseBody)
        } catch (e: Exception) {
            e.printStackTrace()
            ResponseDeudasCuentasCorrientes(flag_val = 0)
        }
    }

    override suspend fun getVerificarComprobante(request: VerificarComprobanteRequest): ResponseVerificarComprobante {
        return try {
            val response = httpClient.post("${Constantes.BASE_PDF_FLYWIRE}obtener-comprobante") {
                contentType(ContentType.Application.Json)
                setBody(request)
            }
            val responseBody = response.body<String>()
            val json = Json { ignoreUnknownKeys = true }
            json.decodeFromString<ResponseVerificarComprobante>(responseBody)
        } catch (e: Exception) {
            e.printStackTrace()
            ResponseVerificarComprobante(
                statusCode = 0,
                mensaje = "",
                servicioUrl = "",
                metodo = "",
                excepcion = "",
                resultado = ""
            )
        }
    }

    override suspend fun getTextosHtml(request: TextosHtmlRequest): ResponseTextosHtml {
        return try {
            val response =
                httpClient.post("${Constantes.BASE_ROOT_INTRANET}${Constantes.URL_BASE_INTRANET}TextosHtml") {
                    contentType(ContentType.Application.Json)
                    setBody(request)
                }
            val responseBody = response.body<String>()
            val json = Json { ignoreUnknownKeys = true }
            json.decodeFromString<ResponseTextosHtml>(responseBody)
        } catch (e: Exception) {
            e.printStackTrace()
            ResponseTextosHtml(flag_val = 0, pdf = "")
        }
    }

    override suspend fun getComprobantePecano(request: ComprobantePecanoRequest): ResponseComprobantePecano {
        return try {
            val response = httpClient.post("${Constantes.URL_PDF_FLYWIREPECANO}Universidad") {
                contentType(ContentType.Application.Json)
                setBody(request)
            }
            val responseBody = response.body<String>()
            val json = Json { ignoreUnknownKeys = true }
            json.decodeFromString<ResponseComprobantePecano>(responseBody)
        } catch (e: Exception) {
            e.printStackTrace()
            ResponseComprobantePecano(enlacePDF = "")
        }
    }

    override suspend fun getListarCampania(request: ListarCampaniaRequest): ResponseListarCampania {
        return try {
            val response =
                httpClient.post("${Constantes.BASE_ROOT_INTRANET}${Constantes.URL_BASE_INTRANET}ListarCampania") {
                    contentType(ContentType.Application.Json)
                    setBody(request)
                }
            val responseBody = response.body<String>()
            val json = Json { ignoreUnknownKeys = true }
            json.decodeFromString<ResponseListarCampania>(responseBody)
        } catch (e: Exception) {
            e.printStackTrace()
            ResponseListarCampania(flag_val = 0)
        }
    }

    override suspend fun getSolicitarCampania(request: SolicitarCampaniaRequest): ResponseSolicitarCampania {
        return try {
            val response =
                httpClient.post("${Constantes.BASE_ROOT_INTRANET}${Constantes.URL_BASE_INTRANET}SolicitarCampania") {
                    contentType(ContentType.Application.Json)
                    setBody(request)
                }
            val responseBody = response.body<String>()
            val json = Json { ignoreUnknownKeys = true }
            json.decodeFromString<ResponseSolicitarCampania>(responseBody)
        } catch (e: Exception) {
            e.printStackTrace()
            ResponseSolicitarCampania(flag_val = 0)
        }
    }

    override suspend fun getVerMatricula(request: VerMatriculaRequest): ResponseVerMatricula {
        return try {
            val response =
                httpClient.post("${Constantes.BASE_ROOT_INTRANET}${Constantes.URL_BASE_INTRANET}VerMatricula") {
                    contentType(ContentType.Application.Json)
                    setBody(request)
                }
            val responseBody = response.body<String>()
            val json = Json { ignoreUnknownKeys = true }
            json.decodeFromString<ResponseVerMatricula>(responseBody)
        } catch (e: Exception) {
            e.printStackTrace()
            ResponseVerMatricula(flag_val = 0)
        }
    }

    override suspend fun getDetalleMatricula(request: DetalleMatriculaRequest): ResponseDetalleMatricula {
        return try {
            val response =
                httpClient.post("${Constantes.BASE_ROOT_INTRANET}${Constantes.URL_BASE_INTRANET}DetalleMatricula") {
                    contentType(ContentType.Application.Json)
                    setBody(request)
                }
            val responseBody = response.body<String>()
            val json = Json { ignoreUnknownKeys = true }
            json.decodeFromString<ResponseDetalleMatricula>(responseBody)
        } catch (e: Exception) {
            e.printStackTrace()
            ResponseDetalleMatricula(flag_val = 0)
        }
    }

    override suspend fun getResumenHistorico(request: ResumenHistoricoRequest): ResponseResumenHistorico {
        return try {
            val response =
                httpClient.post("${Constantes.BASE_ROOT_INTRANET}${Constantes.URL_BASE_INTRANET}ResumenHistorico") {
                    contentType(ContentType.Application.Json)
                    setBody(request)
                }
            val responseBody = response.body<String>()
            val json = Json { ignoreUnknownKeys = true }
            json.decodeFromString<ResponseResumenHistorico>(responseBody)
        } catch (e: Exception) {
            e.printStackTrace()
            ResponseResumenHistorico(flag_val = 0)
        }
    }

    override suspend fun getTramiteDocFiltro(request: TramiteDocFiltroRequest): ResponseTramiteDocFiltro {
        return try {
            val response =
                httpClient.post("${Constantes.BASE_ROOT_INTRANET}${Constantes.URL_BASE_INTRANET}listarfiltros") {
                    contentType(ContentType.Application.Json)
                    setBody(request)
                }
            val responseBody = response.body<String>()
            val json = Json { ignoreUnknownKeys = true }
            json.decodeFromString<ResponseTramiteDocFiltro>(responseBody)
        } catch (e: Exception) {
            e.printStackTrace()
            ResponseTramiteDocFiltro(flag_val = 0)
        }
    }

    override suspend fun getTramiteFiltroJson(request: TramiteDocFiltroRequest): JsonObject {
        return try {
            val response =
                httpClient.post("${Constantes.BASE_ROOT_INTRANET}${Constantes.URL_BASE_INTRANET}listarfiltros") {
                    contentType(ContentType.Application.Json)
                    setBody(request)
                }
            val responseBody = response.body<String>()
            Json.parseToJsonElement(responseBody).jsonObject
        } catch (e: Exception) {
            e.printStackTrace()
            JsonObject(mapOf("flag_val" to JsonPrimitive(0)))
        }
    }

    override suspend fun getTramitePaises(request: TramitePaisesRequest): ResponseTramitePaises {
        return try {
            val response =
                httpClient.post("${Constantes.BASE_ROOT_INTRANET}${Constantes.URL_BASE_INTRANET}obtenerPaises") {
                    contentType(ContentType.Application.Json)
                    setBody(request)
                }
            val responseBody = response.body<String>()
            val json = Json { ignoreUnknownKeys = true }
            json.decodeFromString<ResponseTramitePaises>(responseBody)
        } catch (e: Exception) {
            e.printStackTrace()
            ResponseTramitePaises(flag_val = 0)
        }
    }

    override suspend fun getDocumentosCreados(request: DocumentosCreadosRequest): ResponseTramitesDocumentos {
        return try {
            val response =
                httpClient.post("${Constantes.BASE_ROOT_INTRANET}${Constantes.URL_BASE_INTRANET}obtenerTramitesEstudiante") {
                    contentType(ContentType.Application.Json)
                    setBody(request)
                }
            val responseBody = response.body<String>()
            val json = Json { ignoreUnknownKeys = true }
            json.decodeFromString<ResponseTramitesDocumentos>(responseBody)
        } catch (e: Exception) {
            e.printStackTrace()
            ResponseTramitesDocumentos(flag_val = 0)
        }
    }

    override suspend fun crearTramitesEstudiante(request: CrearTramitesRequest): ResponseCrearTramites {
        return try {
            val response =
                httpClient.post("${Constantes.BASE_ROOT_INTRANET}${Constantes.URL_BASE_INTRANET}crearTramitesEstudiante") {
                    contentType(ContentType.Application.Json)
                    setBody(request)
                }
            val responseBody = response.body<String>()
            val json = Json { ignoreUnknownKeys = true }
            json.decodeFromString<ResponseCrearTramites>(responseBody)
        } catch (e: Exception) {
            e.printStackTrace()
            ResponseCrearTramites(flag_val = 0)
        }
    }

    override suspend fun validarEgresado(request: ValidarEgresadoRequest): ResponseValidarEgresado {
        return try {
            val response =
                httpClient.post("${Constantes.BASE_ROOT_INTRANET}${Constantes.URL_BASE_INTRANET}verificarEstadoEgresado") {
                    contentType(ContentType.Application.Json)
                    setBody(request)
                }
            val responseBody = response.body<String>()
            val json = Json { ignoreUnknownKeys = true }
            json.decodeFromString<ResponseValidarEgresado>(responseBody)
        } catch (e: Exception) {
            e.printStackTrace()
            ResponseValidarEgresado(flag_val = 0)
        }
    }

    override suspend fun registrarTramiteEstudiante(request: RegistrarTramiteRequest): ResponseRegistrarTramite {
        return try {
            val response =
                httpClient.post("${Constantes.BASE_ROOT_INTRANET}${Constantes.URL_BASE_INTRANET}guardarTramiteEstudiante") {
                    contentType(ContentType.Application.Json)
                    setBody(request)
                }
            val responseBody = response.body<String>()
            val json = Json { ignoreUnknownKeys = true }
            json.decodeFromString<ResponseRegistrarTramite>(responseBody)
        } catch (e: Exception) {
            e.printStackTrace()
            ResponseRegistrarTramite(flag_val = 0)
        }
    }

    override suspend fun registrarTramiteTemp(request: RegistrarTramiteRequest): ResponseRequisitosTemp {
        return try {
            val response =
                httpClient.post("${Constantes.BASE_ROOT_INTRANET}${Constantes.URL_BASE_INTRANET}guardarTramiteCheckEstudiante") {
                    contentType(ContentType.Application.Json)
                    setBody(request)
                }
            val responseBody = response.body<String>()
            val json = Json { ignoreUnknownKeys = true }
            json.decodeFromString<ResponseRequisitosTemp>(responseBody)
        } catch (e: Exception) {
            e.printStackTrace()
            ResponseRequisitosTemp(flag_val = 0, ListTempRequisito = emptyList())
        }
    }

    override suspend fun getVerificarComprobanteTramite(request: ComprobanteTramiteRequest): ResponseComprobanteTramite {
        return try {
            val response = httpClient.post("${Constantes.BASE_PDF_FLYWIRE}obtener-comprobante") {
                contentType(ContentType.Application.Json)
                setBody(request)
            }
            val responseBody = response.body<String>()
            val json = Json { ignoreUnknownKeys = true }
            json.decodeFromString<ResponseComprobanteTramite>(responseBody)
        } catch (e: Exception) {
            e.printStackTrace()
            ResponseComprobanteTramite(
                statusCode = 0,
                mensaje = "",
                servicioUrl = "",
                metodo = "",
                excepcion = null,
                resultado = ""
            )
        }
    }

    override suspend fun getCorreccionTramiteSave(request: CorreccionTramiteSaveRequest): ResponseCorreccionTramiteSave {
        return try {
            val response =
                httpClient.post("${Constantes.BASE_ROOT_INTRANET}${Constantes.URL_BASE_INTRANET}correccionTramiteEstudiante") {
                    contentType(ContentType.Application.Json)
                    setBody(request)
                }
            val responseBody = response.body<String>()
            val json = Json { ignoreUnknownKeys = true }
            json.decodeFromString<ResponseCorreccionTramiteSave>(responseBody)
        } catch (e: Exception) {
            e.printStackTrace()
            ResponseCorreccionTramiteSave(flag_val = 0)
        }
    }

    override suspend fun guardarTramiteArchivo(request: GuardarArchivoTramiteRequest): ResponseRequisitosTemp {
        return try {
            val response =
                httpClient.post("${Constantes.BASE_ROOT_INTRANET}${Constantes.URL_BASE_INTRANET}guardarTramiteArchivo") {
                    contentType(ContentType.Application.Json)
                    setBody(request)
                }
            val responseBody = response.body<String>()
            val json = Json { ignoreUnknownKeys = true }
            json.decodeFromString<ResponseRequisitosTemp>(responseBody)
        } catch (e: Exception) {
            e.printStackTrace()
            ResponseRequisitosTemp(flag_val = 0, ListTempRequisito = emptyList())
        }
    }

    override suspend fun duplicadoTituloGuardar(request: DuplicadoTituloGuardarRequest): ResponseDuplicadoTituloGuardar {
        return try {
            val response =
                httpClient.post("${Constantes.BASE_ROOT_INTRANET}${Constantes.URL_BASE_INTRANET}duplicadoTituloGuardar") {
                    contentType(ContentType.Application.Json)
                    setBody(request)
                }
            val responseBody = response.body<String>()
            val json = Json { ignoreUnknownKeys = true }
            json.decodeFromString<ResponseDuplicadoTituloGuardar>(responseBody)
        } catch (e: Exception) {
            e.printStackTrace()
            ResponseDuplicadoTituloGuardar(
                flag_val = 0,
                ListTempRequisito = emptyList()
            )
        }
    }

    override suspend fun generarFichaMatricula(request: FichaMatriculaRequest): ByteArray {
        return try {
            val response =
                httpClient.post("${Constantes.BASE_FICHA_MTR}GeneralControlClaseServlet?accion=generarPdfPreMatApp") {
                    contentType(ContentType.Application.Json)
                    setBody(request)
                }
            response.body<ByteArray>()
        } catch (e: Exception) {
            e.printStackTrace()
            ByteArray(0)
        }
    }

    override suspend fun getObtenerEstudianteMatricula(request: ObtenerEstudianteMatriculaRequest): ResponseObtenerEstudianteMatricula {
        return try {
            val response = httpClient.post("${Constantes.BASE_ROOT_INTRANET}${Constantes.URL_BASE_INTRANET}ObtenerEstudianteMatricula") {
                contentType(ContentType.Application.Json)
                setBody(request)
            }
            val responseBody = response.body<String>()
            val json = Json { ignoreUnknownKeys = true }
            json.decodeFromString<ResponseObtenerEstudianteMatricula>(responseBody)
        } catch (e: Exception) {
            e.printStackTrace()
            ResponseObtenerEstudianteMatricula(flag_val = 0)
        }
    }

    override suspend fun getListaMatriculaDeudas(request: ListaMatriculaDeudasRequest): ResponseListaMatriculaDeudas {
        return try {
            val response = httpClient.post("${Constantes.BASE_ROOT_INTRANET}${Constantes.URL_BASE_INTRANET}ValidacionMatricula") {
                contentType(ContentType.Application.Json)
                setBody(request)
            }
            val responseBody = response.body<String>()
            val json = Json { ignoreUnknownKeys = true }
            json.decodeFromString<ResponseListaMatriculaDeudas>(responseBody)
        } catch (e: Exception) {
            e.printStackTrace()
            ResponseListaMatriculaDeudas(flag_val = 0)
        }
    }

    override suspend fun getHoraPagoMatricula(request: HoraPagoMatriculaRequest): ResponseHoraPagoMatricula {
        return try {
            val response = httpClient.post("${Constantes.BASE_ROOT_INTRANET}${Constantes.URL_BASE_INTRANET}HoraPagoMatricula") {
                contentType(ContentType.Application.Json)
                setBody(request)
            }
            val responseBody = response.body<String>()
            val json = Json { ignoreUnknownKeys = true }
            json.decodeFromString<ResponseHoraPagoMatricula>(responseBody)
        } catch (e: Exception) {
            e.printStackTrace()
            ResponseHoraPagoMatricula(flag_val = 0)
        }
    }

    override suspend fun getObtenerTurnoMatricula(request: ObtenerTurnoMatriculaRequest): ResponseObtenerTurnoMatricula {
        return try {
            val response = httpClient.post("${Constantes.BASE_ROOT_INTRANET}${Constantes.URL_BASE_INTRANET}obtenerTurnoMatricula") {
                contentType(ContentType.Application.Json)
                setBody(request)
            }
            val responseBody = response.body<String>()
            val json = Json { ignoreUnknownKeys = true }
            json.decodeFromString<ResponseObtenerTurnoMatricula>(responseBody)
        } catch (e: Exception) {
            e.printStackTrace()
            ResponseObtenerTurnoMatricula(flag_val = 0)
        }
    }

    override suspend fun getValidarDocumentos(request: ValidarDocumentosRequest): ResponseValidarDocumentos {
        return try {
            val response = httpClient.post("${Constantes.BASE_ROOT_INTRANET}${Constantes.URL_BASE_INTRANET}ValidarDocumentosIngresante") {
                contentType(ContentType.Application.Json)
                setBody(request)
            }
            val responseBody = response.body<String>()
            val json = Json { ignoreUnknownKeys = true }
            json.decodeFromString<ResponseValidarDocumentos>(responseBody)
        } catch (e: Exception) {
            e.printStackTrace()
            ResponseValidarDocumentos(flag_val = 0)
        }
    }

    override suspend fun getValidarInicioMatricula(request: ValidarInicioMatriculaRequest): ResponseValidarInicioMatricula {
        return try {
            val response = httpClient.post("${Constantes.BASE_ROOT_INTRANET}${Constantes.URL_BASE_INTRANET}ValidarMatriculaIniMat") {
                contentType(ContentType.Application.Json)
                setBody(request)
            }
            val responseBody = response.body<String>()
            val json = Json { ignoreUnknownKeys = true }
            json.decodeFromString<ResponseValidarInicioMatricula>(responseBody)
        } catch (e: Exception) {
            e.printStackTrace()
            ResponseValidarInicioMatricula(flag_val = 0)
        }
    }

    override suspend fun registrarMatricula(request: RegistrarMatriculaBodyRequest): ResponseRegistrarMatricula {
        return try {
            val response = httpClient.post("${Constantes.BASE_ROOT_INTRANET}${Constantes.URL_BASE_INTRANET}RegistrarMatriculaEstudiante") {
                contentType(ContentType.Application.Json)
                setBody(request)
            }
            val responseBody = response.body<String>()
            val json = Json { ignoreUnknownKeys = true }
            json.decodeFromString<ResponseRegistrarMatricula>(responseBody)
        } catch (e: Exception) {
            e.printStackTrace()
            ResponseRegistrarMatricula(flag_val = 0, mensaje = e.message ?: "Error al registrar matricula", "Error", "", 0)
        }
    }

    override suspend fun getHorarioPDF(request: HorarioPDFRequest): ResponseHorarioPDF {
        return try {
            val response = httpClient.post("${Constantes.BASE_ROOT_INTRANET}${Constantes.URL_BASE_INTRANET}generarHorarioPDF") {
                contentType(ContentType.Application.Json)
                setBody(request)
            }
            val responseBody = response.body<String>()
            val json = Json { ignoreUnknownKeys = true }
            json.decodeFromString<ResponseHorarioPDF>(responseBody)
        } catch (e: Exception) {
            e.printStackTrace()
            ResponseHorarioPDF(flag_val = 0)
        }
    }

}
