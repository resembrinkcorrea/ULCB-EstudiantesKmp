package pe.lecordonbleu.universidadestudiante.data.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class ListarTipoServicio(
    val contador: Int,
    val id_tiposerva: Int,
    val tiposerva_nombre: String
)

@Serializable
data class ResponseTipoServicio(
    val flag_val: Int,
    val ListarTipoServicio: List<ListarTipoServicio> = emptyList()
)
