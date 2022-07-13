package com.example.woowagithubrepositoryapp.network

import com.example.woowagithubrepositoryapp.model.Notification
import com.example.woowagithubrepositoryapp.model.User
import retrofit2.Response
import retrofit2.http.*

interface GithubService {

    @GET("/user")
    suspend fun getUserData() : Response<User>

    @GET("/notifications")
    suspend fun getNotifications() : Response<List<Notification>>

    companion object{
        val instance = GithubClient().generate(GithubService::class.java)
    }
}