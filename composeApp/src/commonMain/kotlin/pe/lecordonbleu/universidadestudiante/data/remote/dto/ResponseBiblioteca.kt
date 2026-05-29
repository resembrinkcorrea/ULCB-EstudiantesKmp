package pe.lecordonbleu.universidadestudiante.data.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class LinksBiblioteca(
    val descri_biblioteca_det: String,
    val nombre_biblioteca_cab: String,
    val imagen_biblioteca_cab: String,
    val nombre_biblioteca_det: String,
    val url_biblioteca_cab: String,
    val url_biblioteca_det: String
)

@Serializable
data class ResponseBiblioteca(
    val flag_val: Int,
    val listadoBiblioteca: List<LinksBiblioteca> = emptyList()
)
