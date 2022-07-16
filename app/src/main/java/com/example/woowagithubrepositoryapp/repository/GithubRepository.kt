package com.example.woowagithubrepositoryapp.repository

import com.example.woowagithubrepositoryapp.model.User
import com.example.woowagithubrepositoryapp.network.GithubClient
import com.example.woowagithubrepositoryapp.network.GithubService

class GithubRepository {

    private val service = GithubClient().generate(GithubService::class.java)

    suspend fun getUserData(): User? {
        val response = service.getUserData()
        val body = response.body()
        return if (response.isSuccessful && body != null) body else null
    }

    suspend fun getNotifications() = service.getNotifications()

    suspend fun patchNotificationThread(
        threadId: String
    ) = service.patchNotificationThread(
        threadId = threadId
    )

    suspend fun getUserIssues(
        state: String,
        page: Int
    ) = service.getIssues(
        state = state,
        page = page
    )

    suspend fun searchRepos(
        searchText: String,
        page: Int
    ) = service.searchRepositories(
        searchText = searchText,
        page = page
    )

    suspend fun getNotificationInfo(
        url: String
    ) = service.getNotificationInfo(url)

    suspend fun getStarredRepos(): Int {
        val response = service.getStarredRepos()
        val body = response.body()
        return if (response.isSuccessful && body != null) {
            body.size
        } else 0
    }

    companion object {
        private var instance: GithubRepository? = null
        fun getInstance(): GithubRepository {
            if (instance == null) {
                instance = GithubRepository()
            }
            return instance!!
        }
    }
}