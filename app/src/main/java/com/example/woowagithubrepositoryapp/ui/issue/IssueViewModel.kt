package com.example.woowagithubrepositoryapp.ui.issue

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.woowagithubrepositoryapp.model.Issue
import com.example.woowagithubrepositoryapp.repository.GithubRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class IssueViewModel(private val repository: GithubRepository) : ViewModel() {
    val pageNumber = MutableLiveData(1)
    val selectState = MutableLiveData("open")
    val issueList = mutableListOf<Issue>()

    fun getIssues(complete: (List<Issue>) -> Unit){
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val issues = repository.getUserIssues(selectState.value!!,pageNumber.value!!)
                withContext(Dispatchers.Main){
                    if(pageNumber.value == 1)
                        issueList.clear()
                    issueList.addAll(issues)
                    complete(issueList)
                }
            } catch (e: Exception) {
                Log.e("IssueViewModel", "getIssues error")
            }
        }
    }
}