package pe.lecordonbleu.universidadestudiante

expect fun getStoreUpdateUrl(): String
expect fun getFirebaseVersionUrl(): String
expect fun fetchFirebaseVersion(onVersion: (String) -> Unit): () -> Unit
