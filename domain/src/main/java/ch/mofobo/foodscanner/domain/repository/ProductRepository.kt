package ch.mofobo.foodscanner.domain.repository

import ch.mofobo.foodscanner.domain.model.Product
import ch.mofobo.foodscanner.domain.model.SearchRequest
import ch.mofobo.foodscanner.domain.model.SearchResponse
import retrofit2.Response

interface ProductRepository {

    suspend fun getProduct(id: Long): Response<Product>
    suspend fun getProduct(searchRequest: SearchRequest): Response<SearchResponse>
}