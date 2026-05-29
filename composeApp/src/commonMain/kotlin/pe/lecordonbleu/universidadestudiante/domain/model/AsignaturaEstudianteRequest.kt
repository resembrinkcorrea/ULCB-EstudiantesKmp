package pe.lecordonbleu.universidadestudiante.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class AsignaturaEstudianteRequest(val id_estud_pe: Int, val id_peracad: Int)
