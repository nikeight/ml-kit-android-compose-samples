package com.example.geminichatapp.util

import android.icu.util.Calendar
import java.util.Date

// For Chat Records
fun getCurrentDate(): Date = Calendar.getInstance().time

// For Chat Bubble
fun Date.getCurrentTime(): String {
    val calendar = Calendar.getInstance()
    calendar.time = this
    val hour = calendar.get(Calendar.HOUR).let { value ->
        if (value >= 10) {
            "$value"
        } else {
            "0$value"
        }
    }
    val minutes = calendar.get(Calendar.MINUTE)
    return buildString {
        append(hour)
        append(":")
        append(minutes)
    }
}

// For Map Key
fun Date.getDateAsYearMonthDay(): String {
    val calendar = Calendar.getInstance()
    calendar.time = this
    val year = calendar.get(Calendar.YEAR)
    val month = calendar.get(Calendar.MONTH)
    val date = calendar.get(Calendar.DATE)
    return buildString {
        append(year)
        append("-")
        append(month.plus(1))
        append("-")
        append(date)
    }
}

// For Stick Header
fun Date.getDateAsDayDateAndMonth(): String {
    val calendar = Calendar.getInstance()
    calendar.time = this
    val day = calendar.get(Calendar.DAY_OF_WEEK)
    val date = calendar.get(Calendar.DATE)
    val month = calendar.get(Calendar.MONTH)
    return buildString {
        append(day.getDayFromCode())
        append(",")
        append(date)
        append(" ")
        append(month)
    }
}

fun Int.getDayFromCode(): String {
    return when (this) {
        Calendar.SUNDAY -> "Sun"
        Calendar.SATURDAY -> "Sat"
        Calendar.FRIDAY -> "Fri"
        Calendar.THURSDAY -> "Thur"
        Calendar.WEDNESDAY -> "Wed"
        Calendar.TUESDAY -> "Tue"
        Calendar.MONDAY -> "Mon"
        else -> ""
    }
}