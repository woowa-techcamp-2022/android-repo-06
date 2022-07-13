package com.example.woowagithubrepositoryapp.utils

import android.annotation.SuppressLint
import android.util.Log
import java.lang.Math.abs
import java.text.SimpleDateFormat
import java.util.*

object TimeUtil {
    private const val SECOND = 1000
    private const val MINUTE = SECOND * 60
    private const val HOUR = MINUTE * 60
    private const val DAY = HOUR * 24
    private const val WEEK = DAY * 7

    fun getTimeData(updated: String) : String {
        try {
            val parser = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss",Locale.KOREAN)
            val currentDate = Calendar.getInstance().apply {
                timeInMillis = Date().time
            }

            val targetDate = Calendar.getInstance().apply {
                timeInMillis = parser.parse(updated)?.time ?: 0L
            }

            val difference = abs(currentDate.timeInMillis - targetDate.timeInMillis)

            return when(difference){
/*                in 0 until MINUTE -> {
                    "${(difference / SECOND).toInt()}초 전"
                }
                in MINUTE until HOUR -> {
                    "${(difference / MINUTE).toInt()}분 전"
                }*/
                in 0 until DAY -> {
                    "오늘"
                }
                else -> {
                    "${(difference / DAY).toInt()}일 전"
                }
            }
        }catch (e : Exception){
            return updated
        }
    }
}