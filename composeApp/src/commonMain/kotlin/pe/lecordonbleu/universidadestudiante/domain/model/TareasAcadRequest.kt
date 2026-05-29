package pe.lecordonbleu.universidadestudiante.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class TareasAcadRequest(
    val id_matric_not: Int,
    val id_tipactacad: Int
)
