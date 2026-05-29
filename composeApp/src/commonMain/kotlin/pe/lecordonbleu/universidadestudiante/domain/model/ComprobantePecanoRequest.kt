package pe.lecordonbleu.universidadestudiante.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class ComprobantePecanoRequest(
    val TipoDocumento: Int,
    val FechaEmision: String,
    val NumeroDocumento: String
)
