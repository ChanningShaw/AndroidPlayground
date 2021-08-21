package com.wedream.demo.util

import java.text.SimpleDateFormat
import java.util.*

object TimeUtils {
    fun getHourAndMinutes(calendar: Calendar): String {
        val h = calendar.get(Calendar.HOUR_OF_DAY)
        val hs = if (h >= 10) {
            h.toString()
        } else {
            "0$h"
        }
        val m = calendar.get(Calendar.MINUTE)
        val ms = if (m > 10) {
            m.toString()
        } else {
            "0$m"
        }
        return "$hs:$ms"
    }

    fun getTimeString(calendar: Calendar): String {
        val df = SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss")
        return df.format(calendar.time)
    }
}

val Long.format: String
    get() = run {
        val date = Date(this)
        val strDateFormat = "yyyy-MM-dd HH:mm:ss:SSS"
        val sdf = SimpleDateFormat(strDateFormat)
        sdf.format(date)
    }

val curTimeString: String
    get() = System.currentTimeMillis().format