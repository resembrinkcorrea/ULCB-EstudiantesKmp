package pe.lecordonbleu.universidadestudiante

import android.app.Application
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import pe.lecordonbleu.universidadestudiante.core.di.appModule

class MainApplication : Application() {

    companion object {
        lateinit var instance: MainApplication
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
        initializeKoin()
    }

    private fun initializeKoin() {
        startKoin {
            androidContext(this@MainApplication)
            androidLogger()
            modules(appModule())
        }
    }
}
