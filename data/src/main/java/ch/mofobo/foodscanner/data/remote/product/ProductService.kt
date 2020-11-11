package ch.mofobo.foodscanner.data.remote.product

import ch.mofobo.foodscanner.domain.model.Product
import ch.mofobo.foodscanner.domain.model.SearchRequest
import ch.mofobo.foodscanner.domain.model.SearchResponse
import retrofit2.Response
import retrofit2.http.*

interface ProductService {

    @GET("products/{id}")
    suspend fun getProduct(@Path("id") reservationID: Long): Response<Product>

    //@Headers("Content-Type: application/json")
    @POST("products/_search")
    suspend fun getProduct(@Body searchRequest: SearchRequest): Response<SearchResponse>

}