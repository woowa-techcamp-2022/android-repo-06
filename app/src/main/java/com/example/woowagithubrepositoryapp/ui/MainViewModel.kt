package com.example.woowagithubrepositoryapp.ui

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.woowagithubrepositoryapp.App
import com.example.woowagithubrepositoryapp.model.Issue
import com.example.woowagithubrepositoryapp.model.Notification
import com.example.woowagithubrepositoryapp.repository.GithubRepository
import com.example.woowagithubrepositoryapp.utils.Constants.DataLoading
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainViewModel(private val repository: GithubRepository) : ViewModel() {

    val issuePage = MutableLiveData(1)
    val issueSelectState = MutableLiveData("open")
    val issueList = mutableListOf<Issue>()

    private val _notifications = MutableLiveData<MutableList<Notification>>()
    val notifications : LiveData<MutableList<Notification>> = _notifications
    private var notificationPage = 1
    var isNotificationDataLoading : DataLoading = DataLoading.BEFORE

    init {
        _notifications.value = mutableListOf()
    }

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

    fun getIssues(complete: (List<Issue>) -> Unit) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val issues = repository.getUserIssues(issueSelectState.value!!, issuePage.value!!)
                withContext(Dispatchers.Main) {
                    if (issuePage.value == 1)
                        issueList.clear()
                    issueList.addAll(issues)
                    complete(issueList)
                }
            } catch (e: Exception) {
                Log.e("IssueViewModel", "getIssues error")
            }
        }
    }

    fun getNotifications() {
        viewModelScope.launch {
            val notifications = repository.getNotifications(notificationPage)
            if(notifications.size != 0){
                _notifications.value?.addAll(notifications)
                _notifications.value = _notifications.value
                isNotificationDataLoading = DataLoading.BEFORE
                notificationPage++
            }else {
                isNotificationDataLoading = DataLoading.AFTER
                //더 이상 받아올 알림이 없기 때문에 페이지를 증가시키지 않는다.
                //무한 스크롤을 막는 코드가 필요
            }
        }
    }

    fun markNotificationAsRead(notification : Notification) {
        viewModelScope.launch {
            val marked = repository.patchNotificationThread(notification.threadId)
            if (marked) {
                removeNotificationAtPosition(notification)
            } else {
                //서버에서 읽음 처리가 되지 않은 경우 어떻게 처리할 것인가?
            }
        }
    }

    private fun removeNotificationAtPosition(notification : Notification){
        _notifications.value?.remove(notification)
        _notifications.value = _notifications.value
    }
}

