package pe.lecordonbleu.universidadestudiante.data.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class ListResumenHist(
    val ultimo_periodo: String,
    val fec_creacion: String,
    val prom_ciclo: String,
    val total_cred_total: String,
    val exec_tipo_tari: String,
    val cate_nombre: String,
    val traslado: String,
    val prom_ponder: String,
    val cred_mat: String,
    val periodo_ingreso: String,
    val mod_matricula: String,
    val beca: String,
    val fec_modif: String,
    val asig_aprob: String,
    val cant_asign: String,
    val tipo_pago: String,
    val asig_desaprob: String,
    val user_modif: String,
    val asign_cursadas: String,
    val cant_cred: String,
    val user_creacion: String,
    val id_uneg: String,
    val asign_mat: String,
    val ingresante: String
)

@Serializable
data class ResponseResumenHistorico(
    val flag_val: Int,
    val list_resumenhist: List<ListResumenHist> = emptyList()
)
