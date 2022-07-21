package com.example.woowagithubrepositoryapp.data.repository

import android.util.Log
import com.example.woowagithubrepositoryapp.data.network.GithubClient
import com.example.woowagithubrepositoryapp.data.network.GithubService
import com.example.woowagithubrepositoryapp.model.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.withContext

class GithubRepository {
    private val service = GithubClient().generate(GithubService::class.java)

    suspend fun getUserData(): Result<User> = withContext(Dispatchers.IO) {
        try {
            val response = service.getUserData()
            val body = response.body()
            if (response.isSuccessful && body != null) {
                val starredReposCnt = getStarredRepos()
                body.apply {
                    starredCnt = starredReposCnt
                }
                Result.success(body)
            } else Result.failure(Exception("Get User Data Error"))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getNotifications(page: Int): Result<MutableList<Notification>> =
        withContext(Dispatchers.IO) {
            try {
                val response = service.getNotifications(page = page)
                if (response.isSuccessful) {
                    val notifications = response.body()?.toMutableList() ?: mutableListOf()
                    val notificationInfos = notifications.map {
                        async {
                            getNotificationInfo(it.subject.url,it.id)
                        }
                    }.awaitAll()

                    Result.success(notifications.zip(notificationInfos){ notification, notificationInfo ->
                        notification.apply {
                            this.comments = notificationInfo?.comments.toString()
                            this.issueNum = "#${notificationInfo?.number}"
                        }
                    }.toMutableList())
                } else Result.failure(Exception("Get Notifications Error : response isn't successful"))
            } catch (e: Exception) {
                Log.d("getNotiError", e.cause.toString())
                Result.failure(e)
            }
        }

    suspend fun patchNotificationThread(
        threadId: String
    ): Result<Boolean> = withContext(Dispatchers.IO) {
        try {
            val response = service.patchNotificationThread(threadId)
            Result.success(response.isSuccessful)
        } catch (e: Exception) {
            Log.d("patchNotiThreadError", e.cause.toString())
            Result.failure(e)
        }
    }

    private suspend fun getNotificationInfo(fullUrl: String,id : String): NotificationInfo? {
        return try {
            service.getNotificationInfo(fullUrl).body().apply {
                this?.notificationId = id
            }
        } catch (e: Exception) {
            Log.d("getNotiInfoError", e.cause.toString())
            null
        }
    }

    suspend fun getUserIssues(
        state: String,
        page: Int
    ): Result<List<Issue>> = withContext(Dispatchers.IO) {
        try {
            val response = service.getIssues(
                state = state,
                page = page
            )
            val body = response.body()
            if (response.isSuccessful && body != null)
                Result.success(body)
            else
                Result.failure(Exception("Get User Issues Error"))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun searchRepos(
        searchText: String,
        page: Int
    ): Result<RepoResponse> = withContext(Dispatchers.IO) {
        try {
            val response = service.searchRepositories(
                searchText = searchText,
                page = page
            )
            val body = response.body()
            if (response.isSuccessful && body != null)
                Result.success(body)
            else Result.failure(Exception("Search Repos Error"))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    private suspend fun getStarredRepos(): Int {
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