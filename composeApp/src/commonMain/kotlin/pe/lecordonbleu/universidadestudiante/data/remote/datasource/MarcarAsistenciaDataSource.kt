package pe.lecordonbleu.universidadestudiante.data.remote.datasource

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.serialization.json.Json
import pe.lecordonbleu.universidadestudiante.core.config.Constantes
import pe.lecordonbleu.universidadestudiante.data.remote.dto.ResponseEstadoMarcacion
import pe.lecordonbleu.universidadestudiante.domain.model.EstadoMarcacionRequest

class MarcarAsistenciaDataSource(private val httpClient: HttpClient) {

    fun getEstadoMarcacion(request: EstadoMarcacionRequest): Flow<ResponseEstadoMarcacion> = flow {
        val json = Json { ignoreUnknownKeys = true }
        while (true) {
            try {
                val response = httpClient.post("${Constantes.BASE_ROOT_INTRANET}${Constantes.URL_BASE_INTRANET}estadoMarcacionEstudiante") {
                    contentType(ContentType.Application.Json)
                    setBody(request)
                }
                val body = response.body<String>()
                emit(json.decodeFromString<ResponseEstadoMarcacion>(body))
            } catch (e: Exception) {
                emit(ResponseEstadoMarcacion(flag_val = 0))
            }
            delay(5000)
        }
    }
}
