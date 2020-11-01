package ch.mofobo.foodscanner.features.scanner

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ch.mofobo.foodscanner.data.ProductDataRepository
import ch.mofobo.foodscanner.domain.model.Product
import ch.mofobo.foodscanner.utils.NetworkHelper
import ch.mofobo.foodscanner.utils.Resource
import kotlinx.coroutines.launch

class ScannerViewModel(
    private val productDataRepository: ProductDataRepository,
    private val networkHelper: NetworkHelper
) : ViewModel() {

    private val _products = MutableLiveData<Resource<Product>>()
    val products: LiveData<Resource<Product>>
        get() = _products


    init {
        fetchProducts()
    }

    private fun fetchProducts() {
        viewModelScope.launch {
            _products.postValue(Resource.loading(null))
            if (networkHelper.isNetworkConnected()) {
                productDataRepository.getProduct(2030).let {
                    if (it.isSuccessful) {
                        _products.postValue(Resource.success(it.body()))
                    } else _products.postValue(Resource.error(it.errorBody().toString(), null))
                }
            } else _products.postValue(Resource.error("No internet connection", null))
        }
    }
}