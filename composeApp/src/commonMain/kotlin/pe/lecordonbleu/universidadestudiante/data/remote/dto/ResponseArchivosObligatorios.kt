package pe.lecordonbleu.universidadestudiante.data.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class ResponseArchivosObligatorios(
    val flag_val: Int,
    val listadoArchivosObligatorios: List<ArchivoObligatorio> = emptyList()
)

@Serializable
data class ArchivoObligatorio(
    val id_oferta_carpeta_docu: Int,
    val nombre_docu: String,
    val descripcion_docu: String,
    val extension_docu: String,
    val url_docu: String,
    val ruta_carpeta: String,
    val flag_publicado: Int,
    val flag_descargado: Int,
    val flag_leido: Int,
    val last_fec_modif: String,
    val id_carpeta_docu_estado: Int,
    val tamanio_mostrar_mas: Int,
    val contador: Int,
    val last_id_user_modif: Int
)
