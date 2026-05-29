package pe.lecordonbleu.universidadestudiante.presentation.screens.qr

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Text
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import coil3.compose.AsyncImage
import io.ktor.util.encodeBase64
import pe.lecordonbleu.universidadestudiante.LoadingIndicator
import pe.lecordonbleu.universidadestudiante.SettingsStorage
import pe.lecordonbleu.universidadestudiante.getColorsTheme
import pe.lecordonbleu.universidadestudiante.getSettingsStorage
import pe.lecordonbleu.universidadestudiante.presentation.components.StandardTopBar
import pe.lecordonbleu.universidadestudiante.presentation.vo.ResourceUiState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun QrScreen(
    viewModel: QrViewModel,
    navigator: NavController
) {
    val colors = getColorsTheme()
    val settingsStorage: SettingsStorage = getSettingsStorage()

    val idEstud = settingsStorage.getInt("idEstud", 0)
    val urlFoto = settingsStorage.getString("estUrlFoto", "").orEmpty()
    val nombreUser = settingsStorage.getString("persNombre", "").orEmpty()
    val apellidoUser = buildString {
        append(settingsStorage.getString("persApellidoPat", "").orEmpty())
        val mat = settingsStorage.getString("persApellidoMat", "").orEmpty()
        if (mat.isNotEmpty()) append(" $mat")
    }
    val emailUser = settingsStorage.getString("emailUser", "").orEmpty()

    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    val scope = rememberCoroutineScope()

    var showLoading by remember { mutableStateOf(false) }
    var isRefreshing by remember { mutableStateOf(false) }
    var qrBitmap by remember { mutableStateOf<ImageBitmap?>(null) }
    var qrGenerado by remember { mutableStateOf(false) }

    LaunchedEffect(idEstud) {
            viewModel.setQRequest(idEstud)
    }

    Scaffold(
        topBar = {
            StandardTopBar(
                title = "Mi Código QR",
                subtitle = "IDENTIFICACIÓN ESTUDIANTIL",
                onBackClick = { navigator.popBackStack() }
            )
        },
        containerColor = colors.backGroundColor
    ) { paddingValues ->
        PullToRefreshBox(
            isRefreshing = isRefreshing,
            onRefresh = {
                isRefreshing = true
                qrGenerado = false
                qrBitmap = null
                viewModel.setQRequest(idEstud)
            },
            modifier = Modifier.padding(paddingValues)
        ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "$apellidoUser $nombreUser",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center,
                    color = colors.textColor
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = emailUser,
                    fontSize = 13.sp,
                    textAlign = TextAlign.Center,
                    color = colors.textColor.copy(alpha = 0.55f)
                )
                Spacer(modifier = Modifier.height(20.dp))

                Surface(
                    shape = CircleShape,
                    color = Color.White,
                    shadowElevation = 10.dp
                ) {
                    Surface(
                        shape = CircleShape,
                        color = Color.Transparent,
                        border = BorderStroke(
                            width = 6.dp,
                            brush = Brush.sweepGradient(
                                colors = listOf(
                                    colors.colorMixPrimary,
                                    colors.colorAmbar,
                                    colors.colorMixPrimary
                                )
                            )
                        ),
                        modifier = Modifier.size(200.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(6.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            AsyncImage(
                                model = urlFoto,
                                contentDescription = "User Photo",
                                contentScale = ContentScale.Crop,
                                modifier = Modifier
                                    .fillMaxSize()
                                    .clip(CircleShape)
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                if (qrBitmap != null) {
                    Surface(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 8.dp),
                        shape = RoundedCornerShape(22.dp),
                        color = colors.colorExpenseItem,
                        shadowElevation = 6.dp
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(24.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Surface(
                                shape = RoundedCornerShape(12.dp),
                                color = Color.White,
                                shadowElevation = 2.dp
                            ) {
                                Image(
                                    bitmap = qrBitmap!!,
                                    contentDescription = "QR Code",
                                    modifier = Modifier
                                        .size(240.dp)
                                        .padding(10.dp)
                                )
                            }
                        }
                    }
                }

                if (qrBitmap != null) {
                    Spacer(modifier = Modifier.height(26.dp))
                    Text(
                        text = "Presenta este codigo para identificarte",
                        fontSize = 14.sp,
                        textAlign = TextAlign.Center,
                        color = colors.textColor.copy(alpha = 0.6f)
                    )
                }
        }
        }
    }

    when (val s = uiState) {
        is ResourceUiState.Loading -> showLoading = true
        is ResourceUiState.Success -> {
            showLoading = false
            isRefreshing = false
            if (!qrGenerado) {
                qrGenerado = true
                val qrData = s.data.firstOrNull()
                val qrBase64 = qrData?.codigo_qr?.encodeBase64() ?: ""
                if (qrBase64.isNotEmpty()) generarQrBitmap(qrBase64, scope) { qrBitmap = it }
            }
        }
        is ResourceUiState.Error -> {
            showLoading = false
            isRefreshing = false
            qrBitmap = null
        }
        ResourceUiState.Empty -> {}
        else -> Unit
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

private fun generarQrBitmap(
    qrBase64: String,
    scope: CoroutineScope,
    onResult: (ImageBitmap) -> Unit
) {
    scope.launch(Dispatchers.Default) {
        val bitmap = qrgenerator.generateCode(qrBase64) ?: return@launch
        withContext(Dispatchers.Main) { onResult(bitmap) }
    }
}
