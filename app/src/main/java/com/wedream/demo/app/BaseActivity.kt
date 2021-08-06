package com.wedream.demo.app

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.wedream.demo.inject.Delegate
import com.wedream.demo.util.LogUtils.log

open class BaseActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        inject()
    }

    override fun onWindowFocusChanged(hasFocus: Boolean) {
        super.onWindowFocusChanged(hasFocus)
        if (hasFocus) {
            MyApplication.appResumeTime = System.currentTimeMillis()
        }
    }

    private fun inject() {
        val clazz = this.javaClass
        val fields = clazz.declaredFields
        for (field in fields) {
            if (Delegate::class.java.isAssignableFrom(field.type)) {
                field.isAccessible = true
            }
        }
    }
}