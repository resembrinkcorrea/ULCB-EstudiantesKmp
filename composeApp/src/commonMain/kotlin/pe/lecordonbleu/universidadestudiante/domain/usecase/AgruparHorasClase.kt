package pe.lecordonbleu.universidadestudiante.domain.usecase

import pe.lecordonbleu.universidadestudiante.data.remote.dto.Horario
import pe.lecordonbleu.universidadestudiante.domain.model.GrupoHorarioKey

fun agruparHorasClase(horarios: List<Horario>): List<Horario> {
    val horariosAgrupados = horarios.groupBy {
        GrupoHorarioKey(it.id_hor, it.id_dia_semana, it.id_aula)
    }

    val listaConsolidado = mutableListOf<Horario>()

    horariosAgrupados.forEach { (key, horarios) ->
        val ordenados = horarios.sortedBy { it.hora_inicio }
        var subGrupo = mutableListOf<Horario>()

        for (i in ordenados.indices) {
            val actual = ordenados[i]
            if (i == 0) {
                subGrupo.add(actual)
                continue
            }

            val anterior = ordenados[i - 1]
            if (anterior.hora_fin == actual.hora_inicio) {
                subGrupo.add(actual)
            } else {
                val primero = subGrupo.first()
                val ultimo = subGrupo.last()

                listaConsolidado.add(
                    Horario(
                        id_aula = key.id_aula,
                        id_hor = key.id_hor,
                        oad_seccion_nombre = primero.oad_seccion_nombre,
                        aula_nombre = primero.aula_nombre,
                        id_dia_semana = key.id_dia_semana,
                        pest_asign_nombre = primero.pest_asign_nombre,
                        sede = primero.sede,
                        hora_inicio = primero.hora_inicio,
                        hora = "${primero.hora_inicio} - ${ultimo.hora_fin}",
                        hor_asis_dia = primero.hor_asis_dia,
                        hora_fin = ultimo.hora_fin,
                        docente = primero.docente,
                        tipo_dictado_nombre = primero.tipo_dictado_nombre,
                        dia_semana_nombre = primero.dia_semana_nombre,
                        tipaula_dictado = primero.tipaula_dictado
                    )
                )

                subGrupo = mutableListOf(actual)
            }
        }

        if (subGrupo.isNotEmpty()) {
            val primero = subGrupo.first()
            val ultimo = subGrupo.last()

            listaConsolidado.add(
                Horario(
                    id_aula = key.id_aula,
                    id_hor = key.id_hor,
                    oad_seccion_nombre = primero.oad_seccion_nombre,
                    aula_nombre = primero.aula_nombre,
                    id_dia_semana = key.id_dia_semana,
                    pest_asign_nombre = primero.pest_asign_nombre,
                    sede = primero.sede,
                    hora_inicio = primero.hora_inicio,
                    hora = "${primero.hora_inicio} - ${ultimo.hora_fin}",
                    hor_asis_dia = primero.hor_asis_dia,
                    hora_fin = ultimo.hora_fin,
                    docente = primero.docente,
                    tipo_dictado_nombre = primero.tipo_dictado_nombre,
                    dia_semana_nombre = primero.dia_semana_nombre,
                    tipaula_dictado = primero.tipaula_dictado
                )
            )
        }
    }

    return listaConsolidado.sortedBy { it.hora_inicio }
}
