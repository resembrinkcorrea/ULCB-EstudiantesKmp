package pe.lecordonbleu.universidadestudiante.presentation.screens.home.encuestasatisfaccion.mappers

import pe.lecordonbleu.universidadestudiante.data.remote.dto.ListPreguntasEncuesta
import pe.lecordonbleu.universidadestudiante.presentation.screens.home.encuestasatisfaccion.helpers.AlternativaVM
import pe.lecordonbleu.universidadestudiante.presentation.screens.home.encuestasatisfaccion.helpers.PreguntaVM
import pe.lecordonbleu.universidadestudiante.presentation.screens.home.encuestasatisfaccion.enums.TipoPreguntaEncuesta

object PreguntasMapper {
    fun mapear(rows: List<ListPreguntasEncuesta>): List<PreguntaVM> {
        val grupos = rows.groupBy { it.nro_pregunta }

        val preguntas = grupos.values.mapNotNull { grupo ->
            val base = grupo.firstOrNull() ?: return@mapNotNull null

            val tipo = when (base.id_tipo_preg) {
                6 -> TipoPreguntaEncuesta.TEXTO
                4 -> TipoPreguntaEncuesta.UNICA
                5 -> TipoPreguntaEncuesta.CHECK
                8 -> TipoPreguntaEncuesta.OPTIONLINEAL
                else -> return@mapNotNull null
            }

            val opciones = grupo.mapNotNull { r ->
                val idsCondicion = r.id_exam_preg_condicion
                    .takeIf { it.isNotBlank() }
                    ?.split(",")
                    ?.mapNotNull { it.trim().toIntOrNull() }
                    ?: emptyList()

                AlternativaVM(
                    id = r.id_examen_preg_alter,
                    orden = 0,
                    titulo = r.examen_preg_alter_abrev ?: "",
                    habilitaPreguntaIds = idsCondicion,
                    flagTexto = r.flag_texto
                )
            }

            PreguntaVM(
                id = base.id_exam_preg,
                numero = base.nro_pregunta,
                titulo = base.exam_preg_nombre,
                obligatorio = base.preg_obligatorio == 1,
                categoria = base.categoria_nombre,
                tipo = tipo,
                opciones = opciones
            )
        }

        return preguntas.sortedBy { it.numero }
    }
}
