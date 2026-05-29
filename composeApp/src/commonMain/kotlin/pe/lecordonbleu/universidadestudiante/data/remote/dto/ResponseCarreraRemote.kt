package pe.lecordonbleu.universidadestudiante.data.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class CarreraRemote(
    val id_serv: String,
    val id_estud_serv: String,
    val id_tiposerva: String,
    val id_uneg: String,
    val serv_nombre: String,
    val id_estud: String,
    val ped_url_imagen: String,
    val flag_carrera: String,
    val id_pest_det: String,
    val id_estud_pe: Int
)

@Serializable
data class ResponseCarreraRemote(
    val flag_val: Int,
    val carrera: List<CarreraRemote> = emptyList()
)
