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

    val issuePage = MutableLiveData(1)
    val issueSelectState = MutableLiveData("open")
    val issueList = mutableListOf<Issue>()

    val tabSelectState = MutableLiveData("Issue")

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
        viewModelScope.launch(Dispatchers.IO){
            isProgressOn.postValue(true)
            val result = repository.getNotifications(notificationPage)
            when {
                result.isSuccess -> {
                    val notificationList = result.getOrDefault(mutableListOf())
                    if(notificationList.size != 0) {
                        withContext(Dispatchers.Main){
                            if (notificationPage == 1) {
                                _notifications.value?.clear()
                            }
                            _notifications.value?.addAll(notificationList)
                            _notifications.value = _notifications.value
                            isNotificationDataLoading = Constants.DataLoading.BEFORE
                            notificationPage++
                        }
                    }
                    else {
                        isNotificationDataLoading = Constants.DataLoading.AFTER
                    }
                }
                result.isFailure -> {
                    isNotificationDataLoading = Constants.DataLoading.AFTER
                    toastMsg("Notification 정보를 가져오지 못하였습니다.")
                }
            }
            isProgressOn.postValue(false)
        }
    }

    fun markNotificationAsRead(notification: Notification) {
        viewModelScope.launch(Dispatchers.IO) {
            val result = repository.patchNotificationThread(notification.threadId)
            when {
                result.isSuccess -> {
                    withContext(Dispatchers.Main) {
                        removeNotificationAtPosition(notification)
                    }
                }
                result.isFailure -> {
                    toastMsg("읽음 처리에 실패했습니다.")
                }
            }
        }
    }

    private fun removeNotificationAtPosition(notification: Notification) {
        _notifications.value?.remove(notification)
        _notifications.value = _notifications.value
    }
}

