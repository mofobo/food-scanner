package ch.mofobo.foodscanner.features.details

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ch.mofobo.foodscanner.domain.exception.BaseException
import ch.mofobo.foodscanner.domain.model.Product
import ch.mofobo.foodscanner.domain.repository.ProductRepository
import ch.mofobo.foodscanner.utils.NetworkHelper
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class DetailsViewModel(
    private val productRepository: ProductRepository,
    private val networkHelper: NetworkHelper
) : ViewModel() {

    val product = MutableLiveData<Product>()
    val error = MutableLiveData<BaseException>()

    private val viewModelJob = Job()
    private val coroutineScope = CoroutineScope(viewModelJob + Dispatchers.IO)

    fun searchProduct(id: Long?, barcode: String?) {

        coroutineScope.launch {

            val localProduct = productRepository.get(id, barcode)
            if (localProduct != null) product.postValue(localProduct)

            if (networkHelper.isNetworkConnected()) {

                try {
                    val remoteProduct = when {
                        id != null -> productRepository.fetchProduct(id)
                        barcode != null -> productRepository.fetchProduct(barcode)
                        else -> throw BaseException.ProductSearchException
                    }

                    productRepository.add(remoteProduct)
                    product.postValue(remoteProduct)

                } catch (e: BaseException) {
                    error.postValue(e)
                }

            } else {
                error.postValue(BaseException.NetworkException)
            }

        }
    }

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }
}