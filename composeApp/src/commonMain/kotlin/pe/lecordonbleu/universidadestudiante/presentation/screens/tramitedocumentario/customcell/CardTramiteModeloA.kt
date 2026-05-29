package pe.lecordonbleu.universidadestudiante.presentation.screens.tramitedocumentario.customcell

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.serialization.Serializable
import pe.lecordonbleu.universidadestudiante.data.remote.dto.TReTramiteItem
import pe.lecordonbleu.universidadestudiante.getColorsTheme


@Serializable
data class RequisitoSeleccionado(
    val id_asignatura: Int,
    val id_tramite_req_doc: Int,
    val requisito_nombre: String,
    val valorinput: Boolean,
    val cumplio: Int
)

@Composable
fun CardTramiteModeloA(
    requisitos: List<Any>,
    onCheckedChange: (List<RequisitoSeleccionado>) -> Unit,
    flag_crear: Boolean = true
) {
    val colors = getColorsTheme()
    val seleccionados = remember { mutableStateListOf<RequisitoSeleccionado>() }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .border(1.dp, colors.colorGrisAzulado, RoundedCornerShape(4.dp))
            .padding(8.dp)
    ) {
        requisitos.forEach { raw ->
            val item = raw as? TReTramiteItem ?: return@forEach
            val cumpleHtml = item.DATOCUMPLE.lowercase()
            val isCheckedInicial = "checked" in cumpleHtml
            val isDisabled = "disabled" in cumpleHtml
            val idAsignatura = Regex("""value=['"](\d+)['"]""")
                .find(item.DATOCUMPLE)
                ?.groupValues?.get(1)
                ?.toIntOrNull() ?: 0

            var isChecked by remember(item.requisito) { mutableStateOf(isCheckedInicial) }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp, horizontal = 5.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = item.requisito_nombre,
                    fontSize = 12.sp,
                    color = colors.textColor,
                    modifier = Modifier.weight(1f)
                )

                Checkbox(
                    checked = isChecked,
                    onCheckedChange = { nuevoValor ->
                        isChecked = nuevoValor
                        val nuevo = RequisitoSeleccionado(
                            id_asignatura = idAsignatura,
                            id_tramite_req_doc = item.id_tramite_req_doc.toIntOrNull() ?: 0,
                            requisito_nombre = item.requisito_nombre,
                            valorinput = nuevoValor,
                            cumplio = if (nuevoValor) 1 else 0
                        )
                        seleccionados.removeAll { it.requisito_nombre == nuevo.requisito_nombre }
                        if (nuevo.valorinput) seleccionados.add(nuevo)
                        onCheckedChange(seleccionados.toList())
                    },
                    enabled = flag_crear && !isDisabled,
                    modifier = Modifier.weight(0.35f)
                )

                Box(
                    modifier = Modifier
                        .size(14.dp)
                        .clip(CircleShape)
                        .background(colors.colorAmbar)
                )
            }
        }
    }
}
