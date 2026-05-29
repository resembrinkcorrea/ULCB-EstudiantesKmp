package pe.lecordonbleu.universidadestudiante

import android.content.Context
import android.content.SharedPreferences
import org.koin.core.context.GlobalContext

class AndroidSettingsStorage(private val context: Context) : SettingsStorage {
    private val sharedPreferences: SharedPreferences =
        context.getSharedPreferences("AppPreferences", Context.MODE_PRIVATE)

    override fun putString(key: String, value: String) {
        sharedPreferences.edit().putString(key, value).apply()
    }

    override fun getString(key: String, defaultValue: String?): String? =
        sharedPreferences.getString(key, defaultValue)

    override fun putInt(key: String, value: Int) {
        sharedPreferences.edit().putInt(key, value).apply()
    }

    override fun getInt(key: String, defaultValue: Int): Int =
        sharedPreferences.getInt(key, defaultValue)

    override fun removeKey(key: String) {
        sharedPreferences.edit().remove(key).apply()
    }

    override fun clearAll() {
        sharedPreferences.edit().clear().apply()
    }
}

actual fun getSettingsStorage(): SettingsStorage {
    val context: Context = GlobalContext.get().get<Context>()
    return AndroidSettingsStorage(context)
}
