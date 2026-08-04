package pe.lecordonbleu.universidadestudiante.core.di

import io.ktor.client.HttpClient
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import org.koin.dsl.module
import pe.lecordonbleu.universidadestudiante.data.repository.AppRepositoryImpl
import pe.lecordonbleu.universidadestudiante.SettingsStorage
import pe.lecordonbleu.universidadestudiante.getSettingsStorage
import pe.lecordonbleu.universidadestudiante.domain.repository.AppRepository
import pe.lecordonbleu.universidadestudiante.presentation.screens.home.HomeViewModel
import pe.lecordonbleu.universidadestudiante.presentation.screens.home.encuestasatisfaccion.EncuestaSatisfaccionViewModel
import pe.lecordonbleu.universidadestudiante.presentation.screens.horario.HorarioViewModel
import pe.lecordonbleu.universidadestudiante.presentation.screens.perfil.PerfilViewModel
import pe.lecordonbleu.universidadestudiante.presentation.screens.archivoscompartidos.ArchivosCompartidosViewModel
import pe.lecordonbleu.universidadestudiante.presentation.screens.misenlaces.MisEnlacesViewModel
import pe.lecordonbleu.universidadestudiante.presentation.screens.qr.QrViewModel
import pe.lecordonbleu.universidadestudiante.presentation.screens.cuentacorriente.CuentaCorrienteViewModel
import pe.lecordonbleu.universidadestudiante.presentation.screens.tramitedocumentario.TramiteDocumentarioViewModel
import pe.lecordonbleu.universidadestudiante.presentation.screens.tramitedocumentario.RegistrarTramiteDocumentarioViewModel
import pe.lecordonbleu.universidadestudiante.presentation.screens.vermatricula.MatriculaViewModel

fun appModule() = module {

    single<HttpClient> {
        HttpClient {
            install(ContentNegotiation) {
                json(Json {
                    ignoreUnknownKeys = true
                    isLenient = true
                })
            }
        }
    }

    single<SettingsStorage> { getSettingsStorage() }

    single<AppRepository> { AppRepositoryImpl(get()) }

    factory { HomeViewModel(get()) }
    factory { QrViewModel(get()) }
    factory { HorarioViewModel(get()) }
    factory { PerfilViewModel(get()) }
    factory { ArchivosCompartidosViewModel(get()) }
    factory { MisEnlacesViewModel(get()) }
    factory { EncuestaSatisfaccionViewModel(get()) }
    factory { CuentaCorrienteViewModel(get()) }
    factory { MatriculaViewModel(get()) }
    factory { TramiteDocumentarioViewModel(get()) }
    factory { RegistrarTramiteDocumentarioViewModel(get()) }
}
