package com.example.woowagithubrepositoryapp.ui.auth

import android.content.Intent
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.woowagithubrepositoryapp.App
import com.example.woowagithubrepositoryapp.repository.GithubRepository
import com.example.woowagithubrepositoryapp.ui.MainActivity
import com.example.woowagithubrepositoryapp.utils.Prefs
import com.example.woowagithubrepositoryapp.utils.clearTasksAndStartActivity
import kotlinx.coroutines.launch

class LoginViewModel : ViewModel() {
    val code = MutableLiveData("")
    val repository = GithubRepository()

    fun getToken() = viewModelScope.launch {
        try {
            val response = repository.getAccessToken(code.value.toString())
            val body = response.body()
            if (body != null){
                Prefs.accessToken = body.accessToken
                Prefs.code = code.value.toString()

                clearTasksAndStartActivity<MainActivity>()
            }
        }catch (e : Exception){
            Log.e("LoginViewModel","getToken error")
        }
    }
}