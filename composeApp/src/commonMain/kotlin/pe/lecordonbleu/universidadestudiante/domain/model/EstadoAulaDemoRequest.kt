package pe.lecordonbleu.universidadestudiante.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class EstadoAulaDemoRequest(
    private val id_docente: Int,
    private val id_uneg: Int
)
