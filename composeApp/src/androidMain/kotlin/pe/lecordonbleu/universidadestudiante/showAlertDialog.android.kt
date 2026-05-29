package pe.lecordonbleu.universidadestudiante

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Error
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogProperties

@Composable

actual fun showAlertDialog(
    title: String,
    message: String,
    positiveButtonText: String,
    onPositiveAction: () -> Unit,
    icon: (@Composable () -> Unit)?
) {
    var showDialog by remember { mutableStateOf(true) }

    if (showDialog) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            text = {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // Caja centrada con icono y título
                    Box(
                        modifier = Modifier.fillMaxWidth(),
                        contentAlignment = Alignment.Center
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            if (icon != null) {
                                icon()
                                Spacer(modifier = Modifier.width(8.dp))
                            }
                            Text(
                                text = title,
                                style = MaterialTheme.typography.h6,
                                textAlign = TextAlign.Start,
                                modifier = Modifier.widthIn(max = 220.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Text(
                        text = message,
                        style = MaterialTheme.typography.body1,
                        textAlign = TextAlign.Center,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp)
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        onPositiveAction()
                        showDialog = false
                    },
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier.padding(8.dp),
                    colors = ButtonDefaults.buttonColors(
                        backgroundColor = Color.Black,
                        contentColor = Color.White
                    )
                ) {
                    Text(positiveButtonText)
                }
            },
            shape = RoundedCornerShape(16.dp),
            backgroundColor = Color.White,
            modifier = Modifier
                .padding(16.dp)
                .background(color = Color.White, shape = RoundedCornerShape(16.dp)),
            properties = DialogProperties(usePlatformDefaultWidth = false)
        )
    }
}







@Preview(showBackground = true)
@Composable
fun PreviewDialog() {
    showAlertDialog(
        title = "Marcación Exitosa",
        message = "Se registró tu asistencia correctamente.",
        positiveButtonText = "Aceptar",
        onPositiveAction = {},
        icon = {
            Icon(
                imageVector = Icons.Default.CheckCircle,
                contentDescription = null,
                tint = Color(0xFF4CAF50),
                modifier = Modifier
                    .size(48.dp)
                    .padding(top = 8.dp)
            )
        }
    )
}

@Preview(showBackground = true)
@Composable
fun PreviewDialogError() {
    showAlertDialog(
        title = "Error de Marcación",
        message = "No se pudo registrar tu asistencia.",
        positiveButtonText = "Cerrar",
        onPositiveAction = {},
        icon = {
            Icon(
                imageVector = Icons.Default.Error,
                contentDescription = null,
                tint = Color.Red,
                modifier = Modifier
                    .size(48.dp)
                    .padding(top = 8.dp)
            )
        }
    )
}