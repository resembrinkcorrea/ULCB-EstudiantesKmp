package pe.lecordonbleu.universidadestudiante.data.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class ListEncuestaData(
    val id_serv: Int = 0,
    val id_pest_det: Int = 0,
    val id_exam_cab: Int = 0,
    val id_procdet: Int = 0,
    val id_exam_preg: Int = 0,
    val exam_preg_nombre: String = "",
    val examen_preg_alter_abrev: Int = 0,
    val escala_minima: String = "",
    val escala_maxima: String = "",
    val id_examen_preg_alter: Int = 0,
    val cod_examen_preg_alter: String = "",
    val id_alternativa: Int = 0,
    val id_banc_preg: Int = 0,
    val cod_orden: String = "",
    val flag_correcta: Int = 0,
    val puntaje: Int = 0,
    val msg_encabezado_enc: String = "",
    val id_exam_serv: Int = 0
)

@Serializable
data class ResponseListarEncuesta(
    val flag_val: Int = 0,
    val ListEncuestaData: List<ListEncuestaData> = emptyList()
)
