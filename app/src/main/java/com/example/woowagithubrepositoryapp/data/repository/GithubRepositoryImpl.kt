package com.example.woowagithubrepositoryapp.data.repository

import android.util.Log
import com.example.woowagithubrepositoryapp.data.datasource.GithubDataSource
import com.example.woowagithubrepositoryapp.data.datasource.GithubRemoteDataSourceImpl
import com.example.woowagithubrepositoryapp.data.network.GithubClient
import com.example.woowagithubrepositoryapp.data.network.GithubService
import com.example.woowagithubrepositoryapp.model.*
import kotlinx.coroutines.*
import java.io.IOException

class GithubRepositoryImpl : GithubRepository {
    private val service = GithubClient().generate(GithubService::class.java)
    private val dataSource = GithubRemoteDataSourceImpl()

    override suspend fun getUserData(): Result<User> {
        return try {
            val getUserDataResult = dataSource.getUserData()
            if(getUserDataResult.isSuccess){
                val userData = getUserDataResult.getOrThrow()
                val starredRepos = dataSource.getStarredRepos().getOrThrow()
                Result.success(userData.apply {
                    this.starredCnt = starredRepos
                })
            }
            else {
                getUserDataResult
            }
        }
        catch (e : Exception){
            Result.failure(e)
        }
    }

    override suspend fun getNotifications(page: Int)
    : Result<MutableList<Notification>> = supervisorScope {
        return@supervisorScope try {
            val getNotificationResult = dataSource.getNotifications(page)
            if (getNotificationResult.isSuccess) {
                val notifications = getNotificationResult.getOrThrow()
                val notificationInfo = notifications.map {
                    async {
                        dataSource.getNotificationInfo(it.subject.url).getOrThrow()
                    }
                }.awaitAll()
                Result.success(notifications.zip(notificationInfo) { noti, notiInfo ->
                    noti.apply {
                        this.comments = notiInfo?.comments.toString()
                        this.issueNum = "#${notiInfo?.number}"
                    }
                }.toMutableList())
            } else
                getNotificationResult
        }
        catch(e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun patchNotificationThread(
        threadIds: MutableList<String>
    ): Result<Boolean> {
        return dataSource.patchNotificationThread(threadIds)
    }

    override suspend fun getUserIssues(
        state: String,
        page: Int
    ): Result<List<Issue>> {
        return dataSource.getUserIssues(state, page)
    }

    override suspend fun searchRepos(
        searchText: String,
        page: Int
    ): Result<RepoResponse> {
        return dataSource.getRepos(searchText, page)
    }

    companion object {
        private var instance: GithubRepositoryImpl? = null
        fun getInstance(): GithubRepositoryImpl {
            if (instance == null) {
                instance = GithubRepositoryImpl()
            }
            return instance!!
        }
    }
}