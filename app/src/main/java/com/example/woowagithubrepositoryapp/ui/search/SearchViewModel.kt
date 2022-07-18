package com.example.woowagithubrepositoryapp.ui.search

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.woowagithubrepositoryapp.model.Repo
import com.example.woowagithubrepositoryapp.repository.GithubRepository
import kotlinx.coroutines.*

class SearchViewModel(private val repository: GithubRepository) : ViewModel() {

    val searchText = MutableLiveData("")
    val isRecyclerViewOn = MutableLiveData(false)
    val isSearchBarActive = MutableLiveData(false)

    val repoList = mutableListOf<Repo>()
    var pageNumber = 1

    fun searchRepos(complete: (List<Repo>) -> Unit) {
        viewModelScope.launch(Dispatchers.IO) {
            val q = searchText.value.toString()
            try {
                val repoResponse = repository.searchRepos(q, pageNumber)
                withContext(Dispatchers.Main) {
                    repoResponse?.let {
                        if (it.totalCount > 0) {
                            repoList.addAll(it.items)
                            complete(repoList)
                            isRecyclerViewOn.value = true
                        }
                    }
                }
            } catch (e: Exception) {
                Log.e("SearchViewModel", "Repository searching error")
                isRecyclerViewOn.value = false
            }
        }
    }

    fun checkText(t: String, search: () -> Unit) = viewModelScope.launch {
        delay(1000L)
        if (t == searchText.value) {
            search()
        }
    }

    fun removeSearchText() {
        searchText.value = ""
    }
}