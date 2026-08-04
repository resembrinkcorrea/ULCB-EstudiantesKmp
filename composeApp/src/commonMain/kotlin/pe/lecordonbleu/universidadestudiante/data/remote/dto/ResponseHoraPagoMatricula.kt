package pe.lecordonbleu.universidadestudiante.data.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class ListTurnoPago(
    val horario: String,
    val matric_fin: String ,
    val matric_inicio: String,
    val id_grmatricd: Int,
    val estado: Int
)

@Serializable
data class ResponseHoraPagoMatricula(
    val flag_val: Int ,
    val ListTurnoPago: List<ListTurnoPago> = emptyList()
)
