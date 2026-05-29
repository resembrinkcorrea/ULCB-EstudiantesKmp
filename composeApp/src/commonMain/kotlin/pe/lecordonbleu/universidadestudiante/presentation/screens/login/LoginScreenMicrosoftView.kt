package pe.lecordonbleu.universidadestudiante.presentation.screens.login

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.material3.*
import pe.lecordonbleu.universidadestudiante.presentation.components.StandardTopBar
import androidx.compose.runtime.*
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import coil3.compose.AsyncImage
import pe.lecordonbleu.universidadestudiante.MicrosoftLogin
import pe.lecordonbleu.universidadestudiante.MicrosoftLoginListener
import pe.lecordonbleu.universidadestudiante.SettingsStorage
import pe.lecordonbleu.universidadestudiante.getColorsTheme
import pe.lecordonbleu.universidadestudiante.getPlatformContext
import pe.lecordonbleu.universidadestudiante.getSettingsStorage
import pe.lecordonbleu.universidadestudiante.getSistemaCapByPlatform
import pe.lecordonbleu.universidadestudiante.logout
import pe.lecordonbleu.universidadestudiante.presentation.components.dialogs.CustomDialogBasic
import pe.lecordonbleu.universidadestudiante.presentation.components.LoadingDialog
import pe.lecordonbleu.universidadestudiante.presentation.vo.ResourceUiState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreenMicrosoftView(viewModel: LoginMicrosoftViewModel, navigator: NavController) {

    val sistema = getSistemaCapByPlatform()

    var isLogged by remember { mutableStateOf(false) }
    var loading by remember { mutableStateOf(false) }

    val settingsStorage: SettingsStorage = getSettingsStorage()
    val primerLogueo = settingsStorage.getInt("primerlogueo", 0)


    var displayName by remember { mutableStateOf("") }
    var jobTitle by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var officeLocation by remember { mutableStateOf("") }
    var mobilePhone by remember { mutableStateOf("") }
    var lastPasswordChangeDate by remember { mutableStateOf("") }

    var photoBytes by remember { mutableStateOf<ByteArray?>(null) }
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    var showDialog by remember { mutableStateOf(false) }
    var showLoading by remember { mutableStateOf(false) }
    var rememberMeState by remember { mutableStateOf(true) }
    var usuarioState by remember { mutableStateOf(settingsStorage.getString("Email", "") ?: "") }
    var contrasenaState by remember {
        mutableStateOf(
            settingsStorage.getString("Contrasenha", "") ?: ""
        )
    }
    var errorMessage by remember { mutableStateOf("") }
    var rstaLoginMicrosoft by remember { mutableStateOf(1) }
    var tipoLogin by remember { mutableStateOf(1) }

    var flagLogueo by remember { mutableStateOf(1) }
    var idUneg by remember { mutableStateOf(1) }
    var tipoConexion by remember { mutableStateOf("ANDROID Estudiante") }
    var ipConexion by remember { mutableStateOf("") }

    var loginChecked by remember { mutableStateOf(false) }

    var passwordScreenShown by remember { mutableStateOf(false) }

    var showDialogError by remember { mutableStateOf(false) }
    var errorTitle by remember { mutableStateOf("") }


    val platformContext = getPlatformContext()
    val colors = getColorsTheme()

    LaunchedEffect(primerLogueo) {

        if (primerLogueo == 1 && !loginChecked) {

            loginChecked = true
            loading = true

            MicrosoftLogin(
                platformContext,
                object : MicrosoftLoginListener {

                    override suspend fun onSuccess(
                        emailR: String,
                        displayNameM: String,
                        jobTitleM: String,
                        officeLocationM: String,
                        mobilePhoneM: String,
                        photoBytesM: ByteArray?,
                        lastPasswordChangeDateM: String
                    ) {
                        email = emailR
                        displayName = displayNameM
                        jobTitle = jobTitleM
                        officeLocation = officeLocationM
                        mobilePhone = mobilePhoneM
                        photoBytes = photoBytesM
                        lastPasswordChangeDate = lastPasswordChangeDateM
                        isLogged = true
                        loading = false
                    }

                    override fun onError(errorMessage: String) {
                        loading = false
                    }
                })
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(colors.backGroundColor)
    ) {

        StandardTopBar(
            title = "Login Microsoft",
            subtitle = "Office 365",
            onBackClick = { navigator.popBackStack() }
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        )
        {

            Spacer(modifier = Modifier.height(120.dp))

            Text("ULCB Sesión Microsoft", color = colors.textColor)

            Spacer(modifier = Modifier.height(60.dp))

            if (loading) {
                CircularProgressIndicator()
                Spacer(modifier = Modifier.height(30.dp))
            }

            if (isLogged) {

                photoBytes?.let { bytes ->
                    Surface(
                        shape = CircleShape,
                        color = Color.White,
                        shadowElevation = 10.dp
                    ) {
                        Surface(
                            shape = CircleShape,
                            color = Color.Transparent,
                            border = BorderStroke(
                                width = 4.dp,
                                brush = Brush.sweepGradient(
                                    colors = listOf(
                                        colors.colorMixPrimary,
                                        colors.colorAmbar,
                                        colors.colorMixPrimary
                                    )
                                )
                            ),
                            modifier = Modifier.size(120.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(4.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                AsyncImage(
                                    model = bytes,
                                    contentDescription = null,
                                    contentScale = androidx.compose.ui.layout.ContentScale.Crop,
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .clip(CircleShape)
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                Text(displayName, color = colors.textColor)
                Text(jobTitle, color = colors.textColor)
                Text(email, color = colors.textColor)
                Text("Oficina: $officeLocation", color = colors.textColor)
                Text("Teléfono: $mobilePhone", color = colors.textColor)

                Spacer(modifier = Modifier.height(40.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Button(
                        colors = ButtonDefaults.buttonColors(
                            containerColor = colors.primary,
                            contentColor = colors.colorBlanco
                        ),
                        onClick = {
                            loading = true
                            logout(platformContext, object : MicrosoftLoginListener {
                                override suspend fun onSuccess(
                                    email: String,
                                    displayNameM: String,
                                    jobTitleM: String,
                                    officeLocationM: String,
                                    mobilePhoneM: String,
                                    photoBytesM: ByteArray?,
                                    lastPasswordChangeDateM: String
                                ) {
                                    loading = false
                                    isLogged = false
                                    photoBytes = null
                                    settingsStorage.putInt("primerlogueo", 0)
                                }

                                override fun onError(errorMessage: String) {
                                    loading = false
                                }
                            })
                        }) {
                        Text("Cerrar Sesión", color = Color.White)
                    }

                    Spacer(modifier = Modifier.width(20.dp))

                    Button(
                        colors = ButtonDefaults.buttonColors(
                            containerColor = colors.primary,
                            contentColor = colors.colorBlanco
                        ),
                        onClick = {
                            if (email.isNotEmpty()) {
                                showLoading = true
                                flagLogueo = 3
                                viewModel.setUsuarioRequest(
                                    tipoLogin,
                                    rstaLoginMicrosoft,
                                    email,
                                    "",
                                    sistema,
                                    idUneg,
                                    tipoConexion,
                                    ipConexion,
                                    lastPasswordChangeDate
                                )
                            }
                        }) {
                        Text("Ingresar", color = Color.White)
                    }
                }

            } else {


                Button(
                    colors = ButtonDefaults.buttonColors
                        (
                        containerColor = colors.primary,
                        contentColor = colors.colorBlanco
                    ), onClick = {
                        loading = true

                        MicrosoftLogin(platformContext, object : MicrosoftLoginListener {

                            override suspend fun onSuccess(
                                emailR: String,
                                displayNameM: String,
                                jobTitleM: String,
                                officeLocationM: String,
                                mobilePhoneM: String,
                                photoBytesM: ByteArray?,
                                lastPasswordChangeDateM: String
                            ) {

                                email = emailR
                                displayName = displayNameM
                                jobTitle = jobTitleM
                                officeLocation = officeLocationM
                                mobilePhone = mobilePhoneM
                                photoBytes = photoBytesM
                                lastPasswordChangeDate = lastPasswordChangeDateM
                                settingsStorage.putInt("primerlogueo", 1)
                                isLogged = true
                                loading = false
                            }

                            override fun onError(errorMesg: String) {
                                showLoading = false
                                errorTitle = "Error de Autenticación"
                                errorMessage = errorMesg
                                showDialogError = true
                            }
                        })
                    }) {
                    Text("Siguiente", color = Color.White)
                }
            }
        }
    }

    when (uiState) {
        is ResourceUiState.Success -> {
            showLoading = false
            val firstUser =
                (uiState as ResourceUiState.Success<List<pe.lecordonbleu.universidadestudiante.data.remote.dto.ResponseLoginUser>>).data.firstOrNull()?.data_usuario?.firstOrNull()

            val idSistema = firstUser?.id_sistema ?: 0
            val idPerfil = firstUser?.id_perfil ?: 0
            val estUrlFoto = firstUser?.est_url_foto ?: ""
            val idPersDet = firstUser?.id_pers_det ?: 0
            val emailUser = firstUser?.usua_usuario ?: ""
            val idUsuario = firstUser?.idUsuario ?: 0
            val idTipoUsuario = firstUser?.id_tipo_usuario ?: 0
            val idEstud = firstUser?.id_estud ?: 0
            val estUrlPhoto = firstUser?.est_url_foto ?: ""

            val response = (uiState as ResourceUiState.Success<List<pe.lecordonbleu.universidadestudiante.data.remote.dto.ResponseLoginUser>>)
                .data
                .firstOrNull()

            val data1 = response?.jsonGeneral?.data1?.firstOrNull()

            val checkValida: Int =
                data1?.check_valida ?: -1

            val persNombre      = firstUser?.personaNombre ?: ""
            val persApellidoPat = firstUser?.pers_apellido_pat ?: ""
            val persApellidoMat = firstUser?.pers_apellido_mat ?: ""

            settingsStorage.putInt("idSistema", idSistema)
            settingsStorage.putInt("idPerfil", idPerfil)
            settingsStorage.putString("estUrlFoto", estUrlFoto)
            settingsStorage.putInt("idPersDet", idPersDet)
            settingsStorage.putString("emailUser", emailUser)
            settingsStorage.putInt("flagLogueo", flagLogueo)
            settingsStorage.putInt("idUsuario", idUsuario)
            settingsStorage.putInt("idTipoUsuario", idTipoUsuario)
            settingsStorage.putInt("idEstud", idEstud)
            settingsStorage.putString("estUrlPhoto", estUrlPhoto)
            settingsStorage.putString("persNombre", persNombre)
            settingsStorage.putString("persApellidoPat", persApellidoPat)
            settingsStorage.putString("persApellidoMat", persApellidoMat)

            if (rememberMeState) {
                settingsStorage.putString("Email", emailUser)
                settingsStorage.putString("Contrasenha", contrasenaState)
                settingsStorage.putInt("Session", 1)
            } else {
                settingsStorage.removeKey("Email")
                settingsStorage.removeKey("Contrasenha")
                settingsStorage.putInt("Session", -1)
            }
            navigator.navigate("/home/$idSistema/$idPerfil") {
                popUpTo("/login") { inclusive = true }
                launchSingleTop = true
            }
        }

        is ResourceUiState.Error -> {
            showLoading = false
            errorTitle = "Error de autenticación"
            errorMessage = (uiState as ResourceUiState.Error).message
            showDialogError = true
        }

        else -> {
            println("Estado Loading o vacío: $uiState")
            showDialog = false
        }
    }
    if (showLoading) {
        LoadingDialog()
    }
    if (showDialogError) {
        CustomDialogBasic(
            visible = true,
            titulo = errorTitle,
            mensaje = errorMessage,
            flag_val = 0,
            confirmado = false,
            aceptarSelected = 0,
            dismissOnOutsideClick = true,
            onDismiss = {
                showDialogError = false
                viewModel.resetUiState()
            }
        )
    }
}
