package com.zhigaras.viewsandanimations

import android.icu.util.Calendar
import android.icu.util.TimeZone

fun main() {
    val calendar = Calendar.getInstance()
    calendar.timeInMillis = 0L
    calendar.timeZone = TimeZone.GMT_ZONE
    println(calendar.time)
}