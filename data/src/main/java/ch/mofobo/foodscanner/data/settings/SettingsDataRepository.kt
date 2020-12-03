package ch.mofobo.foodscanner.data.settings

import ch.mofobo.foodscanner.data.settings.locale.LocaleSettingsDataSource
import ch.mofobo.foodscanner.domain.repository.SettingsRepository
import java.util.*

class SettingsDataRepository constructor(private val localeSettingsDataSource: LocaleSettingsDataSource) : SettingsRepository {

    override fun setLocale(locale: Locale) = localeSettingsDataSource.setLocale(locale)

    override fun getLocale() = localeSettingsDataSource.getLocale()

    override fun removeLocale() =localeSettingsDataSource.removeLocale()
}