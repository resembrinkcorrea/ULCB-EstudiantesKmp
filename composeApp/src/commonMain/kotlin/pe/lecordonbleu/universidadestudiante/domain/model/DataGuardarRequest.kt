package pe.lecordonbleu.universidadestudiante.domain.model

import kotlinx.serialization.Serializable

@Serializable
class DataGuardarRequest(
    private val id_uneg: Int,
    private val pdfbase64: String,
    private val id_estud: String,
    private val id_user: String,
    private val id_pcs_estud: String,
    private val id_pcs_docu: String,
    private val nombreDocAbrev: String,
    private val pcs_estud_nombre: String,
    private val id_pcs_estud_exam: String,
    private val id_sistema: String
)
