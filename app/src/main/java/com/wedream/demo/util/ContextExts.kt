package com.wedream.demo.util

import android.app.Service
import android.content.Context
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator

fun Context.vibrate(time: Long) {
    val vibrator = getSystemService(Service.VIBRATOR_SERVICE) as Vibrator
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        vibrator.vibrate(
            VibrationEffect.createOneShot
                (time, VibrationEffect.DEFAULT_AMPLITUDE)
        )
    } else {
        vibrator.vibrate(time)
    }
}