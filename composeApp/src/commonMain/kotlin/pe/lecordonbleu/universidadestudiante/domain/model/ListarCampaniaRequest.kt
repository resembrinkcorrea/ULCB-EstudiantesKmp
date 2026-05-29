package pe.lecordonbleu.universidadestudiante.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class ListarCampaniaRequest(val id_oacad_arranque: Int)
