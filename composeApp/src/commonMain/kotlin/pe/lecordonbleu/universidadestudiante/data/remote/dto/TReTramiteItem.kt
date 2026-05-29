package pe.lecordonbleu.universidadestudiante.data.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class TReTramiteItem(
    val estado: String,
    val DATOCUMPLE: String,
    val multiple: String,
    val documento: String,
    val id_tramite_estud: String,
    val id_tramite_estud_req_doc: String,
    val periodo_mat: String,
    val contador: Int,
    val id_tramite_req_doc: String,
    val requisito: String,
    val id_tramite_estud_req: String,
    val cumplio: String,
    val requisito_nombre: String,
    val empresa: String,
    val carrera: String,
    val id: Int?,
    val nombre: String?
)
