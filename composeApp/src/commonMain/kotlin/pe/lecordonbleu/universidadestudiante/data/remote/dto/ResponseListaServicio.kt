package pe.lecordonbleu.universidadestudiante.data.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class ListarServicio(
    val contador: Int,
    val id_pest_det: Int,
    val id_serv: Int,
    val flag_carrera: Int,
    val id_estud_serv: Int,
    val id_tiposerva: Int,
    val id_uneg: Int,
    val serv_nombre: String,
    val id_estud: Int,
    val serv_abrev: String
)

@Serializable
data class ResponseListaServicio(
    val flag_val: Int,
    val ListarServicio: List<ListarServicio> = emptyList()
)
