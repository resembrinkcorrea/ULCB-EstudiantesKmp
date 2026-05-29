package pe.lecordonbleu.universidadestudiante.data.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class ResponseAnuncios(
    val data_notificaciones: List<data_notificaciones>
)

@Serializable
data class data_notificaciones(
    val avisos_titulo: String,
    val avisos_asunto: String,
    val avisos_nombre: String,
    val avisos_url_imagen: String,
    val fec_ini_publi: String,
    val fec_fin_publi: String,
    val fecha_ini_vig: String,
    val fecha_fin_vig: String,
    val flag_publi: Int,
    val flag_prioritario: Int,
    val contador: Int,
    val avisos_contenido: String
)
