package com.example.woowagithubrepositoryapp.ui.search

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.woowagithubrepositoryapp.model.Repo
import com.example.woowagithubrepositoryapp.repository.GithubRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class SearchViewModel(private val repository: GithubRepository) : ViewModel() {

    val searchText = MutableLiveData("")
    val recyclerViewOn = MutableLiveData(false)
    val repoList = mutableListOf<Repo>()
    var pageNumber = 1

    fun searchRepos(complete: (List<Repo>) -> Unit) {
        viewModelScope.launch(Dispatchers.IO) {
            val q = searchText.value.toString()
            try {
                val response = repository.searchRepos(q, pageNumber)
                val body = response.body()
                if (response.isSuccessful && body != null) {
                    withContext(Dispatchers.Main) {
                        if (body.totalCount == 0) {
                            recyclerViewOn.value = false
                        } else {
                            repoList.addAll(body.items)
                            complete(repoList)
                            recyclerViewOn.value = true
                        }
                    }
                }
            } catch (e: Exception) {
                Log.e("SearchViewModel", "Repository searching error")
                recyclerViewOn.value = false
            }
        }
    }

    fun removeSearchText() {
        searchText.value = ""
    }
}