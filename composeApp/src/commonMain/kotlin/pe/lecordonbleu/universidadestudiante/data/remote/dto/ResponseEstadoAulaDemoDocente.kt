package pe.lecordonbleu.universidadestudiante.data.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class ListEstadoAulaDemoDocente(
    val limiteant_salida: Int,
    val fec_date_actual: String,
    val horario: String,
    val hora_ing_sup: String,
    val limiteant_ingreso: Int,
    val salida: Int,
    val limitesup_salida: Int,
    val hora_sal_inf: String,
    val fecha: String,
    val hor_fin: String,
    val asign_det_nombre: String,
    val contador: Int,
    val id_hor_asis: String,
    val entrada: Int,
    val hora_ing_inf: String,
    val flag_demo: Int,
    val id_docente_det_asig: String,
    val hora_sal_sup: String,
    val hor_inicio: String,
    val limitesup_ingreso: Int,
    val flag_aut_off: Int,
    val fec_aut_demo_fin: String,
    val fec_aut_demo_ini: String
)

@Serializable
data class ResponseEstadoAulaDemoDocente(
    val flag_val: Int,
    val ListEstadoAulaDemoDocente: List<ListEstadoAulaDemoDocente> = emptyList()
)
