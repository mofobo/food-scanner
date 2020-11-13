package ch.mofobo.foodscanner.data

import ch.mofobo.foodscanner.data.locale.LocalProductDataSource
import ch.mofobo.foodscanner.data.remote.product.RemoteProductDataSource
import ch.mofobo.foodscanner.domain.model.Product
import ch.mofobo.foodscanner.domain.model.SearchRequest
import ch.mofobo.foodscanner.domain.model.SearchResponse
import ch.mofobo.foodscanner.domain.repository.ProductRepository
import retrofit2.Response

class ProductDataRepository constructor(
    private val remoteProductDataSource: RemoteProductDataSource,
    private val localProductDataSource: LocalProductDataSource
) : ProductRepository {

    override suspend fun fetchProduct(id: Long): Response<Product> = remoteProductDataSource.fetchProduct(id)

    override suspend fun fetchProduct(searchRequest: SearchRequest): Response<SearchResponse> = remoteProductDataSource.fetchProduct(searchRequest)

    override suspend fun add(product: Product) = localProductDataSource.add(product)

    override suspend fun remove(product: Product) = localProductDataSource.remove(product)

    override suspend fun getAll(): List<Product> = localProductDataSource.getAll()

    override suspend fun removeAll() = localProductDataSource.removeAll()
}