package pe.lecordonbleu.universidadestudiante.data.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class ListadoNotas(
    val pest_asign_nombre: String,
    val estado_matric: String,
    val tipo_matric_asign_abrev: String,
    val matric_not_flag_aprobado: String,
    val matric_not_prom_final: String,
    val id_matric_not: String,
    val nota_min: String,
    val matric_not_prac_calif: String,
    val matric_not_exam_parc: String,
    val matric_not_exam_final: String,
    val peso_pract_calif: String,
    val peso_exam_parcial: String,
    val peso_exam_final: String,
    val flag_pract_calif: String,
    val flag_exam_parcial: String,
    val flag_exam_final: String
)

@Serializable
data class ResponseCursos(
    val flag_val: Int,
    val listadoNotas: List<ListadoNotas> = emptyList()
)
