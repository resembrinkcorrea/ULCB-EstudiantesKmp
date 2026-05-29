package pe.lecordonbleu.universidadestudiante.data.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class AsignaturaEstudiante(
    val id_matric: String,
    val id_matric_asig_secc: String,
    val id_pest_det_asign: String,
    val id_oad_seccion: String,
    val id_modulod_pestd: String?,
    val id_oacad_arranque: String?,
    val id_hor: String?,
    val pest_asign_nombre: String,
    val peda_url_imagen: String?,
    val pest_det_asis_min: String,
    val total_max_inas: String,
    val matric_asig_porc_inasistencia: String
)

@Serializable
data class ResponseAsignaturaEstudiante(
    val flag_val: Int,
    val asignatura: List<AsignaturaEstudiante> = emptyList()
)
