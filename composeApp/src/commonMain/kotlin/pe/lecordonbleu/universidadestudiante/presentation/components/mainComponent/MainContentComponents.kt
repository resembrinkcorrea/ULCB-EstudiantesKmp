package pe.lecordonbleu.universidadestudiante.presentation.components.mainComponent

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.rounded.AccountBalanceWallet
import androidx.compose.material.icons.rounded.CalendarMonth
import androidx.compose.material.icons.rounded.ChevronRight
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import ulcbintranetkmp.composeapp.generated.resources.Res
import ulcbintranetkmp.composeapp.generated.resources.imgdefault
import org.jetbrains.compose.resources.painterResource
import pe.lecordonbleu.universidadestudiante.DarkModeColors
import pe.lecordonbleu.universidadestudiante.getPlatform
import pe.lecordonbleu.universidadestudiante.getTodayLocalDate
import pe.lecordonbleu.universidadestudiante.core.theme.menuLabelFontFamily
import pe.lecordonbleu.universidadestudiante.data.remote.dto.DataMenu
import pe.lecordonbleu.universidadestudiante.data.remote.dto.Horario
import pe.lecordonbleu.universidadestudiante.domain.usecase.agruparHorasClase
import pe.lecordonbleu.universidadestudiante.presentation.screens.horario.customcell.ClaseCard

// ─────────────────────────────────────────────────────
// CONTENIDO PRINCIPAL DEL HOME
// ─────────────────────────────────────────────────────
@Composable
fun HomeContent(
    menus: List<DataMenu>,
    isLoading: Boolean,
    colors: DarkModeColors,
    onMenuClick: (DataMenu) -> Unit,
    onNavigate: (String) -> Unit,
    showFichaMatri: Boolean = false,
    isFichaMatriLoading: Boolean = false,
    onFichaMatriClick: () -> Unit = {},
    clasesHoy: List<Horario> = emptyList(),
    showClasesHoy: Boolean = false,
    onClasesHoyClose: () -> Unit = {}
) {
    if (isLoading && menus.isEmpty()) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator(color = colors.colorMixPrimary)
        }
        return
    }

    fun menuImagen(abrev: String) = menus.firstOrNull { it.textoMenuAbrev == abrev }?.imagen.orEmpty()

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 16.dp, vertical = 8.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        TarjetaQrAcceso(
            imageUrl = menuImagen("MI QR"),
            colors = colors,
            onClick = { onNavigate("/qrEstudiante") }
        )

        GrillaModulosPrincipales(
            notasImageUrl     = menuImagen("MIS NOTAS"),
            horarioImageUrl   = menuImagen("MI HORARIO").ifEmpty { menuImagen("HORARIO") },
            asistImageUrl     = menuImagen("MIS ASISTENCIAS"),
            bibImageUrl       = menuImagen("BIBLIOTECA"),
            notasBgUrl        = "https://mercadeo.blob.core.windows.net/applcb/ilcb_curso_0063.jpg",
            horarioBgUrl      = "https://mercadeo.blob.core.windows.net/applcb/ilcb_curso_0021.jpg",
            asistBgUrl        = "https://mercadeo.blob.core.windows.net/applcb/ilcb_curso_0070.jpg",
            bibBgUrl          = "https://mercadeo.blob.core.windows.net/applcb/ilcb_curso_0047.jpg",
            colors = colors,
            onNotas      = { onNavigate("/notas") },
            onHorario    = { onNavigate("/horarioEstudiante") },
            onAsistencias = { onNavigate("/misAsistencias") },
            onBiblioteca = { onNavigate("/biblioteca") }
        )

        TarjetaEstadoCuenta(colors = colors, onClick = { onNavigate("/cuentaCorriente") })

        if (showClasesHoy) {
            TarjetaClasesHoy(clases = clasesHoy, colors = colors, onClose = onClasesHoyClose)
        }

        val excludedKeys = listOf(
            "MI QR", "MIS NOTAS", "MI HORARIO", "HORARIO", "MIS ASISTENCIAS", "BIBLIOTECA", "CUENTA CORRIENTE"
        )
        val otrosMenus = menus.filter {
            it.textoMenuAbrev.trim().uppercase() !in excludedKeys
        }
        if (otrosMenus.isNotEmpty()) {
            SeccionMasServicios(
                menus = otrosMenus,
                colors = colors,
                onClick = onMenuClick
            )
        }

        if (showFichaMatri) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                BtnFichaMatricula(
                    isLoading = isFichaMatriLoading,
                    colors = colors,
                    onClick = onFichaMatriClick
                )
            }
        }

        Spacer(modifier = Modifier.height(100.dp))
    }
}

@Composable
fun TarjetaQrAcceso(
    imageUrl: String,
    colors: DarkModeColors,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        onClick = onClick,
        shape = RoundedCornerShape(24.dp),
        shadowElevation = 8.dp,
        modifier = modifier.fillMaxWidth()
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(Brush.linearGradient(listOf(colors.colorGrisOscuro, colors.colorAzulOscuro)))
                .drawBehind {
                    drawCircle(
                        brush = Brush.radialGradient(
                            colors = listOf(colors.colorNaranjaBrillante.copy(alpha = 0.15f), Color.Transparent),
                            center = Offset(0f, 0f),
                            radius = size.width * 0.5f
                        )
                    )
                }
                .padding(24.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Column {
                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = colors.colorNaranjaBrillante.copy(alpha = 0.12f),
                        modifier = Modifier.size(48.dp)
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            AsyncImage(
                                model = imageUrl,
                                contentDescription = null,
                                contentScale = ContentScale.Fit,
                                colorFilter = ColorFilter.tint(colors.colorNaranjaBrillante),
                                modifier = Modifier.size(28.dp),
                                error = painterResource(Res.drawable.imgdefault)
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = "Mi QR\nAcceso",
                        color = Color.White,
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Black,
                        lineHeight = 28.sp,
                        letterSpacing = (-0.5).sp
                    )
                }
                Surface(
                    shape = CircleShape,
                    color = Color.White.copy(alpha = 0.1f),
                    modifier = Modifier.size(48.dp)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(
                            imageVector = Icons.Default.ArrowForward,
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(22.dp)
                        )
                    }
                }
            }
        }
    }
}

// ─────────────────────────────────────────────────────
// 2. GRILLA MÓDULOS PRINCIPALES (2×2)
// ─────────────────────────────────────────────────────
@Composable
fun GrillaModulosPrincipales(
    notasImageUrl: String,
    horarioImageUrl: String,
    asistImageUrl: String,
    bibImageUrl: String,
    notasBgUrl: String = "",
    horarioBgUrl: String = "",
    asistBgUrl: String = "",
    bibBgUrl: String = "",
    colors: DarkModeColors,
    onNotas: () -> Unit,
    onHorario: () -> Unit,
    onAsistencias: () -> Unit,
    onBiblioteca: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            DashboardGridCard(
                title = "Mis Notas",
                imageUrl = notasImageUrl,
                backgroundImageUrl = notasBgUrl,
                accentColor = colors.colorVioletaMedio,
                colors = colors,
                modifier = Modifier.weight(1f),
                onClick = onNotas
            )
            DashboardGridCard(
                title = "Mi Horario",
                imageUrl = horarioImageUrl,
                backgroundImageUrl = horarioBgUrl,
                accentColor = colors.colorMixPrimary,
                colors = colors,
                modifier = Modifier.weight(1f),
                onClick = onHorario
            )
        }
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            DashboardGridCard(
                title = "Asistencias",
                imageUrl = asistImageUrl,
                backgroundImageUrl = asistBgUrl,
                accentColor = colors.colorRojo,
                colors = colors,
                modifier = Modifier.weight(1f),
                onClick = onAsistencias
            )
            DashboardGridCard(
                title = "Biblioteca",
                imageUrl = bibImageUrl,
                backgroundImageUrl = bibBgUrl,
                accentColor = colors.colorAzulCielo,
                colors = colors,
                modifier = Modifier.weight(1f),
                onClick = onBiblioteca
            )
        }
    }
}

@Composable
fun DashboardGridCard(
    title: String,
    imageUrl: String,
    backgroundImageUrl: String = "",
    accentColor: Color,
    colors: DarkModeColors,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Surface(
        onClick = onClick,
        shape = RoundedCornerShape(24.dp),
        color = colors.colorExpenseItem,
        shadowElevation = 12.dp,
        border = BorderStroke(1.dp, colors.textColor.copy(alpha = 0.03f)),
        modifier = modifier.height(130.dp)
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            if (backgroundImageUrl.isNotEmpty()) {
                AsyncImage(
                    model = backgroundImageUrl,
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(70.dp)
                        .align(Alignment.BottomCenter)
                        .background(
                            Brush.verticalGradient(
                                listOf(Color.Transparent, Color.Black.copy(alpha = 0.65f))
                            )
                        )
                )
            }
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .drawBehind {
                        drawLine(
                            brush = Brush.horizontalGradient(
                                listOf(Color.Transparent, accentColor.copy(alpha = 0.5f), Color.Transparent)
                            ),
                            start = Offset(0f, 0f),
                            end = Offset(size.width, 0f),
                            strokeWidth = 1.dp.toPx()
                        )
                    }
                    .padding(16.dp)
            ) {
                Surface(
                    shape = CircleShape,
                    color = accentColor,
                    modifier = Modifier.size(46.dp).align(Alignment.TopEnd)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        AsyncImage(
                            model = imageUrl,
                            contentDescription = null,
                            contentScale = ContentScale.Fit,
                            colorFilter = ColorFilter.tint(Color.White),
                            modifier = Modifier.size(27.dp),
                            error = painterResource(Res.drawable.imgdefault)
                        )
                    }
                }
                Text(
                    text = title,
                    color = Color.White,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.align(Alignment.BottomStart),
                    letterSpacing = 0.5.sp
                )
            }
        }
    }
}

// ─────────────────────────────────────────────────────
// 3. TARJETA ESTADO DE CUENTA
// ─────────────────────────────────────────────────────
@Composable
fun TarjetaEstadoCuenta(
    colors: DarkModeColors,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        onClick = onClick,
        shape = RoundedCornerShape(24.dp),
        shadowElevation = 6.dp,
        modifier = modifier.fillMaxWidth()
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(Brush.horizontalGradient(listOf(colors.colorNaranjaDorado, colors.colorDoradoClaro)))
                .padding(horizontal = 20.dp, vertical = 20.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Surface(
                    shape = RoundedCornerShape(16.dp),
                    color = Color.White.copy(alpha = 0.15f),
                    modifier = Modifier.size(52.dp)
                ) {
                    Icon(
                        imageVector = Icons.Rounded.AccountBalanceWallet,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.padding(14.dp)
                    )
                }
                Spacer(modifier = Modifier.width(16.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "Estado de Cuenta",
                        color = Color.White,
                        fontSize = 17.sp,
                        fontWeight = FontWeight.ExtraBold,
                        letterSpacing = 0.5.sp
                    )
                    Text(
                        text = "Ver pagos y deudas",
                        color = Color.White.copy(alpha = 0.7f),
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
                Icon(
                    imageVector = Icons.Rounded.ChevronRight,
                    contentDescription = "Ir",
                    tint = Color.White.copy(alpha = 0.8f),
                    modifier = Modifier.size(28.dp)
                )
            }
        }
    }
}

// ─────────────────────────────────────────────────────
// 4. SECCIÓN MÁS SERVICIOS (grilla desde API)
// ─────────────────────────────────────────────────────
@Composable
fun SeccionMasServicios(
    menus: List<DataMenu>,
    colors: DarkModeColors,
    onClick: (DataMenu) -> Unit,
    modifier: Modifier = Modifier
) {
    val backgroundColors = listOf(
        colors.colorPastelRosa,
        colors.colorPastelTeal,
        colors.colorPastelGris,
        colors.colorPastelVerde,
        colors.colorPastelNaranja,
        colors.colorPastelMarron,
        colors.colorPastelAzul,
        colors.colorPastelMorado
    )
    val accentColors = listOf(
        colors.colorRosado,           // 0 → PastelRosa
        colors.colorNaranjaBrillante, // 1 → PastelTeal  |  10 → PastelLavanda
        colors.colorVioletaMedio,     // 2 → PastelLavanda
        colors.colorEsmeralda,        // 3 → PastelVerde
        colors.colorAmbar,            // 4 → PastelNaranja
        colors.colorRojo,             // 5 → PastelMarron
        colors.colorCian,             // 6 → PastelAzul
        colors.colorVioletaIntenso,   // 7 → PastelMorado
        colors.colorAzulCielo         // 8 → PastelRosa (ciclo)
    )

    Column(modifier = modifier.fillMaxWidth()) {
        Text(
            text = "MÁS SERVICIOS",
            color = colors.textColor.copy(alpha = 0.5f),
            fontWeight = FontWeight.Bold,
            fontSize = 12.sp,
            letterSpacing = 1.sp,
            modifier = Modifier.padding(start = 4.dp, bottom = 16.dp)
        )

        androidx.compose.foundation.layout.BoxWithConstraints(modifier = Modifier.fillMaxWidth()) {
            val columns = if (maxWidth > 600.dp) 6 else 4
            val itemWidth = maxWidth / columns

            Column {
                menus.chunked(columns).forEach { fila ->
                    Row(
                        modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp),
                        horizontalArrangement = Arrangement.Start
                    ) {
                        fila.forEach { menu ->
                            val i = menus.indexOf(menu)
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                modifier = Modifier.width(itemWidth)
                            ) {
                                Surface(
                                    onClick = { onClick(menu) },
                                    shape = RoundedCornerShape(20.dp),
                                    color = backgroundColors[i % backgroundColors.size],
                                    shadowElevation = 2.dp,
                                    modifier = Modifier.size(64.dp)
                                ) {
                                    Box(contentAlignment = Alignment.Center) {
                                        AsyncImage(
                                            model = menu.imagen,
                                            contentDescription = menu.textoMenu,
                                            contentScale = ContentScale.Fit,
                                            colorFilter = ColorFilter.tint(accentColors[i % accentColors.size]),
                                            modifier = Modifier.size(32.dp).padding(2.dp),
                                            error = painterResource(Res.drawable.imgdefault)
                                        )
                                    }
                                }
                                Spacer(modifier = Modifier.height(8.dp))
                                val isIos = getPlatform().name.contains("iOS", ignoreCase = true)
                                Text(
                                    text = menu.textoMenu,
                                    color = colors.textColor.copy(alpha = 0.8f),
                                    fontSize = if (isIos) 9.5.sp else 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    fontFamily = menuLabelFontFamily(),
                                    textAlign = TextAlign.Center,
                                    lineHeight = if (isIos) 11.5.sp else 13.sp,
                                    maxLines = 2,
                                    modifier = Modifier.padding(horizontal = 4.dp)
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun TarjetaClasesHoy(
    clases: List<Horario>,
    colors: DarkModeColors,
    onClose: () -> Unit,
    modifier: Modifier = Modifier
) {
    val hoy = remember { getTodayLocalDate() }
    val diasSemana = listOf("Lunes", "Martes", "Miércoles", "Jueves", "Viernes", "Sábado", "Domingo")
    val tituloFecha = "${diasSemana[hoy.dayOfWeek.ordinal]} ${hoy.dayOfMonth}"
    val clasesAgrupadas = remember(clases) { agruparHorasClase(clases) }
    var expandida by remember { mutableStateOf(false) }
    val extras = clasesAgrupadas.drop(1)

    Surface(
        shape = RoundedCornerShape(24.dp),
        color = colors.colorExpenseItem,
        shadowElevation = 6.dp,
        modifier = modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.fillMaxWidth().padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Surface(
                        shape = RoundedCornerShape(14.dp),
                        color = colors.colorMixPrimary.copy(alpha = 0.12f),
                        modifier = Modifier.size(48.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Rounded.CalendarMonth,
                            contentDescription = null,
                            tint = colors.colorMixPrimary,
                            modifier = Modifier.padding(12.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text(
                            text = "MIS CLASES DE HOY",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = colors.textColor.copy(alpha = 0.5f),
                            letterSpacing = 1.sp
                        )
                        Text(
                            text = tituloFecha,
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Black,
                            color = colors.textColor,
                            letterSpacing = (-0.5).sp
                        )
                    }
                }
                Surface(
                    onClick = onClose,
                    shape = CircleShape,
                    color = colors.textColor.copy(alpha = 0.08f),
                    modifier = Modifier.size(32.dp)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Cerrar",
                            tint = colors.textColor.copy(alpha = 0.6f),
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            if (clasesAgrupadas.isEmpty()) {
                Text(
                    text = "No tiene clases programadas para el dia hoy.",
                    color = colors.textColor.copy(alpha = 0.5f),
                    fontSize = 13.sp,
                    modifier = Modifier.padding(vertical = 8.dp)
                )
            } else {
                ClaseCard(
                    item = clasesAgrupadas.first(),
                    paddingStart = 0.dp,
                    showExpandIcon = false,
                    applyTopPadding = true
                )

                if (extras.isNotEmpty()) {
                    AnimatedVisibility(visible = expandida) {
                        Column {
                            extras.forEach { clase ->
                                ClaseCard(
                                    item = clase,
                                    paddingStart = 0.dp,
                                    showExpandIcon = false,
                                    applyTopPadding = true
                                )
                            }
                        }
                    }

                    Row(
                        modifier = Modifier.fillMaxWidth().padding(top = 6.dp),
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Surface(
                            onClick = { expandida = !expandida },
                            shape = RoundedCornerShape(20.dp),
                            color = colors.colorMixPrimary.copy(alpha = 0.1f)
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.padding(horizontal = 16.dp, vertical = 6.dp)
                            ) {
                                Text(
                                    text = if (expandida) "Ver menos"
                                           else "Ver ${extras.size} ${if (extras.size == 1) "clase más" else "clases más"}",
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = colors.colorMixPrimary
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Icon(
                                    imageVector = if (expandida) Icons.Default.KeyboardArrowUp
                                                  else Icons.Default.KeyboardArrowDown,
                                    contentDescription = null,
                                    tint = colors.colorMixPrimary,
                                    modifier = Modifier.size(16.dp)
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun BtnFichaMatricula(
    isLoading: Boolean,
    colors: DarkModeColors,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        onClick = { if (!isLoading) onClick() },
        shape = RoundedCornerShape(16.dp),
        color = colors.colorMixPrimary,
        shadowElevation = 6.dp,
        modifier = modifier
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(horizontal = 20.dp, vertical = 14.dp)
        ) {
            if (isLoading) {
                CircularProgressIndicator(
                    color = Color.White,
                    strokeWidth = 2.dp,
                    modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(10.dp))
            }
            Text(
                text = "Ficha Proyección",
                color = Color.White,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 0.5.sp
            )
        }
    }
}
