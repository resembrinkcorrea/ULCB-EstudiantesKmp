package pe.lecordonbleu.universidadestudiante.presentation.screens.misasistencias.customcell

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.EaseOutCubic
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.Canvas
import kotlinx.coroutines.launch
import pe.lecordonbleu.universidadestudiante.DarkModeColors

@Composable
fun AsistenciaGraficoPremium(
    colors: DarkModeColors,
    porcentajeInasistencia: Float,
    detasismin: Float,
    totalMaxInas: Int,
    practicasAsistio: Int,
    practicasFalto: Int,
    practicasTotal: Int,
    teoricaAsistio: Int,
    teoricaFalto: Int,
    teoricaTotal: Int
) {
    val numberAnim = remember { Animatable(0f) }
    val progressAnim = remember { Animatable(0f) }
    val practicasAnim = remember { Animatable(0f) }
    val teoricaAnim = remember { Animatable(0f) }

    LaunchedEffect(porcentajeInasistencia) {
        launch {
            numberAnim.animateTo(
                targetValue = porcentajeInasistencia,
                animationSpec = tween(durationMillis = 2000, easing = EaseOutCubic)
            )
        }
        launch {
            progressAnim.animateTo(
                targetValue = (porcentajeInasistencia / 100f).coerceIn(0f, 1f),
                animationSpec = tween(durationMillis = 2000, easing = EaseOutCubic)
            )
        }
    }

    LaunchedEffect(practicasTotal, teoricaTotal) {
        launch {
            practicasAnim.animateTo(
                targetValue = if (practicasTotal > 0) practicasAsistio.toFloat() / practicasTotal.toFloat() else 0f,
                animationSpec = tween(durationMillis = 1500, easing = EaseOutCubic)
            )
        }
        launch {
            teoricaAnim.animateTo(
                targetValue = if (teoricaTotal > 0) teoricaAsistio.toFloat() / teoricaTotal.toFloat() else 0f,
                animationSpec = tween(durationMillis = 1500, easing = EaseOutCubic)
            )
        }
    }

    val barColor = when {
        porcentajeInasistencia >= detasismin -> colors.colorRojo
        porcentajeInasistencia > 0f          -> colors.colorNaranjaOscuro
        else                                 -> colors.colorVerdeFuerte
    }

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 8.dp),
        color = colors.colorExpenseItem,
        shape = RoundedCornerShape(20.dp),
        border = BorderStroke(1.dp, colors.textColor.copy(alpha = 0.05f)),
        shadowElevation = 4.dp
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp)
        ) {
            Text(
                text = "RESUMEN DE ASISTENCIA",
                color = colors.colorMixPrimary,
                fontSize = 12.sp,
                fontWeight = FontWeight.ExtraBold,
                letterSpacing = 1.sp
            )
            Spacer(modifier = Modifier.height(20.dp))

            // --- Fila superior: círculo + faltas disp. ---
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(contentAlignment = Alignment.Center, modifier = Modifier.size(90.dp)) {
                    Canvas(modifier = Modifier.size(90.dp)) {
                        val strokeW = 10.dp.toPx()
                        drawArc(
                            color = colors.textColor.copy(alpha = 0.08f),
                            startAngle = -90f, sweepAngle = 360f, useCenter = false,
                            style = Stroke(width = strokeW, cap = StrokeCap.Round)
                        )
                        drawArc(
                            color = colors.colorMixPrimary,
                            startAngle = -90f, sweepAngle = 360f * progressAnim.value, useCenter = false,
                            style = Stroke(width = strokeW, cap = StrokeCap.Round)
                        )
                    }
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = "${numberAnim.value.toInt()}%",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Black,
                            color = colors.colorMixPrimary
                        )
                        Text(
                            text = "Inasis.",
                            fontSize = 10.sp,
                            color = colors.textColor.copy(alpha = 0.5f),
                            fontWeight = FontWeight.Bold
                        )
                    }
                }

                Spacer(modifier = Modifier.width(20.dp))

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Surface(
                        color = barColor.copy(alpha = 0.1f),
                        shape = RoundedCornerShape(12.dp),
                        border = BorderStroke(1.dp, barColor.copy(alpha = 0.2f))
                    ) {
                        Text(
                            text = "$totalMaxInas",
                            fontSize = 22.sp,
                            fontWeight = FontWeight.Black,
                            color = barColor,
                            modifier = Modifier.padding(horizontal = 14.dp, vertical = 8.dp)
                        )
                    }

                    Spacer(modifier = Modifier.width(12.dp))

                    Column {
                        Text(
                            text = "Inasistencias permitidas",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            color = colors.textColor
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // --- PRÁCTICAS ---
            ClaseBreakdownRow(
                label = "PRACTICAS",
                asistio = practicasAsistio,
                falto = practicasFalto,
                progress = practicasAnim.value,
                colors = colors
            )

            Spacer(modifier = Modifier.height(12.dp))

            // --- TEORÍA ---
            ClaseBreakdownRow(
                label = "TEORICA",
                asistio = teoricaAsistio,
                falto = teoricaFalto,
                progress = teoricaAnim.value,
                colors = colors
            )

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = "* Sin contar clases no registradas",
                fontSize = 10.sp,
                color = colors.textColor.copy(alpha = 0.4f),
                fontWeight = FontWeight.Normal
            )
        }
    }
}

@Composable
private fun ClaseBreakdownRow(
    label: String,
    asistio: Int,
    falto: Int,
    progress: Float,
    colors: DarkModeColors
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = label,
                fontSize = 12.sp,
                fontWeight = FontWeight.ExtraBold,
                color = colors.textColor
            )
            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                    Box(modifier = Modifier.size(7.dp).clip(CircleShape).background(colors.colorVerdeFuerte))
                    Text(text = "Asistio: $asistio", fontSize = 11.sp, color = colors.textColor.copy(alpha = 0.8f))
                }
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                    Box(modifier = Modifier.size(7.dp).clip(CircleShape).background(colors.colorRojo))
                    Text(text = "Falto: $falto", fontSize = 11.sp, color = colors.textColor.copy(alpha = 0.8f))
                }
            }
        }
        Spacer(modifier = Modifier.height(6.dp))
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(6.dp)
                .clip(CircleShape)
                .background(colors.textColor.copy(alpha = 0.08f))
        ) {
            Box(
                modifier = Modifier
                    .fillMaxHeight()
                    .fillMaxWidth(fraction = progress.coerceIn(0f, 1f))
                    .clip(CircleShape)
                    .background(colors.colorVerdeFuerte)
            )
        }
    }
}
