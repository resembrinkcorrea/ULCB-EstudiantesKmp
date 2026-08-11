package pe.lecordonbleu.universidadestudiante.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class MpIdentification(val type: String = "DNI", val number: String)

@Serializable
data class MpPhone(val number: String)

@Serializable
data class MpPayerYape(
    val email: String,
    val phone: MpPhone,
    val identification: MpIdentification
)

@Serializable
data class YapeTokenRequest(
    val phoneNumber: String,
    val otp: String,
    val requestId: String
)

@Serializable
data class YapeTokenResponse(
    val id: String = "",
    val token: String = ""
)

@Serializable
data class YapeRequest(
    val payment_method_id: String = "yape",
    val token: String,
    val transaction_amount: Double,
    val description: String,
    val external_reference: String,
    val callback_id: String,
    val id_uneg: Int,
    val payer: MpPayerYape
)

@Serializable
data class MpPayerTarjeta(
    val email: String,
    val identification: MpIdentification
)

@Serializable
data class TarjetaRequest(
    val payment_method_id: String,
    val token: String,
    val transaction_amount: Double,
    val installments: Int = 1,
    val description: String,
    val external_reference: String,
    val callback_id: String,
    val id_uneg: Int,
    val payer: MpPayerTarjeta
)

@Serializable
data class PagoEfectivoRequest(
    val payment_method_id: String = "pagoefectivo_atm",
    val transaction_amount: Double,
    val description: String,
    val external_reference: String,
    val callback_id: String,
    val id_uneg: Int,
    val payer: MpPayerTarjeta
)

@Serializable
data class MpPayerCosto(
    val installments: Int = 1,
    val installment_rate: Double = 0.0,
    val recommended_message: String = "",
    val installment_amount: Double = 0.0,
    val total_amount: Double = 0.0
)

@Serializable
data class RegisterPaymentRequest(
    val id: String,
    val amount: String,
    val at: String,
    val callback_id: String,
    val id_uneg: Int,
    val tipo: String = "APPCC",
    val payment_method_id: String = ""
)
