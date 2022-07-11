package com.example.woowagithubrepositoryapp.model

import com.google.gson.annotations.SerializedName

data class User(
    @SerializedName("login") val login : String,
    @SerializedName("id") val id : Long,
    @SerializedName("node_id") val nodeId : String,
    @SerializedName("avatar_url") val avatarUrl : String,
    @SerializedName("gravatar_id") val gravatarId : String,
    @SerializedName("url") val url : String,
    @SerializedName("html_url") val htmlUrl : String,
    @SerializedName("followers_url") val followersUrl : String,
    @SerializedName("following_url") val followingUrl : String,
    @SerializedName("gists_url") val gistsUrl : String,
    @SerializedName("starred_url") val starredUrl : String,
    @SerializedName("subscriptions_url") val subscriptionsUrl : String,
    @SerializedName("organizations_url") val organizationsUrl : String,
    @SerializedName("repos_url") val reposUrl : String,
    @SerializedName("events_url") val eventsUrl : String,
    @SerializedName("received_events_url") val receivedEventsUrl : String,
    @SerializedName("type") val type : String,
    @SerializedName("site_admin") val siteAdmin : String,
    @SerializedName("name") val name : String?,
    @SerializedName("company") val company : String?,
    @SerializedName("blog") val blog : String,
    @SerializedName("location") val location : String?,
    @SerializedName("email") val email : String?,
    @SerializedName("hireable") val hireable : String?,
    @SerializedName("bio") val bio : String?,
    @SerializedName("twitter_username") val twitterUsername : String?,
    @SerializedName("public_repos") val publicRepos : Int,
    @SerializedName("public_gists") val publicGists : Int,
    @SerializedName("followers") val followers : Int,
    @SerializedName("following") val following : Int
)
