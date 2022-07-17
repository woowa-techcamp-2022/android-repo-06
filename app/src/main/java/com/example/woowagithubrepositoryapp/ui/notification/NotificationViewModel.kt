package com.example.woowagithubrepositoryapp.ui.notification

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.woowagithubrepositoryapp.utils.Constants.DataLoading
import com.example.woowagithubrepositoryapp.model.Notification
import com.example.woowagithubrepositoryapp.repository.GithubRepository
import com.example.woowagithubrepositoryapp.utils.Constants
import kotlinx.coroutines.launch

class NotificationViewModel(private val repository: GithubRepository) : ViewModel() {

    private val _notifications = MutableLiveData<MutableList<Notification>>()
    val notifications : LiveData<MutableList<Notification>> = _notifications
    private var page = 1
    var isDataLoading : DataLoading = DataLoading.BEFORE

    init {
        _notifications.value = mutableListOf()
        getNotifications()
    }

    fun getNotifications() {
        viewModelScope.launch {
            val notifications = repository.getNotifications(page)
            if(notifications.size != 0){
                _notifications.value?.addAll(notifications)
                _notifications.value = _notifications.value
                isDataLoading = DataLoading.BEFORE
                page++
            }else {
                isDataLoading = DataLoading.AFTER
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