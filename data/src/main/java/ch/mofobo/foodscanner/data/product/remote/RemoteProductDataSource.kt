package ch.mofobo.foodscanner.data.product.remote

import ch.mofobo.foodscanner.domain.model.Product

interface RemoteProductDataSource {

    suspend fun fetchProduct(id: Long): Product

    suspend fun fetchProduct(barcode: String): Product
}