package pe.lecordonbleu.universidadestudiante

import android.content.Context
import com.mercadopago.sdk.android.domain.model.CountryCode
import com.mercadopago.sdk.android.initializer.MercadoPagoSDK
import kotlin.text.isEmpty

actual fun initMpSdkIfNeeded(context: Any?, publicKey: String) {
    if (publicKey.isEmpty()) return
    val ctx: Context = (context as? Context)?.applicationContext ?: MainApplication.instance
    if (!MercadoPagoSDK.isInitialized) {
        MercadoPagoSDK.initialize(ctx, publicKey, CountryCode.PER)
    } else {
        MercadoPagoSDK.setNewConfiguration(publicKey, CountryCode.PER)
    }
}
