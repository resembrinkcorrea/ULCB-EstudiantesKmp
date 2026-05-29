package pe.lecordonbleu.universidadestudiante.domain.model

import kotlinx.serialization.Serializable

@Serializable

data class FichaMatriculaRequest(
    val idUNEG: Int,
    val idPeriodoAcademico: String,
    val personaNombre: String,
    val personaPaterno: String,
    val personaMaterno: String,
    val periodo: String,
    val codigo_estud: String,
    val carrera_prof: String,
    val valorFecha: String,
    val id_oacad_arranque: String,
    val id_estud_pe: String,
    val idUsuario: Int,
    val id_pest_det: Int,
    val id_estud: Int,
    val estado_ingresante: String,
    val prom_ult_mat: String
)
