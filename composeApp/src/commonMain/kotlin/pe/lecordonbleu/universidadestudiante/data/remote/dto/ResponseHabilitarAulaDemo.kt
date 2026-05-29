package pe.lecordonbleu.universidadestudiante.data.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class ListClaseHabilitada(
    val icono: String,
    val tipo: Int,
    val contador: Int,
    val hora_inicio: String,
    val hora_fin: String,
    val mensaje: String,
    val notificacion: String,
    val titulo: String,
)

@Serializable
data class ResponseHabilitarAulaDemo(
    val flag_val: Int,
    val ListClaseHabilitada: List<ListClaseHabilitada>
)
