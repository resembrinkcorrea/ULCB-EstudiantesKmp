package pe.lecordonbleu.universidadestudiante.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class CarrerasConvalidacionRequest(
    val id_ofer_adm: Int,
    val id_serv: Int
)
