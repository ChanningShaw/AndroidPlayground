package com.wedream.demo.util

import android.content.pm.PackageManager
import com.wedream.demo.app.ApplicationHolder
import kotlin.math.roundToInt

object AndroidUtils {
    /**
     * 获取当前本地apk的版本
     *
     * @return versionCode
     */
    fun getVersionCode(): Int {
        var versionCode = 0
        try {
            // 获取软件版本号，对应AndroidManifest.xml下android:versionCode
            versionCode = ApplicationHolder.instance.packageManager
                .getPackageInfo(ApplicationHolder.instance.packageName, 0).versionCode
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
        }
        return versionCode
    }

    fun dip2pix(dip: Int): Int {
        return (ApplicationHolder.instance.resources.displayMetrics.density * dip).roundToInt()
    }
}