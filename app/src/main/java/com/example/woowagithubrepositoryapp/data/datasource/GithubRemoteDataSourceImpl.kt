package com.example.woowagithubrepositoryapp.data.datasource

import com.example.woowagithubrepositoryapp.data.network.GithubClient
import com.example.woowagithubrepositoryapp.data.network.GithubService
import com.example.woowagithubrepositoryapp.model.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.IOException

class GithubRemoteDataSourceImpl : GithubDataSource {
    private val service = GithubClient().generate(GithubService::class.java)

    override suspend fun getUserData(): Result<User> {
        TODO("Not yet implemented")
    }

    override suspend fun getNotifications(page: Int): Result<MutableList<Notification>> {
        TODO("Not yet implemented")
    }

    override suspend fun patchNotificationThread(threadId: String): Result<Boolean> {
        TODO("Not yet implemented")
    }

    override suspend fun getStarredRepos(): Result<Int>  {
        TODO("Not yet implemented")
    }

    override suspend fun getUserIssues(state: String, page: Int): Result<List<Issue>> {
        TODO("Not yet implemented")
    }

    override suspend fun getRepos(searchText: String, page: Int): Result<RepoResponse> {
        TODO("Not yet implemented")
    }

    override suspend fun getNotificationInfo(fullUrl: String, id: String): Result<NotificationInfo?> {
        TODO("Not yet implemented")
    }
}