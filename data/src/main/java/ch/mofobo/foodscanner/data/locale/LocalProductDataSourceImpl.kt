package ch.mofobo.foodscanner.data.locale

import android.content.Context
import android.content.SharedPreferences
import ch.mofobo.foodscanner.domain.model.Product
import ch.mofobo.foodscanner.domain.repository.ProductRepository
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.lang.reflect.Type

class LocalProductDataSourceImpl constructor(context: Context) : LocalProductDataSource {

    private var sharedPreferences: SharedPreferences
    private var listType: Type
    private lateinit var gson: Gson

    init {

        gson = Gson()

        sharedPreferences = context.getSharedPreferences(PREFERENCES_KEY, Context.MODE_PRIVATE)
        listType = object : TypeToken<ArrayList<Product>>() {}.type
    }


    override suspend fun getAll(): List<Product> {
        return try {
            val emptyJSONArray = "[]"
            gson.fromJson(sharedPreferences.getString(TAG, emptyJSONArray), listType)

        } catch (e: Exception) {
            emptyList()
        }
    }

    override suspend fun add(product: Product) {
        val tickets = getAll().toMutableList()
        tickets.remove(product)
        tickets.add(product)
        persist(tickets)
    }

    override suspend fun remove(product: Product) {
        val tickets = getAll().toMutableList()
        tickets.remove(product)
        persist(tickets)
    }

    override suspend fun removeAll() {
        persist(arrayListOf())
    }


    private fun persist(tickets: List<Product>) {
        sharedPreferences.edit().putString(TAG, gson.toJson(tickets)).apply()
    }

    companion object {
        private const val PREFERENCES_KEY = "STORAGE"
        private val TAG = ProductRepository::class.simpleName
    }
}