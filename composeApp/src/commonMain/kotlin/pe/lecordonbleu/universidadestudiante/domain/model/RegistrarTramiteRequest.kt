package pe.lecordonbleu.universidadestudiante.domain.model

import kotlinx.serialization.Serializable

@Serializable
class RegistrarTramiteRequest(
    private val id_estud: Int,
    private val id_uneg: Int,
    private val id_tari_gen: Int,
    private val id_user: Int,
    private val estado_pasarela: String,
    private val tari_gen_cod_nav: String,
    private val transw_id_tx: String,
    private val id_pest_det: Int,
    private val id_estud_pe: Int,
    private val id_estud_serv: Int,
    private val pg_tipo_entrega: Int,
    private val motivo: String,
    private val flag_recojo: Int,
    private val recojo_dni: String,
    private val recojo_nombre: String,
    private val flag_pago: Int,
    private val id_req_temp: Int,
    private val id_tramite: Int,
    private val id_sistema: Int,
    private val monto: Double,
    private val id_modalidad: Int,
    private val tipo_tramite_reg: Int,
    @Serializable(with = RequisitosSealedAsArraySerializer::class)
    val requisitos: RequisitosTramiteSealed
)

