package pe.lecordonbleu.universidadestudiante.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class FcmTokenRequest(
    val id_estud: Int,
    val token_app: String
)
