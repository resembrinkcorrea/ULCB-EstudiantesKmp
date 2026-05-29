package pe.lecordonbleu.universidadestudiante.data.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class Horario(
    val id_aula: Int,
    val id_hor: Int,
    val oad_seccion_nombre: String,
    val aula_nombre: String,
    val id_dia_semana: String,
    val sede: String,
    val hora_inicio: String,
    val hora: String,
    val hor_asis_dia: String,
    val hora_fin: String,
    val docente: String,
    val pest_asign_nombre: String,
    val tipo_dictado_nombre: String,
    val dia_semana_nombre: String,
    val tipaula_dictado: String
)

@Serializable
data class ResponseHorario(
    val flag_val: Int,
    val listadoHorario: List<Horario> = emptyList()
)
