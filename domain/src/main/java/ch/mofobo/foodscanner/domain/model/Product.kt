package ch.mofobo.foodscanner.domain.model

data class Product(

    val id: Int,
    val country: String,
    val barcode: String,
    val name_translations: Translations,
    val display_name_translations: Translations,
    val ingredients_translations: Translations,
    val origin_translations: Translations,
    val status: String,
    val quantity: Int,
    val unit: String,
    val portion_quantity: Int,
    val portion_unit: String,
    val alcohol_by_volume: Int,
    val images: List<Images>,
    val nutrients: Nutrients,
    val created_at: String,
    val updated_at: String
)