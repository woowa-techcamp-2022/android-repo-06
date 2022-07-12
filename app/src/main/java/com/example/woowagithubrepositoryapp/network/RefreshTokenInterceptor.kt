package com.example.woowagithubrepositoryapp.network

import android.content.Intent
import com.example.woowagithubrepositoryapp.App
import com.example.woowagithubrepositoryapp.ui.auth.LoginActivity
import com.example.woowagithubrepositoryapp.utils.Prefs
import okhttp3.Interceptor
import okhttp3.Response

class RefreshTokenInterceptor : Interceptor{
    override fun intercept(chain: Interceptor.Chain): Response {
        val original = chain.request()
        val request = original.newBuilder().apply {
            header("Authorization", "token ${Prefs.accessToken}")
            method(original.method,original.body)
        }.build()

        val response = chain.proceed(request)

        if (response.code == 401){
            Prefs.accessToken = ""
            val intent = Intent(App.instance,LoginActivity::class.java).apply {
                addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            }
            App.instance.startActivity(intent)
        }

        return response
    }
}