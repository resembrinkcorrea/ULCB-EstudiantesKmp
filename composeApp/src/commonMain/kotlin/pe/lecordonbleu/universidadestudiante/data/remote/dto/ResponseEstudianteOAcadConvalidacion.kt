package pe.lecordonbleu.universidadestudiante.data.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class ResponseEstudianteOAcadConvalidacion(
    val flag_val: Int,
    val ListOfertaAcademica: List<ListOfertaAcademica> = emptyList()
)

@Serializable
data class ListOfertaAcademica(
    val peracad_nombre: String? = null,
    val id_ofer_adm: Int? = null,
    val id_peracad: Int? = null,
    val cente_nombre: String? = null,
    val id_centest: Int? = null
)
