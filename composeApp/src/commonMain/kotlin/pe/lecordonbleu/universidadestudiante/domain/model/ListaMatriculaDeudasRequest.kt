package pe.lecordonbleu.universidadestudiante.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class ListaMatriculaDeudasRequest(
    val idPlanEstudioDet: Int,
    val idEstudServ: Int,
    val idEstudiante: Int,
    val idPeriodoAcad: Int
)
