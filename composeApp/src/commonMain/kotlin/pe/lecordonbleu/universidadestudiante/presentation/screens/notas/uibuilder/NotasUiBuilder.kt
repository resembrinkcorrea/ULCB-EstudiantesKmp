package pe.lecordonbleu.universidadestudiante.presentation.screens.notas.uibuilder

import pe.lecordonbleu.universidadestudiante.data.remote.dto.ListadoNotasDetalle
import pe.lecordonbleu.universidadestudiante.data.remote.dto.TipoTareaAcad

object NotasUiBuilder {

    fun agruparTiposTareas(lista: List<TipoTareaAcad>): List<GrupoPestanaTarea> {
        val filtrada = lista.filter { it.flag_det_act == "1" }
        if (filtrada.isEmpty()) return emptyList()

        val mapa = mutableMapOf<String, GrupoPestanaTarea>()
        filtrada.forEach { item ->
            val abrev        = item.tipactacad_abrev
            val esTeoria     = abrev.contains("(T)", ignoreCase = true)
            val esPractica   = abrev.contains("(P)", ignoreCase = true)
            val esGeneral    = !esTeoria && !esPractica
            val nombreLimpio = abrev.replace(Regex("\\(.*?\\)"), "").trim()
            val id           = item.id_tipactacad.toIntOrNull() ?: return@forEach

            val grupo = mapa[nombreLimpio] ?: GrupoPestanaTarea(nombrePestana = nombreLimpio)
            mapa[nombreLimpio] = grupo.copy(
                idsTeoria   = if (esTeoria)   grupo.idsTeoria   + id else grupo.idsTeoria,
                idsPractica = if (esPractica) grupo.idsPractica + id else grupo.idsPractica,
                idsGeneral  = if (esGeneral)  grupo.idsGeneral  + id else grupo.idsGeneral
            )
        }
        return mapa.values.toList()
    }

    fun prepararSimulacion(detalle: ListadoNotasDetalle): List<SimuladorNota> {
        val pesoPerm  = detalle.peso_prac_calif.toDoubleOrNull()?.times(100) ?: 0.0
        val pesoParc  = detalle.peso_exam_parc.toDoubleOrNull()?.times(100)  ?: 0.0
        val pesoFinal = detalle.peso_exam_final.toDoubleOrNull()?.times(100) ?: 0.0
        return listOf(
            SimuladorNota("Practicas Calificadas", pesoPerm,  detalle.not_prac_calif.toDoubleOrNull() ?: 0.0, detalle.not_prac_calif),
            SimuladorNota("Examen Parcial",        pesoParc,  detalle.not_exam_parc.toDoubleOrNull()  ?: 0.0, detalle.not_exam_parc),
            SimuladorNota("Examen Final",           pesoFinal, detalle.not_exam_final.toDoubleOrNull() ?: 0.0, detalle.not_exam_final),
            SimuladorNota("Examen Sustitutorio",    0.0,       detalle.not_exam_susti.toDoubleOrNull() ?: 0.0, detalle.not_exam_susti)
        )
    }
}
