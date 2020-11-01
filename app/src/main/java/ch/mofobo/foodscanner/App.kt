package ch.mofobo.foodscanner

import android.app.Application
import ch.mofobo.foodscanner.di.module.appModule
import ch.mofobo.foodscanner.di.module.repoModule
import ch.mofobo.foodscanner.di.module.viewModelModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class App : Application() {

    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@App)
            modules(listOf(appModule, repoModule, viewModelModule))
        }
    }
}