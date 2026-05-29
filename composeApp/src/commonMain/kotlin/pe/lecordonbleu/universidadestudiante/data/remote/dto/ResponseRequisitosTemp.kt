package pe.lecordonbleu.universidadestudiante.data.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class ResponseRequisitosTemp(
    val flag_val: Int,
    val ListTempRequisito: List<ListTempRequisito>
)

@Serializable
data class ListTempRequisito(
    val contador: Int,
    val id_req_temp: Int
)
