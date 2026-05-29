package pe.lecordonbleu.universidadestudiante.data.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class ListadoDetacad(
    val tipo_matric_asign_abrev: String,
    val pest_asign_nombre: String,
    val ciclo_nivel: String,
    val tipo_asign_nombre: String,
    val modal_asign_nombre: String,
    val peracad_nombre: String,
    val matric_not_prom_final: String,
    val pg_estado_resultado_nota: String,
    val asign_nombre: String
)

@Serializable
data class ResponseHistorialAcademicoAlumnoDetalle(
    val flag_val: Int,
    val listado_detacad: List<ListadoDetacad> = emptyList()
)
