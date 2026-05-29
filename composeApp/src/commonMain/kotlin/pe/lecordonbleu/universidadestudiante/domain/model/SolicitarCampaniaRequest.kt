package pe.lecordonbleu.universidadestudiante.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class SolicitarCampaniaRequest(
    val id_oper: Int,
    val id_estud_pe: Int,
    val id_user: Int,
    val id_camp_desc: Int
)
