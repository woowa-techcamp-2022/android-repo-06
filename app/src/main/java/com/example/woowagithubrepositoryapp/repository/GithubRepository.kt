package com.example.woowagithubrepositoryapp.repository

import com.example.woowagithubrepositoryapp.network.GithubService

class GithubRepository {
    suspend fun getUserData() = GithubService.instance.getUserData()
    
    suspend fun getNotifications(page: Int)
    = GithubService.instance.getNotifications(page = page)

    suspend fun patchNotificationThread(
        threadId : String
    ) = GithubService.instance.patchNotificationThread(
        threadId = threadId
    )

    suspend fun getUserIssues(
        state: String,
        page: Int
    ) = GithubService.instance.getIssues(
        state = state,
        page = page
    )

    suspend fun searchRepos(
        searchText: String,
        page : Int
    ) = GithubService.instance.searchRepositories(
        searchText = searchText,
        page = page
    )

    companion object {
        val instance = GithubRepository()
    }
}