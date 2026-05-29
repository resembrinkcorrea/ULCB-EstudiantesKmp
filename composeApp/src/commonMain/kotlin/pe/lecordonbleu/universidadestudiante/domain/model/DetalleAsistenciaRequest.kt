package pe.lecordonbleu.universidadestudiante.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class DetalleAsistenciaRequest(val id_estud_pe: Int, val id_matric_asig_secc: Int)
