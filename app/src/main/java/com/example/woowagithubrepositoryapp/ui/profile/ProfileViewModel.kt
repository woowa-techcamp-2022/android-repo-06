package com.example.woowagithubrepositoryapp.ui.profile

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.woowagithubrepositoryapp.App
import com.example.woowagithubrepositoryapp.model.User
import com.example.woowagithubrepositoryapp.repository.GithubRepository
import com.example.woowagithubrepositoryapp.utils.toastMsg
import kotlinx.coroutines.launch

class ProfileViewModel(private val repository: GithubRepository) : ViewModel() {

    val userData = MutableLiveData<User>()

    init {
        App.user?.let {
            userData.value = it
        }
        getUserData()
    }

    private fun getUserData() {
        viewModelScope.launch {
            val result = repository.getUserData()
            when {
                result.isSuccess -> {
                    result.getOrNull()?.let {
                        if (it != App.user) {
                            App.user = it
                            userData.value = it
                        }
                    }
                }
                result.isFailure -> {
                    toastMsg("사용자의 정보를 불러오지 못하였습니다.")
                }
            }
        }
    }
}