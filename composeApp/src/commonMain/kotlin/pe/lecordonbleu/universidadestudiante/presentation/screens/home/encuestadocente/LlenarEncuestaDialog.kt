package pe.lecordonbleu.universidadestudiante.presentation.screens.home.encuestadocente

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.AlertDialog
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Card
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.RadioButton
import androidx.compose.material.RadioButtonDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import pe.lecordonbleu.universidadestudiante.core.theme.IlcbBlueMD
import pe.lecordonbleu.universidadestudiante.core.theme.IlcbDialogBg
import pe.lecordonbleu.universidadestudiante.core.theme.IlcbGrisSuave
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import pe.lecordonbleu.universidadestudiante.data.remote.dto.ListEncuestaData

@Composable
fun LlenarEncuestaDialog(
    preguntas: List<ListEncuestaData>,
    tituloAsignatura: String,
    onDismiss: () -> Unit
) {
    if (preguntas.isEmpty()) return

    val encabezado = preguntas.firstOrNull()?.msg_encabezado_enc.orEmpty()
    val preguntasDistintas = preguntas.distinctBy { it.id_exam_preg }
    val radioSeleccionado = remember { mutableStateMapOf<Int, Int>() }
    val textosLibres = remember { mutableStateMapOf<Int, String>() }

    AlertDialog(
        onDismissRequest = {},
        backgroundColor = IlcbDialogBg,
        shape = RoundedCornerShape(16.dp),
        title = {
            Column(modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = tituloAsignatura,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = IlcbBlueMD,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )
                if (encabezado.isNotEmpty()) {
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = encabezado,
                        fontSize = 11.sp,
                        color = Color.DarkGray,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        },
        text = {
            LazyColumn(modifier = Modifier.heightIn(max = 400.dp)) {
                itemsIndexed(preguntasDistintas) { index, pregunta ->
                    val alternativas = preguntas.filter { it.id_exam_preg == pregunta.id_exam_preg }
                    val esOpcionMultiple = alternativas.size > 2

                    Card(
                        backgroundColor = Color.White,
                        shape = RoundedCornerShape(8.dp),
                        elevation = 2.dp,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp)
                    ) {
                        Column(modifier = Modifier.padding(10.dp)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Box(
                                    contentAlignment = Alignment.Center,
                                    modifier = Modifier
                                        .size(24.dp)
                                        .padding(end = 6.dp)
                                ) {
                                    Text(
                                        text = "${index + 1}",
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = IlcbBlueMD
                                    )
                                }
                                Text(
                                    text = pregunta.exam_preg_nombre,
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Medium,
                                    color = Color.DarkGray
                                )
                            }

                            Spacer(modifier = Modifier.height(8.dp))

                            if (esOpcionMultiple) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = "Desacuerdo",
                                        fontSize = 10.sp,
                                        color = Color.Gray,
                                        modifier = Modifier.weight(1f)
                                    )
                                    Column(
                                        horizontalAlignment = Alignment.CenterHorizontally,
                                        modifier = Modifier.selectableGroup()
                                    ) {
                                        Row {
                                            (1..5).forEach { valor ->
                                                RadioButton(
                                                    selected = radioSeleccionado[pregunta.id_exam_preg] == valor,
                                                    onClick = { radioSeleccionado[pregunta.id_exam_preg] = valor },
                                                    colors = RadioButtonDefaults.colors(
                                                        selectedColor = IlcbBlueMD
                                                    ),
                                                    modifier = Modifier.size(28.dp)
                                                )
                                            }
                                        }
                                        Row {
                                            (1..5).forEach { valor ->
                                                Text(
                                                    text = "$valor",
                                                    fontSize = 10.sp,
                                                    color = Color.Gray,
                                                    textAlign = TextAlign.Center,
                                                    modifier = Modifier.width(28.dp)
                                                )
                                            }
                                        }
                                    }
                                    Text(
                                        text = "Acuerdo",
                                        fontSize = 10.sp,
                                        color = Color.Gray,
                                        textAlign = TextAlign.End,
                                        modifier = Modifier.weight(1f)
                                    )
                                }
                            } else {
                                var texto by remember { mutableStateOf(textosLibres[pregunta.id_exam_preg] ?: "") }
                                OutlinedTextField(
                                    value = texto,
                                    onValueChange = {
                                        texto = it
                                        textosLibres[pregunta.id_exam_preg] = it
                                    },
                                    placeholder = { Text("Ingresa tu respuesta", fontSize = 11.sp) },
                                    modifier = Modifier.fillMaxWidth(),
                                    singleLine = false,
                                    maxLines = 3
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
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterHorizontally)
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
                Button(
                    onClick = onDismiss,
                    shape = RoundedCornerShape(30),
                    colors = ButtonDefaults.buttonColors(
                        backgroundColor = IlcbBlueMD,
                        contentColor = Color.White
                    )
                ) {
                    Text("Guardar")
                }
            }
        }
    )
}
