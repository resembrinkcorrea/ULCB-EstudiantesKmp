package pe.lecordonbleu.universidadestudiante.data.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class LogListNavigation(
    val contador: Int,
    val valido: Int
)

@Serializable
data class ResponseNavigationLog(
    val flag_val: Int,
    val LogListNavigation: List<LogListNavigation> = emptyList()
)
