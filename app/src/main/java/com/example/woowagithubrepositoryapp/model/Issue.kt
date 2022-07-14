package com.example.woowagithubrepositoryapp.model

import com.google.gson.annotations.SerializedName

data class Issue(
    @SerializedName("id") val id: Int,
    @SerializedName("repository") val repo : Repo,
    @SerializedName("state") val state : String,
    @SerializedName("title") val title : String,
    @SerializedName("updated_at") val updatedAt : String
)