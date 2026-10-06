package pe.lecordonbleu.universidadestudiante.presentation.screens.mercadopago

object MpPaymentSession {
    var callbackId: String = ""
    var monto: Double = 0.0
    var montoDisplay: String = ""
    var email: String = ""
    var dni: String = ""
    var idUneg: Int = 1
    var externalReference: String = ""
    var tipo: String = "APPCC"

    fun clear() {
        callbackId = ""
        monto = 0.0
        montoDisplay = ""
        email = ""
        dni = ""
        idUneg = 1
        externalReference = ""
        tipo = "APPCC"
    }
}
