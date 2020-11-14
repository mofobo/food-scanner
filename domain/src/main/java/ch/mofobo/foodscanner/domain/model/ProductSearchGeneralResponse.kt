package ch.mofobo.foodscanner.domain.model

data class ProductSearchGeneralResponse(
    val took: Int,
    val timed_out: Boolean,
    val _shards: Shards,
    val hits: Hit,
    val meta: Meta
) {
    val product: Product?
        get() = hits.hits.firstOrNull()?._source
}