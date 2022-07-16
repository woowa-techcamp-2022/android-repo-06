package com.example.woowagithubrepositoryapp.ui

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.woowagithubrepositoryapp.App
import com.example.woowagithubrepositoryapp.repository.GithubRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainViewModel(private val repository: GithubRepository) : ViewModel() {
    fun getUserData(complete : () -> Unit) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val userData = repository.getUserData()
                if (userData != null){
                    withContext(Dispatchers.Main){
                        App.user = userData
                        complete()
                    }
                }else{
                
                }
            } catch (e: Exception) {
                Log.e("MainViewModel", "getUserData error")
            }
        }
    }
}

