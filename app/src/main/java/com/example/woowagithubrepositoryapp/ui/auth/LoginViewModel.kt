package com.example.woowagithubrepositoryapp.ui.auth

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.woowagithubrepositoryapp.repository.TokenRepository
import com.example.woowagithubrepositoryapp.ui.MainActivity
import com.example.woowagithubrepositoryapp.utils.clearTasksAndStartActivity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class LoginViewModel(private val repository: TokenRepository) : ViewModel() {

    val code = MutableLiveData("")
    var login : () -> Unit = { }

    fun getToken() = viewModelScope.launch(Dispatchers.IO) {
        try {
            if (repository.getAccessToken(code.value.toString())) {
                clearTasksAndStartActivity<MainActivity>()
                return@launch
            }
            else{
                // fail action
            }
        }catch (e : Exception){
            Log.e("LoginViewModel","getToken error")
        }
    }

    fun startLogin(){
        login()
    }
}