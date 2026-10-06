package pe.lecordonbleu.universidadestudiante.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class ArchivosObligatoriosRequest(
    val id_uneg: Int,
    val id_estud: Int,
    val id_serv: Int,
    val id_usuario: Int,
    val id_tipo_usuario: Int
)
