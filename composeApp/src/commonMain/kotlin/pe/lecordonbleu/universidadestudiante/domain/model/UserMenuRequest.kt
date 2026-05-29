package pe.lecordonbleu.universidadestudiante.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class UserMenuRequest(
    val id_uneg: Int,
    val id_sistema: Int,
    val id_perfil: Int
)
