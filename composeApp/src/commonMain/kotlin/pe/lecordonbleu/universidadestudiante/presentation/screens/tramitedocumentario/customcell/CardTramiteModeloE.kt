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
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import pe.lecordonbleu.universidadestudiante.data.remote.dto.TReTramiteItem
import pe.lecordonbleu.universidadestudiante.getColorsTheme

@Composable
fun CardTramiteModeloE(
    requisitos: List<Any>,
    onSeleccionChange: (List<RequisitoSeleccionado>) -> Unit = {},
    flag_crear: Boolean = true
) {
    val colors = getColorsTheme()
    var selectedAsignatura by remember { mutableStateOf<Int?>(null) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .border(1.dp, colors.colorGrisAzulado, RoundedCornerShape(4.dp))
            .padding(8.dp)
    ) {
        requisitos.forEach { raw ->
            val item = raw as? TReTramiteItem ?: return@forEach
            val idAsignatura = Regex("""value=['"](\d+)['"]""")
                .find(item.DATOCUMPLE)
                ?.groupValues?.get(1)
                ?.toIntOrNull() ?: 0
            val circleColor = parseCssColor(extractCssBackgroundColor(item.estado)) ?: colors.colorAmbar

            if (flag_crear) {
                val isSelected = selectedAsignatura == idAsignatura
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
                    RadioButton(
                        selected = isSelected,
                        onClick = {
                            selectedAsignatura = idAsignatura
                            onSeleccionChange(
                                listOf(
                                    RequisitoSeleccionado(
                                        id_asignatura = idAsignatura,
                                        id_tramite_req_doc = item.id_tramite_req_doc.toIntOrNull() ?: 0,
                                        requisito_nombre = item.requisito_nombre,
                                        valorinput = true,
                                        cumplio = 1
                                    )
                                )
                            )
                        },
                        enabled = true,
                        modifier = Modifier.weight(0.35f)
                    )
                    Box(
                        modifier = Modifier
                            .size(14.dp)
                            .clip(CircleShape)
                            .background(circleColor)
                    )
                }
            } else {
                val isChecked = "checked" in item.DATOCUMPLE.lowercase()
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
                    RadioButton(
                        selected = isChecked,
                        onClick = null,
                        enabled = false,
                        modifier = Modifier.weight(0.35f)
                    )
                    Box(
                        modifier = Modifier
                            .size(14.dp)
                            .clip(CircleShape)
                            .background(circleColor)
                    )
                }
            }
        }
    }
}
