package ch.mofobo.foodscanner.data.locale

import ch.mofobo.foodscanner.domain.model.Product

interface LocaleProductDataSource {

    suspend fun add(product: Product)

    suspend fun get(id: Long?, barcode: String?): Product?

    suspend fun getAll(): List<Product>

    suspend fun remove(product: Product)

    suspend fun removeAll()
}