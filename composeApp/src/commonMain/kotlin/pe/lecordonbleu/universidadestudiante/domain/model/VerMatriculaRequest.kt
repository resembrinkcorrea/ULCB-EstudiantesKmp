package pe.lecordonbleu.universidadestudiante.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class VerMatriculaRequest(
    val idPeriodoAcad: Int,
    val idServicio: Int,
    val idPlanEstudioDet: Int,
    val idEstudiante: Int,
    val id_sistema: Int,
    val uneg: Int,
    val id_usuario: Int
)
