package com.example.woowagithubrepositoryapp

import android.app.Application
import com.example.woowagithubrepositoryapp.model.User

class App : Application() {
    override fun onCreate() {
        super.onCreate()
        instance = this
    }
    companion object{
        var user : User? = null
        lateinit var instance : App
        const val AUTH_HOST ="https://github.com"
        const val HOST = "https://api.github.com"
    }
}