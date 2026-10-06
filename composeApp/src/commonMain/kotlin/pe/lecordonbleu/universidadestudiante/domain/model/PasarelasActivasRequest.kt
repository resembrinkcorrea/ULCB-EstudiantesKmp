package pe.lecordonbleu.universidadestudiante.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class PasarelasActivasRequest(val condicion: Int,val id_uneg: Int, val id_sistema: Int, val id_usuario: Int)
