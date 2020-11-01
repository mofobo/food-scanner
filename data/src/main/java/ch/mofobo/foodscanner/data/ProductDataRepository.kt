package ch.mofobo.foodscanner.data


import ch.mofobo.foodscanner.data.remote.product.ProductDataSource
import ch.mofobo.foodscanner.domain.model.Product
import ch.mofobo.foodscanner.domain.repository.ProductRepository
import retrofit2.Response

class ProductDataRepository constructor(private val dataSource: ProductDataSource) :
    ProductRepository {

    override suspend fun getProduct(id: Long): Response<Product> = dataSource.getProduct(id)
}