package com.example.woowagithubrepositoryapp.data.network

import com.example.woowagithubrepositoryapp.model.AccessTokenResponse
import com.example.woowagithubrepositoryapp.utils.Constants
import retrofit2.Response
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.Headers
import retrofit2.http.POST

interface TokenService {

    @FormUrlEncoded
    @POST("/login/oauth/access_token")
    @Headers("Accept: application/json")
    suspend fun getAccessToken(
        @Field("client_id") clientId: String = Constants.GITHUB_CLIENT_ID,
        @Field("client_secret") clientSecret: String = Constants.GITHUB_CLIENT_SECRET,
        @Field("code") code: String
    ) : Response<AccessTokenResponse>
}