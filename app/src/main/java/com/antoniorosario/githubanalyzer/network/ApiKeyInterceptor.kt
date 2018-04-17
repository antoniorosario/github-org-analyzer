package com.antoniorosario.githubanalyzer.network

import okhttp3.Interceptor
import okhttp3.Response

class ApiKeyInterceptor(private val apiKey: String) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val original = chain.request()
        val originalHttpUrl = original.url()

        val url = originalHttpUrl
                .newBuilder()
                .addQueryParameter("access_token", apiKey)
                .build()

        // Request customization: add request headers
        val request = original
                .newBuilder()
                .url(url).build()

        return chain.proceed(request)
    }
}

