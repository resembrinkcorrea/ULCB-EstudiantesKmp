package pe.lecordonbleu.universidadestudiante.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class ValidarInicioMatriculaRequest(
    val id_grmatricd: Int,
    val id_oacad_arranque: Int,
    val id_estud_pe: Int
)
