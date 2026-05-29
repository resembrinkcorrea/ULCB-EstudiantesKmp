package pe.lecordonbleu.universidadestudiante.presentation.screens.vermatricula.customcell

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
import androidx.compose.material.icons.filled.Email
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import pe.lecordonbleu.universidadestudiante.data.remote.dto.ListResumenHist
import pe.lecordonbleu.universidadestudiante.getColorsTheme

@Composable
fun ResumenHistoricoCard(item: ListResumenHist) {
    val colors = getColorsTheme()
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
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 14.dp)
            ) {
                // â”€â”€ Header: ultimo periodo + prom ciclo â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "Ultimo periodo: ${item.ultimo_periodo}",
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.Bold,
                            color = colors.textColor
                        )
                        if (item.periodo_ingreso.isNotEmpty()) {
                            Text(
                                text = "Periodo ingreso: ${item.periodo_ingreso}",
                                style = MaterialTheme.typography.labelSmall,
                                color = colors.textColor.copy(alpha = 0.5f),
                                modifier = Modifier.padding(top = 2.dp)
                            )
                        }
                    }
                    if (item.prom_ciclo.isNotEmpty()) {
                        Surface(
                            shape = RoundedCornerShape(50),
                            color = colors.colorMixPrimary.copy(alpha = 0.15f)
                        ) {
                            Text(
                                text = "Prom. Ciclo: ${item.prom_ciclo}",
                                style = MaterialTheme.typography.labelSmall,
                                fontWeight = FontWeight.SemiBold,
                                color = colors.colorMixPrimary,
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))
                HorizontalDivider(color = colors.textColor.copy(alpha = 0.07f))
                Spacer(modifier = Modifier.height(10.dp))

                // â”€â”€ Bloque 1: Promedios + categoria â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    HistInfoItem("Prom. ciclo", item.prom_ciclo, Modifier.weight(1f))
                    HistInfoItem("Prom. pond.", item.prom_ponder, Modifier.weight(1f))
                    HistInfoItem("Categoria", item.cate_nombre, Modifier.weight(1f))
                }

                Spacer(modifier = Modifier.height(8.dp))

                // â”€â”€ Bloque 2: Asignaturas â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    HistInfoItem("Asig. mat.", item.asign_mat, Modifier.weight(1f))
                    HistInfoItem(
                        label = "Asig. aprob.",
                        value = item.asig_aprob,
                        modifier = Modifier.weight(1f),
                        valueColor = if ((item.asig_aprob.toIntOrNull() ?: 0) > 0) colors.colorVerdeMedio else null
                    )
                    HistInfoItem(
                        label = "Asig. desaprob.",
                        value = item.asig_desaprob,
                        modifier = Modifier.weight(1f),
                        valueColor = if ((item.asig_desaprob.toIntOrNull() ?: 0) > 0) colors.colorRojo else null
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                // â”€â”€ Bloque 3: Creditos y cursadas (con formato x/y) â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    val cursadasDisplay = buildString {
                        append(item.asign_cursadas)
                        if (item.cant_asign.isNotEmpty()) append("/${item.cant_asign}")
                    }
                    val credDisplay = buildString {
                        append(item.total_cred_total)
                        if (item.cant_cred.isNotEmpty()) append("/${item.cant_cred}")
                    }
                    HistInfoItem("Asig. cursadas", cursadasDisplay, Modifier.weight(1f))
                    HistInfoItem("Tot. creditos", credDisplay, Modifier.weight(1f))
                    HistInfoItem("Cred. mat.", item.cred_mat, Modifier.weight(1f))
                }

                Spacer(modifier = Modifier.height(8.dp))
                HorizontalDivider(color = colors.textColor.copy(alpha = 0.07f))
                Spacer(modifier = Modifier.height(8.dp))

                // â”€â”€ Bloque 4: Datos adicionales â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    HistInfoItem("Ingresante", item.ingresante, Modifier.weight(1f))
                    HistInfoItem("Traslado", item.traslado, Modifier.weight(1f))
                    HistInfoItem("Mod. matricula", item.mod_matricula, Modifier.weight(1f))
                }

                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    HistInfoItem("Beca", item.beca, Modifier.weight(1f))
                    Spacer(modifier = Modifier.weight(1f))
                    Spacer(modifier = Modifier.weight(1f))
                }

                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    HistInfoItem("Tipo pago", item.tipo_pago, Modifier.weight(1f))
                    if (item.exec_tipo_tari.isNotEmpty()) {
                        HistInfoItem("Tipo tarifa", item.exec_tipo_tari, Modifier.weight(1f))
                    } else {
                        Spacer(modifier = Modifier.weight(1f))
                    }
                    Row(
                        modifier = Modifier.weight(1f),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Email,
                            contentDescription = null,
                            tint = colors.textColor.copy(alpha = 0.55f),
                            modifier = Modifier.size(14.dp)
                        )
                        Text(
                            text = "Enviar correo",
                            style = MaterialTheme.typography.labelSmall,
                            color = colors.textColor.copy(alpha = 0.55f)
                        )
                        Checkbox(
                            checked = true,
                            onCheckedChange = null,
                            enabled = false,
                            modifier = Modifier.size(20.dp),
                            colors = CheckboxDefaults.colors(
                                disabledCheckedColor = colors.colorMixPrimary,
                                checkmarkColor = colors.colorBlanco
                            )
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun HistInfoItem(
    label: String,
    value: String,
    modifier: Modifier = Modifier,
    valueColor: Color? = null
) {
    val colors = getColorsTheme()
    Column(modifier = modifier) {
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall,
            color = colors.textColor.copy(alpha = 0.45f)
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodySmall,
            fontWeight = FontWeight.Medium,
            color = valueColor ?: colors.textColor
        )
    }
}
