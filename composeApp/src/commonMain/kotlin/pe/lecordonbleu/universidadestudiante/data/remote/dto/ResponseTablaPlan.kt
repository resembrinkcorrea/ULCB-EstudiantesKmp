package pe.lecordonbleu.universidadestudiante.data.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class ListTablaPlanEstudio(
    val ESTADO_MALLA: String,
    val CICLO_ACADÉMICO: Int,
    val TIPO_ASIGNATURA: String,
    val PERIODOS_CURSADOS: String,
    val ASIGNATURA: String,
    val PROMEDIO_FINAL: String,
    val ESTADO_ASIGNATURA: String,
    val CREDITOS: Int,
    val CLASE: String,
    val PREREQUISITO: String
)

@Serializable
data class ResponseTablaPlan(
    val ListTablaPlanEstudio: List<ListTablaPlanEstudio> = emptyList(),
    val flag_val: Int
)
