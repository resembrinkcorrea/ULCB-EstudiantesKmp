package pe.lecordonbleu.universidadestudiante.data.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class Carrera(
    val id_serv: String,
    val id_estud_serv: String,
    val id_tiposerva: String,
    val id_uneg: String,
    val serv_nombre: String,
    val id_estud: String,
    val ped_url_imagen: String,
    val flag_carrera: String,
    val id_pest_det: String
)

@Serializable
data class ResponseCarrera(
    val flag_val: Int,
    val carrera: List<Carrera> = emptyList()
)
