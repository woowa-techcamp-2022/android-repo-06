package com.example.woowagithubrepositoryapp.utils

import android.app.Activity
import android.content.Intent
import android.widget.Toast
import com.example.woowagithubrepositoryapp.App

inline fun <reified A : Activity> clearTasksAndStartActivity(){
    val intent = Intent(App.instance, A::class.java).apply {
        addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
        addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
    }
    App.instance.startActivity(intent)
}

fun toastMsg(text : String){
    Toast.makeText(App.instance, text , Toast.LENGTH_SHORT).show()
}