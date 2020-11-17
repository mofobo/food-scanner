package ch.mofobo.foodscanner.domain.model

import com.squareup.moshi.Json

data class Translations(
    @field:Json(name = "de")
    val german: String?,
    @field:Json(name = "fr")
    val french: String?,
    @field:Json(name = "it")
    val italien: String?,
    @field:Json(name = "en")
    val englisch: String?
) {
    fun getTranslation(lang: Lang, defaultValue: String): String {
        return when (lang) {
            Lang.ENGLISCH -> englisch
            Lang.FRENCH -> french
            Lang.GERMAN -> german
            Lang.ITALIAN -> italien
        } ?: defaultValue
    }

    /**
     * Try to return any tanslation if the main one is not available, otherwise return default value.
     */
    fun getAnyTranslation(lang: Lang, defaultValue: String): String {

        var translation = getTranslation(lang)

        if (!translation.isNullOrBlank()) return translation

        enumValues<Lang>().filter { it != lang }.forEach {
            translation = getTranslation(it)
            if (!translation.isNullOrBlank()) return translation!!
        }

        return defaultValue
    }

    private fun getTranslation(lang: Lang): String? {
        return when (lang) {
            Lang.ENGLISCH -> englisch
            Lang.FRENCH -> french
            Lang.GERMAN -> german
            Lang.ITALIAN -> italien
        }
    }
}