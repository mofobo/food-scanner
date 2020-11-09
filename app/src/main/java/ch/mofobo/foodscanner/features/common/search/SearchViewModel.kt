package ch.mofobo.foodscanner.features.common.search

import androidx.lifecycle.ViewModel
import ch.mofobo.foodscanner.data.ProductDataRepository
import ch.mofobo.foodscanner.domain.model.Product
import ch.mofobo.foodscanner.domain.model.Query
import ch.mofobo.foodscanner.domain.model.SearchRequest
import ch.mofobo.foodscanner.domain.model.Terms
import ch.mofobo.foodscanner.utils.NetworkHelper
import ch.mofobo.foodscanner.utils.Resource
import ch.mofobo.foodscanner.utils.SingleLiveEvent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

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
            val getProperty = productDataRepository.getProduct(request)
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