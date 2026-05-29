package pe.lecordonbleu.universidadestudiante.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class AnunciosRequest(
    val id_uneg: Int,
    val id_sistema: Int,
    val id_usuario: Int
)
