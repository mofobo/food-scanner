package ch.mofobo.foodscanner.utils

import ch.mofobo.foodscanner.BuildConfig
import okhttp3.Interceptor
import okhttp3.Response

class FoodRepoAuthenticationInterceptor : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val original = chain.request()
        val token = BuildConfig.FOODREPO_API_KEY
        return when {
            token.isNullOrBlank() -> chain.proceed(original)
            else -> {
                val requestBuilder = original.newBuilder()
                requestBuilder.header("Authorization", "Bearer " + token)
                chain.proceed(requestBuilder.build())
            }
        }
    }
}