package com.example.woowagithubrepositoryapp.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.woowagithubrepositoryapp.App
import com.example.woowagithubrepositoryapp.model.Issue
import com.example.woowagithubrepositoryapp.model.Notification
import com.example.woowagithubrepositoryapp.repository.GithubRepository
import com.example.woowagithubrepositoryapp.utils.Constants
import com.example.woowagithubrepositoryapp.utils.toastMsg
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainViewModel(private val repository: GithubRepository) : ViewModel() {

    var currentTabState = Constants.MainTab.ISSUE

    val issuePage = MutableLiveData(1)
    val issueSelectState = MutableLiveData("open")
    val issueList = mutableListOf<Issue>()

    private val _notifications = MutableLiveData<MutableList<Notification>>()
    val notifications: LiveData<MutableList<Notification>> = _notifications
    private var notificationPage = 1
    var isNotificationDataLoading: Constants.DataLoading = Constants.DataLoading.BEFORE

    init {
        _notifications.value = mutableListOf()
    }

    val isProgressOn = MutableLiveData(false)

    fun getUserData(complete: () -> Unit) {
        viewModelScope.launch(Dispatchers.IO) {
            isProgressOn.postValue(true)
            val result = repository.getUserData()
            withContext(Dispatchers.Main) {
                when {
                    result.isSuccess -> {
                        App.user = result.getOrNull()
                        complete()
                    }
                    result.isFailure -> {
                        toastMsg("사용자의 정보를 불러오지 못하였습니다.")
                    }
                }
            }
            isProgressOn.postValue(false)
        }
    }

    fun getIssues(complete: (List<Issue>) -> Unit) {
        viewModelScope.launch(Dispatchers.IO) {
            isProgressOn.postValue(true)
            val result = repository.getUserIssues(issueSelectState.value!!, issuePage.value!!)
            withContext(Dispatchers.Main) {
                if (issuePage.value == 1)
                    issueList.clear()
                when {
                    result.isSuccess -> {
                        issueList.addAll(result.getOrDefault(listOf()))
                        complete(issueList)
                    }
                    result.isFailure -> {
                        toastMsg("Issue 정보를 가져오지 못하였습니다.")
                    }
                }
            }
            isProgressOn.postValue(false)
        }
    }

    fun getNotifications() {
        viewModelScope.launch {
            isProgressOn.postValue(true)
            val notifications = repository.getNotifications(notificationPage)
            if (notifications.size != 0) {
                if (notificationPage == 1) {
                    _notifications.value?.clear()
                }
                _notifications.value?.addAll(notifications)
                _notifications.value = _notifications.value
                isNotificationDataLoading = Constants.DataLoading.BEFORE
                notificationPage++
            } else {
                isNotificationDataLoading = Constants.DataLoading.AFTER
                //더 이상 받아올 알림이 없기 때문에 페이지를 증가시키지 않는다.
                //무한 스크롤을 막는 코드가 필요
            }
            isProgressOn.postValue(false)
        }
    }

    fun markNotificationAsRead(notification: Notification) {
        viewModelScope.launch {
            val marked = repository.patchNotificationThread(notification.threadId)
            if (marked) {
                removeNotificationAtPosition(notification)
            } else {
                //서버에서 읽음 처리가 되지 않은 경우 어떻게 처리할 것인가?
            }
        }
    }

    private fun removeNotificationAtPosition(notification: Notification) {
        _notifications.value?.remove(notification)
        _notifications.value = _notifications.value
    }
}

