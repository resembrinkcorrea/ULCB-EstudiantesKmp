package pe.lecordonbleu.universidadestudiante.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class UserLoginRequest(
    val tipoLogin: Int,
    val rstaLoginMicrosoft: Int,
    val usuario: String,
    val contrasena: String,
    val sistema: Int,
    val idUneg: Int,
    val pc: String,
    val ip: String,
    val lastPwd: String
)
