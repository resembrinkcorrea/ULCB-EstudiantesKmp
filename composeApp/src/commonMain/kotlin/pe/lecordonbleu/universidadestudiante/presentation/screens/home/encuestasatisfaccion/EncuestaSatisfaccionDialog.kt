package pe.lecordonbleu.universidadestudiante.presentation.screens.home.encuestasatisfaccion

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.AlertDialog
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.koin.compose.viewmodel.koinViewModel
import pe.lecordonbleu.universidadestudiante.core.theme.IlcbGrisNeutro
import pe.lecordonbleu.universidadestudiante.data.remote.dto.ListPreguntasEncuesta
import pe.lecordonbleu.universidadestudiante.data.remote.dto.ResponseEncuestaSatisfaccion
import pe.lecordonbleu.universidadestudiante.data.remote.dto.ResponseGuardarEncuestaSatisfaccion
import pe.lecordonbleu.universidadestudiante.getColorsTheme
import pe.lecordonbleu.universidadestudiante.getSettingsStorage
import pe.lecordonbleu.universidadestudiante.presentation.screens.home.encuestasatisfaccion.customcell.PreguntaCheckCell
import pe.lecordonbleu.universidadestudiante.presentation.screens.home.encuestasatisfaccion.customcell.PreguntaOptionCell
import pe.lecordonbleu.universidadestudiante.presentation.screens.home.encuestasatisfaccion.customcell.PreguntaOptionLinealCell
import pe.lecordonbleu.universidadestudiante.presentation.screens.home.encuestasatisfaccion.customcell.PreguntaTextoCell
import pe.lecordonbleu.universidadestudiante.presentation.screens.home.encuestasatisfaccion.enums.TipoAlerta
import pe.lecordonbleu.universidadestudiante.presentation.screens.home.encuestasatisfaccion.mappers.EncabezadoMapper
import pe.lecordonbleu.universidadestudiante.presentation.screens.home.encuestasatisfaccion.mappers.EncuestaMapper
import pe.lecordonbleu.universidadestudiante.presentation.screens.home.encuestasatisfaccion.mappers.PreguntasMapper
import pe.lecordonbleu.universidadestudiante.presentation.screens.home.encuestasatisfaccion.helpers.PreguntaVM
import pe.lecordonbleu.universidadestudiante.presentation.screens.home.encuestasatisfaccion.helpers.RespuestaUsuario
import pe.lecordonbleu.universidadestudiante.presentation.screens.home.encuestasatisfaccion.enums.TipoPreguntaEncuesta
import pe.lecordonbleu.universidadestudiante.presentation.vo.ResourceUiState
import pe.lecordonbleu.universidadestudiante.util.renderHtmlToFormattedText

@Composable
fun EncuestaSatisfaccionDialog(
    idPeracad: Int,
    idEstudPe: Int,
    idServ: Int,
    idOacadArranque: Int,
    idUsuario: Int,
    onDismiss: () -> Unit,
    viewModel: EncuestaSatisfaccionViewModel = koinViewModel()
) {
    val settings = getSettingsStorage()
    val idSistema =  settings.getInt("idSistema", 0)
    val colors = getColorsTheme()

    val uiState by viewModel.uiStateEncuesta.collectAsStateWithLifecycle()
    val uiStateGuardar by viewModel.uiStateGuardar.collectAsStateWithLifecycle()

    var preguntasRaw by remember { mutableStateOf<List<ListPreguntasEncuesta>>(emptyList()) }
    var preguntas by remember { mutableStateOf<List<PreguntaVM>>(emptyList()) }
    var respuestas by remember { mutableStateOf<Map<Int, List<RespuestaUsuario>>>(emptyMap()) }
    var habilitadas by remember { mutableStateOf<Map<Int, Boolean>>(emptyMap()) }
    var showAlert by remember { mutableStateOf(false) }
    var alertMensaje by remember { mutableStateOf("") }
    var tipoAlerta by remember { mutableStateOf<TipoAlerta?>(null) }
    var isLoading by remember { mutableStateOf(false) }
    var ultimaCategoriaMostrada: String? = null

    LaunchedEffect(Unit) {
        viewModel.initIfNeeded(idPeracad, idEstudPe, idServ, idOacadArranque)
    }

    when (uiState) {
        is ResourceUiState.Success -> {
            isLoading = false
            val data = (uiState as ResourceUiState.Success<ResponseEncuestaSatisfaccion>).data
            val rows = data.ListPreguntasEncuesta
            if (preguntasRaw.isEmpty() && rows.isNotEmpty()) {
                preguntasRaw = rows
            }
            val mapeadas = PreguntasMapper.mapear(rows)
            if (preguntas != mapeadas) {
                preguntas = mapeadas
                habilitadas = calcularHabilitadasEnc(mapeadas, respuestas.flattenToMapEnc())
            }
        }
        is ResourceUiState.Loading -> {
            isLoading = true
            preguntas = emptyList()
        }
        is ResourceUiState.Error -> {
            isLoading = false
            preguntas = emptyList()
        }
        ResourceUiState.Empty -> {
            preguntas = emptyList()
        }
    }

    when (uiStateGuardar) {
        is ResourceUiState.Loading -> { isLoading = true }
        is ResourceUiState.Success -> {
            isLoading = false
            val resp = (uiStateGuardar as ResourceUiState.Success<ResponseGuardarEncuestaSatisfaccion>).data
            if (!showAlert) {
                alertMensaje = resp.ListEncuestaSatisfaccion.firstOrNull()?.mensaje ?: "Encuesta guardada"
                tipoAlerta = TipoAlerta.EXITO
                showAlert = true
            }
        }
        is ResourceUiState.Error -> {
            isLoading = false
            val msg = (uiStateGuardar as ResourceUiState.Error).message
            if (!showAlert) {
                alertMensaje = msg
                tipoAlerta = TipoAlerta.ERROR
                showAlert = true
            }
        }
        ResourceUiState.Empty -> {}
    }

    if (showAlert) {
        AlertDialog(
            onDismissRequest = {},
            backgroundColor = colors.colorExpenseItem,
            shape = RoundedCornerShape(22.dp),
            title = {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = when (tipoAlerta) {
                            TipoAlerta.EXITO -> "Encuesta enviada"
                            TipoAlerta.ERROR -> "No se pudo guardar"
                            else -> "Completa la encuesta"
                        },
                        fontWeight = FontWeight.Bold,
                        color = colors.colorAzulContraste,
                        fontSize = 18.sp,
                        textAlign = TextAlign.Center
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    Text(
                        text = if (tipoAlerta == TipoAlerta.EXITO) "OK" else "!",
                        fontWeight = FontWeight.Bold,
                        color = colors.colorAzulContraste,
                        fontSize = 15.sp,
                        textAlign = TextAlign.Center
                    )
                }
            },
            text = {
                Box(
                    modifier = Modifier.fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = alertMensaje,
                        color = colors.textColor,
                        textAlign = TextAlign.Center,
                        lineHeight = 18.sp
                    )
                }
            },
            buttons = {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 20.dp, end = 20.dp, bottom = 20.dp),
                    horizontalArrangement = Arrangement.Center
                ) {
                    Button(
                        onClick = {
                            when (tipoAlerta) {
                                TipoAlerta.EXITO -> { viewModel.resetGuardarState(); onDismiss() }
                                else -> { showAlert = false }
                            }
                        },
                        colors = ButtonDefaults.buttonColors(backgroundColor = colors.primary, contentColor = Color.White),
                        shape = RoundedCornerShape(16.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) { Text("Aceptar", fontWeight = FontWeight.Bold) }
                }
            }
        )
    }

    Dialog(
        onDismissRequest = {},
        properties = DialogProperties(
            usePlatformDefaultWidth = false,
            dismissOnBackPress = false,
            dismissOnClickOutside = false
        )
    ) {
        Surface(
            modifier = Modifier
                .fillMaxWidth(0.95f)
                .fillMaxHeight(0.92f),
            shape = RoundedCornerShape(26.dp),
            color = colors.colorExpenseItem,
            elevation = 8.dp
        ) {
            Box(
                Modifier
                    .fillMaxSize()
                    .background(
                        brush = Brush.verticalGradient(
                            colors = listOf(
                                colors.colorBlancoGris,
                                colors.colorExpenseItem
                            )
                        )
                    )
            ) {
                if (isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier.align(Alignment.Center),
                        color = colors.primary
                    )
                } else {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(start = 16.dp, end = 16.dp, top = 16.dp, bottom = 92.dp)
                            .verticalScroll(rememberScrollState())
                    ) {
                        Surface(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(22.dp),
                            color = colors.primary.copy(alpha = 0.08f),
                            elevation = 0.dp
                        ) {
                            Column(
                                modifier = Modifier.padding(horizontal = 18.dp, vertical = 18.dp)
                            ) {
                                Text(
                                    text = "Encuesta de Satisfacción",
                                    fontSize = 20.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = colors.colorAzulContraste
                                )

                                Spacer(Modifier.height(6.dp))

                                Text(
                                    text = "Tu opinión nos ayuda a mejorar la experiencia académica.",
                                    fontSize = 13.sp,
                                    color = colors.textColor.copy(alpha = 0.75f),
                                    lineHeight = 18.sp
                                )
                            }
                        }

                        val encabezado = renderHtmlToFormattedText(EncabezadoMapper.obtener(preguntasRaw))
                        if (encabezado.text.isNotBlank()) {
                            Spacer(Modifier.height(14.dp))
                            Surface(
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(18.dp),
                                color = colors.colorBlancoGris,
                                elevation = 0.dp
                            ) {
                                Text(
                                    text = encabezado,
                                    fontSize = 12.sp,
                                    color = colors.textColor.copy(alpha = 0.82f),
                                    lineHeight = 18.sp,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .border(
                                            width = 1.dp,
                                            color = colors.primary.copy(alpha = 0.18f),
                                            shape = RoundedCornerShape(18.dp)
                                        )
                                        .padding(14.dp)
                                )
                            }
                        }

                        Spacer(Modifier.height(16.dp))

                        preguntas.forEach { p ->
                            val enabled = habilitadas[p.id] ?: true
                            if (p.categoria != ultimaCategoriaMostrada) {
                                ultimaCategoriaMostrada = p.categoria
                                Surface(
                                    shape = RoundedCornerShape(50.dp),
                                    color = colors.primary.copy(alpha = 0.10f),
                                    elevation = 0.dp,
                                    modifier = Modifier.padding(top = 4.dp, bottom = 10.dp)
                                ) {
                                    Text(
                                        text = p.categoria,
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = colors.colorAzulContraste,
                                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp)
                                    )
                                }
                            }
                            when (p.tipo) {
                                TipoPreguntaEncuesta.UNICA -> PreguntaOptionCell(
                                    pregunta = p,
                                    seleccionAlternativaId = respuestas[p.id]?.firstOrNull()?.idAlternativa,
                                    textoActual = respuestas[p.id]?.firstOrNull()?.texto ?: "",
                                    onAlternativaSeleccionada = { newValue ->
                                        val actual = respuestas[p.id]?.firstOrNull()?.idAlternativa
                                        if (actual == newValue) return@PreguntaOptionCell
                                        val altSel = p.opciones.find { it.id == newValue }
                                        val textoActual = respuestas[p.id]?.firstOrNull()?.texto ?: ""
                                        respuestas = respuestas.toMutableMap().apply {
                                            this[p.id] = listOf(
                                                RespuestaUsuario(p.id, newValue, if (altSel?.flagTexto == 1) textoActual else "")
                                            )
                                        }
                                        val nuevas = calcularHabilitadasEnc(preguntas, respuestas.flattenToMapEnc())
                                        aplicarHabilitadasEnc(nuevas, respuestas) { respuestas = it }
                                        habilitadas = nuevas
                                    },
                                    onTextoChanged = { newText ->
                                        val existente = respuestas[p.id]?.firstOrNull()
                                        if (existente != null) {
                                            respuestas = respuestas.toMutableMap().apply {
                                                this[p.id] = listOf(existente.copy(texto = newText))
                                            }
                                        }
                                    },
                                    enabled = enabled
                                )
                                TipoPreguntaEncuesta.TEXTO -> PreguntaTextoCell(
                                    pregunta = p,
                                    textoActual = respuestas[p.id]?.firstOrNull()?.texto ?: "",
                                    onTextoChanged = { newText ->
                                        respuestas = respuestas.toMutableMap().apply {
                                            this[p.id] = listOf(RespuestaUsuario(p.id, texto = newText))
                                        }
                                    },
                                    enabled = enabled
                                )
                                TipoPreguntaEncuesta.CHECK -> {
                                    val respuestasActuales = respuestas[p.id].orEmpty()
                                    PreguntaCheckCell(
                                        pregunta = p,
                                        seleccionadas = respuestasActuales.mapNotNull { it.idAlternativa }.toSet(),
                                        textosPorAlternativa = respuestasActuales
                                            .filter { !it.texto.isNullOrBlank() }
                                            .associate { it.idAlternativa!! to it.texto!! },
                                        onAlternativaChanged = { altId, checked ->
                                            val lista = respuestasActuales.toMutableList()
                                            if (checked) {
                                                if (lista.none { it.idAlternativa == altId }) {
                                                    lista.add(RespuestaUsuario(p.id, altId, ""))
                                                }
                                            } else {
                                                lista.removeAll { it.idAlternativa == altId }
                                            }
                                            respuestas = respuestas.toMutableMap().apply {
                                                if (lista.isEmpty()) remove(p.id) else this[p.id] = lista
                                            }
                                            val nuevas = calcularHabilitadasEnc(preguntas, respuestas.flattenToMapEnc())
                                            aplicarHabilitadasEnc(nuevas, respuestas) { respuestas = it }
                                            habilitadas = nuevas
                                        },
                                        onTextoChanged = { altId, nuevoTexto ->
                                            val actual = respuestasActuales.firstOrNull { it.idAlternativa == altId }?.texto
                                            if (actual == nuevoTexto) return@PreguntaCheckCell
                                            respuestas = respuestas.toMutableMap().apply {
                                                this[p.id] = respuestasActuales.map {
                                                    if (it.idAlternativa == altId) it.copy(texto = nuevoTexto) else it
                                                }
                                            }
                                        },
                                        enabled = enabled
                                    )
                                }
                                TipoPreguntaEncuesta.OPTIONLINEAL -> PreguntaOptionLinealCell(
                                    pregunta = p,
                                    seleccionAlternativaId = respuestas[p.id]?.firstOrNull()?.idAlternativa,
                                    onAlternativaSeleccionada = { newValue ->
                                        val actual = respuestas[p.id]?.firstOrNull()?.idAlternativa
                                        if (actual == newValue) return@PreguntaOptionLinealCell
                                        respuestas = respuestas.toMutableMap().apply {
                                            this[p.id] = listOf(RespuestaUsuario(p.id, newValue))
                                        }
                                        val nuevas = calcularHabilitadasEnc(preguntas, respuestas.flattenToMapEnc())
                                        aplicarHabilitadasEnc(nuevas, respuestas) { respuestas = it }
                                        habilitadas = nuevas
                                    },
                                    enabled = enabled
                                )
                            }
                        }

                        Spacer(Modifier.height(24.dp))
                    }
                }

                Row(
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .fillMaxWidth()
                        .background(colors.colorExpenseItem)
                        .padding(16.dp)
                ) {
                    Button(
                        onClick = onDismiss,
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(16.dp),
                        colors = ButtonDefaults.buttonColors(
                            backgroundColor = IlcbGrisNeutro,
                            contentColor = Color.White
                        )
                    ) { Text("Salir", fontWeight = FontWeight.Bold) }
                    Button(
                        onClick = {
                            val resultado = validarObligatoriasEnc(preguntas, respuestas, habilitadas)
                            if (resultado.faltantes.isNotEmpty()) {
                                alertMensaje = "Debe completar las preguntas: ${resultado.faltantes.joinToString(", ")}"
                                tipoAlerta = TipoAlerta.VALIDACION
                                showAlert = true
                            } else {
                                val req = EncuestaMapper.crear(
                                    respuestas = respuestas,
                                    preguntas = preguntasRaw,
                                    idSistema = idSistema,
                                    idEstudPe = idEstudPe,
                                    idServ = idServ,
                                    idPeracad = idPeracad,
                                    idUser = idUsuario
                                )
                                viewModel.guardar(req)
                            }
                        },
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(16.dp),
                        colors = ButtonDefaults.buttonColors(
                            backgroundColor = colors.primary,
                            contentColor = Color.White
                        )
                    ) { Text("Guardar", fontWeight = FontWeight.Bold) }
                }
            }
        }
    }
}

fun Map<Int, List<RespuestaUsuario>>.flattenToMapEnc(): Map<Int, RespuestaUsuario> =
    mapNotNull { (k, v) -> v.firstOrNull()?.let { k to it } }.toMap()

fun aplicarHabilitadasEnc(
    nuevas: Map<Int, Boolean>,
    respuestas: Map<Int, List<RespuestaUsuario>>,
    update: (Map<Int, List<RespuestaUsuario>>) -> Unit
) {
    val nuevasRespuestas = respuestas.toMutableMap()
    nuevas.filter { !it.value }.forEach { (id, _) -> nuevasRespuestas.remove(id) }
    update(nuevasRespuestas)
}

fun calcularHabilitadasEnc(
    preguntas: List<PreguntaVM>,
    respuestas: Map<Int, RespuestaUsuario>
): Map<Int, Boolean> {
    val resultado = mutableMapOf<Int, Boolean>()
    for (p in preguntas) {
        var tieneCondicion = false
        var activa = false
        for (padre in preguntas) {
            for (alt in padre.opciones) {
                if (alt.habilitaPreguntaIds.contains(p.id)) {
                    tieneCondicion = true
                    if (respuestas[padre.id]?.idAlternativa == alt.id) activa = true
                }
            }
        }
        resultado[p.id] = if (tieneCondicion) activa else true
    }
    return resultado
}

data class ResultadoValidacionEnc(
    val faltantes: List<Int> = emptyList()
)

fun validarObligatoriasEnc(
    preguntas: List<PreguntaVM>,
    respuestas: Map<Int, List<RespuestaUsuario>>,
    habilitadas: Map<Int, Boolean>
): ResultadoValidacionEnc {
    val faltantes = mutableListOf<Int>()
    for (p in preguntas) {
        if (habilitadas[p.id] == false) continue
        if (!p.obligatorio) continue
        val lista = respuestas[p.id] ?: emptyList()
        when (p.tipo) {
            TipoPreguntaEncuesta.UNICA -> {
                val r = lista.firstOrNull()
                if (r?.idAlternativa == null) {
                    faltantes.add(p.numero)
                } else {
                    val alt = p.opciones.firstOrNull { it.id == r.idAlternativa }
                    if (alt?.flagTexto == 1 && r.texto.isNullOrBlank()) faltantes.add(p.numero)
                }
            }
            TipoPreguntaEncuesta.TEXTO -> {
                if (lista.firstOrNull()?.texto?.trim().isNullOrEmpty()) faltantes.add(p.numero)
            }
            TipoPreguntaEncuesta.CHECK -> {
                if (lista.isEmpty()) {
                    faltantes.add(p.numero)
                } else {
                    for (r in lista) {
                        val alt = p.opciones.firstOrNull { it.id == r.idAlternativa }
                        if (alt?.flagTexto == 1 && r.texto.isNullOrBlank()) { faltantes.add(p.numero); break }
                    }
                }
            }
            TipoPreguntaEncuesta.OPTIONLINEAL -> {
                if (lista.firstOrNull()?.idAlternativa == null) faltantes.add(p.numero)
            }
        }
    }
    return ResultadoValidacionEnc(faltantes)
}
