package com.example.woowagithubrepositoryapp.ui.search

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.woowagithubrepositoryapp.model.Repo
import com.example.woowagithubrepositoryapp.repository.GithubRepository
import com.example.woowagithubrepositoryapp.utils.toastMsg
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SearchViewModel(private val repository: GithubRepository) : ViewModel() {

    val searchText = MutableLiveData("")
    val isRecyclerViewOn = MutableLiveData(false)
    val isSearchBarActive = MutableLiveData(false)
    val isProgressOn = MutableLiveData(false)

    val repoList = mutableListOf<Repo>()
    var pageNumber = 1

    var searchType = SearchType.SEARCH
    var scrollPosition = 0

    fun searchRepos(complete: (List<Repo>) -> Unit) {
        viewModelScope.launch {
            isProgressOn.postValue(true)
            val q = searchText.value.toString()
            val result = repository.searchRepos(q, pageNumber)
            when {
                result.isSuccess -> {
                    val repos = result.getOrNull()
                    repos?.let {
                        if (it.totalCount > 0) {
                            repoList.addAll(it.items)
                            complete(repoList)
                            isRecyclerViewOn.value = true
                        } else {
                            toastMsg("검색 결과가 없습니다.")
                            isRecyclerViewOn.value = false
                        }
                    }
                }
                else -> {
                    toastMsg("검색에 실패하였습니다.")
                    isRecyclerViewOn.value = false
                }
            }
            isProgressOn.postValue(false)
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

enum class SearchType {
    CREATE, SEARCH
}