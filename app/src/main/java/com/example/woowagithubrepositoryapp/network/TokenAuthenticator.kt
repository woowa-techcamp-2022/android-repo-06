package com.example.woowagithubrepositoryapp.network

import com.example.woowagithubrepositoryapp.ui.auth.LoginActivity
import com.example.woowagithubrepositoryapp.utils.clearTasksAndStartActivity
import okhttp3.Authenticator
import okhttp3.Request
import okhttp3.Response
import okhttp3.Route


class TokenAuthenticator : Authenticator {
    override fun authenticate(route: Route?, response: Response): Request? {
        if (response.code == 401) {
            clearTasksAndStartActivity<LoginActivity>()
        }
        return null
    }
}