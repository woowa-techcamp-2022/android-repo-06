package com.example.woowagithubrepositoryapp.repository

import com.example.woowagithubrepositoryapp.network.GithubClient
import com.example.woowagithubrepositoryapp.network.TokenService
import com.example.woowagithubrepositoryapp.utils.Prefs

class TokenRepository {

    private val service = GithubClient().generateRefreshClient(TokenService::class.java)

    suspend fun getAccessToken(
        code : String
    ) : Boolean {
        val response = service.getAccessToken(code = code)
        val body = response.body()
        if (response.isSuccessful && body != null){
            Prefs.accessToken = body.accessToken
            return true
        }
        return false
    }

    companion object{
        private var instance : TokenRepository? = null
        fun getInstance() : TokenRepository{
            if (instance == null){
                instance = TokenRepository()
            }
            return instance!!
        }
    }
}