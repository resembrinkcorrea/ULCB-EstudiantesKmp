package pe.lecordonbleu.universidadestudiante.data.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class ResponseTipoTraslado(
    val ListTipoTraslado: List<ListTipoTraslado> = emptyList(),
    val flag_val: Int
)

@Serializable
data class ListTipoTraslado(
    val contador: Int,
    val tipo_traslado_nombre: String,
    val id_tipo_traslado: Int
)
