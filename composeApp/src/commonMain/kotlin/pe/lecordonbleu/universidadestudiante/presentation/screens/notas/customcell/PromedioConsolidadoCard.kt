package pe.lecordonbleu.universidadestudiante.presentation.screens.notas.customcell

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.unit.sp
import pe.lecordonbleu.universidadestudiante.data.remote.dto.TareaAcad
import pe.lecordonbleu.universidadestudiante.getColorsTheme
import kotlin.math.round

@Composable
fun PromedioConsolidadoCard(
    tareasTeoria   : List<TareaAcad>,
    tareasPractica : List<TareaAcad>,
    tareasGeneral  : List<TareaAcad>,
    nombreGrupo    : String
) {
    val colors = getColorsTheme()

    // ── Cálculo del promedio consolidado ──────────────────────────────────
    val promTeoria   = tareasTeoria.firstOrNull()?.matric_not_det_prom?.toDoubleOrNull()   ?: 0.0
    val pesoTeoria   = tareasTeoria.firstOrNull()?.peso_act_det?.toDoubleOrNull()          ?: 0.0
    val promPractica = tareasPractica.firstOrNull()?.matric_not_det_prom?.toDoubleOrNull() ?: 0.0
    val pesoPractica = tareasPractica.firstOrNull()?.peso_act_det?.toDoubleOrNull()        ?: 0.0
    val promGeneral  = tareasGeneral.firstOrNull()?.matric_not_det_prom?.toDoubleOrNull()  ?: 0.0
    val pesoGeneral  = tareasGeneral.firstOrNull()?.peso_act_det?.toDoubleOrNull()         ?: 0.0

    val hasTeoria   = tareasTeoria.isNotEmpty()
    val hasPractica = tareasPractica.isNotEmpty()

    val pesoTotal = pesoTeoria + pesoPractica + pesoGeneral

    val promConsolidado = when {
        pesoTotal > 0           -> ((promTeoria * pesoTeoria) + (promPractica * pesoPractica) + (promGeneral * pesoGeneral)) / pesoTotal
        hasTeoria && hasPractica -> (promTeoria + promPractica) / 2
        hasTeoria               -> promTeoria
        hasPractica             -> promPractica
        else                    -> promGeneral
    }

    val pesoPorc  = (pesoTotal * 100).toInt()
    val titulo    = if (hasTeoria && hasPractica) "Promedio Consolidado" else nombreGrupo
    val promStr   = formatPromedio(promConsolidado)
    val isPending = promConsolidado == 0.0

    // ── UI ────────────────────────────────────────────────────────────────
    val gradientColors = listOf(
        colors.colorMixPrimary.copy(alpha = 0.9f),
        colors.colorMixPrimary.copy(alpha = 0.6f)
    )

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(Brush.verticalGradient(gradientColors))
            .drawBehind {
                drawLine(
                    brush = Brush.horizontalGradient(listOf(Color.Transparent, Color.White.copy(alpha = 0.4f), Color.Transparent)),
                    start = Offset(0f, 0f),
                    end   = Offset(size.width, 0f),
                    strokeWidth = 1.dp.toPx()
                )
            }
            .padding(20.dp)
    ) {
        Column {
            Text(
                text          = titulo.uppercase(),
                fontSize      = 10.sp,
                color         = Color.White.copy(alpha = 0.7f),
                fontWeight    = FontWeight.Bold,
                letterSpacing = 1.sp
            )
            Spacer(modifier = Modifier.height(4.dp))
            Row(verticalAlignment = Alignment.Bottom) {
                Text(
                    text          = if (isPending) "-" else promStr,
                    fontSize      = 56.sp,
                    color         = if (isPending) Color.White.copy(alpha = 0.4f) else Color.White,
                    fontWeight    = FontWeight.Black,
                    letterSpacing = (-2).sp
                )
                Text(
                    text       = " / 20",
                    fontSize   = 16.sp,
                    color      = Color.White.copy(alpha = 0.5f),
                    fontWeight = FontWeight.Bold,
                    modifier   = Modifier.padding(bottom = 12.dp, start = 4.dp)
                )
            }
            Text(
                text       = "Puntaje de Seccion Actual",
                fontSize   = 12.sp,
                color      = Color.White.copy(alpha = 0.8f),
                fontWeight = FontWeight.Medium
            )
            if (pesoPorc > 0) {
                Spacer(modifier = Modifier.height(16.dp))
                Surface(
                    color  = Color.Transparent,
                    shape  = RoundedCornerShape(4.dp),
                    border = androidx.compose.foundation.BorderStroke(1.dp, Color.White.copy(alpha = 0.3f))
                ) {
                    Text(
                        text          = "PESO: $pesoPorc%",
                        fontSize      = 10.sp,
                        fontWeight    = FontWeight.Bold,
                        color         = Color.White,
                        letterSpacing = 0.5.sp,
                        modifier      = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                }
            }
        }
    }
}

private fun formatPromedio(value: Double): String {
    val rounded  = round(value * 10) / 10.0
    val intPart  = rounded.toInt()
    val decPart  = round((rounded - intPart) * 10).toInt()
    return "$intPart.$decPart"
}
