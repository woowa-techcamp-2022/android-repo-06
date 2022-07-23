package com.example.woowagithubrepositoryapp.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.woowagithubrepositoryapp.App
import com.example.woowagithubrepositoryapp.data.repository.GithubRepository
import com.example.woowagithubrepositoryapp.model.Issue
import com.example.woowagithubrepositoryapp.model.Notification
import com.example.woowagithubrepositoryapp.ui.common.DataLoading
import com.example.woowagithubrepositoryapp.ui.common.IssueLoadType
import com.example.woowagithubrepositoryapp.ui.common.TabSelectState
import com.example.woowagithubrepositoryapp.utils.toastMsg
import kotlinx.coroutines.launch

class MainViewModel(private val repository: GithubRepository) : ViewModel() {

    var issuePage = 1
    val issueSelectState = MutableLiveData("open")
    val issueList = mutableListOf<Issue>()
    val issueRefreshState = MutableLiveData(true)
    var issueLoadType = IssueLoadType.CREATE

    val tabSelectState = MutableLiveData(TabSelectState("Issue", false))

    private val _notifications = MutableLiveData<MutableList<Notification>>(mutableListOf())
    val notifications: LiveData<MutableList<Notification>> = _notifications
    val markingNotificationThreadIds = mutableListOf<String>()
    var notificationPage = 1
    var isNotificationDataLoading = DataLoading.BEFORE

    val isProgressOn = MutableLiveData(false)

    fun getUserData(complete: () -> Unit) {
        viewModelScope.launch {
            isProgressOn.postValue(true)
            val result = repository.getUserData()
            when {
                result.isSuccess -> {
                    App.user = result.getOrNull()
                    complete()
                }
                result.isFailure -> {
                    toastMsg("사용자의 정보를 불러오지 못하였습니다.")
                }
            }
            isProgressOn.postValue(false)
        }
    }

    fun getIssues(complete: (List<Issue>) -> Unit) {
        viewModelScope.launch {
            isProgressOn.postValue(true)
            val result = repository.getUserIssues(issueSelectState.value!!, issuePage)
            if (issuePage == 1)
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
            isProgressOn.postValue(false)
        }
    }

    fun getNotifications() {
        viewModelScope.launch {
            isProgressOn.postValue(true)
            isNotificationDataLoading = DataLoading.NOW
            val result = repository.getNotifications(notificationPage)
            when {
                result.isSuccess -> {
                    val notificationList = result.getOrDefault(mutableListOf())
                    if (notificationList.size != 0) {
                        if (notificationPage == 1) {
                            _notifications.value?.clear()
                        }
                        _notifications.value?.addAll(notificationList)
                        _notifications.value = _notifications.value
                        isNotificationDataLoading = DataLoading.BEFORE
                        notificationPage++
                    } else {
                        isNotificationDataLoading = DataLoading.AFTER
                    }
                }
                result.isFailure -> {
                    isNotificationDataLoading = DataLoading.AFTER
                    toastMsg("Notification 정보를 가져오지 못하였습니다.")
                }
            }
            isProgressOn.postValue(false)
        }
    }

    fun markNotification(){
        viewModelScope.launch {
            repository.patchNotificationThread(markingNotificationThreadIds)
            markingNotificationThreadIds.clear()
        }
    }

    fun removeNotification(notification: Notification) {
        markingNotificationThreadIds.add(notification.threadId)
        _notifications.value?.remove(notification)
        _notifications.value = _notifications.value
    }


    fun refreshNotifications() {
        viewModelScope.launch {
            if (isNotificationDataLoading != DataLoading.NOW) {
                if (markingNotificationThreadIds.isNotEmpty()) {
                    repository.patchNotificationThread(markingNotificationThreadIds)
                    markingNotificationThreadIds.clear()
                }
                _notifications.value = mutableListOf()
                notificationPage = 1
                getNotifications()
            }
        }
    }

    fun refreshIssues() {
        issueList.clear()
        issuePage = 1
        issueLoadType = IssueLoadType.CREATE
        issueRefreshState.value = issueRefreshState.value == false
    }
}
