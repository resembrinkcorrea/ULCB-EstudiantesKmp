package pe.lecordonbleu.universidadestudiante.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class DuplicadoTituloGuardarRequest(
    val id_uneg: Int,
    val id_estud: Int,
    val requisitos: RequisitosRequest
)

@Serializable
data class RequisitosRequest(
    val array: List<RequisitoItemRequest>
)

@Serializable
data class RequisitoItemRequest(
    val extFile: String,
    val fileNameDocTitle: String,
    val pdfBase64: String,
    val id_tramite_req_doc: Int,
    val contador: String,
    val nombre: String,
    val requisito_nombre: String,
    val empresa: String,
    val carrera: String,
    val multiple: String,
    val documento: String,
    val id_tramite_estud: String,
    val id_tramite_estud_req_doc: String,
    val periodo_mat: String,
    val id_tramite_estud_req: String,
    val cumplio: String
)
