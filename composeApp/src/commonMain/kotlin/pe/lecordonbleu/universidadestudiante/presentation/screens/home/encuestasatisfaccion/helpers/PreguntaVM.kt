package pe.lecordonbleu.universidadestudiante.presentation.screens.home.encuestasatisfaccion.helpers

import pe.lecordonbleu.universidadestudiante.presentation.screens.home.encuestasatisfaccion.enums.TipoPreguntaEncuesta

data class PreguntaVM(
    val id: Int,
    val numero: Int,
    val titulo: String,
    val obligatorio: Boolean,
    val categoria: String,
    val tipo: TipoPreguntaEncuesta,
    val opciones: List<AlternativaVM>
)
