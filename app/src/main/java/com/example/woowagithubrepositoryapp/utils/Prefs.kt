package com.example.woowagithubrepositoryapp.utils

import androidx.preference.PreferenceManager
import com.example.woowagithubrepositoryapp.App

object Prefs {
    private const val ACCESS_TOKEN = "access_token"
    private const val CODE = "code"

    private val prefs by lazy {
        PreferenceManager.getDefaultSharedPreferences(App.instance)
    }

    var accessToken
        get() = prefs.getString(ACCESS_TOKEN, null).toString()
        set(value) = prefs.edit().putString(ACCESS_TOKEN, value).apply()

    var code
        get() = prefs.getString(CODE, null).toString()
        set(value) = prefs.edit().putString(CODE, value).apply()
}