package ch.mofobo.foodscanner.data.locale

import android.content.Context
import android.content.SharedPreferences
import ch.mofobo.foodscanner.domain.model.Product
import ch.mofobo.foodscanner.domain.repository.ProductRepository
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.lang.reflect.Type

class LocaleProductDataSourceImpl constructor(context: Context) : LocaleProductDataSource {

    private var sharedPreferences: SharedPreferences
    private var listType: Type
    private val gson: Gson = Gson()

    init {
        sharedPreferences = context.getSharedPreferences(PREFERENCES_KEY, Context.MODE_PRIVATE)
        listType = object : TypeToken<ArrayList<Product>>() {}.type
    }

    override suspend fun add(product: Product) {
        val products = getAll().toMutableList()
        products.remove(product)
        products.add(product)
        persist(products)
    }

    override suspend fun get(id: Long?, barcode: String?): Product? {
        val products = getAll().toMutableList()
        return products.firstOrNull { it.id == id || it.barcode == barcode }
    }

    override suspend fun getAll(): List<Product> {
        return try {
            val emptyJSONArray = "[]"
            gson.fromJson(sharedPreferences.getString(TAG, emptyJSONArray), listType)

        } catch (e: Exception) {
            emptyList()
        }
    }

    override suspend fun remove(product: Product) {
        val products = getAll().toMutableList()
        products.remove(product)
        persist(products)
    }

    override suspend fun removeAll() {
        persist(arrayListOf())
    }


    private fun persist(products: List<Product>) {
        sharedPreferences.edit().putString(TAG, gson.toJson(products)).apply()
    }

    companion object {
        private const val PREFERENCES_KEY = "STORAGE"
        private val TAG = ProductRepository::class.simpleName
    }
}