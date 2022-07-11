package com.example.woowagithubrepositoryapp.repository
import com.example.woowagithubrepositoryapp.network.GithubService
import com.example.woowagithubrepositoryapp.network.TokenApi

class GithubRepository {
    suspend fun getAccessToken(
        code : String
    ) = TokenApi.instance.getAccessToken(code = code)
}