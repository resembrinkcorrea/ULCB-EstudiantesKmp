package pe.lecordonbleu.universidadestudiante.data.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class ListDetalleCuentaCorriente(
    val usuario_registro: String,
    val fecha_registro: String,
    val monto_pagado: String,
    val medio_pago_nombre: String,
    val docu_nombre: String,
    val nro_comprobante: String,
    val fecha_modificacion: String,
    val observacion: String,
    val fecha_pago: String,
    val usuario_modificacion: String,
    val flag_pecano: Int = 0,
    val tipoDocu_pecano: Int = 0,
    val fecha_operacion: String = ""
)

@Serializable
data class ResponseDetalleCuentaCorriente(
    val flag_val: Int,
    val ListDetalleCuentaCorriente: List<ListDetalleCuentaCorriente> = emptyList()
)
