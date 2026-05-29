package pe.lecordonbleu.universidadestudiante.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class AsignaturaEncuestaRequest(
    val id_peracad: Int,
    val id_estud_pe: Int,
    val id_serv: Int,
    val id_oacad_arranque: Int
)
