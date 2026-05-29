package pe.lecordonbleu.universidadestudiante.presentation.screens.misasistencias.customcell

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import pe.lecordonbleu.universidadestudiante.DarkModeColors
import pe.lecordonbleu.universidadestudiante.data.remote.dto.ListadoAsistencia

@Composable
fun ClaseAsistenciaCard(
    clase: ListadoAsistencia,
    colors: DarkModeColors
) {
    data class AsistenciaVisual(
        val badgeBgColor: Color,
        val badgeColor: Color,
        val badgeText: String,
        val badgeIcon: ImageVector
    )

    val visual = when (clase.asistio) {
        "1" -> AsistenciaVisual(
            badgeBgColor = colors.colorVerdeFuerte.copy(alpha = 0.15f),
            badgeColor = colors.colorVerdeFuerte,
            badgeText = "Asistio",
            badgeIcon = Icons.Default.CheckCircle
        )
        "2" -> AsistenciaVisual(
            badgeBgColor = colors.colorAmbar.copy(alpha = 0.15f),
            badgeColor = colors.colorAmbar,
            badgeText = "Sin registro",
            badgeIcon = Icons.Default.Schedule
        )
        else -> AsistenciaVisual(
            badgeBgColor = colors.colorNaranjaOscuro.copy(alpha = 0.15f),
            badgeColor = colors.colorNaranjaOscuro,
            badgeText = "Falta",
            badgeIcon = Icons.Default.Close
        )
    }

    val tipoClaseColor = if (clase.clase.uppercase() == "PRACTICO") {
        colors.colorMixPrimary
    } else {
        colors.textColor.copy(alpha = 0.5f)
    }

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .drawBehind {
                drawLine(
                    brush = Brush.horizontalGradient(
                        colors = listOf(
                            Color.Transparent,
                            Color.White.copy(alpha = 0.2f),
                            Color.Transparent
                        )
                    ),
                    start = Offset(size.width * 0.15f, 0f),
                    end = Offset(size.width * 0.85f, 0f),
                    strokeWidth = 0.5.dp.toPx()
                )
            },
        color = colors.colorExpenseItem,
        shape = RoundedCornerShape(12.dp),
        shadowElevation = 2.dp
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    Brush.linearGradient(
                        colors = listOf(
                            Color.White.copy(alpha = 0.08f),
                            Color.Transparent
                        ),
                        start = Offset(0f, 0f),
                        end = Offset(300f, 300f)
                    )
                )
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text(
                            text = "Turno ${clase.sesion}",
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.Bold,
                            color = colors.textColor
                        )
                        Surface(
                            color = tipoClaseColor.copy(alpha = 0.15f),
                            shape = RoundedCornerShape(6.dp)
                        ) {
                            Text(
                                text = clase.clase.uppercase(),
                                style = MaterialTheme.typography.labelSmall,
                                fontWeight = FontWeight.Bold,
                                color = tipoClaseColor,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp),
                                fontSize = 10.sp
                            )
                        }
                    }

                    Surface(
                        color = visual.badgeBgColor,
                        shape = RoundedCornerShape(20.dp)
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Icon(
                                imageVector = visual.badgeIcon,
                                contentDescription = null,
                                tint = visual.badgeColor,
                                modifier = Modifier.size(14.dp)
                            )
                            Text(
                                text = visual.badgeText,
                                style = MaterialTheme.typography.labelSmall,
                                fontWeight = FontWeight.Bold,
                                color = visual.badgeColor,
                                fontSize = 11.sp
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column(
                        modifier = Modifier.weight(1f),
                        verticalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        InfoRowCompact(icon = Icons.Default.Person, value = clase.docente, colors = colors)
                        InfoRowCompact(icon = Icons.Default.CalendarMonth, value = "${clase.dia} ${clase.hor_asis_dia}", colors = colors)
                    }

                    Column(
                        horizontalAlignment = Alignment.End,
                        verticalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Text(
                            text = "${clase.hor_inicio} - ${clase.hor_fin}",
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.SemiBold,
                            color = colors.textColor
                        )
                        Text(
                            text = "Horario",
                            style = MaterialTheme.typography.labelSmall,
                            color = colors.textColor.copy(alpha = 0.5f)
                        )

                        Spacer(modifier = Modifier.height(4.dp))
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Schedule,
                                contentDescription = null,
                                tint = colors.textColor.copy(alpha = 0.8f),
                                modifier = Modifier.size(12.dp)
                            )
                            Text(
                                text = if (clase.hora_marcacion.isNotEmpty()) clase.hora_marcacion else "-",
                                style = MaterialTheme.typography.labelSmall,
                                color = colors.textColor.copy(alpha = 0.5f),
                                fontSize = 10.sp
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun InfoRowCompact(
    icon: ImageVector,
    value: String,
    colors: DarkModeColors
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = colors.textColor.copy(alpha = 0.4f),
            modifier = Modifier.size(14.dp)
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodySmall,
            color = colors.textColor.copy(alpha = 0.8f),
            maxLines = 1
        )
    }
}
