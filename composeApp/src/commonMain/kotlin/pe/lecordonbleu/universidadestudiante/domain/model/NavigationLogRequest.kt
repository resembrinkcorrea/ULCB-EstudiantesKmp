package pe.lecordonbleu.universidadestudiante.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class NavigationLogRequest(
    val nomCompleto: String,
    val nombreArchivo: String,
    val perf_nombre: String,
    val divasitAulaDemo: String,
    val idUNEG: Int,
    val id_usuario: Int,
    val idPerfil: Int,
    val dato: String,
    val sistema: String,
    val ip: String,
    val flag_boton: Int,
    val nombreUNEG: String
)
