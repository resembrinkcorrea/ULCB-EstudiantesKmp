package pe.lecordonbleu.universidadestudiante.domain.model

import kotlinx.serialization.Serializable

@Serializable
class TramiteDocFiltroRequest(
    val id_uneg: Int,
    val id_estud: Int,
    val tipoCombo: String,
    val idEstado: Int,
    val idTipoTramite: Int,
    val idTramite: Int,
    val fechaInicio: Long,
    val fechaFin: Long,
    val idTramiteEstud: Int,
    val idTramiteDt: Int,
    val idTipoServa: Int,
    val id_sistema: Int,
    val cantidadMultiple: Int,
    val id_pest_det: Int,
    val id_estud_pe: Int,
    val id_estud_serv: Int
)
