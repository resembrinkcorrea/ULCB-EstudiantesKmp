package pe.lecordonbleu.universidadestudiante.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class ResumenHistoricoRequest(val idEstudPe: Int, val idPeriodoAcadVal: Int)
