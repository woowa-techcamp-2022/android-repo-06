package com.example.woowagithubrepositoryapp.ui

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.woowagithubrepositoryapp.App
import com.example.woowagithubrepositoryapp.repository.GithubRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class MainViewModel : ViewModel() {
    fun getUserData(complete : () -> Unit) {
        viewModelScope.launch {
            try {
                val response = GithubRepository.instance.getUserData()
                val body = response.body()
                if (response.isSuccessful && body != null) {
                    withContext(Dispatchers.Main){
                        App.user = body
                        complete()
                    }
                } else {
                    //error action
                }
            } catch (e: Exception) {
                Log.e("MainViewModel", "getUserData error")
            }
        }
    }
}