package pe.lecordonbleu.universidadestudiante.data.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class ListLinksInstitucional(
    val id_sistema: Int,
    val plat_web_cab_nombre: String,
    val contador: Int,
    val id_plat_web_cab: Int,
    val plat_web_cab_abrev: String,
    val plat_web_det_orden: Int,
    val id_uneg: Int,
    val plat_web_cab_url: String,
    val plat_web_cab_imagen: String
)

@Serializable
data class ResponseLinksInstitucional(
    val flag_val: Int,
    val ListLinksInstitucional: List<ListLinksInstitucional> = emptyList()
)
