package pe.lecordonbleu.universidadestudiante.presentation.screens.notas.uibuilder

data class GrupoPestanaTarea(
    val nombrePestana: String,
    val idsTeoria: List<Int> = emptyList(),
    val idsPractica: List<Int> = emptyList(),
    val idsGeneral: List<Int> = emptyList()
)
