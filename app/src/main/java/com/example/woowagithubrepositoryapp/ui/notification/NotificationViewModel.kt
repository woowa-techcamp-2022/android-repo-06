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

class NotificationViewModel : ViewModel() {
    private val _notifications = MutableLiveData<MutableList<Notification>>()
    val notifications : LiveData<MutableList<Notification>> = _notifications

    init {
        getNotifications()
    }

    fun getNotifications() = viewModelScope.launch {
        try{
            val response = GithubRepository.instance.getNotifications()
            val body = response.body()
            if (response.isSuccessful && body != null){
                _notifications.postValue(body.toMutableList())
            }
        }
        catch (e:Exception){
            Log.e("NotificationViewModel","getNotification error : ${e.cause.toString()}")
        }
    }
    fun markNotificationAsRead(threadId: String) = viewModelScope.launch {
        try {
            val response = GithubRepository.instance.patchNotificationThread(threadId)
            val body = response.body()
            if (response.isSuccessful && body != null){
                Log.d("markNotification",body?.message.toString())
            }
        } catch (e: Exception){
            Log.e("NotificationViewModel","markNotification error : ${e.cause.toString()}")

        }
    }
}