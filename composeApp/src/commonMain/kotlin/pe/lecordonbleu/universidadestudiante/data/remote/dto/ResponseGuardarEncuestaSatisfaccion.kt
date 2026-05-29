package pe.lecordonbleu.universidadestudiante.data.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class ResponseGuardarEncuestaSatisfaccion(
    val flag_val: Int = 0,
    val ListEncuestaSatisfaccion: List<ListEncuestaSatisfaccion> = emptyList()
)

@Serializable
data class ListEncuestaSatisfaccion(
    val contador: Int = 0,
    val tipo: String = "",
    val icono: String = "",
    val titulo: String = "",
    val mensaje: String = ""
)
