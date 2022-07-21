package com.example.woowagithubrepositoryapp.data.repository

import com.example.woowagithubrepositoryapp.data.network.GithubClient
import com.example.woowagithubrepositoryapp.data.network.TokenService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class TokenRepository {

    private val service = GithubClient().generateRefreshClient(TokenService::class.java)

    suspend fun getAccessToken(
        code: String
    ): Result<String> = withContext(Dispatchers.IO) {
        try {
            val response = service.getAccessToken(code = code)
            val body = response.body()
            if (response.isSuccessful && body != null)
                Result.success(body.accessToken)
            else
                Result.failure(Exception("Get Access Token Error"))
        } catch (e: Exception) {
            Result.failure(e)
        }
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