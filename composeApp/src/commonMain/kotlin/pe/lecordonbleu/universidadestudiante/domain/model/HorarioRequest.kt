package pe.lecordonbleu.universidadestudiante.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class HorarioRequest(
    val id_estud_pe: Int,
    val id_oacad_arranque: Int,
    val fecha_ini: String,
    val fecha_fin: String
)
