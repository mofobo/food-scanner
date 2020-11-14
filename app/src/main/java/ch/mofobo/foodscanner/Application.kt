package ch.mofobo.foodscanner

import android.app.Application
import androidx.camera.camera2.Camera2Config
import androidx.camera.core.CameraXConfig
import ch.mofobo.foodscanner.di.repositoryModule
import ch.mofobo.foodscanner.di.serviceModule
import ch.mofobo.foodscanner.di.viewModelModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class Application : Application(), CameraXConfig.Provider {

    override fun getCameraXConfig(): CameraXConfig {
        return Camera2Config.defaultConfig()
    }

    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@Application)
            modules(
                listOf(
                    serviceModule,
                    repositoryModule,
                    viewModelModule
                )
            )
        }
    }
}