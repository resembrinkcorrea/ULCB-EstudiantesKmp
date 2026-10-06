package pe.lecordonbleu.universidadestudiante

import android.content.Context
import com.google.gson.FieldNamingPolicy
import com.google.gson.GsonBuilder
import com.google.gson.JsonObject
import com.mercadolibre.android.device.sdk.DeviceSDK
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
import kotlin.apply

private var mpDeviceInitialized = false

actual fun initMpDevice(context: Any?) {
    if (mpDeviceInitialized) return
    val ctx = context as? Context ?: return
    DeviceSDK.getInstance().execute(ctx)
    mpDeviceInitialized = true
}

actual suspend fun fetchMpDeviceSession(context: Any?, publicKey: String): String {
    val ctx = (context as? Context) ?: MainApplication.instance
    return withContext(Dispatchers.IO) {
        try {
            val gson =
                GsonBuilder().setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
                    .create()
            val device = DeviceSDK.getInstance()?.info
            val fingerprintJson = gson.toJsonTree(device).asJsonObject?.get("fingerprint")
            val requestJson = JsonObject().apply {
                add("finger_print", fingerprintJson)
                addProperty("site_id", "MPE")
            }
            val body = gson.toJson(requestJson)
            val request = Request.Builder()
                .url("https://api.mercadopago.com/cho-off/v1/devices/session")
                .addHeader("X-Public-Key", publicKey)
                .put(body.toRequestBody("application/json".toMediaType()))
                .build()
            val response = OkHttpClient().newCall(request).execute()
            val responseBody = response.body?.string() ?: ""
            val session = JSONObject(responseBody).optString("meli_session_id", "")
            println("[MP-DEVICE] session=$session")
            session
        } catch (e: Exception) {
            println("[MP-DEVICE] error=${e.message}")
            ""
        }
    }
}
