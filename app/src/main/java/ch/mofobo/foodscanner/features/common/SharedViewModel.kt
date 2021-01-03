package ch.mofobo.foodscanner.features.common

import androidx.lifecycle.ViewModel
import ch.mofobo.foodscanner.domain.repository.SettingsRepository
import org.koin.java.KoinJavaComponent.inject
import java.util.*

class SharedViewModel : ViewModel() {

    private val settingsRepository: SettingsRepository by inject(SettingsRepository::class.java)

    var barcode: String = ""

    fun setLocale(locale: Locale) {
        settingsRepository.setLocale(locale)
    }

    fun removeLocale() {
        settingsRepository.removeLocale()
    }

    fun getLocale(): Locale? {
        return settingsRepository.getLocale()
    }
}