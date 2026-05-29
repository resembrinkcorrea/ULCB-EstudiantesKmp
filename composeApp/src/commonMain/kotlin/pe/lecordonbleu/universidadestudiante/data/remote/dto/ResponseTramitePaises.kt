package pe.lecordonbleu.universidadestudiante.data.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class ResponseTramitePaises(
    val flag_val: Int,
    val ListPaises: List<ListPaises> = emptyList()
)

@Serializable
data class ListPaises(
    val pais_nombre: String,
    val contador: Int,
    val pais_codreg: String,
    val cod_pais: String,
    val pais_prefijo: String,
    val id_empresa: String,
    val id_pais: String,
    val pais_default: String,
    val id_pais_crm: String
)
