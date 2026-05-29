package pe.lecordonbleu.universidadestudiante.presentation.screens.vermatricula.customcell

import androidx.compose.animation.AnimatedVisibility
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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Place
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import pe.lecordonbleu.universidadestudiante.data.remote.dto.ListDetMatric
import pe.lecordonbleu.universidadestudiante.data.remote.dto.ListVerMatric
import pe.lecordonbleu.universidadestudiante.getColorsTheme

@Composable
fun MatriculaCard(
    item: ListVerMatric,
    detalles: List<ListDetMatric>,
    isExpanded: Boolean,
    onExpandClick: () -> Unit
) {
    val colors = pe.lecordonbleu.universidadestudiante.getColorsTheme()
    val glassHighlight = Color.White.copy(alpha = 0.10f)

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 4.dp, vertical = 4.dp),
        shape = RoundedCornerShape(16.dp),
        color = colors.colorExpenseItem,
        tonalElevation = 2.dp,
        shadowElevation = 2.dp
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    Brush.linearGradient(
                        colors = listOf(glassHighlight, Color.Transparent, Color.White.copy(alpha = 0.03f)),
                        start = Offset(0f, 0f),
                        end = Offset(400f, 400f)
                    )
                )
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(IntrinsicSize.Min)
            ) {
                // ── Barra lateral de color ───────────────────────────────────────
                Box(
                    modifier = Modifier
                        .width(4.dp)
                        .fillMaxHeight()
                        .background(
                            color = colors.colorMixPrimary,
                            shape = RoundedCornerShape(topStart = 16.dp, bottomStart = 16.dp)
                        )
                )

                // ── Contenido principal ──────────────────────────────────────────
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .padding(start = 12.dp, end = 12.dp, top = 14.dp, bottom = 14.dp)
                ) {

                    // ── Nombre asignatura + ciclo badge ──────────────────────────
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.Top,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = item.asign_det_nombre_cod,
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.Bold,
                            color = colors.textColor,
                            modifier = Modifier.weight(1f)
                        )
                        if (item.ciclo_nivel.isNotEmpty()) {
                            Surface(
                                shape = RoundedCornerShape(50),
                                color = colors.colorMixPrimary.copy(alpha = 0.15f),
                                modifier = Modifier.padding(start = 8.dp, top = 2.dp)
                            ) {
                                Text(
                                    text = "Ciclo ${item.ciclo_nivel}",
                                    style = MaterialTheme.typography.labelSmall,
                                    fontWeight = FontWeight.SemiBold,
                                    color = colors.colorMixPrimary,
                                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    // ── Etiquetas: Mod. / Tipo Asign. / Periodo / Hrs ───────────
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(6.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        if (item.modal_asign_abrev.isNotEmpty()) {
                            Surface(
                                shape = RoundedCornerShape(6.dp),
                                color = colors.colorMixPrimary.copy(alpha = 0.10f)
                            ) {
                                Text(
                                    text = "Mod. ${item.modal_asign_abrev}",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = colors.colorMixPrimary,
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                                )
                            }
                        }
                        if (item.tipo_asign_abrev.isNotEmpty()) {
                            Surface(
                                shape = RoundedCornerShape(6.dp),
                                color = colors.textColor.copy(alpha = 0.07f)
                            ) {
                                Text(
                                    text = "Tipo ${item.tipo_asign_abrev}",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = colors.textColor.copy(alpha = 0.65f),
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                                )
                            }
                        }
                        if (item.peracad_nombre.isNotEmpty()) {
                            Surface(
                                shape = RoundedCornerShape(6.dp),
                                color = colors.textColor.copy(alpha = 0.07f)
                            ) {
                                Text(
                                    text = "Per. ${item.peracad_nombre}",
                                    style = MaterialTheme.typography.labelSmall,
                                    fontWeight = FontWeight.Bold,
                                    color = colors.textColor.copy(alpha = 0.65f),
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                                )
                            }
                        }
                        if (item.hor_acad.isNotEmpty() && item.hor_acad != "0") {
                            Surface(
                                shape = RoundedCornerShape(6.dp),
                                color = colors.textColor.copy(alpha = 0.07f)
                            ) {
                                Text(
                                    text = "Hrs ${item.hor_acad}",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = colors.textColor.copy(alpha = 0.65f),
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                                )
                            }
                        }
                    }

                    // ── Info: seccion, turno, creditos ───────────────────────────
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        listOf(
                            Triple("Seccion", item.oad_seccion_nombre, Modifier.weight(1f)),
                            Triple("Turno", item.turno_nombre, Modifier.weight(1f)),
                            Triple("Creditos", item.cant_tot_cred, Modifier.weight(0.7f))
                        ).forEach { (label, value, mod) ->
                            Column(modifier = mod) {
                                Text(text = label, style = MaterialTheme.typography.labelSmall, color = colors.textColor.copy(alpha = 0.45f))
                                Text(text = value, style = MaterialTheme.typography.bodySmall, fontWeight = FontWeight.Medium, color = colors.textColor)
                            }
                        }
                    }

                    if (item.horario.isNotEmpty()) {
                        Spacer(modifier = Modifier.height(6.dp))
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.Schedule,
                                contentDescription = null,
                                tint = colors.textColor.copy(alpha = 0.4f),
                                modifier = Modifier.size(12.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = item.horario,
                                style = MaterialTheme.typography.labelSmall,
                                color = colors.textColor.copy(alpha = 0.55f)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    // ── Indicadores Estado y Hor (abajo a la derecha) ────────────
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.End,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        listOf("Estado" to (item.flag_aprobado != 0), "Hor" to (item.valhor != 0)).forEach { (label, isOk) ->
                            Row(
                                modifier = Modifier.padding(start = 16.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(10.dp)
                                        .background(
                                            color = if (isOk) colors.colorVerdeMedio else colors.colorRojo,
                                            shape = CircleShape
                                        )
                                )
                                Text(
                                    text = label,
                                    style = MaterialTheme.typography.labelSmall,
                                    color = colors.textColor.copy(alpha = 0.65f)
                                )
                            }
                        }
                    }

                    // ── Sección expandida (deshabilitada por ahora) ──────────────
                    AnimatedVisibility(visible = isExpanded) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 12.dp, bottom = 4.dp)
                                .clip(RoundedCornerShape(12.dp))
                                .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f))
                                .padding(10.dp)
                        ) {
                            Text(
                                text = "Detalle de horario",
                                style = MaterialTheme.typography.labelMedium,
                                fontWeight = FontWeight.SemiBold,
                                color = colors.textColor.copy(alpha = 0.55f),
                                modifier = Modifier.padding(bottom = 8.dp)
                            )
                            if (detalles.isEmpty()) {
                                Text(
                                    text = "Cargando...",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = colors.textColor.copy(alpha = 0.4f)
                                )
                            } else {
                                detalles.forEachIndexed { idx, det ->
                                    if (idx > 0) {
                                        HorizontalDivider(
                                            modifier = Modifier.padding(vertical = 4.dp),
                                            color = colors.textColor.copy(alpha = 0.06f)
                                        )
                                    }
                                    DetalleMatriculaItem(det = det)
                                }
                            }
                        }
                    }

                    // ── Botón expandir (deshabilitado por ahora, código preservado) ──
                    // IconButton(onClick = onExpandClick, modifier = Modifier.size(40.dp)) {
                    //     Icon(
                    //         imageVector = if (isExpanded) Icons.Default.ExpandLess else Icons.Default.ExpandMore,
                    //         contentDescription = null,
                    //         tint = colors.colorMixPrimary
                    //     )
                    // }
                }
            }
        }
    }
}

@Composable
private fun DetalleMatriculaItem(det: ListDetMatric) {
    val colors = getColorsTheme()
    Column(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.Person,
                contentDescription = null,
                tint = colors.colorMixPrimary.copy(alpha = 0.65f),
                modifier = Modifier.size(14.dp)
            )
            Text(
                text = det.docente,
                style = MaterialTheme.typography.bodySmall,
                fontWeight = FontWeight.SemiBold,
                color = colors.textColor
            )
        }
        Spacer(modifier = Modifier.height(4.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                    Icon(
                        imageVector = Icons.Default.Place,
                        contentDescription = null,
                        tint = colors.textColor.copy(alpha = 0.35f),
                        modifier = Modifier.size(12.dp)
                    )
                    Text(
                        text = det.aula_nombre,
                        style = MaterialTheme.typography.labelSmall,
                        color = colors.textColor.copy(alpha = 0.65f)
                    )
                }
                if (det.sede_abrev.isNotEmpty()) {
                    Text(
                        text = det.sede_abrev,
                        style = MaterialTheme.typography.labelSmall,
                        color = colors.textColor.copy(alpha = 0.4f),
                        modifier = Modifier.padding(top = 2.dp, start = 16.dp)
                    )
                }
            }
            Column(modifier = Modifier.weight(1f)) {
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                    Icon(
                        imageVector = Icons.Default.MenuBook,
                        contentDescription = null,
                        tint = colors.textColor.copy(alpha = 0.35f),
                        modifier = Modifier.size(12.dp)
                    )
                    Text(
                        text = det.oad_seccion_nombre,
                        style = MaterialTheme.typography.labelSmall,
                        color = colors.textColor.copy(alpha = 0.65f)
                    )
                }
                if (det.turno_nombre.isNotEmpty()) {
                    Text(
                        text = det.turno_nombre,
                        style = MaterialTheme.typography.labelSmall,
                        color = colors.textColor.copy(alpha = 0.4f),
                        modifier = Modifier.padding(top = 2.dp, start = 16.dp)
                    )
                }
            }
        }
        if (det.horario.isNotEmpty()) {
            Spacer(modifier = Modifier.height(4.dp))
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                Icon(
                    imageVector = Icons.Default.Schedule,
                    contentDescription = null,
                    tint = colors.textColor.copy(alpha = 0.35f),
                    modifier = Modifier.size(12.dp)
                )
                Text(
                    text = det.horario,
                    style = MaterialTheme.typography.labelSmall,
                    color = colors.textColor.copy(alpha = 0.6f)
                )
            }
        }
        if (det.vacantes.isNotEmpty()) {
            Spacer(modifier = Modifier.height(4.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                listOf("Vacantes" to det.vacantes, "Matriculados" to det.matriculados).forEach { (label, value) ->
                    Column {
                        Text(text = label, style = MaterialTheme.typography.labelSmall, color = colors.textColor.copy(alpha = 0.45f))
                        Text(text = value, style = MaterialTheme.typography.bodySmall, fontWeight = FontWeight.Medium, color = colors.textColor)
                    }
                }
            }
        }
    }
}
