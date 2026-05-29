package pe.lecordonbleu.universidadestudiante.presentation.screens.tramitedocumentario.customcell

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PictureAsPdf
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Icon
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import pe.lecordonbleu.universidadestudiante.SelectorImagenes
import pe.lecordonbleu.universidadestudiante.data.remote.dto.TReTramiteItem
import pe.lecordonbleu.universidadestudiante.domain.model.RequisitoTramiteC
import pe.lecordonbleu.universidadestudiante.getColorsTheme
import pe.lecordonbleu.universidadestudiante.getPlatformContext
import pe.lecordonbleu.universidadestudiante.util.openUrl
import kotlin.io.encoding.Base64
import kotlin.io.encoding.ExperimentalEncodingApi

@Composable
fun CardTramiteModeloC(
    requisitos: List<Any>,
    onCheckedChange: (List<RequisitoTramiteC>, String?) -> Unit,
    flag_crear: Boolean
) {
    val colors = getColorsTheme()
    val seleccionados = remember { mutableStateListOf<RequisitoTramiteC>() }
    val regexCheckbox = Regex("""value=["'](\d+)["']""")

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .border(1.dp, colors.colorGrisAzulado, RoundedCornerShape(4.dp))
            .padding(8.dp)
    ) {
        requisitos.forEach { raw ->
            val item = raw as? TReTramiteItem ?: return@forEach
            if (flag_crear) {
                val cumpleHtml = item.DATOCUMPLE.lowercase()
                val isCheckedInicial = "checked" in cumpleHtml
                val isDisabled = "disabled" in cumpleHtml
                val tieneArchivo =
                    item.requisito_nombre.contains("FOTOGRAFÍA", ignoreCase = true) ||
                            item.estado.contains("archivo", ignoreCase = true)
                var isChecked by remember(item.requisito) { mutableStateOf(isCheckedInicial) }
                var mostrarPicker by remember(item.requisito) { mutableStateOf(false) }
                var nombreArchivo by remember(item.requisito) { mutableStateOf<String?>(null) }


                if (mostrarPicker) {
                    SelectorImagenes(
                        onImagenSeleccionada = { bytes, nombre, ext ->
                            val b64 = bytes.toBase64String()
                            nombreArchivo = nombre
                            mostrarPicker = false
                            val nuevo = RequisitoTramiteC.Doc(
                                extFile = ext,
                                fileData = "{}",
                                multiple = item.multiple,
                                documento = item.documento,
                                id_tramite_estud = item.id_tramite_estud,
                                nombre = nombre,
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
                                carrera = item.carrera
                            )
                            seleccionados.removeAll { it is RequisitoTramiteC.Doc && it.id_tramite_req_doc == nuevo.id_tramite_req_doc }
                            seleccionados.add(nuevo)
                            onCheckedChange(seleccionados.toList(), b64)
                        },
                        onDismiss = { mostrarPicker = false }
                    )
                }

                if (tieneArchivo) {
                    CardCargarArchivoItem(
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
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 6.dp, horizontal = 5.dp),
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
                                val idAsignatura = regexCheckbox.find(item.DATOCUMPLE)?.groupValues?.get(1)
                                    ?.toIntOrNull() ?: 0
                                val requisitoPeriodo = Regex(""">([^<]+)<""")
                                    .find(item.requisito)
                                    ?.groupValues?.get(1)
                                    ?.trim().orEmpty()
                                val nuevo = RequisitoTramiteC.Main(
                                    id_asignatura = idAsignatura.toString(),
                                    id_tramite_req_doc = item.id_tramite_req_doc,
                                    requisito = requisitoPeriodo,
                                    valor = nuevoValor.toString(),
                                    cumplio = if (nuevoValor) "1" else "0"
                                )
                                //seleccionados.removeAll { it is RequisitoTramiteC.Main && it.id_tramite_req_doc == nuevo.id_tramite_req_doc }
                                seleccionados.removeAll { it is RequisitoTramiteC.Main && it.id_asignatura == nuevo.id_asignatura }
                                if (nuevoValor) seleccionados.add(nuevo)
                                onCheckedChange(seleccionados.toList(), null)
                            },
                            enabled = !isDisabled,
                            modifier = Modifier.weight(0.35f)
                        )
                        Box(
                            modifier = Modifier
                                .padding(start = 4.dp)
                                .size(14.dp)
                                .clip(CircleShape)
                                .background(parseCssColor(item.estado) ?: colors.colorAmbar)
                        )
                    }
                }
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
fun CardCargarArchivoItem(
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
            Box(
                modifier = Modifier
                    .padding(start = 4.dp)
                    .size(14.dp)
                    .clip(CircleShape)
                    .background(colors.colorAmbar)
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

@Composable
fun ItemFotografiaVer(
    nombre: String,
    circleColorHex: String?,
    urlArchivo: String?
) {
    val colors = getColorsTheme()
    val context = getPlatformContext()
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp, horizontal = 5.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = nombre,
            fontSize = 12.sp,
            color = colors.textColor,
            modifier = Modifier.weight(1f),
            maxLines = 2,
            overflow = TextOverflow.Ellipsis
        )
        Box(modifier = Modifier.width(40.dp), contentAlignment = Alignment.Center) {
            Icon(
                imageVector = Icons.Filled.PictureAsPdf,
                contentDescription = "Ver archivo",
                tint = colors.colorRojo,
                modifier = Modifier
                    .size(18.dp)
                    .clickable(enabled = !urlArchivo.isNullOrBlank()) {
                        urlArchivo?.let { openUrl(context, it) }
                    }
            )
        }
        Box(
            modifier = Modifier
                .padding(start = 4.dp)
                .size(14.dp)
                .clip(CircleShape)
                .background(parseCssColor(circleColorHex) ?: colors.colorAmbar)
        )
    }
}

@Composable
fun ItemCheckTramite(
    nombre: String,
    checked: Boolean,
    disabled: Boolean,
    circleColorHex: String?
) {
    val colors = getColorsTheme()
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp, horizontal = 5.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = nombre,
            fontSize = 12.sp,
            color = colors.textColor,
            modifier = Modifier.weight(1f),
            maxLines = 2,
            overflow = TextOverflow.Ellipsis
        )
        Box(modifier = Modifier.width(40.dp), contentAlignment = Alignment.Center) {
            Checkbox(
                checked = checked,
                onCheckedChange = {},
                enabled = !disabled
            )
        }
        Box(
            modifier = Modifier
                .padding(start = 4.dp)
                .size(14.dp)
                .clip(CircleShape)
                .background(parseCssColor(circleColorHex) ?: colors.colorAmbar)
        )
    }
}



@OptIn(ExperimentalEncodingApi::class)
fun ByteArray.toBase64String(): String = Base64.encode(this)
