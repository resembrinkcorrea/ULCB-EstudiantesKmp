package pe.lecordonbleu.universidadestudiante.presentation.screens.mallacurricular

enum class EstadoAsignaturaVisual {
    APROBADO, DESAPROBADO, NINGUNO;

    companion object {
        fun fromHtml(html: String?): EstadoAsignaturaVisual {
            if (html == null || html.trim() == "-") return NINGUNO
            return when {
                html.contains("flag_si", ignoreCase = true) -> APROBADO
                html.contains("flag_no", ignoreCase = true) -> DESAPROBADO
                else -> NINGUNO
            }
        }
    }
}
