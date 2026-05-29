package pe.lecordonbleu.universidadestudiante.data.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class HistorialNotasItem(
    val peracad_nombre: String,
    val ciclo_nivel: String,
    val pest_asign_nombre: String,
    val tipo_asign_nombre: String,
    val credito: String,
    val matric_not_flag_aprobado: String,
    val matric_not_prom_final: String,
    val tipo_matric_asign_abrev: String
)

@Serializable
data class ResponseHistorialNotas(
    val flag_val: Int,
    val listadoNotas: List<HistorialNotasItem> = emptyList()
)
