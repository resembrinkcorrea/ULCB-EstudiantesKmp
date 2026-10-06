package pe.lecordonbleu.universidadestudiante.presentation.screens.home.customcell

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import kotlinx.coroutines.delay
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import pe.lecordonbleu.universidadestudiante.DarkModeColors
import pe.lecordonbleu.universidadestudiante.core.theme.IlcbPastelRosaSuave
import pe.lecordonbleu.universidadestudiante.data.remote.dto.ArchivoObligatorio
import pe.lecordonbleu.universidadestudiante.getPlatformContext
import pe.lecordonbleu.universidadestudiante.util.openUrl

@Composable
fun CardArchivosObligatorios(
    archivos: List<ArchivoObligatorio>,
    colors: DarkModeColors,
    onClose: () -> Unit,
    modifier: Modifier = Modifier
) {
    if (archivos.isEmpty()) return

    var expandida by remember { mutableStateOf(false) }
    val pagerState = rememberPagerState(pageCount = { archivos.size })

    LaunchedEffect(expandida, archivos.size) {
        if (!expandida && archivos.size > 1) {
            while (true) {
                delay(4000)
                val next = (pagerState.currentPage + 1) % archivos.size
                pagerState.animateScrollToPage(next)
            }
        }
    }

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
                        color = colors.colorRojo.copy(alpha = 0.12f),
                        modifier = Modifier.size(48.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Description,
                            contentDescription = null,
                            tint = colors.colorRojo,
                            modifier = Modifier.padding(12.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text(
                            text = "ARCHIVOS OBLIGATORIOS",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = colors.textColor.copy(alpha = 0.5f),
                            letterSpacing = 1.sp
                        )
                        Text(
                            text = "${archivos.size} ${if (archivos.size == 1) "archivo" else "archivos"}",
                            fontSize = 16.sp,
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

            Spacer(modifier = Modifier.height(12.dp))

            if (!expandida) {
                HorizontalPager(
                    state = pagerState,
                    modifier = Modifier.fillMaxWidth()
                ) { page ->
                    ArchivoCard(archivo = archivos[page], colors = colors)
                }

                if (archivos.size > 1) {
                    Spacer(modifier = Modifier.height(8.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center
                    ) {
                        repeat(archivos.size) { index ->
                            Box(
                                modifier = Modifier
                                    .padding(horizontal = 3.dp)
                                    .size(if (pagerState.currentPage == index) 8.dp else 5.dp)
                                    .background(
                                        color = if (pagerState.currentPage == index)
                                            colors.colorRojo
                                        else
                                            colors.colorGrisNeutro.copy(alpha = 0.4f),
                                        shape = CircleShape
                                    )
                            )
                        }
                    }
                }
            } else {
                archivos.forEach { archivo ->
                    ArchivoCard(archivo = archivo, colors = colors)
                    Spacer(modifier = Modifier.height(8.dp))
                }
            }

            Spacer(modifier = Modifier.height(4.dp))

            Row(
                modifier = Modifier.fillMaxWidth().padding(top = 2.dp),
                horizontalArrangement = Arrangement.Center
            ) {
                Surface(
                    onClick = { expandida = !expandida },
                    shape = RoundedCornerShape(20.dp),
                    color = colors.colorRojo.copy(alpha = 0.1f)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 6.dp)
                    ) {
                        Text(
                            text = if (expandida) "Ver menos"
                            else "Ver ${archivos.size} ${if (archivos.size == 1) "archivo" else "archivos"}",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = colors.colorRojo
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Icon(
                            imageVector = if (expandida) Icons.Default.KeyboardArrowUp
                            else Icons.Default.KeyboardArrowDown,
                            contentDescription = null,
                            tint = colors.colorRojo,
                            modifier = Modifier.size(16.dp)
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun ArchivoCard(
    archivo: ArchivoObligatorio,
    colors: DarkModeColors
) {
    val context = getPlatformContext()
    val modifColor = if (isSystemInDarkTheme()) IlcbPastelRosaSuave else colors.colorRojo
    val docColor = when (archivo.extension_docu.lowercase()) {
        "pdf"         -> colors.colorStripeRojo
        "docx", "doc" -> colors.colorStripeAzul
        "xls", "xlsx" -> colors.colorStripeVerde
        else          -> colors.colorGrisNeutro
    }
    Surface(
        shape = RoundedCornerShape(16.dp),
        color = colors.colorRojo.copy(alpha = 0.05f),
        modifier = Modifier.fillMaxWidth().height(130.dp)
    ) {
        Box(modifier = Modifier.fillMaxSize().padding(12.dp)) {
            Column(modifier = Modifier.fillMaxWidth()) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Surface(
                        shape = RoundedCornerShape(6.dp),
                        color = colors.colorRojo.copy(alpha = 0.15f),
                        modifier = Modifier.weight(1f, fill = false)
                    ) {
                        Text(
                            text = archivo.ruta_carpeta,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            color = colors.colorRojo,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp),
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                    if (archivo.extension_docu.isNotBlank()) {
                        Text(
                            text = archivo.extension_docu.uppercase(),
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            color = docColor
                        )
                    }
                }
                Spacer(modifier = Modifier.height(6.dp))
                Row(
                    verticalAlignment = Alignment.Top,
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable(enabled = archivo.url_docu.isNotBlank()) {
                            openUrl(context, archivo.url_docu)
                        }
                ) {
                    Icon(
                        imageVector = Icons.Default.Description,
                        contentDescription = null,
                        tint = docColor,
                        modifier = Modifier.size(18.dp).padding(top = 2.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = archivo.nombre_docu,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = colors.colorAzulCielo,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )
                }
                if (archivo.descripcion_docu.isNotBlank()) {
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = archivo.descripcion_docu,
                        fontSize = 11.sp,
                        color = colors.textColor.copy(alpha = 0.6f),
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }
            Text(
                text = "Ult. modif: ${archivo.last_fec_modif}",
                fontSize = 10.sp,
                color = modifColor,
                modifier = Modifier.align(Alignment.BottomEnd)
            )
        }
    }
}
