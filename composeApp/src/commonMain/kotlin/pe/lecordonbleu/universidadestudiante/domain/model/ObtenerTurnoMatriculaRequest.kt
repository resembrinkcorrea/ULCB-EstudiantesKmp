package pe.lecordonbleu.universidadestudiante.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class ObtenerTurnoMatriculaRequest(
    val promedio_ponderado: Double,
    val id_pest_det: Int,
    val estado_ingresante: Int,
    val uneg: Int,
    val id_estud_serv: Int,
    val id_peracad: Int
)
