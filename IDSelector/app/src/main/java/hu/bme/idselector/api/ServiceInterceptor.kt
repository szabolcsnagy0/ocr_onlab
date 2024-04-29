package hu.bme.idselector.api

import okhttp3.Interceptor
import okhttp3.Response

/**
 * Adds the saved token to the request header.
 */
class ServiceInterceptor(private val tokenManager: TokenManager?) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        var request = chain.request()

        /**
         * If the request header contains the "No-Authentication" key, then the token is not added.
         */
        if (request.header("No-Authentication") == null) {
            val token = tokenManager?.fetchAuthToken()

            if (!token.isNullOrEmpty()) {
                val finalToken = "Bearer $token"
                request = request.newBuilder()
                    .addHeader("Authorization", finalToken)
                    .build()
            }

        }
        return chain.proceed(request)
    }
}