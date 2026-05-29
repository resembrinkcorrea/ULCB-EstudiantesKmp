package pe.lecordonbleu.universidadestudiante

interface SettingsStorage {
    fun putString(key: String, value: String)
    fun getString(key: String, defaultValue: String? = null): String?
    fun putInt(key: String, value: Int)
    fun getInt(key: String, defaultValue: Int = 0): Int
    fun removeKey(key: String)
    fun clearAll()
}

expect fun getSettingsStorage(): SettingsStorage
