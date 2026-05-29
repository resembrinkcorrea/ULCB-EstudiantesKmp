package pe.lecordonbleu.universidadestudiante.domain.repository

import pe.lecordonbleu.universidadestudiante.data.remote.dto.ResponseAnuncios
import pe.lecordonbleu.universidadestudiante.data.remote.dto.ResponseCarreraRemote
import pe.lecordonbleu.universidadestudiante.data.remote.dto.ResponseDocumentoEta
import pe.lecordonbleu.universidadestudiante.data.remote.dto.ResponseEliminarDocEta
import pe.lecordonbleu.universidadestudiante.data.remote.dto.ResponseGuardarEta
import pe.lecordonbleu.universidadestudiante.data.remote.dto.ResponseHora
import pe.lecordonbleu.universidadestudiante.data.remote.dto.ResponseLoginUser
import pe.lecordonbleu.universidadestudiante.data.remote.dto.ResponsePeriodoEta
import pe.lecordonbleu.universidadestudiante.data.remote.dto.ResponsePlanEstudio
import pe.lecordonbleu.universidadestudiante.data.remote.dto.ResponseTablaPlan
import pe.lecordonbleu.universidadestudiante.data.remote.dto.ResponseValoresPlan
import pe.lecordonbleu.universidadestudiante.domain.model.AnunciosRequest
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

interface Repository {
    suspend fun getDataUsuario(userLoginRequest: UserLoginRequest): List<ResponseLoginUser>
    suspend fun getDataUsuarioCorreo(userRequest: UsuarioCorreoRequest): List<ResponseLoginUser>
    suspend fun getAsistenciaCarrera(dataCarreraRequest: DataCarreraRequest): List<ResponseCarreraRemote>
    suspend fun getPeriodoEta(periodoEtaRequest: PeriodoEtaRequest): List<ResponsePeriodoEta>
    suspend fun getDocumentosEta(documentosEtaRequest: DocumentosEtaRequest): List<ResponseDocumentoEta>
    suspend fun getGuardarEta(dataGuardarRequest: DataGuardarRequest): List<ResponseGuardarEta>
    suspend fun getEliminarDocEta(eliminarDocEtaRequest: EliminarDocEtaRequest): List<ResponseEliminarDocEta>
    suspend fun getPlanEstudioMalla(request: PlanEstudioRequest): List<ResponsePlanEstudio>
    suspend fun getResumenValoresPlan(request: ValoresPlanRequest): List<ResponseValoresPlan>
    suspend fun getTablaPlanEstudio(request: TablaPlanRequest): List<ResponseTablaPlan>
    suspend fun generarPdfMalla(request: GenerarPdfMallaRequest): ByteArray

    suspend fun getHoraServidor(): ResponseHora
    suspend fun getAnuncios(request: AnunciosRequest): ResponseAnuncios
}
