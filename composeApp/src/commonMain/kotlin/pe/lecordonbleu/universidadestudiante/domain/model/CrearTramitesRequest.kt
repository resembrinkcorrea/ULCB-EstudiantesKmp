package pe.lecordonbleu.universidadestudiante.domain.model

import kotlinx.serialization.Serializable

@Serializable
class CrearTramitesRequest(
    private val id_sistema: Int,
    private val id_tiposervad: Int,
    private val id_usuario: Int,
    private val id_tipo_usuario: Int,
    private val idUNEG: Int,
    private val condicion: Int,
    private val id_estud: Int
)
