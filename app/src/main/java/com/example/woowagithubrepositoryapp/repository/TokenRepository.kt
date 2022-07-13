package com.example.woowagithubrepositoryapp.repository

import com.example.woowagithubrepositoryapp.network.TokenService

class TokenRepository {
    suspend fun getAccessToken(
        code : String
    ) = TokenService.instance.getAccessToken(code = code)

    companion object{
        val instance = TokenRepository()
    }
}