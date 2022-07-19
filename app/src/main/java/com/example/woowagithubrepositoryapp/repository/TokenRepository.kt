package com.example.woowagithubrepositoryapp.repository

import com.example.woowagithubrepositoryapp.network.GithubClient
import com.example.woowagithubrepositoryapp.network.TokenService

class TokenRepository {

    private val service = GithubClient().generateRefreshClient(TokenService::class.java)

    suspend fun getAccessToken(
        code: String
    ): Result<String> = try {
        val response = service.getAccessToken(code = code)
        val body = response.body()
        if (response.isSuccessful && body != null)
            Result.success(body.accessToken)
        else
            Result.failure(Exception("Get Access Token Error"))
    } catch (e: Exception) {
        Result.failure(e)
    }

    companion object {
        private var instance: TokenRepository? = null
        fun getInstance(): TokenRepository {
            if (instance == null) {
                instance = TokenRepository()
            }
            return instance!!
        }
    }
}