package com.example.woowagithubrepositoryapp.model

import com.google.gson.annotations.SerializedName

data class AccessTokenResponse(
    @SerializedName("access_token") val accessToken : String
)
