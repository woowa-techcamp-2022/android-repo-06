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
    private var page = 1

    init {
        _notifications.value = mutableListOf()
        getNotifications()
    }

    fun getNotifications() = viewModelScope.launch {
        try{
            val response = GithubRepository.instance.getNotifications(page)
            val body = response.body()
            if (response.isSuccessful && body != null){
                if(response.body()?.size != 0){
                    _notifications.value?.addAll(body.toMutableList())
                    _notifications.value = _notifications.value
                    page++
                }else {
                    //더 이상 받아올 알림이 없기 때문에 페이지를 증가시키지 않는다.
                    //무한 스크롤을 막는 코드가 필요
                }
            }
        }
        catch (e:Exception){
            Log.e("NotificationViewModel","getNotification error : ${e.cause.toString()}")
        }
    }
    suspend fun markNotificationAsRead(threadId: String) : Boolean {
        try {
            val response = GithubRepository.instance.patchNotificationThread(threadId)
            if (response.isSuccessful){
                return true
            }
        } catch (e: Exception){
            Log.e("NotificationViewModel","markNotification error : ${e.cause.toString()}")
        }
        return false
    }
    fun removeNotificationAtPosition(position : Int){
        _notifications.value?.removeAt(position)
        _notifications.value = _notifications.value
    }
}