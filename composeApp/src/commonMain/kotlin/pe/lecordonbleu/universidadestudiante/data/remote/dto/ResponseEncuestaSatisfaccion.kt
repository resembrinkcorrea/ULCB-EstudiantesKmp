package pe.lecordonbleu.universidadestudiante.data.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class ResponseEncuestaSatisfaccion(
    val flag_val: Int = 0,
    val EstadoEncuesta: List<EstadoEncuesta> = emptyList(),
    val ListPreguntasEncuesta: List<ListPreguntasEncuesta> = emptyList()
)

@Serializable
data class EstadoEncuesta(
    val valida_encuesta: Int = 0,
    val contador: Int = 0
)

@Serializable
data class ListPreguntasEncuesta(
    val id_exam_preg: Int = 0,
    val nro_pregunta: Int = 0,
    val exam_preg_nombre: String = "",
    val id_tipo_preg: Int = 0,
    val tipo_preg_valor: Int = 0,
    val id_examen_preg_alter: Int = 0,
    val examen_preg_alter_abrev: String? = null,
    val flag_texto: Int = 0,
    val id_exam_preg_condicion: String = "",
    val preg_obligatorio: Int = 0,
    val categoria_nombre: String = "",
    val msg_encabezado_enc: String = "",
    val id_exam_serv: Int = 0,
    val id_pest_det: Int = 0,
    val id_categoria: Int = 0,
    val id_peracad: Int = 0,
    val id_serv: Int = 0,
    val id_procdet: Int = 0,
    val escala_minima: String = "",
    val escala_maxima: String = "",
    val flag_correcta: Int? = null,
    val id_alternativa: Int? = null,
    val puntaje: Int? = null,
    val contador: Int = 0,
    val id_exam_cab: Int = 0,
    val cod_examen_preg_alter: String? = null,
    val alt_cod_orden: String? = null,
    val cod_orden: String? = null,
    val id_banc_preg: Int? = null,
    val tipo_preg_nombre: String = ""
)
