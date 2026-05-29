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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import pe.lecordonbleu.universidadestudiante.data.remote.dto.ListadoNotasDetalle
import pe.lecordonbleu.universidadestudiante.getColorsTheme

@Composable
fun DetalleNotasCell(item: ListadoNotasDetalle) {
    val colors = getColorsTheme()
    val notaMin = item.nota_min.toFloatOrNull() ?: 0f

    fun notaColor(notaStr: String): Color {
        if (notaStr == "-") return colors.textColor
        val nota = notaStr.toFloatOrNull() ?: return colors.textColor
        return if (notaMin < nota) colors.colorAzulMedio else colors.colorRojo
    }

    val flagColor = when (item.flag_aprobado) {
        "1"  -> colors.colorVerdeMedio
        "2"  -> colors.textColor.copy(alpha = 0.5f)
        else -> colors.colorRojo
    }

    val pesoPerm  = (item.peso_prac_calif.toDoubleOrNull()?.times(100))?.toInt() ?: 0
    val pesoParc  = (item.peso_exam_parc.toDoubleOrNull()?.times(100))?.toInt() ?: 0
    val pesoFinal = (item.peso_exam_final.toDoubleOrNull()?.times(100))?.toInt() ?: 0

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

                // Header: nombre + código + promedio
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = item.alumno,
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = colors.textColor
                        )
                        Text(
                            text = item.est_codigo,
                            style = MaterialTheme.typography.labelSmall,
                            color = colors.textColor.copy(alpha = 0.6f)
                        )
                    }
                    Column(horizontalAlignment = Alignment.End) {
                        Text(
                            text = item.not_prom_final,
                            style = MaterialTheme.typography.headlineSmall,
                            fontWeight = FontWeight.Black,
                            color = notaColor(item.not_prom_final)
                        )
                        Text(
                            text = "Sustitutorio: ${item.not_exam_susti}",
                            style = MaterialTheme.typography.labelSmall,
                            color = colors.textColor.copy(alpha = 0.6f)
                        )
                        Surface(
                            color = flagColor.copy(alpha = 0.15f),
                            shape = RoundedCornerShape(6.dp)
                        ) {
                            Text(
                                text = if (item.flag_aprobado == "1") "APROBADO"
                                       else if (item.flag_aprobado == "2") "-"
                                       else "DESAPROBADO",
                                style = MaterialTheme.typography.labelSmall,
                                fontWeight = FontWeight.Bold,
                                color = flagColor,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))
                Divider(color = colors.textColor.copy(alpha = 0.05f))
                Spacer(modifier = Modifier.height(12.dp))

                // Notas parciales
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceAround
                ) {
                    DetalleNotaItem(
                        label = "Ev. Permanente $pesoPerm%",
                        value = item.not_prac_calif,
                        color = notaColor(item.not_prac_calif)
                    )
                    DetalleNotaItem(
                        label = "Parcial $pesoParc%",
                        value = item.not_exam_parc,
                        color = notaColor(item.not_exam_parc)
                    )
                    DetalleNotaItem(
                        label = "Final $pesoFinal%",
                        value = item.not_exam_final,
                        color = notaColor(item.not_exam_final)
                    )
                }
            }
        }
    }
}

@Composable
private fun DetalleNotaItem(label: String, value: String, color: Color) {
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
