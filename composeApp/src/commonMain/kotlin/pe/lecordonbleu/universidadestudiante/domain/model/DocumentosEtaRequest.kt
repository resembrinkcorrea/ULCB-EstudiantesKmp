package pe.lecordonbleu.universidadestudiante.domain.model

import kotlinx.serialization.Serializable

@Serializable
class DocumentosEtaRequest(
    private val id_pest_det: Int,
    private val id_uneg: Int,
    private val id_oacad_arranque: Int,
    private val id_estud_serv: Int,
    private val id_oaa_pcs: Int
)
