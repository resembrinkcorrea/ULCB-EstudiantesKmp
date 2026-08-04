package pe.lecordonbleu.universidadestudiante.domain.model

import kotlinx.serialization.Serializable
import pe.lecordonbleu.universidadestudiante.data.remote.dto.ListVerMatric

@Serializable
data class CursoHorarioPDF(
    val asign_det_nombre: String,
    val asign_det_nombre_cod: String,
    val horario: String,
    val cant_tot_cred: String,
    val tipo_asign_abrev: String,
    val modal_asign_abrev: String
)

@Serializable
data class HorarioPDFRequest(
    val periodo: String,
    val cursos: List<CursoHorarioPDF>
)

fun ListVerMatric.toCursoHorarioPDF() = CursoHorarioPDF(
    asign_det_nombre     = asign_det_nombre,
    asign_det_nombre_cod = asign_det_nombre_cod,
    horario              = horario,
    cant_tot_cred        = cant_tot_cred,
    tipo_asign_abrev     = tipo_asign_abrev,
    modal_asign_abrev    = modal_asign_abrev
)
