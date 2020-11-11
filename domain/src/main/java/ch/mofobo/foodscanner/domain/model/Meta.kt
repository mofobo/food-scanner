package ch.mofobo.foodscanner.domain.model

import com.squareup.moshi.Json

data class Meta(
    @field:Json(name = "api_version") val apiVersion: Double,
    @field:Json(name = "generated_in") val generatedIn: Int
)