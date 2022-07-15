package com.example.woowagithubrepositoryapp.repository

import android.util.Log
import com.example.woowagithubrepositoryapp.model.Notification
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

    suspend fun getNotifications(page: Int) : MutableList<Notification> {
        return try{
            val response = service.getNotifications(page = page)
            if(response.isSuccessful){
                response.body()?.toMutableList() ?: mutableListOf()
            } else mutableListOf()
        } catch (e : Exception) {
            Log.d("getNotificationError",e.cause.toString())
            mutableListOf()
        }
    }

    suspend fun patchNotificationThread(
        threadId : String
    ) : Boolean {
        return try {
            val response = service.patchNotificationThread(
                threadId = threadId
            )
            response.isSuccessful
        } catch (e : Exception){
            false
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