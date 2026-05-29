package pe.lecordonbleu.universidadestudiante.presentation.screens.archivoscompartidos.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Description
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import pe.lecordonbleu.universidadestudiante.data.remote.dto.ContenidoTags
import pe.lecordonbleu.universidadestudiante.getColorsTheme
import pe.lecordonbleu.universidadestudiante.getPlatformContext
import pe.lecordonbleu.universidadestudiante.util.openUrl
import pe.lecordonbleu.universidadestudiante.util.renderHtmlToText

@Composable
fun ContenidoArchivoItem(doc: ContenidoTags, onClick: () -> Unit, onCheckClick: () -> Unit) {
    val context = getPlatformContext()
    val colors = getColorsTheme()

    val docColor = when (doc.extension_docu.lowercase()) {
        "pdf"         -> colors.colorStripeRojo
        "docx", "doc" -> colors.colorStripeAzul
        "xls", "xlsx" -> colors.colorStripeVerde
        else          -> colors.colorGrisNeutro
    }

    val cleanText = renderHtmlToText(doc.descripcion_docu)

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp, vertical = 6.dp)
            .clickable {
                openUrl(context, doc.url_docu)
                onClick()
            },
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = colors.colorExpenseItem),
        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp),
        border = BorderStroke(1.dp, colors.colorAzulProfundo.copy(alpha = 0.6f))
    ) {
        Column(modifier = Modifier.fillMaxWidth().padding(16.dp)) {

            // ─── Header: icono + título ───────────────────────────────
            Row(
                verticalAlignment = Alignment.Top,
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(44.dp)
                        .background(docColor.copy(alpha = 0.12f), shape = RoundedCornerShape(8.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Description,
                        contentDescription = null,
                        tint = docColor,
                        modifier = Modifier.size(24.dp)
                    )
                }
                Text(
                    text = doc.nombre_docu,
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp,
                    color = colors.textColor,
                    modifier = Modifier.weight(1f)
                )
            }

            // ─── Descripción ─────────────────────────────────────────
            if (cleanText.isNotBlank()) {
                Spacer(Modifier.height(10.dp))
                Text(
                    text = cleanText,
                    fontSize = 13.sp,
                    color = colors.textColor.copy(alpha = 0.6f)
                )
            }

            Spacer(Modifier.height(12.dp))
            HorizontalDivider(color = colors.textColor.copy(alpha = 0.08f))
            Spacer(Modifier.height(10.dp))

            // ─── Footer fila 1: fecha · extensión · checkbox ─────────
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(5.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.CalendarToday,
                        contentDescription = null,
                        tint = colors.textColor.copy(alpha = 0.4f),
                        modifier = Modifier.size(12.dp)
                    )
                    Text(
                        text = doc.last_fec_modif.take(10),
                        fontSize = 12.sp,
                        color = colors.textColor.copy(alpha = 0.5f)
                    )
                    Text(
                        text = "•",
                        fontSize = 12.sp,
                        color = colors.textColor.copy(alpha = 0.3f)
                    )
                    Text(
                        text = doc.extension_docu.uppercase(),
                        fontSize = 12.sp,
                        color = docColor,
                        fontWeight = FontWeight.SemiBold
                    )
                }

                if (doc.flag_descargado == 1) {
                    Box(
                        modifier = Modifier
                            .size(22.dp)
                            .background(
                                color = if (doc.flag_leido == 1) colors.colorEsmeralda else Color.Transparent,
                                shape = RoundedCornerShape(5.dp)
                            )
                            .border(
                                width = 1.5.dp,
                                color = if (doc.flag_leido == 1) colors.colorEsmeralda else colors.textColor.copy(alpha = 0.35f),
                                shape = RoundedCornerShape(5.dp)
                            )
                            .clickable { onCheckClick() },
                        contentAlignment = Alignment.Center
                    ) {
                        if (doc.flag_leido == 1) {
                            Icon(
                                imageVector = Icons.Default.Check,
                                contentDescription = null,
                                tint = colors.colorBlanco,
                                modifier = Modifier.size(14.dp)
                            )
                        }
                    }
                }
            }

            // ─── Footer fila 2: botón VER ─────────────────────────────
            Spacer(Modifier.height(14.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                Box(
                    modifier = Modifier
                        .background(
                            color = colors.colorAzulOscuro,
                            shape = RoundedCornerShape(50.dp)
                        )
                        .clickable {
                            openUrl(context, doc.url_docu)
                            onClick()
                        }
                        .padding(horizontal = 20.dp, vertical = 8.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "VER",
                        color = colors.colorBlanco,
                        fontWeight = FontWeight.Bold,
                        fontSize = 12.sp
                    )
                }
            }
        }
    }
}
