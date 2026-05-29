package pe.lecordonbleu.universidadestudiante.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class MarcarRequest(
    val id_unidad_negocio: Int,
    val id_usuario: Int,
    val id_hor_asis: Int,
    val id_estud_pe: Int,
    val id_sistema: Int
)
