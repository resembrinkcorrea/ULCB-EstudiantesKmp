package pe.lecordonbleu.universidadestudiante.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class ObtenerEstudianteMatriculaRequest(
    val id_estud: Int,
    val id_tiposerva: Int
)
