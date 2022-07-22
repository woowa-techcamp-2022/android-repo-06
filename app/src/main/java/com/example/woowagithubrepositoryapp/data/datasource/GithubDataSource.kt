package com.example.woowagithubrepositoryapp.data.datasource

import com.example.woowagithubrepositoryapp.model.Issue
import com.example.woowagithubrepositoryapp.model.Notification
import com.example.woowagithubrepositoryapp.model.RepoResponse
import com.example.woowagithubrepositoryapp.model.User

interface GithubDataSource {
    suspend fun getUserData(): Result<User>

    suspend fun getNotifications(page: Int): Result<MutableList<Notification>>

    suspend fun patchNotificationThread(
        threadId: String
    ): Result<Boolean>

    suspend fun getUserIssues(
        state: String,
        page: Int
    ): Result<List<Issue>>

    suspend fun searchRepos(
        searchText: String,
        page: Int
    ): Result<RepoResponse>
}