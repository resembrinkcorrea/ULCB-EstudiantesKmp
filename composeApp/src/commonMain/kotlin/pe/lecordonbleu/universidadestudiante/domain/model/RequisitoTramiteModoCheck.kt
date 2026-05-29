package pe.lecordonbleu.universidadestudiante.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class RequisitoTramiteModoCheck(
    val id_asignatura: Int,
    val id_tramite_req_doc: Int,
    val requisito_nombre: String,
    val valorinput: Boolean,
    val cumplio: Int
)
