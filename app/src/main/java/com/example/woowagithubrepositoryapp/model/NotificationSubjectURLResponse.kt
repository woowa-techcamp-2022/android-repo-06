package com.example.woowagithubrepositoryapp.model

import com.google.gson.annotations.SerializedName

data class NotificationSubjectURLResponse(
    @SerializedName("number") val number : Int,
    @SerializedName("comments") val comments : Int,
)
