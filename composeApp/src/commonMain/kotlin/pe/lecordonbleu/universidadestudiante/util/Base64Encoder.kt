package pe.lecordonbleu.universidadestudiante.util

import kotlin.io.encoding.Base64
import kotlin.io.encoding.ExperimentalEncodingApi

@OptIn(ExperimentalEncodingApi::class)
object Base64Encoder {
    fun encodeToBase64(input: String): String {
        val data: ByteArray = input.encodeToByteArray()
        return Base64.encode(data)
    }
}
