package pe.lecordonbleu.universidadestudiante.data.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class ResponseDataMenu(
    val data_menu: List<DataMenu>
)
