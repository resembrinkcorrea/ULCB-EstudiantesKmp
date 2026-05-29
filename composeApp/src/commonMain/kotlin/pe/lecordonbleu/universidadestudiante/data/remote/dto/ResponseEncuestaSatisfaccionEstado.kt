package pe.lecordonbleu.universidadestudiante.data.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class EstadoEncuestaSatisfaccion(
    val valida_encuesta: Int = 0,
    val contador: Int = 0
)

@Serializable
data class ResponseEncuestaSatisfaccionEstado(
    val flag_val: Int = 0,
    val EstadoEncuesta: List<EstadoEncuestaSatisfaccion> = emptyList()
)
