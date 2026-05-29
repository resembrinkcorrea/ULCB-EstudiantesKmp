package pe.lecordonbleu.universidadestudiante.data.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class ResponseLoginUser(
    val data_menu: List<DataMenu> = emptyList(),
    val flag_val: Int,
    val data_usuario: List<DataUsuario> = emptyList(),
    val jsonGeneral: JsonGeneral = JsonGeneral()
)

@Serializable
data class JsonGeneral(
    val data1: List<Data1> = emptyList()
)

@Serializable
data class Data1(
    val flag_uneg_default: Int = 0,
    val check_valida: Int = -1,
    val contador: Int = 0,
    val id_usuario_logueo: Int = 0,
    val uneg_unico: Int = 0,
    val id_uneg: Int = 0,
    val uneg_nombre: String = "",
    val uneg_abrev: String = ""
)

@Serializable
data class DataMenu(
    val icono: String = "",
    val ver: Int = 0,
    val flag_etiqueta: Int = 0,
    val flag: Int = 0,
    val btn_dscto_adm: Int = 0,
    val ruta: String = "",
    val idMenu: Int = 0,
    val imagen: String = "",
    val texto_etiqueta: String = "",
    val descargar: Int = 0,
    val crear: Int = 0,
    val id_sistema: Int = 0,
    val textoMenuAbrev: String = "",
    val eliminar: Int = 0,
    val contador: Int = 0,
    val textoMenu: String = "",
    val editar: Int = 0,
    val orden: Int = 0,
    val nivel: Int = 0,
    val idMenuPadre: Int = 0,
    val color_etiqueta: String = ""
)

@Serializable
data class DataUsuario(
    val num_docu_iden_pd: String = "",
    val idUNEG: Int = 0,
    val personaNombre: String = "",
    val tiempo_inactividad: Int = 0,
    val flag_dashboard: Int = 0,
    val idUsuario: Int = 0,
    val est_url_foto: String = "",
    val genero_abrev: String = "",
    val id_atribp: Int = 0,
    val usua_usuario: String = "",
    val pers_apellido_mat: String = "",
    val pers_apellido_pat: String = "",
    val flag_inactividad: Int = 0,
    val flag_modif_contraseña: Int = 0,
    val contador: Int = 0,
    val id_estud: Int = 0,
    val genero: String = "",
    val flag_minedu: Int = 0,
    val id_uneg: Int = 0,
    val flag_boton_info: Int = 0,
    val id_vendedor: Int = 0,
    val idSistema: Int = 0,
    val empl_url_foto: String = "",
    val id_perfil: Int = 0,
    val personaPaterno: String = "",
    val id_pers_det: Int = 0,
    val id_entrevistador: Int = 0,
    val pers_nombre: String = "",
    val flag_actFlujo: Int = 0,
    val id_sistema: Int = 0,
    val perf_nombre: String = "",
    val id_tipo_usuario: Int = 0,
    val nombreUNEG: String = "",
    val personaMaterno: String = "",
    val id_docente: Int = 0,
    val flag_consulta_producto: Int = 0,
    val flag_presup: Int = 0
)
