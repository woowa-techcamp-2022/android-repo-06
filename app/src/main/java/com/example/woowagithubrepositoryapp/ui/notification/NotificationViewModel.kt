package com.example.woowagithubrepositoryapp.ui.notification

import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.woowagithubrepositoryapp.model.Notification
import com.example.woowagithubrepositoryapp.network.GithubService
import com.example.woowagithubrepositoryapp.repository.GithubRepository
import com.example.woowagithubrepositoryapp.utils.Prefs
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class NotificationViewModel : ViewModel() {
    private val _notifications = MutableLiveData<MutableList<Notification>>()
    val notifications : LiveData<MutableList<Notification>> = _notifications

    init {
        getNotifications()
    }

    fun getNotifications() {
        viewModelScope.launch {
            _notifications.postValue(GithubRepository.instance.getNotifications())
        }
    }

    suspend fun markNotificationAsRead(threadId: String) : Boolean =
        GithubRepository.instance.patchNotificationThread(threadId)

    fun removeNotificationAtPosition(position : Int){
        _notifications.value?.removeAt(position)
        _notifications.value = _notifications.value
    }
}