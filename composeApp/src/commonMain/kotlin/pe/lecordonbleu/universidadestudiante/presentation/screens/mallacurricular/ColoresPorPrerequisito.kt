package pe.lecordonbleu.universidadestudiante.presentation.screens.mallacurricular

import androidx.compose.ui.graphics.Color
import pe.lecordonbleu.universidadestudiante.data.remote.dto.ListTablaPlanEstudio

fun ColoresPorPrerequisito(lista: List<ListTablaPlanEstudio>): Map<String, Color> {
    val colores = mutableMapOf<String, Color>()
    val paletaColores = listOf(
        Color(0xFFE57373), Color(0xFF64B5F6), Color(0xFF81C784),
        Color(0xFFFFD54F), Color(0xFFBA68C8), Color(0xFFA1887F),
        Color(0xFFFF8A65), Color(0xFF4DB6AC), Color(0xFF7986CB),
        Color(0xFF90A4AE)
    )
    val relaciones = mutableMapOf<String, MutableList<String>>()
    var colorIndex = 0

    lista.forEach { asignatura ->
        val nombre = asignatura.ASIGNATURA.trim()
        val prereq = asignatura.PREREQUISITO.trim()
        if (prereq != "-" && prereq.isNotBlank()) {
            relaciones.getOrPut(prereq) { mutableListOf() }.add(nombre)
        }
    }

    relaciones.forEach { (prereq, dependientes) ->
        val color = colores[prereq] ?: paletaColores[colorIndex % paletaColores.size].also {
            colores[prereq] = it
            colorIndex++
        }
        dependientes.forEach { colores[it] = color }
    }

    return colores
}
