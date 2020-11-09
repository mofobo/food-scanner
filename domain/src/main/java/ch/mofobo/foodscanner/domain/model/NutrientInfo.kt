package ch.mofobo.foodscanner.domain.model

import com.squareup.moshi.Json

data class NutrientInfo(
    @field:Json(name = "name_translations") val nameTranslations: Translations,
    val unit: String,
    @field:Json(name = "per_hundred") val perHundred: Double?,
    @field:Json(name = "per_portion") val perPortion: Double?,
    @field:Json(name = "per_day") val perDay: Double?
)