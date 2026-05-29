package pe.lecordonbleu.universidadestudiante.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class GuardarArchivoTramiteRequest(
    val id_uneg: Int,
    val id_estud: Int,
    val image64: String,
    val pdfbase64: String,
    val extFile: String,
    val nombreDocAbrev: String,
    @Serializable(with = RequisitosSealedAsArraySerializer::class)
    val requisitos: RequisitosTramiteSealed
)
