package pe.lecordonbleu.universidadestudiante.presentation.screens.home.encuestasatisfaccion.helpers

data class RespuestaUsuario(
    val idPregunta: Int,
    val idAlternativa: Int? = null,
    val texto: String? = null
)
