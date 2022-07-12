package com.example.woowagithubrepositoryapp.ui.profile

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.woowagithubrepositoryapp.App
import com.example.woowagithubrepositoryapp.model.User

class ProfileViewModel : ViewModel() {

    val userData = MutableLiveData<User>()

    //todo. tag 정보 찾기, starred 정보 찾기

    init {
        App.user?.let {
            userData.value = it
        }
    }
}