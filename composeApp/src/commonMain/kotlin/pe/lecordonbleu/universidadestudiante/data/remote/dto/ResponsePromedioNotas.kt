package pe.lecordonbleu.universidadestudiante.data.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class ListadoNotasDetalle(
    val not_prac_calif: String,
    val foto: String,
    val flag_aprobado: String,
    val est_codigo: String,
    val not_exam_parc: String,
    val alumno: String,
    val not_exam_susti: String,
    val not_prom_final: String,
    val peso_prac_calif: String,
    val peso_exam_parc: String,
    val nota_min: String,
    val peso_exam_final: String,
    val not_exam_final: String
)

@Serializable
data class ResponsePromedioNotas(
    val flag_val: Int,
    val listadoNotasDetalle: List<ListadoNotasDetalle> = emptyList()
)
