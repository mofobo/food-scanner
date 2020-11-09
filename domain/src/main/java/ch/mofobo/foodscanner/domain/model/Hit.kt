package ch.mofobo.foodscanner.domain.model

import com.squareup.moshi.Json

data class Hit(
    val total: Int,
    @field:Json(name = "max_score") val maxScore: Double,
    val hits: List<SourceWrapper>
)