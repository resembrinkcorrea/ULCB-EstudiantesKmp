package pe.lecordonbleu.universidadestudiante.data.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class ListTextosHtml(
    val mensaje: String
)

@Serializable
data class TxtCondiciones(
    val mensaje: String
)

@Serializable
data class DataPerfilList(
    val ubig_nombprov: String,
    val id_estud_docn: String,
    val correo_contacto: String,
    val genero_abrev: String,
    val fecha_nacimiento: String,
    val estado_civil: String,
    val docu_nombre: String,
    val numero_documento: String,
    val genero_nombre: String,
    val pais_procedencia: String,
    val correo_personal: String,
    val correoelec_ins: String,
    val ubig_nombdepa: String,
    val tiene_correoelec_ins: String,
    val telefono1: String,
    val pais_residencia: String,
    val telefono2: String,
    val ubig_nombdist: String,
    val id_docu_tipo: String,
    val direc_resi: String,
    val nombre_contacto: String,
    val usuario_apellido_mat: String,
    val ubigeo: String,
    val ususario_nombre: String,
    val id_pers_det: String,
    val usuario_apellido_pat: String,
    val url_foto: String? = null,
    val id_usuario: String,
    val telefono_contacto: String
)

@Serializable
data class ResponseTextosHtml(
    val flag_val: Int,
    val pdf: String,
    val ListTextosHtml: List<ListTextosHtml> = emptyList(),
    val DataPerfilList: List<DataPerfilList> = emptyList(),
    val TxtCondiciones: List<TxtCondiciones> = emptyList()
)
