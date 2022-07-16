package com.example.woowagithubrepositoryapp.repository

import android.util.Log
import com.example.woowagithubrepositoryapp.model.Notification
import com.example.woowagithubrepositoryapp.model.User
import com.example.woowagithubrepositoryapp.network.GithubClient
import com.example.woowagithubrepositoryapp.model.NotificationInfo
import com.example.woowagithubrepositoryapp.network.GithubService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class GithubRepository {
    private val service = GithubClient().generate(GithubService::class.java)

    suspend fun getUserData(): User? {
        val response = service.getUserData()
        val body = response.body()
        return if (response.isSuccessful && body != null) {
            val starredReposCnt = getStarredRepos()
            body.apply {
                starredCnt = starredReposCnt
            }
        }else null
    }

    suspend fun getNotifications(page: Int) : MutableList<Notification> {
        return try{
            val response = service.getNotifications(page = page)
            if(response.isSuccessful){
                val notifications = response.body()?.toMutableList() ?: mutableListOf()
                notifications.forEach {
                    val info = getNotificationInfo(it.subject.url)
                    it.comments = info?.comments.toString()
                    it.issueNum = "#${info?.number.toString()}"
                }
                notifications
            } else mutableListOf()
        } catch (e : Exception) {
            Log.d("getNotiError",e.cause.toString())
            mutableListOf()
        }
    }

    suspend fun patchNotificationThread(
        threadId : String
    ) : Boolean {
        return try {
            val response = service.patchNotificationThread(threadId)
            response.isSuccessful
        } catch (e : Exception){
            Log.d("patchNotiThreadError",e.cause.toString())
            false
        }
    }

    private suspend fun getNotificationInfo(fullUrl : String) : NotificationInfo? {
        return try {
            service.getNotificationInfo(fullUrl).body()
        } catch (e : Exception){
            Log.d("getNotiInfoError",e.cause.toString())
            null
        }
    }

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