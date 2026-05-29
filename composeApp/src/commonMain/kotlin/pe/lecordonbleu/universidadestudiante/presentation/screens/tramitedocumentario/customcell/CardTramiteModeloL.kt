package pe.lecordonbleu.universidadestudiante.presentation.screens.tramitedocumentario.customcell

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import pe.lecordonbleu.universidadestudiante.SelectorDocumentoPDF
import pe.lecordonbleu.universidadestudiante.data.remote.dto.TReTramiteItem
import pe.lecordonbleu.universidadestudiante.domain.model.RequisitoTramiteC
import pe.lecordonbleu.universidadestudiante.getColorsTheme
import pe.lecordonbleu.universidadestudiante.getPlatformContext
import pe.lecordonbleu.universidadestudiante.util.openUrl

@Composable
fun CardTramiteModeloL(
    requisitos: List<Any>,
    onArchivoChange: (List<RequisitoTramiteC>, String?) -> Unit,
    flag_crear: Boolean = true
) {
    val colors = getColorsTheme()
    val context = getPlatformContext()
    val seleccionados = remember { mutableStateListOf<RequisitoTramiteC>() }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .border(1.dp, colors.colorGrisAzulado, RoundedCornerShape(6.dp))
            .padding(8.dp)
    ) {
        requisitos.forEach { raw ->
            val item = raw as? TReTramiteItem ?: return@forEach
            val esListado = item.cumplio == "1"
            val urlPdf = if (esListado) extraerUrlDesdeHtml(item.DATOCUMPLE) else null
            var mostrarPicker by remember(item.id_tramite_req_doc) { mutableStateOf(false) }
            var nombreArchivo by remember(item.id_tramite_req_doc) { mutableStateOf<String?>(null) }

            if (mostrarPicker) {
                SelectorDocumentoPDF(
                    onDocumentoSeleccionado = { bytes, nombreSel ->
                        val b64 = bytes.toBase64String()
                        println("📎 L archivo recibido: req=${item.id_tramite_req_doc} nombre=$nombreSel len=${bytes.size} b64_end=${b64.takeLast(20)}")
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

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 6.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = item.requisito_nombre,
                    fontSize = 13.sp,
                    color = colors.textColor,
                    modifier = Modifier.weight(1f),
                    maxLines = 2
                )
                Spacer(modifier = Modifier.width(8.dp))

                if (esListado && !urlPdf.isNullOrBlank()) {
                    Button(
                        onClick = { openUrl(context, urlPdf) },
                        modifier = Modifier.height(32.dp),
                        contentPadding = PaddingValues(horizontal = 12.dp)
                    ) { Text("Ver PDF", fontSize = 12.sp) }
                } else if (flag_crear) {
                    Button(
                        onClick = { mostrarPicker = true },
                        modifier = Modifier.height(32.dp),
                        contentPadding = PaddingValues(horizontal = 12.dp)
                    ) { Text("Seleccionar archivo", fontSize = 12.sp) }
                }

                Spacer(modifier = Modifier.width(8.dp))
                Box(
                    modifier = Modifier
                        .size(12.dp)
                        .clip(CircleShape)
                        .background(if (esListado || nombreArchivo != null) colors.colorVerdeMedio else colors.colorAmbar)
                )
            }

            Text(
                text = when {
                    esListado -> "Archivo presentado"
                    nombreArchivo != null -> "Archivo cargado: $nombreArchivo"
                    else -> "Archivo no cargado"
                },
                fontSize = 11.sp,
                color = if (esListado || nombreArchivo != null) colors.colorVerdeMedio else colors.colorGrisNeutro,
                modifier = Modifier.padding(start = 4.dp, bottom = 8.dp)
            )
        }
    }
}
