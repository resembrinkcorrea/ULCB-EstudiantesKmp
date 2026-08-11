package pe.lecordonbleu.universidadestudiante.data.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class PasarelaActiva(
    val id_medio_pago: Int? = null,
    val medio_pago_nombre: String? = null,
    val metodo: String? = null,
    val activo: Int
)

@Serializable
data class ResponsePasarelasActivas(
    val flag_val: Int,
    val pasarelas: List<PasarelaActiva> = emptyList()
)
