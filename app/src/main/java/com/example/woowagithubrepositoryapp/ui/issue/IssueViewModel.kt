package com.example.woowagithubrepositoryapp.ui.issue

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.woowagithubrepositoryapp.model.Issue
import com.example.woowagithubrepositoryapp.repository.GithubRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class IssueViewModel : ViewModel() {
    var pageNumber = 1
    var selectState = "open"

    fun getIssues(filter: String, state: String, page: Int, complete: (List<Issue>) -> Unit) =
        viewModelScope.launch {
            try {
                val response = GithubRepository.instance.getUserIssues(
                    filter, state, page
                )
                val body = response.body()
                if (response.isSuccessful && body != null) {
                    withContext(Dispatchers.Main){
                        complete(body)
                    }
                }
            } catch (e: Exception) {
                Log.e("IssueViewModel", "getIssues error")
            }
        }
}