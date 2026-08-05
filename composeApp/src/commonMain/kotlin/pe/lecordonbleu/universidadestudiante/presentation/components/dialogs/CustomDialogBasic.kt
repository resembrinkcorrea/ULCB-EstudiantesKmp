package pe.lecordonbleu.universidadestudiante.presentation.components.dialogs

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.ui.window.DialogProperties
import ulcbintranetkmp.composeapp.generated.resources.Res
import org.jetbrains.compose.resources.painterResource
import pe.lecordonbleu.universidadestudiante.getPlatformContext
import pe.lecordonbleu.universidadestudiante.core.theme.IlcbBlueDeep
import pe.lecordonbleu.universidadestudiante.core.theme.IlcbError
import pe.lecordonbleu.universidadestudiante.core.theme.IlcbGreenMid
import pe.lecordonbleu.universidadestudiante.core.theme.IlcbWarning
import ulcbintranetkmp.composeapp.generated.resources.ulcb_logo

@Composable
fun CustomDialogBasic(
    visible: Boolean,
    titulo: String,
    mensaje: String,
    flag_val: Int = 0,
    aceptarSelected: Int = 0,
    onDismiss: () -> Unit = {},
    confirmado: Boolean,
    dismissOnOutsideClick: Boolean = true,
    onAceptarExtra: (() -> Unit)? = null,
    onDismissGoScreen: (() -> Unit)? = null,

) {
    val context = getPlatformContext()
    val (icon, colorContent) = when {
        flag_val == 1 && confirmado -> Icons.Default.CheckCircle to IlcbGreenMid
        flag_val == 1 && !confirmado -> Icons.Default.Warning to IlcbWarning
        flag_val == 0 -> Icons.Default.Cancel to IlcbError
        else -> Icons.Default.Info to Color.Gray
    }

    if (visible) {
        Dialog(onDismissRequest = { onDismiss() }, properties = DialogProperties(dismissOnClickOutside = dismissOnOutsideClick)) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White)
            ) {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(IlcbBlueDeep)
                            .padding(vertical = 12.dp)
                    ) {
                        Text(
                            text = titulo,
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White,
                            modifier = Modifier.align(Alignment.Center),
                            textAlign = TextAlign.Center
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Image(
                        painter = painterResource(Res.drawable.ulcb_logo),
                        contentDescription = "Logo LCB",
                        modifier = Modifier
                            .height(60.dp)
                            .padding(vertical = 8.dp)
                    )

                    Text(
                        "Estimado Estudiante",
                        fontWeight = FontWeight.Bold,
                        color = IlcbBlueDeep,
                        modifier = Modifier.padding(bottom = 4.dp)
                    )

                    Row(
                        modifier = Modifier
                            .padding(horizontal = 16.dp)
                            .fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = icon,
                            contentDescription = null,
                            tint = colorContent,
                            modifier = Modifier
                                .size(24.dp)
                                .padding(end = 6.dp)
                        )
                        Text(
                            text = mensaje,
                            color = colorContent,
                            fontWeight = FontWeight.Medium,
                            textAlign = TextAlign.Center
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Button(
                        onClick = {
                            when (aceptarSelected) {
                                1 -> {}
                                3 -> {}
                                4 -> {
                                    onAceptarExtra?.invoke()
                                }
                                5 -> {
                                    onDismissGoScreen?.invoke()
                                }
                            }
                            onDismiss()
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = IlcbBlueDeep),
                        shape = RoundedCornerShape(20.dp),
                        modifier = Modifier
                            .padding(bottom = 16.dp)
                            .width(120.dp)
                            .height(40.dp)
                    ) {
                        Text("ACEPTAR", color = Color.White)
                    }
                }
            }
        }
    }
}
