package pe.lecordonbleu.universidadestudiante.presentation.screens.eta.customcell

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AttachFile
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Details
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.outlined.ArrowOutward
import androidx.compose.material.icons.outlined.ArrowUpward
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import pe.lecordonbleu.universidadestudiante.core.theme.IlcbAzulFuerte
import pe.lecordonbleu.universidadestudiante.core.theme.IlcbBlueMid
import pe.lecordonbleu.universidadestudiante.core.theme.IlcbBlueDeep
import pe.lecordonbleu.universidadestudiante.core.theme.IlcbError
import pe.lecordonbleu.universidadestudiante.core.theme.IlcbGreenMid
import pe.lecordonbleu.universidadestudiante.core.theme.IlcbNaranja
import pe.lecordonbleu.universidadestudiante.core.theme.IlcbNaranjaOscuro
import pe.lecordonbleu.universidadestudiante.data.remote.dto.ListDocumentosEta
import pe.lecordonbleu.universidadestudiante.getColorsTheme
import pe.lecordonbleu.universidadestudiante.presentation.screens.eta.enums.EstadoIcono
import pe.lecordonbleu.universidadestudiante.presentation.screens.eta.enums.EstadoValidacion
import pe.lecordonbleu.universidadestudiante.presentation.screens.eta.enums.TipoAccion

@Composable
fun DocumentosETACell(
    documento: ListDocumentosEta,
    onVer: () -> Unit,
    onEliminar: () -> Unit,
    onSubir: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 16.dp),
        elevation = CardDefaults.cardElevation(4.dp),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {

            // Título del documento
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    documento.nombreDoc,
                    style = MaterialTheme.typography.titleMedium
                )
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = "SUB.",
                        color = IlcbAzulFuerte,
                        fontWeight = FontWeight.Bold,
                        style = MaterialTheme.typography.bodyMedium
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Icon(
                        imageVector = Icons.Outlined.ArrowOutward,
                        contentDescription = "Subido",
                        tint = IlcbAzulFuerte,
                        modifier = Modifier.size(18.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Fecha subido
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = buildString {
                            append("Fecha subido:")
                            if (documento.FECHA_SUBIDO.isNotBlank()) {
                                append(" ${documento.FECHA_SUBIDO}")
                            }
                        },
                        style = MaterialTheme.typography.bodyMedium
                    )
                }

                when (EstadoIcono.fromHtml(documento.SUBIDO)) {
                    EstadoIcono.SUBIDO_VERDE -> Icon(
                        Icons.Default.CheckCircle,
                        contentDescription = "Subido",
                        tint = IlcbGreenMid
                    )

                    EstadoIcono.NINGUNO -> {}
                    else -> {}
                }
            }

            Spacer(modifier = Modifier.height(4.dp))

            // Fecha validación y estado
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = buildString {
                            append("Fecha validación:")
                            if (documento.FECHA_VALIDACION.isNotBlank()) {
                                append(" ${documento.FECHA_VALIDACION}")
                            }
                        },
                        style = MaterialTheme.typography.bodyMedium
                    )

                    if (documento.ESTADO_VALIDACION.isNotBlank()) {
                        Spacer(modifier = Modifier.height(2.dp))
                        EstadoRevisionText(
                            estado = EstadoValidacion.fromHtml(documento.ESTADO_VALIDACION),
                            descripcion = obtenerTextoEstadoDesdeHtml(documento.ESTADO_VALIDACION)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Botones de acción
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                val acciones = TipoAccion.parseHtml(documento.ACCIONES)

                acciones.forEach { accion ->
                    BotonAccionDocumento(
                        tipoAccion = accion,
                        onClick = {
                            when (accion) {
                                TipoAccion.VER -> onVer()
                                TipoAccion.ELIMINAR -> onEliminar()
                                TipoAccion.SUBIR -> onSubir()
                                TipoAccion.NINGUNA -> Unit
                            }
                        }
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                }
            }
        }
    }
}

@Composable
private fun BotonAccionDocumento(
    tipoAccion: TipoAccion,
    onClick: () -> Unit
) {
    val backgroundColor: Color
    val icon: ImageVector
    val iconColor: Color
    val description: String

    when (tipoAccion) {
        TipoAccion.VER -> {
            backgroundColor = IlcbBlueDeep.copy(alpha = 0.1f)
            icon = Icons.Default.Visibility
            iconColor = IlcbBlueMid
            description = "Ver"
        }

        TipoAccion.ELIMINAR -> {
            backgroundColor = IlcbError.copy(alpha = 0.15f)
            icon = Icons.Default.Delete
            iconColor = IlcbError
            description = "Eliminar"
        }

        TipoAccion.SUBIR -> {
            backgroundColor = IlcbNaranja.copy(alpha = 0.25f)
            icon = Icons.Outlined.ArrowUpward
            iconColor = IlcbNaranjaOscuro
            description = "Subir"
        }

        TipoAccion.NINGUNA -> return
    }

    Box(
        modifier = Modifier
            .background(color = backgroundColor, shape = RoundedCornerShape(6.dp))
            .clickable { onClick() }
            .padding(8.dp)
    ) {
        Icon(
            imageVector = icon,
            contentDescription = description,
            tint = iconColor,
            modifier = Modifier.size(20.dp)
        )
    }
}


@Composable
private fun EstadoRevisionText(estado: EstadoValidacion, descripcion: String) {

    val colors = getColorsTheme()

    when (estado) {

        EstadoValidacion.VALIDADO -> {
            Box(
                modifier = Modifier
                    .background(color = IlcbGreenMid.copy(alpha = 0.2f), shape = RoundedCornerShape(6.dp))
                    .padding(horizontal = 8.dp, vertical = 4.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = estado.icon,
                        contentDescription = estado.label,
                        tint = IlcbGreenMid,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = estado.label,
                        color = IlcbGreenMid,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }
        }

        EstadoValidacion.RECHAZADO -> {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = buildAnnotatedString {
                        withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                            append("Revisión: ")
                        }
                        withStyle(
                            style = SpanStyle(
                                color = estado.color,
                                fontWeight = FontWeight.SemiBold
                            )
                        ) {
                            append(estado.label)
                        }
                    },
                    style = MaterialTheme.typography.bodyMedium
                )
                Spacer(modifier = Modifier.width(6.dp))
                Icon(
                    imageVector = estado.icon,
                    contentDescription = estado.label,
                    tint = estado.color,
                    modifier = Modifier.size(18.dp)
                )
            }
        }

        EstadoValidacion.PENDIENTE -> {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = buildAnnotatedString {
                        withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                            append("Revisión: ")
                        }
                        withStyle(
                            style = SpanStyle(
                                color = Color.Gray,
                                fontWeight = FontWeight.SemiBold
                            )
                        ) {
                            append("Pendiente")
                        }
                    },
                    style = MaterialTheme.typography.bodyMedium
                )
                Spacer(modifier = Modifier.width(6.dp))
                Icon(
                    imageVector = Icons.Default.AttachFile,
                    contentDescription = "Pendiente",
                    tint = Color.Gray,
                    modifier = Modifier.size(18.dp)
                )
            }
        }

        EstadoValidacion.OBSERVADO -> {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = "Revisión:",
                    style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Icon(
                    imageVector = Icons.Default.Details,
                    contentDescription = "Observado",
                    tint = colors.colorAmbar,
                    modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = "Observado",
                    style = MaterialTheme.typography.bodyMedium.copy(
                        fontWeight = FontWeight.SemiBold,
                        color = Color.Gray
                    )
                )
            }

            Spacer(modifier = Modifier.height(2.dp))

            Text(
                text = descripcion,
                style = MaterialTheme.typography.bodySmall,
                color = Color.Gray,
                fontWeight = FontWeight.SemiBold
            )
        }
    }
}

fun obtenerTextoEstadoDesdeHtml(html: String?): String {
    if (html.isNullOrEmpty()) return ""
    return html.substringAfter("</i>").trim()
}
