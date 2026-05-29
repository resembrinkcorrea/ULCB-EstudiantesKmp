package pe.lecordonbleu.universidadestudiante.data.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class TipoTareaAcad(
    val id_tipactacad: String,
    val tipactacad_nombre: String,
    val tipactacad_abrev: String,
    val id_nivel: String,
    val flag_det_act: String
)

@Serializable
data class ResponseTiposTareasAcad(
    val listadoTiposTareaAcad: List<TipoTareaAcad> = emptyList()
)
