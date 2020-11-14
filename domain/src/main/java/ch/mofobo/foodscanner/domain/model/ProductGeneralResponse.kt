package ch.mofobo.foodscanner.domain.model

import com.squareup.moshi.Json

data class ProductGeneralResponse(
    @field:Json(name = "data") val product: Product?
)