package ch.mofobo.foodscanner.domain.model

import com.squareup.moshi.Json

data class NutrientInfo(
    @field:Json(name = "name_translations") val nameTranslations: Translations,
    val unit: String,
    @field:Json(name = "per_hundred") val perHundred: Double?,
    @field:Json(name = "per_portion") val perPortion: Double?,
    @field:Json(name = "per_day") val perDay: Double?
) {

    fun getQty(): String {
        val valueDouble: Double? = when {
            perDay != null -> perDay
            perHundred != null -> perHundred
            perPortion != null -> perPortion
            else -> null
        }

        val formattedValueStr = trimTrailingZero(valueDouble, "-")
        return " $formattedValueStr $unit"
    }

    private fun trimTrailingZero(double: Double?, defaultValue: String): String {
        val doubleStr = double.toString()
        val result = when {
            doubleStr.isNullOrBlank() -> defaultValue
            doubleStr.indexOf(".") < 0 -> doubleStr
            else -> doubleStr.replace("0*$".toRegex(), "").replace("\\.$".toRegex(), "")
        }
        return result
    }


}