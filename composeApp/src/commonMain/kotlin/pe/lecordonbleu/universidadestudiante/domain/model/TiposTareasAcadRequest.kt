package pe.lecordonbleu.universidadestudiante.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class TiposTareasAcadRequest(
    val id_matric_not: Int
)
