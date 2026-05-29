package pe.lecordonbleu.universidadestudiante.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class HistorialAcademicoAlumnoRequest(
    val id_estud_serv: String,
    val id_estud: String,
    val ped_estado_reg: String
)
