package pe.lecordonbleu.universidadestudiante.data.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class DataHistorialAcadamico(
    val est_telef01_pers: String,
    val num_docu_iden_pd: String,
    val gacad_nombre: String,
    val est_correoelec_personal: String,
    val pest_det_nombre: String,
    val serv_nombre: String,
    val peracad_nombre: String,
    val tiposerva_abrev: String,
    val pestd_cod: String,
    val e_est_nombre: String,
    val est_apellido_mat: String,
    val est_apellido_pat: String,
    val id_peracad: String,
    val id_estud_pe: String,
    val desc_pg_estado_estud_pe: String,
    val pg_estado_estud_pe: String
)

@Serializable
data class ResponseHistorialAcademicoAlumno(
    val flag_val: Int,
    val data_hist_acad: List<DataHistorialAcadamico> = emptyList()
)
