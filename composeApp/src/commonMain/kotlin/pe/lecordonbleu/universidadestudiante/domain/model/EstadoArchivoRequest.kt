package pe.lecordonbleu.universidadestudiante.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class EstadoArchivoRequest(
    val id_uneg: Int,
    val id_usuario: Int,
    val id_carpeta_docu_estado: Int,
    val flag_leido: Int
)
