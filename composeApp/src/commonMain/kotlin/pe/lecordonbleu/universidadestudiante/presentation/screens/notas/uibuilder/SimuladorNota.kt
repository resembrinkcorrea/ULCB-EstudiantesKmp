package pe.lecordonbleu.universidadestudiante.presentation.screens.notas.uibuilder

data class SimuladorNota(
    val nombreEvaluacion : String,
    val peso             : Double,
    val notaActual       : Double,
    var notaSimulada     : String
)
