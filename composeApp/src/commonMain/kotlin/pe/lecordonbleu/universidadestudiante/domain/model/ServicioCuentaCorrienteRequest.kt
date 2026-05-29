package pe.lecordonbleu.universidadestudiante.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class ServicioCuentaCorrienteRequest(val id_estud: Int, val id_uneg: Int)
