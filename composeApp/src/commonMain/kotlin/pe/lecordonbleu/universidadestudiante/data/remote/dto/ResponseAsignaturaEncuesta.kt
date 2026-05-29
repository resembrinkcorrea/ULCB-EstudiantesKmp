package pe.lecordonbleu.universidadestudiante.data.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class ListAsignaturaEncuesta(
    val oad_seccion_nombre: String = "",
    val id_hor_det: String = "",
    val pest_asign_nombre: String = "",
    val id_aula: String = "",
    val id_oad_seccion: Int = 0,
    val id_pest_det_asign: Int = 0,
    val id_docente: Int = 0,
    val flag_registrado: Int = 0,
    val docente: String = "",
    val id_oacad_det: Int = 0,
    val id_hor: Int = 0
)

@Serializable
data class ResponseAsignaturaEncuesta(
    val flag_val: Int = 0,
    val ListAsignaturaEncuesta: List<ListAsignaturaEncuesta> = emptyList()
)
