package ch.mofobo.foodscanner.data.remote.product

import ch.mofobo.foodscanner.domain.model.Product
import retrofit2.Response

interface ProductDataSource {

    suspend fun getProduct(id: Long): Response<Product>
}