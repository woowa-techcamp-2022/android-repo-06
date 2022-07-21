package com.example.woowagithubrepositoryapp.data.repository

interface TokenRepository {
    suspend fun getAccessToken(
        code: String
    ): Result<String>
}