package pe.lecordonbleu.universidadestudiante.presentation.components.dialogs

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import pe.lecordonbleu.universidadestudiante.core.theme.IlcbGreenMid
import pe.lecordonbleu.universidadestudiante.getColorsTheme
import pe.lecordonbleu.universidadestudiante.getColorsTheme

@Composable
fun ConfirmarEnvioDialog(
    nombreArchivo: String,
    onConfirmar: () -> Unit,
    onCancelar: () -> Unit,
    titulo: String = "Enviar archivo",
    mensaje: String? = null
) {
    val colors = getColorsTheme()
    AlertDialog(
        onDismissRequest = onCancelar,
        icon = {
            Icon(
                imageVector = Icons.Default.CheckCircle,
                contentDescription = "Confirmación",
                tint = colors.colorMixPrimary
            )
        },
        title = { Text(text = titulo) },
        text = {
            Text(mensaje ?: "¿Deseas enviar el archivo \"$nombreArchivo\"?")
        },
        confirmButton = {
            Button(
                onClick = onConfirmar,
                colors = ButtonDefaults.buttonColors(containerColor = IlcbGreenMid)
            ) {
                Text("Aceptar")
            }
        },
        dismissButton = {
            OutlinedButton(onClick = onCancelar) {
                Text("Cancelar")
            }
        }
    )
}
