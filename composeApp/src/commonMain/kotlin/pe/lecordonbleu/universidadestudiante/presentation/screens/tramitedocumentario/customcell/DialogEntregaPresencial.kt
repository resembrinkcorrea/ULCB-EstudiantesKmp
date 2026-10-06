package pe.lecordonbleu.universidadestudiante.presentation.screens.tramitedocumentario.customcell

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Checkbox
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import pe.lecordonbleu.universidadestudiante.DarkModeColors

data class DatosRecojo(
    val dni: String = "",
    val nombres: String = "",
    val recoger: Int = -1
)

@Composable
fun DialogEntregaPresencial(
    visible: Boolean,
    onDismiss: () -> Unit,
    onConfirm: (Int, String, String) -> Unit,
    colors: DarkModeColors,
    preset: Boolean = false,
    initialRecojo: Boolean = false,
    initialDni: String = "",
    initialNombres: String = ""
) {
    val recoger = remember { mutableStateOf(initialRecojo) }
    val dni = remember { mutableStateOf(initialDni) }
    val nombres = remember { mutableStateOf(initialNombres) }
    val errorMessage = remember { mutableStateOf("") }

    val enabled = !recoger.value && !preset
    val backgroundColor = if (enabled) Color.Gray else Color.LightGray
    val textColor = if (enabled) Color.White else Color.DarkGray

    if (visible) {
        AlertDialog(
            onDismissRequest = onDismiss,
            confirmButton = {
                TextButton(onClick = {
                    if (!recoger.value) {
                        when {
                            dni.value.isBlank() -> errorMessage.value = "Debe ingresar el DNI."
                            !dni.value.all { it.isDigit() } -> errorMessage.value = "El DNI debe contener solo numeros."
                            nombres.value.isBlank() -> errorMessage.value = "Debe ingresar nombres y apellidos."
                            else -> {
                                errorMessage.value = ""
                                onConfirm(0, dni.value, nombres.value)
                            }
                        }
                    } else {
                        errorMessage.value = ""
                        onConfirm(1, "", "")
                    }
                }) { Text("Aceptar", color = colors.colorMixPrimary) }
            },
            dismissButton = {
                TextButton(onClick = onDismiss) { Text("Cancelar", color = colors.colorMixPrimary) }
            },
            title = { Text("Entrega presencial") },
            text = {
                Column {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Checkbox(
                            checked = recoger.value,
                            onCheckedChange = {
                                if (!preset) {
                                    recoger.value = it
                                    if (it) { dni.value = ""; nombres.value = "" }
                                }
                            },
                            enabled = !preset
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Recogere el tramite.")
                    }

                    Spacer(modifier = Modifier.height(8.dp))
                    Text("DNI", style = MaterialTheme.typography.bodyMedium)
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(6.dp))
                            .background(backgroundColor)
                            .padding(horizontal = 4.dp, vertical = 2.dp)
                    ) {
                        OutlinedTextField(
                            value = dni.value,
                            onValueChange = { dni.value = it },
                            placeholder = { Text("Dni", color = textColor) },
                            enabled = enabled,
                            textStyle = LocalTextStyle.current.copy(color = textColor),
                            modifier = Modifier.fillMaxWidth(),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = Color.Transparent,
                                unfocusedBorderColor = Color.Transparent,
                                disabledTextColor = textColor,
                                disabledBorderColor = Color.Transparent
                            )
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))
                    Text("Nombres y Apellidos", style = MaterialTheme.typography.bodyMedium)
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(6.dp))
                            .background(backgroundColor)
                            .padding(horizontal = 4.dp, vertical = 2.dp)
                    ) {
                        OutlinedTextField(
                            value = nombres.value,
                            onValueChange = { nombres.value = it },
                            placeholder = { Text("Nombres y Apellidos", color = textColor) },
                            enabled = enabled,
                            textStyle = LocalTextStyle.current.copy(color = textColor),
                            modifier = Modifier.fillMaxWidth(),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = Color.Transparent,
                                unfocusedBorderColor = Color.Transparent,
                                disabledTextColor = textColor,
                                disabledBorderColor = Color.Transparent
                            )
                        )
                    }

                    if (errorMessage.value.isNotEmpty()) {
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = errorMessage.value,
                            color = Color.Red,
                            style = MaterialTheme.typography.bodySmall,
                            modifier = Modifier.padding(horizontal = 4.dp)
                        )
                    }
                }
            }
        )
    }
}
