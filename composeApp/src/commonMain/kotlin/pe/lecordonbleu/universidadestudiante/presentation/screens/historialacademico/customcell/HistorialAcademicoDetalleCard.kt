package pe.lecordonbleu.universidadestudiante.presentation.screens.historialacademico.customcell

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import pe.lecordonbleu.universidadestudiante.data.remote.dto.ListadoDetacad
import pe.lecordonbleu.universidadestudiante.getColorsTheme

@Composable
fun HistorialAcademicoDetalleCard(
    item: ListadoDetacad,
    modifier: Modifier = Modifier
) {
    val colors = getColorsTheme()

    val estadoResultado = item.pg_estado_resultado_nota.trim().uppercase()
    val isAprobado = estadoResultado == "APROBADO" || estadoResultado == "A" || estadoResultado == "1"
    val isPendiente = estadoResultado == "-"

    val estadoColor = if (isAprobado) Color(0xFF00897B) else if (isPendiente) Color.Gray else colors.colorNaranjaOscuro
    val estadoTexto = if (isAprobado) "APROBADO" else if (isPendiente) "PENDIENTE" else "DESAPROBADO"

    Surface(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        color = colors.colorExpenseItem,
        shape = RoundedCornerShape(20.dp),
        border = BorderStroke(1.dp, colors.textColor.copy(alpha = 0.05f)),
        shadowElevation = 2.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(IntrinsicSize.Min)
        ) {
            Box(
                modifier = Modifier
                    .width(6.dp)
                    .fillMaxHeight()
                    .padding(vertical = 12.dp, horizontal = 1.dp)
                    .clip(CircleShape)
                    .background(estadoColor)
            )

            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(start = 12.dp, top = 16.dp, bottom = 16.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.Top
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                            Surface(
                                color = estadoColor.copy(alpha = 0.1f),
                                shape = RoundedCornerShape(6.dp),
                                border = BorderStroke(1.dp, estadoColor.copy(alpha = 0.2f))
                            ) {
                                Text(
                                    text = estadoTexto,
                                    color = estadoColor,
                                    fontSize = 8.sp,
                                    fontWeight = FontWeight.ExtraBold,
                                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 3.dp),
                                    letterSpacing = 0.5.sp
                                )
                            }
                            Surface(
                                color = colors.colorMixPrimary.copy(alpha = 0.1f),
                                shape = RoundedCornerShape(6.dp),
                                border = BorderStroke(1.dp, colors.colorMixPrimary.copy(alpha = 0.2f))
                            ) {
                                Text(
                                    text = item.modal_asign_nombre.uppercase(),
                                    color = colors.colorMixPrimary,
                                    fontSize = 8.sp,
                                    fontWeight = FontWeight.ExtraBold,
                                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 3.dp),
                                    letterSpacing = 0.5.sp
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        Text(
                            text = item.asign_nombre,
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.Bold,
                            color = colors.textColor,
                            maxLines = 2,
                            overflow = TextOverflow.Ellipsis,
                            lineHeight = 18.sp
                        )

                        Spacer(modifier = Modifier.height(4.dp))

                        Text(
                            text = item.pest_asign_nombre,
                            fontSize = 11.sp,
                            color = colors.textColor.copy(alpha = 0.6f),
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }

                    Spacer(modifier = Modifier.width(12.dp))

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(end = 16.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .width(1.dp)
                                .height(48.dp)
                                .background(colors.textColor.copy(alpha = 0.08f))
                        )

                        Spacer(modifier = Modifier.width(16.dp))

                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier.widthIn(min = 50.dp)
                        ) {
                            Text(
                                text = "PROMEDIO",
                                fontSize = 9.sp,
                                color = colors.textColor.copy(alpha = 0.4f),
                                fontWeight = FontWeight.Bold,
                                letterSpacing = 0.5.sp
                            )
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                text = item.matric_not_prom_final,
                                fontSize = 24.sp,
                                fontWeight = FontWeight.Black,
                                color = estadoColor,
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(end = 16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    listOf("CICLO" to item.ciclo_nivel, "MATRICULA" to item.tipo_matric_asign_abrev, "TIPO" to item.tipo_asign_nombre).forEach { (label, value) ->
                        Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
                            Text(text = label, fontSize = 9.sp, color = colors.textColor.copy(alpha = 0.5f), fontWeight = FontWeight.SemiBold, letterSpacing = 0.5.sp)
                            Text(text = value, fontSize = 12.sp, color = colors.textColor.copy(alpha = 0.8f), fontWeight = FontWeight.Medium, maxLines = 1, overflow = TextOverflow.Ellipsis)
                        }
                    }
                }
            }
        }
    }
}
