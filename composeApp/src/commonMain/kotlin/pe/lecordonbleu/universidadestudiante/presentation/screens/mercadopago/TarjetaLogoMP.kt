package pe.lecordonbleu.universidadestudiante.presentation.screens.mercadopago

enum class TarjetaLogoMP(val url: String, val descripcion: String) {
    VISA("https://mercadeo.blob.core.windows.net/logo/visa.png", "Visa"),
    MASTERCARD("https://mercadeo.blob.core.windows.net/logo/mastercard.png", "Mastercard"),
    DINERS("https://mercadeo.blob.core.windows.net/logo/diners.png", "Diners"),
    AMEX("https://mercadeo.blob.core.windows.net/logo/amex.png", "Amex");

    companion object {
        val todos get() = entries.toList()
    }
}
