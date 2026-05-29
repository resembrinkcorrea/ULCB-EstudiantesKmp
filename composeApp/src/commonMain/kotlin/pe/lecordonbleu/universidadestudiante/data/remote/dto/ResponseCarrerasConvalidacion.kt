package pe.lecordonbleu.universidadestudiante.data.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class ResponseCarrerasConvalidacion(
    val ListCarrerasAcademica: List<ListCarrerasAcademicaItem> = emptyList(),
    val flag_val: Int
)

@Serializable
data class ListCarrerasAcademicaItem(
    val contador: Int,
    val pest_det_nombre: String,
    val id_pest_det_destino: Int
)
