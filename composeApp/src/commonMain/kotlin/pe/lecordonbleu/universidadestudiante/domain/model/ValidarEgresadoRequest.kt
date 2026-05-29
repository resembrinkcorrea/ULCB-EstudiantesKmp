package pe.lecordonbleu.universidadestudiante.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class ValidarEgresadoRequest(
    val id_sistema: Int,
    val id_estud_pe: Int,
    val id_pest_det: Int,
    val idTramiteDt: Int,
    val id_tipo_usuario: Int,
    val id_estud_serv: Int,
    val id_uneg: Int,
    val id_estud: Int
)
