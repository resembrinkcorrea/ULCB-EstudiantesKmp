package pe.lecordonbleu.universidadestudiante.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class EstudianteOAcadConvalidacionRequest(
    val condicion: Int,
    val id_uneg: Int,
    val id_tipo_traslado: Int
)
