package pe.lecordonbleu.universidadestudiante.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class TextosHtmlRequest(val idUneg: Int, val id_usuario: Int)
