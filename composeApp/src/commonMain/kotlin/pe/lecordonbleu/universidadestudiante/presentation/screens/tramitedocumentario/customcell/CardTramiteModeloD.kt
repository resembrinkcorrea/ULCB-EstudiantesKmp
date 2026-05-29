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
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.serialization.Serializable
import pe.lecordonbleu.universidadestudiante.data.remote.dto.TReTramiteItem
import pe.lecordonbleu.universidadestudiante.getColorsTheme
import pe.lecordonbleu.universidadestudiante.presentation.components.ComboBoxGenericModel

private fun String?.safeInt(): Int = this?.toIntOrNull() ?: 0

@Serializable
data class RequisitoTramiteD(
    val multiple: Int = 0,
    val documento: Int = 0,
    val id_tramite_estud: Int = 0,
    val id_tramite_estud_req_doc: Int = 0,
    val periodo_mat: Int = 0,
    val id_peracad_select: Int = 0,
    val contador: Int = 0,
    val id_tramite_req_doc: Int = 0,
    val id_tramite_estud_req: Int = 0,
    val cumplio: Int = 0,
    val requisito_nombre: String = "",
    val empresa: Int = 0,
    val carrera: Int = 0
)

@Composable
fun CardTramiteModeloD(
    requisitos: List<Any>,
    flag_crear: Boolean,
    onComboBoxChange: (List<RequisitoTramiteD>) -> Unit
) {
    val colors = getColorsTheme()
    val itemsRaw = requisitos.mapNotNull { it as? TReTramiteItem }
    if (itemsRaw.isEmpty()) return

    val item = itemsRaw.first()
    val opciones = itemsRaw.filter {
        val nombre = it.nombre?.trim().orEmpty()
        nombre.isNotEmpty() && !nombre.equals("SELECCIONE", ignoreCase = true)
    }
    val carreraSeleccionada = remember { mutableStateOf<TReTramiteItem?>(null) }
    val requisitoState = remember(itemsRaw) {
        mutableStateListOf(
            RequisitoTramiteD(
                multiple = item.multiple.safeInt(),
                documento = item.documento.safeInt(),
                id_tramite_estud = item.id_tramite_estud.safeInt(),
                id_tramite_estud_req_doc = item.id_tramite_estud_req_doc.safeInt(),
                periodo_mat = item.periodo_mat.safeInt(),
                id_peracad_select = item.id ?: 0,
                contador = item.contador,
                id_tramite_req_doc = item.id_tramite_req_doc.safeInt(),
                id_tramite_estud_req = item.id_tramite_estud_req.safeInt(),
                cumplio = item.cumplio.safeInt(),
                requisito_nombre = item.requisito_nombre,
                empresa = item.empresa.safeInt(),
                carrera = item.carrera.safeInt()
            )
        )
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .border(1.dp, colors.colorGrisAzulado, RoundedCornerShape(6.dp))
            .padding(8.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(modifier = Modifier.weight(0.9f)) {
                if (!flag_crear) {
                    Column {
                        Text(
                            text = item.requisito_nombre,
                            fontSize = 13.sp,
                            color = colors.colorGrisNeutro
                        )
                        Text(
                            text = stripHtml(item.DATOCUMPLE),
                            fontSize = 14.sp,
                            color = colors.textColor
                        )
                    }
                } else {
                    ComboBoxGenericModel(
                        items = opciones,
                        selectedItem = carreraSeleccionada.value,
                        label = item.requisito_nombre,
                        itemLabel = { it.nombre.orEmpty() },
                        onItemSelected = { sel ->
                            carreraSeleccionada.value = sel
                            requisitoState[0] =
                                requisitoState[0].copy(id_peracad_select = sel.id ?: 0)
                            onComboBoxChange(requisitoState.toList())
                        },
                        enabled = true,
                        backgroundColorComboBox = colors.colorGrisNeutro
                    )
                }
            }

            Box(
                modifier = Modifier
                    .weight(0.1f)
                    .fillMaxHeight(),
                contentAlignment = Alignment.Center
            ) {
                Box(
                    modifier = Modifier
                        .size(14.dp)
                        .clip(CircleShape)
                        .background(if (item.cumplio == "1") colors.colorVerdeMedio else colors.colorAmbar)
                )
            }
        }
    }
}
