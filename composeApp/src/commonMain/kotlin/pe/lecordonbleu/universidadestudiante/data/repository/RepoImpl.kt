package pe.lecordonbleu.universidadestudiante.data.repository

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.bodyAsText
import io.ktor.http.ContentType
import io.ktor.http.contentType
import kotlinx.serialization.json.Json
import pe.lecordonbleu.universidadestudiante.data.remote.dto.ResponseCarreraRemote
import pe.lecordonbleu.universidadestudiante.data.remote.dto.ResponseDocumentoEta
import pe.lecordonbleu.universidadestudiante.data.remote.dto.ResponseEliminarDocEta
import pe.lecordonbleu.universidadestudiante.data.remote.dto.ResponseGuardarEta
import pe.lecordonbleu.universidadestudiante.data.remote.dto.ResponseLoginUser
import pe.lecordonbleu.universidadestudiante.data.remote.dto.ResponsePeriodoEta
import pe.lecordonbleu.universidadestudiante.data.remote.dto.ResponsePlanEstudio
import pe.lecordonbleu.universidadestudiante.data.remote.dto.ResponseTablaPlan
import pe.lecordonbleu.universidadestudiante.data.remote.dto.ResponseValoresPlan
import pe.lecordonbleu.universidadestudiante.domain.model.DataCarreraRequest
import pe.lecordonbleu.universidadestudiante.domain.model.DataGuardarRequest
import pe.lecordonbleu.universidadestudiante.domain.model.DocumentosEtaRequest
import pe.lecordonbleu.universidadestudiante.domain.model.EliminarDocEtaRequest
import pe.lecordonbleu.universidadestudiante.domain.model.GenerarPdfMallaRequest
import pe.lecordonbleu.universidadestudiante.domain.model.PeriodoEtaRequest
import pe.lecordonbleu.universidadestudiante.domain.model.PlanEstudioRequest
import pe.lecordonbleu.universidadestudiante.domain.model.TablaPlanRequest
import pe.lecordonbleu.universidadestudiante.domain.model.UserLoginRequest
import pe.lecordonbleu.universidadestudiante.domain.model.UsuarioCorreoRequest
import pe.lecordonbleu.universidadestudiante.domain.model.ValoresPlanRequest
import pe.lecordonbleu.universidadestudiante.core.config.Constantes
import pe.lecordonbleu.universidadestudiante.data.remote.dto.ResponseAnuncios
import pe.lecordonbleu.universidadestudiante.data.remote.dto.ResponseHora
import pe.lecordonbleu.universidadestudiante.domain.model.AnunciosRequest
import pe.lecordonbleu.universidadestudiante.domain.repository.Repository

class RepoImpl(private val httpClient: HttpClient) : Repository {

    private val json = Json { ignoreUnknownKeys = true }

    override suspend fun getDataUsuario(userLoginRequest: UserLoginRequest): List<ResponseLoginUser> {
        return try {
            val response = httpClient.post("${Constantes.BASE_ROOT_INTRANET}${Constantes.URL_BASE_GENERAL}login/logueoGeneral") {
                contentType(ContentType.Application.Json)
                setBody(userLoginRequest)
            }
            val responseBody = response.body<String>()
            println("JSON recibido: $responseBody")
            listOf(json.decodeFromString<ResponseLoginUser>(responseBody))
        } catch (e: Exception) {
            e.printStackTrace()
            emptyList()
        }
    }

    override suspend fun getDataUsuarioCorreo(userRequest: UsuarioCorreoRequest): List<ResponseLoginUser> {
        return try {
            val response = httpClient.post("${Constantes.BASE_ROOT_INTRANET}${Constantes.URL_BASE_GENERAL}logueoCorreoColaborador") {
                contentType(ContentType.Application.Json)
                setBody(userRequest)
            }
            val responseBody = response.body<String>()
            listOf(json.decodeFromString<ResponseLoginUser>(responseBody))
        } catch (e: Exception) {
            e.printStackTrace()
            emptyList()
        }
    }

    override suspend fun getAsistenciaCarrera(dataCarreraRequest: DataCarreraRequest): List<ResponseCarreraRemote> {
        return try {
            val response = httpClient.post("${Constantes.BASE_ROOT_INTRANET}${Constantes.URL_BASE_INTRANET}estudianteCarrera") {
                contentType(ContentType.Application.Json)
                setBody(dataCarreraRequest)
            }
            val responseBody = response.body<String>()
            println("JSONCarrera recibido: $responseBody")
            listOf(json.decodeFromString<ResponseCarreraRemote>(responseBody))
        } catch (e: Exception) {
            e.printStackTrace()
            emptyList()
        }
    }

    override suspend fun getPeriodoEta(periodoEtaRequest: PeriodoEtaRequest): List<ResponsePeriodoEta> {
        return try {
            val response = httpClient.post("${Constantes.BASE_ROOT_INTRANET}${Constantes.URL_BASE_INTRANET}etaPeriodo") {
                contentType(ContentType.Application.Json)
                setBody(periodoEtaRequest)
            }
            val responseBody = response.body<String>()
            println("JSONEta recibido: $responseBody")
            listOf(json.decodeFromString<ResponsePeriodoEta>(responseBody))
        } catch (e: Exception) {
            e.printStackTrace()
            emptyList()
        }
    }

    override suspend fun getDocumentosEta(documentosEtaRequest: DocumentosEtaRequest): List<ResponseDocumentoEta> {
        return try {
            val response = httpClient.post("${Constantes.BASE_ROOT_INTRANET}${Constantes.URL_BASE_INTRANET}documentosEta") {
                contentType(ContentType.Application.Json)
                setBody(documentosEtaRequest)
            }
            val responseBody = response.body<String>()
            listOf(json.decodeFromString<ResponseDocumentoEta>(responseBody))
        } catch (e: Exception) {
            e.printStackTrace()
            emptyList()
        }
    }

    override suspend fun getGuardarEta(dataGuardarRequest: DataGuardarRequest): List<ResponseGuardarEta> {
        return try {
            val response = httpClient.post("${Constantes.BASE_ROOT_INTRANET}${Constantes.URL_BASE_INTRANET}guardarDocumentosETA") {
                contentType(ContentType.Application.Json)
                setBody(dataGuardarRequest)
            }
            val responseBody = response.body<String>()
            listOf(json.decodeFromString<ResponseGuardarEta>(responseBody))
        } catch (e: Exception) {
            e.printStackTrace()
            emptyList()
        }
    }

    override suspend fun getEliminarDocEta(eliminarDocEtaRequest: EliminarDocEtaRequest): List<ResponseEliminarDocEta> {
        return try {
            val response = httpClient.post("${Constantes.BASE_ROOT_INTRANET}${Constantes.URL_BASE_INTRANET}deleteDocEta") {
                contentType(ContentType.Application.Json)
                setBody(eliminarDocEtaRequest)
            }
            val responseBody = response.body<String>()
            listOf(json.decodeFromString<ResponseEliminarDocEta>(responseBody))
        } catch (e: Exception) {
            e.printStackTrace()
            emptyList()
        }
    }

    override suspend fun getPlanEstudioMalla(request: PlanEstudioRequest): List<ResponsePlanEstudio> {
        return try {
            val response = httpClient.post("${Constantes.BASE_ROOT_INTRANET}${Constantes.URL_BASE_INTRANET}planEstudioMalla") {
                contentType(ContentType.Application.Json)
                setBody(request)
            }
            val responseBody = response.body<String>()
            listOf(json.decodeFromString<ResponsePlanEstudio>(responseBody))
        } catch (e: Exception) {
            e.printStackTrace()
            emptyList()
        }
    }

    override suspend fun getResumenValoresPlan(request: ValoresPlanRequest): List<ResponseValoresPlan> {
        return try {
            val response = httpClient.post("${Constantes.BASE_ROOT_INTRANET}${Constantes.URL_BASE_INTRANET}valoresPlanEstudio") {
                contentType(ContentType.Application.Json)
                setBody(request)
            }
            val responseBody = response.body<String>()
            listOf(json.decodeFromString<ResponseValoresPlan>(responseBody))
        } catch (e: Exception) {
            e.printStackTrace()
            emptyList()
        }
    }

    override suspend fun getTablaPlanEstudio(request: TablaPlanRequest): List<ResponseTablaPlan> {
        return try {
            val response = httpClient.post("${Constantes.BASE_ROOT_INTRANET}${Constantes.URL_BASE_INTRANET}tablaPlanEstudio") {
                contentType(ContentType.Application.Json)
                setBody(request)
            }
            val responseBody = response.body<String>()
            listOf(json.decodeFromString<ResponseTablaPlan>(responseBody))
        } catch (e: Exception) {
            e.printStackTrace()
            emptyList()
        }
    }

    override suspend fun generarPdfMalla(request: GenerarPdfMallaRequest): ByteArray {
        return try {
            val response = httpClient.post("${Constantes.BASE_FICHA_MTR}MallaCurricularServlet?accion=generarPdfMalla") {
                contentType(ContentType.Application.Json)
                setBody(request)
            }
            response.body<ByteArray>()
        } catch (e: Exception) {
            e.printStackTrace()
            ByteArray(0)
        }
    }


    override suspend fun getHoraServidor(): ResponseHora {
        val response = httpClient.post("${Constantes.BASE_ROOT_INTRANET}${Constantes.URL_BASE_GENERAL}intranetSAA/horaServidor") {
            contentType(ContentType.Application.Json)
        }

        return try {
            val responseBody = response.body<String>()
            val json = Json { ignoreUnknownKeys = true }
            json.decodeFromString<ResponseHora>(responseBody)
        } catch (e: Exception) {
            e.printStackTrace()
            ResponseHora(
                flag_val = 0,
                listHoraServer = emptyList()
            )
        }
    }

    override suspend fun getAnuncios(request: AnunciosRequest): ResponseAnuncios {
        return try {
            val url = "${Constantes.BASE_ROOT_INTRANET}${Constantes.URL_BASE_GENERAL}login/notificacionIntranet"
            val response = httpClient.post(url) {
                contentType(ContentType.Application.Json)
                setBody(request)
            }
            val body = response.bodyAsText()
            println("JSON recibido (getAnuncios): $body")
            Json { ignoreUnknownKeys = true }.decodeFromString<ResponseAnuncios>(body)
        } catch (e: Exception) {
            e.printStackTrace()
            ResponseAnuncios(data_notificaciones = emptyList())
        }
    }
}
