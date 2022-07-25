package com.example.woowagithubrepositoryapp.data.repository

import com.example.woowagithubrepositoryapp.model.Issue
import com.example.woowagithubrepositoryapp.model.Notification
import com.example.woowagithubrepositoryapp.model.RepoResponse
import com.example.woowagithubrepositoryapp.model.User

interface GithubRepository {
    suspend fun getUserData(): Result<User>

    suspend fun getNotifications(page: Int): Result<MutableList<Notification>>

    suspend fun patchNotificationThread(
        threadIds: MutableList<String>
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