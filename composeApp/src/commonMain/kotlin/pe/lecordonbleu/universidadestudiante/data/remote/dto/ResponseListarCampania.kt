package pe.lecordonbleu.universidadestudiante.data.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class ListCampania(
    val id_camp_desc: Int,
    val camp_desc_nombre: String
)

@Serializable
data class ResponseListarCampania(
    val flag_val: Int,
    val ListCampania: List<ListCampania> = emptyList()
)
