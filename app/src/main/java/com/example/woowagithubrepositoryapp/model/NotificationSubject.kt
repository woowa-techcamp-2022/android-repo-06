package com.example.woowagithubrepositoryapp.model

import com.google.gson.annotations.SerializedName

data class NotificationSubject(
    @SerializedName("title") val title : String,
    @SerializedName("url") val url : String,
)
