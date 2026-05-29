package pe.lecordonbleu.universidadestudiante.data.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class ListDetMatric(
    val turno_nombre: String,
    val oad_seccion_nombre: String,
    val aula_nombre: String,
    val horario: String,
    val docente: String,
    val sede_abrev: String,
    val id_dia_semana_c: String,
    val vacantes: String,
    val matriculados: String,
    val id_oad_seccion: String,
    val flag_cruce: String,
    val msg_cruce: String,
    val cruce_nom_asign: String,
    val flag_vacantes: String,
    val id_asign_det: Int,
    val id_asign_det_cr: String,
    val id_hora_dia: String,
    val id_dia_semana: String,
    val hora_ini_cr: String,
    val hora_fin_cr: String,
    val hor: String
)

@Serializable
data class ResponseDetalleMatricula(
    val flag_val: Int,
    val list_detmatric: List<ListDetMatric> = emptyList()
)
