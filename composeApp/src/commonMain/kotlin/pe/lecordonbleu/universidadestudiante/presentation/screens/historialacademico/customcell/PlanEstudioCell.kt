package pe.lecordonbleu.universidadestudiante.presentation.screens.historialacademico.customcell

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import pe.lecordonbleu.universidadestudiante.DarkModeColors
import pe.lecordonbleu.universidadestudiante.core.theme.IlcbError
import pe.lecordonbleu.universidadestudiante.core.theme.IlcbEsmeralda
import pe.lecordonbleu.universidadestudiante.data.remote.dto.DataHistorialAcadamico
import pe.lecordonbleu.universidadestudiante.getColorsTheme

@Composable
fun PlanEstudioCell(
    items: List<DataHistorialAcadamico>,
    isExpanded: Boolean,
    onToggle: () -> Unit,
    onChildClick: (DataHistorialAcadamico) -> Unit
) {
    val colors = getColorsTheme()
    val primer = items.first()

    val estadoColor = if (primer.desc_pg_estado_estud_pe.trim().equals("NULL", ignoreCase = true)) {
        IlcbEsmeralda
    } else {
        IlcbError
    }

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 4.dp),
        color = colors.colorExpenseItem,
        shape = RoundedCornerShape(16.dp),
        border = BorderStroke(1.dp, colors.textColor.copy(alpha = 0.05f)),
        shadowElevation = 2.dp
    ) {
        Column {
            // --- Header (tap = expand/collapse) ---
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(IntrinsicSize.Min)
                    .clickable { onToggle() }
            ) {
                Box(
                    modifier = Modifier
                        .width(6.dp)
                        .fillMaxHeight()
                        .padding(vertical = 12.dp, horizontal = 1.dp)
                        .clip(CircleShape)
                        .background(colors.colorMixPrimary)
                )

                Column(
                    modifier = Modifier
                        .weight(1f)
                        .padding(start = 12.dp, top = 16.dp, bottom = 16.dp, end = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.Top
                    ) {
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(6.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.weight(1f)
                        ) {
                            Text(
                                text = primer.peracad_nombre,
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.ExtraBold,
                                color = colors.textColor
                            )
                            Text(
                                text = primer.serv_nombre,
                                style = MaterialTheme.typography.titleSmall,
                                fontWeight = FontWeight.Bold,
                                color = colors.textColor.copy(alpha = 0.85f),
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                        }
                        Column(
                            horizontalAlignment = Alignment.End,
                            verticalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Text(text = "Estado", fontSize = 9.sp, color = colors.textColor.copy(alpha = 0.5f))
                            Box(modifier = Modifier.size(14.dp).clip(CircleShape).background(estadoColor))
                        }
                    }

                    Text(text = primer.pestd_cod, fontSize = 13.sp, color = colors.colorMixPrimary, fontWeight = FontWeight.SemiBold)

                    HorizontalDivider(color = colors.textColor.copy(alpha = 0.06f))

                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
                            Text(text = "Tipo Serv. Acad.:", fontSize = 10.sp, color = colors.textColor.copy(alpha = 0.5f))
                            Text(text = primer.tiposerva_abrev, fontSize = 12.sp, fontWeight = FontWeight.Bold, color = colors.textColor)
                        }
                        Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
                            Text(text = "Servicio:", fontSize = 10.sp, color = colors.textColor.copy(alpha = 0.5f))
                            Text(text = primer.serv_nombre, fontSize = 12.sp, fontWeight = FontWeight.Bold, color = colors.textColor)
                        }
                    }

                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End, verticalAlignment = Alignment.CenterVertically) {
                        Surface(color = colors.colorMixPrimary.copy(alpha = 0.12f), shape = CircleShape, modifier = Modifier.size(28.dp)) {
                            Icon(
                                imageVector = if (isExpanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                                contentDescription = null,
                                tint = colors.colorMixPrimary,
                                modifier = Modifier.padding(4.dp)
                            )
                        }
                    }
                }
            }

            // --- Hijos (visible cuando expandido) ---
            AnimatedVisibility(visible = isExpanded) {
                Column {
                    HorizontalDivider(color = colors.textColor.copy(alpha = 0.08f))
                    items.forEach { child ->
                        HistorialChildRow(item = child, colors = colors, onClick = { onChildClick(child) })
                        if (child != items.last()) {
                            HorizontalDivider(modifier = Modifier.padding(horizontal = 16.dp), color = colors.textColor.copy(alpha = 0.05f))
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun HistorialChildRow(
    item: DataHistorialAcadamico,
    colors: DarkModeColors,
    onClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(horizontal = 20.dp, vertical = 14.dp),
        verticalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
                Text(text = "Tipo Serv.:", fontSize = 10.sp, color = colors.textColor.copy(alpha = 0.5f))
                Text(text = item.tiposerva_abrev, fontSize = 12.sp, fontWeight = FontWeight.Bold, color = colors.textColor)
            }
            Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
                Text(text = "Servicio:", fontSize = 10.sp, color = colors.textColor.copy(alpha = 0.5f))
                Text(text = item.pest_det_nombre, fontSize = 12.sp, fontWeight = FontWeight.Bold, color = colors.textColor)
            }
        }
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
                Text(text = "Periodo:", fontSize = 10.sp, color = colors.textColor.copy(alpha = 0.5f))
                Text(text = item.peracad_nombre, fontSize = 12.sp, fontWeight = FontWeight.Bold, color = colors.colorMixPrimary)
            }
            Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
                Text(text = "Grado:", fontSize = 10.sp, color = colors.textColor.copy(alpha = 0.5f))
                Text(text = item.gacad_nombre, fontSize = 12.sp, fontWeight = FontWeight.Bold, color = colors.textColor)
            }
        }
        Column(modifier = Modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(2.dp)) {
            Text(text = "Cod. Plan:", fontSize = 10.sp, color = colors.textColor.copy(alpha = 0.5f))
            Text(text = item.pestd_cod, fontSize = 12.sp, fontWeight = FontWeight.Bold, color = colors.textColor)
            Text(
                text = "Ver Mas",
                color = colors.colorMixPrimary,
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.align(Alignment.End)
            )
        }
    }
}
