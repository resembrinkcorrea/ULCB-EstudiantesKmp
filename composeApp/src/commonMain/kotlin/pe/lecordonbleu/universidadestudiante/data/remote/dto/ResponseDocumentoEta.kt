package pe.lecordonbleu.universidadestudiante.data.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class ListDocumentosEta(
    val idPcsEstud: Int,
    val idPcsEstudExam: Int,
    val estudNombre: String,
    val FECHA_VALIDACION: String,
    val contador: Int,
    val nombreDocAbrev: String,
    val ESTADO_VALIDACION: String,
    val ACCIONES: String,
    val SUBIDO: String,
    val idPcsDocu: Int,
    val FECHA_SUBIDO: String,
    val nombreDoc: String,
    val url_pcs_exam_exam: String? = ""
)

@Serializable
data class ResponseDocumentoEta(
    val flag_val: Int,
    val ListDocumentosEta: List<ListDocumentosEta> = emptyList()
)
