package pe.lecordonbleu.universidadestudiante.data.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class ListadoAsistencia(
    val hor_fin: String,
    val id_hor_asis: String,
    val sesion: String,
    val fecha_dia: String,
    val hor_asis_dia: String,
    val hora_marcacion: String,
    val docente: String,
    val asistio: String,
    val hor_inicio: String,
    val dia: String,
    val clase: String
)

@Serializable
data class ResponseAsistencia(
    val flag_val: Int,
    val listadoCarrera: List<ListadoAsistencia> = emptyList()
)
