package com.example.woowagithubrepositoryapp.data.datasource

import android.util.Log
import com.example.woowagithubrepositoryapp.data.network.GithubClient
import com.example.woowagithubrepositoryapp.data.network.GithubService
import com.example.woowagithubrepositoryapp.model.*
import kotlinx.coroutines.*
import java.io.IOException

class GithubRemoteDataSourceImpl : GithubDataSource {
    private val service = GithubClient().generate(GithubService::class.java)

    override suspend fun getUserData()
    : Result<User> = withContext(Dispatchers.IO) {
        try {
            val response = service.getUserData()
            val body = response.body()
            if (response.isSuccessful && body != null) {
                Result.success(body)
            } else Result.failure(Exception("Get User Data Error"))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getNotifications(page: Int)
    : Result<MutableList<Notification>> = withContext(Dispatchers.IO) {
        try {
            val response = service.getNotifications(page = page)
            val body = response.body()
            return@withContext if (response.isSuccessful && body != null) {
                Result.success(body.toMutableList())
            } else Result.failure(Exception("GetNotificationsError"))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }


    override suspend fun patchNotificationThread(threadId: String)
    : Result<Boolean> = withContext(Dispatchers.IO) {
        try {
            val response = service.patchNotificationThread(threadId)
            return@withContext if(response.isSuccessful) {
                Result.success(true)
            } else Result.failure(Exception("patchNotiThreadError"))
        }
        catch (e: IOException) {
            Log.d("patchNotiThreadError", e.cause.toString())
            Result.failure(e)
        }
    }

    override suspend fun getStarredRepos()
    : Result<Int> = withContext(Dispatchers.IO){
        try {
            val response = service.getStarredRepos()
            val body = response.body()
            return@withContext if (response.isSuccessful && body != null) {
                Result.success(body.size)
            } else Result.failure(Exception("getStarredReposError"))
        }
        catch (e : Exception){
            return@withContext Result.failure(e)
        }
    }

    override suspend fun getUserIssues(state: String, page: Int)
    : Result<List<Issue>> = withContext(Dispatchers.IO) {
        try {
            val response = service.getIssues(
                state = state,
                page = page
            )
            val body = response.body()
            if (response.isSuccessful && body != null)
                Result.success(body)
            else
                Result.failure(Exception("getUserIssuesError"))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getRepos(searchText: String, page: Int)
    : Result<RepoResponse>  = withContext(Dispatchers.IO) {
        try {
            val response = service.searchRepositories(
                searchText = searchText,
                page = page
            )
            val body = response.body()
            if (response.isSuccessful && body != null)
                Result.success(body)
            else Result.failure(Exception("getReposError"))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getNotificationInfo(fullUrl: String)
    : Result<NotificationInfo?> = withContext(Dispatchers.IO) {
        try {
            val response = service.getNotificationInfo(fullUrl)
            val body = response.body()
            if (response.isSuccessful && body != null)
                Result.success(body)
            else Result.failure(Exception("getNotificationInfosError"))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}