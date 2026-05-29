package pe.lecordonbleu.universidadestudiante.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class ListarCuentaCorrienteRequest(val id_estud_pe: Int, val id_oper: Int)
