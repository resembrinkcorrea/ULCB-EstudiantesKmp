package pe.lecordonbleu.universidadestudiante.presentation.screens.mallacurricular.customcell

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import pe.lecordonbleu.universidadestudiante.data.remote.dto.ListTablaPlanEstudio
import pe.lecordonbleu.universidadestudiante.getColorsTheme
import pe.lecordonbleu.universidadestudiante.presentation.screens.mallacurricular.EstadoAsignaturaVisual

@Composable
fun TablaAsignaturaItemCell(item: ListTablaPlanEstudio) {
    val colors = getColorsTheme()
    val labelColor = colors.colorGrisNeutro
    val valueColor = colors.textColor
    val titleColor = colors.colorMixPrimary

    val estadoMallaColor = if (item.ESTADO_MALLA.equals("Cursado", ignoreCase = true)) {
        titleColor
    } else {
        colors.textColor
    }

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(8.dp),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Column(modifier = Modifier.padding(12.dp)) {

            Text(
                text = item.ASIGNATURA,
                fontWeight = FontWeight.Bold,
                color = titleColor,
                fontSize = 14.sp
            )

            Spacer(modifier = Modifier.height(6.dp))

            Row(Modifier.fillMaxWidth()) {
                Text(text = "Créditos: ", fontSize = 12.sp, color = labelColor)
                Text(text = "${item.CREDITOS}", fontSize = 12.sp, color = valueColor, modifier = Modifier.weight(1f))
                Text(text = "Periodo: ", fontSize = 12.sp, color = labelColor)
                Text(text = item.PERIODOS_CURSADOS, fontSize = 12.sp, color = valueColor)
            }

            Row(Modifier.fillMaxWidth()) {
                Text(text = "Clase: ", fontSize = 12.sp, color = labelColor)
                Text(text = item.CLASE, fontSize = 12.sp, color = valueColor, modifier = Modifier.weight(1f))
                Text(text = "Tipo: ", fontSize = 12.sp, color = labelColor)
                Text(text = item.TIPO_ASIGNATURA, fontSize = 12.sp, color = valueColor)
            }

            Row(Modifier.fillMaxWidth()) {
                Text(text = "Promedio Final: ", fontSize = 12.sp, color = labelColor)
                Text(text = item.PROMEDIO_FINAL, fontSize = 12.sp, color = valueColor, modifier = Modifier.weight(1f))
                Text(text = "Estado Malla: ", fontSize = 12.sp, color = labelColor)
                Text(text = item.ESTADO_MALLA, fontSize = 12.sp, color = estadoMallaColor, fontWeight = FontWeight.Bold)
            }

            if (item.PREREQUISITO.isNotBlank()) {
                Spacer(modifier = Modifier.height(4.dp))
                Row {
                    Text(text = "Prerrequisito: ", fontSize = 12.sp, color = labelColor)
                    Text(text = item.PREREQUISITO, fontSize = 12.sp, color = valueColor)
                }
            }

            val estado = EstadoAsignaturaVisual.fromHtml(item.ESTADO_ASIGNATURA)
            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = "Estado Asignatura:", fontWeight = FontWeight.SemiBold, fontSize = 12.sp, color = valueColor)
                Spacer(modifier = Modifier.width(8.dp))

                when (estado) {
                    EstadoAsignaturaVisual.APROBADO -> {
                        Surface(
                            color = colors.colorVerdeMedio.copy(alpha = 0.15f),
                            shape = RoundedCornerShape(16.dp)
                        ) {
                            Text(
                                text = "Aprobado",
                                color = colors.colorVerdeMedio,
                                fontWeight = FontWeight.Bold,
                                fontSize = 12.sp,
                                modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp)
                            )
                        }
                    }

                    EstadoAsignaturaVisual.DESAPROBADO -> {
                        Surface(
                            color = colors.colorRojo.copy(alpha = 0.15f),
                            shape = RoundedCornerShape(16.dp)
                        ) {
                            Text(
                                text = "Desaprobado",
                                color = colors.colorRojo,
                                fontWeight = FontWeight.Bold,
                                fontSize = 12.sp,
                                modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp)
                            )
                        }
                    }

                    EstadoAsignaturaVisual.NINGUNO -> {
                        Text(text = "-", fontSize = 12.sp, color = colors.colorGrisNeutro)
                    }
                }
            }
        }
    }
}
