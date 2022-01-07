package com.wedream.demo.app

import android.app.Activity
import android.app.Application
import android.content.ComponentName
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import com.tencent.mmkv.MMKV
import com.wedream.demo.MainActivity
import com.wedream.demo.app.ApplicationHolder.instance
import com.wedream.demo.util.LogUtils.log
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class MyApplication : Application() {

    private lateinit var appSp: SharedPreferences
    private var currentResumeActivity: Activity? = null

    companion object {
        const val APP_SP_NAME = "app_sp"
        const val KEY_LAST_RESUME_ACTIVITY = "key_last_resume_activity"
        var appStartTime = 0L
        var appResumeTime = 0L
            set(value) {
                if (field == 0L) {
                    field = value
                    log { "app start took ${appResumeTime - appStartTime} ms" }
                }
            }
    }

    override fun attachBaseContext(base: Context) {
        super.attachBaseContext(base)
        instance = this
        appStartTime = System.currentTimeMillis()
    }

    override fun onCreate() {
        super.onCreate()
        MMKV.initialize(this)
        val handler = CrashHandler(this)
        appSp = getSharedPreferences("app_sp", Context.MODE_PRIVATE)
        DeviceParams.init(this)
        registerActivityLifecycleCallbacks(object : ActivityLifecycleCallbacks {
            override fun onActivityPaused(activity: Activity) {
            }

            override fun onActivityStarted(activity: Activity) {
            }

            override fun onActivityDestroyed(activity: Activity) {
            }

            override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {
            }

            override fun onActivityStopped(activity: Activity) {
            }

            override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
            }

            override fun onActivityResumed(activity: Activity) {
                currentResumeActivity = activity
                if (activity is CategoryActivity) {
                    return
                }
                val value = "${activity.componentName.packageName}/${activity.componentName.className}"
                appSp.edit().putString(KEY_LAST_RESUME_ACTIVITY, value).apply()
            }
        })
    }

    fun getLastResumeActivity(): ComponentName {
        val componentString = appSp.getString(KEY_LAST_RESUME_ACTIVITY, "")
        return if (componentString.isNullOrEmpty()) {
            ComponentName(this, MainActivity::class.java)
        } else {
            val splits = componentString.split("/")
            ComponentName(splits[0], splits[1])
        }
    }

    fun getCurrentResumeActivity() : Activity? {
        return currentResumeActivity
    }

    fun cleanLastResumeActivity() {
        appSp.edit().remove(KEY_LAST_RESUME_ACTIVITY).apply()
    }
}

object ApplicationHolder {
    lateinit var instance: MyApplication
}