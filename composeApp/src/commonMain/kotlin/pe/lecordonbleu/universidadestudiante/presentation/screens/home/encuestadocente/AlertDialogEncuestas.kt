package pe.lecordonbleu.universidadestudiante.presentation.screens.home.encuestadocente

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.AlertDialog
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Edit
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import pe.lecordonbleu.universidadestudiante.core.theme.IlcbBlueMD
import pe.lecordonbleu.universidadestudiante.core.theme.IlcbDialogBg
import pe.lecordonbleu.universidadestudiante.core.theme.IlcbGreenMid
import pe.lecordonbleu.universidadestudiante.core.theme.IlcbGrisSuave
import pe.lecordonbleu.universidadestudiante.core.theme.IlcbNaranjaRojo
import pe.lecordonbleu.universidadestudiante.core.theme.IlcbPastelNaranjaMedio
import pe.lecordonbleu.universidadestudiante.core.theme.IlcbPastelVerdeMedio
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import pe.lecordonbleu.universidadestudiante.data.remote.dto.ListAsignaturaEncuesta

@Composable
fun AlertDialogEncuestas(
    asignaturas: List<ListAsignaturaEncuesta>,
    onAsignaturaClick: (ListAsignaturaEncuesta) -> Unit,
    onDismiss: () -> Unit
) {
    if (asignaturas.isEmpty()) return

    AlertDialog(
        onDismissRequest = {},
        backgroundColor = IlcbDialogBg,
        shape = RoundedCornerShape(20.dp),
        title = {
            Text(
                text = "Encuesta Docente",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = IlcbBlueMD,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
        },
        text = {
            LazyColumn(modifier = Modifier.heightIn(max = 300.dp)) {
                items(asignaturas) { asignatura ->
                    val registrado = asignatura.flag_registrado == 1
                    val cardColor = if (registrado) IlcbPastelVerdeMedio else IlcbPastelNaranjaMedio
                    val iconTint = if (registrado) IlcbGreenMid else IlcbNaranjaRojo

                    Card(
                        backgroundColor = cardColor,
                        shape = RoundedCornerShape(8.dp),
                        elevation = 2.dp,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp)
                            .clickable {
                                if (!registrado) {
                                    onAsignaturaClick(asignatura)
                                }
                            }
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 10.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = if (registrado) Icons.Default.Check else Icons.Default.Edit,
                                contentDescription = null,
                                tint = iconTint,
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(modifier = Modifier.width(10.dp))
                            Column {
                                Text(
                                    text = asignatura.pest_asign_nombre,
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.DarkGray
                                )
                                Text(
                                    text = asignatura.docente,
                                    fontSize = 11.sp,
                                    color = Color.Gray
                                )
                            }
                        }
                    }
                }
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
                        backgroundColor = IlcbGrisSuave,
                        contentColor = Color.White
                    )
                ) {
                    Text("Cancelar")
                }
            }
        }
    )
}
