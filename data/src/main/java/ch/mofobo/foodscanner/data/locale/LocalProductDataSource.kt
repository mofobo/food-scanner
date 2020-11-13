package ch.mofobo.foodscanner.data.locale

import ch.mofobo.foodscanner.domain.model.Product

interface LocalProductDataSource {

    suspend fun getAll(): List<Product>

    suspend fun add(product: Product)

    suspend fun remove(product: Product)

    suspend fun removeAll()
}