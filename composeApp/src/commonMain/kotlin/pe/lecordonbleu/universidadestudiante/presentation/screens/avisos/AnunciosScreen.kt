package pe.lecordonbleu.universidadestudiante.presentation.screens.avisos

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import pe.lecordonbleu.universidadestudiante.presentation.components.StandardTopBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import coil3.compose.AsyncImage
import kotlinx.coroutines.delay
import pe.lecordonbleu.universidadestudiante.SettingsStorage
import pe.lecordonbleu.universidadestudiante.data.remote.dto.data_notificaciones
import pe.lecordonbleu.universidadestudiante.data.remote.dto.listHoraServer
import pe.lecordonbleu.universidadestudiante.getSettingsStorage
import pe.lecordonbleu.universidadestudiante.presentation.vo.ResourceUiState
import pe.lecordonbleu.universidadestudiante.util.renderHtmlToFormattedText

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AnunciosScreen(
    viewModel: AnunciosViewModel,
    navController: NavController
) {
    val uiStateAnuncios by viewModel.uiStateAnuncios.collectAsStateWithLifecycle()
    val uiStateHora by viewModel.uiStateHora.collectAsStateWithLifecycle()

    val settingsStorage: SettingsStorage = getSettingsStorage()
    val idSistema = settingsStorage.getInt("idSistema", 0)
    val idUsuario = settingsStorage.getInt("idUsuario", 0)

    var anuncios by remember { mutableStateOf<List<data_notificaciones>>(emptyList()) }
    var horaServer by remember { mutableStateOf<listHoraServer?>(null) }
    var showLoading by remember { mutableStateOf(false) }
    var mensaje by remember { mutableStateOf("No hay avisos disponibles.") }

    LaunchedEffect(Unit) {
        viewModel.setAnunciosRequest(id_uneg = 2, id_sistema = idSistema, id_usuario = idUsuario)
        viewModel.setHoraServidorRequest()
    }

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        topBar = {
            StandardTopBar(
                title = "Mis Avisos",
                subtitle = "NOTIFICACIONES",
                onBackClick = { navController.popBackStack() }
            )
        }
    ) { innerPadding ->
        AnunciosContent(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            anuncios = anuncios,
            horaServer = horaServer,
            showLoading = showLoading,
            mensaje = mensaje
        )
    }

    when (val state = uiStateAnuncios) {
        is ResourceUiState.Loading -> {
            showLoading = true
            mensaje = ""
        }

        is ResourceUiState.Success -> {
            showLoading = false
            anuncios = state.data.data_notificaciones
            mensaje = ""
        }

        is ResourceUiState.Error -> {
            showLoading = false
            anuncios = emptyList()
            mensaje = state.message
        }

        is ResourceUiState.Empty -> {
            showLoading = false
            anuncios = emptyList()
            mensaje = "No hay avisos disponibles."
        }
    }

    when (val stateHora = uiStateHora) {
        is ResourceUiState.Success -> {
            horaServer = stateHora.data
        }

        is ResourceUiState.Error,
        is ResourceUiState.Empty -> {
            horaServer = null
        }

        is ResourceUiState.Loading -> Unit
    }
}

@Composable
private fun AnunciosContent(
    modifier: Modifier,
    anuncios: List<data_notificaciones>,
    horaServer: listHoraServer?,
    showLoading: Boolean,
    mensaje: String
) {
    when {
        showLoading -> AnunciosLoading(modifier = modifier)
        anuncios.isEmpty() -> AnunciosMensaje(
            modifier = modifier,
            horaServer = horaServer,
            mensaje = mensaje.ifBlank { "No hay avisos disponibles." }
        )

        else -> LazyColumn(
            modifier = modifier.padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item { Spacer(modifier = Modifier.height(8.dp)) }
            items(anuncios) { anuncio ->
                AnuncioCard(
                    anuncio = anuncio,
                    horaServer = horaServer
                )
            }
            item { Spacer(modifier = Modifier.height(8.dp)) }
        }
    }
}

@Composable
private fun AnunciosLoading(modifier: Modifier) {
    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator()
    }
}

@Composable
private fun AnunciosMensaje(
    modifier: Modifier,
    horaServer: listHoraServer?,
    mensaje: String
) {
    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            HoraServidorSimple(horaServer = horaServer)
            Spacer(modifier = Modifier.height(8.dp))
            Icon(
                imageVector = Icons.Default.Notifications,
                contentDescription = null,
                modifier = Modifier.size(48.dp),
                tint = Color.Gray
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = mensaje,
                color = Color.Gray,
                fontSize = 14.sp
            )
        }
    }
}

@Composable
private fun AnuncioCard(
    anuncio: data_notificaciones,
    horaServer: listHoraServer?
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            AnuncioHeader(
                anuncio = anuncio,
                horaServer = horaServer
            )

            Spacer(modifier = Modifier.height(48.dp))

            AsyncImage(
                model = anuncio.avisos_url_imagen,
                contentDescription = anuncio.avisos_titulo,
                modifier = Modifier
                    .fillMaxWidth(0.74f)
                    .align(Alignment.CenterHorizontally)
                    .heightIn(min = 260.dp, max = 420.dp),
                contentScale = ContentScale.Fit
            )

            Spacer(modifier = Modifier.height(56.dp))

            AnuncioFechas(anuncio = anuncio)

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = anuncio.avisos_nombre,
                fontSize = 15.sp,
                fontWeight = FontWeight.Medium,
                color = Color(0xFF0A84FF),
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )

            if (anuncio.avisos_contenido.isNotBlank()) {
                Spacer(modifier = Modifier.height(22.dp))
                Text(
                    text = renderHtmlToFormattedText(anuncio.avisos_contenido),
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
}

@Composable
private fun AnuncioHeader(
    anuncio: data_notificaciones,
    horaServer: listHoraServer?
) {
    val relojServidor = rememberRelojServidorState(horaServer)

    Column {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.Top
        ) {
            Text(
                text = anuncio.avisos_titulo.uppercase(),
                fontWeight = FontWeight.Black,
                fontSize = 20.sp,
                color = Color.Black,
                modifier = Modifier.weight(1f),
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )

            if (anuncio.flag_prioritario == 1) {
                Spacer(modifier = Modifier.width(12.dp))
                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = Color(0xFFFF3B45)
                ) {
                    Text(
                        text = "PRIORITARIO",
                        color = Color.White,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Black,
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 7.dp)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            AnuncioInfoIconText(
                icon = Icons.Default.DateRange,
                text = relojServidor?.fechaHeader.orEmpty()
            )
            AnuncioInfoIconText(
                icon = Icons.Default.Schedule,
                text = relojServidor?.hora.orEmpty()
            )
        }
    }
}

@Composable
private fun AnuncioFechas(anuncio: data_notificaciones) {
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        AnuncioInfoIconText(
            icon = Icons.Default.DateRange,
            label = "Publicacion:",
            text = formatearFechaAnuncio(anuncio.fec_ini_publi)
        )
        AnuncioInfoIconText(
            icon = Icons.Default.DateRange,
            label = "Vigencia:",
            text = formatearFechaAnuncio(anuncio.fecha_ini_vig)
        )
    }
}

@Composable
private fun AnuncioInfoIconText(
    icon: ImageVector,
    text: String,
    label: String? = null
) {
    if (text.isBlank()) return

    Row(verticalAlignment = Alignment.CenterVertically) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = Color(0xFF8E8E93),
            modifier = Modifier.size(18.dp)
        )
        Spacer(modifier = Modifier.width(8.dp))

        if (label != null) {
            Text(
                text = "$label ",
                fontSize = 16.sp,
                fontWeight = FontWeight.Black,
                color = Color(0xFF8E8E93)
            )
        }

        Text(
            text = text,
            fontSize = 16.sp,
            fontWeight = FontWeight.Black,
            color = if (label == null) Color(0xFF8E8E93) else Color.Black
        )
    }
}

@Composable
private fun rememberRelojServidorState(horaServer: listHoraServer?): RelojServidorState? {
    val relojKey = "${horaServer?.datee.orEmpty()}|${horaServer?.Hora.orEmpty()}"
    var relojServidor by remember(relojKey) {
        mutableStateOf(crearEstadoRelojServidor(horaServer))
    }

    LaunchedEffect(relojKey) {
        while (relojServidor != null) {
            delay(1000)
            relojServidor = relojServidor?.siguienteSegundo()
        }
    }

    return relojServidor
}

@Composable
private fun HoraServidorSimple(horaServer: listHoraServer?) {
    val relojServidor = rememberRelojServidorState(horaServer)
    val horaServidor = relojServidor?.texto.orEmpty()
    if (horaServidor.isBlank()) return

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 14.dp, vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = Icons.Default.Schedule,
            contentDescription = null,
            tint = Color.Gray,
            modifier = Modifier.size(16.dp)
        )
        Spacer(modifier = Modifier.width(6.dp))
        Text(
            text = "Hora servidor: $horaServidor",
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Gray
        )
    }
}

private fun formatearFechaAnuncio(fecha: String): String {
    val partes = fecha.take(10).split("-")
    return if (partes.size == 3) "${partes[2]}/${partes[1]}/${partes[0]}" else fecha.take(10)
}

private data class RelojServidorState(
    val anio: Int?,
    val mes: Int?,
    val dia: Int?,
    val segundosDia: Int
) {
    val hora: String
        get() = formatearSegundosDia(segundosDia)

    val fechaHeader: String
        get() {
            if (anio == null || mes == null || dia == null) return ""

            val diaSemana = obtenerDiaSemana(anio, mes, dia)
            val fecha = "${dia.toString().padStart(2, '0')}/${mes.toString().padStart(2, '0')}/${(anio % 100).toString().padStart(2, '0')}"
            return "$diaSemana - $fecha"
        }

    val texto: String
        get() {
            return if (anio != null && mes != null && dia != null) {
                "${dia.toString().padStart(2, '0')}/${mes.toString().padStart(2, '0')}/$anio $hora"
            } else {
                hora
            }
        }

    fun siguienteSegundo(): RelojServidorState {
        val nuevoSegundo = segundosDia + 1
        if (nuevoSegundo < SEGUNDOS_DIA) {
            return copy(segundosDia = nuevoSegundo)
        }

        val nuevaFecha = if (anio != null && mes != null && dia != null) {
            avanzarUnDia(anio, mes, dia)
        } else {
            null
        }

        return if (nuevaFecha != null) {
            copy(
                anio = nuevaFecha.anio,
                mes = nuevaFecha.mes,
                dia = nuevaFecha.dia,
                segundosDia = 0
            )
        } else {
            copy(segundosDia = 0)
        }
    }
}

private data class FechaServidor(
    val anio: Int,
    val mes: Int,
    val dia: Int
)

private const val SEGUNDOS_DIA = 24 * 60 * 60

private fun crearEstadoRelojServidor(horaServer: listHoraServer?): RelojServidorState? {
    if (horaServer == null) return null

    val datee = horaServer.datee
    if (datee.isNotBlank()) {
        val fecha = parsearFechaServidor(datee.substringBefore("T", ""))
        val segundosDia = parsearSegundosDia(datee.substringAfter("T", ""))

        if (fecha != null && segundosDia != null) {
            return RelojServidorState(
                anio = fecha.anio,
                mes = fecha.mes,
                dia = fecha.dia,
                segundosDia = segundosDia
            )
        }
    }

    val segundosDia = parsearSegundosDia(horaServer.Hora)
    return segundosDia?.let {
        RelojServidorState(
            anio = null,
            mes = null,
            dia = null,
            segundosDia = it
        )
    }
}

private fun parsearFechaServidor(fecha: String): FechaServidor? {
    val partes = fecha.take(10).split("-")
    if (partes.size != 3) return null

    val anio = partes[0].toIntOrNull() ?: return null
    val mes = partes[1].toIntOrNull() ?: return null
    val dia = partes[2].toIntOrNull() ?: return null

    return FechaServidor(
        anio = anio,
        mes = mes,
        dia = dia
    )
}

private fun parsearSegundosDia(hora: String): Int? {
    val partes = hora.take(8).split(":")
    if (partes.size < 2) return null

    val horas = partes[0].toIntOrNull() ?: return null
    val minutos = partes[1].toIntOrNull() ?: return null
    val segundos = partes.getOrNull(2)?.toIntOrNull() ?: 0

    return (horas * 60 * 60) + (minutos * 60) + segundos
}

private fun formatearSegundosDia(segundosDia: Int): String {
    val horas = segundosDia / 3600
    val minutos = (segundosDia % 3600) / 60
    val segundos = segundosDia % 60

    return "${horas.toString().padStart(2, '0')}:${minutos.toString().padStart(2, '0')}:${segundos.toString().padStart(2, '0')}"
}

private fun avanzarUnDia(anio: Int, mes: Int, dia: Int): FechaServidor {
    val ultimoDiaMes = obtenerUltimoDiaMes(anio, mes)
    if (dia < ultimoDiaMes) {
        return FechaServidor(anio, mes, dia + 1)
    }

    if (mes < 12) {
        return FechaServidor(anio, mes + 1, 1)
    }

    return FechaServidor(anio + 1, 1, 1)
}

private fun obtenerUltimoDiaMes(anio: Int, mes: Int): Int {
    return when (mes) {
        1, 3, 5, 7, 8, 10, 12 -> 31
        4, 6, 9, 11 -> 30
        2 -> if (esAnioBisiesto(anio)) 29 else 28
        else -> 30
    }
}

private fun esAnioBisiesto(anio: Int): Boolean {
    return (anio % 4 == 0 && anio % 100 != 0) || anio % 400 == 0
}

private fun obtenerDiaSemana(anio: Int, mes: Int, dia: Int): String {
    var mesCalculo = mes
    var anioCalculo = anio

    if (mesCalculo < 3) {
        mesCalculo += 12
        anioCalculo -= 1
    }

    val k = anioCalculo % 100
    val j = anioCalculo / 100
    val h = (dia + ((13 * (mesCalculo + 1)) / 5) + k + (k / 4) + (j / 4) + (5 * j)) % 7

    return when (h) {
        0 -> "SAB"
        1 -> "DOM"
        2 -> "LUN"
        3 -> "MAR"
        4 -> "MIE"
        5 -> "JUE"
        else -> "VIE"
    }
}
