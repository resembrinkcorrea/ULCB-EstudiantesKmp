package pe.lecordonbleu.universidadestudiante.data.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class TrcTramiteItem(
    val contador: Int = 0,
    val descripcion: String = "",
    val accionHtml: String = "",
    val estadoHtml: String = ""
)
