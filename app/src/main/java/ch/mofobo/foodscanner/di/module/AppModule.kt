package ch.mofobo.foodscanner.di.module

import android.content.Context
import ch.mofobo.foodscanner.BuildConfig
import ch.mofobo.foodscanner.data.remote.product.ProductDataSource
import ch.mofobo.foodscanner.data.remote.product.ProductDataSourceImpl
import ch.mofobo.foodscanner.data.remote.product.ProductService
import ch.mofobo.foodscanner.utils.FoodRepoAuthenticationInterceptor
import ch.mofobo.foodscanner.utils.NetworkHelper
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

val appModule = module {
    single { provideOkHttpClient() }
    single { provideRetrofit(get(), BuildConfig.FOODREPO_BASE_URL) }
    single { provideProductService(get()) }
    single { provideNetworkHelper(androidContext()) }

    single<ProductDataSource> {
        return@single ProductDataSourceImpl(get())
    }
}

private fun provideNetworkHelper(context: Context) = NetworkHelper(context)

private fun provideOkHttpClient() = if (BuildConfig.DEBUG) {
    val loggingInterceptor = HttpLoggingInterceptor()
    val foodRepoAuthenticationInterceptor = FoodRepoAuthenticationInterceptor()
    loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY)
    OkHttpClient.Builder()
        .addInterceptor(foodRepoAuthenticationInterceptor)
        .addInterceptor(loggingInterceptor)
        .build()
} else OkHttpClient
    .Builder()
    .build()

private fun provideRetrofit(
    okHttpClient: OkHttpClient,
    BASE_URL: String
): Retrofit =
    Retrofit.Builder()
        .addConverterFactory(MoshiConverterFactory.create())
        .baseUrl(BASE_URL)
        .client(okHttpClient)
        .build()

private fun provideProductService(retrofit: Retrofit): ProductService =
    retrofit.create(
        ProductService::class.java
    )