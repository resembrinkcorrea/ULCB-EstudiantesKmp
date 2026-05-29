package pe.lecordonbleu.universidadestudiante.data.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class ListPeriodoCorriente(
    val id_oper: Int,
    val codigo_periodo_academico: String,
    val id_estud_pe: Int,
    val nombre_periodo_academico: String,
    val id_estud_serv: Int,
    val peracad_nombre: String,
    val id_peracad: Int,
    val flag_verano: Int,
    val flag_campanya: Int,
    val id_camp_desc: Int,
    val id_oacad_arranque: Int = 0
)

@Serializable
data class ResponsePeriodoCuentaCorriente(
    val flag_val: Int,
    val ListPeriodoCorriente: List<ListPeriodoCorriente> = emptyList()
)
