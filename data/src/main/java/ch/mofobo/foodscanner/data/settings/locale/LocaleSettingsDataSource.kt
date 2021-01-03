package ch.mofobo.foodscanner.data.settings.locale

import java.util.*

interface LocaleSettingsDataSource {

    fun setLocale(locale: Locale)

    fun getLocale(): Locale?

    fun removeLocale()
}