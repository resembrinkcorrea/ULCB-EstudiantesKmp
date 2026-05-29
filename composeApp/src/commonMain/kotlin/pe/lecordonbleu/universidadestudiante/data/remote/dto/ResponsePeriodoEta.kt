package pe.lecordonbleu.universidadestudiante.data.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class EtaPeriodo(
    val id_oaa_pcs: Int,
    val id_oacad_arranque: Int,
    val peracad_nombre: String,
    val id_peracad: Int
)

@Serializable
data class ResponsePeriodoEta(
    val flag_val: Int,
    val ListEtaPeriodo: List<EtaPeriodo> = emptyList()
)
