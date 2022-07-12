package com.example.woowagithubrepositoryapp.repository

import com.example.woowagithubrepositoryapp.network.TokenApi

class TokenRepository {
    suspend fun getAccessToken(
        code : String
    ) = TokenApi.instance.getAccessToken(code = code)
}