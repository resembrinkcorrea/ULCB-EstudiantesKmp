package pe.lecordonbleu.universidadestudiante.data.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class ResponseMercadoPago(
    val id: Long = 0L,
    val status: String = "",
    val status_detail: String = "",
    val status_detail_message: String = "",
    val transaction_amount: Double = 0.0,
    val date_approved: String = "",
    val external_reference: String = "",
    val ticket_url: String = ""
)

@Serializable
data class ResponsePublicKeyMP(
    val publicKey: String = "",
    val id_uneg: Int = 0
)


