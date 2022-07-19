package com.example.woowagithubrepositoryapp.model

import com.google.gson.annotations.SerializedName

data class User(
    @SerializedName("login") val login : String,
    @SerializedName("id") val id : Long,
    @SerializedName("avatar_url") val avatarUrl : String,
    @SerializedName("name") val name : String?,
    @SerializedName("blog") val blog : String?,
    @SerializedName("location") val location : String?,
    @SerializedName("email") val email : String?,
    @SerializedName("bio") val bio : String?,
    @SerializedName("public_repos") val publicRepos : Int,
    @SerializedName("followers") val followers : Int,
    @SerializedName("following") val following : Int
){
    var starredCnt : Int = 0
}
