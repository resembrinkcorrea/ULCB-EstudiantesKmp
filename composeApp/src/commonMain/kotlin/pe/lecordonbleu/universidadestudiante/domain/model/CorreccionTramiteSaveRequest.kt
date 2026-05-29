package pe.lecordonbleu.universidadestudiante.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class CorreccionTramiteSaveRequest(
    val descripcion: String,
    val id_sistema: Int,
    val id_tramite: Int,
    val id_usuario: Int,
    val id_tipo_usuario: Int,
    val idTramiteEstud: Int,
    val id_uneg: Int,
    val id_estud: Int,
    val condicion: Int
)
