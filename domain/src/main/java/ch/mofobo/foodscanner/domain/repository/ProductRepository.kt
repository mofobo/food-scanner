package ch.mofobo.foodscanner.domain.repository

import ch.mofobo.foodscanner.domain.model.Product
import ch.mofobo.foodscanner.domain.model.SearchRequest
import ch.mofobo.foodscanner.domain.model.SearchResponse
import retrofit2.Response

interface ProductRepository {

    suspend fun fetchProduct(id: Long): Response<Product>

    suspend fun fetchProduct(searchRequest: SearchRequest): Response<SearchResponse>

    suspend fun getAll(): List<Product>

    suspend fun add(product: Product)

    suspend fun remove(product: Product)

    suspend fun removeAll()
}