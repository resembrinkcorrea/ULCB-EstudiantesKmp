package pe.lecordonbleu.universidadestudiante.data.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class ListaMatriculaValidacion(
    val monto_deuda: String? = null,
    val concepto: String? = null,
    val importe: String? = null,
    val flag_intranet: Int? = null,
    val flag_deuda: Int? = null,
    val flag_proyeccion: Int? = null,
    val msg_proyeccion: String? = null,
    val flag_fecha: Int? = null,
    val total: Int? = null,
    val id_estud_serv: Int? = null
)

@Serializable
data class ResponseListaMatriculaDeudas(
    val flag_val: Int? = null,
    val ListMatriculaValidacion: List<ListaMatriculaValidacion> = emptyList()
)
