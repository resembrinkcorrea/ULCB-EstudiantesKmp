package pe.lecordonbleu.universidadestudiante.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class ComprobanteTramiteRequest(val comprobante: String, val idUnidadNegocio: Int)
