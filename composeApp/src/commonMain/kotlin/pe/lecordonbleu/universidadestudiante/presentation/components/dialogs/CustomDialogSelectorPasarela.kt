package pe.lecordonbleu.universidadestudiante.presentation.components.dialogs

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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import coil3.compose.AsyncImage
import androidx.compose.foundation.isSystemInDarkTheme
import pe.lecordonbleu.universidadestudiante.core.theme.IlcbBlueDeep
import pe.lecordonbleu.universidadestudiante.core.theme.IlcbSurface
import pe.lecordonbleu.universidadestudiante.core.theme.IlcbBlueMid
import pe.lecordonbleu.universidadestudiante.core.theme.IlcbOnBrand
import pe.lecordonbleu.universidadestudiante.data.remote.dto.PasarelaActiva
import pe.lecordonbleu.universidadestudiante.getColorsTheme
import androidx.compose.foundation.border
import pe.lecordonbleu.universidadestudiante.presentation.screens.mercadopago.ResultadoPagoDialog
import pe.lecordonbleu.universidadestudiante.presentation.screens.mercadopago.TarjetaLogoMP

@Composable
fun CustomDialogSelectorPasarela(
    visible: Boolean,
    monto: String,
    pasarelas: List<PasarelaActiva> = emptyList(),
    onFlywire: () -> Unit,
    onYape: () -> Unit,
    onMercadoPago: () -> Unit,
    onDismiss: () -> Unit
) {
    if (!visible) return
    val colors = getColorsTheme()
    val mostrarTodas = pasarelas.isEmpty()
    fun activa(nombre: String) = mostrarTodas || pasarelas.any { it.medio_pago_nombre.equals(nombre, ignoreCase = true) && it.activo == 1 }
    val montoValor = monto.replace(",", "").toDoubleOrNull() ?: 0.0
    var showLimiteYape by remember { mutableStateOf(false) }

    if (showLimiteYape) {
        ResultadoPagoDialog(
            aprobado = false,
            mensaje = "Yape solo está disponible hasta S/ 2,000.00 por operación. Por favor elige otro método de pago.",
            onDismiss = { showLimiteYape = false }
        )
    }

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(dismissOnClickOutside = true)
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = colors.backGroundColor),
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
        ) {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp))
                        .background(
                            Brush.linearGradient(
                                colors = listOf(IlcbBlueDeep, IlcbBlueMid),
                                start = Offset(0f, 0f),
                                end = Offset(600f, 0f)
                            )
                        )
                        .padding(vertical = 18.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = "MÉTODO DE PAGO",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            color = IlcbOnBrand,
                            letterSpacing = 1.sp
                        )
                        Spacer(Modifier.height(8.dp))
                        Surface(
                            shape = RoundedCornerShape(50),
                            color = Color.White.copy(alpha = 0.15f)
                        ) {
                            Text(
                                text = "S/ $monto",
                                fontSize = 22.sp,
                                fontWeight = FontWeight.ExtraBold,
                                color = IlcbOnBrand,
                                modifier = Modifier.padding(horizontal = 24.dp, vertical = 6.dp)
                            )
                        }
                    }
                }

                Spacer(Modifier.height(16.dp))

                Text(
                    text = "Selecciona tu pasarela de pago",
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Bold,
                    color = colors.textColor
                )
                Spacer(Modifier.height(4.dp))
                Text(
                    text = "Elige la pasarela con la que deseas\nrealizar tu pago de forma segura.",
                    style = MaterialTheme.typography.bodySmall,
                    color = colors.textColor.copy(alpha = 0.4f),
                    textAlign = TextAlign.Center
                )

                Spacer(Modifier.height(14.dp))

                if (activa("Flywire")) {
                    PasarelaCard(
                        nombre = "Flywire",
                        descripcion = "Tarjeta / transferencia internacional",
                        logoUrl = "https://mercadeo.blob.core.windows.net/saainstituto/Flywire_logo.png",
                        cardBg = Color(0xFF1A3A5C),
                        modifier = Modifier.padding(horizontal = 16.dp),
                        onClick = onFlywire
                    )
                    Spacer(Modifier.height(8.dp))
                }

                if (activa("Yape")) {
                    PasarelaCard(
                        nombre = "Yape",
                        descripcion = "Pago con número y código OTP",
                        logoUrl = "https://www.yape.com.pe/images/logo-yape_positive.png",
                        cardBg = Color(0xFF6B1FA8),
                        modifier = Modifier.padding(horizontal = 16.dp),
                        onClick = {
                            if (montoValor > 2000.0) showLimiteYape = true
                            else onYape()
                        }
                    )
                    Spacer(Modifier.height(8.dp))
                }

                if (activa("Mercado Pago")) {
                    PasarelaCard(
                        nombre = "Tarjeta de crédito / débito",
                        descripcion = "Pago en cuotas",
                        logos = TarjetaLogoMP.todos.map { it.url to it.descripcion },
                        footnote = "Powered by Mercado Pago",
                        logoUrl = "https://mercadeo.blob.core.windows.net/logo/mercadopago_large_app.png",
                        cardBg = Color(0xFF009EE3),
                        modifier = Modifier.padding(horizontal = 16.dp),
                        onClick = onMercadoPago
                    )
                    Spacer(Modifier.height(8.dp))
                }

                Spacer(Modifier.height(16.dp))

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center,
                    modifier = Modifier.padding(bottom = 8.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Lock,
                        contentDescription = null,
                        tint = colors.textColor.copy(alpha = 0.4f),
                        modifier = Modifier.size(13.dp)
                    )
                    Spacer(Modifier.width(4.dp))
                    Text(
                        text = "Tu información está segura",
                        style = MaterialTheme.typography.labelSmall,
                        color = colors.textColor.copy(alpha = 0.4f)
                    )
                }

                OutlinedButton(
                    onClick = onDismiss,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                        .padding(bottom = 16.dp)
                        .height(44.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.outlinedButtonColors(
                        contentColor = colors.textColor.copy(alpha = 0.7f)
                    )
                ) {
                    Text("Cancelar", fontWeight = FontWeight.SemiBold, fontSize = 13.sp)
                }
            }
        }
    }
}

@Composable
private fun PasarelaCard(
    nombre: String,
    descripcion: String,
    logoUrl: String,
    cardBg: Color,
    modifier: Modifier = Modifier,
    footnote: String? = null,
    logos: List<Pair<String, String>> = emptyList(),
    onClick: () -> Unit
) {
    val logoBg = if (isSystemInDarkTheme()) IlcbSurface else Color.White
    Surface(
        modifier = modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(12.dp),
        color = cardBg,
        tonalElevation = 1.dp
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 14.dp, vertical = 13.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .background(Color.White),
                contentAlignment = Alignment.Center
            ) {
                AsyncImage(
                    model = logoUrl,
                    contentDescription = nombre,
                    contentScale = ContentScale.Fit,
                    modifier = Modifier
                        .size(40.dp)
                        .padding(4.dp)
                )
            }
            Spacer(Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = nombre,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 14.sp,
                    color = Color.White
                )
                Text(
                    text = descripcion,
                    style = MaterialTheme.typography.labelSmall,
                    color = Color.White.copy(alpha = 0.75f)
                )
                if (logos.isNotEmpty()) {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(4.dp),
                        modifier = Modifier.padding(top = 4.dp)
                    ) {
                        logos.forEach { (url, desc) ->
                            Box(
                                modifier = Modifier
                                    .border(0.5.dp, MaterialTheme.colorScheme.outlineVariant, RoundedCornerShape(4.dp))
                                    .clip(RoundedCornerShape(4.dp))
                                    .background(logoBg)
                                    .padding(horizontal = 4.dp, vertical = 2.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                AsyncImage(
                                    model = url,
                                    contentDescription = desc,
                                    contentScale = ContentScale.Fit,
                                    modifier = Modifier.height(12.dp).width(22.dp)
                                )
                            }
                        }
                    }
                }
                if (footnote != null) {
                    Spacer(Modifier.height(6.dp))
                    Text(
                        text = footnote,
                        style = MaterialTheme.typography.labelSmall,
                        color = Color.White.copy(alpha = 0.7f),
                        fontSize = 10.sp
                    )
                }
            }
            Icon(
                imageVector = Icons.Default.KeyboardArrowRight,
                contentDescription = null,
                tint = Color.White.copy(alpha = 0.7f),
                modifier = Modifier.size(18.dp)
            )
        }
    }
}
