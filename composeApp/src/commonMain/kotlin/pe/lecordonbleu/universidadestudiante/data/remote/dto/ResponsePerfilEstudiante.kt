package pe.lecordonbleu.universidadestudiante.data.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class ResponsePerfilEstudiante(
    val ListPerfilEstudiante: List<ListPerfilEstudiante> = emptyList(),
    val flag_val: Int
)

@Serializable
data class ListPerfilEstudiante(
    val ubig_nombprov: String,
    val id_estud_docn: Int,
    val correo_contacto: String,
    val genero_abrev: String,
    val fecha_nacimiento: String,
    val estado_civil: String,
    val docu_nombre: String,
    val numero_documento: Long,
    val genero_nombre: String,
    val pais_procedencia: String,
    val correo_personal: String,
    val correoelec_ins: String,
    val ubig_nombdepa: String,
    val tiene_correoelec_ins: Int,
    val telefono1: String,
    val pais_residencia: String,
    val telefono2: String? = null,
    val ubig_nombdist: String,
    val id_docu_tipo: Int,
    val direc_resi: String,
    val nombre_contacto: String,
    val usuario_apellido_mat: String,
    val ubigeo: String,
    val ususario_nombre: String,
    val id_pers_det: Int,
    val usuario_apellido_pat: String,
    val url_foto: String,
    val id_usuario: Int,
    val telefono_contacto: String
)
