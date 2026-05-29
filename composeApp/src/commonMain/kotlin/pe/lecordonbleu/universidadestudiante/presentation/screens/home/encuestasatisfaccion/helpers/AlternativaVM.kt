package pe.lecordonbleu.universidadestudiante.presentation.screens.home.encuestasatisfaccion.helpers

data class AlternativaVM(
    val id: Int,
    val orden: Int,
    val titulo: String,
    val habilitaPreguntaIds: List<Int> = emptyList(),
    val flagTexto: Int
)
