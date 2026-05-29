package pe.lecordonbleu.universidadestudiante.presentation.screens.eta.enums

enum class EstadoIcono {
    NINGUNO, SUBIDO_VERDE, VALIDADO_VERDE;

    companion object {
        fun fromHtml(html: String?): EstadoIcono {
            if (html == null) return NINGUNO
            return when {
                html.contains("fa-check") && html.contains("Registrado", ignoreCase = true) -> SUBIDO_VERDE
                html.contains("fa-check") && html.contains("Validado", ignoreCase = true) -> VALIDADO_VERDE
                else -> NINGUNO
            }
        }
    }
}
