package pe.lecordonbleu.universidadestudiante.data.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class ResponseTramiteDocFiltro(
    val Estados: List<EstadoTramite> = emptyList(),
    val TipoTramite: List<TipoTramite> = emptyList(),
    val Tramite: List<Tramite> = emptyList(),
    val TipoEntrega: List<TipoEntrega> = emptyList(),
    val flag_val: Int
)

@Serializable
data class EstadoTramite(
    val id_tipo_paragene: String,
    val paragene_nombre: String,
    val contador: Int,
    val id_paragene: String,
    val tipo_paragene_nombre: String,
    val paragene_abrev: String,
    val tipo_paragene_abrev: String
)

@Serializable
data class TipoTramite(
    val contador: Int,
    val id: String,
    val nombre: String
)

@Serializable
data class Tramite(
    val tipo: String,
    val contador: Int,
    val id: String,
    val nombre: String
)

@Serializable
data class TipoEntrega(
    val contador: Int,
    val modal_presencial: String,
    val id: String,
    val nombre: String
)
