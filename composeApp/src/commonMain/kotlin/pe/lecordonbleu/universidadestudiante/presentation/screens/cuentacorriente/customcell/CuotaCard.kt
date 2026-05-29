package pe.lecordonbleu.universidadestudiante.presentation.screens.cuentacorriente.customcell

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Article
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material.icons.filled.Receipt
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import pe.lecordonbleu.universidadestudiante.DarkModeColors
import pe.lecordonbleu.universidadestudiante.core.theme.IlcbGrisCharcoal
import pe.lecordonbleu.universidadestudiante.data.remote.dto.ListDetalleCuentaCorriente
import pe.lecordonbleu.universidadestudiante.presentation.screens.cuentacorriente.CuentaCorrienteFlag

@Composable
fun CuotaCard(
    item: CuentaCorrienteFlag,
    detalles: List<ListDetalleCuentaCorriente>,
    isExpanded: Boolean,
    colors: DarkModeColors,
    onExpandClick: () -> Unit,
    onCheckboxChanged: (Boolean) -> Unit,
    onChildItemClick: (boleta: String, flagPecano: Int, tipoDocuPecano: Int, fechaOperacion: String) -> Unit
) {
    val estadoNombre = item.estado_nombre.trim().uppercase()
    val estadoColor = when (estadoNombre) {
        "PAGADO" -> colors.colorVerdeMedio
        "PENDIENTE" -> colors.colorAmbar
        "VENCIDO", "PENDIENTE/VENCIDO" -> colors.colorRojo
        else -> when (item.estado) {
            1 -> colors.colorVerdeMedio
            2 -> colors.colorAmbar
            else -> colors.colorRojo
        }
    }
    val glassHighlight = Color.White.copy(alpha = 0.12f)

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
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(IntrinsicSize.Min)
            ) {

                // ── Columna izquierda: checkbox / ícono pagado ───────────────────
                Column(
                    modifier = Modifier
                        .width(52.dp)
                        .fillMaxHeight(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    if (item.estado != 1) {
                        if (!item.isEnabled) {
                            Surface(
                                shape = RoundedCornerShape(5.dp),
                                color = IlcbGrisCharcoal,
                                modifier = Modifier.size(20.dp)
                            ) {
                                if (item.isChecked) {
                                    Box(contentAlignment = Alignment.Center) {
                                        Icon(
                                            imageVector = Icons.Default.Check,
                                            contentDescription = null,
                                            tint = colors.colorBlanco,
                                            modifier = Modifier.size(14.dp)
                                        )
                                    }
                                }
                            }
                        } else {
                            Checkbox(
                                checked = item.isChecked,
                                onCheckedChange = onCheckboxChanged,
                                enabled = true,
                                colors = CheckboxDefaults.colors(
                                    checkedColor = colors.colorMixPrimary,
                                    uncheckedColor = colors.textColor.copy(alpha = 0.4f),
                                    checkmarkColor = colors.colorBlanco,
                                    disabledCheckedColor = IlcbGrisCharcoal,
                                    disabledUncheckedColor = IlcbGrisCharcoal
                                )
                            )
                        }
                    } else {
                        Surface(
                            shape = RoundedCornerShape(50),
                            color = colors.colorVerdeMedio.copy(alpha = 0.15f),
                            modifier = Modifier.size(36.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.CheckCircle,
                                contentDescription = null,
                                tint = colors.colorVerdeMedio,
                                modifier = Modifier
                                    .padding(6.dp)
                                    .size(24.dp)
                            )
                        }
                    }
                }

                // ── Columna central: toda la información ─────────────────────────
                Column(modifier = Modifier.weight(1f)) {

                    // ── Header ──────────────────────────────────────────────────
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = 8.dp, end = 4.dp, top = 14.dp, bottom = 4.dp),
                        verticalAlignment = Alignment.Top,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = "Cuota ${item.nro_cuota}",
                                style = MaterialTheme.typography.titleSmall,
                                fontWeight = FontWeight.Bold,
                                color = colors.textColor
                            )
                            Text(
                                text = item.concepto_nombre,
                                style = MaterialTheme.typography.bodySmall,
                                color = colors.textColor.copy(alpha = 0.7f),
                                modifier = Modifier.padding(top = 2.dp)
                            )
                            Text(
                                text = item.periodo,
                                style = MaterialTheme.typography.labelSmall,
                                color = colors.textColor.copy(alpha = 0.45f),
                                modifier = Modifier.padding(top = 2.dp)
                            )
                        }
                        Surface(
                            shape = RoundedCornerShape(50),
                            color = estadoColor.copy(alpha = 0.15f),
                            modifier = Modifier.padding(top = 2.dp)
                        ) {
                            Text(
                                text = item.estado_nombre,
                                style = MaterialTheme.typography.labelSmall,
                                fontWeight = FontWeight.SemiBold,
                                color = estadoColor,
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                            )
                        }
                    }

                    // ── Monto pendiente destacado ────────────────────────────────
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = 8.dp, end = 4.dp, top = 10.dp, bottom = 10.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.Bottom
                    ) {
                        Column {
                            Text(
                                text = "Pendiente",
                                style = MaterialTheme.typography.labelSmall,
                                color = colors.textColor.copy(alpha = 0.5f)
                            )
                            Text(
                                text = "S/ ${item.monto_pendiente}",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = when (item.estado) {
                                    1 -> colors.colorVerdeMedio
                                    else -> colors.colorAmbar
                                }
                            )
                        }
                        Column(horizontalAlignment = Alignment.End) {
                            Text(
                                text = "Total  S/ ${item.monto_total}",
                                style = MaterialTheme.typography.labelSmall,
                                color = colors.textColor.copy(alpha = 0.6f)
                            )
                            Text(
                                text = "Pagado  S/ ${item.monto_total_pago}",
                                style = MaterialTheme.typography.labelSmall,
                                color = colors.colorVerdeMedio.copy(alpha = 0.85f),
                                modifier = Modifier.padding(top = 2.dp)
                            )
                        }
                    }

                    // ── Detalle de montos ────────────────────────────────────────
                    HorizontalDivider(
                        modifier = Modifier.padding(end = 4.dp),
                        color = colors.textColor.copy(alpha = 0.08f)
                    )
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = 8.dp, end = 4.dp, top = 8.dp, bottom = 4.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        CuotaInfoItem("Importe", item.monto_inicial, colors, Modifier.weight(1f))
                        CuotaInfoItem(
                            "Descuento", item.monto_descuento, colors, Modifier.weight(1f),
                            valueColor = if ((item.monto_descuento.toDoubleOrNull() ?: 0.0) > 0.0)
                                colors.colorVerdeMedio else colors.textColor
                        )
                        CuotaInfoItem("Mora", item.monto_mora, colors, Modifier.weight(1f))
                    }

                    // ── Fechas ───────────────────────────────────────────────────
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = 8.dp, end = 4.dp, top = 4.dp, bottom = 14.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        CuotaInfoItem("F. Vencimiento", item.fec_vencimiento, colors, Modifier.weight(1f))
                        CuotaInfoItem("F. Pago", item.fecha_pago, colors, Modifier.weight(1f))
                        Spacer(modifier = Modifier.weight(1f))
                    }

                    // ── Sección expandida ────────────────────────────────────────
                    AnimatedVisibility(visible = isExpanded) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(start = 4.dp, end = 4.dp, bottom = 12.dp)
                                .clip(RoundedCornerShape(12.dp))
                                .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f))
                                .padding(start = 10.dp, end = 10.dp, top = 10.dp, bottom = 10.dp)
                        ) {
                            Text(
                                text = "Pagos registrados",
                                style = MaterialTheme.typography.labelMedium,
                                fontWeight = FontWeight.SemiBold,
                                color = colors.textColor.copy(alpha = 0.55f),
                                modifier = Modifier.padding(bottom = 6.dp)
                            )
                            if (detalles.isEmpty()) {
                                Text(
                                    text = "Sin pagos registrados",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = colors.textColor.copy(alpha = 0.4f),
                                    modifier = Modifier.padding(vertical = 4.dp)
                                )
                            } else {
                                detalles.forEachIndexed { idx, detalle ->
                                    if (idx > 0) {
                                        HorizontalDivider(
                                            modifier = Modifier.padding(vertical = 2.dp),
                                            color = colors.textColor.copy(alpha = 0.06f)
                                        )
                                    }
                                    DetallePagoItem(
                                        detalle = detalle,
                                        colors = colors,
                                        onClick = {
                                            onChildItemClick(
                                                detalle.nro_comprobante,
                                                detalle.flag_pecano,
                                                detalle.tipoDocu_pecano,
                                                detalle.fecha_operacion
                                            )
                                        }
                                    )
                                }
                            }
                        }
                    }
                }

                // ── Columna derecha: botón expandir ──────────────────────────────
                Column(
                    modifier = Modifier
                        .width(48.dp)
                        .fillMaxHeight()
                        .padding(end = 8.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    IconButton(onClick = onExpandClick, modifier = Modifier.size(40.dp)) {
                        Icon(
                            imageVector = if (isExpanded) Icons.Default.ExpandLess else Icons.Default.ExpandMore,
                            contentDescription = null,
                            tint = colors.colorMixPrimary
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun CuotaInfoItem(
    label: String,
    value: String,
    colors: DarkModeColors,
    modifier: Modifier = Modifier,
    valueColor: Color = colors.textColor
) {
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
            color = valueColor
        )
    }
}

@Composable
private fun DetallePagoItem(
    detalle: ListDetalleCuentaCorriente,
    colors: DarkModeColors,
    onClick: () -> Unit
) {
    val tieneComprobante = detalle.flag_pecano == 1 || detalle.nro_comprobante.isNotEmpty()

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp))
            .then(if (tieneComprobante) Modifier.clickable(onClick = onClick) else Modifier)
            .padding(vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = Icons.Default.Receipt,
            contentDescription = null,
            tint = colors.colorMixPrimary.copy(alpha = 0.65f),
            modifier = Modifier.size(20.dp)
        )
        Spacer(modifier = Modifier.width(12.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = detalle.medio_pago_nombre,
                style = MaterialTheme.typography.bodySmall,
                fontWeight = FontWeight.SemiBold,
                color = colors.textColor
            )
            Text(
                text = "${detalle.docu_nombre} ${detalle.nro_comprobante}",
                style = MaterialTheme.typography.labelSmall,
                color = colors.textColor.copy(alpha = 0.6f),
                modifier = Modifier.padding(top = 1.dp)
            )
            Text(
                text = detalle.fecha_pago,
                style = MaterialTheme.typography.labelSmall,
                color = colors.textColor.copy(alpha = 0.4f),
                modifier = Modifier.padding(top = 1.dp)
            )
        }
        Column(horizontalAlignment = Alignment.End) {
            Text(
                text = "S/ ${detalle.monto_pagado}",
                style = MaterialTheme.typography.bodySmall,
                fontWeight = FontWeight.Bold,
                color = colors.colorVerdeMedio
            )
            if (tieneComprobante) {
                Spacer(modifier = Modifier.height(4.dp))
                Surface(
                    shape = RoundedCornerShape(50),
                    color = colors.colorMixPrimary.copy(alpha = 0.12f)
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Article,
                            contentDescription = null,
                            tint = colors.colorMixPrimary,
                            modifier = Modifier.size(12.dp)
                        )
                        Text(
                            text = "Ver comprobante",
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = FontWeight.SemiBold,
                            color = colors.colorMixPrimary
                        )
                    }
                }
            }
        }
    }
}
