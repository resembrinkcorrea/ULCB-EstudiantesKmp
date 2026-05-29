package pe.lecordonbleu.universidadestudiante.presentation.screens.tramitedocumentario.customcell

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BorderColor
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.PictureAsPdf
import androidx.compose.material.icons.filled.PlaylistAdd
import androidx.compose.material.icons.filled.Save
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import pe.lecordonbleu.universidadestudiante.data.remote.dto.TReTramiteItem
import pe.lecordonbleu.universidadestudiante.data.remote.dto.TramiteDocumentos
import pe.lecordonbleu.universidadestudiante.data.remote.dto.TrcTramiteItem
import pe.lecordonbleu.universidadestudiante.domain.model.RequisitoTramiteC
import pe.lecordonbleu.universidadestudiante.getColorsTheme
import pe.lecordonbleu.universidadestudiante.getPlatformContext
import pe.lecordonbleu.universidadestudiante.presentation.screens.tramitedocumentario.TipoFormularioTramite
import pe.lecordonbleu.universidadestudiante.util.openUrl

@Composable
fun DocumentoItemCard(
    tramite: TramiteDocumentos,
    onComprobante: (String) -> Unit,
    onClickItem: (TramiteDocumentos) -> Unit,
    isExpanded: Boolean = false,
    requisitos: List<TReTramiteItem> = emptyList(),
    trcList: List<TrcTramiteItem> = emptyList(),
    onCorregirClick: () -> Unit,
    saveClickCorregir: (String) -> Unit
) {
    val colors = getColorsTheme()
    val context = getPlatformContext()
    val acciones = DocumentoAccion.obtenerAcciones(removeHtmlComments(tramite.ACCIONES))
    val mostrarDialogEntregaPresencial = remember { mutableStateOf(false) }
    var mostrarCorreccion by remember(tramite.id_tramite_estud) { mutableStateOf(false) }
    var datosRecojo by remember { mutableStateOf(DatosRecojo()) }
    val scope = rememberCoroutineScope()

    if (mostrarDialogEntregaPresencial.value) {
        DialogEntregaPresencial(
            visible = true,
            onDismiss = { mostrarDialogEntregaPresencial.value = false },
            onConfirm = { recoger, dni, nombres ->
                datosRecojo = DatosRecojo(dni = dni, nombres = nombres, recoger = recoger)
                mostrarDialogEntregaPresencial.value = false
            },
            preset = true,
            initialRecojo = tramite.flag_recojo == "1",
            initialDni = tramite.dni_presencial,
            initialNombres = tramite.nombre_presencial
        )
    }

    val estadoColor = parseCssColor(extractCssBackgroundColor(tramite.ESTADO))
        ?: colorEstadoTramite(tramite.estado_tramite)
    val estadoTitulo = extraerTituloEstado(tramite.ESTADO).ifBlank { tramite.estado_tramite }
    val pagoColor = parseCssColor(extractCssBackgroundColor(tramite.PAGO))
        ?: if (tramite.estado_pago.trim().equals("PAGADO", ignoreCase = true)) colors.colorVerdeMedio else colors.colorGrisNeutro

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp, horizontal = 4.dp),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.elevatedCardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(
            containerColor = colors.colorBlancoGris
        )
    ) {
        Column {
            // === HEADER: N° badge + Estado chip ===
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(colors.colorAzulContraste.copy(alpha = 0.07f))
                    .padding(horizontal = 12.dp, vertical = 10.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Surface(
                    shape = RoundedCornerShape(6.dp),
                    color = colors.colorAzulContraste.copy(alpha = 0.15f)
                ) {
                    Text(
                        "N° ${tramite.N}",
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        color = colors.colorAzulContraste
                    )
                }

                Surface(
                    shape = RoundedCornerShape(20.dp),
                    color = estadoColor.copy(alpha = 0.15f)
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(5.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(7.dp)
                                .background(estadoColor, CircleShape)
                        )
                        Text(
                            estadoTitulo,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = estadoColor
                        )
                    }
                }
            }

            // === ACCIONES (if any) ===
            if (acciones.isNotEmpty()) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 12.dp, vertical = 8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        "Acciones",
                        fontSize = 12.sp,
                        color = colors.colorGrisNeutro,
                        fontWeight = FontWeight.Medium
                    )
                    Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                        acciones.forEach { accion ->
                            AccionBox(
                                accion = accion,
                                onClick = {
                                    when (accion.tipo) {
                                        DocumentoAccion.ENTREGA_PRESENCIAL -> mostrarDialogEntregaPresencial.value = true
                                        DocumentoAccion.CORREGIR -> {
                                            onCorregirClick()
                                            mostrarCorreccion = !mostrarCorreccion
                                        }
                                        DocumentoAccion.PROCESAR -> Unit
                                        DocumentoAccion.REQUISITOS -> Unit
                                    }
                                }
                            )
                        }
                    }
                }
                HorizontalDivider(color = colors.colorGrisNeutro.copy(alpha = 0.12f))
            }

            // === BODY ===
            Column(modifier = Modifier.padding(start = 12.dp, end = 12.dp, top = 8.dp, bottom = 6.dp)) {

                Text(stripHtml(tramite.TRAMITE_), fontSize = 14.sp, fontWeight = FontWeight.Bold, color = colors.textColor)
                Spacer(Modifier.height(6.dp))
                Text(stripHtml(tramite.TIPO_TRAMITE), fontSize = 13.sp, fontWeight = FontWeight.Bold, color = colors.textColor)
                Spacer(Modifier.height(4.dp))
                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    Column(Modifier.weight(1f)) { FieldBlock("Motivo", stripHtml(tramite.MOTIVO)) }
                    Column(Modifier.weight(1f)) { FieldBlock("Entrega", tramite.TIPO_ENTREGA) }
                    Column(Modifier.weight(1f)) { FieldBlock("Modalidad", stripHtml(tramite.MODALIDAD)) }
                }

                Spacer(Modifier.height(6.dp))
                HorizontalDivider(color = colors.colorGrisNeutro.copy(alpha = 0.10f))
                Spacer(Modifier.height(6.dp))

                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp), verticalAlignment = Alignment.Top) {
                    Column(Modifier.weight(1f)) { FieldBlock("Fecha solicitante", tramite.FECHA_SOLICITANTE) }
                    Column(Modifier.weight(1f)) {
                        val tieneReq = tramite.flag_requisitos == "1"
                        Text("Requisitos", fontSize = 11.sp, color = colors.colorGrisNeutro, fontWeight = FontWeight.Medium)
                        Spacer(Modifier.height(2.dp))
                        Surface(
                            shape = RoundedCornerShape(4.dp),
                            color = if (tieneReq) colors.colorVerdeMedio.copy(alpha = 0.15f)
                                    else colors.colorGrisNeutro.copy(alpha = 0.10f)
                        ) {
                            Text(
                                if (tieneReq) "SI" else "NO",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = if (tieneReq) colors.colorVerdeMedio else colors.colorGrisNeutro,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
                            )
                        }
                    }
                    Column(Modifier.weight(1f)) { FieldBlock("Monto", "S/ ${stripHtml(tramite.MONTO)}") }
                }

                Spacer(Modifier.height(6.dp))

                // Pago
                Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        "Pago",
                        fontSize = 11.sp,
                        color = colors.colorGrisNeutro,
                        fontWeight = FontWeight.Medium,
                        modifier = Modifier.weight(1f)
                    )
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(5.dp)
                    ) {
                        Box(Modifier.size(10.dp).background(pagoColor, CircleShape))
                        Text(tramite.estado_pago, fontSize = 12.sp, color = pagoColor, fontWeight = FontWeight.Medium)
                    }
                }

                Spacer(Modifier.height(4.dp))

                // Comprobante
                Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        "Comprobante",
                        fontSize = 11.sp,
                        color = colors.colorGrisNeutro,
                        fontWeight = FontWeight.Medium,
                        modifier = Modifier.weight(1f)
                    )
                    if (tieneComprobante(tramite.COMPROBANTE)) {
                        IconButton(
                            onClick = {
                                extraerComprobanteDesdeHtml(tramite.COMPROBANTE)
                                    ?.takeIf { it.isNotBlank() }
                                    ?.let(onComprobante)
                            },
                            modifier = Modifier.size(28.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.PictureAsPdf,
                                contentDescription = "Ver comprobante PDF",
                                tint = colors.colorRojo,
                                modifier = Modifier.size(22.dp)
                            )
                        }
                    } else {
                        Text(
                            textoComprobanteSinPDF(tramite.COMPROBANTE),
                            fontSize = 12.sp,
                            color = colors.textColor
                        )
                    }
                }

                Spacer(Modifier.height(6.dp))
                HorizontalDivider(color = colors.colorGrisNeutro.copy(alpha = 0.10f))
                Spacer(Modifier.height(6.dp))

                // Respuesta
                FieldBlock("Respuesta", stripHtml(tramite.RESPUESTA))

                Spacer(Modifier.height(4.dp))

                // Archivo respuesta
                Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        "Archivo respuesta",
                        fontSize = 11.sp,
                        color = colors.colorGrisNeutro,
                        fontWeight = FontWeight.Medium,
                        modifier = Modifier.weight(1f)
                    )
                    val archivoUrl = extraerUrlDesdeHtml(tramite.ARCHIVO_RESPUESTA)
                    if (!archivoUrl.isNullOrBlank()) {
                        IconButton(
                            onClick = { openUrl(context, archivoUrl) },
                            modifier = Modifier.size(28.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.PictureAsPdf,
                                contentDescription = "Ver archivo PDF",
                                tint = colors.colorRojo,
                                modifier = Modifier.size(22.dp)
                            )
                        }
                    } else {
                        Text(
                            stripHtml(tramite.ARCHIVO_RESPUESTA),
                            fontSize = 12.sp,
                            color = colors.textColor
                        )
                    }
                }
            }

            // === "Ver más detalles" BUTTON — only when flag_requisitos == "1" ===
            if (tramite.flag_requisitos == "1") {
                HorizontalDivider(color = colors.colorGrisNeutro.copy(alpha = 0.15f))
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { onClickItem(tramite) }
                        .padding(horizontal = 12.dp, vertical = 10.dp),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = if (isExpanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                        contentDescription = null,
                        tint = colors.colorAzulContraste,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(Modifier.width(4.dp))
                    Text(
                        if (isExpanded) "Ver menos detalles" else "Ver más detalles",
                        fontSize = 13.sp,
                        color = colors.colorAzulContraste,
                        fontWeight = FontWeight.Medium
                    )
                }
            }
        }
    }

    if (isExpanded && requisitos.isNotEmpty()) {
        Box(modifier = Modifier.padding(horizontal = 12.dp)) {
            MostrarFormularioPorTipo(
                tipoFormulario = TipoFormularioTramite.desdeTipoTramite(tramite.id_tramite.toIntOrNull()),
                requisitosList = requisitos,
                onCheckedListUpdatedA = {},
                onCheckedListUpdatedB = {},
                onCheckedListUpdatedC = { _, _ -> },
                flag_crear = false,
                onCheckedListUpdatedD = {},
                onCheckedListUpdatedH = { _, _ -> },
                onCheckedListUpdatedL = { _, _ -> }
            )
        }
    }

    if (mostrarCorreccion && trcList.isNotEmpty()) {
        Spacer(modifier = Modifier.height(8.dp))
        CardTramiteCorreccion(
            trcList = trcList,
            saveClickCorregir = { texto ->
                saveClickCorregir(texto)
                scope.launch {
                    delay(5000)
                    mostrarCorreccion = false
                }
            }
        )
    }
}

@Composable
private fun FieldBlock(label: String, value: String) {
    val colors = getColorsTheme()
    Text(label, fontSize = 11.sp, color = colors.colorGrisNeutro, fontWeight = FontWeight.Medium)
    Text(value, fontSize = 13.sp, color = colors.textColor)
}

@Composable
private fun AccionBox(accion: DocumentoAccionVisual, onClick: () -> Unit) {
    val colors = getColorsTheme()
    Box(
        modifier = Modifier
            .size(36.dp)
            .background(
                if (accion.habilitado) accion.tipo.backgroundColor()
                else colors.colorGrisAzulado.copy(alpha = 0.4f),
                shape = RoundedCornerShape(6.dp)
            )
            .border(
                1.dp,
                colors.colorGrisAzulado.copy(alpha = 0.3f),
                shape = RoundedCornerShape(6.dp)
            )
    ) {
        IconButton(
            onClick = onClick,
            enabled = accion.habilitado,
            modifier = Modifier
                .fillMaxSize()
                .padding(6.dp)
        ) {
            Icon(
                imageVector = accion.icono,
                contentDescription = accion.descripcion,
                tint = Color.White,
                modifier = Modifier.size(18.dp)
            )
        }
    }
}

fun tieneComprobante(html: String?): Boolean =
    html?.contains("obtenerDetailCtaComprobante(", ignoreCase = true) == true

fun textoComprobanteSinPDF(html: String?): String =
    stripHtml(html ?: "").ifBlank { "Sin comprobante" }

fun removeHtmlComments(input: String): String = input.replace(Regex("<!--[\\s\\S]*?-->"), "")

fun extraerComprobanteDesdeHtml(html: String): String? =
    Regex("obtenerDetailCtaComprobante\\('([^']+)'").find(html)?.groupValues?.get(1)

@Composable
fun colorEstadoTramite(nombre: String): Color {
    val colors = getColorsTheme()
    return when (nombre.uppercase()) {
        "EN TRAMITE" -> colors.colorCian
        "ENTREGADO" -> colors.colorVerdeMedio
        "OBSERVADO" -> colors.colorRojo
        "EN CORRECCION" -> colors.colorNaranjaAmbar
        "PENDIENTE" -> colors.colorAmbar
        else -> colors.colorGrisNeutro
    }
}

fun stripHtml(html: String?): String {
    return html
        ?.replace(Regex("<[^>]*>"), "")
        ?.replace("*", "")
        ?.replace("\r", "")
        ?.replace("\n", "")
        ?.trim()
        ?: ""
}

fun extraerUrlDesdeHtml(html: String): String? =
    Regex("verArchivoTramite\\('([^']+)'").find(html)?.groupValues?.get(1)

fun extractCssBackgroundColor(html: String?): String? {
    return html?.let {
        Regex("""background-color:\s*([^;\"'>\s]+)""", RegexOption.IGNORE_CASE)
            .find(it)
            ?.groupValues?.getOrNull(1)
            ?.trim()
    }
}

fun parseCssColor(value: String?): Color? {
    val raw = value?.trim()?.removePrefix("#") ?: return null
    val hex = when (raw.length) {
        3 -> raw.map { "$it$it" }.joinToString("")
        6 -> raw
        8 -> raw
        else -> return null
    }
    return hex.toLongOrNull(16)?.let { parsed ->
        val argb = if (hex.length == 6) (0xFF000000 or parsed).toInt() else parsed.toInt()
        Color(argb)
    }
}

fun extraerTituloEstado(html: String): String {
    val regex = Regex("title=\"(.*?)\"", RegexOption.IGNORE_CASE)
    return regex.find(html)?.groupValues?.get(1)?.trim() ?: ""
}

enum class DocumentoAccion(
    val idHtml: String,
    val icono: ImageVector,
    val descripcion: String
) {
    ENTREGA_PRESENCIAL("entrega-presencial[]", Icons.Default.Person, "Entrega presencial"),
    PROCESAR("btn-procesar[]", Icons.Default.Save, "Solicitar"),
    CORREGIR("btn-correcion[]", Icons.Default.BorderColor, "Corregir"),
    REQUISITOS("completarRequisitos[]", Icons.Default.PlaylistAdd, "Completar requisitos");

    @Composable
    fun backgroundColor(): Color {
        val colors = getColorsTheme()
        return when (this) {
            ENTREGA_PRESENCIAL -> colors.colorVioletaIntenso
            PROCESAR -> colors.colorNaranjaBrillante
            CORREGIR -> colors.colorRojo
            REQUISITOS -> colors.colorVerdeMedio
        }
    }

    companion object {
        fun obtenerAcciones(html: String): List<DocumentoAccionVisual> {
            return values().mapNotNull { accion ->
                val idIndex = html.indexOf("id=\"${accion.idHtml}\"", ignoreCase = true)
                if (idIndex == -1) return@mapNotNull null
                val subHtml =
                    html.substring(maxOf(0, idIndex - 100), minOf(html.length, idIndex + 300))
                val visible = !subHtml.contains("display: none", ignoreCase = true)
                val habilitado = !subHtml.contains("disabled", ignoreCase = true)
                if (visible) DocumentoAccionVisual(
                    accion,
                    accion.icono,
                    accion.descripcion,
                    habilitado
                ) else null
            }
        }
    }
}

data class DocumentoAccionVisual(
    val tipo: DocumentoAccion,
    val icono: ImageVector,
    val descripcion: String,
    val habilitado: Boolean
)

@Composable
fun MostrarFormularioPorTipo(
    tipoFormulario: TipoFormularioTramite,
    requisitosList: List<Any>,
    onCheckedListUpdatedA: (List<RequisitoSeleccionado>) -> Unit,
    onCheckedListUpdatedB: (List<RequisitoTramiteB>) -> Unit,
    onCheckedListUpdatedC: (List<RequisitoTramiteC>, String?) -> Unit,
    flag_crear: Boolean,
    onCheckedListUpdatedD: (List<RequisitoTramiteD>) -> Unit,
    onCheckedListUpdatedE: (List<RequisitoSeleccionado>) -> Unit = {},
    onCheckedListUpdatedH: (List<RequisitoTramiteC>, String?) -> Unit,
    onCheckedListUpdatedL: (List<RequisitoTramiteC>, String?) -> Unit
) {
    when (tipoFormulario) {
        TipoFormularioTramite.A -> CardTramiteModeloA(
            requisitos = requisitosList,
            onCheckedChange = onCheckedListUpdatedA,
            flag_crear = flag_crear
        )

        TipoFormularioTramite.B -> CardTramiteModeloB(
            requisitos = requisitosList,
            onCheckedChange = onCheckedListUpdatedB,
            flag_crear = flag_crear
        )

        TipoFormularioTramite.C -> CardTramiteModeloC(
            requisitos = requisitosList,
            onCheckedChange = onCheckedListUpdatedC,
            flag_crear = flag_crear
        )

        TipoFormularioTramite.D -> CardTramiteModeloD(
            requisitos = requisitosList,
            onComboBoxChange = onCheckedListUpdatedD,
            flag_crear = flag_crear
        )

        TipoFormularioTramite.E -> CardTramiteModeloE(
            requisitos = requisitosList,
            onSeleccionChange = onCheckedListUpdatedE,
            flag_crear = flag_crear
        )

        TipoFormularioTramite.H -> CardTramiteModeloH(
            requisitos = requisitosList,
            onArchivoChange = onCheckedListUpdatedH,
            flag_crear = flag_crear
        )

        TipoFormularioTramite.L -> CardTramiteModeloL(
            requisitos = requisitosList,
            onArchivoChange = onCheckedListUpdatedL,
            flag_crear = flag_crear
        )

        else -> Unit
    }
}
