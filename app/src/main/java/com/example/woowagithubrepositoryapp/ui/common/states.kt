package com.example.woowagithubrepositoryapp.ui.common

enum class DataLoading {
    BEFORE, NOW, AFTER
}

enum class Tab(val text : String) {
    ISSUE("Issue"), NOTI("Notifications")
}

enum class IssueLoadType{
    CREATE, LOAD, PAGING
}

enum class SearchType {
    CREATE, SEARCH
}