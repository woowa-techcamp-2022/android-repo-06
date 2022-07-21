package com.example.woowagithubrepositoryapp.utils

import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.abs

object TimeUtil {
    private const val SECOND = 1000
    private const val MINUTE = SECOND * 60
    private const val HOUR = MINUTE * 60
    private const val DAY = HOUR * 24

    fun getTimeData(updated: String): String {
        try {
            val parser = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.KOREAN)
            val currentDate = Calendar.getInstance().apply {
                timeInMillis = Date().time
            }

            val targetDate = Calendar.getInstance().apply {
                timeInMillis = parser.parse(updated)?.time ?: 0L
            }

            return when (val difference = abs(currentDate.timeInMillis - targetDate.timeInMillis)) {
                in 0 until DAY -> {
                    "오늘"
                }
                else -> {
                    "${(difference / DAY).toInt()}일 전"
                }
            }
        } catch (e: Exception) {
            return updated
        }
    }
}