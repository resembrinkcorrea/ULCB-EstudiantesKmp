package pe.lecordonbleu.universidadestudiante.data.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class ResponseHorarioPDF(
    val flag_val: Int,
    val pdfbase64: String = "",
    val mensaje: String = ""
)
