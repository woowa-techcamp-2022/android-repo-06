package com.example.woowagithubrepositoryapp.utils

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.woowagithubrepositoryapp.repository.GithubRepository
import com.example.woowagithubrepositoryapp.repository.TokenRepository
import com.example.woowagithubrepositoryapp.ui.MainViewModel
import com.example.woowagithubrepositoryapp.ui.auth.LoginViewModel
import com.example.woowagithubrepositoryapp.ui.issue.IssueViewModel
import com.example.woowagithubrepositoryapp.ui.notification.NotificationViewModel
import com.example.woowagithubrepositoryapp.ui.profile.ProfileViewModel
import com.example.woowagithubrepositoryapp.ui.search.SearchViewModel

class ViewModelFactory : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return when{
            modelClass.isAssignableFrom(LoginViewModel::class.java) -> {
                LoginViewModel(TokenRepository.getInstance()) as T
            }
            modelClass.isAssignableFrom(MainViewModel::class.java) -> {
                MainViewModel(GithubRepository.getInstance()) as T
            }
            modelClass.isAssignableFrom(ProfileViewModel::class.java) ->{
                ProfileViewModel(GithubRepository.getInstance()) as T
            }
            modelClass.isAssignableFrom(SearchViewModel::class.java) ->{
                SearchViewModel(GithubRepository.getInstance()) as T
            }
            modelClass.isAssignableFrom(IssueViewModel::class.java) ->{
                IssueViewModel(GithubRepository.getInstance()) as T
            }
            modelClass.isAssignableFrom(NotificationViewModel::class.java) ->{
                NotificationViewModel(GithubRepository.getInstance()) as T
            }
            else -> throw IllegalAccessException("Unknown ViewModel Class")
        }
    }
}