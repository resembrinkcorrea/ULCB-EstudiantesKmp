package pe.lecordonbleu.universidadestudiante.presentation.screens.home.customcell

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.AlertDialog
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import pe.lecordonbleu.universidadestudiante.data.remote.dto.ListClaseHabilitada

@Composable
fun ClaseHabilitadaDialog(
    data: List<ListClaseHabilitada>,
    onDismiss: () -> Unit
) {
    if (data.isEmpty()) return
    val clase = data.first()

    val fecha = clase.hora_inicio.split(" ").firstOrNull() ?: ""
    val horaInicio = clase.hora_inicio.split(" ").getOrNull(1)?.replace(".0", "") ?: ""
    val horaFin = clase.hora_fin.split(" ").getOrNull(1)?.replace(".0", "") ?: ""
    val rango = "$horaInicio - $horaFin"

    val textoBase = clase.notificacion.substringBefore(fecha).trim()

    val iconoEstado = when (clase.tipo) {
        1 -> Icons.Default.Check
        0 -> Icons.Default.Close
        else -> Icons.Default.ArrowForward
    }
    val iconColor = when (clase.tipo) {
        1 -> Color(0xFF2E7D32)
        0 -> Color(0xFFC62828)
        else -> Color.Gray
    }

    AlertDialog(
        onDismissRequest = onDismiss,
        backgroundColor = Color(0xFFF9F9F9),
        shape = RoundedCornerShape(20.dp),
        title = {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp, bottom = 4.dp),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = iconoEstado,
                    contentDescription = null,
                    tint = iconColor,
                    modifier = Modifier.size(48.dp)
                )
            }
        },
        text = {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = clase.mensaje,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = if (clase.tipo == 0) Color(0xFFC62828) else Color(0xFF0D47A1),
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = textoBase,
                    fontSize = 16.sp,
                    color = Color.DarkGray,
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = fecha,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF1B5E20),
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = rango,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = if (clase.tipo == 0) Color(0xFFC62828) else Color(0xFF0D47A1),
                    textAlign = TextAlign.Center
                )
            }
        },
        buttons = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.Center
            ) {
                Button(
                    onClick = onDismiss,
                    shape = RoundedCornerShape(30),
                    colors = ButtonDefaults.buttonColors(
                        backgroundColor = Color(0xFF2E7D32),
                        contentColor = Color.White
                    )
                ) {
                    Text("Aceptar")
                }
            }
        }
    )
}
