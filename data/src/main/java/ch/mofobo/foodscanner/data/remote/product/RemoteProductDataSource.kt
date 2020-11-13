package ch.mofobo.foodscanner.data.remote.product

import ch.mofobo.foodscanner.domain.model.Product
import ch.mofobo.foodscanner.domain.model.SearchRequest
import ch.mofobo.foodscanner.domain.model.SearchResponse
import retrofit2.Response

interface RemoteProductDataSource {

    suspend fun fetchProduct(id: Long): Response<Product>
    suspend fun fetchProduct(searchRequest: SearchRequest): Response<SearchResponse>
}