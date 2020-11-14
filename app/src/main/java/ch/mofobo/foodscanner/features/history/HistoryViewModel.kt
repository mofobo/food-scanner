package ch.mofobo.foodscanner.features.history

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ch.mofobo.foodscanner.domain.model.Product
import ch.mofobo.foodscanner.domain.repository.ProductRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class HistoryViewModel(val productRepository: ProductRepository) : ViewModel() {

    private val viewModelJob = Job()
    private val coroutineScope = CoroutineScope(viewModelJob + Dispatchers.IO)

    val products = MutableLiveData<List<Product>>()
    val error = MutableLiveData<Throwable>()

    fun getProducts() {
        coroutineScope.launch {
            try {
                val localProducts = productRepository.getAll()
                products.postValue(localProducts)
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