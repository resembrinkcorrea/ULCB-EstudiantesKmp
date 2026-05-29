package pe.lecordonbleu.universidadestudiante.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class DetalleCuentaCorrienteRequest(val id_pago: Int, val id_oper_cuota_det: Int)
