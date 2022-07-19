package com.example.woowagithubrepositoryapp.model

import com.google.gson.annotations.SerializedName

data class NotificationInfo(
    @SerializedName("number") val number : Int,
    @SerializedName("comments") val comments : Int,
    var notificationId : String = ""
)
