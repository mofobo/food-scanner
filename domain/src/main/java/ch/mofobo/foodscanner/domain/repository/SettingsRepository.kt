package ch.mofobo.foodscanner.domain.repository

import java.util.*

interface SettingsRepository {

    fun setLocale(locale: Locale)
    fun getLocale(): Locale?
    fun removeLocale()

}