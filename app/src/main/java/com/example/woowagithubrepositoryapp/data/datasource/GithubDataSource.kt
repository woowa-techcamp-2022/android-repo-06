package com.example.woowagithubrepositoryapp.data.datasource

import com.example.woowagithubrepositoryapp.model.*

interface GithubDataSource {
    suspend fun getUserData(): Result<User>

    suspend fun getNotifications(page: Int): Result<MutableList<Notification>>

    suspend fun patchNotificationThread(threadIds: MutableList<String>): Result<Boolean>

    suspend fun getStarredRepos(): Result<Int>

    suspend fun getUserIssues(
        state: String,
        page: Int
    ): Result<List<Issue>>

    suspend fun getRepos(
        searchText: String,
        page: Int
    ): Result<RepoResponse>

    suspend fun getNotificationInfo(
        fullUrl: String
    ) : Result<NotificationInfo?>


}