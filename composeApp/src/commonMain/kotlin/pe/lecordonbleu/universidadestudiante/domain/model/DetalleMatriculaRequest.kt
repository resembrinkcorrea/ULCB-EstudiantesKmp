package pe.lecordonbleu.universidadestudiante.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class DetalleMatriculaRequest(
    val idOfertaAcadDet: Int,
    val id_asign_det_cr: String,
    val id_hora_dia: String,
    val id_dia_semana: String,
    val hora_ini_cr: String,
    val hora_fin_cr: String
)
