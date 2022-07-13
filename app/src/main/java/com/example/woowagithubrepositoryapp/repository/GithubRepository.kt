package com.example.woowagithubrepositoryapp.repository

import com.example.woowagithubrepositoryapp.network.GithubService

class GithubRepository {
    suspend fun getUserData() = GithubService.instance.getUserData()

    suspend fun getNotifications() = GithubService.instance.getNotifications()
}