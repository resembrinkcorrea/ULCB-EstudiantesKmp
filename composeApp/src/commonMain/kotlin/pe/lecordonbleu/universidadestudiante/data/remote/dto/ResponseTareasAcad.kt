package pe.lecordonbleu.universidadestudiante.data.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class TareaAcad(
    val estudiante: String,
    val est_codigo: String,
    val act_acad_abrev: String,
    val nota: String,
    val peso_act_det: String,
    val matric_not_det_prom: String? = null,
    val nro_nota: String,
    val flag_nota_vacia: String,
    val pest_det_nota_min_aprob: String,
    val usuario_modif: String = "",
    val fecha_modif: String = ""
)

@Serializable
data class ResponseTareasAcad(
    val flag_val: Int,
    val listadoTareaAcad: List<TareaAcad> = emptyList()
)
