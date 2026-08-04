package pe.lecordonbleu.universidadestudiante.data.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class ObtenerTurnoMatriculaDto(
    val horario: String,
    val contador: Int,
    val matric_fin: String,
    val matric_inicio: String,
    val id_grmatricd: Int
)

@Serializable
data class ResponseObtenerTurnoMatricula(
    val flag_val: Int,
    val ObtenerTurnoMatricula: List<ObtenerTurnoMatriculaDto> = emptyList()
)
