package pe.lecordonbleu.universidadestudiante.data.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class DataIniMatricula(
    val icono: String,
    val tipo: Int,
    val contador: Int,
    val titulo: String,
    val mensaje: String
)

@Serializable
data class ResponseValidarInicioMatricula(
    val flag_val: Int,
    val data_ini_matricula: List<DataIniMatricula> = emptyList()
)
