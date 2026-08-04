@file:OptIn(ExperimentalMaterial3Api::class)

package pe.lecordonbleu.universidadestudiante.presentation.screens.vermatricula.registrar

import androidx.compose.foundation.background
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Receipt
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import pe.lecordonbleu.universidadestudiante.data.remote.dto.ListDetMatric
import pe.lecordonbleu.universidadestudiante.data.remote.dto.ListVerMatric
import pe.lecordonbleu.universidadestudiante.domain.model.MatriculaBody
import pe.lecordonbleu.universidadestudiante.domain.model.MatriculaDetalleItem
import pe.lecordonbleu.universidadestudiante.getColorsTheme
import pe.lecordonbleu.universidadestudiante.presentation.screens.vermatricula.customcell.CursoMatriculaCard
import pe.lecordonbleu.universidadestudiante.presentation.screens.vermatricula.customcell.HorarioMatriculaTab

@Composable
fun GuardarMatriculaTab(
    cursosList: List<ListVerMatric>,
    detalleMap: Map<String, List<ListDetMatric>>,
    servNombre: String,
    idEstud: String,
    idServ: String,
    idPestDet: String,
    idUneg: String,
    idUsuario: String,
    idTipmatric: String,
    estadoIngresante: Int,
    onCursoTapped: (ListVerMatric, String, String, String, String, String) -> Unit,
    onMatricularClick: (MatriculaBody) -> Unit,
    onResumenHistoricoClick: () -> Unit,
    onCuentaCorrienteClick: () -> Unit,
    onCompartirHorarioClick: (periodo: String, cursos: List<ListVerMatric>) -> Unit
) {
    val colors = getColorsTheme()
    val selectedSecciones = remember { mutableStateMapOf<String, ListDetMatric>() }
    val deseleccionados = remember { mutableStateMapOf<String, Boolean>() }
    var expandedCursoId by remember { mutableStateOf<String?>(null) }
    var showHorario by remember { mutableStateOf(false) }
    val sheetState = rememberModalBottomSheetState()

    val selectedCreditos = cursosList
        .filter { !deseleccionados.containsKey(it.id_oacad_det) && (selectedSecciones.containsKey(it.id_oacad_det) || it.oad_seccion_nombre.isNotEmpty()) }
        .sumOf { it.cant_tot_cred.toIntOrNull() ?: 0 }
    val maxCred = cursosList.firstOrNull()?.max_cred ?: "-"

    val peracadNombre = cursosList.firstOrNull()?.peracad_nombre ?: ""

    Column(modifier = Modifier.fillMaxSize()) {
        Spacer(modifier = Modifier.height(10.dp))
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Card 1: carrera + periodo + creditos
            Row(
                modifier = Modifier
                    .weight(1f)
                    .background(colors.colorMixPrimary, shape = MaterialTheme.shapes.medium)
                    .padding(horizontal = 12.dp, vertical = 10.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    if (servNombre.isNotEmpty()) {
                        Text(
                            text = servNombre,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White,
                            maxLines = 2,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                    if (peracadNombre.isNotEmpty()) {
                        Text(
                            text = peracadNombre,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    }
                }
                Text(
                    text = "Credito: $selectedCreditos/$maxCred",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }
            // Card 2: Ver Horario
            Column(
                modifier = Modifier
                    .background(colors.colorMixPrimary, shape = MaterialTheme.shapes.medium)
                    .padding(horizontal = 10.dp, vertical = 10.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                IconButton(
                    onClick = { showHorario = true },
                    modifier = Modifier.size(28.dp),
                    colors = IconButtonDefaults.iconButtonColors(contentColor = Color.White)
                ) {
                    Icon(
                        imageVector = Icons.Default.CalendarMonth,
                        contentDescription = "Ver Horario",
                        modifier = Modifier.size(22.dp)
                    )
                }
                Text(
                    text = "Ver Horario",
                    fontSize = 9.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color.White
                )
            }
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            TextButton(
                onClick = onResumenHistoricoClick,
                modifier = Modifier.weight(1f)
            ) {
                Icon(
                    imageVector = Icons.Default.History,
                    contentDescription = null,
                    tint = colors.colorAzulProfundo,
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = "Resumen Histórico",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = colors.colorAzulProfundo
                )
            }
            TextButton(
                onClick = onCuentaCorrienteClick,
                modifier = Modifier.weight(1f)
            ) {
                Icon(
                    imageVector = Icons.Default.Receipt,
                    contentDescription = null,
                    tint = colors.colorVerdeFuerte,
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = "Cuenta Corriente",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = colors.colorVerdeFuerte
                )
            }
        }

        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .padding(horizontal = 12.dp),
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            item { Spacer(modifier = Modifier.height(4.dp)) }

            items(cursosList, key = { it.id_oacad_det }) { curso ->
                val isExpanded = expandedCursoId == curso.id_oacad_det
                val selectedSec = selectedSecciones[curso.id_oacad_det]
                val secciones = detalleMap[curso.id_oacad_det]

                CursoMatriculaCard(
                    curso = curso,
                    secciones = secciones,
                    selectedSec = selectedSec,
                    isExpanded = isExpanded,
                    isDeseleccionado = deseleccionados.containsKey(curso.id_oacad_det),
                    onToggleExpand = {
                        expandedCursoId = if (isExpanded) null else curso.id_oacad_det
                        if (!isExpanded) {
                            val ids_AsignDetCr = cursosList.joinToString(",") { c -> if (deseleccionados.containsKey(c.id_oacad_det)) "" else selectedSecciones[c.id_oacad_det]?.id_asign_det_cr ?: c.id_asign_det_cr }
                            val id_HoraDia    = cursosList.joinToString(",") { c -> if (deseleccionados.containsKey(c.id_oacad_det)) "" else selectedSecciones[c.id_oacad_det]?.id_hora_dia    ?: c.id_hora_dia }
                            val id_DiaSemana  = cursosList.joinToString(",") { c -> if (deseleccionados.containsKey(c.id_oacad_det)) "" else selectedSecciones[c.id_oacad_det]?.id_dia_semana  ?: c.id_dia_semana }
                            val hora_IniCr    = cursosList.joinToString(",") { c -> if (deseleccionados.containsKey(c.id_oacad_det)) "" else selectedSecciones[c.id_oacad_det]?.hora_ini_cr    ?: c.hora_ini_cr }
                            val hora_FinCr    = cursosList.joinToString(",") { c -> if (deseleccionados.containsKey(c.id_oacad_det)) "" else selectedSecciones[c.id_oacad_det]?.hora_fin_cr    ?: c.hora_fin_cr }
                            onCursoTapped(curso, ids_AsignDetCr, id_HoraDia, id_DiaSemana, hora_IniCr, hora_FinCr)
                        }
                    },
                    onSeccionSelected = { sec ->
                        selectedSecciones[curso.id_oacad_det] = sec
                        deseleccionados.remove(curso.id_oacad_det)
                        expandedCursoId = null
                    },
                    onDeseleccionar = {
                        selectedSecciones.remove(curso.id_oacad_det)
                        deseleccionados[curso.id_oacad_det] = true
                        expandedCursoId = null
                    }
                )
            }
            item { Spacer(modifier = Modifier.height(8.dp)) }
        }

        Button(
            onClick = {
                val items = buildMatriculaItems(
                    cursosList = cursosList.filter { !deseleccionados.containsKey(it.id_oacad_det) },
                    selectedSecciones = selectedSecciones,
                    selectedCreditos = selectedCreditos,
                    idEstud = idEstud,
                    idServ = idServ,
                    idPestDet = idPestDet,
                    idUneg = idUneg,
                    idUsuario = idUsuario,
                    idTipmatric = idTipmatric,
                    estadoIngresante = estadoIngresante
                )
                onMatricularClick(MatriculaBody(items))
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = colors.colorMixPrimary
            )
        ) {
            Text(
                text = "Registrar Matricula",
                fontSize = 14.sp,
                fontWeight = FontWeight.SemiBold
            )
        }

        Spacer(modifier = Modifier.height(8.dp))
    }

    if (showHorario) {
        val horariosSeleccionados = cursosList
            .filter { !deseleccionados.containsKey(it.id_oacad_det) && (selectedSecciones.containsKey(it.id_oacad_det) || it.oad_seccion_nombre.isNotEmpty()) }
            .map { curso ->
                val sec = selectedSecciones[curso.id_oacad_det]
                if (sec != null) curso.copy(horario = sec.horario) else curso
            }
        ModalBottomSheet(
            onDismissRequest = { showHorario = false },
            sheetState = sheetState,
            containerColor = MaterialTheme.colorScheme.surface
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 4.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Ver Horario",
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    color = colors.textColor
                )
                TextButton(
                    onClick = {
                        onCompartirHorarioClick(peracadNombre, horariosSeleccionados)
                    }
                ) {
                    Icon(
                        imageVector = Icons.Default.Share,
                        contentDescription = "Compartir horario",
                        tint = colors.colorMixPrimary,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "Compartir",
                        color = colors.colorMixPrimary,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
            }
            if (horariosSeleccionados.isEmpty()) {
                Box(modifier = Modifier.fillMaxWidth().height(280.dp)) {
                    HorarioMatriculaTab(
                        items = horariosSeleccionados,
                        emptyText = "Sin horarios seleccionados"
                    )
                }
            } else {
                HorarioMatriculaTab(
                    items = horariosSeleccionados,
                    emptyText = "Sin horarios seleccionados"
                )
            }
        }
    }
}

private fun buildMatriculaItems(
    cursosList: List<ListVerMatric>,
    selectedSecciones: Map<String, ListDetMatric>,
    selectedCreditos: Int,
    idEstud: String,
    idServ: String,
    idPestDet: String,
    idUneg: String,
    idUsuario: String,
    idTipmatric: String,
    estadoIngresante: Int
): List<MatriculaDetalleItem> {
    val seleccionados = cursosList.filter {
        selectedSecciones.containsKey(it.id_oacad_det) || it.oad_seccion_nombre.isNotEmpty()
    }
    return seleccionados.map { curso ->
        val sec = selectedSecciones[curso.id_oacad_det]
        MatriculaDetalleItem(
            asign_det_nombre = curso.asign_det_nombre,
            cred_asign = curso.cant_tot_cred,
            cred_total = selectedCreditos.toString(),
            flag_ficha_envio = "1",
            flag_mat_term_cond = "1",
            id_estud = idEstud,
            id_matric = curso.id_matric.toString(),
            id_matric_asig = curso.id_matric_asig.toString(),
            id_matric_asig_secc = curso.id_matric_asig_secc.toString(),
            id_oacad_arranque = curso.id_oacad_arranque.toString(),
            id_oacad_det = curso.id_oacad_det,
            id_oad_seccion = sec?.id_oad_seccion ?: curso.id_oad_seccion,
            id_peracad = curso.id_peracad,
            id_pest_det = idPestDet,
            id_serv = idServ,
            id_tipmatric = idTipmatric,
            id_uneg = idUneg,
            id_user = idUsuario,
            nro_rep = curso.nro_rep.toString(),
            oad_seccion_nombre = sec?.oad_seccion_nombre ?: curso.oad_seccion_nombre,
            origen = curso.origen.toString()
        )
    }
}
