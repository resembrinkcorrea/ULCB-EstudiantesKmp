package pe.lecordonbleu.universidadestudiante.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class TagsArchivosRequest(
    val id_uneg: Int,
    val id_estud: Int,
    val id_tiposerva: Int,
    val id_serv: Int
)
