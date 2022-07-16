package com.example.woowagithubrepositoryapp.ui.profile

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.woowagithubrepositoryapp.App
import com.example.woowagithubrepositoryapp.model.User
import com.example.woowagithubrepositoryapp.repository.GithubRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ProfileViewModel(private val repository: GithubRepository) : ViewModel() {

    val userData = MutableLiveData<User>()
    val starredRepos = MutableLiveData(0)

    init {
        App.user?.let {
            userData.value = it
            getStarredRepository()
        }
    }

    private fun getStarredRepository() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                starredRepos.postValue(repository.getStarredRepos())
            } catch (e: Exception) {

            }
        }
    }
}