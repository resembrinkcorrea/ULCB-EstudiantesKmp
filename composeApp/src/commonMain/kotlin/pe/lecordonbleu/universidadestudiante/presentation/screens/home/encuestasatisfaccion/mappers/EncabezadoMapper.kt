package pe.lecordonbleu.universidadestudiante.presentation.screens.home.encuestasatisfaccion.mappers

import pe.lecordonbleu.universidadestudiante.data.remote.dto.ListPreguntasEncuesta

object EncabezadoMapper {
    fun obtener(rows: List<ListPreguntasEncuesta>): String {
        return rows.firstOrNull()?.msg_encabezado_enc ?: ""
    }
}