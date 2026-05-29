package pe.lecordonbleu.universidadestudiante.data.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class ResponseValidarEgresado(
    val flag_val: Int = 0,
    val ValidarTramite: List<EgresadoValidacionItem>? = emptyList()
)

@Serializable
data class EgresadoValidacionItem(
    val icono: String = "",
    val tipo: String = "",
    val contador: Int = 0,
    val titulo: String = "",
    val esCorrecto: String = "",
    val mensaje: String = ""
)
