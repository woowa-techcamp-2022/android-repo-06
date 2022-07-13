package com.example.woowagithubrepositoryapp.ui.notification

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.woowagithubrepositoryapp.model.Notification
import com.example.woowagithubrepositoryapp.network.GithubService
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
            val response = GithubService.instance.getNotifications()
            val body = response.body()
            if (response.isSuccessful && body != null){
                _notifications.postValue(response.body()?.toMutableList())
            }
        }
        catch (e:Exception){
            Log.e("NotificationViewModel","getNotification error : ${e.cause.toString()}")
        }
    }
}