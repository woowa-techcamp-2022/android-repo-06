package com.example.woowagithubrepositoryapp.utils

import com.example.woowagithubrepositoryapp.BuildConfig

object Constants {
    const val GITHUB_CLIENT_ID = BuildConfig.githubClientId
    const val GITHUB_CLIENT_SECRET = BuildConfig.githubClientSecret

    enum class DataLoading {
        BEFORE, NOW, AFTER
    }

    enum class Tab(val text : String) {
        ISSUE("Issue"), NOTI("Notifications")
    }
}
