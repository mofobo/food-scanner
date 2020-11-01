package ch.mofobo.foodscanner.domain.repository

import ch.mofobo.foodscanner.domain.model.Product
import retrofit2.Response

interface ProductRepository {

    suspend fun getProduct(id: Long): Response<Product>
}