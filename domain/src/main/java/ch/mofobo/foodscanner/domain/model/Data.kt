package ch.mofobo.foodscanner.domain.model

data class Data(

    val id: Int,
    val country: String,
    val barcode: String,
    val name_translations: Name_translations,
    val display_name_translations: Display_name_translations,
    val ingredients_translations: Ingredients_translations,
    val origin_translations: Origin_translations,
    val status: String,
    val quantity: Int,
    val unit: String,
    val hundred_unit: String,
    val portion_quantity: Int,
    val portion_unit: String,
    val alcohol_by_volume: Int,
    val images: List<Images>,
    val nutrients: Nutrients,
    val created_at: String,
    val updated_at: String
)