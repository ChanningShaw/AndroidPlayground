package com.wedream.demo.app

import android.Manifest
import android.app.Activity
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat


object PermissionHelper {

    val PERMISSION_FILE: String = Manifest.permission.WRITE_EXTERNAL_STORAGE
    const val PERMISSION_RECORD: String = Manifest.permission.RECORD_AUDIO
    val PERMISSION_READ: String = Manifest.permission.READ_EXTERNAL_STORAGE

    private val requestPermissions = arrayOf(PERMISSION_RECORD)

    fun requestPermission(activity: Activity, callback: PermissionGrant) {
        val requestList: MutableList<String> = ArrayList()
        for (i in requestPermissions.indices) {
            if (ContextCompat.checkSelfPermission(
                    activity,
                    requestPermissions[i]
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                requestList.add(requestPermissions[i])
            }
        }
        if (requestList.isEmpty()) { //未授予的权限为空，表示都授予了
            callback.onPermissionGranted()
        } else { //请求权限方法
            ActivityCompat.requestPermissions(activity, requestList.toTypedArray(), 1)
        }

    }

    interface PermissionGrant {
        fun onPermissionGranted()
    }
}