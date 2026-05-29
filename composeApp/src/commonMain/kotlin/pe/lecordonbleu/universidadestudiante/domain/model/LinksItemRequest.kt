package pe.lecordonbleu.universidadestudiante.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class LinksItemRequest(
    val id_uneg: Int,
    val id_sistema: Int
)
