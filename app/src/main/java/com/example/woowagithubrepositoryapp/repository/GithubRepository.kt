package com.example.woowagithubrepositoryapp.repository

import com.example.woowagithubrepositoryapp.network.GithubService

class GithubRepository {
    suspend fun getUserData() = GithubService.instance.getUserData()
    
    suspend fun getNotifications() = GithubService.instance.getNotifications()

    suspend fun patchNotificationThread(
        threadId : String
    ) = GithubService.instance.patchNotificationThread(
        threadId = threadId
    )

    suspend fun getUserIssues(
        filter: String,
        state: String,
        page: Int
    ) = GithubService.instance.getIssues(
        filter = filter,
        state = state,
        page = page
    )

    suspend fun searchRepos(
        searchText: String
    ) = GithubService.instance.searchRepositories(
        searchText = searchText
    )

    companion object {
        val instance = GithubRepository()
    }
}