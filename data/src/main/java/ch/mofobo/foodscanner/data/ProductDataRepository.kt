package ch.mofobo.foodscanner.data

import ch.mofobo.foodscanner.data.locale.LocaleProductDataSource
import ch.mofobo.foodscanner.data.remote.RemoteProductDataSource
import ch.mofobo.foodscanner.domain.model.Product
import ch.mofobo.foodscanner.domain.repository.ProductRepository

class ProductDataRepository constructor(
    private val remoteProductDataSource: RemoteProductDataSource,
    private val localeProductDataSource: LocaleProductDataSource
) : ProductRepository {

    override suspend fun fetchProduct(id: Long): Product = remoteProductDataSource.fetchProduct(id)

    override suspend fun fetchProduct(barcode: String): Product = remoteProductDataSource.fetchProduct(barcode)

    override suspend fun add(product: Product) = localeProductDataSource.add(product)

    override suspend fun add(product: Product, position: Int) = localeProductDataSource.add(product, position)

    override suspend fun get(id: Long?, barcode: String?): Product? = localeProductDataSource.get(id, barcode)

    override suspend fun getAll(): List<Product> = localeProductDataSource.getAll()

    override suspend fun remove(product: Product) = localeProductDataSource.remove(product)

    override suspend fun removeAll() = localeProductDataSource.removeAll()
}