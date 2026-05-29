package pe.lecordonbleu.universidadestudiante.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class DocumentosCreadosRequest(
    val idTramite: Int,
    val id_sistema: Int,
    val idEstado: Int,
    val fechaInicio: String,
    val idUsuario: Int,
    val id_tipo_usuario: Int,
    val idUNEG: Int,
    val idTipoTramite: Int,
    val condicion: Int,
    val fechaFin: String,
    val id_estud: Int,
    val idTipoServa: Int
)
