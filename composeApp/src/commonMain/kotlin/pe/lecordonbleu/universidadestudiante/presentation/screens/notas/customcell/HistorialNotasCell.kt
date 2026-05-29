package pe.lecordonbleu.universidadestudiante.presentation.screens.notas.customcell

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Divider
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import pe.lecordonbleu.universidadestudiante.data.remote.dto.HistorialNotasItem
import pe.lecordonbleu.universidadestudiante.getColorsTheme

@Composable
fun HistorialNotasCell(item: HistorialNotasItem) {
    val colors = getColorsTheme()
    val credito = item.credito.toDoubleOrNull()?.toInt() ?: 0
    val condicionColor = when (item.matric_not_flag_aprobado) {
        "APROBADO"    -> colors.colorVerdeMedio
        "DESAPROBADO" -> colors.colorRojo
        else          -> colors.textColor.copy(alpha = 0.6f)
    }
    val notaColor = when (item.matric_not_flag_aprobado) {
        "APROBADO"    -> colors.colorAzulMedio
        "DESAPROBADO" -> colors.colorRojo
        else          -> colors.textColor
    }

    val glassHighlight = Color.White.copy(alpha = 0.12f)

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .drawBehind {
                drawLine(
                    brush = Brush.horizontalGradient(
                        colors = listOf(
                            Color.Transparent,
                            Color.White.copy(alpha = 0.25f),
                            Color.Transparent
                        )
                    ),
                    start = Offset(size.width * 0.1f, 0f),
                    end = Offset(size.width * 0.9f, 0f),
                    strokeWidth = 1.dp.toPx()
                )
            },
        color = colors.colorExpenseItem,
        shape = RoundedCornerShape(16.dp),
        shadowElevation = 2.dp
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    Brush.linearGradient(
                        colors = listOf(
                            glassHighlight,
                            Color.Transparent,
                            Color.White.copy(alpha = 0.03f)
                        ),
                        start = Offset(0f, 0f),
                        end = Offset(400f, 400f)
                    )
                )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {

                // Header: nombre + badge ciclo
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.Top
                ) {
                    Text(
                        text = item.pest_asign_nombre.uppercase(),
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = colors.textColor,
                        lineHeight = 20.sp,
                        modifier = Modifier.weight(1f),
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Surface(
                        color = colors.colorMixPrimary.copy(alpha = 0.15f),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text(
                            text = "Ciclo ${item.ciclo_nivel}",
                            style = MaterialTheme.typography.labelSmall,
                            color = colors.colorMixPrimary,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Info: periodo · tipo · créditos
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    HistorialInfoItem(label = "Periodo", value = item.peracad_nombre)
                    HistorialInfoItem(label = "Tipo", value = item.tipo_asign_nombre)
                    HistorialInfoItem(label = "Creditos", value = "$credito")
                }

                Spacer(modifier = Modifier.height(16.dp))
                Divider(color = colors.textColor.copy(alpha = 0.05f))
                Spacer(modifier = Modifier.height(12.dp))

                // Footer: promedio final + badge condicion
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "PROMEDIO FINAL",
                        style = MaterialTheme.typography.labelSmall,
                        color = colors.textColor.copy(alpha = 0.6f),
                        letterSpacing = 0.5.sp
                    )
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = item.matric_not_prom_final,
                            style = MaterialTheme.typography.headlineSmall,
                            fontWeight = FontWeight.Black,
                            color = notaColor
                        )
                        Spacer(modifier = Modifier.width(10.dp))
                        Surface(
                            color = condicionColor.copy(alpha = 0.15f),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Text(
                                text = item.matric_not_flag_aprobado,
                                color = condicionColor,
                                style = MaterialTheme.typography.labelSmall,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun HistorialInfoItem(label: String, value: String) {
    val colors = getColorsTheme()
    Column(horizontalAlignment = Alignment.Start) {
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall,
            color = colors.textColor.copy(alpha = 0.5f)
        )
        Spacer(modifier = Modifier.height(2.dp))
        Text(
            text = value,
            style = MaterialTheme.typography.bodySmall,
            fontWeight = FontWeight.SemiBold,
            color = colors.textColor
        )
    }
}
