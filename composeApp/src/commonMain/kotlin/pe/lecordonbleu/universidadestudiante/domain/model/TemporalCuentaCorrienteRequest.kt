package pe.lecordonbleu.universidadestudiante.domain.model

import kotlinx.serialization.Serializable

data class TemporalCuentaCorrienteRequest(val body: String)

@Serializable
data class TemporalBody(val nameValuePairs: PagosTopLevel)

@Serializable
data class PagosTopLevel(val Pagos: PagosNameValuePairs)

@Serializable
data class PagosNameValuePairs(val nameValuePairs: PagoNameValuePairs)

@Serializable
data class PagoNameValuePairs(val Pago: namePairs)

@Serializable
data class namePairs(val nameValuePairs: PagoDetalleTemporal)

@Serializable
data class PagoDetalleTemporal(
    val cod_transaccion: String,
    val amount: String,
    val callback_id: String,
    val callback_url: String,
    val displayPayerInformation: String,
    val email_address: String,
    val env: String,
    val invoice_number: String,
    val locale: String,
    val program_code: String,
    val provider: String,
    val recipient: String,
    val sender_address1: String,
    val sender_city: String,
    val sender_country: String,
    val sender_email: String,
    val sender_first_name: String,
    val sender_last_name: String,
    val sender_middle_name: String,
    val sender_phone: String,
    val sender_state: String,
    val sender_zip_code: String,
    val student_first_name: String,
    val student_id: String,
    val student_last_name: String,
    val return_url: String,
    val sender_address2: String,
    val id_uneg: String
)
