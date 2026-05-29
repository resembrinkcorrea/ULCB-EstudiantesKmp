package pe.lecordonbleu.universidadestudiante.presentation.screens.tramitedocumentario.customcell

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.ArrowDropUp
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.datetime.LocalDate
import pe.lecordonbleu.universidadestudiante.getTodayLocalDate
import pe.lecordonbleu.universidadestudiante.data.remote.dto.EstadoTramite
import pe.lecordonbleu.universidadestudiante.data.remote.dto.TipoTramite
import pe.lecordonbleu.universidadestudiante.data.remote.dto.Tramite
import pe.lecordonbleu.universidadestudiante.getColorsTheme
import pe.lecordonbleu.universidadestudiante.presentation.components.AppDropdownMenu

@Composable
fun FiltrosTramiteExpandable(
    estados: List<EstadoTramite>,
    tiposTramite: List<TipoTramite>,
    tramites: List<Tramite>,
    selectedEstado: EstadoTramite?,
    selectedTipoTramite: TipoTramite?,
    selectedTramite: Tramite?,
    onEstadoSelected: (EstadoTramite) -> Unit,
    onTipoTramiteSelected: (TipoTramite) -> Unit,
    onTramiteSelected: (Tramite) -> Unit,
    fechaInicio: String,
    fechaFin: String,
    onFechaInicioChange: (String) -> Unit,
    onFechaFinChange: (String) -> Unit,
    isInitiallyExpanded: Boolean = false,
    onExpandedChange: (Boolean) -> Unit
) {
    val colors = getColorsTheme()
    var showDatePickerInicio by remember { mutableStateOf(false) }
    var showDatePickerFin by remember { mutableStateOf(false) }
    var fechaInicioDate by remember { mutableStateOf<LocalDate>(getTodayLocalDate()) }
    var fechaFinDate by remember { mutableStateOf<LocalDate>(getTodayLocalDate()) }
    val expanded by rememberUpdatedState(isInitiallyExpanded)

    Column(modifier = Modifier.fillMaxWidth()) {
        Column {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(colors.colorAzulOscuro)
                    .clickable { onExpandedChange(!expanded) }
                    .padding(horizontal = 16.dp, vertical = 14.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.FilterList,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(20.dp)
                    )
                    Text(
                        "Filtros de Búsqueda",
                        color = Color.White,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 15.sp
                    )
                }
                Icon(
                    imageVector = if (expanded) Icons.Default.ArrowDropUp else Icons.Default.ArrowDropDown,
                    contentDescription = null,
                    tint = Color.White
                )
            }

            if (expanded) {
                Column(modifier = Modifier.padding(12.dp)) {
                    Spacer(modifier = Modifier.size(8.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            AppDropdownMenu(
                                items = tiposTramite,
                                selectedItem = selectedTipoTramite ?: tiposTramite.firstOrNull(),
                                label = "Tipo de Trámite",
                                itemLabel = { it.nombre },
                                onItemSelected = onTipoTramiteSelected
                            )
                        }

                        Column(modifier = Modifier.weight(1f)) {
                            AppDropdownMenu(
                                items = estados,
                                selectedItem = selectedEstado ?: estados.firstOrNull(),
                                label = "Estado",
                                itemLabel = { it.paragene_nombre },
                                onItemSelected = onEstadoSelected
                            )
                        }
                    }

                    Spacer(modifier = Modifier.size(12.dp))

                    AppDropdownMenu(
                        items = tramites,
                        selectedItem = selectedTramite ?: tramites.firstOrNull(),
                        label = "Selecciona un trámite específico",
                        itemLabel = { if (it.nombre == "SELECCIONE") "TODOS" else it.nombre },
                        onItemSelected = onTramiteSelected
                    )

                    Spacer(modifier = Modifier.size(12.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        FechaBox(
                            titulo = "Fecha Inicio",
                            valor = fechaInicio,
                            onClick = { showDatePickerInicio = true },
                            modifier = Modifier.weight(1f)
                        )
                        FechaBox(
                            titulo = "Fecha Fin",
                            valor = fechaFin,
                            onClick = { showDatePickerFin = true },
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
            }
        }

        if (showDatePickerInicio) {
            FechaSelectorDialog(
                selectedDate = fechaInicioDate,
                onDismiss = { showDatePickerInicio = false },
                onDateSelected = {
                    fechaInicioDate = it
                    onFechaInicioChange("${it.dayOfMonth.toString().padStart(2, '0')}/${it.monthNumber.toString().padStart(2, '0')}/${it.year}")
                }
            )
        }

        if (showDatePickerFin) {
            FechaSelectorDialog(
                selectedDate = fechaFinDate,
                onDismiss = { showDatePickerFin = false },
                onDateSelected = {
                    fechaFinDate = it
                    onFechaFinChange("${it.dayOfMonth.toString().padStart(2, '0')}/${it.monthNumber.toString().padStart(2, '0')}/${it.year}")
                }
            )
        }
    }
}

@Composable
private fun FechaBox(
    titulo: String,
    valor: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val colors = getColorsTheme()
    Column(modifier = modifier, verticalArrangement = Arrangement.spacedBy(4.dp)) {
        Text(
            titulo,
            color = colors.colorGrisNeutro,
            fontSize = 12.sp,
            fontWeight = FontWeight.Medium
        )
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .clickable(onClick = onClick),
            shape = RoundedCornerShape(8.dp),
            color = colors.colorExpenseItem,
            tonalElevation = 0.dp
        ) {
            Row(
                modifier = Modifier.padding(horizontal = 12.dp, vertical = 12.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.DateRange,
                    contentDescription = null,
                    tint = colors.colorAzulContraste,
                    modifier = Modifier.size(18.dp)
                )
                Text(valor, color = colors.textColor, fontSize = 14.sp, fontWeight = FontWeight.Medium)
            }
        }
    }
}
