package ch.mofobo.foodscanner.data.settings.locale

import android.content.Context
import android.content.SharedPreferences
import ch.mofobo.foodscanner.domain.repository.SettingsRepository
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.lang.reflect.Type
import java.util.*

class LocaleSettingsDataSourceImpl constructor(context: Context) : LocaleSettingsDataSource {

    private var sharedPreferences: SharedPreferences
    private var localeType: Type
    private val gson: Gson = Gson()

    init {
        sharedPreferences = context.getSharedPreferences(PREFERENCES_KEY, Context.MODE_PRIVATE)
        localeType = object : TypeToken<Locale>() {}.type
    }


    override fun setLocale(locale: Locale) {
        sharedPreferences.edit().putString(getSetttingsKey(LOCALE), gson.toJson(locale)).apply()
    }

    override fun getLocale(): Locale? {
        return try {
            gson.fromJson(sharedPreferences.getString(getSetttingsKey(LOCALE), null), localeType)

        } catch (e: Exception) {
            null
        }
    }

    override fun removeLocale() {
        sharedPreferences.edit().remove(getSetttingsKey(LOCALE)).apply()
    }

    private fun getSetttingsKey(suffix: String): String {
        return "${TAG}_${suffix}"
    }

    companion object {
        private const val PREFERENCES_KEY = "STORAGE"
        private const val LOCALE = "LOCALE"

        private val TAG = SettingsRepository::class.simpleName
    }


}