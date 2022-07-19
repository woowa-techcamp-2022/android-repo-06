package com.example.woowagithubrepositoryapp.model

import com.google.gson.annotations.SerializedName

data class AccessTokenResponse(
    @SerializedName("access_token") val accessToken : String,
    @SerializedName("scope") val scope : String,
    @SerializedName("token_type") val tokenType : String
)
