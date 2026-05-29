package pe.lecordonbleu.universidadestudiante.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class CursosRequest(
    val id_estud_pe: Int,
    val id_oacad_arranque: Int
)
