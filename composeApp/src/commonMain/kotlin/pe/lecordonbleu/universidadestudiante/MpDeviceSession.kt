package pe.lecordonbleu.universidadestudiante

// Inicia la colección de fingerprint en background (llamar al entrar a PagoYapeScreen)
expect fun initMpDevice(context: Any?)

// Obtiene el device session ID usando la publicKey ya disponible (llamar en el ViewModel)
expect suspend fun fetchMpDeviceSession(context: Any?, publicKey: String): String
