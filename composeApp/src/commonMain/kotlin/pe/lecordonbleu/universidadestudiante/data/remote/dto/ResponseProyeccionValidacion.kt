package pe.lecordonbleu.universidadestudiante.data.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class ListProyeccionValidacion(
    val id_estud_pe: Int = 0,
    val id_serv: Int = 0,
    val id_peracad: Int = 0,
    val id_oacad_arranque: Int = 0,
    val id_pest_det: Int = 0,
    val id_estud_serv: Int = 0,
    val id_proce_mat: Int = 0,
    val id_matric: Int = 0,
    val id_estud: Int = 0,
    val id_tiposerva: Int = 0,
    val principal: Int = 0,
    val flag_proyeccion: Int = 0,
    val flag_matricula: Int = 0,
    val flag_mat_term_cond: Int = 0,
    val flag_intranet_mat: Int = 0,
    val flag_deuda: Int = 0,
    val estado_ingresante: Int = 0,
    val est_codigo: String = "",
    val pest_det_nombre: String = "",
    val serv_nombre: String = "",
    val peracad_nombre: String = "",
    val msg_proyeccion: String = "",
    val promedio_ult_matricula: String = "",
    val promedio_ponderado: String = "",
    val matric_url: String = ""
)

@Serializable
data class ResponseProyeccionValidacion(
    val flag_val: Int = 0,
    val ListProyeccionValidacion: List<ListProyeccionValidacion> = emptyList()
)
