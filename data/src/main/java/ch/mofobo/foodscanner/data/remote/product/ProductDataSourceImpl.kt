package ch.mofobo.foodscanner.data.remote.product

import ch.mofobo.foodscanner.domain.model.Product
import ch.mofobo.foodscanner.domain.model.SearchRequest
import ch.mofobo.foodscanner.domain.model.SearchResponse
import retrofit2.Response

class ProductDataSourceImpl(private val apiService: ProductService) : ProductDataSource {

    override suspend fun getProduct(id: Long): Response<Product> = apiService.getProduct(id)
    override suspend fun getProduct(searchRequest: SearchRequest): Response<SearchResponse> = apiService.getProduct(searchRequest)

}