package pe.lecordonbleu.universidadestudiante.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import io.ktor.client.HttpClient
import kotlinx.datetime.LocalDate
import org.koin.compose.viewmodel.koinViewModel
import communicationapp.createHttpClient
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import pe.lecordonbleu.universidadestudiante.data.repository.AppRepositoryImpl
import pe.lecordonbleu.universidadestudiante.data.repository.RepoImpl
import pe.lecordonbleu.universidadestudiante.presentation.screens.convalidacion.ConvalidacionScreen
import pe.lecordonbleu.universidadestudiante.presentation.screens.convalidacion.ConvalidacionViewModel
import pe.lecordonbleu.universidadestudiante.presentation.screens.eta.ETAScreen
import pe.lecordonbleu.universidadestudiante.presentation.screens.eta.ETAViewModel
import pe.lecordonbleu.universidadestudiante.presentation.screens.home.HomeScreen
import pe.lecordonbleu.universidadestudiante.presentation.screens.home.HomeViewModel
import pe.lecordonbleu.universidadestudiante.presentation.screens.horario.HorarioEstudianteScreen
import pe.lecordonbleu.universidadestudiante.presentation.screens.horario.HorarioPorDiaScreen
import pe.lecordonbleu.universidadestudiante.presentation.screens.horario.HorarioViewModel
import pe.lecordonbleu.universidadestudiante.presentation.screens.login.LoginMicrosoftViewModel
import pe.lecordonbleu.universidadestudiante.presentation.screens.login.LoginScreen
import pe.lecordonbleu.universidadestudiante.presentation.screens.login.LoginScreenMicrosoftView
import pe.lecordonbleu.universidadestudiante.presentation.screens.login.LoginViewModel
import pe.lecordonbleu.universidadestudiante.presentation.screens.mallacurricular.MallaCurricularScreen
import pe.lecordonbleu.universidadestudiante.presentation.screens.mallacurricular.MallaCurricularViewModel
import pe.lecordonbleu.universidadestudiante.presentation.screens.onboarding.OnBoardingScreen
import pe.lecordonbleu.universidadestudiante.presentation.screens.perfil.PerfilEstudianteScreen
import pe.lecordonbleu.universidadestudiante.presentation.screens.perfil.PerfilViewModel
import pe.lecordonbleu.universidadestudiante.presentation.screens.qr.QrScreen
import pe.lecordonbleu.universidadestudiante.presentation.screens.qr.QrViewModel
import pe.lecordonbleu.universidadestudiante.presentation.screens.archivoscompartidos.ArchivosCompartidosScreen
import pe.lecordonbleu.universidadestudiante.presentation.screens.archivoscompartidos.ArchivosCompartidosViewModel
import pe.lecordonbleu.universidadestudiante.presentation.screens.avisos.AnunciosScreen
import pe.lecordonbleu.universidadestudiante.presentation.screens.avisos.AnunciosViewModel
import pe.lecordonbleu.universidadestudiante.presentation.screens.misenlaces.MisEnlacesScreen
import pe.lecordonbleu.universidadestudiante.presentation.screens.misenlaces.MisEnlacesViewModel
import pe.lecordonbleu.universidadestudiante.presentation.screens.notas.DetalleNotasScreen
import pe.lecordonbleu.universidadestudiante.presentation.screens.notas.DetalleNotasViewModel
import pe.lecordonbleu.universidadestudiante.presentation.screens.notas.HistorialNotasScreen
import pe.lecordonbleu.universidadestudiante.presentation.screens.notas.HistorialNotasViewModel
import pe.lecordonbleu.universidadestudiante.presentation.screens.notas.NotasScreen
import pe.lecordonbleu.universidadestudiante.presentation.screens.notas.NotasViewModel
import pe.lecordonbleu.universidadestudiante.presentation.screens.misasistencias.DetalleAsistenciaScreen
import pe.lecordonbleu.universidadestudiante.presentation.screens.misasistencias.DetalleAsistenciaViewModel
import pe.lecordonbleu.universidadestudiante.presentation.screens.misasistencias.MisAsistenciasScreen
import pe.lecordonbleu.universidadestudiante.presentation.screens.misasistencias.MisAsistenciasViewModel
import pe.lecordonbleu.universidadestudiante.presentation.screens.historialacademico.HistorialAcademicoScreen
import pe.lecordonbleu.universidadestudiante.presentation.screens.historialacademico.HistorialAcademicoDetalleScreen
import pe.lecordonbleu.universidadestudiante.presentation.screens.historialacademico.HistorialAcademicoViewModel
import pe.lecordonbleu.universidadestudiante.presentation.screens.historialacademico.HistorialAcademicoDetalleViewModel
import pe.lecordonbleu.universidadestudiante.presentation.screens.biblioteca.BibliotecaScreen
import pe.lecordonbleu.universidadestudiante.presentation.screens.biblioteca.BibliotecaViewModel
import pe.lecordonbleu.universidadestudiante.presentation.screens.marcarasistencia.MarcarAsistenciaScreen
import pe.lecordonbleu.universidadestudiante.presentation.screens.marcarasistencia.MarcarAsistenciaViewModel
import pe.lecordonbleu.universidadestudiante.presentation.screens.cuentacorriente.CuentaCorrienteScreen
import pe.lecordonbleu.universidadestudiante.presentation.screens.cuentacorriente.CuentaCorrienteViewModel
import pe.lecordonbleu.universidadestudiante.presentation.screens.cuentacorriente.PagoFlywireScreen
import pe.lecordonbleu.universidadestudiante.presentation.screens.misofertas.MisOfertasScreen
import pe.lecordonbleu.universidadestudiante.presentation.screens.vermatricula.VerMatriculaScreen
import pe.lecordonbleu.universidadestudiante.presentation.screens.tramitedocumentario.TramiteDocumentarioScreen
import pe.lecordonbleu.universidadestudiante.presentation.screens.tramitedocumentario.TramiteDocumentarioViewModel
import pe.lecordonbleu.universidadestudiante.presentation.screens.tramitedocumentario.RegistrarTramiteDocumentarioScreen
import pe.lecordonbleu.universidadestudiante.presentation.screens.tramitedocumentario.RegistrarTramiteDocumentarioViewModel
import pe.lecordonbleu.universidadestudiante.presentation.screens.vermatricula.MatriculaScreen
import pe.lecordonbleu.universidadestudiante.presentation.screens.vermatricula.MatriculaViewModel


fun provideHttpClient(): HttpClient = createHttpClient()

@Composable
fun Navigation(
    navController: NavController = rememberNavController()
) {
    //val httpClient = provideHttpClient()    ---solo para debugear en el inspector

    val httpClient = remember {
        HttpClient {
            install(ContentNegotiation) {
                json(Json {
                    ignoreUnknownKeys = true
                    isLenient = true
                })
            }
        }
    }

    NavHost(
        navController = navController as NavHostController,
        startDestination = "/onboarding"
    ) {

        composable("/onboarding") {
            OnBoardingScreen(navController)
        }

        composable("/login") {
            val vm: LoginViewModel = viewModel { LoginViewModel(RepoImpl(httpClient)) }
            LoginScreen(vm, navController)
        }

        composable("/loginMicrosoftView") {
            val vm: LoginMicrosoftViewModel = viewModel { LoginMicrosoftViewModel(RepoImpl(httpClient)) }
            LoginScreenMicrosoftView(vm, navController)
        }

        composable("/eta") {
            val etaViewModel: ETAViewModel = viewModel { ETAViewModel(RepoImpl(httpClient)) }
            ETAScreen(etaViewModel, navController)
        }

        composable("/convalidacion") {
            val vm: ConvalidacionViewModel = viewModel { ConvalidacionViewModel(
                AppRepositoryImpl(httpClient)
            ) }
            ConvalidacionScreen(navigator = navController, convalidacionViewModel = vm)
        }

        composable("/malla") {
            val mallaCurricularViewModel: MallaCurricularViewModel = viewModel { MallaCurricularViewModel(RepoImpl(httpClient)) }
            MallaCurricularScreen(viewModel = mallaCurricularViewModel, navigator = navController)
        }

        composable("/qrEstudiante") {
            val vm: QrViewModel = koinViewModel()
            QrScreen(vm, navController)
        }

        composable("/perfilEstudiante") {
            val vm: PerfilViewModel = koinViewModel()
            PerfilEstudianteScreen(navigator = navController, viewModel = vm)
        }

        composable("/archivosCompartidos") {
            val vm: ArchivosCompartidosViewModel = koinViewModel()
            ArchivosCompartidosScreen(viewModel = vm, navigator = navController)
        }

        composable("/misEnlaces") {
            val vm: MisEnlacesViewModel = koinViewModel()
            MisEnlacesScreen(viewModel = vm, navigator = navController)
        }

        composable("/notas") {
            val vm: NotasViewModel = viewModel { NotasViewModel(AppRepositoryImpl(httpClient)) }
            NotasScreen(viewModel = vm, navigator = navController)
        }

        composable(
            route = "/detalleNotas/{idMatricNot}",
            arguments = listOf(
                navArgument("idMatricNot") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val idMatricNot = backStackEntry.savedStateHandle.get<String>("idMatricNot")?.toIntOrNull() ?: 0
            val vm: DetalleNotasViewModel = viewModel { DetalleNotasViewModel(AppRepositoryImpl(httpClient)) }
            DetalleNotasScreen(idMatricNot = idMatricNot, viewModel = vm, navigator = navController)
        }

        composable(
            route = "/historialNotas/{idEstudPe}/{idOacadArranque}",
            arguments = listOf(
                navArgument("idEstudPe") { type = NavType.StringType },
                navArgument("idOacadArranque") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val idEstudPe = backStackEntry.savedStateHandle.get<String>("idEstudPe")?.toIntOrNull() ?: 0
            val idOacadArranque = backStackEntry.savedStateHandle.get<String>("idOacadArranque")?.toIntOrNull() ?: 0
            val vm: HistorialNotasViewModel = viewModel { HistorialNotasViewModel(AppRepositoryImpl(httpClient)) }
            HistorialNotasScreen(idEstudPe = idEstudPe, idOacadArranque = idOacadArranque, viewModel = vm, navigator = navController)
        }

        composable("/misAsistencias") {
            val vm: MisAsistenciasViewModel = viewModel { MisAsistenciasViewModel(AppRepositoryImpl(httpClient)) }
            MisAsistenciasScreen(viewModel = vm, navigator = navController)
        }

        composable(
            route = "/detalleAsistencia/{idEstudPe}/{idMatricAsigSecc}/{nombreAsignatura}/{totalMaxInas}/{porcentajeInasistencia}/{detasismin}",
            arguments = listOf(
                navArgument("idEstudPe") { type = NavType.StringType },
                navArgument("idMatricAsigSecc") { type = NavType.StringType },
                navArgument("nombreAsignatura") { type = NavType.StringType },
                navArgument("totalMaxInas") { type = NavType.StringType },
                navArgument("porcentajeInasistencia") { type = NavType.StringType },
                navArgument("detasismin") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val idEstudPe = backStackEntry.savedStateHandle.get<String>("idEstudPe")?.toIntOrNull() ?: 0
            val idMatricAsigSecc = backStackEntry.savedStateHandle.get<String>("idMatricAsigSecc")?.toIntOrNull() ?: 0
            val nombreAsignatura = backStackEntry.savedStateHandle.get<String>("nombreAsignatura") ?: ""
            val totalMaxInas = backStackEntry.savedStateHandle.get<String>("totalMaxInas")?.toIntOrNull() ?: 0
            val porcentajeInasistencia = backStackEntry.savedStateHandle.get<String>("porcentajeInasistencia")?.toFloatOrNull() ?: 0f
            val detasismin = backStackEntry.savedStateHandle.get<String>("detasismin")?.toFloatOrNull() ?: 30f
            val vm: DetalleAsistenciaViewModel = viewModel { DetalleAsistenciaViewModel(AppRepositoryImpl(httpClient)) }
            DetalleAsistenciaScreen(
                viewModel = vm,
                idEstudPe = idEstudPe,
                idMatricAsigSecc = idMatricAsigSecc,
                nombreAsignatura = nombreAsignatura,
                totalMaxInas = totalMaxInas,
                porcentajeInasistencia = porcentajeInasistencia,
                detasismin = detasismin,
                navigator = navController
            )
        }

        composable("/historialAcademico") {
            val vm: HistorialAcademicoViewModel = viewModel { HistorialAcademicoViewModel(AppRepositoryImpl(httpClient)) }
            HistorialAcademicoScreen(viewModel = vm, navigator = navController)
        }

        composable(
            route = "/historialAcademicoDetalle/{idEstudPe}/{idPeracad}",
            arguments = listOf(
                navArgument("idEstudPe") { type = NavType.StringType },
                navArgument("idPeracad") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val idEstudPe = backStackEntry.savedStateHandle.get<String>("idEstudPe")?.toIntOrNull() ?: 0
            val idPeracad = backStackEntry.savedStateHandle.get<String>("idPeracad")?.toIntOrNull() ?: 0
            val vm: HistorialAcademicoDetalleViewModel = viewModel { HistorialAcademicoDetalleViewModel(AppRepositoryImpl(httpClient)) }
            HistorialAcademicoDetalleScreen(viewModel = vm, idEstudPe = idEstudPe, idPeracad = idPeracad, navigator = navController)
        }

        composable("/biblioteca") {
            val vm: BibliotecaViewModel = viewModel { BibliotecaViewModel(AppRepositoryImpl(httpClient)) }
            BibliotecaScreen(viewModel = vm, navigator = navController)
        }

        composable("/marcarAsistencia") {
            val vm: MarcarAsistenciaViewModel = viewModel { MarcarAsistenciaViewModel(AppRepositoryImpl(httpClient)) }
            MarcarAsistenciaScreen(viewModel = vm, navigator = navController)
        }

        composable("/cuentaCorriente") {
            val vm: CuentaCorrienteViewModel = koinViewModel()
            CuentaCorrienteScreen(viewModel = vm, navigator = navController)
        }

        composable(
            route = "/pagoFlywire/{codTransaccion}/{rutaRetorno}",
            arguments = listOf(
                navArgument("codTransaccion") { type = NavType.StringType },
                navArgument("rutaRetorno") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val codTransaccion = backStackEntry.savedStateHandle.get<String>("codTransaccion") ?: ""
            val rutaRetorno = backStackEntry.savedStateHandle.get<String>("rutaRetorno") ?: "cuentaCorriente"
            PagoFlywireScreen(codTransaccion = codTransaccion, rutaRetorno = rutaRetorno, navigator = navController)
        }

        composable("/horarioEstudiante") {
            val vm: HorarioViewModel = koinViewModel()
            HorarioEstudianteScreen(viewModel = vm, navigator = navController)
        }

        composable(
            route = "/horarioPorDia/{fecha}/{idEstudPe}/{idOacadArranque}",
            arguments = listOf(
                navArgument("fecha") { type = NavType.StringType },
                navArgument("idEstudPe") { type = NavType.StringType },
                navArgument("idOacadArranque") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val fecha = backStackEntry.savedStateHandle.get<String>("fecha") ?: ""
            val idEstudPe = backStackEntry.savedStateHandle.get<String>("idEstudPe")?.toIntOrNull() ?: 0
            val idOacadArranque = backStackEntry.savedStateHandle.get<String>("idOacadArranque")?.toIntOrNull() ?: 0
            val vm: HorarioViewModel = koinViewModel()
            HorarioPorDiaScreen(
                idStudPe = idEstudPe,
                idOacadArranque = idOacadArranque,
                fechaInicial = LocalDate.parse(fecha),
                viewModel = vm,
                onBack = { navController.popBackStack() }
            )
        }

        composable(
            route = "/home/{id_sistema}/{id_perfil}",
            arguments = listOf(
                navArgument("id_sistema") { type = NavType.StringType },
                navArgument("id_perfil") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val idSistema = backStackEntry.savedStateHandle.get<String>("id_sistema")?.toIntOrNull() ?: 0
            val idPerfil = backStackEntry.savedStateHandle.get<String>("id_perfil")?.toIntOrNull() ?: 0
            val vm: HomeViewModel = koinViewModel()
            HomeScreen(viewModel = vm, navigator = navController, idSistema = idSistema, idPerfil = idPerfil)
        }


        composable("/tramiteDocumentario") {
            val vm: TramiteDocumentarioViewModel = koinViewModel()
            TramiteDocumentarioScreen(viewModel = vm, navigator = navController)
        }

        composable("/registrarTramite") {
            val vm: RegistrarTramiteDocumentarioViewModel = koinViewModel()
            RegistrarTramiteDocumentarioScreen(
                viewModel = vm,
                navigator = navController
            )
        }
        composable("/misOfertas") {
            MisOfertasScreen(navController)
        }

        composable("/matricula") {
            val vm: MatriculaViewModel = koinViewModel()
            MatriculaScreen(
                viewModel = vm,
                navigator = navController
            )
        }

        composable("/misavisos") {
            val vm: AnunciosViewModel = viewModel {
                AnunciosViewModel(RepoImpl(httpClient))
            }
            AnunciosScreen(vm, navController)
        }

    }
}
