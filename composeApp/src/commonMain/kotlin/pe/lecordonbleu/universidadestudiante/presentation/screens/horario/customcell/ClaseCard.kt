package pe.lecordonbleu.universidadestudiante.presentation.screens.horario.customcell

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.MeetingRoom
import androidx.compose.material.icons.filled.ZoomOutMap
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import pe.lecordonbleu.universidadestudiante.DarkModeColors
import pe.lecordonbleu.universidadestudiante.core.theme.IlcbStripeAmber
import pe.lecordonbleu.universidadestudiante.core.theme.IlcbStripeBlue
import pe.lecordonbleu.universidadestudiante.core.theme.IlcbStripeCyan
import pe.lecordonbleu.universidadestudiante.core.theme.IlcbStripeGreen
import pe.lecordonbleu.universidadestudiante.core.theme.IlcbStripeIndigo
import pe.lecordonbleu.universidadestudiante.core.theme.IlcbStripeOrange
import pe.lecordonbleu.universidadestudiante.core.theme.IlcbStripePink
import pe.lecordonbleu.universidadestudiante.core.theme.IlcbStripePurple
import pe.lecordonbleu.universidadestudiante.core.theme.IlcbStripeRed
import pe.lecordonbleu.universidadestudiante.core.theme.IlcbStripeTeal
import pe.lecordonbleu.universidadestudiante.data.remote.dto.Horario
import pe.lecordonbleu.universidadestudiante.getColorsTheme

private val stripeColors = listOf(
    IlcbStripeBlue,
    IlcbStripeRed,
    IlcbStripeAmber,
    IlcbStripeGreen,
    IlcbStripePurple,
    IlcbStripeCyan,
    IlcbStripeOrange,
    IlcbStripeIndigo,
    IlcbStripePink,
    IlcbStripeTeal
)

@Composable
fun ClaseCard(
    item: Horario,
    paddingStart: Dp = 16.dp,
    modifier: Modifier = Modifier,
    onExpand: (() -> Unit)? = null,
    onNavigate: (() -> Unit)? = null,
    showExpandIcon: Boolean = true,
    applyTopPadding: Boolean = true
) {
    val colors = getColorsTheme()
    val accentColor = stripeColors[kotlin.math.abs(item.pest_asign_nombre.hashCode()) % stripeColors.size]

    Surface(
        shape = RoundedCornerShape(16.dp),
        color = colors.colorExpenseItem,
        border = BorderStroke(1.dp, colors.textColor.copy(alpha = 0.05f)),
        shadowElevation = 2.dp,
        modifier = modifier
            .padding(
                start = paddingStart,
                end = 16.dp,
                top = if (applyTopPadding) 8.dp else 0.dp,
                bottom = if (applyTopPadding) 8.dp else 0.dp
            )
            .fillMaxWidth()
    ) {
        Row(modifier = Modifier.fillMaxWidth().height(IntrinsicSize.Min)) {
            // Franja lateral con extremos redondeados
            Box(
                modifier = Modifier
                    .width(6.dp)
                    .fillMaxHeight()
                    .padding(vertical = 12.dp, horizontal = 1.dp)
                    .clip(CircleShape)
                    .background(accentColor)
            )

            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(start = 12.dp, end = 12.dp, top = 16.dp, bottom = 16.dp)
            ) {
                // Fila 1: pill de hora + badges
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .background(accentColor.copy(alpha = 0.1f))
                            .padding(horizontal = 8.dp, vertical = 4.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.AccessTime,
                            contentDescription = null,
                            tint = accentColor,
                            modifier = Modifier.size(14.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "${item.hora_inicio} - ${item.hora_fin}",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = accentColor,
                            letterSpacing = 0.5.sp
                        )
                    }

                    Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                        BadgeClase(text = item.tipaula_dictado, colors = colors)
                        BadgeClase(text = item.tipo_dictado_nombre, colors = colors)
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Fila 2: nombre del curso + flecha
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = item.pest_asign_nombre,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.ExtraBold,
                        color = colors.textColor,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis,
                        lineHeight = 20.sp,
                        modifier = Modifier.weight(1f)
                    )
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        if (showExpandIcon) {
                            Icon(
                                imageVector = Icons.Default.ZoomOutMap,
                                contentDescription = null,
                                tint = accentColor,
                                modifier = Modifier
                                    .size(20.dp)
                                    .clickable { onExpand?.invoke() }
                            )
                        }
                        if (onNavigate != null) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                                contentDescription = null,
                                tint = accentColor,
                                modifier = Modifier
                                    .size(20.dp)
                                    .clickable { onNavigate() }
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                // Fila 3: sede y aula
                Column(modifier = Modifier.fillMaxWidth()) {
                    if (item.sede.isNotEmpty()) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.LocationOn,
                                contentDescription = null,
                                tint = colors.textColor.copy(alpha = 0.4f),
                                modifier = Modifier.size(14.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = item.sede,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Medium,
                                color = colors.textColor.copy(alpha = 0.7f),
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                        }
                        Spacer(modifier = Modifier.height(4.dp))
                    }

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.MeetingRoom,
                            contentDescription = null,
                            tint = colors.textColor.copy(alpha = 0.4f),
                            modifier = Modifier.size(14.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "Aula ${item.aula_nombre}",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Medium,
                            color = colors.textColor.copy(alpha = 0.7f),
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun BadgeClase(text: String, colors: DarkModeColors) {
    if (text.isBlank()) return
    val txtColor = when {
        text.equals("PRESENCIAL", true) -> colors.colorMixPrimary
        text.equals("SINCRONICA", true) -> colors.colorEsmeralda
        else                            -> colors.colorOrange800
    }

    Surface(
        color = txtColor.copy(alpha = 0.1f),
        shape = RoundedCornerShape(6.dp),
        border = BorderStroke(1.dp, txtColor.copy(alpha = 0.15f))
    ) {
        Text(
            text = text.uppercase(),
            fontSize = 8.sp,
            fontWeight = FontWeight.ExtraBold,
            color = txtColor,
            modifier = Modifier.padding(horizontal = 6.dp, vertical = 3.dp),
            letterSpacing = 0.5.sp
        )
    }
}

@Composable
fun CustomColorTipoClase(text: String, backgroundColor: Color, contentColor: Color) {
    Box(
        modifier = Modifier
            .background(backgroundColor, shape = RoundedCornerShape(4.dp))
            .padding(horizontal = 8.dp, vertical = 2.dp)
    ) {
        Text(text = text, color = contentColor, fontSize = 10.sp, fontWeight = FontWeight.SemiBold)
    }
}
