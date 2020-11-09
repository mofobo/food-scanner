package ch.mofobo.foodscanner.domain.model

import com.squareup.moshi.Json

data class Nutrients(
    val salt: NutrientInfo?,
    val protein: NutrientInfo?,
    val fiber: NutrientInfo?,
    val sugars: NutrientInfo?,
    val carbohydrates: NutrientInfo?,
    @field:Json(name = "saturated_fat") val saturatedFat: NutrientInfo?,
    val fat: NutrientInfo?,
    @field:Json(name = "energy_kcal") val energyKcal: NutrientInfo?,
    val energy: NutrientInfo?
)