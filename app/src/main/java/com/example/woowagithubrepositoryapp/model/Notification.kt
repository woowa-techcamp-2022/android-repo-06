package com.example.woowagithubrepositoryapp.model

import com.google.gson.annotations.SerializedName

data class Notification(
    @SerializedName("id") val id : String,
    @SerializedName("repository") val repository : Repo,
    @SerializedName("updated_at") val updatedAt : String,
    @SerializedName("unread") val unread : Boolean,
    @SerializedName("subject") val subject: NotificationSubject,


)
