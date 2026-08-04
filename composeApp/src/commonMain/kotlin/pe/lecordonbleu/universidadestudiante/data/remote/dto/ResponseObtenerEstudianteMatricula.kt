package pe.lecordonbleu.universidadestudiante.data.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class DataObtenerMatricula(
    val id_tipmatric: Int = 0
)

@Serializable
data class ResponseObtenerEstudianteMatricula(
    val flag_val: Int = 0,
    val DataObtenerMatricula: List<DataObtenerMatricula> = emptyList()
)
