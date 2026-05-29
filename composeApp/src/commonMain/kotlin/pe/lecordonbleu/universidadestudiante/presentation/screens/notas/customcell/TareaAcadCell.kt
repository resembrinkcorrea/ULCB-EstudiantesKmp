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
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import pe.lecordonbleu.universidadestudiante.data.remote.dto.TareaAcad
import pe.lecordonbleu.universidadestudiante.getColorsTheme

@Composable
fun TareaAcadCell(item: TareaAcad) {
    val colors = getColorsTheme()
    val glassHighlight = Color.White.copy(alpha = 0.12f)
    val cardShape = RoundedCornerShape(12.dp)
    val notaVacia = item.flag_nota_vacia == "0"
    val notaNumero = item.nota.toDoubleOrNull()
    val notaMinima = item.pest_det_nota_min_aprob.toDoubleOrNull()
    val estadoNota = when {
        notaVacia || notaNumero == null || notaMinima == null -> null
        notaNumero >= notaMinima -> "Aprobado"
        else -> "Desaprobado"
    }
    val colorEstado = if (estadoNota == "Aprobado") {
        Color(0xFF2E7D32)
    } else {
        Color(0xFFC62828)
    }

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
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
        color = if (notaVacia) colors.colorPastelRosaSuave else colors.colorExpenseItem,
        shape = cardShape,
        shadowElevation = 2.dp
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(cardShape)
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
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 12.dp)
            ) {
                val pesoPorc = (item.peso_act_det.toDoubleOrNull()?.times(100))?.toInt() ?: 0
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "${item.nro_nota}. ${item.act_acad_abrev.trim()}",
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.Bold,
                            color = colors.textColor
                        )
                        Text(
                            text = "Peso: $pesoPorc%",
                            style = MaterialTheme.typography.labelSmall,
                            color = colors.textColor.copy(alpha = 0.6f)
                        )
                    }
                    Column(
                        horizontalAlignment = Alignment.End,
                        modifier = Modifier.padding(start = 12.dp)
                    ) {
                        Text(
                            text = if (notaVacia) "" else item.nota,
                            style = MaterialTheme.typography.headlineSmall,
                            fontWeight = FontWeight.Black,
                            color = colors.textColor
                        )
                        if (estadoNota != null) {
                            Text(
                                text = estadoNota,
                                style = MaterialTheme.typography.labelSmall,
                                fontWeight = FontWeight.SemiBold,
                                color = colorEstado
                            )
                        }
                    }
                }
                if (!notaVacia) {
                    Spacer(modifier = Modifier.height(8.dp))
                    HorizontalDivider(color = colors.textColor.copy(alpha = 0.06f))
                    Spacer(modifier = Modifier.height(6.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = item.usuario_modif,
                            style = MaterialTheme.typography.labelSmall,
                            color = colors.textColor.copy(alpha = 0.4f)
                        )
                        Text(
                            text = item.fecha_modif,
                            style = MaterialTheme.typography.labelSmall,
                            color = colors.textColor.copy(alpha = 0.4f)
                        )
                    }
                }
            }
        }
    }
}
