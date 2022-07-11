package com.example.woowagithubrepositoryapp.network

import com.example.woowagithubrepositoryapp.utils.Prefs
import okhttp3.Interceptor
import okhttp3.Response

class TokenInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val original = chain.request()

        val request = original.newBuilder().apply {
            header("Authorization", "token ${Prefs.accessToken}")
            method(original.method, original.body)
        }.build()

        return chain.proceed(request)
    }

}