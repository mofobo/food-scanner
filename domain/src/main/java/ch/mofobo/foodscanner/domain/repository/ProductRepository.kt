package ch.mofobo.foodscanner.domain.repository

import ch.mofobo.foodscanner.domain.model.Product

interface ProductRepository {

    suspend fun fetchProduct(id: Long): Product

    suspend fun fetchProduct(barcode: String): Product

    suspend fun add(product: Product)

    suspend fun get(id: Long?, barcode: String?): Product?

    suspend fun getAll(): List<Product>

    suspend fun remove(product: Product)

    suspend fun removeAll()
}