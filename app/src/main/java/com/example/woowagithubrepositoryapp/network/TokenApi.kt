package com.example.woowagithubrepositoryapp.network

import com.example.woowagithubrepositoryapp.App
import com.example.woowagithubrepositoryapp.R
import com.example.woowagithubrepositoryapp.model.AccessTokenResponse
import retrofit2.Response
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.Headers
import retrofit2.http.POST

interface TokenApi {

    @FormUrlEncoded
    @POST("/login/oauth/access_token")
    @Headers("Accept: application/json")
    suspend fun getAccessToken(
        @Field("client_id") clientId: String = App.instance.getString(R.string.GITHUB_CLIENT_ID),
        @Field("client_secret") clientSecret: String = App.instance.getString(R.string.GITHUB_CLIENT_SECRET),
        @Field("code") code: String
    ) : Response<AccessTokenResponse>

    companion object{
        val instance = GithubClient().generateRefreshClient(TokenApi::class.java)
    }
}