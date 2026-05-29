package pe.lecordonbleu.universidadestudiante.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class ContenidoTagsRequest(
    val id_uneg: Int,
    val id_oferta_carpeta_det: Int,
    val id_usuario: Int
)
