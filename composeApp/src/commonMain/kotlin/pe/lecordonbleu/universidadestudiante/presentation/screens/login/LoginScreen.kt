package pe.lecordonbleu.universidadestudiante.presentation.screens.login

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface

import androidx.compose.foundation.gestures.awaitEachGesture
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.gestures.waitForUpOrCancellation
import androidx.compose.material.OutlinedTextField
import androidx.compose.runtime.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import ulcbintranetkmp.composeapp.generated.resources.Res
import ulcbintranetkmp.composeapp.generated.resources.contrasenha
import ulcbintranetkmp.composeapp.generated.resources.email_hint
import ulcbintranetkmp.composeapp.generated.resources.ingresar
import ulcbintranetkmp.composeapp.generated.resources.logo_365
import ulcbintranetkmp.composeapp.generated.resources.recordarme
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import pe.lecordonbleu.universidadestudiante.SettingsStorage
import pe.lecordonbleu.universidadestudiante.getPlatformContext
import pe.lecordonbleu.universidadestudiante.util.openUrl
import pe.lecordonbleu.universidadestudiante.getSettingsStorage
import pe.lecordonbleu.universidadestudiante.core.utils.NetworkUtils
import pe.lecordonbleu.universidadestudiante.LoadingIndicator
import pe.lecordonbleu.universidadestudiante.getColorsTheme
import pe.lecordonbleu.universidadestudiante.NotificationManagerPermission
import pe.lecordonbleu.universidadestudiante.getPlatform
import pe.lecordonbleu.universidadestudiante.core.extensions.LoginTextureOverlay
import pe.lecordonbleu.universidadestudiante.core.extensions.drawFoodAppBackground
import pe.lecordonbleu.universidadestudiante.presentation.components.CarruselLogos
import pe.lecordonbleu.universidadestudiante.data.remote.dto.ResponseLoginUser
import pe.lecordonbleu.universidadestudiante.domain.model.UtilsIcons
import pe.lecordonbleu.universidadestudiante.getSistemaCapByPlatform
import pe.lecordonbleu.universidadestudiante.presentation.components.ButtonComponent
import pe.lecordonbleu.universidadestudiante.presentation.components.CheckboxComponent
import pe.lecordonbleu.universidadestudiante.presentation.components.MyTextFieldComponent
import pe.lecordonbleu.universidadestudiante.presentation.components.PasswordTextFieldComponent
import pe.lecordonbleu.universidadestudiante.presentation.vo.ResourceUiState

@Composable
fun LoginScreen(
    viewModel: LoginViewModel,
    navigator: NavController
) {

    val sistema = getSistemaCapByPlatform()

    val settingsStorage: SettingsStorage = getSettingsStorage()
    val platformContext = getPlatformContext()

    var usuarioState by remember { mutableStateOf(settingsStorage.getString("Email", "") ?: "") }
    var contrasenaState by remember { mutableStateOf(settingsStorage.getString("Contrasenha", "") ?: "") }

    var rememberMeState by remember { mutableStateOf(true) }
    var notificacionesActivas by remember { mutableStateOf(false) }
    val notificationManager = remember {
        NotificationManagerPermission(platformContext)
    }


    val scope = rememberCoroutineScope()

    var showDialog by remember { mutableStateOf(false) }
    var sinConexion by remember { mutableStateOf(false) }
    var showLoading by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf("") }

    var rstaLoginMicrosoft by remember { mutableStateOf(0) }
    var tipoLogin by remember { mutableStateOf(0) }



    var flagLogueo by remember { mutableStateOf(0) }
    var idUneg by remember { mutableStateOf(1) }
    var tipoConexion by remember { mutableStateOf("ANDROID Estudiante") }
    var ipConexion by remember { mutableStateOf("") }
    var lastPwd by remember { mutableStateOf("") }

    var mostrarFormularioInstitucional by remember { mutableStateOf(false) }

    val colors = getColorsTheme()
    val platform = getPlatform()
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val focusManager = LocalFocusManager.current
    val earlyWarmupFocusRequester = remember { FocusRequester() }
    val keyboardController = LocalSoftwareKeyboardController.current

    val backgroundRatio = 0.45f

    LaunchedEffect(Unit) {
        viewModel.resetUiState()
        ipConexion = NetworkUtils.getPublicIPAddress().orEmpty()
    }

    LaunchedEffect("textWarmup") {
        delay(1000)
        try { earlyWarmupFocusRequester.requestFocus() } catch (e: Exception) {}
        try { earlyWarmupFocusRequester.freeFocus() } catch (e: Exception) {}
        keyboardController?.hide()
    }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colors.background
    )
    {

        Box(
            modifier = Modifier
                .fillMaxSize()
                .pointerInput(Unit) {
                    awaitEachGesture {
                        awaitFirstDown(requireUnconsumed = true)
                        val up = waitForUpOrCancellation()
                        if (up != null) focusManager.clearFocus()
                    }
                }
                .drawFoodAppBackground(
                    topColor = colors.colorAzulOscuro,
                    bottomColor = colors.backGroundColor,
                    blueHeightRatio = backgroundRatio
                )
        ) {
            LoginTextureOverlay(
                imageUrl = "https://mercadeo.blob.core.windows.net/applcb/login_background.png",
                tintColor = colors.backGroundColor,
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(1f - backgroundRatio)
                    .align(Alignment.BottomCenter)
            )

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(backgroundRatio),
                contentAlignment = Alignment.Center
            ) {
                CarruselLogos(modifier = Modifier.height(150.dp))
            }

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 30.dp)
                    .padding(top = 140.dp), // empuja los controles hacia bajo el logo
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                if (!mostrarFormularioInstitucional) {

                    Surface(
                        shape = RoundedCornerShape(24.dp),
                        color = colors.colorExpenseItem,
                        border = BorderStroke(
                            width = 2.dp,
                            brush = Brush.linearGradient(
                                colors = listOf(
                                    Color(0xFFC5A059),
                                    colors.colorExpenseItem,
                                    colors.colorMixPrimary
                                )
                            )
                        ),
                        shadowElevation = 20.dp,
                        modifier = Modifier.fillMaxWidth()
                    ) {

                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 32.dp, vertical = 40.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {

                            Text(
                                text = "PORTAL INSTITUCIONAL",
                                color = Color(0xFF9B865C),
                                letterSpacing = 2.sp,
                                fontSize = 11.sp
                            )

                            Spacer(modifier = Modifier.height(8.dp))

                            Text(
                                text = "BIENVENIDO",
                                color = colors.textColor,
                                fontSize = 28.sp,
                                fontWeight = FontWeight.ExtraBold
                            )

                            Spacer(modifier = Modifier.height(8.dp))

                            Text(
                                text = "Seleccione su método de ingreso.",
                                color = colors.textColor.copy(alpha = 0.6f),
                                textAlign = TextAlign.Center,
                                fontSize = 14.sp
                            )

                            Spacer(modifier = Modifier.height(24.dp))

                            HorizontalDivider(
                                modifier = Modifier.fillMaxWidth(0.3f),
                                color = Color(0xFF9B865C).copy(alpha = 0.4f),
                                thickness = 2.dp
                            )

                            Spacer(modifier = Modifier.height(32.dp))

                            Button(
                                onClick = {
                                    showLoading = true
                                    navigator.navigate("/loginMicrosoftView")
                                },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(56.dp),
                                shape = RoundedCornerShape(16.dp),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = colors.colorMixPrimary
                                )
                            ) {

                                Icon(
                                    painter = painterResource(Res.drawable.logo_365),
                                    contentDescription = null,
                                    modifier = Modifier.size(24.dp),
                                    tint = Color.Unspecified
                                )

                                Spacer(modifier = Modifier.width(12.dp))

                                Text(
                                    "Ingresar con Office 365",
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White
                                )
                            }

                            Spacer(modifier = Modifier.height(16.dp))

                            OutlinedButton(
                                onClick = { mostrarFormularioInstitucional = true },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(56.dp),
                                shape = RoundedCornerShape(16.dp),
                                border = BorderStroke(
                                    1.dp,
                                    colors.textColor.copy(alpha = 0.15f)
                                ),
                                colors = ButtonDefaults.outlinedButtonColors(
                                    contentColor = colors.textColor
                                )
                            ) {

                                Text(
                                    "Ingreso con correo",
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = colors.textColor
                                )
                            }
                        }
                    }
                    Spacer(modifier = Modifier.height(14.dp))
                    Text(
                        text = "Ver Manual del Estudiante",
                        color = colors.colorMixPrimary,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.SemiBold,
                        modifier = Modifier.clickable {
                            openUrl(
                                platformContext,
                                "https://docs.google.com/viewer?url=https://mercadeo.blob.core.windows.net/saainstituto/InstructivoILCB.pdf"
                            )
                        }
                    )
                }
                if (mostrarFormularioInstitucional) {

                    Surface(
                        shape = RoundedCornerShape(24.dp),
                        color = colors.colorExpenseItem,
                        border = BorderStroke(
                            width = 2.dp,
                            brush = Brush.linearGradient(
                                colors = listOf(
                                    Color(0xFFC5A059),
                                    colors.colorExpenseItem,
                                    colors.colorMixPrimary
                                )
                            )
                        ),
                        shadowElevation = 20.dp,
                        modifier = Modifier.fillMaxWidth()
                    ) {

                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 28.dp, vertical = 32.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {

                            Box(
                                modifier = Modifier.fillMaxWidth(),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = "LOGIN",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 18.sp,
                                    color = colors.textColor
                                )
                                Box(
                                    modifier = Modifier
                                        .align(Alignment.CenterStart)
                                        .size(36.dp)
                                        .background(
                                            color = colors.textColor.copy(alpha = 0.1f),
                                            shape = CircleShape
                                        )
                                        .clickable { mostrarFormularioInstitucional = false },
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                        contentDescription = "Volver",
                                        tint = colors.textColor,
                                        modifier = Modifier.size(20.dp)
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(30.dp))

                            MyTextFieldComponent(
                                labelValue = stringResource(Res.string.email_hint),
                                painterResource = UtilsIcons.MESSAGE.icon,
                                onTextChanged = { usuarioState = it },
                                initialValue = usuarioState
                            )

                            Spacer(modifier = Modifier.height(10.dp))

                            PasswordTextFieldComponent(
                                labelValue = stringResource(Res.string.contrasenha),
                                painterResource = UtilsIcons.PASSWORD.icon,
                                onTextSelected = { contrasenaState = it },
                                initialValue = contrasenaState
                            )

                            Spacer(modifier = Modifier.height(5.dp))

                            CheckboxComponent(
                                value = stringResource(Res.string.recordarme),
                                onTextSelected = {},
                                onCheckedChange = { rememberMeState = it }
                            )

                            Spacer(modifier = Modifier.height(5.dp))

                            CheckboxComponent(
                                value = "Recibir notificaciones",
                                onTextSelected = {},
                                initialValue = false,
                                onCheckedChange = {
                                    notificacionesActivas = it
                                    notificationManager.requestPermission(it)
                                }
                            )

                            Spacer(modifier = Modifier.height(20.dp))

                            ButtonComponent(
                                value = stringResource(Res.string.ingresar),
                                onButtonClicked = {
                                    if (usuarioState.isNotEmpty()) {
                                        showLoading = true
                                        scope.launch {
                                            val ip = NetworkUtils.getPublicIPAddress()
                                            if (ip == null) {
                                                showLoading = false
                                                sinConexion = true
                                                errorMessage = "Verificá tu conexión a internet e intentá de nuevo."
                                                showDialog = true
                                            } else {
                                                ipConexion = ip
                                                flagLogueo = 1
                                                viewModel.setUsuarioRequest(
                                                    tipoLogin,
                                                    rstaLoginMicrosoft,
                                                    usuarioState,
                                                    contrasenaState,
                                                    sistema,
                                                    idUneg,
                                                    tipoConexion,
                                                    ipConexion,
                                                    lastPwd
                                                )
                                            }
                                        }
                                    }
                                },
                                textSize = 12,
                                contentColor = Color.White,
                                backgroundColor = colors.primary
                            )
                        }
                    }
                }

            }
        }
    }

    when (uiState) {

        is ResourceUiState.Success -> {
            showLoading = false

            val response = (uiState as ResourceUiState.Success<List<ResponseLoginUser>>).data.firstOrNull()
            val firstUser = response?.data_usuario?.firstOrNull()
            val jsonGeneral = response?.jsonGeneral

            val idSistema         = firstUser?.id_sistema ?: 0
            val idPerfil          = firstUser?.id_perfil ?: 0
            val estUrlFoto        = firstUser?.est_url_foto ?: ""
            val idPersDet         = firstUser?.id_pers_det ?: 0
            val emailUser         = firstUser?.usua_usuario ?: ""
            val idUsuario         = firstUser?.idUsuario ?: 0
            val estUrlPhoto       = firstUser?.est_url_foto ?: ""
            val perfNombre        = firstUser?.perf_nombre ?: ""
            val idTipoUsuario     = firstUser?.id_tipo_usuario ?: 0
            val persNombre        = firstUser?.pers_nombre ?: ""
            val persApellidoPat   = firstUser?.pers_apellido_pat ?: ""
            val persApellidoMat   = firstUser?.pers_apellido_mat ?: ""
            val idAtribp          = firstUser?.id_atribp ?: 0
            val idEstud           = firstUser?.id_estud ?: 0
            val idUsuarioLogueo   = jsonGeneral?.data1?.firstOrNull()?.id_usuario_logueo ?: 0
            val numDocuIden       = firstUser?.num_docu_iden_pd ?: ""

            settingsStorage.putInt("idSistema", idSistema)
            settingsStorage.putInt("idPerfil", idPerfil)
            settingsStorage.putString("estUrlFoto", estUrlFoto)
            settingsStorage.putInt("idPersDet", idPersDet)
            settingsStorage.putString("emailUser", emailUser)
            settingsStorage.putInt("flagLogueo", flagLogueo)
            settingsStorage.putInt("idUsuario", idUsuario)
            settingsStorage.putString("estUrlPhoto", estUrlPhoto)
            settingsStorage.putString("perfNombre", perfNombre)
            settingsStorage.putInt("idTipoUsuario", idTipoUsuario)
            settingsStorage.putString("persNombre", persNombre)
            settingsStorage.putString("persApellidoPat", persApellidoPat)
            settingsStorage.putString("persApellidoMat", persApellidoMat)
            settingsStorage.putInt("idAtribp", idAtribp)
            settingsStorage.putInt("idEstud", idEstud)
            settingsStorage.putInt("idUsuarioLogueo", idUsuarioLogueo)
            settingsStorage.putString("numDocuIden", numDocuIden)

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
            sinConexion = false
            showDialog = true
            errorMessage = (uiState as ResourceUiState.Error).message
        }

        else -> {}
    }

    var textFieldWarmup by remember { mutableStateOf("") }
    OutlinedTextField(
        value = textFieldWarmup,
        onValueChange = { textFieldWarmup = it },
        modifier = Modifier.size(1.dp).alpha(0f).focusRequester(earlyWarmupFocusRequester)
    )

    if (showDialog) {
        LoginErrorDialog(
            mensaje = errorMessage,
            sinConexion = sinConexion,
            onDismiss = {
                viewModel.resetUiState()
                showDialog = false
                sinConexion = false
            }
        )
    }
    if (showLoading) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            LoadingIndicator(modifier = Modifier.size(50.dp))
        }
    }
}
