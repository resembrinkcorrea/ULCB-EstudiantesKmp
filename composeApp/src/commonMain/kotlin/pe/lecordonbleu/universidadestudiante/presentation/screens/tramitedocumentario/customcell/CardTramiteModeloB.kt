package pe.lecordonbleu.universidadestudiante.presentation.screens.tramitedocumentario.customcell

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.toMutableStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import kotlinx.serialization.Serializable
import pe.lecordonbleu.universidadestudiante.data.remote.dto.TReTramiteItem
import pe.lecordonbleu.universidadestudiante.getColorsTheme
import pe.lecordonbleu.universidadestudiante.presentation.components.MyTextFieldComponent


@Serializable
data class RequisitoTramiteB(
    val multiple: Int,
    val documento: Int,
    val id_tramite_estud: Int,
    val id_tramite_estud_req_doc: Int,
    val periodo_mat: Int,
    val contador: Int,
    val id_tramite_req_doc: Int,
    val valorinput: String,
    val id_tramite_estud_req: Int,
    val cumplio: Int,
    val requisito_nombre: String,
    val empresa: Int,
    val carrera: Int
)

@Composable
fun CardTramiteModeloB(
    requisitos: List<Any>,
    onCheckedChange: (List<RequisitoTramiteB>) -> Unit,
    flag_crear: Boolean = true
) {
    // Inicializar lista completa desde el inicio
    val colors = getColorsTheme()
    val requisitosState = remember {
        requisitos.mapIndexed { index, item ->
            if (item is TReTramiteItem) {
                val valorInicial = extraerValorDesdeInput(item.DATOCUMPLE)
                RequisitoTramiteB(
                    multiple = 0,
                    documento = 0,
                    id_tramite_estud = 0,
                    id_tramite_estud_req_doc = 0,
                    periodo_mat = 0,
                    contador = index + 1,
                    id_tramite_req_doc = 0,
                    valorinput = valorInicial,
                    id_tramite_estud_req = 0,
                    cumplio = 1,
                    requisito_nombre = item.requisito_nombre,
                    empresa = 1,
                    carrera = 0
                )
            } else {
                null
            }
        }.filterNotNull().toMutableStateList()
    }

    // Notificar la lista inicial
    LaunchedEffect(Unit) {
        onCheckedChange(requisitosState)
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .border(1.dp, Color.LightGray, RoundedCornerShape(4.dp))
            .padding(3.dp)
    ) {
        requisitos.forEachIndexed { index, item ->
            if (item is TReTramiteItem) {
                val deshabilitado = estaDeshabilitado(item.DATOCUMPLE)
                var textInput by remember { mutableStateOf(requisitosState[index].valorinput) }

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 6.dp, horizontal = 5.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .weight(0.94f)
                            .padding(end = 4.dp)
                    ) {
                        MyTextFieldComponent(
                            labelValue = item.requisito_nombre,
                            painterResource = Icons.Default.Edit,
                            onTextChanged = { nuevoValor ->
                                textInput = nuevoValor
                                requisitosState[index] =
                                    requisitosState[index].copy(valorinput = nuevoValor)
                                onCheckedChange(requisitosState)
                            },
                            initialValue = textInput,
                            enabled = flag_crear && !deshabilitado
                        )
                    }

                    Box(
                        modifier = Modifier
                            .weight(0.15f)
                            .fillMaxHeight(),
                        contentAlignment = Alignment.Center
                    ) {
                        Box(
                            modifier = Modifier
                                .size(18.dp)
                                .clip(CircleShape)
                                .background(Color(0xFFFEC10C))
                        )
                    }
                }
            } else {
                Text("Tipo de item no soportado", color = Color.Red)
            }
        }
    }
}


fun extraerValorDesdeInput(html: String): String {
    val regex = Regex("value\\s*=\\s*\"([^\"]*)\"")
    return regex.find(html)?.groupValues?.get(1)?.trim() ?: ""
}

fun estaDeshabilitado(html: String): Boolean {
    return html.contains("disabled", ignoreCase = true)
}






