package pe.lecordonbleu.universidadestudiante.presentation.components.dialogs

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import pe.lecordonbleu.universidadestudiante.core.theme.IlcbError

@Composable
fun DialogoEliminarDocumento(
    onConfirmar: () -> Unit,
    onCancelar: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onCancelar,
        icon = {
            Icon(
                imageVector = Icons.Default.Delete,
                contentDescription = "Eliminar documento",
                tint = IlcbError
            )
        },
        title = { Text(text = "Eliminar documento") },
        text = {
            Text("¿Seguro que querés eliminar este documento?")
        },
        confirmButton = {
            Button(
                onClick = onConfirmar,
                colors = ButtonDefaults.buttonColors(containerColor = IlcbError)
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
