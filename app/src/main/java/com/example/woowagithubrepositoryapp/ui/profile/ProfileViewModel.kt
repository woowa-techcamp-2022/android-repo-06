package com.example.woowagithubrepositoryapp.ui.profile

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.woowagithubrepositoryapp.App
import com.example.woowagithubrepositoryapp.model.User
import com.example.woowagithubrepositoryapp.repository.GithubRepository

class ProfileViewModel(private val repository: GithubRepository) : ViewModel() {

    val userData = MutableLiveData<User>()

    init {
        App.user?.let {
            userData.value = it
        }
    }
}