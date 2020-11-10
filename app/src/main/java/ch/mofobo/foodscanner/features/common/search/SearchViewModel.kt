package ch.mofobo.foodscanner.features.common.search

import androidx.lifecycle.ViewModel
import ch.mofobo.foodscanner.data.ProductDataRepository
import ch.mofobo.foodscanner.domain.model.*
import ch.mofobo.foodscanner.utils.NetworkHelper
import ch.mofobo.foodscanner.utils.Resource
import ch.mofobo.foodscanner.utils.SingleLiveEvent
import kotlinx.coroutines.*
import kotlin.reflect.full.memberProperties

class SearchViewModel(
    private val productDataRepository: ProductDataRepository,
    private val networkHelper: NetworkHelper
) : ViewModel() {

    val product = SingleLiveEvent<Resource<Product?>>()
    val actions = SingleLiveEvent<Action>()

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

    fun retrieveNutrientsTable(nutrients: Nutrients, nutrientsHtmlTemplate: String) {
        coroutineScope.async {
            try {
                var nutrientsHtmlTemplateStr = nutrientsHtmlTemplate
                val nutrientInfosHTML = mutableListOf<String>()
                for (property in Nutrients::class.memberProperties) {
                    val nutrient = property.call(nutrients) as NutrientInfo?
                    nutrient?.let {

                        val name = nutrient.nameTranslations.getTranslation(Lang.FRENCH, property.name)
                        val qty = nutrient.getQty()
                        val nutriRec = "69%"

                        val nutrientInfoHtml = String.format(NUTRIENT_MAIN_HTML_TEMPLATE, name, qty, nutriRec)
                        nutrientInfosHTML.add(nutrientInfoHtml)
                    }
                }
                nutrientsHtmlTemplateStr = nutrientsHtmlTemplateStr.replace("[NUTRIENTS_ITEMS]", nutrientInfosHTML.joinToString(separator = ""))
                actions.postValue(Action.NutrientsTable(nutrientsHtmlTemplate))
            } catch (e: Exception) {
            }
        }
    }


    sealed class Action {
        data class NutrientsTable(val html: String) : Action()
    }

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }


    companion object {
        private const val NUTRIENT_MAIN_HTML_TEMPLATE = "<tr><th colspan=\"2\"><b>%1s</b>%2s</th><td><b>%3s</b></td></tr>"
        private const val NUTRIENT_SUB_HTML_TEMPLATE = "<tr><td class=\"blank-cell\"></td><th>[NUTRIENT_NAME] [NUTRIENT_QTY]</th><td><b>[NUTRIENT_REC]</b></td></tr>"
    }

}