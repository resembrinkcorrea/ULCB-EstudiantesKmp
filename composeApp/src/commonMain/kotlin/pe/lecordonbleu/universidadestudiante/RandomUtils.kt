package pe.lecordonbleu.universidadestudiante

fun randomAlphanumeric4(): String {
    val chars = ('a'..'z').toList() + ('0'..'9').toList()
    return (1..4).map { chars.random() }.joinToString("")
}
