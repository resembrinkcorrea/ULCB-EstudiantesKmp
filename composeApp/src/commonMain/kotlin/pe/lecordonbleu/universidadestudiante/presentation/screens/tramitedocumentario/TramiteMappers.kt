package pe.lecordonbleu.universidadestudiante.presentation.screens.tramitedocumentario

import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.contentOrNull
import kotlinx.serialization.json.intOrNull
import kotlinx.serialization.json.jsonPrimitive
import pe.lecordonbleu.universidadestudiante.data.remote.dto.TReTramiteItem

internal fun mapTReItems(items: List<JsonObject>): List<TReTramiteItem> = items.mapNotNull { obj ->
    try {
        TReTramiteItem(
            estado = obj["estado"]?.jsonPrimitive?.contentOrNull.orEmpty(),
            DATOCUMPLE = obj["DATOCUMPLE"]?.jsonPrimitive?.contentOrNull.orEmpty(),
            multiple = obj["multiple"]?.jsonPrimitive?.contentOrNull.orEmpty(),
            documento = obj["documento"]?.jsonPrimitive?.contentOrNull.orEmpty(),
            id_tramite_estud = obj["id_tramite_estud"]?.jsonPrimitive?.contentOrNull.orEmpty(),
            id_tramite_estud_req_doc = obj["id_tramite_estud_req_doc"]?.jsonPrimitive?.contentOrNull.orEmpty(),
            periodo_mat = obj["periodo_mat"]?.jsonPrimitive?.contentOrNull.orEmpty(),
            contador = obj["contador"]?.jsonPrimitive?.intOrNull ?: 0,
            id_tramite_req_doc = obj["id_tramite_req_doc"]?.jsonPrimitive?.contentOrNull.orEmpty(),
            requisito = obj["requisito"]?.jsonPrimitive?.contentOrNull.orEmpty(),
            id_tramite_estud_req = obj["id_tramite_estud_req"]?.jsonPrimitive?.contentOrNull.orEmpty(),
            cumplio = obj["cumplio"]?.jsonPrimitive?.contentOrNull.orEmpty(),
            requisito_nombre = obj["requisito_nombre"]?.jsonPrimitive?.contentOrNull.orEmpty(),
            empresa = obj["empresa"]?.jsonPrimitive?.contentOrNull.orEmpty(),
            carrera = obj["carrera"]?.jsonPrimitive?.contentOrNull.orEmpty(),
            id = obj["id"]?.jsonPrimitive?.intOrNull,
            nombre = obj["nombre"]?.jsonPrimitive?.contentOrNull
        )
    } catch (_: Exception) {
        null
    }
}
