package com.example.woowagithubrepositoryapp.repository

import android.util.Log
import com.example.woowagithubrepositoryapp.model.Notification
import com.example.woowagithubrepositoryapp.network.GithubService

class GithubRepository {
    suspend fun getUserData() = GithubService.instance.getUserData()
    
    suspend fun getNotifications() : MutableList<Notification> {
        val response = GithubService.instance.getNotifications()
        try {
            if (response.isSuccessful) {
                return response.body()?.toMutableList() ?: mutableListOf()
            }else {
                return mutableListOf()
            }
        }catch (e : Exception){
            Log.d("getNotificationError",e.cause.toString())
            return mutableListOf()
        }
    }

    suspend fun patchNotificationThread(
        threadId : String
    ) : Boolean {
        try {
            val response = GithubService.instance.patchNotificationThread(threadId)
            if (response.isSuccessful) {
                return true
            }
        }catch (e : Exception) {
            Log.d("getNotificationError",e.cause.toString())
        }
        return false
    }

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