package pe.lecordonbleu.universidadestudiante.data.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class ResponseTramitesDocumentos(
    val flag_val: Int = 0,
    val TramiteDocumentos: List<TramiteDocumentos> = emptyList()
)

@Serializable
data class TramiteDocumentos(
    val id_tramite: String = "",
    val MONTO: String = "",
    val N: String = "",
    val flag_recojo: String = "",
    val ESTADO: String = "",
    val COMPROBANTE: String = "",
    val pg_tipo_entrega: String = "",
    val contador: Int = 0,
    val dni_presencial: String = "",
    val TIPO_TRAMITE: String = "",
    val ARCHIVO_RESPUESTA: String = "",
    val orden: String = "",
    val FECHA_SOLICITANTE: String = "",
    val TIPO_ENTREGA: String = "",
    val flag_validar: String = "",
    val TRAMITE_: String = "",
    val multiple: String = "",
    val id_tramite_estud: String = "",
    val nombre_presencial: String = "",
    val RESPUESTA: String = "",
    val MOTIVO: String = "",
    val montobase: String = "",
    val ACCIONES: String = "",
    val flag_requisitos2: String = "",
    val flag_requisitos: String = "",
    val MODALIDAD: String = "",
    val REQUISITOS: String = "",
    val estado_tramite: String = "",
    val estado_pago: String = "",
    val PAGO: String = ""
)
