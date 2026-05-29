package pe.lecordonbleu.universidadestudiante.presentation.screens.home.customcell

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import pe.lecordonbleu.universidadestudiante.data.remote.dto.ListEstadoAulaDemoDocente

@Composable
fun AulaDemoDialog(
    data: List<ListEstadoAulaDemoDocente>,
    onDismiss: () -> Unit,
    onHabilitarMarcacion: (ListEstadoAulaDemoDocente) -> Unit
) {
    if (data.isEmpty()) return

    val clase = data.first()

    AlertDialog(
        onDismissRequest = onDismiss,
        backgroundColor = Color(0xFFF9F9F9),
        shape = RoundedCornerShape(20.dp),
        text = {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = clase.asign_det_nombre,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF0D47A1),
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = "Horario: ${clase.hor_inicio} - ${clase.hor_fin}",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF1B5E20),
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "Fecha: ${clase.fecha}",
                    fontSize = 16.sp,
                    color = Color.DarkGray,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center
                )
            }
        },
        buttons = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Button(
                    onClick = { onHabilitarMarcacion(clase) },
                    shape = RoundedCornerShape(30),
                    colors = ButtonDefaults.buttonColors(
                        backgroundColor = Color(0xFF2E7D32),
                        contentColor = Color.White
                    )
                ) {
                    Text("Habilitar Marcación")
                }
                Spacer(modifier = Modifier.width(16.dp))
                OutlinedButton(
                    onClick = onDismiss,
                    shape = RoundedCornerShape(30),
                    colors = ButtonDefaults.outlinedButtonColors(
                        contentColor = Color(0xFFD32F2F)
                    )
                ) {
                    Text("Salir")
                }
            }
        }
    )
}
