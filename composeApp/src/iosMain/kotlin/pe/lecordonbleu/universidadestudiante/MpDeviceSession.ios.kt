@file:OptIn(ExperimentalForeignApi::class)

package pe.lecordonbleu.universidadestudiante

import cocoapods.MercadoPagoDevicesSDK.MercadoPagoDevicesSDK
import io.ktor.client.HttpClient
import io.ktor.client.engine.darwin.Darwin
import io.ktor.client.request.headers
import io.ktor.client.request.put
import io.ktor.client.request.setBody
import io.ktor.client.statement.bodyAsText
import io.ktor.http.ContentType
import io.ktor.http.contentType
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive

actual fun initMpDevice(context: Any?) {
    // iOS SDK no requiere inicialización explícita
}

actual suspend fun fetchMpDeviceSession(context: Any?, publicKey: String): String {
    return try {
        val infoJsonStr = MercadoPagoDevicesSDK.shared().getInfoAsJsonString() ?: return ""
        val infoJson = Json.parseToJsonElement(infoJsonStr).jsonObject
        val fingerprint = infoJson["fingerprint"] ?: return ""

        val bodyJson = """{"finger_print":$fingerprint,"site_id":"MPE"}"""

        val client = HttpClient(Darwin)
        val session = try {
            val response = client.put("https://api.mercadopago.com/cho-off/v1/devices/session") {
                headers { append("X-Public-Key", publicKey) }
                contentType(ContentType.Application.Json)
                setBody(bodyJson)
            }
            val responseJson = Json.parseToJsonElement(response.bodyAsText()).jsonObject
            responseJson["meli_session_id"]?.jsonPrimitive?.content ?: ""
        } finally {
            client.close()
        }
        println("[MP-DEVICE] session=$session")
        session
    } catch (e: Exception) {
        println("[MP-DEVICE] error=${e.message}")
        ""
    }
}
