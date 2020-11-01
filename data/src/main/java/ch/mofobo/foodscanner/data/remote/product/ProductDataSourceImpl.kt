package ch.mofobo.foodscanner.data.remote.product

import ch.mofobo.foodscanner.domain.model.Product
import retrofit2.Response

class ProductDataSourceImpl(private val apiService: ProductService) :
    ProductDataSource {

    override suspend fun getProduct(id: Long): Response<Product> = apiService.getProduct(id)

}