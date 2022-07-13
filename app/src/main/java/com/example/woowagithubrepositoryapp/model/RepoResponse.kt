package com.example.woowagithubrepositoryapp.model

import com.google.gson.annotations.SerializedName

data class RepoResponse(
    @SerializedName("total_count") val totalCount : Int,
    @SerializedName("incomplete_results") val results : Boolean,
    @SerializedName("items") val items : List<Repo>
)
