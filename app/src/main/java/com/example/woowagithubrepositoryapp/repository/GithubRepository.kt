package com.example.woowagithubrepositoryapp.repository

import android.util.Log
import com.example.woowagithubrepositoryapp.model.*
import com.example.woowagithubrepositoryapp.network.GithubClient
import com.example.woowagithubrepositoryapp.network.GithubService


class GithubRepository {
    private val service = GithubClient().generate(GithubService::class.java)

    suspend fun getUserData(): Result<User> = try {
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

    suspend fun getNotifications(page: Int): Result<MutableList<Notification>> {
        return try {
            val response = service.getNotifications(page = page)
            if (response.isSuccessful) {
                val notifications = response.body()?.toMutableList() ?: mutableListOf()
                notifications.forEach {
                    val info = getNotificationInfo(it.subject.url)
                    it.comments = info?.comments.toString()
                    it.issueNum = "#${info?.number.toString()}"
                }
                Result.success(notifications)
            } else Result.failure(Exception("Get Notifications Error : response isn't successful"))
        } catch (e: Exception) {
            Log.d("getNotiError", e.cause.toString())
            Result.failure(e)
        }
    }

    suspend fun patchNotificationThread(
        threadId: String
    ): Result<Boolean> {
        return try {
            val response = service.patchNotificationThread(threadId)
            Result.success(response.isSuccessful)
        } catch (e: Exception) {
            Log.d("patchNotiThreadError", e.cause.toString())
            Result.failure(e)
        }
    }

    private suspend fun getNotificationInfo(fullUrl: String): NotificationInfo? {
        return try {
            service.getNotificationInfo(fullUrl).body()
        } catch (e: Exception) {
            Log.d("getNotiInfoError", e.cause.toString())
            null
        }
    }

    suspend fun getUserIssues(
        state: String,
        page: Int
    ): Result<List<Issue>> = try {
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

    suspend fun searchRepos(
        searchText: String,
        page: Int
    ): Result<RepoResponse> = try {
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