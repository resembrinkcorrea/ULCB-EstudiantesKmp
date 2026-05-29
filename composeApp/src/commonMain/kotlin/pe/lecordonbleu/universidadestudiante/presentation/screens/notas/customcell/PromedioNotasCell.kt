package pe.lecordonbleu.universidadestudiante.presentation.screens.notas.customcell

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import pe.lecordonbleu.universidadestudiante.data.remote.dto.ListadoNotasDetalle
import pe.lecordonbleu.universidadestudiante.getColorsTheme

@Composable
fun PromedioNotasCell(
    item: ListadoNotasDetalle,
    onSimularClick: () -> Unit
) {
    val colors = getColorsTheme()

    // 1. Campo base numérico
    val notaMin = item.nota_min.toFloatOrNull() ?: 0f

    // 2. Campos a comparar — parseados con toFloatOrNull(), sin modificar el valor del servidor
    val notaPerm  = item.not_prac_calif.toFloatOrNull()
    val notaParc  = item.not_exam_parc.toFloatOrNull()
    val notaFinal = item.not_exam_final.toFloatOrNull()
    val notaSusti = item.not_exam_susti.toFloatOrNull()
    val notaProm  = item.not_prom_final.toFloatOrNull()

    // 3. Lambda de color — numérica pura
    val notaColor: (Float?) -> Color = { nota ->
        nota?.let { if (notaMin < it) colors.colorAzulMedio else colors.colorRojo }
            ?: colors.textColor.copy(alpha = 0.5f)
    }

    // 4. Colores resultantes — guard explícito por campo: "0.00" es centinela del servidor
    val colorPerm  = if (item.not_prac_calif == "0.00") colors.textColor.copy(alpha = 0.5f) else notaColor(notaPerm)
    val colorParc  = if (item.not_exam_parc  == "0.00") colors.textColor.copy(alpha = 0.5f) else notaColor(notaParc)
    val colorFin   = if (item.not_exam_final == "0.00") colors.textColor.copy(alpha = 0.5f) else notaColor(notaFinal)
    val colorSusti = notaColor(notaSusti)

    // Estado texto por nota — null (no parseable) o "0.00" → PENDIENTE
    val estadoTexto: (String, Float?) -> Pair<String, Color> = { str, nota ->
        when {
            nota == null || str == "0.00" -> "PENDIENTE"   to colors.textColor.copy(alpha = 0.4f)
            nota < notaMin                -> "DESAPROBADO" to colors.colorRojo
            else                          -> "APROBADO"    to colors.colorAzulMedio
        }
    }

    // 5. Color de estado — flag_aprobado con ignoreCase; not_prom_final tiene prioridad si es número
    val headerEstado: Pair<String, Color> = if (notaProm != null) {
        if (notaProm < notaMin) "ESTADO: DESAPROBADO" to colors.colorRojo
        else                    "ESTADO: APROBADO"    to colors.colorMixPrimary
    } else when {
        item.flag_aprobado.equals("1", ignoreCase = true) -> "ESTADO: APROBADO"    to colors.colorMixPrimary
        item.flag_aprobado.equals("2", ignoreCase = true) -> "ESTADO: -"           to colors.textColor.copy(alpha = 0.5f)
        else                                               -> "ESTADO: DESAPROBADO" to colors.colorRojo
    }

    // 6. Pesos — directo del backend × 100
    val pesoPerm  = (item.peso_prac_calif.toDoubleOrNull()?.times(100))?.toInt() ?: 0
    val pesoParc  = (item.peso_exam_parc.toDoubleOrNull()?.times(100))?.toInt()  ?: 0
    val pesoFinal = (item.peso_exam_final.toDoubleOrNull()?.times(100))?.toInt() ?: 0

    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Header — promedio final
        Surface(
            modifier = Modifier.fillMaxWidth(),
            shape    = RoundedCornerShape(16.dp),
            color    = headerEstado.second
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text          = headerEstado.first,
                    style         = MaterialTheme.typography.labelMedium,
                    fontWeight    = FontWeight.Bold,
                    color         = Color.White.copy(alpha = 0.9f),
                    letterSpacing = 0.5.sp
                )
                Spacer(modifier = Modifier.height(12.dp))
                Row(verticalAlignment = Alignment.Bottom) {
                    Text(
                        text       = item.not_prom_final,
                        fontSize   = 36.sp,
                        fontWeight = FontWeight.Black,
                        color      = Color.White
                    )
                    Text(
                        text     = " / 20",
                        style    = MaterialTheme.typography.titleMedium,
                        color    = Color.White.copy(alpha = 0.7f),
                        modifier = Modifier.padding(bottom = 4.dp)
                    )
                }
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text  = "Promedio Final del Curso",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.White.copy(alpha = 0.8f)
                )
            }
        }

        // Sección desglose
        Text(
            text          = "DESGLOSE ACADEMICO",
            style         = MaterialTheme.typography.labelMedium,
            fontWeight    = FontWeight.Bold,
            color         = colors.textColor.copy(alpha = 0.5f),
            letterSpacing = 0.8.sp
        )

        // Filas — datos directos del DTO, colores ya calculados arriba
        EvaluacionItem(
            nombre    = "Practicas Calificadas",
            nota      = item.not_prac_calif,
            peso      = "PESO: $pesoPerm%",
            icon      = Icons.Default.Edit,
            estado    = estadoTexto(item.not_prac_calif, notaPerm),
            colorNota = colorPerm
        )
        EvaluacionItem(
            nombre    = "Examen Parcial",
            nota      = item.not_exam_parc,
            peso      = "PESO: $pesoParc%",
            icon      = Icons.Default.List,
            estado    = estadoTexto(item.not_exam_parc, notaParc),
            colorNota = colorParc
        )
        EvaluacionItem(
            nombre    = "Examen Final",
            nota      = item.not_exam_final,
            peso      = "PESO: $pesoFinal%",
            icon      = Icons.Default.Star,
            estado    = estadoTexto(item.not_exam_final, notaFinal),
            colorNota = colorFin
        )
        val tieneParcial   = item.not_exam_parc != "0.00" && item.not_exam_parc.toFloatOrNull() != null
        val esDesaprobado  = notaProm != null && notaProm < notaMin
        if (tieneParcial && esDesaprobado) {
            EvaluacionItem(
                nombre    = "Examen Sustitutorio",
                nota      = item.not_exam_susti,
                peso      = "",
                icon      = Icons.Default.Warning,
                estado    = estadoTexto(item.not_exam_susti, notaSusti),
                colorNota = colorSusti
            )
        }

        // Simulador
        OutlinedButton(
            onClick  = onSimularClick,
            modifier = Modifier.fillMaxWidth().height(48.dp),
            shape    = RoundedCornerShape(12.dp),
            border   = BorderStroke(1.dp, colors.colorMixPrimary),
            colors   = ButtonDefaults.outlinedButtonColors(contentColor = colors.colorMixPrimary)
        ) {
            Icon(
                imageVector        = Icons.Default.Star,
                contentDescription = null,
                modifier           = Modifier.size(18.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text       = "SIMULADOR DE NOTA FINAL",
                fontWeight = FontWeight.Bold,
                fontSize   = 12.sp
            )
        }
    }
}

@Composable
private fun EvaluacionItem(
    nombre    : String,
    nota      : String,
    peso      : String,
    icon      : ImageVector,
    estado    : Pair<String, Color>,
    colorNota : Color
) {
    val colors      = getColorsTheme()
    val notaDisplay = if (nota == "0.00") "-" else nota

    Surface(
        modifier        = Modifier.fillMaxWidth(),
        shape           = RoundedCornerShape(12.dp),
        color           = colors.colorExpenseItem,
        shadowElevation = 1.dp
    ) {
        Row(
            modifier          = Modifier.padding(horizontal = 12.dp, vertical = 14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Surface(
                shape = RoundedCornerShape(8.dp),
                color = colors.textColor.copy(alpha = 0.06f)
            ) {
                Icon(
                    imageVector        = icon,
                    contentDescription = null,
                    tint               = colors.textColor.copy(alpha = 0.5f),
                    modifier           = Modifier.padding(8.dp).size(20.dp)
                )
            }
            Spacer(modifier = Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text       = nombre,
                    style      = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.SemiBold,
                    color      = colors.textColor
                )
                if (peso.isNotEmpty()) {
                    Text(
                        text  = peso,
                        style = MaterialTheme.typography.labelSmall,
                        color = colors.textColor.copy(alpha = 0.4f)
                    )
                }
            }
            Column(horizontalAlignment = Alignment.End) {
                Text(
                    text       = notaDisplay,
                    style      = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color      = colorNota
                )
                Text(
                    text       = estado.first,
                    style      = MaterialTheme.typography.labelSmall,
                    fontWeight = FontWeight.Bold,
                    color      = estado.second
                )
            }
        }
    }
}
