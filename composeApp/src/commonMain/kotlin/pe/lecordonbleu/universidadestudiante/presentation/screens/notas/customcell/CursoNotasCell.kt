package pe.lecordonbleu.universidadestudiante.presentation.screens.notas.customcell

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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material3.Divider
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import pe.lecordonbleu.universidadestudiante.data.remote.dto.ListadoNotas
import pe.lecordonbleu.universidadestudiante.getColorsTheme
import kotlin.math.roundToInt

@Composable
fun CursoNotasCell(
    item: ListadoNotas,
    onClick: () -> Unit
) {
    val colors = getColorsTheme()

    val notaMin   = item.nota_min.toFloatOrNull() ?: 0f
    val notaCalif = item.matric_not_prac_calif.toFloatOrNull()
    val notaParc  = item.matric_not_exam_parc.toFloatOrNull()
    val notaFinal = item.matric_not_exam_final.toFloatOrNull()
    val notaProm  = item.matric_not_prom_final.toFloatOrNull()


    val condicionColor = when (item.matric_not_flag_aprobado) {
        "APROBADO"    -> colors.colorVerdeMedio
        "DESAPROBADO" -> colors.colorRojo
        "PENDIENTE"   -> colors.textColor.copy(alpha = 0.6f)
        else          -> colors.textColor.copy(alpha = 0.6f)
    }

    val notaColor: (Float?) -> Color = { nota ->
        nota?.let { if (notaMin < it) colors.colorAzulMedio else colors.colorRojo } ?: colors.textColor
    }

    val colorCalif = notaColor(notaCalif)
    val colorParc  = notaColor(notaParc)
    val colorFinal = notaColor(notaFinal)
    val colorProm  = if (item.matric_not_prom_final == "-") colors.textColor else notaColor(notaProm)


    val estadoIndicatorColor = when {
        item.estado_matric.equals("1",    ignoreCase = true) -> colors.colorVerdeMedio
        item.estado_matric.equals("0",    ignoreCase = true) -> colors.colorRojo
        item.estado_matric.equals("NULL", ignoreCase = true) -> colors.textColor.copy(alpha = 0.4f)
        else                                                  -> colors.textColor.copy(alpha = 0.4f)
    }

    val pesoPrac  = item.peso_pract_calif.toFloatOrNull()?.roundToInt() ?: 0
    val pesoParc  = item.peso_exam_parcial.toFloatOrNull()?.roundToInt() ?: 0
    val pesoFinal = item.peso_exam_final.toFloatOrNull()?.roundToInt() ?: 0

    val glassHighlight = Color.White.copy(alpha = 0.12f)

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
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

                // Header
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.Top
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = item.pest_asign_nombre.uppercase(),
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = colors.textColor,
                            lineHeight = 20.sp,
                            maxLines = 2,
                            overflow = TextOverflow.Ellipsis
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Surface(
                            color = colors.textColor.copy(alpha = 0.08f),
                            shape = RoundedCornerShape(6.dp)
                        ) {
                            Text(
                                text = "Tipo: ${item.tipo_matric_asign_abrev}",
                                style = MaterialTheme.typography.labelSmall,
                                color = colors.textColor.copy(alpha = 0.6f),
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
                            )
                        }
                    }
                    Box(
                        modifier = Modifier
                            .padding(start = 8.dp, top = 4.dp)
                            .size(12.dp)
                            .background(estadoIndicatorColor, CircleShape)
                    )
                }

                // Notas parciales
                Spacer(modifier = Modifier.height(16.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceAround
                ) {
                    NotaCursoItem(
                        label = "Practicas ($pesoPrac%)",
                        value = item.matric_not_prac_calif,
                        color = colorCalif
                    )
                    NotaCursoItem(
                        label = "Parcial ($pesoParc%)",
                        value = item.matric_not_exam_parc,
                        color = colorParc
                    )
                    NotaCursoItem(
                        label = "Final ($pesoFinal%)",
                        value = item.matric_not_exam_final,
                        color = colorFinal
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))
                Divider(color = colors.textColor.copy(alpha = 0.05f))
                Spacer(modifier = Modifier.height(12.dp))

                // Footer: promedio final + badge + botón ver detalle
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "PROMEDIO FINAL",
                            style = MaterialTheme.typography.labelSmall,
                            color = colors.textColor.copy(alpha = 0.5f),
                            letterSpacing = 0.5.sp
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = item.matric_not_prom_final,
                                style = MaterialTheme.typography.headlineSmall,
                                fontWeight = FontWeight.Black,
                                color = colorProm
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

                    Surface(
                        color = colors.colorMixPrimary.copy(alpha = 0.15f),
                        shape = RoundedCornerShape(20.dp),
                        modifier = Modifier.clickable { onClick() }
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 14.dp, vertical = 8.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Text(
                                text = "Ver detalle",
                                style = MaterialTheme.typography.labelMedium,
                                fontWeight = FontWeight.SemiBold,
                                color = colors.colorMixPrimary
                            )
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                                contentDescription = null,
                                tint = colors.colorMixPrimary,
                                modifier = Modifier.size(18.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun NotaCursoItem(label: String, value: String, color: Color) {
    val colors = getColorsTheme()
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(text = label, fontSize = 9.sp, color = colors.textColor.copy(alpha = 0.6f))
        Text(
            text = value,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            color = color
        )
    }
}
