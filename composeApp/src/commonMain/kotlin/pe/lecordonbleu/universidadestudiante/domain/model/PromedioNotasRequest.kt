package pe.lecordonbleu.universidadestudiante.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class PromedioNotasRequest(
    val id_estud_pe: Int,
    val uneg: Int
)
