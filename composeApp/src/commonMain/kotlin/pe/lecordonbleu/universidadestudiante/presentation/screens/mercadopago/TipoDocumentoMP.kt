package pe.lecordonbleu.universidadestudiante.presentation.screens.mercadopago

enum class TipoDocumentoMP(val valor: String, val etiqueta: String, val soloNumeros: Boolean, val maxLen: Int) {
    DNI("DNI", "DNI", soloNumeros = true, maxLen = 8),
    CE("C.E", "Carnet de extranjería", soloNumeros = false, maxLen = 12),
    RUC("RUC", "RUC", soloNumeros = true, maxLen = 11),
    OTRO("Otro", "Otro", soloNumeros = false, maxLen = 20);

    companion object {
        val todos get() = entries.toList()
        fun porValor(valor: String) = entries.firstOrNull { it.valor == valor } ?: DNI
    }
}
