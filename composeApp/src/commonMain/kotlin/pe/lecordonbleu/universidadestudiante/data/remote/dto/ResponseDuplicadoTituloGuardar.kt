package pe.lecordonbleu.universidadestudiante.data.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class ResponseDuplicadoTituloGuardar(
    val flag_val: Int,
    val ListTempRequisito: List<TempRequisitoResponse>
)

@Serializable
data class TempRequisitoResponse(
    val id_req_temp: Int
)
