package ch.mofobo.foodscanner.features.common.search

import androidx.lifecycle.ViewModel
import ch.mofobo.foodscanner.data.ProductDataRepository
import ch.mofobo.foodscanner.domain.model.*
import ch.mofobo.foodscanner.utils.NetworkHelper
import ch.mofobo.foodscanner.utils.Resource
import ch.mofobo.foodscanner.utils.SingleLiveEvent
import kotlinx.coroutines.*

class SearchViewModel(
    private val productDataRepository: ProductDataRepository,
    private val networkHelper: NetworkHelper
) : ViewModel() {

    val product = SingleLiveEvent<Resource<Product?>>()

    private val viewModelJob = Job()
    private val coroutineScope = CoroutineScope(viewModelJob + Dispatchers.IO)

    fun searchProduct(barcode: String) {
        val request = SearchRequest(Query(Terms(barcode = listOf(barcode))))
        coroutineScope.launch {
            val getProperty = productDataRepository.fetchProduct(request)
            try {
                product.postValue(Resource.success(getProperty.body()?.product))
            } catch (e: Exception) {
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }
}