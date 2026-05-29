package pe.lecordonbleu.universidadestudiante.data.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class ListaDeudaCuentaCorriente(
    val estado: Int,
    val mensaje: String
)

@Serializable
data class ResponseDeudasCuentasCorrientes(
    val flag_val: Int,
    val ListaDeudaCuentaCorriente: List<ListaDeudaCuentaCorriente> = emptyList()
)
