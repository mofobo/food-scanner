package ch.mofobo.foodscanner.data.remote.product

import ch.mofobo.foodscanner.domain.model.Product
import ch.mofobo.foodscanner.domain.model.SearchRequest
import ch.mofobo.foodscanner.domain.model.SearchResponse
import retrofit2.Response

class RemoteProductDataSourceImpl(private val remoteProductService: RemoteProductService) : RemoteProductDataSource {

    override suspend fun fetchProduct(id: Long): Response<Product> = remoteProductService.getProduct(id)
    override suspend fun fetchProduct(searchRequest: SearchRequest): Response<SearchResponse> = remoteProductService.getProduct(searchRequest)

}