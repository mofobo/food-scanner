package ch.mofobo.foodscanner.di

import android.content.Context
import android.text.TextUtils
import ch.mofobo.foodscanner.BuildConfig
import ch.mofobo.foodscanner.data.remote.RemoteProductService
import ch.mofobo.foodscanner.utils.FoodRepoAuthenticationInterceptor
import ch.mofobo.foodscanner.utils.NetworkHelper
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
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

private fun provideOkHttpClient() = if (BuildConfig.DEBUG) {
    val loggingInterceptor = HttpLoggingInterceptor()
    val foodRepoAuthenticationInterceptor = FoodRepoAuthenticationInterceptor()

    val timeoutInterceptor: Interceptor = object : Interceptor {
        override fun intercept(chain: Interceptor.Chain): Response {
            val request: Request = chain.request()
            var connectTimeout: Int = chain.connectTimeoutMillis()
            var readTimeout: Int = chain.readTimeoutMillis()
            var writeTimeout: Int = chain.writeTimeoutMillis()
            val connectNew: String? = request.header(CONNECT_TIMEOUT)
            val readNew: String?= request.header(READ_TIMEOUT)
            val writeNew: String?= request.header(WRITE_TIMEOUT)
            if (!TextUtils.isEmpty(connectNew)) {
                connectTimeout = Integer.valueOf(connectNew)
            }
            if (!TextUtils.isEmpty(readNew)) {
                readTimeout = Integer.valueOf(readNew)
            }
            if (!TextUtils.isEmpty(writeNew)) {
                writeTimeout = Integer.valueOf(writeNew)
            }
            return chain
                .withConnectTimeout(connectTimeout, TimeUnit.MILLISECONDS)
                .withReadTimeout(readTimeout, TimeUnit.MILLISECONDS)
                .withWriteTimeout(writeTimeout, TimeUnit.MILLISECONDS)
                .proceed(request)
        }
    }



    loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY)
    OkHttpClient.Builder()
        .connectTimeout(10000, TimeUnit.MILLISECONDS)
        .writeTimeout(15000, TimeUnit.MILLISECONDS)
        .readTimeout(5000, TimeUnit.MILLISECONDS)
        .addInterceptor(timeoutInterceptor)
        .addInterceptor(foodRepoAuthenticationInterceptor)
        .addInterceptor(loggingInterceptor)
        .build()
} else OkHttpClient
    .Builder()
    .build()

private fun provideNetworkHelper(context: Context) = NetworkHelper(context)


const val CONNECT_TIMEOUT = "CONNECT_TIMEOUT"
const val READ_TIMEOUT = "READ_TIMEOUT"
const val WRITE_TIMEOUT = "WRITE_TIMEOUT"