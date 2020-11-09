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
)