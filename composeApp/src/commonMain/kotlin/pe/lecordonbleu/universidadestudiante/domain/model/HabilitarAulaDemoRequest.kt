package pe.lecordonbleu.universidadestudiante.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class HabilitarAulaDemoRequest(
    val id_sistema: Int,
    val id_usuario: Int,
    val id_hor_asis: String
)
