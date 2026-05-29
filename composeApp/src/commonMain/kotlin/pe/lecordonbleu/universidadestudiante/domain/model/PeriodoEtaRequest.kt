package pe.lecordonbleu.universidadestudiante.domain.model

import kotlinx.serialization.Serializable

@Serializable
class PeriodoEtaRequest(private val id_pest_det: Int, private val id_serv: Int)
