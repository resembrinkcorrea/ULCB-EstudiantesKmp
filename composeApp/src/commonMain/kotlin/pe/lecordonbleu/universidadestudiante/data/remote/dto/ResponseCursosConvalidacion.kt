package pe.lecordonbleu.universidadestudiante.data.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class ResponseCursosConvalidacion(
    val ListCursosAcademica: List<ListCursosAcademicaItem> = emptyList(),
    val flag_val: Int
)

@Serializable
data class ListCursosAcademicaItem(
    val id_pest_det_asign_mat: Int,
    val contador: Int,
    val ciclo_nivel: Int,
    val ciclo_dest: Int,
    val matric_asig_nota_final: Int,
    val pest_asign_nombre_dest: String,
    val mensaje_porc: String,
    val mat_asign_nombre: String
)
