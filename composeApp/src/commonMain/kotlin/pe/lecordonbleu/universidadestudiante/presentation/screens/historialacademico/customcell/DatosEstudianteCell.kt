package pe.lecordonbleu.universidadestudiante.presentation.screens.historialacademico.customcell

import androidx.compose.foundation.BorderStroke
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Badge
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import pe.lecordonbleu.universidadestudiante.data.remote.dto.DataHistorialAcadamico
import pe.lecordonbleu.universidadestudiante.getColorsTheme

@Composable
fun DatosEstudianteCell(estudiante: DataHistorialAcadamico) {
    val colors = getColorsTheme()

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 12.dp),
        color = colors.colorExpenseItem,
        shape = RoundedCornerShape(24.dp),
        border = BorderStroke(1.dp, colors.textColor.copy(alpha = 0.05f)),
        shadowElevation = 6.dp
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(colors.colorMixPrimary.copy(alpha = 0.1f))
                    .padding(horizontal = 20.dp, vertical = 16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Surface(
                    color = colors.colorMixPrimary.copy(alpha = 0.2f),
                    shape = CircleShape,
                    modifier = Modifier.size(36.dp)
                ) {
                    Icon(
                        imageVector = Icons.Rounded.Badge,
                        contentDescription = null,
                        tint = colors.colorMixPrimary,
                        modifier = Modifier.padding(8.dp)
                    )
                }
                Spacer(modifier = Modifier.width(14.dp))
                Text(
                    text = "DATOS DEL ESTUDIANTE",
                    color = colors.colorMixPrimary,
                    fontWeight = FontWeight.ExtraBold,
                    letterSpacing = 1.sp,
                    style = MaterialTheme.typography.titleSmall
                )
            }

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp),
                verticalArrangement = Arrangement.spacedBy(20.dp)
            ) {
                Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    Text(text = "NOMBRE COMPLETO", fontSize = 10.sp, color = colors.textColor.copy(alpha = 0.4f), fontWeight = FontWeight.Normal, letterSpacing = 0.5.sp)
                    Text(text = "${estudiante.est_apellido_pat} ${estudiante.est_apellido_mat}, ${estudiante.e_est_nombre}", fontSize = 14.sp, color = colors.textColor.copy(alpha = 0.9f), fontWeight = FontWeight.Medium)
                }

                Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                    Box(modifier = Modifier.weight(1f)) {
                        Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                            Text(text = "CODIGO", fontSize = 10.sp, color = colors.textColor.copy(alpha = 0.4f), fontWeight = FontWeight.Normal, letterSpacing = 0.5.sp)
                            Text(text = estudiante.pestd_cod, fontSize = 14.sp, color = colors.textColor.copy(alpha = 0.9f), fontWeight = FontWeight.Medium)
                        }
                    }
                    Box(modifier = Modifier.height(30.dp).width(1.dp).background(colors.textColor.copy(alpha = 0.1f)))
                    Box(modifier = Modifier.weight(1f).padding(start = 16.dp)) {
                        Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                            Text(text = "TELEFONO", fontSize = 10.sp, color = colors.textColor.copy(alpha = 0.4f), fontWeight = FontWeight.Normal, letterSpacing = 0.5.sp)
                            Text(text = estudiante.est_telef01_pers, fontSize = 14.sp, color = colors.textColor.copy(alpha = 0.9f), fontWeight = FontWeight.Medium)
                        }
                    }
                }

                HorizontalDivider(color = colors.textColor.copy(alpha = 0.05f))

                Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                    Box(modifier = Modifier.weight(1f)) {
                        Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                            Text(text = "DOCUMENTO", fontSize = 10.sp, color = colors.textColor.copy(alpha = 0.4f), fontWeight = FontWeight.Normal, letterSpacing = 0.5.sp)
                            Text(text = "DNI", fontSize = 14.sp, color = colors.textColor.copy(alpha = 0.9f), fontWeight = FontWeight.Medium)
                        }
                    }
                    Box(modifier = Modifier.height(30.dp).width(1.dp).background(colors.textColor.copy(alpha = 0.1f)))
                    Box(modifier = Modifier.weight(1f).padding(start = 16.dp)) {
                        Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                            Text(text = "NUMERO DOC.", fontSize = 10.sp, color = colors.textColor.copy(alpha = 0.4f), fontWeight = FontWeight.Normal, letterSpacing = 0.5.sp)
                            Text(text = estudiante.num_docu_iden_pd, fontSize = 14.sp, color = colors.textColor.copy(alpha = 0.9f), fontWeight = FontWeight.Medium)
                        }
                    }
                }

                HorizontalDivider(color = colors.textColor.copy(alpha = 0.05f))

                Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    Text(text = "CORREO INSTITUCIONAL", fontSize = 10.sp, color = colors.textColor.copy(alpha = 0.4f), fontWeight = FontWeight.Normal, letterSpacing = 0.5.sp)
                    Text(text = estudiante.est_correoelec_personal, fontSize = 14.sp, color = colors.textColor.copy(alpha = 0.9f), fontWeight = FontWeight.Medium)
                }
            }
        }
    }
}
