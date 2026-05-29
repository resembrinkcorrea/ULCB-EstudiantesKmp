package pe.lecordonbleu.universidadestudiante.presentation.screens.tramitedocumentario.customcell

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import pe.lecordonbleu.universidadestudiante.SelectorDocumentoPDF
import pe.lecordonbleu.universidadestudiante.data.remote.dto.TReTramiteItem
import pe.lecordonbleu.universidadestudiante.domain.model.RequisitoTramiteC
import pe.lecordonbleu.universidadestudiante.getColorsTheme

@Composable
fun CardTramiteModeloH(
    requisitos: List<Any>,
    onArchivoChange: (List<RequisitoTramiteC>, String?) -> Unit,
    flag_crear: Boolean
) {
    val colors = getColorsTheme()
    val seleccionados = remember { mutableStateListOf<RequisitoTramiteC>() }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .border(1.dp, colors.colorGrisAzulado, RoundedCornerShape(4.dp))
            .padding(8.dp)
    ) {
        requisitos.forEach { raw ->
            val item = raw as? TReTramiteItem ?: return@forEach
            if (flag_crear) {
                var mostrarPicker by remember(item.requisito) { mutableStateOf(false) }
                var nombreArchivo by remember(item.requisito) { mutableStateOf<String?>(null) }

                if (mostrarPicker) {
                    SelectorDocumentoPDF(
                        onDocumentoSeleccionado = { bytes, nombreSel ->
                            val b64 = bytes.toBase64String()
                            nombreArchivo = nombreSel
                            mostrarPicker = false
                            val nuevo = RequisitoTramiteC.Doc(
                                extFile = ".pdf",
                                fileData = "{}",
                                multiple = item.multiple,
                                documento = item.documento,
                                id_tramite_estud = item.id_tramite_estud,
                                nombre = nombreSel,
                                id_tramite_estud_req_doc = item.id_tramite_estud_req_doc,
                                periodo_mat = item.periodo_mat,
                                contador = item.contador,
                                fileNameDocTitle = "DOC_TRAMITE",
                                urlDoc = "",
                                id_tramite_req_doc = item.id_tramite_req_doc,
                                id_tramite_estud_req = item.id_tramite_estud_req,
                                cumplio = "1",
                                requisito_nombre = item.requisito_nombre,
                                empresa = item.empresa,
                                carrera = item.carrera,
                                pdfBase64 = b64
                            )
                            seleccionados.removeAll { it is RequisitoTramiteC.Doc && it.id_tramite_req_doc == nuevo.id_tramite_req_doc }
                            seleccionados.add(nuevo)
                            onArchivoChange(seleccionados.toList(), b64)
                        },
                        onDismiss = { mostrarPicker = false }
                    )
                }

                CardCargarArchivoPdfItem(
                    requisitoNombre = item.requisito_nombre,
                    onClickSeleccionarArchivo = { mostrarPicker = true }
                )

                val cargado = nombreArchivo != null
                Text(
                    text = if (cargado) "Archivo cargado: $nombreArchivo" else "Archivo no cargado",
                    fontSize = 12.sp,
                    fontWeight = if (cargado) FontWeight.Bold else FontWeight.Normal,
                    color = if (cargado) colors.colorVerdeMedio else colors.colorGrisNeutro,
                    modifier = Modifier.padding(start = 4.dp, top = 4.dp, bottom = 8.dp)
                )
            } else {
                val nombre = item.requisito
                    .replace(Regex("""<br\s*/?>""", RegexOption.IGNORE_CASE), "\n")
                    .replace(Regex("<[^>]+>"), "")
                    .trim()
                    .ifBlank { item.requisito_nombre }
                val circleColorHex = extractCssBackgroundColor(item.estado)
                val esCheckbox = item.DATOCUMPLE.contains("type=\"checkbox\"", ignoreCase = true)
                if (esCheckbox) {
                    ItemCheckTramite(
                        nombre = nombre,
                        checked = item.DATOCUMPLE.contains("checked", ignoreCase = true),
                        disabled = item.DATOCUMPLE.contains("disabled", ignoreCase = true),
                        circleColorHex = circleColorHex
                    )
                } else {
                    val urlArchivo = Regex("""verArchivoTramite\('([^']+)'\s*,""", RegexOption.IGNORE_CASE)
                        .find(item.DATOCUMPLE)?.groupValues?.getOrNull(1)
                    ItemFotografiaVer(
                        nombre = nombre,
                        circleColorHex = circleColorHex,
                        urlArchivo = urlArchivo
                    )
                }
            }
        }
    }
}

@Composable
fun CardCargarArchivoPdfItem(
    requisitoNombre: String,
    onClickSeleccionarArchivo: () -> Unit = {}
) {
    val colors = getColorsTheme()
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 12.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(end = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = requisitoNombre,
                fontSize = 13.sp,
                fontWeight = FontWeight.SemiBold,
                color = colors.textColor,
                modifier = Modifier.weight(1f)
            )
        }

        Spacer(modifier = Modifier.height(6.dp))

        Button(
            onClick = onClickSeleccionarArchivo,
            modifier = Modifier
                .height(32.dp)
                .defaultMinSize(minWidth = 1.dp),
            contentPadding = PaddingValues(horizontal = 12.dp, vertical = 0.dp)
        ) {
            Text("Seleccionar archivo", fontSize = 12.sp)
        }

        Spacer(modifier = Modifier.height(4.dp))
    }
}
