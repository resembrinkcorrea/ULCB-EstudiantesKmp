package pe.lecordonbleu.universidadestudiante.data.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class ResponseTemporalCuentaCorriente(
    val flag_val: Int,
    val mensaje: String,
    val cod_transaccion: String
)
