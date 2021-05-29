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
import com.wedream.demo.database.greenDao.DaoMaster
import com.wedream.demo.database.greenDao.DaoOpenHelper
import com.wedream.demo.database.greenDao.DaoSession

class MyApplication : Application() {

    companion object {
        const val APP_SP_NAME = "app_sp"
        const val KEY_LAST_RESUME_ACTIVITY = "key_last_resume_activity"
        const val DB_NAME = "investment.db"
        val daoSession: DaoSession by lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED) {
            val daoMaster = DaoMaster(DaoOpenHelper(instance, DB_NAME, null).writableDb)
            daoMaster.newSession()
        }
    }

    private lateinit var appSp: SharedPreferences

    override fun onCreate() {
        super.onCreate()
        MMKV.initialize(this)
        val handler = MyCrashHandler()
//        Thread.setDefaultUncaughtExceptionHandler(handler)
        ApplicationHolder.instance = this
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

    fun getDaoSession(): DaoSession {
        return daoSession
    }

    fun cleanLastResumeActivity() {
        appSp.edit().remove(KEY_LAST_RESUME_ACTIVITY).apply()
    }
}

object ApplicationHolder {
    lateinit var instance: MyApplication
}