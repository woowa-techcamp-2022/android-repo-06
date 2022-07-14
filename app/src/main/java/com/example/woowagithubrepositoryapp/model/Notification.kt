package com.example.woowagithubrepositoryapp.model

import com.google.gson.annotations.SerializedName

data class Notification(
    @SerializedName("id") val id : String,
    @SerializedName("repository") val repository : Repo,
    @SerializedName("updated_at") val updatedAt : String,
    @SerializedName("unread") val unread : Boolean,
    @SerializedName("subject") val subject: NotificationSubject,
    @SerializedName("last_read_at") val lastReadAt : String,
    @SerializedName("url") val url : String,
    var comments : String? = null,
    var issueNum : String? = null    
){
    val threadId : String
        get() = url.split("/").last()
}
