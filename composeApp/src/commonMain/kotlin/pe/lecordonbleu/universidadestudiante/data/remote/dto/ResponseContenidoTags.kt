package pe.lecordonbleu.universidadestudiante.data.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class ResponseContenidoTags(
    val flag_val: Int,
    val ContenidoTags: List<ContenidoTags> = emptyList()
)

@Serializable
data class ContenidoTags(
    val last_fec_modif: String,
    val extension_docu: String,
    val last_id_user_modif: Int,
    val descripcion_docu: String,
    val flag_publicado: Int,
    val url_docu: String,
    val flag_descargado: Int,
    val nombre_docu: String,
    val contador: Int,
    val id_carpeta_docu_estado: Int,
    val ruta_carpeta: String,
    val tamanio_mostrar_mas: Int,
    val flag_leido: Int,
    val flag_obligatorio: Int? = null,
    val id_oferta_carpeta_det: Int,
    val id_oferta_carpeta_docu: Int
)
