package com.example.woowagithubrepositoryapp

import android.app.Application

class App : Application() {
    override fun onCreate() {
        super.onCreate()
        instance = this
    }
    companion object{
        lateinit var instance : App
        const val AUTH_HOST ="https://github.com"
        const val HOST = "https://api.github.com"
    }
}