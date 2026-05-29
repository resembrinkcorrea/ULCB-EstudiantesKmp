package pe.lecordonbleu.universidadestudiante.data.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class estado_marcar(
    val limiteant_salida: String,
    val aula_nombre: String,
    val dia_semana_abrev: String,
    val flag_asis: String,
    val hora_inicio: String,
    val hor_asis_fin: String,
    val hora_fin_limite: String,
    val docente: String,
    val limiteant_ingreso: String,
    val limitesup_salida: String,
    val asign_det_nombre: String,
    val id_hor_asis: String,
    val hor_asis_dia: String,
    val hora_fin: String,
    val detalle_marcacion: String,
    val hor_asis_inicio: String,
    val id_asign_det: String,
    val id_hor: String,
    val serv_abrev: String,
    val limitesup_ingreso: String,
    val id_modal_hora_acad: Int,
    val fec_aut_demo_ini: String,
    val fec_aut_demo_fin: String
)

@Serializable
data class ResponseEstadoMarcacion(
    val flag_val: Int,
    val estado_marcar: List<estado_marcar> = emptyList()
)
