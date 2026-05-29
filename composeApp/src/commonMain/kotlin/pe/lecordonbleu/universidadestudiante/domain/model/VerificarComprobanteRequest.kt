package pe.lecordonbleu.universidadestudiante.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class VerificarComprobanteRequest(val comprobante: String, val idUnidadNegocio: Int)
