package ch.mofobo.foodscanner.data.product.remote

import ch.mofobo.foodscanner.domain.exception.BaseException
import ch.mofobo.foodscanner.domain.model.Product
import ch.mofobo.foodscanner.domain.model.Query
import ch.mofobo.foodscanner.domain.model.SearchRequest
import ch.mofobo.foodscanner.domain.model.Terms

class RemoteProductDataSourceImpl(private val remoteProductService: RemoteProductService) : RemoteProductDataSource {

    override suspend fun fetchProduct(id: Long): Product {

        val response = remoteProductService.getProduct(id)

        response.let {
            if (it.isSuccessful) it.body()?.let { it.product?.let { return it } }
        }

        throw BaseException.ProductNotFoundException(id.toString())
    }

    override suspend fun fetchProduct(barcode: String): Product {

        val searchRequest = SearchRequest(Query(Terms(barcode = listOf(barcode))))

        val response = remoteProductService.getProduct(searchRequest)

        response.let {
            if (it.isSuccessful) it.body()?.let { it.product?.let { return it } }
        }

        throw BaseException.ProductNotFoundException(barcode)
    }
}
