package com.example.woowagithubrepositoryapp.ui.common

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.woowagithubrepositoryapp.data.repository.GithubRepositoryImpl
import com.example.woowagithubrepositoryapp.data.repository.TokenRepositoryImpl
import com.example.woowagithubrepositoryapp.ui.MainViewModel
import com.example.woowagithubrepositoryapp.ui.auth.LoginViewModel
import com.example.woowagithubrepositoryapp.ui.profile.ProfileViewModel
import com.example.woowagithubrepositoryapp.ui.search.SearchViewModel

class ViewModelFactory : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return when{
            modelClass.isAssignableFrom(LoginViewModel::class.java) -> {
                LoginViewModel(TokenRepositoryImpl.getInstance()) as T
            }
            modelClass.isAssignableFrom(MainViewModel::class.java) -> {
                MainViewModel(GithubRepositoryImpl.getInstance()) as T
            }
            modelClass.isAssignableFrom(ProfileViewModel::class.java) ->{
                ProfileViewModel(GithubRepositoryImpl.getInstance()) as T
            }
            modelClass.isAssignableFrom(SearchViewModel::class.java) -> {
                SearchViewModel(GithubRepositoryImpl.getInstance()) as T
            }
            else -> throw IllegalAccessException("Unknown ViewModel Class")
        }
    }
}