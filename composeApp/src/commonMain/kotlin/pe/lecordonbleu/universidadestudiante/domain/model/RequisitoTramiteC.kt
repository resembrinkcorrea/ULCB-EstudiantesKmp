package pe.lecordonbleu.universidadestudiante.domain.model

import kotlinx.serialization.Serializable

@Serializable
sealed class RequisitoTramiteC {
    @Serializable
    data class Doc(
        val extFile: String,
        val fileData: String,
        val multiple: String,
        val documento: String,
        val id_tramite_estud: String,
        val nombre: String,
        val id_tramite_estud_req_doc: String,
        val periodo_mat: String,
        val contador: Int,
        val fileNameDocTitle: String,
        val urlDoc: String,
        val id_tramite_req_doc: String,
        val id_tramite_estud_req: String,
        val cumplio: String,
        val requisito_nombre: String,
        val empresa: String,
        val carrera: String,
        val pdfBase64: String? = null
    ) : RequisitoTramiteC()

    @Serializable
    data class Main(
        val id_asignatura: String,
        val id_tramite_req_doc: String,
        val requisito: String,
        val valor: String,
        val cumplio: String
    ) : RequisitoTramiteC()
}
