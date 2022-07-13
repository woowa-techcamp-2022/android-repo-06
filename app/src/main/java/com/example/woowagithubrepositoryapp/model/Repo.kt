package com.example.woowagithubrepositoryapp.model

import com.google.gson.annotations.SerializedName

data class Repo(
    @SerializedName("id") val id : Int,
    @SerializedName("name") val name : String,
    @SerializedName("owner") val owner : RepoOwner,
    @SerializedName("description") val description : String,
    @SerializedName("stargazers_count") val starCount : Int,
    @SerializedName("language") val language : String,
    @SerializedName("full_name") val fullName : String
)
