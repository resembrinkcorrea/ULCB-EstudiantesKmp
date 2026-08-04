package pe.lecordonbleu.universidadestudiante.presentation.screens.vermatricula.customcell

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import pe.lecordonbleu.universidadestudiante.data.remote.dto.ListDetMatric
import pe.lecordonbleu.universidadestudiante.data.remote.dto.ListVerMatric
import pe.lecordonbleu.universidadestudiante.getColorsTheme
import pe.lecordonbleu.universidadestudiante.presentation.components.dialogs.ConfirmarEnvioDialog
import pe.lecordonbleu.universidadestudiante.presentation.components.dialogs.CustomDialogBasic

@Composable
fun CursoMatriculaCard(
    curso: ListVerMatric,
    secciones: List<ListDetMatric>?,
    selectedSec: ListDetMatric?,
    isExpanded: Boolean,
    isDeseleccionado: Boolean,
    onToggleExpand: () -> Unit,
    onSeccionSelected: (ListDetMatric) -> Unit,
    onDeseleccionar: () -> Unit
) {
    val colors = getColorsTheme()
    var showCruceDialog by remember { mutableStateOf(false) }
    var showDeleteDialog by remember { mutableStateOf(false) }
    var showDesaprobadaDialog by remember { mutableStateOf(false) }
    var cruceMensaje by remember { mutableStateOf("") }

    val hasSeccionAsignada = !isDeseleccionado && (selectedSec != null || curso.oad_seccion_nombre.isNotEmpty())

    val seccionNombre = if (isDeseleccionado) null else (selectedSec?.oad_seccion_nombre
        ?: curso.oad_seccion_nombre.takeIf { it.isNotEmpty() })
    val diaActivo = if (isDeseleccionado) null else (selectedSec?.horario?.replace(";", "\n")
        ?: curso.horario.replace(";", "\n").takeIf { it.isNotEmpty() })
    val turnoActivo = if (isDeseleccionado) null else (selectedSec?.turno_nombre
        ?: curso.turno_nombre.takeIf { it.isNotEmpty() })

    val isEstadoGreen = curso.flag_aprobado != 0
    val isHorGreen = curso.valhor == 1

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = colors.colorExpenseItem),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        shape = MaterialTheme.shapes.small,
        border = BorderStroke(1.dp, colors.colorMixPrimary.copy(alpha = 0.25f))
    ) {
        Column(modifier = Modifier.padding(horizontal = 10.dp, vertical = 8.dp)) {

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = curso.asign_det_nombre_cod.ifEmpty { curso.asign_det_nombre },
                    style = MaterialTheme.typography.bodySmall,
                    fontWeight = FontWeight.Bold,
                    color = colors.colorMixPrimary,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.weight(1f)
                )
                Spacer(modifier = Modifier.width(8.dp))
                CirculoEstado(label = "Estado", verde = isEstadoGreen)
                Spacer(modifier = Modifier.width(6.dp))
                CirculoEstado(label = "Hor.", verde = isHorGreen)
            }

            Spacer(modifier = Modifier.height(6.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                DatoChip(label = "Ciclo", valor = curso.ciclo_nivel, modifier = Modifier.weight(1f))
                DatoChip(label = "Cred.", valor = curso.cant_tot_cred, modifier = Modifier.weight(1f))
                DatoChip(label = "Hrs", valor = curso.hor_acad, modifier = Modifier.weight(1f))
                DatoChip(label = "Mod.", valor = curso.modal_asign_abrev, modifier = Modifier.weight(1f))
                DatoChip(label = "Tipo Asign.", valor = curso.tipo_asign_abrev, modifier = Modifier.weight(1.5f))
            }

            Spacer(modifier = Modifier.height(6.dp))

            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = MaterialTheme.shapes.extraSmall,
                color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.6f),
                tonalElevation = 1.dp
            ) {
                Column(modifier = Modifier.padding(horizontal = 8.dp, vertical = 6.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = "Seccion: ",
                            fontSize = 8.sp,
                            color = colors.textColor.copy(alpha = 0.6f)
                        )
                        Text(
                            text = seccionNombre ?: "",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = colors.colorVerdeFuerte
                        )
                    }

                    Spacer(modifier = Modifier.height(4.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(text = "Dia: ", fontSize = 8.sp, color = colors.textColor.copy(alpha = 0.6f))
                        Text(
                            text = diaActivo ?: "",
                            fontSize = 9.sp,
                            fontWeight = FontWeight.Bold,
                            color = colors.colorVerdeFuerte,
                            modifier = Modifier.weight(1f)
                        )
                        Text(text = "Turno: ", fontSize = 8.sp, color = colors.textColor.copy(alpha = 0.6f))
                        Text(
                            text = turnoActivo ?: "",
                            fontSize = 9.sp,
                            fontWeight = FontWeight.Bold,
                            color = colors.colorMixPrimary
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        IconButton(
                            onClick = {
                                if (hasSeccionAsignada) {
                                    if (curso.flag_aprobado == 0) {
                                        showDesaprobadaDialog = true
                                    } else if (curso.id_matric == 0) {
                                        showDeleteDialog = true
                                    }
                                } else {
                                    onToggleExpand()
                                }
                            },
                            modifier = Modifier.size(28.dp),
                            colors = IconButtonDefaults.iconButtonColors(
                                contentColor = colors.colorMixPrimary
                            )
                        ) {
                            Icon(
                                imageVector = when {
                                    hasSeccionAsignada -> Icons.Default.Delete
                                    isExpanded -> Icons.Default.Remove
                                    else -> Icons.Default.Add
                                },
                                contentDescription = null,
                                modifier = Modifier.size(18.dp)
                            )
                        }
                    }
                }
            }

            if (isExpanded && secciones != null) {
                HorizontalDivider(
                    modifier = Modifier.padding(vertical = 8.dp),
                    color = colors.textColor.copy(alpha = 0.1f)
                )
                secciones.forEachIndexed { index, sec ->
                    if (index > 0) {
                        HorizontalDivider(
                            modifier = Modifier.padding(horizontal = 4.dp),
                            color = colors.textColor.copy(alpha = 0.08f)
                        )
                    }
                    val isSelected = selectedSec?.id_oad_seccion == sec.id_oad_seccion
                    val sinVacantes = sec.flag_vacantes == "0"
                    val tieneCruce = sec.flag_cruce == "1"
                    val bgModifier = when {
                        isSelected -> Modifier
                            .fillMaxWidth()
                            .padding(vertical = 2.dp)
                            .background(
                                colors.colorMixPrimary.copy(alpha = 0.1f),
                                MaterialTheme.shapes.extraSmall
                            )
                            .border(
                                1.dp,
                                colors.colorMixPrimary.copy(alpha = 0.4f),
                                MaterialTheme.shapes.extraSmall
                            )
                        sinVacantes -> Modifier
                            .fillMaxWidth()
                            .padding(vertical = 2.dp)
                            .background(
                                colors.colorSinVacante,
                                MaterialTheme.shapes.extraSmall
                            )
                        tieneCruce -> Modifier
                            .fillMaxWidth()
                            .padding(vertical = 2.dp)
                            .background(
                                colors.colorCruceHorario,
                                MaterialTheme.shapes.extraSmall
                            )
                        else -> Modifier
                            .fillMaxWidth()
                            .padding(vertical = 2.dp)
                    }
                    Box(
                        modifier = bgModifier
                            .clickable(enabled = !sinVacantes) {
                                if (tieneCruce) {
                                    cruceMensaje = "${sec.msg_cruce}\n\n* Tiene cruce de horario con ${sec.cruce_nom_asign}"
                                    showCruceDialog = true
                                } else {
                                    onSeccionSelected(sec)
                                }
                            }
                    ) {
                        Column(modifier = Modifier.padding(horizontal = 8.dp, vertical = 6.dp)) {
                            Row(modifier = Modifier.fillMaxWidth()) {
                                EtiquetaValor(label = "Seccion", valor = sec.oad_seccion_nombre, modifier = Modifier.weight(2f))
                                EtiquetaValor(label = "Aula", valor = sec.aula_nombre, modifier = Modifier.weight(1f))
                            }
                            Spacer(modifier = Modifier.height(3.dp))
                            Row(modifier = Modifier.fillMaxWidth()) {
                                EtiquetaValor(label = "Docente", valor = sec.docente, modifier = Modifier.weight(2f))
                                EtiquetaValor(label = "Turno", valor = sec.turno_nombre, modifier = Modifier.weight(1f))
                            }
                            Spacer(modifier = Modifier.height(3.dp))
                            EtiquetaValor(label = "Dia", valor = sec.horario.replace(";", "\n"))
                            Spacer(modifier = Modifier.height(3.dp))
                            Row(modifier = Modifier.fillMaxWidth()) {
                                EtiquetaValor(label = "Sede", valor = sec.sede_abrev, modifier = Modifier.weight(3f))
                                EtiquetaValor(label = "#Vac", valor = sec.vacantes, modifier = Modifier.weight(2f))
                                EtiquetaValor(label = "#Matr", valor = sec.matriculados, modifier = Modifier.weight(2f))
                            }
                            if (sinVacantes) {
                                Spacer(modifier = Modifier.height(3.dp))
                                Text(
                                    text = "Sin vacantes",
                                    fontSize = 8.sp,
                                    color = colors.colorRojo,
                                    fontWeight = FontWeight.SemiBold
                                )
                            }
                        }
                    }
                }
            }
        }
    }

    if (showCruceDialog) {
        CustomDialogBasic(
            visible = true,
            titulo = "Matricula",
            mensaje = cruceMensaje,
            flag_val = 0,
            confirmado = false,
            onDismiss = { showCruceDialog = false }
        )
    }

    if (showDesaprobadaDialog) {
        CustomDialogBasic(
            visible = true,
            titulo = "Matricula",
            mensaje = "No se puede eliminar una asignatura obligatoria.",
            flag_val = 0,
            confirmado = false,
            onDismiss = { showDesaprobadaDialog = false }
        )
    }

    if (showDeleteDialog) {
        ConfirmarEnvioDialog(
            nombreArchivo = "",
            titulo = "Matricula",
            mensaje = "Esta seguro que desea eliminar el horario de la asignatura?",
            onConfirmar = {
                showDeleteDialog = false
                onDeseleccionar()
            },
            onCancelar = { showDeleteDialog = false }
        )
    }
}

@Composable
private fun CirculoEstado(label: String, verde: Boolean) {
    val colors = getColorsTheme()
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = label,
            fontSize = 7.sp,
            color = colors.textColor.copy(alpha = 0.6f)
        )
        Spacer(modifier = Modifier.height(2.dp))
        Box(
            modifier = Modifier
                .size(14.dp)
                .background(
                    color = if (verde) colors.colorVerdeFuerte else colors.colorRojo,
                    shape = CircleShape
                )
        )
    }
}

@Composable
private fun DatoChip(label: String, valor: String, modifier: Modifier = Modifier) {
    val colors = getColorsTheme()
    Column(modifier = modifier, horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = label,
            fontSize = 7.sp,
            color = colors.textColor.copy(alpha = 0.6f)
        )
        Text(
            text = valor.ifEmpty { "-" },
            fontSize = 9.sp,
            fontWeight = FontWeight.Bold,
            color = colors.colorMixPrimary
        )
    }
}

@Composable
private fun EtiquetaValor(label: String, valor: String, modifier: Modifier = Modifier) {
    val colors = getColorsTheme()
    Row(modifier = modifier, verticalAlignment = Alignment.Top) {
        Text(
            text = "$label: ",
            fontSize = 8.sp,
            color = colors.textColor.copy(alpha = 0.6f)
        )
        Text(
            text = valor.ifEmpty { "-" },
            fontSize = 8.sp,
            fontWeight = FontWeight.Bold,
            color = colors.colorMixPrimary
        )
    }
}
