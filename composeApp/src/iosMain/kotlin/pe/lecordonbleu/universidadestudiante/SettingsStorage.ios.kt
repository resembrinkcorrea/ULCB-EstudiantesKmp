package pe.lecordonbleu.universidadestudiante

import platform.Foundation.NSUserDefaults

actual fun getSettingsStorage(): SettingsStorage = IOSSettingsStorage()

class IOSSettingsStorage : SettingsStorage {
    private val userDefaults = NSUserDefaults.standardUserDefaults

    override fun putString(key: String, value: String) {
        userDefaults.setObject(value, forKey = key)
    }

    override fun getString(key: String, defaultValue: String?): String? =
        userDefaults.stringForKey(key) ?: defaultValue

    override fun putInt(key: String, value: Int) {
        userDefaults.setInteger(value.toLong(), forKey = key)
    }

    override fun getInt(key: String, defaultValue: Int): Int =
        if (userDefaults.objectForKey(key) != null)
            userDefaults.integerForKey(key).toInt()
        else
            defaultValue

    override fun removeKey(key: String) {
        userDefaults.removeObjectForKey(key)
    }

    override fun clearAll() {
        userDefaults.dictionaryRepresentation().keys.forEach { key ->
            userDefaults.removeObjectForKey(key as String)
        }
    }
}
