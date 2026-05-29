package pe.lecordonbleu.universidadestudiante.presentation.screens.vermatricula.customcell

import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.datetime.LocalTime
import pe.lecordonbleu.universidadestudiante.core.theme.IlcbStripeAmber
import pe.lecordonbleu.universidadestudiante.core.theme.IlcbStripeBlue
import pe.lecordonbleu.universidadestudiante.core.theme.IlcbStripeBlueGrey
import pe.lecordonbleu.universidadestudiante.core.theme.IlcbStripeBrown
import pe.lecordonbleu.universidadestudiante.core.theme.IlcbStripeCyan
import pe.lecordonbleu.universidadestudiante.core.theme.IlcbStripeDeepOrange
import pe.lecordonbleu.universidadestudiante.core.theme.IlcbStripeDeepPurple
import pe.lecordonbleu.universidadestudiante.core.theme.IlcbStripeGold
import pe.lecordonbleu.universidadestudiante.core.theme.IlcbStripeGreen
import pe.lecordonbleu.universidadestudiante.core.theme.IlcbStripeIndigo
import pe.lecordonbleu.universidadestudiante.core.theme.IlcbStripeLightBlue
import pe.lecordonbleu.universidadestudiante.core.theme.IlcbStripeLime
import pe.lecordonbleu.universidadestudiante.core.theme.IlcbStripeMagenta
import pe.lecordonbleu.universidadestudiante.core.theme.IlcbStripeNavy
import pe.lecordonbleu.universidadestudiante.core.theme.IlcbStripeOrange
import pe.lecordonbleu.universidadestudiante.core.theme.IlcbStripePink
import pe.lecordonbleu.universidadestudiante.core.theme.IlcbStripePurple
import pe.lecordonbleu.universidadestudiante.core.theme.IlcbStripeRed
import pe.lecordonbleu.universidadestudiante.core.theme.IlcbStripeViolet
import pe.lecordonbleu.universidadestudiante.data.remote.dto.ListVerMatric
import pe.lecordonbleu.universidadestudiante.getColorsTheme

private val stripeColors = listOf(
    IlcbStripeOrange, IlcbStripeIndigo,
    IlcbStripePink, IlcbStripeViolet, IlcbStripeLime, IlcbStripeDeepOrange,
    IlcbStripeBlue, IlcbStripeRed, IlcbStripeAmber, IlcbStripeGreen,
    IlcbStripePurple, IlcbStripeCyan,
    IlcbStripeBrown, IlcbStripeBlueGrey, IlcbStripeDeepPurple, IlcbStripeGold,
    IlcbStripeLightBlue, IlcbStripeMagenta, IlcbStripeNavy
)

// "JUE: 07:00 - 09:30" → grupo 1=dia, 2=ini, 3=fin
private val REGEX_SLOT =
    Regex("""([A-ZÁÉÍÓÚÑa-záéíóúñ]+):\s*(\d{1,2}:\d{2})\s*-\s*(\d{1,2}:\d{2})""")

private data class SlotHorario(
    val diaAbrev: String,   // "LUN", "MAR", "JUE", etc.
    val horaIni: LocalTime,
    val horaFin: LocalTime,
    val item: ListVerMatric
)

// "JUE: 07:00 - 09:30" → SlotHorario. "07:00" se parsea con fallback ":00" para segundos.
private fun parsearHorario(items: List<ListVerMatric>): List<SlotHorario> =
    items.flatMap { item ->
        item.horario.split(";").mapNotNull { seg ->
            val m = REGEX_SLOT.find(seg.trim()) ?: return@mapNotNull null
            val (dia, ini, fin) = m.destructured
            fun hora(s: String) = runCatching { LocalTime.parse(s.trim()) }.getOrNull()
                ?: runCatching { LocalTime.parse("${s.trim()}:00") }.getOrNull()
            SlotHorario(
                dia.trim().uppercase(),
                hora(ini) ?: return@mapNotNull null,
                hora(fin) ?: return@mapNotNull null,
                item
            )
        }
    }

private fun nombreDia(abrev: String): String = when (abrev.uppercase()) {
    "LUN" -> "Lunes"; "MAR" -> "Martes"; "MIE", "MIÉ" -> "Miércoles"
    "JUE" -> "Jueves"; "VIE" -> "Viernes"; "SAB", "SÁB" -> "Sábado"; "DOM" -> "Domingo"
    else -> abrev
}

@Composable
fun HorarioMatriculaTab(items: List<ListVerMatric>) {
    val colors = getColorsTheme()
    val horaInicio = 6
    val horaFin = 23
    val alturaTotalDp = 1700.dp
    val anchoHoras = 48.dp
    val anchoColumna = 120.dp
    val alturaHeader = 38.dp
    val minutosTotales = (horaFin - horaInicio) * 60

    // Parsear el campo `horario` de cada ítem → slots (día, horaIni, horaFin, item)
    val slots = remember(items) { parsearHorario(items) }

    // Color por curso según orden de aparición en la lista (índice 0, 1, 2...)
    val colorPorCurso = remember(items) {
        items.map { it.asign_det_nombre }
            .distinct()
            .mapIndexed { idx, nombre -> nombre to stripeColors[idx % stripeColors.size] }
            .toMap()
    }

    val diasOrdenados = remember(slots) {
        slots.map { it.diaAbrev }.distinct().sortedBy { abrev ->
            when (abrev.uppercase()) {
                "LUN" -> 1; "MAR" -> 2; "MIE", "MIÉ" -> 3
                "JUE" -> 4; "VIE" -> 5; "SAB", "SÁB" -> 6; "DOM" -> 7
                else -> 99
            }
        }
    }

    if (diasOrdenados.isEmpty()) {
        Box(
            modifier = Modifier.fillMaxSize().padding(32.dp),
            contentAlignment = Alignment.Center
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Icon(
                    imageVector = Icons.Default.CalendarToday,
                    contentDescription = null,
                    tint = colors.textColor.copy(alpha = 0.2f),
                    modifier = Modifier.size(52.dp)
                )
                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    text = "Selecciona una carrera",
                    color = colors.textColor.copy(alpha = 0.4f),
                    fontSize = 14.sp,
                    textAlign = TextAlign.Center
                )
            }
        }
        return
    }

    val density = LocalDensity.current
    val escala = with(density) { alturaTotalDp.toPx() / minutosTotales }
    val hScroll = rememberScrollState()
    val vScroll = rememberScrollState()

    Column(modifier = Modifier.fillMaxSize()) {

        // ── Encabezados de días ───────────────────────────────────────────────
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(colors.colorExpenseItem)
        ) {
            Box(modifier = Modifier.width(anchoHoras).height(alturaHeader))
            Row(modifier = Modifier.horizontalScroll(hScroll)) {
                diasOrdenados.forEach { abrev ->
                    // El Box tiene exactamente anchoColumna, igual que la grilla debajo
                    Box(
                        modifier = Modifier
                            .width(anchoColumna)
                            .height(alturaHeader),
                        contentAlignment = Alignment.Center
                    ) {
                        Surface(
                            shape = RoundedCornerShape(10.dp),
                            color = colors.colorMixPrimary,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 6.dp, vertical = 5.dp)
                        ) {
                            Box(
                                modifier = Modifier.fillMaxWidth(),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = nombreDia(abrev).uppercase(),
                                    color = Color.White,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.ExtraBold,
                                    letterSpacing = 0.5.sp,
                                    modifier = Modifier.padding(vertical = 4.dp)
                                )
                            }
                        }
                    }
                }
            }
        }

        // ── Cuerpo ────────────────────────────────────────────────────────────
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .verticalScroll(vScroll)
                .height(alturaTotalDp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(alturaTotalDp)
            ) {
                // Etiquetas de hora (fijas, no scrollean)
                Column(modifier = Modifier.width(anchoHoras).height(alturaTotalDp)) {
                    for (hora in horaInicio until horaFin) {
                        Box(
                            modifier = Modifier
                                .width(anchoHoras)
                                .height(with(density) { (60 * escala).toDp() })
                        ) {
                            Text(
                                text = "${hora.toString().padStart(2, '0')}:00",
                                modifier = Modifier
                                    .align(Alignment.TopEnd)
                                    .padding(end = 6.dp, top = 2.dp),
                                color = colors.colorGrisNeutro,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Medium
                            )
                        }
                    }
                }

                // Grilla de días
                Box(modifier = Modifier.horizontalScroll(hScroll).height(alturaTotalDp)) {
                    Box(
                        modifier = Modifier
                            .width(anchoColumna * diasOrdenados.size)
                            .height(alturaTotalDp)
                    ) {
                        // Fondo con bandas alternas por hora
                        Column(modifier = Modifier.fillMaxSize()) {
                            for (hora in horaInicio until horaFin) {
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(with(density) { (60 * escala).toDp() })
                                        .background(
                                            if ((hora - horaInicio) % 2 == 0)
                                                colors.backGroundColor
                                            else
                                                colors.colorBlancoGris
                                        )
                                ) {
                                    HorizontalDivider(
                                        color = colors.colorGrisNeutro.copy(alpha = 0.25f),
                                        thickness = 0.5.dp,
                                        modifier = Modifier.align(Alignment.BottomStart)
                                    )
                                }
                            }
                        }

                        // Separadores verticales entre columnas
                        Row(modifier = Modifier.fillMaxSize()) {
                            diasOrdenados.forEach { _ ->
                                Row(modifier = Modifier.width(anchoColumna).fillMaxHeight()) {
                                    Spacer(modifier = Modifier.weight(1f))
                                    Box(
                                        modifier = Modifier
                                            .width(0.5.dp)
                                            .fillMaxHeight()
                                            .background(colors.colorGrisNeutro.copy(alpha = 0.3f))
                                    )
                                }
                            }
                        }

                        // Cards posicionadas por hora y columna
                        diasOrdenados.forEachIndexed { colIdx, abrevDia ->
                            slots
                                .filter { it.diaAbrev == abrevDia }
                                .forEach { slot ->
                                    val minDesde =
                                        (slot.horaIni.hour * 60 + slot.horaIni.minute) - (horaInicio * 60)
                                    val durMin =
                                        slot.horaFin.toSecondOfDay() / 60 - slot.horaIni.toSecondOfDay() / 60
                                    if (minDesde < 0 || durMin <= 0) return@forEach

                                    val offsetY = with(density) { (minDesde * escala).toDp() }
                                    val altCard = with(density) { (durMin * escala).toDp() }
                                    val offsetX = anchoColumna * colIdx
                                    val accent =
                                        colorPorCurso[slot.item.asign_det_nombre] ?: stripeColors[0]

                                    Box(
                                        modifier = Modifier
                                            .offset(x = offsetX + 2.dp, y = offsetY + 1.dp)
                                            .width(anchoColumna - 5.dp)
                                            .height(altCard - 2.dp)
                                    ) {
                                        AgendaClaseCard(slot = slot, accentColor = accent)
                                    }
                                }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun AgendaClaseCard(slot: SlotHorario, accentColor: Color) {
    Surface(
        modifier = Modifier.fillMaxSize(),
        shape = RoundedCornerShape(6.dp),
        color = accentColor
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 5.dp, vertical = 3.dp)
        ) {
            Text(
                text = slot.item.asign_det_nombre,
                fontSize = 10.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White,
                maxLines = 3,
                overflow = TextOverflow.Ellipsis,
                lineHeight = 12.sp
            )
            Text(
                text = "${
                    slot.horaIni.hour.toString().padStart(2, '0')
                }:${
                    slot.horaIni.minute.toString().padStart(2, '0')
                } - ${
                    slot.horaFin.hour.toString().padStart(2, '0')
                }:${slot.horaFin.minute.toString().padStart(2, '0')}",
                fontSize = 9.sp,
                color = Color.White.copy(alpha = 0.85f),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}
