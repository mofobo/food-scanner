package ch.mofobo.foodscanner.domain.model

import com.squareup.moshi.Json
import java.util.*

data class SourceWrapper(
    val _score: Double,
    val _source: Product,
    @field:Json(name = "created_at") val createdAt: String,
    @field:Json(name = "updated_at") val updatedAt: String
)