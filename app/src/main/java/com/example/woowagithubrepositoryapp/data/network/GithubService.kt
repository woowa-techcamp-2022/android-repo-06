package com.example.woowagithubrepositoryapp.data.network

import com.example.woowagithubrepositoryapp.model.*
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.Path
import retrofit2.http.Query

interface GithubService {

    @GET("/user")
    suspend fun getUserData(): Response<User>

    @GET("/notifications")
    suspend fun getNotifications(
        @Query("per_page") perPage: Int = 10, // default = 30
        @Query("page") page: Int
    ): Response<List<Notification>>

    @GET("/issues")
    suspend fun getIssues(
        @Query("filter") filter: String = "all",
        @Query("state") state: String,
        @Query("per_page") perPage: Int = 10, //default = 30
        @Query("page") page: Int,
        @Query("sort") sort: String = "updated" //default = "created"
    ): Response<List<Issue>>


    @GET("{notificationInfoUrl}")
    suspend fun getNotificationInfo(
        @Path("notificationInfoUrl", encoded = true) notificationInfoUrl : String
    ) : Response<NotificationInfo>

    @GET("/search/repositories")
    suspend fun searchRepositories(
        @Query("q") searchText: String,
        @Query("sort") sort: String = "start",
        @Query("order") order: String = "desc",
        @Query("per_page") perPage: Int = 10,
        @Query("page") page: Int
    ): Response<RepoResponse>

    @PATCH("/notifications/threads/{thread_id}")
    suspend fun patchNotificationThread(
        @Path("thread_id") threadId: String
    ): Response<SuccessResponse>

    @GET("/user/starred")
    suspend fun getStarredRepos(): Response<List<Starred>>
}