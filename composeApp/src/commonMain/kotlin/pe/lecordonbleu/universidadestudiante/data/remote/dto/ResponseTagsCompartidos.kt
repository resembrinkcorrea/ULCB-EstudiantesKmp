package pe.lecordonbleu.universidadestudiante.data.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class ResponseTagsCompartidos(
    val flag_val: Int,
    val TagsCompartidosEstudiante: List<TagsCompartidosEstudiante> = emptyList()
)

@Serializable
data class TagsCompartidosEstudiante(
    val nombre_oferta_cab: String,
    val orden_oferta_cab: Int,
    val orden_oferta_det: Int,
    val contador: Int,
    val nombre_oferta_det: String,
    val flag_publicado: Int,
    val flag_publicado_cab: Int,
    val id_oferta_carpeta_det: Int,
    val id_oferta_carpeta_cab: Int
)
