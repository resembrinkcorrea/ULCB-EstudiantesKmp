package pe.lecordonbleu.universidadestudiante.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class EncuestaSatisfaccionGuardarRequest(
    val id_sistema: Int,
    val Encuesta: List<EncuestaItem>
)

@Serializable
data class EncuestaItem(
    val id_user: Int,
    val id_exam_preg: Int,
    val id_exam_preg_alter: Int,
    val id_estud_pe: Int,
    val id_peracad: Int,
    val id_exam_serv: Int,
    val id_uneg: Int,
    val descripcion: String,
    val puntaje: Int,
    val id_serv: Int
)
