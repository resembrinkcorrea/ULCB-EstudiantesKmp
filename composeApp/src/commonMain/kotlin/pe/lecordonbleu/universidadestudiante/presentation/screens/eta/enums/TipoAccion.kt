package pe.lecordonbleu.universidadestudiante.presentation.screens.eta.enums

enum class TipoAccion {
    NINGUNA, VER, ELIMINAR, SUBIR;

    companion object {
        fun parseHtml(html: String?): List<TipoAccion> {
            if (html == null) return emptyList()
            val acciones = mutableListOf<TipoAccion>()
            if (html.contains("fa-eye")) acciones.add(VER)
            if (html.contains("fa-trash")) acciones.add(ELIMINAR)
            if (html.contains("fa-upload")) acciones.add(SUBIR)
            return if (acciones.isEmpty()) listOf(NINGUNA) else acciones
        }
    }
}
