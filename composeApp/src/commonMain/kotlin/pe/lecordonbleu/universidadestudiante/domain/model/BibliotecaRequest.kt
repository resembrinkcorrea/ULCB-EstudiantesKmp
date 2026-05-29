package pe.lecordonbleu.universidadestudiante.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class BibliotecaRequest(
    val uneg: Int,
    val id_tipo_usuario: Int
)
