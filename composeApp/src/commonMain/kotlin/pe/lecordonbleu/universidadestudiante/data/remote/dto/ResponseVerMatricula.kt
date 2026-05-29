package pe.lecordonbleu.universidadestudiante.data.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class ListVerMatric(
    val condicion_ciclos_c: String,
    val max_cred: Int,
    val min_cred: Int,
    val estado_ingresante: String,
    val horario: String,
    val id_oad_seccion: String,
    val hor_acad: String,
    val hora_fin_cr: String,
    val id_oacad_det: String,
    val id_matric_asig: Int,
    val hor: String,
    val cant_tot_cred: String,
    val origen: Int,
    val modal_asign_abrev: String,
    val tipo_asign_abrev: String,
    val id_peracad: String,
    val peracad_nombre: String,
    val limite_maxcred: String,
    val id_asign_det_cr: String,
    val asign_det_nombre: String,
    val id_matric: Int,
    val id_matric_asig_secc: Int,
    val turno_nombre: String,
    val oad_seccion_nombre: String,
    val id_dia_semana: String,
    val id_hora_dia: String,
    val valhor: Int,
    val ciclo_nivel: String,
    val id_dia_semana_c: String,
    val nro_rep: Int,
    val flag_aprobado: Int,
    val asign_det_nombre_cod: String,
    val hora_ini_cr: String,
    val id_asign_det: Int,
    val id_oacad_arranque: Int
)

@Serializable
data class ResponseVerMatricula(
    val flag_val: Int,
    val list_vermatric: List<ListVerMatric> = emptyList()
)
