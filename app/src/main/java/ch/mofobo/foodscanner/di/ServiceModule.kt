package ch.mofobo.foodscanner.di

import android.content.Context
import ch.mofobo.foodscanner.BuildConfig
import ch.mofobo.foodscanner.data.product.remote.RemoteProductService
import ch.mofobo.foodscanner.utils.FoodRepoAuthenticationInterceptor
import ch.mofobo.foodscanner.utils.NetworkHelper
import ch.mofobo.foodscanner.utils.TimeoutInterceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.concurrent.TimeUnit


val serviceModule = module {
    // Services
    single { provideProductService(get()) }

    single { provideOkHttpClient() }
    single { provideRetrofit(get(), BuildConfig.FOODREPO_BASE_URL) }
    single { provideNetworkHelper(androidContext()) }
}

private fun provideProductService(retrofit: Retrofit): RemoteProductService =
    retrofit.create(
        RemoteProductService::class.java
    )

private fun provideRetrofit(
    okHttpClient: OkHttpClient,
    BASE_URL: String
): Retrofit =
    Retrofit.Builder()
        .addConverterFactory(MoshiConverterFactory.create())
        .baseUrl(BASE_URL)
        .client(okHttpClient)
        .build()

private fun provideOkHttpClient() {
    val loggingInterceptor = HttpLoggingInterceptor()
    val foodRepoAuthenticationInterceptor = FoodRepoAuthenticationInterceptor()

    val timeoutInterceptor = TimeoutInterceptor()

    loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY)
    OkHttpClient.Builder()
        .connectTimeout(10000, TimeUnit.MILLISECONDS)
        .writeTimeout(15000, TimeUnit.MILLISECONDS)
        .readTimeout(5000, TimeUnit.MILLISECONDS)
        .addInterceptor(timeoutInterceptor)
        .addInterceptor(foodRepoAuthenticationInterceptor)
        .addInterceptor(loggingInterceptor)
        .build()
}

private fun provideNetworkHelper(context: Context) = NetworkHelper(context)