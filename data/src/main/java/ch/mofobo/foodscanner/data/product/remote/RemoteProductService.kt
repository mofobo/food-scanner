package ch.mofobo.foodscanner.data.product.remote

import ch.mofobo.foodscanner.domain.model.ProductGeneralResponse
import ch.mofobo.foodscanner.domain.model.ProductSearchGeneralResponse
import ch.mofobo.foodscanner.domain.model.SearchRequest
import retrofit2.Response
import retrofit2.http.*

interface RemoteProductService {

    @GET("products/{id}")
    suspend fun getProduct(@Path("id") reservationID: Long): Response<ProductGeneralResponse>

    @POST("products/_search")
    suspend fun getProduct(@Body searchRequest: SearchRequest): Response<ProductSearchGeneralResponse>

}