package pe.lecordonbleu.universidadestudiante.presentation.screens.login

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.rememberNavController
import org.koin.dsl.module
import org.koin.compose.KoinApplication
import pe.lecordonbleu.universidadestudiante.AndroidSettingsStorage
import pe.lecordonbleu.universidadestudiante.AppTheme
import pe.lecordonbleu.universidadestudiante.SettingsStorage
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
import pe.lecordonbleu.universidadestudiante.domain.repository.Repository

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun LoginScreenPreview() {
    val context = LocalContext.current
    KoinApplication(application = {
        modules(module {
            single<Context> { context }
            single<SettingsStorage> { AndroidSettingsStorage(context) }
        })
    }) {
        AppTheme {
            val fakeRepo = object : Repository {
                override suspend fun getDataUsuario(userLoginRequest: UserLoginRequest): List<ResponseLoginUser> = emptyList()
                override suspend fun getDataUsuarioCorreo(userRequest: UsuarioCorreoRequest): List<ResponseLoginUser> = emptyList()
                override suspend fun getAsistenciaCarrera(dataCarreraRequest: DataCarreraRequest): List<ResponseCarreraRemote> = emptyList()
                override suspend fun getPeriodoEta(periodoEtaRequest: PeriodoEtaRequest): List<ResponsePeriodoEta> = emptyList()
                override suspend fun getDocumentosEta(documentosEtaRequest: DocumentosEtaRequest): List<ResponseDocumentoEta> = emptyList()
                override suspend fun getGuardarEta(dataGuardarRequest: DataGuardarRequest): List<ResponseGuardarEta> = emptyList()
                override suspend fun getEliminarDocEta(eliminarDocEtaRequest: EliminarDocEtaRequest): List<ResponseEliminarDocEta> = emptyList()
                override suspend fun getPlanEstudioMalla(request: PlanEstudioRequest): List<ResponsePlanEstudio> = emptyList()
                override suspend fun getResumenValoresPlan(request: ValoresPlanRequest): List<ResponseValoresPlan> = emptyList()
                override suspend fun getTablaPlanEstudio(request: TablaPlanRequest): List<ResponseTablaPlan> = emptyList()
                override suspend fun generarPdfMalla(request: GenerarPdfMallaRequest): ByteArray = ByteArray(0)
                override suspend fun getHoraServidor(): ResponseHora = ResponseHora(flag_val = 0)
                override suspend fun getAnuncios(request: AnunciosRequest): ResponseAnuncios = ResponseAnuncios(data_notificaciones = emptyList())
            }
            LoginScreen(viewModel = LoginViewModel(fakeRepo), navigator = rememberNavController())
        }
    }
}
