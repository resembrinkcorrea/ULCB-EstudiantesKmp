package pe.lecordonbleu.universidadestudiante.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class MatriculaDetalleItem(
    val asign_det_nombre: String,
    val cred_asign: String,
    val cred_total: String,
    val flag_ficha_envio: String,
    val flag_mat_term_cond: String,
    val id_estud: String,
    val id_matric: String,
    val id_matric_asig: String,
    val id_matric_asig_secc: String,
    val id_oacad_arranque: String,
    val id_oacad_det: String,
    val id_oad_seccion: String,
    val id_peracad: String,
    val id_pest_det: String,
    val id_serv: String,
    val id_tipmatric: String,
    val id_uneg: String,
    val id_user: String,
    val nro_rep: String,
    val oad_seccion_nombre: String,
    val origen: String
)

@Serializable
data class MatriculaBody(
    val data: List<MatriculaDetalleItem>
)

@Serializable
data class RegistrarMatriculaBodyRequest(
    val id_tiposerva: Int,
    val matricula: MatriculaBody
)
