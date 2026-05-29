package pe.lecordonbleu.universidadestudiante.data.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class ListValoresPlanEstudio(
    val cant_cursados: Int,
    val cant_aprobados: Int,
    val contador: Int,
    val cant_total: Int,
    val cant_cred_electivos: Int,
    val cant_total_creditos: Int,
    val cant_cred_obligatorios: Int,
    val cant_desaprobados: Int,
    val cantidad_creditos_aprob: Int
)

@Serializable
data class ResponseValoresPlan(
    val ListValoresPlanEstudio: List<ListValoresPlanEstudio> = emptyList(),
    val flag_val: Int
)
