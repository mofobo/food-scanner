package ch.mofobo.foodscanner.features.details

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
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
    val error = MutableLiveData<Throwable>()

    private val viewModelJob = Job()
    private val coroutineScope = CoroutineScope(viewModelJob + Dispatchers.IO)

    fun searchProduct(id: Long?, barcode: String?) {

        coroutineScope.launch {
            try {

                val localProduct = productRepository.get(id, barcode)
                if (localProduct != null) product.postValue(localProduct)

                if (networkHelper.isNetworkConnected()) {

                    val remoteProduct = when {
                        id != null -> productRepository.fetchProduct(id)
                        barcode != null -> productRepository.fetchProduct(barcode)
                        else -> throw Exception("DetailsViewModel.searchProduct(id?, barcode?): both are null")
                    }

                    productRepository.add(remoteProduct)
                    product.postValue(remoteProduct)

                } else {
                    error.postValue(Throwable("No Internet :-("))
                }

            } catch (e: Exception) {
                error.postValue(Throwable(e.message ?: "No message?"))
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }
}