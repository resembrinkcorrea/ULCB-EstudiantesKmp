package pe.lecordonbleu.universidadestudiante.presentation.screens.home.encuestasatisfaccion.mappers

import pe.lecordonbleu.universidadestudiante.data.remote.dto.ListPreguntasEncuesta
import pe.lecordonbleu.universidadestudiante.domain.model.EncuestaItem
import pe.lecordonbleu.universidadestudiante.domain.model.EncuestaSatisfaccionGuardarRequest
import pe.lecordonbleu.universidadestudiante.presentation.screens.home.encuestasatisfaccion.helpers.RespuestaUsuario

object EncuestaMapper {
    fun crear(
        respuestas: Map<Int, List<RespuestaUsuario>>,
        preguntas: List<ListPreguntasEncuesta>,
        idSistema: Int,
        idEstudPe: Int,
        idServ: Int,
        idPeracad: Int,
        idUser: Int
    ): EncuestaSatisfaccionGuardarRequest {
        val idExamServ = preguntas.firstOrNull()?.id_exam_serv ?: 0

        val items = respuestas.values.flatten()
            .sortedBy { it.idPregunta }
            .map { r ->
                EncuestaItem(
                    id_user = idUser,
                    id_exam_preg = r.idPregunta,
                    id_exam_preg_alter = r.idAlternativa ?: -1,
                    id_estud_pe = idEstudPe,
                    id_peracad = idPeracad,
                    id_exam_serv = idExamServ,
                    id_uneg = 1,
                    descripcion = r.texto ?: "",
                    puntaje = 0,
                    id_serv = idServ
                )
            }

        return EncuestaSatisfaccionGuardarRequest(
            id_sistema = idSistema,
            Encuesta = items
        )
    }
}
