package com.example.woowagithubrepositoryapp.ui.auth

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.woowagithubrepositoryapp.repository.TokenRepository
import com.example.woowagithubrepositoryapp.ui.MainActivity
import com.example.woowagithubrepositoryapp.utils.Prefs
import com.example.woowagithubrepositoryapp.utils.clearTasksAndStartActivity
import com.example.woowagithubrepositoryapp.utils.toastMsg
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class LoginViewModel(private val repository: TokenRepository) : ViewModel() {

    val code = MutableLiveData("")
    var login: () -> Unit = { }

    fun getToken() = viewModelScope.launch(Dispatchers.IO) {
        val result = repository.getAccessToken(code.value.toString())
        withContext(Dispatchers.Main) {
            when {
                result.isSuccess -> {
                    Prefs.accessToken = result.getOrThrow()
                    clearTasksAndStartActivity<MainActivity>()
                }
                result.isFailure -> {
                    toastMsg("로그인 실패")
                    clearTasksAndStartActivity<LoginActivity>()
                }
            }
        }
    }

    fun startLogin() {
        login()
    }
}